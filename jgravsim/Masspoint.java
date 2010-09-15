package jgravsim;

public class Masspoint {
	
	public static final int revision = 1;
	public static final boolean DEBUG = false;
	public static final double DFTRADIUS = 6371010;	//Earth Radius (m)
	public static final double DFTMASS = CalcCode.EM;	//Earth Mass (kg)
	public static XMLParser myXMLParser;
	
	protected int iID;
	protected String sName;
	protected double dMass;
	protected double dRadius;
	protected double dSpeed;	
	protected double dSpeedVecX = 0.0;
	protected double dSpeedVecY = 0.0;
	protected double dSpeedVecZ = 0.0;
	protected long dPosX;
	protected long dPosY;
	protected long dPosZ;
	
	public Masspoint(int iid, double mass, double radius, double speed, double speedX, double speedY, double speedZ, long posX, long posY, long posZ) {
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
		dPosX = posX;
		dPosY = posY;
		dPosZ = posZ;
	}
	public Masspoint(int kid, long lx, long ly ,long lz) {
		iID = kid;
		dPosX = lx;
		dPosY = ly;
		dPosZ = lz;
		dSpeedVecX = dSpeedVecY = dSpeedVecZ = Math.sqrt(3.0)/3;
		dSpeed = 0.0;
		dMass = DFTMASS; 		//eg: sun = 1.99E30 kg
		dRadius = DFTRADIUS;
	}	
	public Masspoint(int kid, long lx, long ly ,long lz, Masspoint mp_clone) {
		iID = kid;
		dPosX = lx;
		dPosY = ly;
		dPosZ = lz;
		dSpeedVecX = mp_clone.getSpeedVecX();
		dSpeedVecY = mp_clone.getSpeedVecY();
		dSpeedVecZ = mp_clone.getSpeedVecZ();
		dSpeed = mp_clone.dSpeed;
		dMass = mp_clone.dMass; 		//eg: sun = 1.99E30 kg
		dRadius = mp_clone.dRadius;
	}	
	public Masspoint(double dmass, double dradius) {
		iID = -1;
		dPosX = dPosY = dPosZ = 0;
		dSpeedVecX = dSpeedVecY = dSpeedVecZ = 0;
		dSpeed = 0;
		dMass = dmass; 		//eg: sun = 1.99E30 kg
		dRadius = dradius;
	}

	@Override
	public String toString() {
		if(sName == null || sName.isEmpty() || sName == "")
			return myXMLParser.getText(256)+" "+iID;
		else
			return sName;
	}
	
	@SuppressWarnings("unused")
	protected static void debugout(String a) {
		if(Controller.CURRENTBUILD && DEBUG)
			System.out.println(a);
	}

	public void setID(int id) {
		iID = id;		
	}
	public int getID() {
		return iID;
	}
	
	public void setName(String name) {
		sName = name;
	}
	public String getName() {
		return sName;
	}
	
	public static boolean SpeedBarrierCheck(String name,double speed) {
		if(speed > CalcCode.LIGHTSPEED) {
			debugout("Masspoint() - ERROR "+name+" > c");
			return true;
		}
		return false;
	}
	public boolean addSpeed(double dvx, double dvy, double dvz) {
		MDVector mdvspeed = getMDVSpeed();
		mdvspeed.x1 += dvx;
		mdvspeed.x2 += dvy;
		mdvspeed.x3 += dvz;
		
		if(SpeedBarrierCheck("addSpeedxyz",mdvspeed.abs()))
			return false;
		
		setSpeed(mdvspeed);
		return true;
	}	
	public boolean addSpeed(MDVector a) {
		MDVector mdvspeed = getMDVSpeed();
		mdvspeed = MVMath.AddMV(a, mdvspeed);
		
		if(SpeedBarrierCheck("addSpeedmdv2",mdvspeed.abs()))
			return false;
		
		setSpeed(mdvspeed);
		return true;
	}
	public boolean setSpeed(MDVector a) {
		if(SpeedBarrierCheck("setSpeed",a.abs()))
			return false;

		dSpeed = a.abs();
		MDVector unita = a.UnitVec();
		dSpeedVecX = unita.x1;
		dSpeedVecY = unita.x2;
		dSpeedVecZ = unita.x3;
		return true;
	}	
	public boolean setAbsSpeed(double a) {
		if(SpeedBarrierCheck("setSpeed",a))
			return false;
		dSpeed = a;
		return true;
	}
	public boolean setSpeedx(double a) {
		//debugout("Updating speed for "+id);
		MDVector mdvspeed = getMDVSpeed();
		mdvspeed.x1 = a;
		if(SpeedBarrierCheck("setSpeedx",mdvspeed.abs()))
			return false;
		
		setSpeed(mdvspeed);
		return true;
	}	
	public boolean setSpeedy(double a) {
		//debugout("Updating speed for "+id);
		MDVector mdvspeed = getMDVSpeed();
		mdvspeed.x2 = a;		
		if(SpeedBarrierCheck("setSpeedy",mdvspeed.abs()))
			return false;
		
		setSpeed(mdvspeed);
		return true;
	}	
	public boolean setSpeedz(double a) {
		//debugout("Updating speed for "+id);
		MDVector mdvspeed = getMDVSpeed();
		mdvspeed.x3 = a;
		if(SpeedBarrierCheck("setSpeedz",mdvspeed.abs()))
			return false;
		
		setSpeed(mdvspeed);
		return true;
	}	
	
	public double getSpeed() {
		return dSpeed;
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
	public double getSpeedVecX() {
		return dSpeedVecX;
	}
	public double getSpeedVecY() {
		return dSpeedVecY;
	}
	public double getSpeedVecZ() {
		return dSpeedVecZ;
	}
	public MDVector getMDVSpeed() {
		return new MDVector(dSpeedVecX * dSpeed, dSpeedVecY * dSpeed, dSpeedVecZ * dSpeed);
	}
	
	public void setMass(double a) {
		//debugout("Updating mass for "+id);
		dMass = a;
	}
	/**
	 * @return absolute mass
	 */
	public double getAbsMass() {
		return dMass;
	}
	/**
	 * @deprecated As of build 66, replaced by {@link #getAbsMass()}
	 * @return absolute mass
	 */
	@Deprecated public double getMass() {
		return dMass;
	}
	public double getSRTMass() {
		//falls der v-vektor (0|0|0) ist, ist auch v=0
		if(dSpeed == 0 || (dSpeedVecX == 0 && dSpeedVecY == 0 && dSpeedVecZ == 0) ) 
			return dMass;
			
		return ( dMass*gamma(dSpeed));
	}
	
	public void addMLVCoord(MLVector ds) {
		dPosX += ds.x1;
		dPosY += ds.x2;
		dPosZ += ds.x3;
	}	
	public void setCoordx(long a) {
		dPosX = a;
	}	
	public void setCoordy(long a) {
		dPosY = a;
	}	
	public void setCoordz(long a) {
		dPosZ = a;
	}	
	public void setCoordMLV(MLVector mlvl) {
		dPosX = mlvl.x1;
		dPosY = mlvl.x2;
		dPosZ = mlvl.x3;
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

	
	public void setAbsRadius(double a) {
		debugout("Masspoint - new Radius="+a);
		dRadius = a;
	}
	/** Absolute radius of masspoint in [m]
	 * 
	 * @return absolute radius
	 */
	public double getAbsRadius() {
		return dRadius;
	}
	public double getDensity() {
		return (dMass / getAbsVolume());
	}
	public double getAbsVolume() {
		return (4.0/3.0*Math.pow(dRadius, 3.0)*Math.PI);
	}
	
	/** Calculates the schwarzschild radius of the masspoint
	 * <p>
	 * Zu beachten ist ferner, dass der Schwarzschildradius in der allgemeinen
	 * Relativitätstheorie nicht den Abstand vom Mittelpunkt angibt, sondern
	 * über die Oberfläche von Kugeln definiert ist. Ein kugelförmiger
	 * Ereignishorizont mit Radius rS hat dieselbe Fläche wie eine Sphäre
	 * gleichen Radius im euklidischen Raum, nämlich A = 4πr2. Aufgrund der
	 * Raumzeitkrümmung sind die radialen Abstände im Gravitationsfeld
	 * vergrößert (sprich: der Abstand zweier Kugelschalen mit – über die
	 * Kugelfläche definierten – Radialkoordinaten r1 und r2 ist größer als die
	 * Differenz dieser Radien).<p>
	 * http://de.wikipedia.org/wiki/Ereignishorizont#Schwarzschild-Radius_und_Gravitationsradius
	 * 
	 * @return schwarzschild radius (2 * G*m0 / c^2)
	 */
	public double getSchwarzschildRadius() {
		return (2.0*CalcCode.GRAVCONST*dMass)/Math.pow(CalcCode.LIGHTSPEED, 2.0);
	}
	/** Calculates the schwarzschild volume in [m]
	 * 
	 * @return schwarzschild volume
	 */
	public double getSchwarzschildVolume() {
		return (4.0/3.0*Math.pow(getSchwarzschildRadius(), 3.0)*Math.PI);
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
	//Unit [m]
	public double getVolume() {
		if(isBlackHole()) 
			return getSchwarzschildVolume();
		else
			return getAbsVolume();
	}
	
	
	//setData v2
	public void setData(Masspoint mpdata) {
		iID = mpdata.iID;
		dMass = mpdata.dMass;
		dRadius = mpdata.dRadius;
		dPosX = mpdata.getPosX();
		dPosY = mpdata.getPosY();
		dPosZ = mpdata.getPosZ();
		dSpeed = mpdata.dSpeed;
		dSpeedVecX = mpdata.getSpeedVecX();
		dSpeedVecY = mpdata.getSpeedVecY();
		dSpeedVecZ = mpdata.getSpeedVecZ();
	}
	
	public void remove() {
		dMass =	dRadius = 0;
		dSpeed = 0;
		dSpeedVecX = dSpeedVecY = dSpeedVecZ = 0;
		dPosX = dPosY = dPosZ = 0;
	}
	
	//Lorentz factor (SRT)
	static public double gamma(double v) { 
		if(SpeedBarrierCheck("gamma",v))
			return 0.0;
		debugout(" Gamma() - v="+v);
		double sqrt = Math.sqrt(1.0 - Math.pow(v/(double)CalcCode.LIGHTSPEED,2.0));
		debugout(" Gamma() - gamma="+(1.0/sqrt));
		return (1.0/sqrt);
	}
	
	public double drange(Masspoint mp2) {
		MLVector mlvrange = MVMath.SubMV(this.getPos(), mp2.getPos());	
		return (MVMath.ConvertToD(mlvrange)).abs();
	}
	
	public Masspoint_Sim toMasspoint_Sim() {
		return new Masspoint_Sim(iID, dMass, dRadius, dSpeed, dSpeedVecX, dSpeedVecY, dSpeedVecZ, dPosX, dPosY, dPosZ);
	}
	public void setUnitSpeed(double x, double y, double z) {
		MDVector mdvUnitSpeed = new MDVector(x,y,z);
		if(mdvUnitSpeed.abs() != 1.0) {
			debugout("setUnitSpeed() - WARNING - you are adding (x,y,z) which is no unit vector");
			dSpeedVecX = mdvUnitSpeed.UnitVec().x1;
			dSpeedVecY = mdvUnitSpeed.UnitVec().x2;
			dSpeedVecZ = mdvUnitSpeed.UnitVec().x3;
		}
		else {
			dSpeedVecX = x;
			dSpeedVecY = y;
			dSpeedVecZ = z;
		}
	}
	
	/*public void setUnitSpeedxyAngle(double a) {
		double factor = 1.0 - mdvunitspeed.x3 * mdvunitspeed.x3;
		double dx1 = Math.cos(Math.toRadians(a) * factor);
		double dx2 = Math.sin(Math.toRadians(a) * factor);
		mdvunitspeed = new MDVector(dx1, dx2, mdvunitspeed.x3);
	}
	public void setUnitSpeedxzAngle(double a) {
		double factor = 1.0 - mdvunitspeed.x2 * mdvunitspeed.x2;
		double dx1 = Math.cos(Math.toRadians(a) * factor);
		double dx3 = Math.sin(Math.toRadians(a) * factor);
		mdvunitspeed = new MDVector(dx1, mdvunitspeed.x2, dx3);
	}
	public double[] getUnitSpeedinPlane(char[] cAxes) {
		double x1 = 0.0;
		double x2 = 0.0;
		double x3 = 0.0;
		
		switch(cAxes[0]) {
		case 'x': x1 = mdvunitspeed.x1; break;
		case 'y': x2 = mdvunitspeed.x2; break;
		case 'z': x3 = mdvunitspeed.x3; break;
		default: break;
		}
		switch(cAxes[1]) {
		case 'x': x1 = mdvunitspeed.x1; break;
		case 'y': x2 = mdvunitspeed.x2; break;
		case 'z': x3 = mdvunitspeed.x3; break;
		default: break;
		}
		
		MDVector mdvtemp = new MDVector(x1, x2, x3);
		mdvtemp = mdvtemp.UnitVec();
		double[] out = new double[2];
		if(x1 == 0.0) {
			out[0] = mdvtemp.x2;
			out[1] = mdvtemp.x3;
		} else if(x2 == 0.0) {
			out[0] = mdvtemp.x1;
			out[1] = mdvtemp.x3;
		} else if(x3 == 0.0) {
			out[0] = mdvtemp.x1;
			out[1] = mdvtemp.x2;
		}
		else {
			return null;
		}
		return out;
	}*/
	public void setUnitSpeedAngle(float theta, float phi) {
		double dtheta = Math.toRadians(theta);
		double dphi = Math.toRadians(phi);
		double dx1;
		double dx2;
		double dx3;
		if(theta == 90) {
			if(phi == 90 || phi == 270 || phi == -90)
				dx1 = 0.0;
			else
				dx1 = Math.cos(dphi);
			
			if(phi == 180 || phi == -0 || phi == 0)
				dx2 = 0.0;
			else
				dx2 = Math.sin(dphi);
			
			dx3 = 0.0;
		}
		else if(theta == 0) {
			dx1 = 0.0;
			dx2 = 0.0;
			dx3 = 1.0;
		}
		else if(theta == 180){
			dx1 = 0.0;
			dx2 = 0.0;
			dx3 = -1.0;
		}
		else if(phi == 0 || phi == 180) {
			dx1 = Math.sin(dtheta) * Math.cos(dphi);
			dx2 = 0.0;
			dx3 = Math.cos(dtheta);
		}
		else if(phi == 90 || phi == 270 || phi == -90) {
			dx1 = 0.0;
			dx2 = Math.sin(dtheta) * Math.sin(dphi);
			dx3 = Math.cos(dtheta);
		}
		else {
			dx1 = Math.sin(dtheta) * Math.cos(dphi);
			dx2 = Math.sin(dtheta) * Math.sin(dphi);
			dx3 = Math.cos(dtheta);
		}
		dSpeedVecX = dx1;
		dSpeedVecY = dx2;
		dSpeedVecZ = dx3;
	}
	public float[] getUnitSpeedAngle() {
		float[] angles = new float[2];
		double theta = 0.0;
		double phi = 0.0;
		double temp = 1.0; //==mdvunitspeed.abs()
		//if(mdvunitspeed.x1 == 0 && mdvunitspeed.x2 == 0){
		//	angles[0] = (float)(0.0);
		//	angles[1] = (float)(0.0);
		//	return angles;
		//}
		//temp = Math.sqrt(mdvunitspeed.x1*mdvunitspeed.x1 + mdvunitspeed.x2*mdvunitspeed.x2);
		//theta = Math.PI / 2.0;
		//theta -= Math.atan(mdvunitspeed.x3 / temp);
		/*if(mdvunitspeed.x2 < 0) {
			phi = Math.PI * 2.0;
			phi -= Math.acos(mdvunitspeed.x1 / temp);
		} else {
			phi = Math.acos(mdvunitspeed.x1 / temp);
		}*/
		theta = Math.acos(dSpeedVecZ / temp);
		phi = Math.atan2(dSpeedVecY, dSpeedVecX);

			
		angles[0] = (float)Math.toDegrees(theta);
		if(phi < 0)
			angles[1] = 360.0f + (float)Math.toDegrees(phi);
		else 
			angles[1] = (float)Math.toDegrees(phi);
		return angles;
	}
	
	/**
	 * Returns the relativistic impulse for the masspoint
	 */
	public MDVector getImpulse() {
		return MVMath.ProMVNum(getMDVSpeed(), getSRTMass());	//momentum = gamma*absmass*speed
	}

	/**
	 * Returns the relativistic energy for the masspoint
	 */
	public double getEnergy() {
		double Energy = dMass* CalcCode.LIGHTSPEED * CalcCode.LIGHTSPEED;
		Energy *= Energy;
		Energy += CalcCode.LIGHTSPEED * CalcCode.LIGHTSPEED + MVMath.ProScaMV(getImpulse(), getImpulse());
		return Math.sqrt(Energy);
	}
}