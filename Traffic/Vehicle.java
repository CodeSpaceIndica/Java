package codespace.traffic;

import java.awt.*;

/*
	Class Car.
	Each car has a random speed. If a car encounters another car before it, then
	it has to slow down. If a car encounters another car too close on the sides, then
	it will swerve to avoid it.
*/
public class Vehicle implements Runnable
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

	public Vehicle()
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
			y = CommonVars.random.nextInt(CommonVars.HEIGHT);
		}while( y < CommonVars.CAR_SIZE );

		do
		{
			iX = CommonVars.random.nextInt(CommonVars.MAX_SPEED);
		}while( iX < CommonVars.MIN_SPEED );
		oX = iX;
		iY=0;
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
			if ( y > CommonVars.HEIGHT )
				y--;
			else if( y <= 0 )
				y++;
			for(int i=0; i<CommonVars.vehicles.size(); i++)
			{
				Vehicle v = CommonVars.vehicles.get(i);
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
							//conflictV++;
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