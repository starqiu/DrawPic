/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * CIGrowthPane.java
 * 2015年4月16日
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package controller;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import utils.CommonUtils;

/**
 * 实现功能：
 * <p>
 * date author email notes<br />
 * -------- --------------------------- ---------------<br />
 * 2015年4月16日 邱星 starqiu@mail.ustc.edu.cn 新建类<br />
 * </p>
 *
 */
public class CIGrowthPane extends JPanel {
	private  final  String classPath = this.getClass().getResource("/").getPath();
	private static final Logger log = Logger.getLogger(CIGrowthPane.class);
	private String worspace = CommonUtils.getValueByKeyFromConfig("work.space", classPath + "tempVariables.properties");
	private BufferedImage paintImage = new BufferedImage(500, 400, BufferedImage.TYPE_3BYTE_BGR);

	/**
	 * Create the panel.
	 * @throws IOException 
	 */
	public CIGrowthPane() throws IOException {
		load();
	}

	@Override
	protected void paintComponent(Graphics g) {
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
		paintImage = ImageIO.read(new File(worspace+"ci.png"));
		// update panel with new paint image
		repaint();
	}

}
