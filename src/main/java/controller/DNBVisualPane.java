package controller;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
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
	public  int WINDOW_WIDTH;
	public  int WINDOW_LENGTH;
	public final static Random random = new Random();
	private  final  String classPath = this.getClass().getResource("/").getPath();
	public  String workspace=CommonUtils.getValueByKeyFromConfig("work.space", classPath + "tempVariables.properties");
	private static final Logger log = Logger.getLogger(CIGrowthPane.class);
	private BufferedImage paintImage ;

	// 构造函数
	public DNBVisualPane() {
		super(); 
		
		setVisible(true); // 设置窗口可视
	}

	@Override
	protected void paintComponent(Graphics g) {
		log.info("paintComponent");
		super.paintComponent(g);
		g.drawImage(paintImage, 0, 0, null);
	}

	// draw painting
	public void updatePaint() {
		Graphics g = paintImage.createGraphics();

		// draw on paintImage using Graphics

		g.dispose();
		// repaint panel with new modified paint
		repaint();
	}

	public void save(File savedFile) throws IOException {
		ImageIO.write(paintImage, "PNG", savedFile);
	}

	public void load() throws IOException {
		paintImage = ImageIO.read(new File(workspace+"ci.png"));
		// update panel with new paint image
		repaint();
	}
	
	@Override
	public void paint(Graphics g) { // 重载窗口组件的paint()方法
		log.info("paint");
		WINDOW_WIDTH = getWidth();
		WINDOW_LENGTH = getHeight();
		paintImage = new BufferedImage(WINDOW_WIDTH, WINDOW_LENGTH, BufferedImage.TYPE_3BYTE_BGR);
		
		super.paint(g);
//		setSize(WINDOW_WIDTH, WINDOW_LENGTH); // 设置窗口尺寸
		Graphics2D g2D = (Graphics2D) g; // 获取图形环境

//		String sourcePath ="src"+File.separator;
//		String sourcePath ="";
		String period = "1";
		Map<String, Node> nodesMap = getAllNodesByPeriod( period);
		List<Edge> edges = getAllEdgesByPeriod( period, nodesMap);

		for (Node node : nodesMap.values()) {
			g2D.drawLine(node.getX(), node.getY(), node.getX(), node.getY());
		}

		for (Edge edge : edges) {
			g2D.drawLine(edge.getSource().getX(), edge.getSource().getY(), edge
					.getTarget().getX(), edge.getTarget().getY());
		}
		

	}

	public  Map<String, Node> getAllNodesByPeriod(String period) {
		Map<String, Node> nodesMap = new HashMap<String, Node>();
		// HashMap<String, String> dnbMap = getDnbMapByPeriod(classPath,
		// period);

		try {
			BufferedReader idBr = new BufferedReader(new FileReader(new File(
					workspace + "matrix_table_" + period + "_all_genes.txt")));
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

	public  List<Edge> getAllEdgesByPeriod(String period, Map<String, Node> nodesMap) {
		
		CommonUtils.geneateGdmCsv(classPath);
		
		List<Edge> edges = new ArrayList<Edge>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					workspace + "gdm_" + period + ".csv")));
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
