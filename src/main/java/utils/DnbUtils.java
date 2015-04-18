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
import java.util.List;
import java.util.Map;
import java.util.Random;

import model.CiData;
import model.DnbData;
import model.Edge;
import model.Node;

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
		if (nodesMap == null) {
			nodesMap = new HashMap<String, Node>();
			// HashMap<String, String> dnbMap = getDnbMapByPeriod(classPath,
			// period);

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
					node.setX(random.nextInt(maxWidth));
					node.setY(random.nextInt(maxHeight));
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
				edge.setId(line[2]);
				edges.add(edge);
			}
			br.close();
		} catch (IOException e) {
			System.out.println("get all edges error! period=" + period);
		}
		return edges;
	}

	/**
	 * change dnb list to map ,so it can indicate dnb genes from all genes
	 * faster
	 * 
	 * @param classPath
	 * @param period
	 * @return
	 */
	@SuppressWarnings("unused")
	private static HashMap<String, String> getDnbMapByPeriod(String classPath,
			String period) {
		List<String> dnbIds = getDnbGeneIds(classPath, period);

		Iterator<String> dnbIter = dnbIds.iterator();
		HashMap<String, String> dnbMap = new HashMap<String, String>();
		while (dnbIter.hasNext()) {
			dnbMap.put(dnbIter.next(), "1");
		}
		return dnbMap;
	}

	public static String[] getAllPeriods(String classPath) {
		return getAllCIs(classPath).getCategories();
	}

	public static List<DnbData> getDnbDatas(String classPath) {
		List<DnbData> dnbs = new ArrayList<DnbData>();

		String[] periods = getAllDnbPeriods(classPath);

		for (String period : periods) {
			DnbData dnb = new DnbData();
			dnb.setPeriod(period);
			dnb.setIds(getDnbGeneIds(classPath, period));
			dnbs.add(dnb);
		}
		log.info("get DNB data successfully !");
		return dnbs;
	}

	public static List<String> getDnbGeneIds(String classPath, String weekPeriod) {
		List<String> geneIds = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					classPath + "matrix_table_" + weekPeriod + "_dnb.txt")));
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

	public static CiData getAllCIs(String classPath) {
		CiData ci = new CiData();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					classPath + "all_ci.txt")));
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
