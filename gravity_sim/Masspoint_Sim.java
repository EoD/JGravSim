package gravity_sim;

public class Masspoint_Sim {
	private int iID;
	private double dMass;
	private double dRadius;
	private double dSpeedX;
	private double dSpeedY;
	private double dSpeedZ;
	private double dAccX;
	private double dAccY;
	private double dAccZ;
	private long dPosX;
	private long dPosY;
	private long dPosZ;

	public Masspoint_Sim(int iid, double mass, double radius, double speedX, double speedY, double speedZ, double accX, double accY, double accZ, long posX, long posY, long posZ) {
		iID = iid;
		dMass = mass;
		dRadius = radius;
		dSpeedX = speedX;
		dSpeedY = speedY;
		dSpeedZ = speedZ;
		dAccX = accX;
		dAccY = accY;
		dAccZ = accZ;
		dPosX = posX;
		dPosY = posY;
		dPosZ = posZ;
	}
	
	@Override
	public String toString() {
		return ("Objekt "+iID);
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
	public long getPosX() {
		return dPosX;
	}
	public long getPosY() {
		return dPosY;
	}
	public long getPosZ() {
		return dPosZ;
	}
	public double getRadius() {
		return dRadius;
	}
	public double getSpeedX() {
		return dSpeedX;
	}
	public double getSpeedY() {
		return dSpeedY;
	}
	public double getSpeedZ() {
		return dSpeedZ;
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
		MDVector mdvspeed = new MDVector(dSpeedX, dSpeedY, dSpeedZ);

		//falls der v-vektor (0|0|0) ist, ist auch v=0
		if(mdvspeed.abs() <= 0) 
			return dMass;
			
		return ( dMass*gamma(mdvspeed.abs()));
	}
	
	public double getSchwarzschildRadius() {
		//r = 2Gm / c^2
		return ((2.0*CalcCode.GRAVCONST*this.getSRTMass())/Math.pow(CalcCode.LIGHTSPEED, 2.0));
	}
}
