/**
 * 
 */
package com.card;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * 生成遊戲的功能表欄，實現功能表欄中各個元件的事件監聽<br>
 * TODO MenuBar上文字太小，看如何把它調大<br>
 * @author jeff
 * @date 2015/10/06
 */
public class SpiderMenuBar extends JMenuBar {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//生成spider物件
	private Spider main = null;
	//生成功能表組
	private JMenu jNewGame = null;
	private JMenu jHelp = null;
	//生成功能表項
	private JMenuItem jItemAbout = null;
	private JMenuItem jItemOpen = null;
	private JMenuItem jItemPlayAgain = null;
	//遊戲級別(單選按鈕)
	private JRadioButtonMenuItem jRMItemEasy = null;
	private JRadioButtonMenuItem jRMItemNormal = null;
	private JRadioButtonMenuItem jRMItemHard = null;
	//其它功能
	private JMenuItem jItemExit = null;
	private JMenuItem jItemValid = null;
	

	/**
	 * 
	 */
	public SpiderMenuBar(Spider spider) {
		//Initialize
		this.initUIObject();
		
		this.main = spider;
		
		//初始化遊戲功能表
		jNewGame.add(jItemOpen);
		jNewGame.add(jItemPlayAgain);
		jNewGame.add(jItemValid);
		
		jNewGame.addSeparator();
		
		jNewGame.add(jRMItemEasy);
		jNewGame.add(jRMItemNormal);
		jNewGame.add(jRMItemHard);
		
		ButtonGroup group = new ButtonGroup();
		group.add(jRMItemEasy);
		group.add(jRMItemNormal);
		group.add(jRMItemHard);
		
		jNewGame.addSeparator();
		
		jNewGame.add(jItemExit);
		
		jHelp.add(jItemAbout);
		
		this.add(jNewGame);
		this.add(jHelp);
		
		//新增各元件事件監聽
		this.addEventListener();
	}
	
	/**
	 * 初始化畫面上的UI物件
	 */
	private void initUIObject() {
		//生成功能表組
		jNewGame = new JMenu("遊戲");
		jHelp = new JMenu("設明");
		
		//生成功能表項
		jItemAbout = new JMenuItem("關於");
		jItemOpen = new JMenuItem("開局");
		jItemPlayAgain = new JMenuItem("重新發牌");
		
		//遊戲級別(單選按鈕)
		jRMItemEasy = new JRadioButtonMenuItem("簡單:單花色");
		jRMItemNormal = new JRadioButtonMenuItem("中級:雙花色");
		jRMItemHard = new JRadioButtonMenuItem("高級:四花色");
		
		//其它功能
		jItemExit = new JMenuItem("退出");
		jItemValid = new JMenuItem("顯示可行操作");
		
	}
	
	/**
	 * 添加各元件的事件監聽
	 */
	private void addEventListener() {
		//開局
		jItemOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("開局..");
				main.newGame();
			}
		});
		
		//重新發牌
		jItemPlayAgain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("重新發牌..c = " + main.getC());
				if(main.getC() < 60)
					main.deal();
			}
		});
		
		//顯示可行操作
		jItemValid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("顯示可行操作..");
				new Show().start();
			}
		});
		
		//退出
		jItemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * 釋放由此 Window、其子元件及其擁有的所有子元件所使用的所有本機螢幕資源。
				 */
				main.dispose();
				System.out.println("退出..");
				System.exit(0);
			}
		});
		
		//困難級別(default:簡單)
		jRMItemEasy.setSelected(true);
		
		//簡單
		jRMItemEasy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("簡單..");
				main.setGrade(Spider.EASY);
				main.initCards();
				main.newGame();
			}
		});
		
		//中級
		jRMItemNormal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("中級..");
				main.setGrade(Spider.NATURAL);
				main.initCards();
				main.newGame();
			}
		});
		
		//困難
		jRMItemHard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("困難..");
				main.setGrade(Spider.HARD);
				main.initCards();
				main.newGame();
			}
		});
		
		//關於
		jItemAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println("關於..");
				new AboutDialog(main);
			}
		});
		
		//遊戲 Menu
		jNewGame.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				System.out.println("jNewGame menuSelected");
				if(main.getC() < 60) {
					jItemPlayAgain.setEnabled(true);
				} else {
					jItemPlayAgain.setEnabled(false);
				}
			}
			
			@Override
			public void menuDeselected(MenuEvent e) {
				//System.out.println("jNewGame menuDeselected");
			}
			
			@Override
			public void menuCanceled(MenuEvent e) {
				//System.out.println("jNewGame menuCanceled");
			}
		});
		
	}
	
	/**
	 * 程序：顯示可以執行的操作
	 * @author jeff
	 */
	private class Show extends Thread {
		@Override
		public void run() {
			main.showEnableOperator();
		}
	}
}
