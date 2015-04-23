/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * NodeType.java
 * 2015年4月22日
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
 *2015年4月22日	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
public enum NodeType {
	/** DNB生成算法生成的DNB*/
	DNB,
	/** 非DNB*/
	NOT_DNB,
	/** 用户自定义DNB*/
	CUSTOM_DNB
}

