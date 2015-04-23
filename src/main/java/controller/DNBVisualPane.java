package controller;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.Edge;
import model.Node;
import model.NodeType;

import org.apache.log4j.Logger;

import utils.Constants;
import utils.DnbUtils;
import utils.TempVar;

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
 * 实现功能：DNB 可视化,样式如下:<br/>
 * _________________________________________________________________<br />
 * |                                                             |  |                                     |<br />
 * |                                                             |  |       custom               |<br />
 * |                                                             |  |         DNB                   |<br />
 * |                                                             |  |______________________ |<br />
 * |         not DNB                                   |  |______________________ |<br />
 * |                                                             |  |                                     |<br />
 * |                                                             |  |      Gen DNB             |<br />
 * |                                                             |  |                                     |<br />
 * |                                                             |  |                                     |<br />
 * |____________________________________ |_ |______________________|<br />
 * <p>
 * date author email notes<br />
 * -------- --------------------------- ---------------<br />
 * 2015年3月31日 邱星 starqiu@mail.ustc.edu.cn 新建类<br />
 * </p>
 *
 */
public class DNBVisualPane extends JPanel {
	/** */
	private static final long serialVersionUID = 2894886282413861500L;
	public final static Random random = new Random();
	private  final  String classPath = new File(this.getClass().getResource("/").getPath()).getAbsolutePath()+File.separator;
//	private  String workspace=CommonUtils.getValueByKeyFromConfig("work.space", classPath + "tempVariables.properties");
	private static final Logger log = Logger.getLogger(DNBVisualPane.class);
	private BufferedImage paintImage = new BufferedImage(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHTH, BufferedImage.TYPE_INT_RGB);
	private Map<String, Node> nodesMap = null;
	private String period = null;

	public DNBVisualPane() {
		super(); 
		setVisible(true); // 设置窗口可视
	}
	public DNBVisualPane(String period) {
		super(); 
		this.period = period;
		log.info("current dnb period i= "+period);
		load();
		setVisible(true); // 设置窗口可视
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(paintImage, 0, 0, Color.RED, null);

	}

	/**
	 * @param g
	 * @param period
	 * 
	 */
	private void drawAllNodesAndEdgesByPeriod(Graphics g) {
		nodesMap = DnbUtils.getAllNodesByPeriod(TempVar.WORK_SPACE,
				period, random, Constants.WINDOW_WIDTH	, Constants.WINDOW_HEIGHTH, nodesMap);
//		log.info("nodemap="+nodesMap);
		Map<String, List<String>> relatedNodeMap = DnbUtils.getRelatedNodeMapTogetherByPeriod(classPath, TempVar.WORK_SPACE,
				period, nodesMap);

		int radiusOfNode = 5;//点的半径
		for (Node node : nodesMap.values()) {
			switch (node.getNodeType()) {
			case NOT_DNB:
				g.fillOval(node.getX(), node.getY(), radiusOfNode, radiusOfNode);
				break;
			case DNB:
				g.setColor(Color.RED);
				g.fillOval(node.getX(), node.getY(), radiusOfNode*4, radiusOfNode*4);
				g.setColor(Color.BLACK);
				break;

			default:
				break;
			}
//			g.fillOval(node.getX(), node.getY(), radiusOfNode, radiusOfNode);
//			g.drawLine(node.getX(), node.getY(), node.getX(), node.getY());
		}
		
		for (Entry<String, List<String>> nodeEntry : relatedNodeMap.entrySet()) {
			Node sourceNode = nodesMap.get(nodeEntry.getKey());
			Node targetNode = null;
			for ( String targetNodeId : nodeEntry.getValue()) {
				targetNode = nodesMap.get(targetNodeId);
				if (sourceNode.getNodeType() == NodeType.NOT_DNB  &&
					targetNode.getNodeType() == NodeType.NOT_DNB ) {
					g.drawLine(sourceNode.getX(), sourceNode.getY(), targetNode.getX(), targetNode.getY());
				}else {
					g.setColor(Color.YELLOW);
					g.drawLine(sourceNode.getX(), sourceNode.getY(), targetNode.getX(), targetNode.getY());
					g.setColor(Color.BLACK);
				}
			}
		}

//		for (Edge edge : edges) {
//			g.drawLine(edge.getSource().getX(), edge.getSource().getY(), edge
//					.getTarget().getX(), edge.getTarget().getY());
//		}
	}
	
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

	public void load()  {
		
		Graphics2D g2D = paintImage.createGraphics();
//		g2D.setColor(Color.RED);
//		g2D.setBackground(Color.RED);
		g2D.setPaint(Color.WHITE);
		g2D.fillRect(0, 0, paintImage.getWidth(), paintImage.getHeight());
		g2D.setPaint(Color.BLACK);
		// draw on paintImage using Graphics
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//反锯齿
		drawAllNodesAndEdgesByPeriod(g2D);

		g2D.dispose();
		// repaint panel with new modified paint
		repaint();
	}
	
	@Override
	public void paint(Graphics g) { 
//		WINDOW_WIDTH = getWidth();
//		WINDOW_LENGTH = getHeight();
		super.paint(g);
	}

	public String getPeriod() {
		return period;
	}
	
	public void setPeriod(String period) {
		this.period = period;
	}
	public Map<String, Node> getNodesMap() {
		return nodesMap;
	}

	public void setNodesMap(Map<String, Node> nodesMap) {
		this.nodesMap = nodesMap;
	}
}
