/**
 * 
 */
package com.card;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * 生成新接龍遊戲的幫助欄<br>
 * @author jeff
 * @date 2015/10/09
 */
public class AboutDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3118958349843759470L;
	
	private JPanel jMainPane = null;
	
	private JTabbedPane jTabbedPane = null;
	private JPanel jPanel1 = null;
	private JPanel jPanel2 = null;
	
	private JTextArea jt1 = null;
	private JTextArea jt2 = null;
	
	/**
	 * Constructor<br>
	 * @param owner 新接龍窗窗
	 */
	public AboutDialog(JFrame owner) {
		super(owner, owner.getTitle());
		
		//setTitle("新接龍");
		setSize(300, 200);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		//位置應於遊戲視窗中間
		//方法一：用算的(x軸,y軸)搭上(長,寬)算出中心點
//		Point local = null;
//		Point ownerLocal = owner.getLocation();
//		Dimension ownerSize = owner.getSize();
//		System.out.println("Owner Point:(" + ownerLocal.x + "," + ownerLocal.y + ").");
//		System.out.println("Owner Size:(" + ownerSize.getHeight() + "," + ownerSize.getWidth() + ").");
//		
//		setLocation(local);
		
		//方法二 : setLocationRelativeTo
		setLocationRelativeTo(owner);
		
		Container c = this.getContentPane();
		
		jt1 = new JTextArea();
		jt2 = new JTextArea();
		jMainPane = new JPanel();
		jTabbedPane = new JTabbedPane();
		jPanel1 = new JPanel();
		jPanel2 = new JPanel();
		
		jt1.setSize(260, 200);
		jt2.setSize(jt1.getSize());
		
		jt1.setEditable(false);
		jt2.setEditable(jt1.isEditable());
		
		jt1.setLineWrap(true);
		jt2.setLineWrap(jt1.getLineWrap());
		
		jt1.setFont(new Font("微軟正黑體", Font.BOLD, 13));
		jt2.setFont(jt1.getFont());
		
		jt1.setForeground(Color.BLUE);
		jt2.setForeground(Color.BLACK);
		
		jt1.setText("將電腦多次分發給你的牌按照相同的花色由大至小排列起來。直到桌面上的牌全都消失。");
		jt2.setText("該遊戲中，其中一組紙牌的圖片來自於Windows XP的紙牌遊戲，另一組則來自於網路取得，勿亂發。謝謝！");
		
		jPanel1.add(jt1);
		jPanel2.add(jt2);
		
		jTabbedPane.setSize(300, 200);
		jTabbedPane.addTab("遊戲規則", jPanel1);
		jTabbedPane.addTab("聲明", jPanel2);
		
		jMainPane.add(jTabbedPane);
		
		c.add(jMainPane);
		
		pack();
		setVisible(true);
	}
	
}
