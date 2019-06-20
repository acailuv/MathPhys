package Meeting06_Billiard;

public class Vector {
	double x;
	double y;
	
	public Vector()
	{
		x = 0;
		y = 0;		
	}
	
	public Vector(double x, double y)
	{
		this.x = x;
		this.y = y;
	}		
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public void setX(double newX)
	{
		x = newX;
	}
	
	public void setY(double newY)
	{
		y = newY;
	}
}
