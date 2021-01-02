package codespace.traffic;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class Main extends Frame implements Runnable, WindowListener
{
	private Thread thr = null;
	private boolean runThread = false;

	private static final int SLEEPTIME = 100;

	private Holder h = null;
	private HolderT ht = null;

	private ArrayList<Integer> vech = new ArrayList<Integer>();
	private ArrayList<Integer> vecht = new ArrayList<Integer>();

	private ArrayList<Double> spd = new ArrayList<Double>();
	private ArrayList<Double> spdT = new ArrayList<Double>();

	private double minSpeed = Double.MAX_VALUE;
	private double maxSpeed = Double.MIN_VALUE;
	private double minSpeedT = Double.MAX_VALUE;
	private double maxSpeedT = Double.MIN_VALUE;

	private static final int HEIGHT = 180;
	private static final int WIDTH = 300;
	private static final int MINH = 90;
	private static final int MINH2 = 140;
	private static final int WAIT_PERIOD = 0;//5 seconds wait before starting current thread.

	private Image offScreenBuffer = null;

	public Main()
	{
		this.setBackground(Color.black);
		this.setForeground(Color.white);

		this.setSize(WIDTH, HEIGHT);
		this.setLocation(20, 20);
		this.setTitle("Main");
		this.setResizable(false);

		this.addWindowListener(this);

		this.setVisible(true);

		h = new Holder();
		ht = new HolderT();

		thr = new Thread(this);
		runThread = true;
		thr.start();
	}

	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowClosing(WindowEvent e)
	{
		h.dispose();
		ht.dispose();
		this.dispose();
		System.exit(0);
	}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}

	public void paint(Graphics g)
	{
		if(h == null || ht == null)
			return;
		String hStr  = "Cars Passed :" + String.valueOf(h.carsPassed);
		String htStr = "Cars Passed :" + String.valueOf(ht.carsPassed);

		vech.add( new Integer(h.carsPassed) );
		vecht.add( new Integer(ht.carsPassed) );

		int totalSpeed = 0;
		double avgSpeed = 0;
		for(int i=0; i<CommonVars.vehicles.size(); i++)
		{
			int speed = CommonVars.vehicles.get(i).iX;
			totalSpeed += speed;
		}
		avgSpeed = (double)totalSpeed / (double)CommonVars.vehicles.size();
		minSpeed = avgSpeed < minSpeed ? avgSpeed : minSpeed;
		maxSpeed = avgSpeed > maxSpeed ? avgSpeed : maxSpeed;
		spd.add( new Double(avgSpeed) );
		String speedStr = "Avg Speed = " + String.valueOf(avgSpeed);
		String minSpeedStr = "Min = " + String.valueOf(minSpeed);
		String maxSpeedStr = "Max = " + String.valueOf(maxSpeed);

		totalSpeed = 0;
		avgSpeed = 0;
		for(int i=0; i<CommonVars.vehiclesT.size(); i++)
		{
			int speed = CommonVars.vehiclesT.get(i).iX;
			totalSpeed += speed;
		}
		avgSpeed = (double)totalSpeed / (double)CommonVars.vehiclesT.size();
		minSpeedT = avgSpeed < minSpeedT ? avgSpeed : minSpeedT;
		maxSpeedT = avgSpeed > maxSpeedT ? avgSpeed : maxSpeedT;
		spdT.add( new Double(avgSpeed) );
		String speedStrT = "Avg Speed = " + String.valueOf(avgSpeed);
		String minSpeedStrT = "Min = " + String.valueOf(minSpeedT);
		String maxSpeedStrT = "Max = " + String.valueOf(maxSpeedT);
//NO TRAFFIC REGULATIONS
		g.setColor(Color.red);
		g.drawString(hStr, 5, 60);
		g.drawString(speedStr, 180, 60);
		g.drawString(minSpeedStr, 5, 150);
		g.drawString(maxSpeedStr, 5, 163);
		int x = 0;
		int y = this.HEIGHT;
		int pX=x;
		int pY=y;
		int pixDiff = MINH - HEIGHT;
		for(int i=0; i<vech.size()-1; i++)
		{
			int diff = vech.get(i+1).intValue() - vech.get(i).intValue();
			y = (diff*pixDiff) / CommonVars.MAX_CARS;
			y+=MINH;
			x+=2;
			g.drawLine(x, y, pX, pY);
			pX = x;
			pY = y;
		}
		for(;x > this.getSize().getWidth();x--)
			vech.remove(0);

		x = 0;
		y = this.HEIGHT;;
		pX = x;
		pY = y;
		int pixDiff2 = MINH2 - HEIGHT;
		for(int i=0; i<spd.size(); i++)
		{
			double val = spd.get(i).doubleValue();
			y = (int)((val*pixDiff2) / CommonVars.MAX_SPEED);
			y+=MINH2;
			x+=2;
			g.drawLine(x, y, pX, pY);
			pX = x;
			pY = y;
		}
		for(;x > this.getSize().getWidth();x--)
			spd.remove(0);
//TRAFFIC REGULATIONS
		g.setColor(Color.green);
		g.drawString(htStr, 5, 73);
		g.drawString(speedStrT, 180, 73);
		g.drawString(minSpeedStrT, 90, 150);
		g.drawString(maxSpeedStrT, 90, 163);
		x = 0;
		y = this.HEIGHT;;
		pX = x;
		pY = y;
		for(int i=0; i<vecht.size()-1; i++)
		{
			int diff = vecht.get(i+1).intValue() - vecht.get(i).intValue();
			y = (diff*pixDiff) / CommonVars.MAX_CARS;
			y+=MINH+20;
			x+=2;
			g.drawLine(x, y, pX, pY);
			pX = x;
			pY = y;
			if(x > this.getSize().getWidth())
				vecht.remove(0);
		}
		x = 0;
		y = this.HEIGHT;;
		pX = x;
		pY = y;
		for(int i=0; i<spdT.size(); i++)
		{
			double val = spdT.get(i).doubleValue();
			y = (int)((val*pixDiff2) / CommonVars.MAX_SPEED);
			y+=MINH2;
			x+=2;
			g.drawLine(x, y, pX, pY);
			pX = x;
			pY = y;
			if(x > this.getSize().getWidth())
				spdT.remove(0);
		}
//Lines
		g.setColor(Color.gray);
		g.drawLine(0, MINH+25, WIDTH, MINH+25);
	}

	public void update(Graphics g)
	{
		Graphics gr;
		if ( offScreenBuffer==null )
			offScreenBuffer = this.createImage(this.getWidth(), this.getHeight());

		gr = offScreenBuffer.getGraphics();
		gr.setColor(getBackground());
		gr.fillRect(0, 0, this.getWidth(), this.getHeight());

		gr.setColor(getForeground());
		paint(gr);

		g.drawImage(offScreenBuffer, 0, 0, this);
	}

	public void run()
	{
		try{Thread.sleep(WAIT_PERIOD);}catch (Exception e){}
		while(runThread)
		{
			repaint();
			try{Thread.sleep(SLEEPTIME);}catch (Exception e){}
		}
		runThread = false;
		thr = null;
	}

	public static void main(String s[]) throws Exception
	{
		new Main();
	}
}
