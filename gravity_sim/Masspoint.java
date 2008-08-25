package gravity_sim;

public class Masspoint {
	
	static public boolean DEBUG = false;
	static final double DFTRADIUS = 5.0;
	
	int id;
	MLVector mlvpos;	//position in mm
	double mass;
	double radius;
	MDVector mdvzerospeed;
	double dabsspeed;
	//direction
	
	public Masspoint(int kid, long lx, long ly ,long lz) {
		id = kid;
		mlvpos = new MLVector(lx,ly,lz);
		mdvzerospeed = new MDVector(0.0,0.0,0.0);
		dabsspeed = 0.0;
		mass = 1.0; 		//eg: sun = 1.99E30 kg
		radius = DFTRADIUS;
	}

	@Override
	public String toString() {
		return ("Objekt "+id);
	}
	
	public static void debugout(String a) {
		if(Controller.MAINDEBUG && DEBUG)
			System.out.println(a);
	}
	
	public static boolean SpeedBarrierCheck(String name,double speed) {
		if(speed > CalcCode.LIGHTSPEED) {
			debugout("Masspoint() - ERROR "+name+" > c");
			return true;
		}
		return false;
	}
	
	
	//Velocity addition under special relativity 
	//*** this function shouldn't be used
/*	public void addSRTMDVSpeed(MDVector dv) {
		double dtemp;
		MDVector newpart1;
		MDVector newpart2;
		
		//first summand
		newpart1 = MVMath.AddMV(mdvspeed, dv);
		dtemp = (1.0 +MVMath.ProScaMV(mdvspeed, dv) / Math.pow(CalculationCode.LIGHTSPEED,2.0));
		newpart1 = MVMath.DivMVNum(newpart1, dtemp);
		
		//second summand
		newpart2 = MVMath.ProVecMV(mdvspeed, dv);		// mdvspeed x dv
		newpart2 = MVMath.ProVecMV(mdvspeed, newpart2);	// mdvspeed x %
		newpart2 = MVMath.DivMVNum(newpart2, Math.pow(CalculationCode.LIGHTSPEED,2.0));	// mdvtemp / c^2
		dtemp = 1.0 + MVMath.ProScaMV(mdvspeed, dv);	// 1.0 + (mdvspeed . dv)
		newpart2 = MVMath.DivMVNum(newpart2, dtemp);		// mdvtemp / %
		dtemp = MVMath.ProScaMV(mdvspeed, mdvspeed);	// mdvspeed . mdvspeed
		dtemp = 1.0 + Math.sqrt(1.0 - (dtemp / Math.pow(CalculationCode.LIGHTSPEED,2.0) ));
		//1 + Sqrt( 1 - % / c^2 )
		newpart2 = MVMath.DivMVNum(newpart2, dtemp);		// mdvtemp / %
		
		mdvspeed = MVMath.AddMV(newpart1, newpart2);
		//debugout("Masspoint - addMDVSpeed - mdvspeed,part1,part2:"+mdvspeed.abs()+","+newpart1.abs()+","+newpart2.abs());
		SpeedBarrierCheck("addMDVSpeed",mdvspeed.abs());
	}	
*/
	
	public boolean addSpeed(double dvx, double dvy, double dvz) {
		MDVector mdvspeed = MVMath.ProMVNum(mdvzerospeed, dabsspeed);
		mdvspeed.x1 += dvx;
		mdvspeed.x2 += dvy;
		mdvspeed.x3 += dvz;
		if(SpeedBarrierCheck("addSpeedxyz",mdvspeed.abs()))
			return false;		
		dabsspeed = mdvspeed.abs();
		mdvzerospeed = mdvspeed.UnitVec();		
		return true;
	}	

	public boolean addSpeed(MDVector a) {
		MDVector mdvspeed = MVMath.ProMVNum(mdvzerospeed, dabsspeed);
		if(SpeedBarrierCheck("addSpeedmdv2",dabsspeed))
			return false;
		mdvspeed = MVMath.AddMV(a, mdvspeed);
		dabsspeed = mdvspeed.abs();
		mdvzerospeed = mdvspeed.UnitVec();
		return true;
	}
	
	public boolean setSpeed(MDVector a) {
		if(SpeedBarrierCheck("setSpeed",a.abs()))
			return false;
		dabsspeed = a.abs();
		mdvzerospeed = a.UnitVec();
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
		MDVector mdvspeed = MVMath.ProMVNum(mdvzerospeed, dabsspeed);
		mdvspeed.x1 = a;
		if(SpeedBarrierCheck("setSpeedx",mdvspeed.abs()))
			return false;
		dabsspeed = mdvspeed.abs();
		mdvzerospeed = mdvspeed.UnitVec();
		return true;
	}	
	
	public boolean setSpeedy(double a) {
		//debugout("Updating speed for "+id);
		MDVector mdvspeed = MVMath.ProMVNum(mdvzerospeed, dabsspeed);
		mdvspeed.x2 = a;		
		if(SpeedBarrierCheck("setSpeedy",mdvspeed.abs()))
			return false;
		dabsspeed = mdvspeed.abs();
		mdvzerospeed = mdvspeed.UnitVec();
		return true;
	}	

	public boolean setSpeedz(double a) {
		//debugout("Updating speed for "+id);
		MDVector mdvspeed = MVMath.ProMVNum(mdvzerospeed, dabsspeed);
		mdvspeed.x3 = a;
		if(SpeedBarrierCheck("setSpeedz",mdvspeed.abs()))
			return false;
		dabsspeed = mdvspeed.abs();
		mdvzerospeed = mdvspeed.UnitVec();	
		return true;
	}	
	
	public double getSpeed() {
		return dabsspeed;
	}
	public MDVector getMDVSpeed() {
		return MVMath.ProMVNum(mdvzerospeed, dabsspeed);
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
		if(mdvzerospeed.abs() <= 0) 
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

	public void setRadius(double a) {
		debugout("Masspoint - new Radius="+a);
		radius = a;
	}
	public double getRadius() {
		return radius;
	}
	
	public double getDensity() {
		return (mass / getVolume());
	}
	
	public double getVolume() {
		return (4.0/3.0*Math.pow(radius, 3.0)*Math.PI);
	}
	
	//Zu beachten ist hierbei, dass der Schwarzschildradius in der Allgemeinen 
	//Relativitätstheorie nicht den Abstand vom Mittelpunkt angibt, sondern über 
	//die Oberfläche von Kugeln definiert ist. Ein kugelförmiger Ereignishorizont 
	//mit Radius rS hat dieselbe Fläche wie eine Sphäre gleichen Radius im euklidischen 
	//Raum, nämlich A=4\,\pi\,r^2. Aufgrund der Raumzeitkrümmung sind die radialen 
	//Abstände im Gravitationsfeld vergrößert (sprich: der Abstand zweier Kugelschalen 
	//mit – über die Kugelfläche definierten – Radialkoordinaten r1 und r2 ist größer 
	//als die Differenz dieser Radien).
	//de.wikipedia.org/wiki/Ereignishorizont#Schwarzschild-Radius_und_Gravitationsradius
	public double getSchwarzschildRadius() {
		//r = 2Gm / c^2
		return ((2.0*CalcCode.GRAVCONST*this.getSRTMass())/Math.pow(CalcCode.LIGHTSPEED, 2.0));
	}
	
	/* btw: du hast post bekommen. Da hab ich nochmal genau geschrieben was eigentlich zu tun ist */
	public double getSchwarzschildVolume() {
		return (4.0/3.0*Math.pow(getSchwarzschildRadius(), 3.0)*Math.PI);
	}
	
	//*** won't work with new version
/*	public double[] getData() {
		double[] data = new double[9];
		data[0] = id;
		data[1] = mass;
		data[2] = radius;
		data[3] = mlvlpos.x1;
		data[4] = mlvlpos.x2;
		data[5] = mlvlpos.x3;
		data[6] = mdvspeed.x1;
		data[7] = mdvspeed.x2;
		data[8] = mdvspeed.x3;
		return data;
	}
*/	
	//*** won't work with new version
/*	public void setData(double[] data) {
		id = (int)data[0];
		mass = data[1];
		radius = data[2];
		mlvpos.x1 = data[3];
		mlvpos.x2 = data[4];
		mlvpos.x3 = data[5];
		mdvspeed.x1 = data[6];
		mdvspeed.x2 = data[7];
		mdvspeed.x3 = data[8];
	}*/
	
	//setData v2
	public void setData(Masspoint mpdata) {
		id = mpdata.id;
		mass = mpdata.mass;
		radius = mpdata.radius;
		mlvpos.x1 = mpdata.mlvpos.x1;
		mlvpos.x2 = mpdata.mlvpos.x2;
		mlvpos.x3 = mpdata.mlvpos.x3;
		dabsspeed = mpdata.dabsspeed;
		mdvzerospeed.x1 = mpdata.mdvzerospeed.x1;
		mdvzerospeed.x2 = mpdata.mdvzerospeed.x2;
		mdvzerospeed.x3 = mpdata.mdvzerospeed.x3;
	}
	
	public void remove() {
		mass = 0;
		radius = 0;
		dabsspeed = 0;
		mdvzerospeed = new MDVector(0,0,0);
		mlvpos = new MLVector(0,0,0);
	}
	
	//Lorentz factor (SRT)
	static public double gamma(double v) { 
		//debugout(" Gamma() - v="+v);
		if(SpeedBarrierCheck("gamma",v))
			return 0.0;
		double sqrt = Math.sqrt(1.0 - Math.pow(v/(double)CalcCode.LIGHTSPEED,2.0));
		//Controller.debugout(" Gamma() - sqrt="+sqrt);
		return (1.0/sqrt);
	}
	
	public double drange(Masspoint mp2) {
		MLVector mlvrange = MVMath.SubMV(this.getCoordMLV(), mp2.getCoordMLV());	
		return (MVMath.ConvertToD(mlvrange)).abs();
	}
	
	public Masspoint_Sim toMasspoint_Sim() {
		return new Masspoint_Sim(id, mass, radius,  mdvzerospeed.x1 * dabsspeed, mdvzerospeed.x2 * dabsspeed, mdvzerospeed.x3 * dabsspeed, 0, 0, 0, mlvpos.x1, mlvpos.x2, mlvpos.x3);
	}

}