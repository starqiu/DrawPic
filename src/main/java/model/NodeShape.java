/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * NodeShape.java
 * 2015年4月24日
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package model;

/**
 * 实现功能： 绘制DNB时点的形状
 * <p>
 * date	        author            email		           notes<br />
 * ----------------------------------------------------------------<br />
 *2015年4月24日        邱星         starqiu@mail.ustc.edu.cn	    新建类<br /></p>
 *
 */
public enum NodeShape {
	CIRCLE("圆形"),
	RECTANGLE("方形");
	
	private String name;
	NodeShape(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}

