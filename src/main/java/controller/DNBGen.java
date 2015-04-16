package controller;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

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
 * 实现功能： 
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2015年4月15日	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
public class DNBGen extends JFrame {

	private JPanel contentPane;
	private JTextField txtWorkSpace;
	private JFileChooser fileChooser = new JFileChooser() ;
	private JTextField txtCaseFile;
	private JTextField txtControlFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DNBGen1 frame = new DNBGen1();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
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
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel jpDNBGen = new JPanel();
		tabbedPane.addTab("参数输入", null, jpDNBGen, null);
		
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
		
		JLabel lblCase = new JLabel("Case文件:");
		JLabel lblControl = new JLabel("Control文件:");
		
		txtCaseFile = new JTextField();
		txtCaseFile.setColumns(10);
		
		txtControlFile = new JTextField();
		txtControlFile.setColumns(10);
		
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
		
		JLabel lblPeriod= new JLabel("总时期数(Period): ");
		JLabel lblPeriodSampleCount= new JLabel("每个时期样本数: ");
		JLabel lblSdThreshold= new JLabel("标准差筛选阈值: ");
		JLabel lblClusterH= new JLabel("层次聚类剪枝H值: ");
		JLabel lblPccOutAmount= new JLabel("模块间的需要计算的边数: ");
		JLabel lblCores= new JLabel("CPU加速所使用的核心数: ");
		
		JTextField txtPeriod= new JTextField();
		JTextField txtPeriodSampleCount= new JTextField();
		JTextField txtSdThreshold= new JTextField();
		JTextField txtClusterH= new JTextField();
		JTextField txtPccOutAmount= new JTextField();
		JTextField txtCores= new JTextField();

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
			}
		});
		
		dnbGenLayout(jpDNBGen, lblWorkSpace, lblCase, lblControl, lblPeriod,
				lblPeriodSampleCount, lblSdThreshold, lblClusterH,
				lblPccOutAmount, lblCores, txtPeriod, txtPeriodSampleCount,
				txtSdThreshold, txtClusterH, txtPccOutAmount, txtCores,
				btnWorkSpace, btnCase, btnControl, btnDnbgen);

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
	 * @param txtPeriod
	 * @param txtPeriodSampleCount
	 * @param txtSdThreshold
	 * @param txtClusterH
	 * @param txtPccOutAmount
	 * @param txtCores
	 * @param btnWorkSpace
	 * @param btnCase
	 * @param btnControl
	 * @param btnDnbgen
	 */
	private void dnbGenLayout(JPanel jpDNBGen, JLabel lblWorkSpace,
			JLabel lblCase, JLabel lblControl, JLabel lblPeriod,
			JLabel lblPeriodSampleCount, JLabel lblSdThreshold,
			JLabel lblClusterH, JLabel lblPccOutAmount, JLabel lblCores,
			JTextField txtPeriod, JTextField txtPeriodSampleCount,
			JTextField txtSdThreshold, JTextField txtClusterH,
			JTextField txtPccOutAmount, JTextField txtCores,
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
}

