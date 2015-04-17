package controller;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import utils.CommonUtils;

import javax.swing.JPopupMenu;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.Color;

/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * DNBGen.java
 * 2015年4月15日
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */

/**
 * 实现功能： DNB的检测分析主程序
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2015年4月15日	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
public class DNBGen extends JFrame {
	
	private  final  String classPath = this.getClass().getResource("/").getPath();
	private static final Logger log = Logger.getLogger(DNBGen.class);
	private JPanel contentPane;
	private JTextField txtWorkSpace;
	private JFileChooser fileChooser = new JFileChooser() ;
	private JTextField txtCaseFile;
	private JTextField txtControlFile;
	private JTextField txtPeriod;
	private JTextField txtPeriodSampleCount;
	private JTextField txtSdThreshold;
	private JTextField txtPccOutAmount;
	private JTextField txtCores;
	private JTextField txtClusterH;
	private CIGrowthPane pCIGrowth = null;
	private DNBVisualPane pDNBVisual  = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DNBGen frame = new DNBGen();
					frame.setVisible(true);
				} catch (Exception e) {
					log.error("new frame failed!", e);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DNBGen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 629, 721);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		setMenu(menuBar);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		dnbGenTab(tabbedPane);
		
		ciGrowthTab(tabbedPane);
		
		pDNBVisual = new DNBVisualPane();
		pDNBVisual.setBackground(Color.WHITE);
		tabbedPane.addTab("DNB可视化", null, pDNBVisual, null);
		//右键菜单=>另存为,保存图片
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(pDNBVisual, popupMenu);
		
		JMenuItem mntmSaveAs = new JMenuItem("另存为");
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					File savedFile  =  saveFile(pDNBVisual);
					if (savedFile == null ) {
						return;
					}
					BufferedImage image = new BufferedImage(pDNBVisual.getWidth(), pDNBVisual.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
					Graphics2D g = image.createGraphics();
					pDNBVisual.paint(g);
//					g.drawImage(image, pDNBVisual.getWidth(), pDNBVisual.getHeight(), null);
					ImageIO.write(image,"png", savedFile);
//					pDNBVisual.save(savedFile);
				} catch (IOException e1) {
					log.error("create file error!", e1);
				}
			}
		});
		popupMenu.add(mntmSaveAs);
	}

	/**
	 * 绘制DNB的综合指数(CI)折线图,并可以另存到文件中
	 * @param tabbedPane
	 */
	public void ciGrowthTab(JTabbedPane tabbedPane) {
		try {
			pCIGrowth = new CIGrowthPane();
		} catch (IOException e) {
			log.error("draw ci growth failed!", e);
		}
		tabbedPane.addTab("综合指数折线图", null, pCIGrowth, null);
		
		//右键菜单=>另存为,保存图片
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(pCIGrowth, popupMenu);
		
		JMenuItem mntmSaveAs = new JMenuItem("另存为");
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					File savedFile  =  saveFile(pCIGrowth);
					pCIGrowth.save(savedFile);
				} catch (IOException e1) {
					log.error("create file error!", e1);
				}
			}
		});
		popupMenu.add(mntmSaveAs);
	}

	/**
	 * 参数输入(生成DNB)tab相关的方法
	 * @param tabbedPane
	 */
	public void dnbGenTab(JTabbedPane tabbedPane) {
		JPanel pDNBGen = new JPanel();
		tabbedPane.addTab("参数输入", null, pDNBGen, null);
		
		JLabel lblWorkSpace = new JLabel("工作目录:");
		
		txtWorkSpace = new JTextField();
		txtWorkSpace.setColumns(10);
		
		final JButton btnWorkSpace = new JButton("选择");
		btnWorkSpace.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser dirChooser = new JFileChooser();
				dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int operate = dirChooser.showOpenDialog(btnWorkSpace);
				if (operate == JFileChooser.APPROVE_OPTION) {
					txtWorkSpace.setText(dirChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		
		
		final JButton btnCase = new JButton("选择");
		btnCase.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int operate = fileChooser.showOpenDialog(btnCase);
				if (operate == JFileChooser.APPROVE_OPTION) {
					txtCaseFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		final JButton btnControl = new JButton("选择");
		btnControl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int operate = fileChooser.showOpenDialog(btnControl);
				if (operate == JFileChooser.APPROVE_OPTION) {
					txtControlFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		JLabel lblCase = new JLabel("Case文件:");
		JLabel lblControl = new JLabel("Control文件:");
		JLabel lblPeriod= new JLabel("总时期数(Period): ");
		JLabel lblPeriodSampleCount= new JLabel("每个时期样本数: ");
		JLabel lblSdThreshold= new JLabel("标准差筛选阈值: ");
		JLabel lblClusterH= new JLabel("层次聚类剪枝H值: ");
		JLabel lblPccOutAmount= new JLabel("模块间的需要计算的边数: ");
		JLabel lblCores_1= new JLabel("CPU加速所使用的核心数: ");
		
		txtCaseFile = new JTextField();
		txtControlFile = new JTextField();
		txtPeriod= new JTextField();
		txtPeriod.setText("5");
		txtPeriodSampleCount= new JTextField();
		txtPeriodSampleCount.setText("5");
		txtSdThreshold= new JTextField();
		txtSdThreshold.setText("0.001");
		txtClusterH= new JTextField();
		txtClusterH.setText("0.75");
		txtPccOutAmount= new JTextField();
		txtPccOutAmount.setText("50");
		txtCores= new JTextField();
		txtCores.setText("6");

		txtCaseFile.setColumns(10);
		txtControlFile.setColumns(10);
		txtPeriod.setColumns(10);
		txtPeriodSampleCount.setColumns(10);
		txtSdThreshold.setColumns(10);
		txtClusterH.setColumns(10);
		txtPccOutAmount.setColumns(10);
		txtCores.setColumns(10);
		
		JButton btnDnbgen = new JButton("生成DNB");
		btnDnbgen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dnbGenMain();
			}
		});
		
		dnbGenLayout(pDNBGen, lblWorkSpace, lblCase, lblControl, lblPeriod,
				lblPeriodSampleCount, lblSdThreshold, lblClusterH,
				lblPccOutAmount, lblCores_1,
				btnWorkSpace, btnCase, btnControl, btnDnbgen);
	}

	

	/**
	 * add menu for application
	 * @param menuBar menu bar
	 */
	private void setMenu(JMenuBar menuBar) {
		//File
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		JMenuItem miOpen = new JMenuItem("Open");
		JMenuItem miClose = new JMenuItem("Close");
		mnFile.add(miOpen);
		mnFile.add(miClose);
		
		//Help
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		JMenuItem miAbout = new JMenuItem("About");
		mnHelp.add(miAbout);
		
	}

	/**
	 * the main method of DNB generate
	 */
	public void dnbGenMain() {
		String corePath = classPath + "core/";
		String addExeModCmd = "bash " + corePath + "addExeModAndRemoveOldFiles.sh " + corePath;
		log.info("addExeModCmd: " + addExeModCmd);
		CommonUtils.execShellCmd(addExeModCmd);

		String basePath = txtWorkSpace.getText() + File.separator;
		String caseFilePath = txtCaseFile.getText();
		String controlFilePath = txtControlFile.getText();
		String periodCount = txtPeriod.getText();
		String periodSampleCount = txtPeriodSampleCount.getText();
		String featuresSdThreshold = txtSdThreshold.getText();
		String clusterHclustH = txtClusterH.getText();
		String pccOutAmount = txtPccOutAmount.getText();
		String cores = txtCores.getText();

		StringBuffer gdmCmd = new StringBuffer();
		gdmCmd.append(classPath).append("core/gdm4Par.R ")
				.append(" -p ").append(basePath)
				.append("  --case.file.path  ").append(caseFilePath)
				.append("  --period.count   ").append(periodCount)
				.append("  --period.sample.count  ")
				.append(periodSampleCount)
				.append(" --features.sd.threshold  ")
				.append(featuresSdThreshold)
				.append(" --cluster.hclust.h  ").append(clusterHclustH)
				.append(" --pcc.out.amount  ").append(pccOutAmount)
				.append(" --cores ").append(cores);
		if (!controlFilePath.isEmpty()) {
			gdmCmd.append("  --control.file.path  ").append(
					controlFilePath);
		}

		log.info("gdmCmd:" + gdmCmd.toString());
		CommonUtils.execShellCmd(gdmCmd.toString());

		// store cores, periodCount and periodSampleCount into file
		String propPath = classPath + "tempVariables.properties";
		CommonUtils.storeValueByKeyFromConfig("cores", cores, propPath);
		CommonUtils.storeValueByKeyFromConfig("period.count", periodCount, propPath);
		CommonUtils.storeValueByKeyFromConfig("period.sample.count", periodSampleCount, propPath);
		CommonUtils.storeValueByKeyFromConfig("work.space", basePath, propPath);
	}
	
	/**
	 * DNBGen TabbedPane Layout
	 * @param jpDNBGen
	 * @param lblWorkSpace
	 * @param lblCase
	 * @param lblControl
	 * @param lblPeriod
	 * @param lblPeriodSampleCount
	 * @param lblSdThreshold
	 * @param lblClusterH
	 * @param lblPccOutAmount
	 * @param lblCores
	 * @param btnWorkSpace
	 * @param btnCase
	 * @param btnControl
	 * @param btnDnbgen
	 */
	private void dnbGenLayout(JPanel jpDNBGen, JLabel lblWorkSpace,
			JLabel lblCase, JLabel lblControl, JLabel lblPeriod,
			JLabel lblPeriodSampleCount, JLabel lblSdThreshold,
			JLabel lblClusterH, JLabel lblPccOutAmount, JLabel lblCores,
			JButton btnWorkSpace, JButton btnCase, JButton btnControl,
			JButton btnDnbgen) {
		GroupLayout gl_jpDNBGen = new GroupLayout(jpDNBGen);
		gl_jpDNBGen.setHorizontalGroup(
			gl_jpDNBGen.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jpDNBGen.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_jpDNBGen.createSequentialGroup()
							.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_jpDNBGen.createSequentialGroup()
									.addComponent(lblWorkSpace)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtWorkSpace, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_jpDNBGen.createSequentialGroup()
									.addComponent(lblCase)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtCaseFile, 220, 220, 220))
								.addGroup(gl_jpDNBGen.createSequentialGroup()
									.addComponent(lblControl)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(txtControlFile, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)))
							.addGap(35)
							.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.LEADING)
								.addComponent(btnCase)
								.addComponent(btnWorkSpace)
								.addComponent(btnControl, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
							.addGap(217))
						.addGroup(gl_jpDNBGen.createSequentialGroup()
							.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_jpDNBGen.createSequentialGroup()
									.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.LEADING, false)
										.addGroup(gl_jpDNBGen.createSequentialGroup()
											.addComponent(lblSdThreshold)
											.addGap(18)
											.addComponent(txtSdThreshold, 0, 0, Short.MAX_VALUE))
										.addGroup(gl_jpDNBGen.createSequentialGroup()
											.addComponent(lblPeriod)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(txtPeriod, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)))
									.addGap(17)
									.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.LEADING)
										.addComponent(lblClusterH)
										.addComponent(lblPeriodSampleCount))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_jpDNBGen.createSequentialGroup()
											.addComponent(txtPeriodSampleCount, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED, 45, Short.MAX_VALUE))
										.addComponent(txtClusterH, GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)))
								.addGroup(Alignment.LEADING, gl_jpDNBGen.createSequentialGroup()
									.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.TRAILING)
										.addComponent(btnDnbgen)
										.addGroup(gl_jpDNBGen.createSequentialGroup()
											.addComponent(lblPccOutAmount)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(txtPccOutAmount, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)))
									.addGap(40)
									.addComponent(lblCores, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)))
							.addGap(18)
							.addComponent(txtCores, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
							.addGap(122))))
		);
		gl_jpDNBGen.setVerticalGroup(
			gl_jpDNBGen.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jpDNBGen.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblWorkSpace)
						.addComponent(txtWorkSpace, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnWorkSpace))
					.addGap(18)
					.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCase)
						.addComponent(txtCaseFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCase))
					.addGap(18)
					.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtControlFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnControl)
						.addComponent(lblControl))
					.addGap(18)
					.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPeriod)
						.addComponent(txtPeriod, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPeriodSampleCount)
						.addComponent(txtPeriodSampleCount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.BASELINE)
							.addComponent(txtSdThreshold, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblClusterH)
							.addComponent(lblSdThreshold))
						.addComponent(txtClusterH, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPccOutAmount)
						.addComponent(txtPccOutAmount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCores)
						.addComponent(txtCores, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(45)
					.addComponent(btnDnbgen)
					.addContainerGap(353, Short.MAX_VALUE))
		);
		jpDNBGen.setLayout(gl_jpDNBGen);
	}
	/**
	 * 保存文件
	 * @throws IOException 
	 * 
	 */
	public File saveFile(Component parent) throws IOException {
		int operatre = fileChooser.showSaveDialog(parent);
		if (operatre == JFileChooser.APPROVE_OPTION) {
			File saveFile = fileChooser.getSelectedFile();
			if (!saveFile.exists()) {
				saveFile.createNewFile();
			}else {
				int dlgOperate = JOptionPane.showConfirmDialog(fileChooser, "文件已存在,是否覆盖?");
				if (dlgOperate == JOptionPane.OK_OPTION) {
					saveFile.delete();
					saveFile.createNewFile();
				}
			}
			return saveFile;
		}
		return null;
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}

