package codespace.traffic;

import java.awt.*;

/*
	Class Holder
	This is the environment in which the animals will live.
	Very simple.
*/
public class HolderT extends Frame implements Runnable
{
	private Thread fireThread = null;
	private boolean runThread = false;

	private static final int SLEEPTIME = 25;

	private Image offScreenBuffer = null;

	private boolean over = false;

	public int carsPassed = 0;

	public HolderT()
	{
		this.setBackground(Color.black);
		this.setForeground(Color.green);

		int x = (this.getToolkit().getScreenSize().width/2) - (CommonVars.WIDTH / 2);
		int y = (this.getToolkit().getScreenSize().height/2) - (CommonVars.HEIGHT / 2);
		this.setSize(CommonVars.WIDTH, CommonVars.HEIGHT);
		this.setLocation(x, y+CommonVars.HEIGHT+10);
		this.setTitle("Environment with traffic regulation");
		this.setUndecorated(true);

		this.setVisible(true);

		initialize();

		fireThread = new Thread(this);
		runThread = true;
		fireThread.start();
	}

	private void initialize()
	{
		for(int i=0; i<CommonVars.MAX_CARS; i++)
		{
			VehicleT veh = new VehicleT();
			CommonVars.vehiclesT.add(veh);
			repaint();
			try{Thread.sleep(SLEEPTIME);}catch (Exception e){}
		}
	}

	public void paint(Graphics g)
	{
		g.drawRect( (int)CommonVars.WIDTH/2, 0, CommonVars.CAR_SIZE, this.getHeight() );
		for(int i=0; i<CommonVars.vehiclesT.size(); i++)
		{
			VehicleT vehT = CommonVars.vehiclesT.get(i);
			vehT.render(g);
			if( vehT.x > CommonVars.WIDTH/2 && vehT.x < CommonVars.WIDTH/2+CommonVars.CAR_SIZE )
			{
				carsPassed++;
				vehT.x += CommonVars.CAR_SIZE;
			}
		}
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
		while(runThread)
		{
			repaint();
			try{Thread.sleep(SLEEPTIME);}catch (Exception e){}
		}
		runThread = false;
		fireThread = null;
	}
}