package jgravsim;

public class Masspoint {
	
	public static boolean DEBUG = false;
	static final double DFTRADIUS = 6371010;	//Earth Radius (m)
	static final double DFTMASS = CalcCode.EM;	//Earth Mass (kg)
	
	int id;
	String name;
	MLVector mlvpos;	//position in mm
	double mass;
	double radius;
	MDVector mdvunitspeed;
	double dabsspeed;
	//direction
	
	public Masspoint(int kid, long lx, long ly ,long lz) {
		id = kid;
		mlvpos = new MLVector(lx,ly,lz);
		mdvunitspeed = new MDVector(Math.sqrt(3.0)/3,Math.sqrt(3.0)/3,Math.sqrt(3.0)/3);
		dabsspeed = 0.0;
		mass = DFTMASS; 		//eg: sun = 1.99E30 kg
		radius = DFTRADIUS;
	}	
	public Masspoint(int kid, long lx, long ly ,long lz, Masspoint mp_clone) {
		id = kid;
		mlvpos = new MLVector(lx, ly, lz);
		mdvunitspeed = new MDVector(mp_clone.mdvunitspeed.x1, mp_clone.mdvunitspeed.x2, mp_clone.mdvunitspeed.x3);
		dabsspeed = mp_clone.dabsspeed;
		mass = mp_clone.mass; 		//eg: sun = 1.99E30 kg
		radius = mp_clone.radius;
	}	
	public Masspoint(double dmass, double dradius) {
		id = -1;
		mlvpos = new MLVector(0, 0, 0);
		mdvunitspeed = new MDVector(0, 0, 0);
		dabsspeed = 0;
		mass = dmass; 		//eg: sun = 1.99E30 kg
		radius = dradius;
	}

	@Override
	public String toString() {
		if(name == null || name.isEmpty() || name == "")
			return ("Objekt "+id);
		else
			return name;
	}
	
	private static void debugout(String a) {
		if(Controller.CURRENTBUILD && DEBUG)
			System.out.println(a);
	}
	
	public static boolean SpeedBarrierCheck(String name,double speed) {
		if(speed > CalcCode.LIGHTSPEED) {
			debugout("Masspoint() - ERROR "+name+" > c");
			return true;
		}
		return false;
	}
	
	public boolean addSpeed(double dvx, double dvy, double dvz) {
		MDVector mdvspeed = MVMath.ProMVNum(mdvunitspeed, dabsspeed);
		mdvspeed.x1 += dvx;
		mdvspeed.x2 += dvy;
		mdvspeed.x3 += dvz;
		if(SpeedBarrierCheck("addSpeedxyz",mdvspeed.abs()))
			return false;		
		dabsspeed = mdvspeed.abs();
		mdvunitspeed = mdvspeed.UnitVec();		
		return true;
	}	

	public boolean addSpeed(MDVector a) {
		MDVector mdvspeed = MVMath.ProMVNum(mdvunitspeed, dabsspeed);
		if(SpeedBarrierCheck("addSpeedmdv2",dabsspeed))
			return false;
		mdvspeed = MVMath.AddMV(a, mdvspeed);
		dabsspeed = mdvspeed.abs();
		mdvunitspeed = mdvspeed.UnitVec();
		return true;
	}
	
	public boolean setSpeed(MDVector a) {
		if(SpeedBarrierCheck("setSpeed",a.abs()))
			return false;
		dabsspeed = a.abs();
		mdvunitspeed = a.UnitVec();
		return true;
	}	
	
	public boolean setAbsSpeed(double a) {
		if(SpeedBarrierCheck("setSpeed",a))
			return false;
		dabsspeed = a;
		return true;
	}
	
	public boolean setSpeedx(double a) {
		//debugout("Updating speed for "+id);
		MDVector mdvspeed = MVMath.ProMVNum(mdvunitspeed, dabsspeed);
		mdvspeed.x1 = a;
		if(SpeedBarrierCheck("setSpeedx",mdvspeed.abs()))
			return false;
		dabsspeed = mdvspeed.abs();
		mdvunitspeed = mdvspeed.UnitVec();
		return true;
	}	
	
	public boolean setSpeedy(double a) {
		//debugout("Updating speed for "+id);
		MDVector mdvspeed = MVMath.ProMVNum(mdvunitspeed, dabsspeed);
		mdvspeed.x2 = a;		
		if(SpeedBarrierCheck("setSpeedy",mdvspeed.abs()))
			return false;
		dabsspeed = mdvspeed.abs();
		mdvunitspeed = mdvspeed.UnitVec();
		return true;
	}	

	public boolean setSpeedz(double a) {
		//debugout("Updating speed for "+id);
		MDVector mdvspeed = MVMath.ProMVNum(mdvunitspeed, dabsspeed);
		mdvspeed.x3 = a;
		if(SpeedBarrierCheck("setSpeedz",mdvspeed.abs()))
			return false;
		dabsspeed = mdvspeed.abs();
		mdvunitspeed = mdvspeed.UnitVec();	
		return true;
	}	
	
	public double getSpeed() {
		return dabsspeed;
	}
	public MDVector getMDVSpeed() {
		return MVMath.ProMVNum(mdvunitspeed, dabsspeed);
	}
	
	public void setMass(double a) {
		//debugout("Updating mass for "+id);
		mass = a;
	}
	public double getAbsMass() {
		return mass;
	}
	public double getSRTMass() {
		//falls der v-vektor (0|0|0) ist, ist auch v=0
		if(mdvunitspeed.abs() <= 0) 
			return mass;
			
		return ( mass*gamma(dabsspeed));
	}
	
	public void addMLVCoord(MLVector ds) {
		mlvpos = MVMath.AddMV(mlvpos, ds);
	}	
	public void setCoordx(long a) {
		//debugout("Updating speed for "+id);
		mlvpos.x1 = a;
	}	
	public void setCoordy(long a) {
		//debugout("Updating speed for "+id);
		mlvpos.x2 = a;
	}	
	public void setCoordz(long a) {
		//debugout("Updating speed for "+id);
		mlvpos.x3 = a;
	}	
	public void setCoordMLV(MLVector mlvl) {
		mlvpos = mlvl;
	}
	public MLVector getCoordMLV() {
		return mlvpos;
	}

	public void setAbsRadius(double a) {
		debugout("Masspoint - new Radius="+a);
		radius = a;
	}
	//Unit [m]
	public double getAbsRadius() {
		return radius;
	}
	
	public double getDensity() {
		return (mass / getAbsVolume());
	}
	
	public double getAbsVolume() {
		return (4.0/3.0*Math.pow(radius, 3.0)*Math.PI);
	}
	
	//Zu beachten ist hierbei, dass der Schwarzschildradius in der Allgemeinen 
	//Relativit�tstheorie nicht den Abstand vom Mittelpunkt angibt, sondern �ber 
	//die Oberfl�che von Kugeln definiert ist. Ein kugelf�rmiger Ereignishorizont 
	//mit Radius rS hat dieselbe Fl�che wie eine Sph�re gleichen Radius im euklidischen 
	//Raum, n�mlich A=4\,\pi\,r^2. Aufgrund der Raumzeitkr�mmung sind die radialen 
	//Abst�nde im Gravitationsfeld vergr��ert (sprich: der Abstand zweier Kugelschalen 
	//mit � �ber die Kugelfl�che definierten � Radialkoordinaten r1 und r2 ist gr��er 
	//als die Differenz dieser Radien).
	//de.wikipedia.org/wiki/Ereignishorizont#Schwarzschild-Radius_und_Gravitationsradius
	public double getSchwarzschildRadius() {
		//r = 2Gm / c^2
		return ((2.0*CalcCode.GRAVCONST*this.getSRTMass())/Math.pow(CalcCode.LIGHTSPEED, 2.0));
	}
	
	//Unit [m]
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
		id = mpdata.id;
		mass = mpdata.mass;
		radius = mpdata.radius;
		mlvpos.x1 = mpdata.mlvpos.x1;
		mlvpos.x2 = mpdata.mlvpos.x2;
		mlvpos.x3 = mpdata.mlvpos.x3;
		dabsspeed = mpdata.dabsspeed;
		mdvunitspeed.x1 = mpdata.mdvunitspeed.x1;
		mdvunitspeed.x2 = mpdata.mdvunitspeed.x2;
		mdvunitspeed.x3 = mpdata.mdvunitspeed.x3;
	}
	
	public void remove() {
		mass = 0;
		radius = 0;
		dabsspeed = 0;
		mdvunitspeed = new MDVector(0,0,0);
		mlvpos = new MLVector(0,0,0);
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
		MLVector mlvrange = MVMath.SubMV(this.getCoordMLV(), mp2.getCoordMLV());	
		return (MVMath.ConvertToD(mlvrange)).abs();
	}
	
	public Masspoint_Sim toMasspoint_Sim() {
		return new Masspoint_Sim(id, mass, radius, dabsspeed, mdvunitspeed.x1, mdvunitspeed.x2, mdvunitspeed.x3, 0, 0, 0, mlvpos.x1, mlvpos.x2, mlvpos.x3);
	}
	public String getName() {
		return name;
	}
	public void setUnitSpeed(double x, double y, double z) {
		mdvunitspeed = new MDVector(x,y,z);
		mdvunitspeed = mdvunitspeed.UnitVec();
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
		mdvunitspeed = new MDVector(dx1, dx2, dx3);
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
		theta = Math.acos(mdvunitspeed.x3 / temp);
		phi = Math.atan2(mdvunitspeed.x2, mdvunitspeed.x1);

			
		angles[0] = (float)Math.toDegrees(theta);
		if(phi < 0)
			angles[1] = 360.0f + (float)Math.toDegrees(phi);
		else 
			angles[1] = (float)Math.toDegrees(phi);
		return angles;
	}
}