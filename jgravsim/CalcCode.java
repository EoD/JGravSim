package jgravsim;

import java.io.File;
import java.util.Vector;

public class CalcCode extends Thread {

	public static final long serialVersionUID = 42 ;
	
	static public boolean DEBUG = false;
	static final double LIGHTSPEED = 299792458.0; 	//Lichtgeschwindigkeit in m/s
	static final double GRAVCONST = 6.673E-11;		//Gravitationskonstante
	static final double AE = 1.495979E11; 	//Astronomische Einheit (L�ngeneinheit) in m
	static final double LY = 9.461E15;		//Lichtjahr in m
	static final double ps = 30.857E15;		//Parsec in m
	static final double mi = 1609.344;		//mile in m
	static final double A = 100E-12;		//Angström in m
	static final double EM = 5.9736E24;		//Earth mass in kg
	static final double SM = 1.9884E30;		//Sun mass in kg
	static final double SR = 1.392E9;		//Sun radius in m
	static final double ER = 1.2735E7;		//Earth radius in m
	static final double u = 1.66053878283E-27;		//atomic mass in kg
	//static final double MACH = 340.3;		//mach in m/s
	static final double KNOT = 0.5144444;		//knot in m/s
	static final double TIMESTEPX = 30.0*60.0;			//(in s) - 30min
	static final double TIMECOUNTX = TIMESTEPX*2.0; 	//1 h
	static final double DATACOUNTX = 14.0*24.0*TIMECOUNTX; //14days - F�r wie lange er berechnen soll =10s
	//static final double EXACTSTEP = TIMESTEP/Math.pow(10.0, 3.0); //default exactstep 10ms
	static final double LACCURACY = 1.0E2; //Math.pow(10.0,3.0);	//Genauigkeit der longs: 0=m;2=cm;3=mm;6=nm;9=pm (yes it is 10^+3)
	static final int SMASSCONST = 17;			//constant for mass-slider (nach belieben �nderbar)
	static final int SZOOMCONST = 10;		//to avoid zoom=0
	static final double SRADIUSCONST = 5.6;		//constant for radius-slider (nach belieben �nderbar)
	static final double SSPEEDCONST = 8.0;		//constant for speed-slider
	static final double SDENSITYCONST = 4.0;

	static final int UNKNOWN = -1;
	static final int NOERROR = 0;
	static final int LIGHTSPEEDERROR = 1;
	static final int LONGLIMIT = 2;
	static final int DOUBLELIMIT = 3;
	
	Controller myController;
	Model myModel;
	double dtmax;
	double timecount;
	double timestep;
	double exactstep;
	
	double dtsmallsum;
	double dtsum;
	double dt;
	int error;
	boolean b_console;
	Vector<Masspoint> vmps_temp;
	Vector<Masspoint> vmps_current;
	
	CalcCode(Controller MyController, Model MyModel, double ddatacount, double dtimecount, double dtimestep, boolean console) {
		dtmax = ddatacount;
		timecount = dtimecount;
		timestep = dtimestep;
		exactstep = timestep/Math.pow(10.0, 3.0);
		
		myController = MyController;
		if(MyController == null)
			debugout("CalcCode Constructor - MyController==null!");
		
		myModel = MyModel;
		b_console = console;
		
		if(b_console == false)
			myController.startCalcProgress((int)(dtmax/timecount));
		error = NOERROR;
	}
	
	public void run() {
		calcMain();
	}
	
	public void debugout(String a) {
		if(Controller.CURRENTBUILD && DEBUG)
			System.out.println(a);
	}
	
	//Main part of the calculation
	@SuppressWarnings("unchecked")	//unchecked cast Vector<Masspoint>
	public void calcMain() {
    	debugout("Calculation starting");
		vmps_temp = new Vector<Masspoint>();
		//vmps_current = new Vector<Masspoint>();
		vmps_current =  (Vector<Masspoint>)(myController.getVMasspoints().clone());
		
		for(int i = vmps_current.size()-1 ;i >= 0; i--) {
			Masspoint mpi= (Masspoint)vmps_current.get(i);
			debugout( "  ID: " + mpi.getID() + ", "+" "
					+"  Mass: " + mpi.getAbsMass() + ", "
					+"  Radius: " + mpi.getRadius() + ", "
					+"  velx: " + mpi.getMDVSpeed().x1 + ", "
					+"  vely: " + mpi.getMDVSpeed().x2 + ", "
					+"  velz: " + mpi.getMDVSpeed().x3 + ", "
					//std::cout << "  accx: " << mpi.accx << "\n";	//deprecated
					//std::cout << "  accy: " << mpi.accy << "\n";	//deprecated
					//std::cout << "  accz: " << mpi.accz << "\n";	//deprecated
					+"  posx: " + mpi.getPos().x1 + ", "
					+"  posy: " + mpi.getPos().x2 + ", "
					+"  posz: " + mpi.getPos().x3);
			}
		//vmps_current.addAll(myController.getVMasspoints());	//hole Start-daten aus dem Controller
	
		
		dt = timestep;
		debugout("calcMain() - first, dt=timestep="+dt+"="+timestep+", timecount="+timecount);
		debugout("calcMain() - currentsize="+vmps_current.size()+", tempsize="+vmps_temp.size());
    	
    	//diese while-schleife ist dafür verantwortlich, dass alle Steps ausgeführt werden
    	//quasi die schleife die solange berechnet bis DATACOUNT vollständig ist
    	//läuft solange bis Abort (flagcalc == false) gedrückt wurde
		
		while(dtsum < dtmax && myController.flagcalc == true) {			
			//debugout("calcMain() -a currentsize="+vmps_current.size()+", tempsize="+vmps_temp.size());
			
			if(vmps_temp != null) // && vmps_temp.size() > 0)
				vmps_temp.removeAllElements();
			else //if(vmps_temp == null) 
				vmps_temp = new Vector<Masspoint>();

			debugout("calcMain() -b currentsize="+vmps_current.size()+", tempsize="+vmps_temp.size());

			//prüfe erst nach Kollisionen und nimm alle die nicht kollidiert sind
			vmps_current = collisionCheck(vmps_current);
			
			debugout("calcMain() -c currentsize="+vmps_current.size()+", tempsize="+vmps_temp.size());
			
			//Überprüfe ob eines der MPs schneller als 60%c fliegt
			if(checkSpeedBorder(vmps_current,0.8)) {
				debugout("calcMain() - v>LIGHTSPEED");
				if(dt > exactstep) {
					dt = exactstep;
					debugout("calcMain() - dt = exactstep ="+dt+"="+exactstep);
				}
				else
					debugout("calcMain() - dt <= exactstep: "+dt+"<"+exactstep);
			}				
/*			else if(checkSpeedBorder(vmps_current,0.9)) {
				debugout("calcMain() - v>LIGHTSPEED");
				if(dt > exactstep/Math.pow(10.0, 3.0)) {
					dt = exactstep/Math.pow(10.0, 3.0);
					debugout("calcMain() - dt = exactstep/1000 ="+dt+"="+exactstep/Math.pow(10.0, 3.0));
				}
				else
					debugout("calcMain() - dt <= exactstep/1000: "+dt+"<"+exactstep/Math.pow(10.0, 3.0));
			}					
			else if(checkSpeedBorder(vmps_current,0.95)) {
				debugout("calcMain() - v>LIGHTSPEED");
				if(dt > exactstep/Math.pow(10.0, 9.0)) {
					dt = exactstep/Math.pow(10.0, 9.0);
					debugout("calcMain() - dt = exactstep/10^9 ="+dt+"="+exactstep/Math.pow(10.0, 9.0));
				}
				else
					debugout("calcMain() - dt <= exactstep/10^9: "+dt+"<"+exactstep/Math.pow(10.0, 9.0));
			}		
*/
			debugout("calcMain() - calcAcc("+dt+","+vmps_current.size()+") wird gestartet");
			// Something really weird happened
			if(vmps_current.size() <= 0) {
				debugout("calcMain() - vmps_current.size <= 0!");
				myController.flagcalc = false;
				error = UNKNOWN;
			}
			
			vmps_temp = calcAcc(dt, vmps_current);
			
			if(vmps_temp == null) {
				debugout("calcMain() - calcAcc hat null zur�ckgegeben. Neues dt="+dt+", dtsum="+dtsum);
				continue;
			}
			else {
				dtsmallsum += dt;
				debugout("calcMain() - calcAcc hat nicht null zur�ckgegeben (size="+vmps_temp.size()+", dt="+dt+"). Neues dtsum="+dtsum);
			}
			
			if(dtsmallsum >= timecount) {
				debugout("calcMain() - dtsum%timestep==0: "+dtsum+"%"+timestep+"=="+dtsum%timestep);
				debugout("calcMain() - dtsmallsum >= timestep: "+dtsmallsum+">="+timestep);
				myModel.AddStep(vmps_temp);	//save data to file
				dtsum += dtsmallsum;
				dtsmallsum = 0;
				debugout("calcMain() - dtsum ="+dtsum+", Datacount="+dtmax);
				dt = timestep; //reset to old step size
				debugout("calcMain() - end, dt = timestep ="+dt+"="+timestep);
				if(b_console == false)
					myController.myView_CalcProgress.step();
			}
			else
				debugout("dtsmallsum < timestep: "+dtsmallsum+" < "+timestep+".dtsum="+dtsum);

			debugout("calcMain() -d currentsize="+vmps_current.size()+", tempsize="+vmps_temp.size());			
			vmps_current.removeAllElements();
			//debugout("calcMain() -e currentsize="+vmps_current.size()+", tempsize="+vmps_temp.size());
			vmps_current.addAll(vmps_temp);	   
			debugout("calcMain() -f currentsize="+vmps_current.size()+", tempsize="+vmps_temp.size());  	
		}
		if(myController.flagcalc == false) {
			myModel.correctHeader(new File(Model.FILE_TEMP), (int)(dtsum/timecount));
		}
		debugout("calcMain - Quit - Roger and out");
		if(b_console == false) {
			myController.myView_CalcProgress.setVisible(false);
			myController.ThreadFinished(vmps_current, error);
		}
		else
			myController.ThreadFinished_Console(error);
	}

	//Calculates accerlation,speed and the new position of each object
	public Vector<Masspoint> calcAcc(double dstep, Vector<Masspoint> vmpsinsert) {
		Vector<Masspoint> vmpsout = new Vector<Masspoint>();
		vmpsout.addAll(vmpsinsert);
	
		//Berechnung der neuen Position f�r alle Objekte von ID 0 bis ID i
		debugout("calcAcc() - Start() - dstep="+dstep+" - size="+vmpsinsert.size());
		for(int i=0; i<vmpsinsert.size(); i++) {
			//debugout("calcAcc() - for Schleife Start");
			Masspoint mpold = (Masspoint)vmpsinsert.get(i);
			Masspoint mpnew = (Masspoint)vmpsout.get(i);
			
			debugout("calcAcc() - ID "+mpold.getID()+": Old Coords(x1,x2,x3): "+mpold.getPosX()+","+mpold.getPosY()+","+mpold.getPosZ());		
			MDVector mdvforce = new MDVector(0,0,0);		
			//Calculation of the whole force on object i
			mdvforce = calcForce(mpold, vmpsinsert);
			
			debugout("calcAcc() - |MDVforcetotal|,x1,x2,x3: "+mdvforce.abs()+","+mdvforce.x1+","+mdvforce.x2+","+mdvforce.x3);

			//relativistic accelration formula
			//Math.sqrt( Math.pow(LIGHTSPEED,2) - Math.pow(mpmain.getSpeed(),2) / Math.pow(LIGHTSPEED,2));
			double da1 = Masspoint.gamma(mpold.getSpeed());
			da1 *= Math.pow(LIGHTSPEED,2) * mdvforce.x1 - mpold.getMDVSpeed().x1 * MVMath.ProScaMV(mdvforce, mpold.getMDVSpeed());
			da1 /= Math.pow(LIGHTSPEED,2) * mpold.getAbsMass();
			
			double da2 = Masspoint.gamma(mpold.getSpeed());
			da2 *= Math.pow(LIGHTSPEED,2) * mdvforce.x2 - mpold.getMDVSpeed().x2 * MVMath.ProScaMV(mdvforce, mpold.getMDVSpeed());
			da2 /= Math.pow(LIGHTSPEED,2) * mpold.getAbsMass();
			
			double da3 = Masspoint.gamma(mpold.getSpeed());
			da3 *= Math.pow(LIGHTSPEED,2) * mdvforce.x3 - mpold.getMDVSpeed().x3 * MVMath.ProScaMV(mdvforce, mpold.getMDVSpeed());
			da3 /= Math.pow(LIGHTSPEED,2) * mpold.getAbsMass();
			debugout("calcAcc() -  Acceleration (da1,da2,da3): "+da1+","+da2+","+da3);
			
			MDVector mva = new MDVector(da1,da2,da3);
			
			MDVector deltav = new MDVector(0,0,0);
			deltav = MVMath.ProMVNum(mva, dstep);
			//debugout("calcAcc() -  Delta-v (dv1,dv2,dv3): "+deltav.x1+","+deltav.x2+","+deltav.x3);
			debugout(" calcAcc() - deltav x:"+deltav.x1);
			debugout(" calcAcc() - deltav y:"+deltav.x2);
			debugout(" calcAcc() - deltav z:"+deltav.x3);
			debugout(" calcAcc() - abs(deltav)+mpold->getAbsSpeed()="+(deltav.abs()+mpold.getSpeed()));
			
			
			if(deltav.abs()+mpold.getSpeed() > LIGHTSPEED) {
				debugout("calcAcc() - Changing dt");
				while((LIGHTSPEED - mpold.getSpeed()) / mva.abs() < dt) {
					if(dt <= 10.0*java.lang.Double.MIN_VALUE) {
						debugout("calcAcc - ERROR double-limit reached!");
						myController.flagcalc = false;
						error = DOUBLELIMIT;
						break;
					}
					dt /= 10.0;	
					debugout("calcAcc() -  (c-v0)/a < dt = ("+LIGHTSPEED+"-"+mpold.getSpeed()+")/"+mva.abs()+"="+(LIGHTSPEED - mpold.getSpeed()) / mva.abs()+"<"+dt);			
				}
				debugout("calcAcc() - dv+v0 > c => break. New dt="+dt);
				return null;
			}
			
			//f�ge dv zu gesamt v hinzu
			//Additionsverfahren sollte v = v0 + dv lauten wurde allerdings durch
			//eine formel ersetzt indem v < c bleibt, obwohl v0=99%c und dv = 99%c
			//Sollte in der Endversion durch v = v0 + dv ersetzt werden
			if(!mpnew.addSpeed(deltav)) {
				myController.flagcalc = false;
				error = LIGHTSPEEDERROR;
			}

			//new function which produces new coords
			MLVector mlvds = new MLVector(0,0,0);
			
			//calculates the delta-s (ds = v * dt)
			//converts mvspeed mdvector to mlvector
			mlvds = MVMath.ConvertToL(MVMath.ProMVNum(mpold.getMDVSpeed(),dstep));
			//debugout("calcAcc() -  |mdv-v|,v1,v2,v3: "+mpold.getMDVSpeed().abs()+","+mpold.getMDVSpeed().x1+","+mpold.getMDVSpeed().x2+","+mpold.getMDVSpeed().x3);
			//debugout("calcAcc() -  |mlv-v|,v1,v2,v3: "+MVMath.ConvertToL(mpold.getMDVSpeed()).abs()+","+MVMath.ConvertToL(mpold.getMDVSpeed()).x1+","+MVMath.ConvertToL(mpold.getMDVSpeed()).x2+","+MVMath.ConvertToL(mpold.getMDVSpeed()).x3);
			debugout("calcAcc() -  |mlvds|,ds1,ds2,ds3: "+mlvds.abs()+", "+mlvds.x1+", "+mlvds.x2+", "+mlvds.x3);
					
			//new position = ds + old position
			long limit;
			if(mpold.getPos().x1 < 0)
				limit = Long.MIN_VALUE - mpold.getPos().x1;
			else
				limit = Long.MAX_VALUE - mpold.getPos().x1;
			if(Math.abs(limit) - Math.abs(mlvds.x1) < 0) {
				myController.flagcalc = false;
				Controller.debugout("calcAcc - Obj"+mpold.getID()+" Long Limit1 reached, break");
				error = LONGLIMIT;
				return vmpsout;
			}
			
			if(mpold.getPos().x2 < 0)
				limit = Long.MIN_VALUE - mpold.getPos().x2;
			else
				limit = Long.MAX_VALUE - mpold.getPos().x2;
			if(Math.abs(limit) - Math.abs(mlvds.x2) < 0) {
				myController.flagcalc = false;
				Controller.debugout("calcAcc - Obj"+mpold.getID()+" Long Limit2 reached, break");
				error = LONGLIMIT;
				return vmpsout;
			}

			if(mpold.getPos().x3 < 0)
				limit = Long.MIN_VALUE - mpold.getPos().x3;
			else
				limit = Long.MAX_VALUE - mpold.getPos().x3;
			if(Math.abs(limit) - Math.abs(mlvds.x3) < 0) {
				myController.flagcalc = false;
				Controller.debugout("calcAcc - Obj"+mpold.getID()+" Long Limit3 reached, break");
				error = LONGLIMIT;
				return vmpsout;
			}
			
				
			mpnew.addMLVCoord(mlvds);
			//mp.mlvpos = MVMath.AddMV(mlvds, mp.mlvpos);
			debugout("calcAcc() - ID "+mpnew.getID()+": New Coords(x1,x2,x3): "+mpnew.getPosX()+" , "+mpnew.getPosY()+" , "+mpnew.getPosZ());	
		}
		debugout("calcAcc() - Finish, size="+vmpsout.size());	
		return vmpsout;
	}

	
	//Berechnet die Kraft auf ein referenz objekt (mpmain)
	//Kraft entsteht durch grav-wirkung aller anderen
	public MDVector calcForce(Masspoint mpmain, Vector<Masspoint> vmpsinsert) {
		MDVector mdvforcetotal = new MDVector(0,0,0);
		debugout("calcForce() - START ID: "+mpmain.getID()+"; size="+vmpsinsert.size());
		for(int i=0; i<vmpsinsert.size(); i++) {
			Masspoint mpsec = (Masspoint)vmpsinsert.get(i);
			debugout("calcForce() - START secID: "+mpsec.getID()+":START");
			
			//Objekt wechselwirkt nicht mit sich selbst (nicht hier!)
			if(mpmain.getID() == mpsec.getID()) 
			{	continue;	}
			
			//veraltet: ehem. �berpr�fung von oben
			if(mpmain == mpsec)  {	
				debugout("calcForce() - MoveSteps - WARNING Objects ("+mpmain.getID()+","+mpsec.getID()+" had different ids,but are the same object");
				continue;	
			}			

			//distance between objects
			MLVector mlvdist = new MLVector(0,0,0);
			mlvdist = MVMath.SubMV(mpsec.getPos(),mpmain.getPos());
			debugout("calcForce() - mlvdist.x1="+mlvdist.x1);
			debugout("calcForce() - mlvdist.x2="+mlvdist.x2);
			debugout("calcForce() - mlvdist.x3="+mlvdist.x3);
			
			
			//halb-relativistische Gravitations-Kraftberechnung
			//(in mehrere Einzelschritt zerlegt)
			
			//absolute newtonian force
			double dforce = 0.0;	// |Fg| = m1*m2*G
			dforce = mpmain.getSRTMass();
			debugout("calcForce() - dforce1="+dforce);
			dforce *= mpsec.getSRTMass();
			debugout("calcForce() - dforce2="+dforce);
			dforce *= GRAVCONST;
			debugout("calcForce() - dforce3="+dforce);
			dforce /= Math.pow(MVMath.ConvertToD(mlvdist).abs(), 2.0);
			debugout("calcForce() - dforce4="+dforce);
						
			//debugout("calcForce() - relm1,relm2 = dforce: "+mpmain.getSRTMass()+","+mpsec.getSRTMass()+" = "+dforce);
			//debugout("calcForce() - mpsec: ID="+mpsec.id+", absmass="+mpsec.getAbsMass()+", absspeed="+mpsec.getSpeed());
			//debugout("calcForce() - mpsec: vx="+mpsec.getMDVSpeed().x1+", absmass="+mpsec.getAbsMass()+", absspeed="+mpsec.getSpeed());
				
			//mdvrquot calculated with MDVector because 'radius' is r / r^3
			MDVector mdvrquot = new MDVector(0.0,0.0,0.0);
			//Converted from long (mm) to double (meter)
			
			debugout("calcForce() - mlvdist.abs()="+MVMath.ConvertToD(mlvdist).abs());
			
			mdvrquot = MVMath.DivMVNum(MVMath.ConvertToD(mlvdist), MVMath.ConvertToD(mlvdist).abs()); 	// vec r / |r|
			debugout(" calcForce() - Mdvrquot.abs,r1,r2,r3: "+mdvrquot.abs()+","+mdvrquot.x1+","+mdvrquot.x2+","+mdvrquot.x3);
			
			//Force-Vector: combination of mdvrquot and dforce
			MDVector mdvforce = new MDVector(0.0,0.0,0.0);			
			mdvforce = MVMath.ProMVNum(mdvrquot, dforce);	// % * |Fg|
			debugout(" calcForce() - |mdvforce|,x1,x2,x3: "+mdvforce.abs()+","+mdvforce.x1+","+mdvforce.x2+","+mdvforce.x3);
			
			//kraft von objekt i auf ref-objekt wird zu einer gesamt kraft addiert
			mdvforcetotal = MVMath.AddMV(mdvforcetotal,mdvforce);
			debugout(" calcForce() - |mvforcesum|,x1,x2,x3: "+mdvforcetotal.abs()+","+mdvforcetotal.x1+","+mdvforcetotal.x2+","+mdvforcetotal.x3);

		}
		//debugout("calcForce() - |mvforcesum|,x1,x2,x3: "+mdvforcetotal.abs()+","+mdvforcetotal.x1+","+mdvforcetotal.x2+","+mdvforcetotal.x3);
		
		return mdvforcetotal;
	}
	
	//Kollisions-Abfrage. Alle Objekte werden gepr�ft ob sie kollidieren
	//Objekt i wird entfernt, falls eine Kollision vorherrscht (i>j)
	public Vector<Masspoint> collisionCheck(Vector<Masspoint> vmpinsert) {
		Vector<Masspoint> vmpout = new Vector<Masspoint>();
		debugout("collisionCheck() - Starting, Size:"+vmpinsert.size());
		
		for(int i = vmpinsert.size()-1 ;i >= 0; i--) {
			Masspoint mpi= (Masspoint)vmpinsert.get(i);
			boolean breakflag = false;
			
			//check collision only for (i-1) objects
			for(int j=i-1 ;j >= 0 && i >= 1; j--) {
				//debugout("collisionCheck() - Checking collision between No"+i+"and No"+j);
				Masspoint mpj= (Masspoint)vmpinsert.get(j);
				double iradius = mpi.getRadius();
				double jradius = mpj.getRadius();
				//check if the two objects are colliding 
				//(they are also colliding if object goes into schwarzschild radius)
				
				if(myController.flagschwarzschild == false && (mpj.isBlackHole() || mpi.isBlackHole()))
					//if objects have a distance less than 2*Schwarzschild Radius the calculation gets very imprecisely
					if(mpi.drange(mpj) < 2*(mpj.getSchwarzschildRadius()+mpi.getSchwarzschildRadius())) {
						myController.flagschwarzschild = true;
				}
						
				if(mpi.drange(mpj) < (iradius+jradius)) {
					debugout("collisionCheck() - Object "+mpi.getID()+" collided with Object"+mpj.getID());
					collision(mpj,mpi);
					breakflag = true;
					break;
				}	
			}
			//if object didn't collide add it to the first entry of the list of objects
			//(the for starts with the last object so, the first object to add, was
			// before the last object)
			if(breakflag == false) {
				//debugout("collisionCheck() - Adding Object "+i);
				vmpout.insertElementAt(mpi, 0);
			}
		}
		//debugout("collisionCheck() - Ending, Size:"+vmpout.size());
		return vmpout;
	}
	
	//If object collide, one object will survive, the other one will be removed)
	public Masspoint collision(Masspoint mpsurvive, Masspoint mpkill) {
		//the one with the smaller radius has to be removed
		//REMOVED because only object 1 surives
		/*if(mpkill.getRadius() > mpsurvive.getRadius()) {
			Masspoint temp = mpsurvive;
			mpsurvive = mpkill;
			mpkill = temp;
		}*/
		MDVector mpsecspeed = new MDVector(0,0,0);
		double dvolume = mpsurvive.getAbsVolume() + mpkill.getAbsVolume();	//die volumina (nicht die radien!) werden addiert
		debugout("Collision! Object "+mpsurvive.getID()+" ("+mpsurvive.getAbsVolume()+") and Object "+mpkill.getID()+"/kill ("+mpkill.getAbsVolume()+") collided. New volume: "+dvolume);
		//Berechnung des neuen radiuses aus dem Volumen
		double dradius = Math.pow((3*dvolume)/(4*Math.PI), 1.0/3.0);	//V=4/3*r^3*PI --> r = 3.sqrt(3*V/PI/4)
		debugout("Collision! Object "+mpsurvive.getID()+" ("+mpsurvive.getAbsRadius()+") and Object "+mpkill.getID()+"/kill ("+mpkill.getAbsRadius()+") collided. New radius: "+dradius);
		//double dradius = mp1.getRadius() + mp2.getRadius();  //DEBUG - has to be replaced by following line:
		//Math.pow( (3*dvolume)/(4*Math.PI), 1/3);	//V=4/3*r^3*PI --> r = 3.sqrt(3*V/PI/4)
		
		//new momentum (=impuls)
		// TODO a lot to do ;)
		MDVector mdvmoment = MVMath.AddMV(mpsurvive.getImpulse(), mpkill.getImpulse());
		double dfactora = MVMath.ProScaMV(mdvmoment, mdvmoment);	//(momentum1+momentum2)^2
		
		/*
		 * The resulting mass of a total inelastic collision between relastivic
		 * objects is m = sqrt( ( E1 + E2 )^2 / c^2 - (p1 + p2)^2 ) / c
		 */
		double E1 = mpsurvive.getEnergy(), E2 = mpkill.getEnergy();
		
		double dmass = Math.sqrt( (E1+E2)*(E1+E2) / (LIGHTSPEED*LIGHTSPEED) - dfactora) / LIGHTSPEED;
		
		double dgamma3;
		dgamma3 = LIGHTSPEED*LIGHTSPEED*Math.pow(dmass, 2.0);
		dgamma3 = dgamma3 + dfactora;
		dgamma3 = dfactora / dgamma3;
		dgamma3 = Math.sqrt(1.0 - dgamma3);
		
		mpsecspeed = MVMath.DivMVNum(mdvmoment, dmass); 	//gesamtv = gesamtimpuls / gesamtmasse
		mpsecspeed = MVMath.ProMVNum(mpsecspeed, dgamma3);
		
		//Object has position between the old centers
		//(arithmetisches mittel der positionen)

		//	MLVector mlvnewcoord = MVMath.AddMV(mpsurvive.getCoordMLV(),mpkill.getCoordMLV());
		//	mlvnewcoord = MVMath.DivMVNum(mlvnewcoord, 2.0);
		
		double dvolumekill = mpkill.getVolume();
		double dvolumesurvive = mpsurvive.getVolume();
		
		double dconst = (dvolumesurvive+dvolumekill) / 2.0;
		
		MLVector mlvcoordsur = MVMath.ProMVNum(mpsurvive.getPos(), dvolumesurvive/dconst);
		MLVector mlvcoordkil = MVMath.ProMVNum(mpkill.getPos(), dvolumekill/dconst);
		MLVector mlvnewcoord = MVMath.AddMV(mlvcoordsur, mlvcoordkil);
		mlvnewcoord = MVMath.DivMVNum(mlvnewcoord, (dvolumesurvive+dvolumekill)/dconst);

		//we don't want a black hole become a normal object after collision!
		if(mpsurvive.isBlackHole() || mpkill.isBlackHole())
			mpsurvive.setAbsRadius(1.0);
		else
			mpsurvive.setAbsRadius(dradius);
		
		mpsurvive.setCoordMLV(mlvnewcoord);
		mpsurvive.setMass(dmass);
		if(!mpsurvive.setSpeed(mpsecspeed)) {
			myController.flagcalc = false;
			error = LIGHTSPEEDERROR;
		}
		//myController.RemoveMp(mp2);
		return mpsurvive;
	}
	
	public boolean checkSpeedBorder(Vector<Masspoint> vmpinsert, double dpercentage) {
		//diese for-schleife dient nur daf�r, dass die berechnung exakt wird, falls ein objekt �ber dpercentage% c kommt
    	for(int i=0;i<vmpinsert.size();i++) {
    		Masspoint mp = (Masspoint)vmpinsert.get(i);
			//debugout("checkSpeedBorder() - Object Nr."+i+"/"+mp.id+" (vx,vy,vz): "+mp.getMDVSpeed().x1+","+mp.getMDVSpeed().x2+","+mp.getMDVSpeed().x3);
			//debugout("checkSpeedBorder() - Object Nr."+i+"/"+mp.id+" has a speed of "+mp.getSpeed()+" m/s (<"+dpercentage+"*Lightspeed="+dpercentage*LIGHTSPEED+")");
    		if(mp.getSpeed() >= (dpercentage*LIGHTSPEED)) {
    			debugout("checkSpeedBorder() - Object Nr."+i+"/"+mp.getID()+" has a speed larger "+dpercentage*100.0+"% lightspeed");
    			return true;
    		}
    	}
    	debugout("checkSpeedBorder() - No Object has a speed larger 75% lightspeed");
    	return false;
	}
}
