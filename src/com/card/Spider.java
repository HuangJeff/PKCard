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
	/**
	 * 建立位置(883, 606)，長寬(121, 96)的區域。<br>
	 * 會在此區域建立需『被拖曳』的牌組(Event)
	 */
	private JLabel clickLagel = null;
	private JLabel[] groundLabel = null;
	
	//生成紙牌陣列
	private PKCard[] cards = null;
	//初始難度
	private int grade = 0;
	
	private Hashtable<Point, PKCard> table = null;
	private int a = 0;
	private int n = 0;
	/**
	 * 記錄右下角牌組張數(6組 * 10(每組) = 60張牌)
	 */
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
		
		this.setVisible(true);
		this.deal();
		
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
				//System.out.println("clickLagel --- mouseReleased e = " + e);
				if(Spider.this.c < 60) { //右下角牌組尚未發完(c < 60)
					Spider.this.deal();
				}
			}
		});
		
		this.addKeyListener(new KeyAdapter() {
			class Show extends Thread {
				@Override
				public void run() {
					Spider.this.showEnableOperator();
				}
			}
			
			public void keyPressed(KeyEvent e) {
				System.out.println("this.addKeyListener --- keyPressed finish = " + Spider.this.finish);
				if(Spider.this.finish != 8) {
					System.out.println("this.addKeyListener --- e.getKeyCode() = " + e.getKeyCode() +
							" this.c = " + Spider.this.c);
					if(e.getKeyCode() == KeyEvent.VK_D && Spider.this.c < 60) { //右下角牌組尚未發完(c < 60)
						Spider.this.deal();
					} else if(e.getKeyCode() == KeyEvent.VK_M) {
						//按 M 鍵，提示使用都可以移動之牌
						new Show().start();
					}
				}
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
		
		//隨機紙牌初始化
		this.randomCards();
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
		//初始化待展開的紙牌(共60張牌，待展開，右下角)
		for(int i=0;i<6;i++) {
			for(int j=0;j<10;j++) {
				int _n = i * 10 + j;
				//System.out.println("初始化待展開的紙牌 _n = " + _n);
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
	public void setNA() {
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
		//處理10組牌的最後一張牌(轉正面、設定可移動、判斷若為1是否結束)
		for(int i=0;i<10;i++) {
			Point lastPoint = this.getLastCardLocation(i);
			PKCard lastCard = cards[c + i];
			//這張牌應"背面向上"
			if(c == 0) {
				lastPoint.y += 5;
			} //這張牌應"正面向上"
			else {
				lastPoint.y += 20;
			}
			
			table.remove(lastCard.getLocation());
			lastCard.moveto(lastPoint);
			table.put(lastPoint, lastCard);
			lastCard.turnFront();
			lastCard.setCanMove(true);
			
			//將元件card移動到容器中指定的順序索引
			/* setComponentZOrder()
			 * 將指定元件移動到容器中指定的 z 順序索引。
			 * z 順序確定了繪製元件的順序；具有最高 z 順序的元件將第一個繪製，而具有最低 z 順序的元件將最後一個繪製。
			 */
			this.pane.setComponentZOrder(lastCard, 1);
			
			Point point = new Point(lastPoint);
			if(lastCard.getCardValue() == 1) { //如果最後一張牌是1
				int _n = lastCard.whichColumnAvailable(point);
				point.y -= 240;
				PKCard _card = this.table.get(point);
				if(_card != null && _card.isCardCanMove()) {
					this.haveFinish(_n); //判斷是否結束(因為最後一張牌是1)
				}
			}
			x += 101;
		}
		c += 10; //右下角牌組，每次送出10張牌出來，故 c += 10
	}
	
	/**
	 * 取得第column列最後一張牌的位置<br>
	 * 比對放入Hashtable[table]
	 * @param column
	 * @return
	 */
	public Point getLastCardLocation(int column) {
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
	 * 取得傳入牌(card)的前一張牌
	 * @param card
	 * @return
	 */
	public PKCard getPreviousCard(PKCard card) {
		Point point = new Point(card.getLocation());
		point.y -= 5;
		card = table.get(point);
		if(card != null) {
			return card;
		}
		point.y -= 15;
		card = table.get(point);
		return card;
	}
	
	/**
	 * 取得傳入牌(card)的下一張牌
	 * @param card
	 * @return
	 */
	public PKCard getNextCard(PKCard card) {
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
	 * 顯示(提示)可移動的操作<br>
	 * 提示操作者那張牌可以移動。
	 */
	private void showEnableOperator() {
		int _x = 0;
		out: while(true) {
			Point point = null;
			PKCard _card = null;
			do {
				if(point != null) {
					this.n++;
				}
				point = this.getLastCardLocation(n);
				while(point == null) {
					point = this.getLastCardLocation(++n);
					if(this.n == 10)
						this.n = 0;
					_x++;
					if(_x == 10)
						break out;
				}
				_card = this.table.get(point);
			} while(!_card.isCardCanMove());
			//取得前一張可以移動的牌
			while(this.getPreviousCard(_card) != null &&
					this.getPreviousCard(_card).isCardCanMove())
			{
				_card = this.getPreviousCard(_card);
			}
			if(this.a == 10) {
				this.a = 0;
			}
			
			for(;this.a < 10;this.a++) {
				if(this.a != this.n) {
					Point _p = null;
					PKCard _c = null;
					do {
						if(_p != null) {
							this.a++;
						}
						_p = this.getLastCardLocation(a);
						int _z = 0;
						while(_p == null) {
							_p = this.getLastCardLocation(++a);
							if(this.a == 10)
								this.a = 0;
							if(this.a == this.n)
								this.a++;
							_z++;
							if(_z == 10)
								break out;
						}
						_c = this.table.get(_p);
					} while(!_c.isCardCanMove());
					if(_c.getCardValue() == _card.getCardValue() + 1)
					{
						_card.flashCard(_card);
						try {
							Thread.sleep(800);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						_c.flashCard(_c);
						this.a++;
						if(this.a == 10) {
							this.n++;
						}
						break out;
					}
				}
			}
			this.n++;
			if(this.n == 10) {
				this.n = 0;
			}
			_x++;
			if(_x == 10) {
				break out;
			}
		}
	}
	
	/**
	 * 取得groundLabel(桌面上的框框(紙牌放置處)) Location
	 * @param column
	 * @return
	 */
	public Point getGroundLabelLocation(int column) {
		return new Point(this.groundLabel[column].getLocation());
	}
	
	/**
	 * 判斷紙牌的擺放是否完成(那一列)
	 * @param column : 那一列
	 */
	private void haveFinish(int column) {
		Point point = this.getLastCardLocation(column);
		PKCard _card = this.table.get(point);
		do {
			this.table.remove(point);
			_card.moveto(new Point(20 + finish * 10, 580));
			//將元件移動到容器中指定的順序索引
			this.pane.setComponentZOrder(_card, 1);
			//將紙牌新的相關資訊存入Hashtable
			this.table.put(_card.getLocation(), _card);
			_card.setCanMove(false);
			point = this.getLastCardLocation(column);
			if(point == null)
				_card = null;
			else
				_card = this.table.get(point);
		} while(_card != null && _card.isCardCanMove());
		finish++;
		//如果8付牌全部組合成功，則顯示成功的對話方塊
		if(finish == 8) {
			JOptionPane.showMessageDialog(this, "恭喜你，順利過關！", "成功", JOptionPane.PLAIN_MESSAGE);
		}
		if(_card != null) {
			_card.turnFront();
			_card.setCanMove(true);
		}
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
