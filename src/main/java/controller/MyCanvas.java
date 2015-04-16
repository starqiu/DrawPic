package controller;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Window;
import java.awt.geom.GeneralPath;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;

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
 * 实现功能：
 * <p>
 * date author email notes<br />
 * -------- --------------------------- ---------------<br />
 * 2015年3月31日 邱星 starqiu@mail.ustc.edu.cn 新建类<br />
 * </p>
 *
 */
public class MyCanvas extends JFrame {
	public final static int WINDOW_WIDTH = 1280;
	public final static int WINDOW_LENGTH = 1024;
	public final static Random random = new Random();
	public static String sourcePath="/host/data/";

	GeneralPath gPath = new GeneralPath(); // GeneralPath对象实例
	Point aPoint;

	// 构造函数
	public MyCanvas() {
		super("Draw Network"); // 调用父类构造函数
//		 enableEvents(AWTEvent.MOUSE_EVENT_MASK|AWTEvent.MOUSE_MOTION_EVENT_MASK);
		// //允许事件

		setSize(WINDOW_WIDTH, WINDOW_LENGTH); // 设置窗口尺寸
		setVisible(true); // 设置窗口可视
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 关闭窗口时退出程序
	}

	public void paint(Graphics g) { // 重载窗口组件的paint()方法
		Graphics2D g2D = (Graphics2D) g; // 获取图形环境

//		String sourcePath ="src"+File.separator;
//		String sourcePath ="";
		Map<String, Node> nodesMap = getAllNodesByPeriod(sourcePath, "1");
		List<Edge> edges = getAllEdgesByPeriod(sourcePath, "1", nodesMap);

		for (Node node : nodesMap.values()) {
			g2D.drawLine(node.getX(), node.getY(), node.getX(), node.getY());
		}

		for (Edge edge : edges) {
			g2D.drawLine(edge.getSource().getX(), edge.getSource().getY(), edge
					.getTarget().getX(), edge.getTarget().getY());
		}
	}

	public static void main(String[] args) {
		if (args.length>0) {
			sourcePath = args[0];
		}
		new MyCanvas();
	}

	public static Map<String, Node> getAllNodesByPeriod(String classPath,
			String period) {
		Map<String, Node> nodesMap = new HashMap<String, Node>();
		// HashMap<String, String> dnbMap = getDnbMapByPeriod(classPath,
		// period);

		try {
			BufferedReader idBr = new BufferedReader(new FileReader(new File(
					classPath + "matrix_table_" + period + "_all_genes.txt")));
			// skip the title
			idBr.readLine();

			while (idBr.ready()) {
				Node node = new Node();
				node.setId(idBr.readLine());
				node.setX(random.nextInt(WINDOW_WIDTH));
				node.setY(random.nextInt(WINDOW_LENGTH));
				nodesMap.put(node.getId(), node);
			}

			idBr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nodesMap;
	}

	public static List<Edge> getAllEdgesByPeriod(String classPath,
			String period, Map<String, Node> nodesMap) {
		List<Edge> edges = new ArrayList<Edge>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					classPath + "gdm_" + period + ".csv")));
			// skip the title
			br.readLine();
			String[] line;
			while (br.ready()) {
				line = br.readLine().split(",");

				Edge edge = new Edge();
				edge.setSource(nodesMap.get(line[0]));
				edge.setTarget(nodesMap.get(line[1]));
				edge.setId(line[2]);
				edges.add(edge);
			}
		} catch (IOException e) {
			System.out.println("get all edges error! period=" + period);
		}
		return edges;
	}
}
