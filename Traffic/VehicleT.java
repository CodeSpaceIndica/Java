package codespace.traffic;

import java.awt.*;

/*
	Class VehicleT.
	Each car has a random speed. If a car encounters another car before it, then
	it has to slow down. If a car encounters another car too close on the sides, then
	it will swerve to avoid it.
	These cars follow traffic lane regulations
*/
public class VehicleT implements Runnable
{
	private Thread carThread = null;
	private boolean runner = false;

	private static final int SLEEP = 25;

	public int x = 0;
	public int y = 0;
	public int iX = 0;
	public int iY = 0;
	public int oX = 0;
	public int oY = 0;

	public VehicleT()
	{
		initialize();

		carThread = new Thread(this);
		runner = true;
		carThread.start();
	}

	private void initialize()
	{
		x = 0;

		do
		{
			iX = CommonVars.random.nextInt(CommonVars.MAX_SPEED );
		}while( iX < CommonVars.MIN_SPEED );
		oX = iX;
		iY=0;

		int minY = (CommonVars.HEIGHT - 25) / iX;
		int maxY = 0;
		if(iX == 1)
			maxY = CommonVars.HEIGHT + CommonVars.CAR_SIZE - 25;
		else
			maxY = (CommonVars.HEIGHT - 25) / (iX-1);
		do
		{
			y = CommonVars.random.nextInt(maxY);
		}while( y < minY );
	}

	public void render(Graphics g)
	{
		g.fillOval(x, y, CommonVars.CAR_SIZE, CommonVars.CAR_SIZE);
	}

	public void run()
	{
		while( runner )
		{
			x += iX;
			if ( x > CommonVars.WIDTH )
				initialize();
			y += iY;
			for(int i=0; i<CommonVars.vehiclesT.size(); i++)
			{
				VehicleT v = CommonVars.vehiclesT.get(i);
				if( v != this)
				{
					Rectangle rect1 = new Rectangle( v.x-5, v.y, CommonVars.CAR_SIZE+10, CommonVars.CAR_SIZE);
					Rectangle rect2 = new Rectangle( this.x, this.y, CommonVars.CAR_SIZE, CommonVars.CAR_SIZE);
					if( rect2.intersects(rect1) )
					{
						Rectangle inter = rect2.intersection(rect1);
						if( inter.x > rect2.x )
						{
							this.iX = 0;
							if(this.y < v.y)
								this.y--;
							if(this.y >= v.y)
								this.y++;
							//conflictVT++;
						}
					}
				}
			}
			if( iX < oX)
				iX++;

			try{Thread.sleep(SLEEP);}catch (Exception e){}
		}
	}
}