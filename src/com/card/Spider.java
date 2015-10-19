/**
 * 
 */
package com.card;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

/**
 * PKCard 主程式<br>
 * 主要功能：生成新遊戲的框架，實現遊戲中的方法，包括：紙牌的隨機生成、位置的擺放等。<br>
 * @author jeff
 * @date 2015/10/09
 */
public class Spider extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1318098837399580564L;
	
	//難度等級：簡單
	public static final int EASY = 1;
	//難度等級：中級
	public static final int NATURAL = 1;
	//難度等級：困難
	public static final int HARD = 1;
	
	private Container pane = null;
	private JLabel clickLagel = null;
	private JLabel[] groundLabel = null;
	
	//生成紙牌陣列
	private PKCard[] cards = null;
	//初始難度
	private int grade = 0;
	
	private Hashtable<Point, PKCard> table = null;
	private int a = 0;
	private int n = 0;
	private int c = 0;
	private int finish = 0;
	
	/**
	 * 
	 */
	public Spider() {
		setTitle("新接龍");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//設定框架大小
		setSize(1024, 742);
		//置中
		setLocationRelativeTo(null);
		
		//生成SpiderMenuBar物件，並放置於框架之上
		setJMenuBar(new SpiderMenuBar(this));
		
		this.pane = this.getContentPane();
		//設置背景顏色
		pane.setBackground(new Color(0, 112, 26));
		//將佈局管理器設置成null
		pane.setLayout(null);
		
		clickLagel = new JLabel();
		clickLagel.setBounds(883, 606, 121, 96);
		
		pane.add(clickLagel);
		
		this.initCards();
		this.randomCards();
		this.setCardsLocation();
		
		//初始化groundLabel，並指定位置(桌面上的框框(紙牌放置處))
		groundLabel = new JLabel[10];
		int x = 20;
		for(int i=0;i<10;i++) {
			groundLabel[i] = new JLabel();
			/*
			 * BorderFactory.createEtchedBorder()
			 * 創建一個具有“浮雕化”外觀效果的邊框，將元件的當前背景色用於高亮顯示和陰影顯示。
			 * type - EtchedBorder.RAISED 或 EtchedBorder.LOWERED 之一 
			 */
			groundLabel[i].setBorder(BorderFactory.createEtchedBorder(
					EtchedBorder.RAISED));
			//groundLabel[i].setBounds(x, 25, 71, 96);
			groundLabel[i].setBounds(x, 25, 71, 100);
			//For Test
			/*try {
				//URL url = this.getClass().getResource("images/");	//找到(資料夾)的物件(因為要用URL，所以要加反鈄線)
				URL url = this.getClass().getResource("../images/"); //相對路徑
				groundLabel[i].setIcon(new ImageIcon(new URL(url, "rear.gif")));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}*/
			x += 101;
			this.pane.add(groundLabel[i]);
		}
		
		//this.setVisible(true);
		
		
		
		//addEventListener
		this.addEventListener();
	}
	
	/**
	 * 添加事件監聽事件
	 */
	private void addEventListener() {
		clickLagel.addMouseListener(new MouseAdapter() {
			//鼠標按鈕在元件上釋放時調用。
			public void mouseReleased(MouseEvent e) {
				System.out.println("clickLagel --- mouseReleased e = " + e);
			}
		});
		
		this.addKeyListener(new KeyAdapter() {
			
			public void keyPressed(KeyEvent e) {
				
			}
		});
		
	}
	
	/**
	 * 紙牌初始化
	 */
	private void initCards() {
		//如果是空的，初始化
		if(cards == null)
			cards = new PKCard[104]; //撲克牌52張(104 = 52 * 2)
		//如果紙牌已被賦值，即將其從框架的面板中移去
		if(cards[0] != null) {
			for(int i=0;i<104;i++) {
				this.pane.remove(cards[i]);
			}
		}
		
		if(this.grade == 0)
			this.grade = Spider.EASY;
		int _n = 0;
		//通過難度等級，給n數值
		if(this.grade == Spider.EASY) {
			_n = 1;
		} else if(this.grade == Spider.NATURAL) {
			_n = 2;
		} else {
			_n = 4;
		}
		
		//初始card的值
		for(int i=1;i<=8;i++) {	//2副牌(2 * 4種花色)
			for(int j=1;j<=13;j++) { //13張牌
				String _cardName = (i % _n + 1) + "-" + j; //=> new PKCard((i % n + 1) + "-" + j, this);
				int cardNum = (i - 1) * 13 + j - 1;
				// ex:card value i = [1] and j = [1] value = 0 _cardName = 1-1
				//System.out.println("card value i = [" + i +
				//		"] and j = [" + j + "] value = " + cardNum + " _cardName = " + _cardName);
				cards[cardNum] = new PKCard(_cardName, this);
			}
		}
		
		//隨機紙牌初始化 TODO 先Mark，之後要開
		//this.randomCards();
	}
	
	/**
	 * 紙牌隨機分配
	 */
	private void randomCards() {
		PKCard temp = null;
		//隨機生成牌號
		for(int i=0;i<52;i++) {
			int _a = (int)(Math.random() * 104);
			int _b = (int)(Math.random() * 104);
			temp = cards[_a];
			cards[_a] = cards[_b];
			cards[_b] = temp;
		}
	}
	
	/**
	 * 設定紙牌的位置
	 */
	private void setCardsLocation() {
		if(table == null) {
			table = new Hashtable<Point, PKCard>();
		} else {
			table.clear();
		}
		a = 0;
		c = 0;
		n = 0;
		finish = 0;
		
		int x = 883;
		int y = 580;
		//初始化待展開的紙牌(共60張牌，待展開)
		for(int i=0;i<6;i++) {
			for(int j=0;j<10;j++) {
				int _n = i * 10 + j;
				System.out.println("初始化待展開的紙牌 _n = " + _n);
				this.pane.add(cards[_n]);
				//將card轉向背面
				cards[_n].turnRear();
				//將card放有固定的位置上(右下角，發牌區)
				cards[_n].moveto(new Point(x, y));
				//將card的位置及相關資訊存入
				table.put(new Point(x, y), cards[_n]);
			}
			//x軸+10
			x += 10;
		}
		x = 20;
		y = 45;
		//初始化表面顯示的紙牌(上方白色框內)
		for(int i=10;i>5;i--) { //往下每組各5張牌
			for(int j=0;j<10;j++) { //往右排10組
				int _n = i * 10 + j;
				if(_n >= 104)
					continue;
				//System.out.println("初始化表面顯示的紙牌 _n = " + _n);
				this.pane.add(cards[_n]);
				//轉背面
				cards[_n].turnRear();
				//移動到固定位置
				cards[_n].moveto(new Point(x, y));
				//將card的位置及相關資訊存入
				table.put(new Point(x, y), cards[_n]);
				x += 101; //往右排
			}
			x = 20; //重設回原點
			y -= 5;
		}
	}
	
	/**
	 * 設置還原
	 */
	private void setNA() {
		this.a = 0;
		this.n = 0;
	}
	
	/**
	 * 遊戲建立
	 */
	private void deal() {
		this.setNA();
		//判斷10列中是否空列
		for(int i=0;i<10;i++) {
			if(this.getLastCardLocation(i) == null) {
				JOptionPane.showMessageDialog(this,
						"有空位不能發牌！", "提示", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		int x = 20;
		
		for(int i=0;i<10;) {
			Point lastPoint = this.getLastCardLocation(i);
			//這張牌應"背面向上"
			if(c == 0) {
				lastPoint.y += 5;
			} //這張牌應"正面向上"
			else {
				lastPoint.y += 20;
			}
			
			table.remove(cards[c + i].getLocation());
			//cards[c + i]
			
		}
	}
	
	/**
	 * 取得第column列最後一張牌的位置<br>
	 * 比對放入Hashtable[table]
	 * @param column
	 * @return
	 */
	private Point getLastCardLocation(int column) {
		Point point = new Point(20 + column * 101, 25);
		PKCard card = (PKCard)this.table.get(point);
		if(card == null)
			return null;
		while(card != null) {
			point = card.getLocation();
			card = this.getNextCard(card);
		}
		return point;
	}
	
	/**
	 * 取得card下面的一張牌
	 * @param card
	 * @return
	 */
	private PKCard getNextCard(PKCard card) {
		Point point = new Point(card.getLocation());
		point.y += 5;
		card = (PKCard)this.table.get(point);
		if(card != null)
			return card;
		point.y += 15;
		card = (PKCard)this.table.get(point);
		return card;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Spider sp = new Spider();
				sp.setVisible(true);
			}
		});
	}
}
