/**
 * 
 */
package com.card;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * 定義紙牌的屬性，包括名稱、位置等相關資訊。並透過相關方法實現了紙牌的移動<br>
 * @author jeff
 * @date 2015/10/09
 */
public class PKCard extends JLabel implements MouseListener, MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1685847849234515380L;
	//紙牌的位置
	private Point point = null;
	/** 紙牌最後位置 */
	private Point initPoint = null;
	
	private int value = 0;
	private int type = 0;
	
	private String name = null;
	
	private PKCard previousCard = null;
	private Container pane = null;
	private Spider main = null;
	
	private boolean canMove = false;
	/** 是否為正面 */
	private boolean isFront = false;
	
	/**
	 * Constructor
	 * @param name : 格式是[1-10,1-1,1-2...]
	 * @param spider : Spider
	 */
	public PKCard(String name, Spider spider) {
		super();
		String[] aryOfName = name.split("-");
		this.type = Integer.parseInt(aryOfName[0]);
		this.value = Integer.parseInt(aryOfName[1]);
		
		this.name = name;
		this.main = spider;
		//default Picture
		//this.setIcon(new ImageIcon("images/rear.gif")); //can't work
		try {
			//URL url = this.getClass().getResource("images/");	//找到(資料夾)的物件(因為要用URL，所以要加反鈄線)
			URL url = this.getClass().getResource("../images/"); //相對路徑
			this.setIcon(new ImageIcon(new URL(url, "rear.gif")));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		this.setSize(71, 96);
		this.setVisible(true);
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	/**
	 * 轉至正面
	 */
	public void turnFront() {
		this.setIcon(new ImageIcon("images/" + name + ".gif"));
		this.isFront = true;
	}
	
	/**
	 * 轉至背面
	 */
	public void turnRear() {
		this.setIcon(new ImageIcon("images/rear.gif"));
		this.isFront = false;
		this.canMove = false;
	}
	
	/**
	 * 將牌移至point點處
	 * @param point
	 */
	public void moveto(Point point) {
		//setLocation = 將元件移到新位置。通過此元件父級坐標空間中的 x 和 y 參數來指定新位置的左上角。
		this.setLocation(point);
		this.initPoint = point;
	}
	
	
	
	//----------MouseMotionListener----------
	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}
	
	//----------MouseListener----------
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

}
