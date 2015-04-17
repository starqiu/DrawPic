package controller;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import utils.CommonUtils;
import utils.DnbUtils;
import model.Edge;
import model.Node;

/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * MyCanvas.java
 * 2015年3月31日
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */

/**
 * 实现功能：DNB 可视化
 * <p>
 * date author email notes<br />
 * -------- --------------------------- ---------------<br />
 * 2015年3月31日 邱星 starqiu@mail.ustc.edu.cn 新建类<br />
 * </p>
 *
 */
public class DNBVisualPane extends JPanel {
	public  int WINDOW_WIDTH=1024;
	public  int WINDOW_LENGTH=768;
	public final static Random random = new Random();
	private  final  String classPath = this.getClass().getResource("/").getPath();
	public  String workspace=CommonUtils.getValueByKeyFromConfig("work.space", classPath + "tempVariables.properties");
	private static final Logger log = Logger.getLogger(DNBVisualPane.class);
	private BufferedImage paintImage = new BufferedImage(WINDOW_WIDTH, WINDOW_LENGTH, BufferedImage.TYPE_INT_RGB);
	private boolean need_repaint = true;
	public Map<String, Node> nodesMap = null;

	// 构造函数
	public DNBVisualPane() {
		super(); 
		try {
			load();
		} catch (IOException e) {
			log.error("create DNB Visual panel error!", e);
		}
		setVisible(true); // 设置窗口可视
	}

	@Override
	protected void paintComponent(Graphics g) {
//		log.info("paintComponent");
		super.paintComponent(g);
		
//		Graphics2D g2D = (Graphics2D) g; // 获取图形环境
//		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//反锯齿
//		String period = "1";
//		drawAllNodesAndEdgesByPeriod(g2D, period);
		g.drawImage(paintImage, 0, 0, Color.RED, null);

	}

	/**
	 * @param g
	 * @param period
	 */
	private void drawAllNodesAndEdgesByPeriod(Graphics g, String period) {
		nodesMap = DnbUtils.getAllNodesByPeriod(workspace,
				period, random, WINDOW_WIDTH, WINDOW_LENGTH, nodesMap);
		List<Edge> edges = DnbUtils.getAllEdgesByPeriod(classPath, workspace,
				period, nodesMap);

		for (Node node : nodesMap.values()) {
			g.drawLine(node.getX(), node.getY(), node.getX(), node.getY());
		}

		for (Edge edge : edges) {
			g.drawLine(edge.getSource().getX(), edge.getSource().getY(), edge
					.getTarget().getX(), edge.getTarget().getY());
		}
	}

	@Override
	protected void paintChildren(Graphics g) {
		super.paintChildren(g);
	};
	
	// draw painting
	public void updatePaint() {
		Graphics2D g2D = paintImage.createGraphics();

		// draw on paintImage using Graphics

		g2D.dispose();
		// repaint panel with new modified paint
		repaint();
	}

	public void save(File savedFile) throws IOException {
		ImageIO.write(paintImage, "PNG", savedFile);
	}

	public void load() throws IOException {
		
		Graphics2D g2D = paintImage.createGraphics();
//		g2D.setColor(Color.RED);
//		g2D.setBackground(Color.RED);
		g2D.setPaint(Color.WHITE);
		g2D.fillRect(0, 0, paintImage.getWidth(), paintImage.getHeight());
		g2D.setPaint(Color.BLACK);
		// draw on paintImage using Graphics
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//反锯齿
		String period = "1";
		drawAllNodesAndEdgesByPeriod(g2D, period);

		g2D.dispose();
		// repaint panel with new modified paint
		repaint();
	}
	
	@Override
	public void paint(Graphics g) { // 重载窗口组件的paint()方法
//		log.info("paint");
		WINDOW_WIDTH = getWidth();
		WINDOW_LENGTH = getHeight();
//		paintImage = new BufferedImage(WINDOW_WIDTH, WINDOW_LENGTH, BufferedImage.TYPE_3BYTE_BGR);
		
		super.paint(g);
//		setSize(WINDOW_WIDTH, WINDOW_LENGTH); // 设置窗口尺寸
//		Graphics2D g2D = (Graphics2D) g; // 获取图形环境
//		if (need_repaint) {
//			String period = "1";
//			Map<String, Node> nodesMap = getAllNodesByPeriod( period);
//			List<Edge> edges = getAllEdgesByPeriod( period, nodesMap);
//
//			for (Node node : nodesMap.values()) {
//				g2D.drawLine(node.getX(), node.getY(), node.getX(), node.getY());
//			}
//
//			for (Edge edge : edges) {
//				g2D.drawLine(edge.getSource().getX(), edge.getSource().getY(), edge
//						.getTarget().getX(), edge.getTarget().getY());
//			}
//			
//			need_repaint =  false;
//		}
		

	}




}
