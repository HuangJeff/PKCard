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
	/** IMG Folder URL */
	private static final URL url = PKCard.class.getResource("../images/"); //相對路徑 (設成全域變數)
	//private final URL url = this.getClass().getResource("../images/"); //方法二
	
	//紙牌的位置
	private Point point = null;
	/** 紙牌最後位置 */
	private Point initPoint = null;
	
	private int value = 0;
	private int type = 0;
	
	private String name = null;
	/** 前一張牌 */
	private PKCard previousCard = null;
	private Container pane = null;
	/** Spider 主程式 */
	private Spider main = null;
	/** 是否可以移動 */
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
			//URL url = this.getClass().getResource("../images/"); //相對路徑(往上拉成全域變數)
			//this.setIcon(new ImageIcon(new URL(url, "rear.gif")));
			
			this.setIcon(this.getRearImage());
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
		//this.setIcon(new ImageIcon("images/" + name + ".gif"));
		try {
			this.setIcon(this.getImageIcon(name + ".gif"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.isFront = true;
	}
	
	/**
	 * 轉至背面
	 */
	public void turnRear() {
		//this.setIcon(new ImageIcon("images/rear.gif"));
		try {
			this.setIcon(this.getRearImage());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
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
	
	/**
	 * 判斷牌是否能移動
	 * @param can
	 */
	public void setCanMove(boolean can) {
		this.canMove = can;
		PKCard prevCard = main.getPreviousCard(this);
		if(prevCard != null && prevCard.isCardFront()) {
			if(!can) {
				if(!prevCard.isCardCanMove()) {
					return;
				} else {
					prevCard.setCanMove(can);
				}
			} else {
				if((this.value + 1) == prevCard.getCardValue() &&
						this.type == prevCard.getCardType())
				{
					prevCard.setCanMove(can);
				} else {
					prevCard.setCanMove(false);
				}
			}
		}
	}
	
	/**
	 * 判斷可用列
	 * @param point
	 * @return
	 */
	public int whichColumnAvailable(Point point) {
		int _x = point.x;
		int _y = point.y;
		int _a = (_x - 20) / 101;
		int _b = (_x - 20) % 101;
		if(_a != 9) {
			if(_b > 30 && _b <= 71) {
				_a = -1;
			} else if(_b > 71) {
				_a++;
			}
		} else if(_b > 71) {
			_a = -1;
		}
		
		if(_a != -1) {
			Point _p = this.main.getLastCardLocation(_a);
			if(_p == null)
				this.main.getGroundLabelLocation(_a);
			_b = _y - _p.y;
			if(_b <= -96 || _b >= 96) {
				_a = -1;
			}
		}
		return _a;
	}
	
	
	public void flashCard(PKCard card) {
		//啟動Flash程序
		new Flash(card).start();
		//不停的獾得下一張牌，直到完成
		if(this.main.getNextCard(card) != null) {
			card.flashCard(this.main.getNextCard(card));
		}
	}
	
	/**
	 * 取得牌背面圖檔(rear.gif)
	 * @return
	 * @throws MalformedURLException 
	 */
	private ImageIcon getRearImage() throws MalformedURLException {
		return this.getImageIcon("rear.gif");
	}
	
	/**
	 * 取得白色牌圖檔(white.gif)
	 * @return
	 * @throws MalformedURLException 
	 */
	private ImageIcon getWhiteImage() throws MalformedURLException {
		return this.getImageIcon("white.gif");
	}
	
	/**
	 * 取得指定名稱的Image File
	 * @param fileName : 指定圖檔名稱
	 * @return
	 * @throws MalformedURLException 
	 */
	private ImageIcon getImageIcon(String fileName) throws MalformedURLException {
		return new ImageIcon(new URL(url, fileName));
	}
	
	/**
	 * 是否為正面
	 * @return
	 */
	public boolean isCardFront() {
		return this.isFront;
	}
	
	/**
	 * 是否能移動
	 * @return
	 */
	public boolean isCardCanMove() {
		return this.canMove;
	}
	
	/**
	 * 取得牌面數值
	 * @return
	 */
	public int getCardValue() {
		return this.value;
	}
	
	/**
	 * 取得牌面類型
	 * @return
	 */
	public int getCardType() {
		return this.type;
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
	
	/**
	 * 點擊滑鼠
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		Point _p = e.getPoint();
		this.main.setNA();
		this.previousCard = this.main.getPreviousCard(this);
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
	
	
	class Flash extends Thread {
		private PKCard card = null;
		
		/**
		 * Constructor
		 * @param card
		 */
		public Flash(PKCard card) {
			this.card = card;
		}
		
		/**
		 * 為紙牌的正面設置白色圖片
		 */
		@Override
		public void run() {
			boolean is = false;
			ImageIcon whiteIcon = null;
			try {
				//白色圖片
				whiteIcon = this.card.getWhiteImage();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			for(int i = 0;i < 4;i++) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(is) {
					this.card.turnFront();
					is = !is;
				} else {
					this.card.setIcon(whiteIcon);
					is = !is;
				}
				//根據現在外觀將card的UI屬性重置
				this.card.updateUI();
			}
		}
	}
}
