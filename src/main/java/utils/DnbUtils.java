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

import model.CiData;
import model.DnbData;

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


//	public static List<EleObj> getElementByPeriod(String classPath,
//			String period) {
//
//		// CytoscapeElement ele = new CytoscapeElement();
//		// ele.setId("ele" + period);
//		// ele.setNodes(getAllNodesByPeriod(classPath, period));
//		// ele.setEdges(getAllEdgesByPeriod(classPath, period));
//
//		// Element ele = new Element();
////		List<EleObj> ele = getAllNodesByPeriod(classPath, period);// add nodes
//		List<EleObj> ele = getAllHighSdNodesByPeriod(classPath, period);// add nodes
//		ele.addAll(getAllEdgesByPeriod(classPath, period));
//		return ele;
//	}
//
//	public static List<EleObj> getAllEdgesByPeriod(String classPath,
//			String period) {
//		// List<CytoscapeEdge> edges = new ArrayList<CytoscapeEdge>();
//		List<EleObj> edges = new ArrayList<EleObj>();
//		// HashMap<String, String> dnbMap = getDnbMapByPeriod(classPath,
//		// period);
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(new File(
//					classPath + "gdm_" + period + ".csv")));
//			// skip the title
//			br.readLine();
//			String[] line;
//			while (br.ready()) {
//				line = br.readLine().split(",");
//
//				EdgeData data = new EdgeData();
//				data.setSource(line[0]);
//				data.setTarget(line[1]);
//				data.setId(line[2]);
////				data.setWeight(Double.valueOf(line[3]));
//
//				EleObj edge = new EleObj();
//				edge.setData(data);
//				edge.setGroup("edges");
//
//				edges.add(edge);
//			}
//		} catch (IOException e) {
//			log.error("get all edges error! period=" + period, e);
//		}
//		return edges;
//	}

//	public static List<EleObj> getAllNodesByPeriod(String classPath,
//			String period) {
//		List<EleObj> nodes = new ArrayList<EleObj>();
//		// HashMap<String, String> dnbMap = getDnbMapByPeriod(classPath,
//		// period);
//
//		try {
//			BufferedReader idBr = new BufferedReader(new FileReader(new File(
//					classPath + "matrix_table_" + period + "_genes.txt")));
//			BufferedReader sdBr = new BufferedReader(new FileReader(new File(
//					classPath + "matrix_table_" + period + "_sd.txt")));
//
//			// skip the title
//			idBr.readLine();
//			sdBr.readLine();
//
//			while (idBr.ready() && sdBr.ready()) {
//				NodeData data = new NodeData();
//
//				data.setId(idBr.readLine());
//				// data.setGene_name(data.getId());
////				 data.setScore(Double.valueOf(sdBr.readLine()));
//				// if (dnbMap.get(data.getId()) != null) {
//				// data.setNode_type("dnb");
//				// }
//
//				EleObj node = new EleObj();
//				node.setData(data);
//				// node.setPosition(new Position());
//				// node.setSelected(true);
//
//				nodes.add(node);
//			}
//		} catch (IOException e) {
//			log.error("get all nodes error!period=" + period, e);
//		}
//		return nodes;
//	}
//	
//	public static List<EleObj> getAllHighSdNodesByPeriod(String classPath,
//			String period) {
//		List<EleObj> nodes = new ArrayList<EleObj>();
//		 HashMap<String, String> dnbMap = getDnbMapByPeriod(classPath,
//		 period);
//		
//		//add DNB node
//		EleObj dnbNode = new EleObj();
//		NodeData dnbData = new NodeData();
//		dnbData.setId("dnb");
////		dnbData.setScore(50000);
//		dnbNode.setData(dnbData);
//		nodes.add(dnbNode);
//		
//		//add  not DNB  node
////		EleObj notDnbNode = new EleObj();
////		NodeData notDnbData = new NodeData();
////		notDnbData.setId("notDnb");
////		notDnbData.setScore(500000);
////		notDnbNode.setData(notDnbData);
////		nodes.add(notDnbNode);
//		
//		try {
//			BufferedReader highSdGeneBr = new BufferedReader(new FileReader(new File(
//					classPath + "matrix_table_" + period + "_high_sd_genes.txt")));
//			
//			String[] line;
//			while (highSdGeneBr.ready()) {
//				
//				line = highSdGeneBr.readLine().split(",");
//				
//				NodeData data = new NodeData();
//				
//				data.setId(line[0]);
//				// data.setGene_name(data.getId());
////				data.setScore(Double.valueOf(line[1]));
//				 if (dnbMap.get(data.getId()) != null) {
////				 data.setNode_type("dnb");
//					 data.setParent("dnb");   
//				 }else {
//					 data.setParent("notDnb");   
//					
//				}
//				EleObj node = new EleObj();
//				node.setData(data);
//				// node.setPosition(new Position());
//				// node.setSelected(true);
//				
//				nodes.add(node);
//			}
//		} catch (IOException e) {
//			log.error("get all nodes error!period=" + period, e);
//		}
//		return nodes;
//	}

	/**
	 * change dnb list to map ,so it can indicate dnb genes from all genes
	 * faster
	 * 
	 * @param classPath
	 * @param period
	 * @return
	 */
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

	public static String[] getAllDnbPeriods(String classPath) {
		String[] periods = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					classPath + "ci_maxima_index.txt")));
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
