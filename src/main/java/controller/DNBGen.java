package controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.NodeShape;
import model.NodeType;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import utils.CommonUtils;
import utils.Constants;
import utils.DnbUtils;
import utils.TempVar;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

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
	
	/** */
	private static final long serialVersionUID = 496091640493909358L;
	private  final  String classPath = new File(this.getClass().getResource("/").getPath()).getAbsolutePath()+File.separator;
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
	private JPanel pDNBVisual  = null;
	private DNBVisualPane dnbCanvas;
	private GroupLayout gl_pDNBVisual = null;
	private JTabbedPane tabbedPane= null;
	private JComboBox<String> cmboxDNBPeriod;
	private JLabel lbldnbperiod;
	private JComboBox<NodeShape> cmbNodeShape;
	private JLabel lblWorkSpace;
	private JLabel lblCase;
	private JLabel lblControl;
	private JButton btnCase;
	private JButton btnWorkSpace;
	private JButton btnControl;
	private JLabel lblSdThreshold;
	private JLabel lblPeriod;
	private JLabel lblClusterH;
	private JLabel lblPeriodSampleCount;
	private JButton btnDnbgen;
	private JLabel lblPccOutAmount;
	private JLabel lblCores;
	private JButton btnDnbNodeColor;
	private JButton btnNotDnbNodeColor;
	private JButton btnDnbEdgeColor;
	private JButton btnNotDnbEdgeColor;
	private JLabel lbldnb;
	private JTextField txtCustomDnb;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					log.info("Constants.PROJECT_DIR="+Constants.PROJECT_DIR);
					DNBGen frame = new DNBGen();
					frame.setSize(Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHTH);
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
		setBounds(100, 100, 772, 751);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		setMenu(menuBar);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				log.info("Tab index = "+ tabbedPane.getSelectedIndex());
				switch (tabbedPane.getSelectedIndex()) {
				case 1:
					pCIGrowth.load();
					break;
				case 2:
					dnbVisualMain(cmboxDNBPeriod);
					break;

				default:
					break;
				}
			}
		});
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
//		addExeMod();
		
		dnbGenTab(tabbedPane);
		
		ciGrowthTab(tabbedPane);
		
		dnbVisualTab(tabbedPane);
	}

	/**
	 * add execute mode for R file
	 */
	private void addExeMod() {
		String corePath = classPath + "core/";
		String addExeModCmd = "bash " + corePath + "addExeMod.sh " + corePath;
		log.info("addExeModCmd: " + addExeModCmd);
		CommonUtils.execShellCmd(addExeModCmd);
	}
	
	/**
	 * 移除工作目录中的旧文件
	 */
	private void removeOldFiles() {
		String corePath = classPath + "core/";
		String removeOldFilesCmd = "bash " + corePath + "removeOldFiles.sh " + TempVar.WORK_SPACE;
		log.info("addExeModCmd: " + removeOldFilesCmd);
		CommonUtils.execShellCmd(removeOldFilesCmd);
	}

	/**
	 * 可视化DNB ,并可以通过选择DNB的时期来绘制不同的DNB
	 * @param tabbedPane
	 */
	private void dnbVisualTab(JTabbedPane tabbedPane) {
		pDNBVisual = new JPanel();
		tabbedPane.addTab("DNB可视化", null, pDNBVisual, null);
		
		lbldnbperiod = new JLabel("请选择DNB的时期(Period):");
		cmboxDNBPeriod = new JComboBox<String>();
		dnbCanvas = new DNBVisualPane();
		dnbCanvasSaveAs();
		dnbVisualTabLayout(lbldnbperiod, cmboxDNBPeriod);
		
//		dnbVisualMain(cmboxDNBPeriod);
		
		cmboxDNBPeriod.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				dnbCanvas.setNodesMap(null);
				dnbCanvas.setPeriod((String)e.getItem());
				dnbCanvas.load();
			}
		});
	}

	/**
	 * DNB Visual 的主方法:导入所有DNB的时期作为下拉框的值,并绘制DNB图
	 * @param cmboxDNBPeriod
	 */
	private void dnbVisualMain(JComboBox<String> cmboxDNBPeriod) {
		String[] dnbPeriods = DnbUtils.getAllDnbPeriods(TempVar.WORK_SPACE);
		if (null == dnbPeriods) {
			log.error("no dnb,create DNB Visual Tab failed!");
			return;
		}
		if (cmboxDNBPeriod.getItemCount() == 0) {
			//这段语句必须放到dnbVisualTabLayout方法之后,否则进入Design界面会因为无法加载动态代码而失败
			for (String p : dnbPeriods) {
				cmboxDNBPeriod.addItem(p);
			}
		}
		dnbCanvas.setPeriod(dnbPeriods[0]);
		dnbCanvas.load();
	}

	/**
	 * DNB可视化画板的右键菜单=>另存为,保存图片
	 */
	private void dnbCanvasSaveAs() {
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(dnbCanvas, popupMenu);
		
		JMenuItem mntmSaveAs = new JMenuItem("另存为");
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					File savedFile  =  saveFile(dnbCanvas);
					if (savedFile == null ) {
						return;
					}
					BufferedImage image = new BufferedImage(dnbCanvas.getWidth(), dnbCanvas.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
					Graphics2D g = image.createGraphics();
					dnbCanvas.paint(g);
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
	 * DNB可视化Tab的布局
	 * @param lbldnbperiod
	 * @param cmboxDNBPeriod
	 */
	private void dnbVisualTabLayout(JLabel lbldnbperiod,
			JComboBox<String> cmboxDNBPeriod) {
		
		JLabel lblNodeShape = new JLabel("顶点形状：");
		
		cmbNodeShape = new JComboBox<NodeShape>();
		cmbNodeShape.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				dnbCanvas.setNodeShape((NodeShape)e.getItem());
			}
		});
		cmbNodeShape.setModel(new DefaultComboBoxModel<NodeShape>(
				NodeShape.values()));
		
		JLabel lblDnb = new JLabel("DNB顶点的颜色：");
		btnDnbNodeColor = new JButton(" ");
		btnDnbNodeColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color selectedColor = JColorChooser.showDialog(btnDnbNodeColor, "请选择颜色",
														btnDnbNodeColor.getBackground());
				btnDnbNodeColor.setBackground(selectedColor);
				dnbCanvas.setDnbNodeColor(selectedColor);
			}
		});
		
		JLabel lblNotDnb = new JLabel("非DNB顶点的颜色：");
		btnNotDnbNodeColor = new JButton(" ");
		btnNotDnbNodeColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color selectedColor = JColorChooser.showDialog(btnDnbNodeColor, "请选择颜色",
						btnNotDnbNodeColor.getBackground());
				btnNotDnbNodeColor.setBackground(selectedColor);
				dnbCanvas.setNotDnbNodeColor(selectedColor);
			}
		});
		
		JLabel lblDnbEdge = new JLabel("与DNB关联的边的颜色：");
		btnDnbEdgeColor = new JButton(" ");
		btnDnbEdgeColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color selectedColor = JColorChooser.showDialog(btnDnbNodeColor, "请选择颜色",
						btnDnbEdgeColor.getBackground());
				btnDnbEdgeColor.setBackground(selectedColor);
				dnbCanvas.setDnbEdgeColor(selectedColor);
			}
		});
		
		JLabel lblNotDnbEdge = new JLabel("未与DNB关联的边的颜色：");
		btnNotDnbEdgeColor = new JButton(" ");
		btnNotDnbEdgeColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color selectedColor = JColorChooser.showDialog(btnDnbNodeColor, "请选择颜色",
						btnNotDnbEdgeColor.getBackground());
				btnNotDnbEdgeColor.setBackground(selectedColor);
				dnbCanvas.setNotDnbEdgeColor(selectedColor);
			}
		});
		
		btnDnbNodeColor.setBackground(Color.RED);
		btnNotDnbNodeColor.setBackground(Color.BLACK);
		btnDnbEdgeColor.setBackground(Color.YELLOW);
		btnNotDnbEdgeColor.setBackground(Color.BLACK);
		
		dnbCanvas.setDnbNodeColor(btnDnbNodeColor.getBackground());
		dnbCanvas.setNotDnbNodeColor(btnNotDnbNodeColor.getBackground());
		dnbCanvas.setDnbEdgeColor(btnDnbEdgeColor.getBackground());
		dnbCanvas.setNotDnbEdgeColor(btnNotDnbEdgeColor.getBackground());
		
		lbldnb = new JLabel("自定义DNB（以,分割）：");
		
		txtCustomDnb = new JTextField();
		txtCustomDnb.getDocument().addDocumentListener(new DocumentListener() {
			
			public void removeUpdate(DocumentEvent e) {
				setValues();
			}
			
			public void insertUpdate(DocumentEvent e) {
				setValues();
			}
			
			public void changedUpdate(DocumentEvent e) {
				setValues();
			}
			
			private void setValues(){
				log.info("custom dnb : " +txtCustomDnb.getText());
				dnbCanvas.setCustomDnb(txtCustomDnb.getText());
			}
		});
		txtCustomDnb.setColumns(10);
		
		gl_pDNBVisual = new GroupLayout(pDNBVisual);
		gl_pDNBVisual.setHorizontalGroup(
			gl_pDNBVisual.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pDNBVisual.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pDNBVisual.createParallelGroup(Alignment.LEADING)
						.addComponent(dnbCanvas, GroupLayout.DEFAULT_SIZE, 731, Short.MAX_VALUE)
						.addGroup(gl_pDNBVisual.createSequentialGroup()
							.addGroup(gl_pDNBVisual.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(gl_pDNBVisual.createSequentialGroup()
									.addComponent(lbldnbperiod)
									.addGap(18)
									.addComponent(cmboxDNBPeriod, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(lblNodeShape)
									.addGap(18)
									.addComponent(cmbNodeShape, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(lbldnb)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtCustomDnb))
								.addGroup(Alignment.LEADING, gl_pDNBVisual.createSequentialGroup()
									.addComponent(lblDnb)
									.addGap(2)
									.addComponent(btnDnbNodeColor)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblNotDnb)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnNotDnbNodeColor)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblDnbEdge)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(btnDnbEdgeColor)
									.addGap(10)
									.addComponent(lblNotDnbEdge)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnNotDnbEdgeColor)))
							.addGap(0, 69, Short.MAX_VALUE)))
					.addGap(0))
		);
		gl_pDNBVisual.setVerticalGroup(
			gl_pDNBVisual.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pDNBVisual.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_pDNBVisual.createParallelGroup(Alignment.BASELINE)
						.addComponent(lbldnbperiod)
						.addComponent(cmboxDNBPeriod, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNodeShape)
						.addComponent(cmbNodeShape, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lbldnb)
						.addComponent(txtCustomDnb, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_pDNBVisual.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDnb)
						.addComponent(btnDnbNodeColor)
						.addComponent(lblNotDnb)
						.addComponent(btnNotDnbNodeColor)
						.addComponent(lblDnbEdge)
						.addComponent(btnDnbEdgeColor)
						.addComponent(lblNotDnbEdge)
						.addComponent(btnNotDnbEdgeColor))
					.addGap(24)
					.addComponent(dnbCanvas, GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
					.addContainerGap())
		);
		pDNBVisual.setLayout(gl_pDNBVisual);
	}

	/**
	 * 绘制DNB的综合指数(CI)折线图,并可以另存到文件中
	 * @param tabbedPane
	 */
	public void ciGrowthTab(JTabbedPane tabbedPane) {
		pCIGrowth = new CIGrowthPane();
		tabbedPane.addTab("综合指数折线图", null, pCIGrowth, null);
		ciGrowthSaveAs();

	}

	/**
	 *  综合指数折线图的右键菜单=>另存为,保存图片
	 */
	private void ciGrowthSaveAs() {
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
		
		lblWorkSpace = new JLabel("工作目录:");
		
		txtWorkSpace = new JTextField();
		txtWorkSpace.setColumns(10);
		
		btnWorkSpace = new JButton("选择");
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
		
		
		
		btnCase = new JButton("选择");
		btnCase.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int operate = fileChooser.showOpenDialog(btnCase);
				if (operate == JFileChooser.APPROVE_OPTION) {
					txtCaseFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		btnControl = new JButton("选择");
		btnControl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int operate = fileChooser.showOpenDialog(btnControl);
				if (operate == JFileChooser.APPROVE_OPTION) {
					txtControlFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		lblCase = new JLabel("Case文件:");
		lblControl = new JLabel("Control文件:");
		lblPeriod= new JLabel("总时期数(Period): ");
		lblPeriodSampleCount= new JLabel("每个时期样本数: ");
		lblSdThreshold= new JLabel("标准差筛选阈值: ");
		lblClusterH= new JLabel("层次聚类剪枝H值: ");
		lblPccOutAmount= new JLabel("模块间的需要计算的边数: ");
		lblCores= new JLabel("CPU加速所使用的核心数: ");
		
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
		
		btnDnbgen = new JButton("生成DNB");
		btnDnbgen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dnbGenMain();
			}
		});
		
		dnbGenLayout(pDNBGen, lblWorkSpace, lblCase, lblControl, lblPeriod,
				lblPeriodSampleCount, lblSdThreshold, lblClusterH,
				lblPccOutAmount, lblCores,
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

		String workspace = txtWorkSpace.getText() + File.separator;
		String caseFilePath = txtCaseFile.getText();
		String controlFilePath = txtControlFile.getText();
		String periodCount = txtPeriod.getText();
		String periodSampleCount = txtPeriodSampleCount.getText();
		String featuresSdThreshold = txtSdThreshold.getText();
		String clusterHclustH = txtClusterH.getText();
		String pccOutAmount = txtPccOutAmount.getText();
		String cores = txtCores.getText();

		TempVar.CORES = cores;
		TempVar.PERIOD_COUNT = periodCount;
		TempVar.PERIOD_SAMPLE_COUNT = periodSampleCount;
		TempVar.WORK_SPACE = workspace;
		
//		removeOldFiles();
		
		StringBuffer gdmCmd = new StringBuffer();
		gdmCmd.append("Rscript ").append(classPath).append("core").append(File.separator).append("gdm4Par.R ")
				.append(" -p ").append(workspace)
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
			gdmCmd.append("  --control.file.path  ").append(controlFilePath);
		}

		log.info("gdmCmd:" + gdmCmd.toString());
		CommonUtils.execShellCmd(gdmCmd.toString());

		// store cores, periodCount and periodSampleCount into file
//		String propPath = classPath + "tempVariables.properties";
//		CommonUtils.storeValueByKeyFromConfig("cores", cores, propPath);
//		CommonUtils.storeValueByKeyFromConfig("period.count", periodCount, propPath);
//		CommonUtils.storeValueByKeyFromConfig("period.sample.count", periodSampleCount, propPath);
//		CommonUtils.storeValueByKeyFromConfig("work.space", basePath, propPath);
		
		//以便重载DNB时期
		cmboxDNBPeriod.removeAllItems();
		//以便重新生成gdm_XXX.csv
		TempVar.HAS_GENERATED_GDM_CSV = false;
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
									.addComponent(txtWorkSpace, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(btnWorkSpace))
								.addGroup(gl_jpDNBGen.createSequentialGroup()
									.addComponent(lblCase)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtCaseFile, 220, 220, 220)
									.addGap(18)
									.addComponent(btnCase))
								.addGroup(gl_jpDNBGen.createSequentialGroup()
									.addComponent(lblControl)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(txtControlFile, GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)))
							.addGap(18)
							.addComponent(btnControl, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
							.addGap(234))
						.addGroup(gl_jpDNBGen.createSequentialGroup()
							.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.LEADING)
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
											.addPreferredGap(ComponentPlacement.RELATED, 199, Short.MAX_VALUE))
										.addComponent(txtClusterH, GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)))
								.addGroup(gl_jpDNBGen.createSequentialGroup()
									.addGroup(gl_jpDNBGen.createParallelGroup(Alignment.TRAILING)
										.addComponent(btnDnbgen)
										.addGroup(gl_jpDNBGen.createSequentialGroup()
											.addComponent(lblPccOutAmount)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(txtPccOutAmount, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)))
									.addGap(40)
									.addComponent(lblCores, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtCores, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)))
							.addGap(182))))
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
						.addComponent(lblControl)
						.addComponent(btnControl))
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
					.addContainerGap(369, Short.MAX_VALUE))
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

