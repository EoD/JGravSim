package gravity_sim;

import java.io.File;
import java.util.Vector;

public class CalcCode extends Thread {

	static public boolean DEBUG = false;
	static final double LIGHTSPEED = 299792458.0; 	//Lichtgeschwindigkeit in m/s
	static final double GRAVCONST = 6.673E-11;		//Gravitationskonstante
	static final double AE = 1.495979E11; 	//Astronomische Einheit (Längeneinheit) in m
	static final double LY = 9.461E15;		//Lichtjahr in m
	static final double TIMESTEPX = 60.0*60.0;			//(in s)
	static final double TIMECOUNTX = TIMESTEPX*10.0; //2months
	// TODO manuelle Eingabe!
	static final double DATACOUNTX = 365.0*24.0*60.0*60.0;	//(int)(1.0*60.0/TIMESTEP);	//Für wie lange er berechnen soll =10s
	//static final double EXACTSTEP = TIMESTEP/Math.pow(10.0, 3.0); //default exactstep 10ms
	static final double LACCURACY = 1.0E2; //Math.pow(10.0,3.0);	//Genauigkeit der longs: 0=m;2=cm;3=mm;6=nm;9=pm (yes it is 10^+3)
	static final int SMASSCONST = 21;			//constant for mass-slider (nach belieben änderbar)
	static final double SRADIUSCONST = 8.4;			//constant for radius-slider (nach belieben änderbar)
	static final double SSPEEDCONST = 8.0;		//constant for speed-slider
	//static final double ZOOM = AE / (1.0E6); //the zoom of the main window
	static final double RACCURACY = Math.pow(10.0, 3.0); //Genauigkeit des radius-feldes (std: km=1000)

	static final int UNKNOWN = -1;
	static final int NOERROR = 0;
	static final int LIGHTSPEEDERROR = 1;
	static final int LONGLIMIT = 2;
	static final int DOUBLELIMIT = 3;
	
	Controller myController;
	Model myModel;
	CalcView myCalculationView;
	double datacount;
	double timecount;
	double timestep;
	double exactstep;
	
	double dtsmallsum;
	double dtsum;
	double dt;
	int error;
	Vector<Masspoint> vmps_temp;
	Vector<Masspoint> vmps_current;
	
	CalcCode(Controller MyController, Model MyModel, double ddatacount, double dtimecount, double dtimestep) {
		datacount = ddatacount;
		timecount = dtimecount;
		timestep = dtimestep;
		exactstep = timestep/Math.pow(10.0, 3.0);
		
		myController = MyController;
		myModel = MyModel;
		myCalculationView = new CalcView((int)(datacount/timecount), myController);
		error = NOERROR;
	}
	
	public void run() {
		calcMain();
	}
	
	public void debugout(String a) {
		if(Controller.MAINDEBUG && DEBUG)
			System.out.println(a);
	}
	
	//Hauptberechnungs-Anwendung
	public void calcMain() {
    	debugout("Calculation starting");
		vmps_temp = new Vector<Masspoint>();
		vmps_current = new Vector<Masspoint>();
		vmps_current.addAll(myController.getVMasspoints());	//hole Start-daten aus dem Controller
		dt = timestep;
		debugout("calcMain() - first, dt = timestep ="+dt+"="+timestep+", timecount="+timecount);
		debugout("calcMain() - currentsize="+vmps_current.size()+", tempsize="+vmps_temp.size());
    	
    	//diese while-schleife ist dafür verantwortlich, dass alle Steps ausgeführt werden
    	//quasi die schleife die solange berechnet bis DATACOUNT vollständig ist
    	//läuft solange bis Abort (flagcalc == false) gedrückt wurde
		
		while(myController.flagcalc == true && dtsum < datacount) {			
			//debugout("calcMain() -a currentsize="+vmps_current.size()+", tempsize="+vmps_temp.size());
			
			if(vmps_temp != null) // && vmps_temp.size() > 0)
				vmps_temp.removeAllElements();
			else //if(vmps_temp == null) 
				vmps_temp = new Vector<Masspoint>();

			debugout("calcMain() -b currentsize="+vmps_current.size()+", tempsize="+vmps_temp.size());

			//prüfe erst nach Kollisionen und nimm alle die nicht kollidiert sind
			vmps_current = collisionCheck(vmps_current);
			
			debugout("calcMain() -c currentsize="+vmps_current.size()+", tempsize="+vmps_temp.size());
			
			//überprüfe ob eines der MPs schneller als 60%c fliegt
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
			// TODO correct: Not for final Version!
			if(vmps_current.size() <= 0) {
				debugout("calcMain() - vmps_current.size <= 0!");
				myController.flagcalc = false;
				error = UNKNOWN;
			}
			
			vmps_temp = calcAcc(dt, vmps_current);
			
			if(vmps_temp == null) {
				debugout("calcMain() - calcAcc hat null zurückgegeben. Neues dt="+dt+", dtsum="+dtsum);
				continue;
			}
			else {
				dtsmallsum += dt;
				debugout("calcMain() - calcAcc hat nicht null zurückgegeben (size="+vmps_temp.size()+", dt="+dt+"). Neues dtsum="+dtsum);
			}
			
			if(dtsmallsum >= timecount) {
				debugout("calcMain() - dtsum%timestep==0: "+dtsum+"%"+timestep+"=="+dtsum%timestep);
				debugout("calcMain() - dtsmallsum >= timestep: "+dtsmallsum+">="+timestep);
				myModel.AddStep(vmps_temp);	//save data to file
				dtsum += dtsmallsum;
				dtsmallsum = 0;
				debugout("calcMain() - dtsum ="+dtsum+", Datacount="+datacount);
				dt = timestep; //reset to old step size
				debugout("calcMain() - end, dt = timestep ="+dt+"="+timestep);
				myCalculationView.step();
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
			myModel.correctHeader(new File(Model.Defaultname), (int)(dtsum/timecount));
		}
		debugout("calcMain - Quit - Roger and out");
		myCalculationView.setVisible(false);
		myController.ThreadFinished(vmps_current, error);
	}

	//Calculates accerlation,speed and the new position of each object
	public Vector<Masspoint> calcAcc(double dstep, Vector<Masspoint> vmpsinsert) {
		Vector<Masspoint> vmpsout = new Vector<Masspoint>();
		vmpsout.addAll(vmpsinsert);
		//debugout("ID 0: Old Coords(x1,x2,x3): "+((Masspoint)masspoints.get(0)).mlvpos.x1+","+((Masspoint)masspoints.get(0)).mlvpos.x2+","+((Masspoint)masspoints.get(0)).mlvpos.x3);
		//debugout("ID 1: Old Coords(x1,x2,x3): "+((Masspoint)masspoints.get(1)).mlvpos.x1+","+((Masspoint)masspoints.get(1)).mlvpos.x2+","+((Masspoint)masspoints.get(1)).mlvpos.x3);
		//Berechnung der neuen Position für alle Objekte von ID 0 bis ID i
		debugout("calcAcc() - Start()");
		for(int i=0; i<vmpsinsert.size(); i++) {
			//debugout("calcAcc() - for Schleife Start");
			Masspoint mpold = (Masspoint)vmpsinsert.get(i);
			Masspoint mpnew = (Masspoint)vmpsout.get(i);
			
			debugout("calcAcc() - ID "+mpold.id+": Old Coords(x1,x2,x3): "+mpold.mlvpos.x1+","+mpold.mlvpos.x2+","+mpold.mlvpos.x3);		
			MDVector mvforce = new MDVector(0,0,0);		
			//Calculation of the whole force on object i
			mvforce = calcForce(mpold, dstep, vmpsinsert);
			
			//debugout(" |MVforcetotal|,x1,x2,x3: "+mvforce.abs()+","+mvforce.x1+","+mvforce.x2+","+mvforce.x3);
			
			//relativistic accelration formula
			//Math.sqrt( Math.pow(LIGHTSPEED,2) - Math.pow(mpmain.getSpeed(),2) / Math.pow(LIGHTSPEED,2));
			double da1 = Masspoint.gamma(mpold.getSpeed());
			da1 *= Math.pow(LIGHTSPEED,2) * mvforce.x1 - mpold.getMDVSpeed().x1 * MVMath.ProScaMV(mvforce, mpold.getMDVSpeed());
			da1 /= Math.pow(LIGHTSPEED,2) * mpold.getAbsMass();
			
			double da2 = Masspoint.gamma(mpold.getSpeed());
			da2 *= Math.pow(LIGHTSPEED,2) * mvforce.x2 - mpold.getMDVSpeed().x2 * MVMath.ProScaMV(mvforce, mpold.getMDVSpeed());
			da2 /= Math.pow(LIGHTSPEED,2) * mpold.getAbsMass();
			
			double da3 = Masspoint.gamma(mpold.getSpeed());
			da3 *= Math.pow(LIGHTSPEED,2) * mvforce.x3 - mpold.getMDVSpeed().x3 * MVMath.ProScaMV(mvforce, mpold.getMDVSpeed());
			da3 /= Math.pow(LIGHTSPEED,2) * mpold.getAbsMass();
			//debugout("calcAcc() -  Acceleration (a1,a2,a3): "+da1+","+da2+","+da3);
			
			MDVector mva = new MDVector(da1,da2,da3);
			
			MDVector deltav = new MDVector(0,0,0);
			deltav = MVMath.ProMVNum(mva, dstep);
			//debugout("calcAcc() -  Delta-v (dv1,dv2,dv3): "+deltav.x1+","+deltav.x2+","+deltav.x3);
			
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
			
			//füge dv zu gesamt v hinzu
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
			mlvds = MVMath.ProMVNum(MVMath.ConvertToL(mpold.getMDVSpeed()),dstep);
			//debugout("calcAcc() -  |mdv-v|,v1,v2,v3: "+mpold.getMDVSpeed().abs()+","+mpold.getMDVSpeed().x1+","+mpold.getMDVSpeed().x2+","+mpold.getMDVSpeed().x3);
			//debugout("calcAcc() -  |mlv-v|,v1,v2,v3: "+MVMath.ConvertToL(mpold.getMDVSpeed()).abs()+","+MVMath.ConvertToL(mpold.getMDVSpeed()).x1+","+MVMath.ConvertToL(mpold.getMDVSpeed()).x2+","+MVMath.ConvertToL(mpold.getMDVSpeed()).x3);
			//debugout("calcAcc() -  |delta-s|,ds1,ds2,ds3: "+mlvds.abs()+","+mlvds.x1+","+mlvds.x2+","+mlvds.x3);
					
			//new position = ds + old position
			long limit;
			if(mpold.getCoordMLV().x1 < 0)
				limit = Long.MIN_VALUE - mpold.getCoordMLV().x1;
			else
				limit = Long.MAX_VALUE - mpold.getCoordMLV().x1;
			if(Math.abs(limit) - Math.abs(mlvds.x1) < 0) {
				myController.flagcalc = false;
				Controller.debugout("calcAcc - Obj"+mpold.id+" Long Limit1 reached, break");
				error = LONGLIMIT;
				return vmpsout;
			}
			
			if(mpold.getCoordMLV().x2 < 0)
				limit = Long.MIN_VALUE - mpold.getCoordMLV().x2;
			else
				limit = Long.MAX_VALUE - mpold.getCoordMLV().x2;
			if(Math.abs(limit) - Math.abs(mlvds.x2) < 0) {
				myController.flagcalc = false;
				Controller.debugout("calcAcc - Obj"+mpold.id+" Long Limit1 reached, break");
				error = LONGLIMIT;
				return vmpsout;
			}

			if(mpold.getCoordMLV().x3 < 0)
				limit = Long.MIN_VALUE - mpold.getCoordMLV().x3;
			else
				limit = Long.MAX_VALUE - mpold.getCoordMLV().x3;
			if(Math.abs(limit) - Math.abs(mlvds.x3) < 0) {
				myController.flagcalc = false;
				Controller.debugout("calcAcc - Obj"+mpold.id+" Long Limit1 reached, break");
				error = LONGLIMIT;
				return vmpsout;
			}
			
				
			mpnew.addMLVCoord(mlvds);
			//mp.mlvpos = MVMath.AddMV(mlvds, mp.mlvpos);
			debugout("calcAcc() - ID "+mpnew.id+": New Coords(x1,x2,x3): "+mpnew.mlvpos.x1+" , "+mpnew.mlvpos.x2+" , "+mpnew.mlvpos.x3);	
		}
		debugout("calcAcc() - Finish, size="+vmpsout.size());	
		return vmpsout;
	}

	
	//Berechnet die Kraft auf ein referenz objekt (mpmain)
	//Kraft entsteht durch grav-wirkung aller anderen
	public MDVector calcForce(Masspoint mpmain, double dstep, Vector<Masspoint> vmpsinsert) {
		MDVector mdvforcetotal = new MDVector(0,0,0);
		debugout("calcForce() - START  ID "+mpmain.id+"; size="+vmpsinsert.size());
		for(int i=0; i<vmpsinsert.size(); i++) {
			Masspoint mpsec = (Masspoint)vmpsinsert.get(i);
			debugout("calcForce() - ID "+mpsec.id+":START");
			
			//Objekt wechselwirkt nicht mit sich selbst (nicht hier!)
			if(mpmain.id == mpsec.id) 
			{	continue;	}
			
			//veraltet: ehem. Überprüfung von oben
			if(mpmain == mpsec)  {	
				debugout("calcForce() - MoveSteps - WARNING Objects ("+mpmain.id+","+mpsec.id+" had different ids,but are the same object");
				continue;	
			}			

			//distance between objects
			MLVector mlvdist = new MLVector(0,0,0);
			mlvdist = MVMath.SubMV(mpsec.mlvpos,mpmain.mlvpos);

			//halb-relativistische Gravitations-Kraftberechnung
			//(in mehrere Einzelschritt zerlegt)
			
			//absolute newtonian force
			double dforce = 0.0;	// |Fg| = m1*m2*G
			dforce = mpmain.getSRTMass();
			dforce *= mpsec.getSRTMass();
			dforce *= GRAVCONST;
			dforce /= Math.pow(MVMath.ConvertToD(mlvdist).abs(), 2.0);
			debugout("calcForce() - relm1,relm2 = dforce: "+mpmain.getSRTMass()+","+mpsec.getSRTMass()+" = "+dforce);
			debugout("calcForce() - mpsec: ID="+mpsec.id+", absmass="+mpsec.getAbsMass()+", absspeed="+mpsec.getSpeed());
			debugout("calcForce() - mpsec: vx="+mpsec.getMDVSpeed().x1+", absmass="+mpsec.getAbsMass()+", absspeed="+mpsec.getSpeed());
			
			debugout("calcForce() - Gravconst = "+GRAVCONST+","+(double)GRAVCONST+","+(float)GRAVCONST+","+(int)GRAVCONST+","+(long)GRAVCONST);
			
			//mdvrquot calculated with MDVector because 'radius' is r / r^3
			MDVector mdvrquot = new MDVector(0.0,0.0,0.0);
			//Converted from long (mm) to double (meter)
			mdvrquot = MVMath.DivMVNum(MVMath.ConvertToD(mlvdist), MVMath.ConvertToD(mlvdist).abs()); 	// vec r / |r|
			debugout(" Mdvrquot.abs,r1,r2,r3: "+mdvrquot.abs()+","+mdvrquot.x1+","+mdvrquot.x2+","+mdvrquot.x3);
			
			//Force-Vector: combination of mdvrquot and dforce
			MDVector mdvforce = new MDVector(0.0,0.0,0.0);			
			mdvforce = MVMath.ProMVNum(mdvrquot, dforce);	// % * |Fg|
			debugout(" |mdvforce|,x1,x2,x3: "+mdvforce.abs()+","+mdvforce.x1+","+mdvforce.x2+","+mdvforce.x3);
			
			//kraft von objekt i auf ref-objekt wird zu einer gesamt kraft addiert
			mdvforcetotal = MVMath.AddMV(mdvforcetotal,mdvforce);
			debugout(" |mvforcetotal|,x1,x2,x3: "+mdvforcetotal.abs()+","+mdvforcetotal.x1+","+mdvforcetotal.x2+","+mdvforcetotal.x3);
		}
		debugout("calcForce() - |mvforcesum|,x1,x2,x3: "+mdvforcetotal.abs()+","+mdvforcetotal.x1+","+mdvforcetotal.x2+","+mdvforcetotal.x3);
		
		//return gesamtkraft
		return mdvforcetotal;
	}
	
	//Kollisions-Abfrage. Alle Objekte werden geprüft ob sie kollidieren
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
				double iradius;
				double jradius;
				//check if the two objects are colliding 
				//(they are also colliding if object goes into schwarschild radius)
				if(mpi.getRadius() > mpi.getSchwarzschildRadius())
					iradius = mpi.getRadius(); 
				else
					iradius = mpi.getSchwarzschildRadius();
				if(mpj.getRadius() > mpj.getSchwarzschildRadius())
					jradius = mpj.getRadius(); 
				else {
					jradius = mpj.getSchwarzschildRadius();
					//if objects have a distance less than 2*Schwarzschild Radius the calculation gets very imprecisely
					if(myController.flagschwarzschild == false && mpi.drange(mpj) < 2*(mpj.getSchwarzschildRadius()+mpi.getSchwarzschildRadius())) {
						myController.flagschwarzschild = true;
					}
				}
						
				if(mpi.drange(mpj) < (iradius+jradius)) {
					debugout("collisionCheck() - Object "+mpi.id+" collided with Object"+mpj.id);
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
		double dmass = mpsurvive.getAbsMass() + mpkill.getAbsMass();	//die massen werden addiert
		double dvolume = mpsurvive.getVolume() + mpkill.getVolume();	//die volumina (nicht die radien!) werden addiert
		debugout("Collision! Object "+mpsurvive.id+" ("+mpsurvive.getVolume()+") and Object "+mpkill.id+"/kill ("+mpkill.getVolume()+") collided. New volume: "+dvolume);
		//Berechnung des neuen radiuses aus dem Volumen
		double dradius = Math.pow((3*dvolume)/(4*Math.PI), 1.0/3.0);	//V=4/3*r^3*PI --> r = 3.sqrt(3*V/PI/4)
		debugout("Collision! Object "+mpsurvive.id+" ("+mpsurvive.getRadius()+") and Object "+mpkill.id+"/kill ("+mpkill.getRadius()+") collided. New radius: "+dradius);
		//double dradius = mp1.getRadius() + mp2.getRadius();  //DEBUG - has to be replaced by following line:
		//Math.pow( (3*dvolume)/(4*Math.PI), 1/3);	//V=4/3*r^3*PI --> r = 3.sqrt(3*V/PI/4)
		
		//new momentum (=impuls)
		// TODO a lot to do ;)
		MDVector mdvmoment1 = MVMath.ProMVNum(mpsurvive.getMDVSpeed(), mpsurvive.getSRTMass());	//momentum = gamma*absmass*speed
		MDVector mdvmoment2 = MVMath.ProMVNum(mpkill.getMDVSpeed(), mpkill.getSRTMass());	//momentum = gamma*absmass*speed
		MDVector mdvmoment = MVMath.AddMV(mdvmoment1, mdvmoment2);
		double dfactora = MVMath.ProScaMV(mdvmoment, mdvmoment);	//(momentum1+momentum2)^2
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
		
		if(mpsurvive.getSchwarzschildRadius() > mpsurvive.getRadius())
			dvolumesurvive = mpsurvive.getSchwarzschildVolume();
		
		if(mpkill.getSchwarzschildRadius() > mpkill.getRadius())
			dvolumekill = mpkill.getSchwarzschildVolume();
		
		double dconst = (dvolumesurvive+dvolumekill) / 2.0;
		
		MLVector mlvcoordsur = MVMath.ProMVNum(mpsurvive.getCoordMLV(), dvolumesurvive/dconst);
		MLVector mlvcoordkil = MVMath.ProMVNum(mpkill.getCoordMLV(), dvolumekill/dconst);
		MLVector mlvnewcoord = MVMath.AddMV(mlvcoordsur, mlvcoordkil);
		mlvnewcoord = MVMath.DivMVNum(mlvnewcoord, (dvolumesurvive+dvolumekill)/dconst);
		
		mpsurvive.setCoordMLV(mlvnewcoord);
		mpsurvive.setMass(dmass);
		if(!mpsurvive.setSpeed(mpsecspeed)) {
			myController.flagcalc = false;
			error = LIGHTSPEEDERROR;
		}
		mpsurvive.setRadius(dradius);
		//myController.RemoveMp(mp2);
		return mpsurvive;
	}
	
	public boolean checkSpeedBorder(Vector<Masspoint> vmpinsert, double dpercentage) {
		//diese for-schleife dient nur dafür, dass die berechnung exakt wird, falls ein objekt über dpercentage% c kommt
    	for(int i=0;i<vmpinsert.size();i++) {
    		Masspoint mp = (Masspoint)vmpinsert.get(i);
			//debugout("checkSpeedBorder() - Object Nr."+i+"/"+mp.id+" (vx,vy,vz): "+mp.getMDVSpeed().x1+","+mp.getMDVSpeed().x2+","+mp.getMDVSpeed().x3);
			//debugout("checkSpeedBorder() - Object Nr."+i+"/"+mp.id+" has a speed of "+mp.getSpeed()+" m/s (<"+dpercentage+"*Lightspeed="+dpercentage*LIGHTSPEED+")");
    		if(mp.getSpeed() >= (dpercentage*LIGHTSPEED)) {
    			debugout("checkSpeedBorder() - Object Nr."+i+"/"+mp.id+" has a speed larger "+dpercentage*100.0+"% lightspeed");
    			return true;
    		}
    	}
    	debugout("checkSpeedBorder() - No Object has a speed larger 75% lightspeed");
    	return false;
	}
}
