/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * DnbUtils.java
 * 2015-3-4
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import model.CiData;
import model.DnbData;
import model.Edge;
import model.Node;
import model.NodeType;

import org.apache.log4j.Logger;

/**
 * 实现功能：operation of DNB relative files ,to get DNB infomation
 * <p>
 * date author email notes<br />
 * -------- --------------------------- ---------------<br />
 * 2015-3-4 邱星 starqiu@mail.ustc.edu.cn 新建类<br />
 * </p>
 * 
 */
public final class DnbUtils {

	public static final Logger log = Logger.getLogger(DnbUtils.class);

	private DnbUtils() {
		super();
	}

	/**
	 * 获取特定时期的所有点,如果点还没有坐标,则为其随机一个坐标
	 * @param nodesMap 同返回值
	 * @param workspace 工作目录
	 * @param period 时期
	 * @param random Random类实例,用于随机生成点的坐标
	 * @param maxWidth 点横坐标的最大值
	 * @param maxHeight 点纵坐标的最大值
	 * @return key为点的ID,value为对应的点
	 */
	public static Map<String, Node> getAllNodesByPeriod(
			String workspace, String period, Random random, int maxWidth,
			int maxHeight,Map<String, Node> nodesMap) {
		if ((null == nodesMap) || !TempVar.HAS_GENERATED_GDM_CSV) {
			nodesMap = new HashMap<String, Node>();
			 HashMap<String, String> dnbMap = getDnbMapByPeriod(workspace,period);

			 int notDNBMaxWidth = (int) (maxWidth * 0.8);
			 int dnbMinWidth = notDNBMaxWidth + Constants.CANVAS_MODULE_MARGIN;
			 int dnbMinHeight = (maxHeight + Constants.CANVAS_MODULE_MARGIN)>>1 ;
			 int dnbWidth = maxWidth - dnbMinWidth;
			 int dnbHeight = maxHeight - dnbMinHeight ;
			try {
				BufferedReader idBr = new BufferedReader(new FileReader(
						new File(workspace + "matrix_table_" + period
								+ "_all_genes.txt")));
				log.info("node file path is "+workspace + "matrix_table_" + period
								+ "_all_genes.txt");
				// skip the title
				idBr.readLine();

				while (idBr.ready()) {
					Node node = new Node();
					node.setId(idBr.readLine());

					if (null == dnbMap.get(node.getId())) {
						node.setNodeType(NodeType.NOT_DNB);
						node.setX(random.nextInt(notDNBMaxWidth));
						node.setY(random.nextInt(maxHeight));
					}else {
						node.setNodeType(NodeType.DNB);
						node.setX(random.nextInt(dnbWidth) + dnbMinWidth);
						node.setY(random.nextInt(dnbHeight) + dnbMinHeight);
					}
					
					nodesMap.put(node.getId(), node);
				}

				idBr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return nodesMap;
	}

	/**
	 * @param classPath 类路径,R文件所在路径
	 * @param workspace 工作目录
	 * @param period 时期
	 * @param nodesMap key为点的ID,value为对应的点
	 * @return
	 */
	public static List<Edge> getAllEdgesByPeriod(String classPath,
			String workspace, String period, Map<String, Node> nodesMap) {

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
//				edge.setId(line[2]);
				edges.add(edge);
			}
			br.close();
		} catch (IOException e) {
			System.out.println("get all edges error! period=" + period);
		}
		return edges;
	}
	
	/**
	 * 将相关的点通过邻接表聚合起来
	 * @param classPath 类路径,R文件所在路径
	 * @param workspace 工作目录
	 * @param period 时期
	 * @param nodesMap key为点的ID,value为对应的点
	 * @return 
	 */
	public static  Map<String, List<String>> getRelatedNodeMapTogetherByPeriod(String classPath,
			String workspace, String period, Map<String, Node> nodesMap) {
		Map<String, List<String>> relatedNodeMap = new HashMap<String, List<String>>();
		CommonUtils.geneateGdmCsv(classPath);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					workspace + "gdm_" + period + ".csv")));
			// skip the title
			br.readLine();
			String[] line;
//			Node source;
//			Node target;
			while (br.ready()) {
				line = br.readLine().split(",");
				
				List<String> relatedSourceNode = relatedNodeMap.get(line[0]);
				if (null == relatedSourceNode ) {
					relatedSourceNode = new  LinkedList<String>();
				}
				relatedSourceNode.add(line[1]);
				relatedNodeMap.put(line[0], relatedSourceNode);
				
				List<String> relatedTargetNode = relatedNodeMap.get(line[1]);
				if (null == relatedTargetNode ) {
					relatedTargetNode = new  LinkedList<String>();
				}
				relatedTargetNode.add(line[0]);
				relatedNodeMap.put(line[1], relatedTargetNode);
//				source = nodesMap.get(line[0]);
//				target = nodesMap.get(line[1]);
//				if (source.getLinkedNodes() == null) {
//					source.setLinkedNodes(new LinkedList<Node>());
//				}
//				if (target.getLinkedNodes() == null) {
//					target.setLinkedNodes(new LinkedList<Node>());
//				}
//				source.getLinkedNodes().add(target);
//				target.getLinkedNodes().add(source);
				
//				nodesMap.put(line[0], source);
//				nodesMap.put(line[1], target);
			}
			br.close();
		} catch (IOException e) {
			System.out.println("get all edges error! period=" + period);
		}
		return relatedNodeMap;
	}

	/**
	 * change dnb list to map ,so it can indicate dnb genes from all genes
	 * faster
	 * 
	 * @param workspace
	 * @param period
	 * @return
	 */
	public static HashMap<String, String> getDnbMapByPeriod(String workspace,
			String period) {
		List<String> dnbIds = getDnbGeneIds(workspace, period);

		Iterator<String> dnbIter = dnbIds.iterator();
		HashMap<String, String> dnbMap = new HashMap<String, String>();
		while (dnbIter.hasNext()) {
			dnbMap.put(dnbIter.next(), "1");
		}
		return dnbMap;
	}

	public static String[] getAllPeriods(String workspace) {
		return getAllCIs(workspace).getCategories();
	}

	public static List<DnbData> getDnbDatas(String workspace) {
		List<DnbData> dnbs = new ArrayList<DnbData>();

		String[] periods = getAllDnbPeriods(workspace);

		for (String period : periods) {
			DnbData dnb = new DnbData();
			dnb.setPeriod(period);
			dnb.setIds(getDnbGeneIds(workspace, period));
			dnbs.add(dnb);
		}
		log.info("get DNB data successfully !");
		return dnbs;
	}

	public static List<String> getDnbGeneIds(String workspace, String weekPeriod) {
		List<String> geneIds = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					workspace + "matrix_table_" + weekPeriod + "_dnb.txt")));
			while (br.ready()) {
				geneIds.add(br.readLine());
			}
			br.close();
		} catch (IOException e) {
			log.error("read ci_maxima_index error!", e);
		}
		log.info("geneIds:" + Arrays.toString(geneIds.toArray()));
		return geneIds;
	}

	/**
	 * 根据工作路径,读取对应的文件获得所有的DNB时期
	 * @param workspace 工作目录
	 * @return 所有的DNB时期
	 */
	public static String[] getAllDnbPeriods(String workspace) {
		String[] periods = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					workspace + "ci_maxima_index.txt")));
			periods = br.readLine().split("\t");
			br.close();
		} catch (IOException e) {
			log.error("read ci_maxima_index error!", e);
		}
		log.info("periods:" + Arrays.toString(periods));
		return periods;
	}

	public static CiData getAllCIs(String workspace) {
		CiData ci = new CiData();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					workspace + "all_ci.txt")));
			ci.setCategories(br.readLine().split("\t"));

			String[] dataStr = br.readLine().split("\t");
			int dataLen = dataStr.length;
			double[] data = new double[dataLen];
			for (int i = 0; i < dataLen; i++) {
				data[i] = Double.parseDouble(dataStr[i]);
			}

			ci.setData(data);
			br.close();
		} catch (IOException e) {
			log.error("read ci error!", e);
		}
		log.info("get ci value  successfully !");
		return ci;
	}

}
