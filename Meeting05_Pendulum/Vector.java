package Meeting05_Pendulum;


public class Vector {
	double x;			// the x component of the vector
	double y;			// the y component of the vector
	
	public Vector() {
		x = 0;
		y = 0;		
	}
	
	public Vector(double x, double y)
	{		
		setV(x,y);
	}		
	
	public double getLength()
	{
		//return 1f / invSqrt((float)(x*x + y*y));		// this is an estimation, so it won't give an exact result
		return Math.sqrt(x*x + y*y);
	}
	
	public double getX()
	{
		return x;
	}	
	public double getY()
	{
		return y;
	}
	public double getAngle()
	{
		return Math.atan2(y, x);
	}
	public void setX(double x)
	{
		setV(x, this.y);
	}
	public void setY(double y)
	{
		setV(this.x, y);
	}
	public void setV(double x, double y)
	{
		this.x = x;
		this.y = y;		
	}
	
	/**
	 * get the unit vector of this vector
	 * @return the unit vector with the same direction as this vector
	 */
	public Vector unitVector()
	{
		return new Vector(this.x/getLength(), this.y/getLength());
	}

	public void add(double x, double y)
	{
		setV(this.x + x, this.y + y);		
	}
	/**
	 * multiply this vector with some scalar
	 * @param scalar the constant to scale this vector
	 */
	public void multiply(double scalar)
	{
		x *= scalar;
		y *= scalar;
	}
	public double dotProduct(Vector vector)
	{
		return getX() * vector.getX() + getY() * vector.getY();
	}
	
	/**
	 * get a vector normal to this vector with by rotating this vector 90 degrees (according to general standard on cartesius coordinate) 
	 * @return a vector normal to this vector
	 */
	public Vector normalVector()
	{
		return new Vector(-getY(), getX());
	}
			
	
}
