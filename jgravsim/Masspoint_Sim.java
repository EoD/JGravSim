package jgravsim;

public class Masspoint_Sim implements Comparable<Masspoint_Sim> {
	private int iID;
	private double dMass;
	private double dRadius;
	private double dSpeedVecX = 0.0;
	private double dSpeedVecY = 0.0;
	private double dSpeedVecZ = 0.0;
	private double dSpeed;
	private double dAccX;
	private double dAccY;
	private double dAccZ;
	private long dPosX;
	private long dPosY;
	private long dPosZ;
	private int highlighted;
	public static XMLParser myXMLParser;

	public Masspoint_Sim(int iid, double mass, double radius, double speed, double speedX, double speedY, double speedZ, double accX, double accY, double accZ, long posX, long posY, long posZ) {
		iID = iid;
		dMass = mass;
		dRadius = radius;
		
		if(!Double.isNaN(speedX))
			dSpeedVecX = speedX;
		
		if(!Double.isNaN(speedY))
			dSpeedVecY = speedY;
		
		if(!Double.isNaN(speedZ))
			dSpeedVecZ = speedZ;

		dSpeed = speed;
		dAccX = accX;
		dAccY = accY;
		dAccZ = accZ;
		dPosX = posX;
		dPosY = posY;
		dPosZ = posZ;
		highlighted = 0;
	}
	
	@Override
	public String toString() {
		return myXMLParser.getText(256)+" "+iID;
	}

	public void setDrawStatus(int a) {
		highlighted = a;
	}
	
	public void setInvisible(boolean b) {
		if(b)
			highlighted = -1;
		else
			highlighted = 0;
	}
	public void setHighlighted(boolean b) {
		if(b)
			highlighted = 1;
		else
			highlighted = 0;
	}
	public int getDrawStatus() {
		return highlighted;
	}
	
	public boolean isHighlighted() {
		if(highlighted == 1)
			return true;
		else
			return false;
	}
	public boolean isInvisible() {
		if(highlighted == -1)
			return true;
		else
			return false;
	}
	
	public double getAccX() {
		return dAccX;
	}
	public double getAccY() {
		return dAccY;
	}
	public double getAccZ() {
		return dAccZ;
	}
	public double getMass() {
		return dMass;
	}
	public MLVector getPos() {
		return new MLVector(dPosX, dPosY, dPosZ);
	}
	public long getPosX() {
		return dPosX;
	}
	public long getPosY() {
		return dPosY;
	}
	public long getPosZ() {
		return dPosZ;
	}
	//Unit [m]
	public double getAbsRadius() {
		return dRadius;
	}
	public double getSpeedX() {
		if(dSpeed <= 0 || dSpeedVecX == 0.0)
			return 0.0;
		return dSpeedVecX*dSpeed;
	}
	public double getSpeedY() {
		if(dSpeed <= 0 || dSpeedVecY == 0.0)
			return 0.0;
		return dSpeedVecY*dSpeed;
	}
	public double getSpeedZ() {
		if(dSpeed <= 0 || dSpeedVecZ == 0.0)
			return 0.0;
		return dSpeedVecZ*dSpeed;
	}
	public double getSpeed() {
		return dSpeed;
	}
	public double getSpeedVecX() {
		return dSpeedVecX;
	}
	public double getSpeedVecY() {
		return dSpeedVecY;
	}
	public double getSpeedVecZ() {
		return dSpeedVecZ;
	}
	public int getID() {
		return iID;
	}	
	
	//not required yet
/*	public Masspoint toMasspoint() {
		Masspoint mptemp = new Masspoint(iID, dPosX, dPosY, dPosZ);
		mptemp.setSpeedx(dSpeedX);
		mptemp.setSpeedx(dSpeedY);
		mptemp.setSpeedx(dSpeedZ);
		mptemp.setRadius(dRadius);
		mptemp.setMass(dMass);
		return mptemp;
	}
*/	
	public static boolean SpeedBarrierCheck(String name,double speed) {
		return Masspoint.SpeedBarrierCheck(name, speed);
	}
	
	//Lorentz factor (SRT)
	static public double gamma(double v) { 
		//debugout(" Gamma() - v="+v);
		SpeedBarrierCheck("gamma",v);
		double sqrt = Math.sqrt(1.0 - Math.pow(v/(double)CalcCode.LIGHTSPEED,2.0));
		//Controller.debugout(" Gamma() - sqrt="+sqrt);
		return (1.0/sqrt);
	}
	
	public double getSRTMass() {
		MDVector mdvspeed = new MDVector(dSpeedVecX*dSpeed, dSpeedVecY*dSpeed, dSpeedVecZ*dSpeed);

		//falls der v-vektor (0|0|0) ist, ist auch v=0
		if(mdvspeed.abs() <= 0) 
			return dMass;

		//Controller.debugout("getSRTMass() - dMass="+dMass+", mdvspeed.abs()="+mdvspeed.abs());
		return ( dMass*gamma(mdvspeed.abs()));
	}
	
	
	//Unit [m]
	public double getSchwarzschildRadius() {
		//r = 2Gm_0 / c^2
		return ((2.0*CalcCode.GRAVCONST*dMass)/Math.pow(CalcCode.LIGHTSPEED, 2.0));
	}
	
	public boolean isBlackHole() {
		if(getSchwarzschildRadius() >= getAbsRadius())
			return true;
		else
			return false;
	}

	//Unit [m]
		public double getRadius() {
		if(isBlackHole())
			return getSchwarzschildRadius();
		else
			return getAbsRadius();
	}
	public void setSpeedVecNotNull() {
		dSpeedVecX = 1.0;
		dSpeedVecY = 1.0;
		dSpeedVecZ = 1.0;
	}
	public boolean isSpeedVecNull() {
		if(dSpeedVecX == 0 && dSpeedVecY == 0 && dSpeedVecZ == 0)
			return true;
		else 
			return false;
	}
	
	public int compareTo(Masspoint_Sim mp2) {
		if(mp2.getID() < iID)
			return -1;
		else if(mp2.getID() > iID)
			return 1;
		else
			return 0;
	}
}
