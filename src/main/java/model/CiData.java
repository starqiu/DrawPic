/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * CiData.java
 * 2015-1-26
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package model;

/**
 * 实现功能： 
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2015-1-26	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
public class CiData {
	private String[] categories ;
	private double[] data;
	
	public String[] getCategories() {
		return categories;
	}
	public void setCategories(String[] categories) {
		this.categories = categories;
	}
	public double[] getData() {
		return data;
	}
	public void setData(double[] data) {
		this.data = data;
	}

}

