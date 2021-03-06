/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * CommonUtils.java
 * 2015-3-4
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 实现功能：
 * <p>
 * date author email notes<br />
 * -------- --------------------------- ---------------<br />
 * 2015-3-4 邱星 starqiu@mail.ustc.edu.cn 新建类<br />
 * </p>
 * 
 */
public class CommonUtils {

	public static final Logger log = Logger.getLogger(CommonUtils.class);

	private CommonUtils(){
		super();
	}
	
	/**
	 * 生成gdm_XXX.csv,以便DNB可视化
	 * @param classPath 累路径
	 */
	public static void geneateGdmCsv(String classPath) {

//		String propPath = classPath + "tempVariables.properties";
		
		if (!TempVar.HAS_GENERATED_GDM_CSV) {
//			String periodCount = CommonUtils.getValueByKeyFromConfig("period.count", propPath);
//			String periodSampleCount = CommonUtils.getValueByKeyFromConfig("period.sample.count", propPath);
//			String cores = CommonUtils.getValueByKeyFromConfig("cores", propPath);
			StringBuffer cmdSb = new StringBuffer();
			cmdSb.append("Rscript ").append(classPath).append("core/cytoscape.R ").append(" -p ")
					.append(TempVar.WORK_SPACE).append("  --period.count   ")
					.append(TempVar.PERIOD_COUNT).append("  --period.sample.count  ")
					.append(TempVar.PERIOD_SAMPLE_COUNT).append(" --cores ")
					.append(TempVar.CORES);
			String cmd = cmdSb.toString();
			log.info(cmd);
			execShellCmd(cmd);
//			CommonUtils.storeValueByKeyFromConfig("has.generated.gdm.csv", "true", propPath);
			TempVar.HAS_GENERATED_GDM_CSV = true;
		}
		
	}

	/**
	 * @param cmd
	 */
	public static void execShellCmd(String cmd) {
		Runtime rt = Runtime.getRuntime();
		BufferedInputStream bis;
		BufferedReader br;
		try {
			Process process = rt.exec(cmd);
			bis = new BufferedInputStream(process.getInputStream());
			br = new BufferedReader(new InputStreamReader(bis));

			String line;
			while ((line = br.readLine()) != null) {
				log.info(line);
			}

			if (process.waitFor() != 0) {
				if (process.exitValue() == 1) {
					log.error("exec shell cmd,incorrectly exit!");
				}
			}

			bis.close();
			br.close();
		} catch (Exception e) {
			log.error("exec shell cmd error", e);
		}
		log.info("execute shell command successfully!");
	}

	public static List<String> queryUploadFileNames(String classPath) {
		File dir = new File(classPath + "sourceData/");
		File[] files = dir.listFiles();

		if (files.length == 0) {
			log.warn("query files is null !");
			return null;
		}

		List<String> fileList = new ArrayList<String>();

		for (File file : files) {
			fileList.add(file.getName());
		}

		return fileList;
	}

	public static String getValueByKeyFromConfig(String key, String propFilePath) {
		Properties prop = new Properties();
		// String propFileName = "utils/tempVariables.properties";

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(propFilePath);
			prop.load(inputStream);
		} catch (IOException e) {
			log.error("IOException", e);
		}
		if (inputStream == null) {
			System.err.println("property file '" + propFilePath
					+ "' not found in the classpath");
			return null;
		}
		return prop.getProperty(key);
	}

	public static boolean storeValueByKeyFromConfig(String key, String value,
			String propFilePath) {
		Properties prop = new Properties();
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(propFilePath);
			prop.load(inputStream);

			prop.setProperty(key, value);

			outputStream = new FileOutputStream(propFilePath);
			prop.store(outputStream, "");
			inputStream.close();
			outputStream.close();
		} catch (IOException e) {
			log.error("IOException", e);
			return false;
		}
		return true;
	}
}
