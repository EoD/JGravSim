package gravity_sim;

public class MVMath {

	static private final double ACCURACY = CalcCode.LACCURACY;
	static public boolean DEBUG = false;
	
	static public void debugout(String a) {
		if(Controller.MAINDEBUG && DEBUG)
			System.out.println(a);
	}
	
	static public MDVector AddMV(MDVector mv1, MDVector mv2) {
		MDVector mvres = new MDVector(0,0,0);
		mvres.x1 = mv1.x1 + mv2.x1;
		mvres.x2 = mv1.x2 + mv2.x2;
		mvres.x3 = mv1.x3 + mv2.x3;
		return mvres;
	}
	static public MLVector AddMV(MLVector mv1, MLVector mv2) {
		MLVector mvres = new MLVector(0,0,0);
		mvres.x1 = mv1.x1 + mv2.x1;
		mvres.x2 = mv1.x2 + mv2.x2;
		mvres.x3 = mv1.x3 + mv2.x3;
		return mvres;
	}
	
	static public MDVector SubMV(MDVector mv1, MDVector mv2) {
		MDVector mvres = new MDVector(0,0,0);
		mvres.x1 = mv1.x1 - mv2.x1;
		mvres.x2 = mv1.x2 - mv2.x2;
		mvres.x3 = mv1.x3 - mv2.x3;
		return mvres;
	}
	static public MLVector SubMV(MLVector mv1, MLVector mv2) {
		MLVector mvres = new MLVector(0,0,0);
		mvres.x1 = mv1.x1 - mv2.x1;
		mvres.x2 = mv1.x2 - mv2.x2;
		mvres.x3 = mv1.x3 - mv2.x3;
		return mvres;
	}
	
	static public double ProScaMV(MDVector mv1, MDVector mv2) {
		double ires = 0;
		ires = mv1.x1 * mv2.x1 + mv1.x2 * mv2.x2 +  mv1.x3 * mv2.x3;
		return ires;
	}	
	static public double ProScaMV(MLVector mv1, MLVector mv2) {
		double ires = 0;
		ires = mv1.x1 * mv2.x1 + mv1.x2 * mv2.x2 +  mv1.x3 * mv2.x3;
		return ires;
	}
	
	static public MDVector ProVecMV(MDVector mv1, MDVector mv2) {
		MDVector mvres = new MDVector(0,0,0);
		mvres.x1 = mv1.x2 * mv2.x1 - mv1.x3 * mv2.x2;
		mvres.x2 = mv1.x3 * mv2.x1 - mv1.x1 * mv2.x3;
		mvres.x3 = mv1.x1 * mv2.x2 - mv1.x2 * mv2.x1;
		return mvres;
	}	
	static public MLVector ProVecMV(MLVector mv1, MLVector mv2) {
		MLVector mvres = new MLVector(0,0,0);
		mvres.x1 = mv1.x2 * mv2.x1 - mv1.x3 * mv2.x2;
		mvres.x2 = mv1.x3 * mv2.x1 - mv1.x1 * mv2.x3;
		mvres.x3 = mv1.x1 * mv2.x2 - mv1.x2 * mv2.x1;
		return mvres;
	}
	
	static public MDVector ProMVNum(MDVector mv, double i) {
		MDVector mvres = new MDVector(0,0,0);
		mvres.x1 = mv.x1 * i;
		mvres.x2 = mv.x2 * i;
		mvres.x3 = mv.x3 * i;
		return mvres;		
	}
	static public MLVector ProMVNum(MLVector mv, double i) {
		MLVector mvres = new MLVector(0,0,0);
		mvres.x1 = (long)(mv.x1 * i);
		mvres.x2 = (long)(mv.x2 * i);
		mvres.x3 = (long)(mv.x3 * i);
		return mvres;		
	}
	
	static public MDVector DivMVNum(MDVector mv, double i) {
		MDVector mvres = new MDVector(0,0,0);
		mvres.x1 = mv.x1 / i;
		mvres.x2 = mv.x2 / i;
		mvres.x3 = mv.x3 / i;
		return mvres;		
	}	
	static public MLVector DivMVNum(MLVector mv, double i) {
		MLVector mvres = new MLVector(0,0,0);
		mvres.x1 = (long)(mv.x1 / i);
		mvres.x2 = (long)(mv.x2 / i);
		mvres.x3 = (long)(mv.x3 / i);
		return mvres;		
	}
	
	//*** no more necessary
/*	static public double abs(MVector mv1) {
		double dres = 0;
		dres = Math.sqrt(mv1.x1*mv1.x1 + mv1.x2*mv1.x2 + mv1.x3*mv1.x3);
		return dres;
	}	
	static public long abs(MLVector mv1) {
		long dres = 0;
		dres = (long)Math.sqrt(mv1.x1*mv1.x1 + mv1.x2*mv1.x2 + mv1.x3*mv1.x3);
		return dres;
	}
*/
	
	//random vector from (0.0,0.0,0.0) to (1.0,1.0,1.0)
	static public MDVector random() {
		MDVector mvres = new MDVector(0,0,0);
		mvres.x1 = Math.random();
		mvres.x2 = Math.random();
		mvres.x3 = Math.random();
		return mvres;
	}
	static public MLVector randomL() {
		MLVector mvres = new MLVector(0,0,0);
		mvres.x1 = Math.round(Math.random());
		mvres.x2 = Math.round(Math.random());
		mvres.x3 = Math.round(Math.random());
		return mvres;
	}
	
	/*		//Method not necessary look into MDV and MDL Classes
	static public MDVector UnitVec(MDVector mv1) {
		debugout("UnitVec() MDV - using");
		MDVector mvres = new MDVector(0.0,0.0,0.0);
		//if the mv1 vector is a zerovector it would be 0/0 == NaN
		if(mv1.abs() <= 0) {
			debugout("UnitVec() MDV - 0/0");
			return mvres;
		}
		mvres = DivMVNum(mv1, mv1.abs()); 
		return mvres;
	}	
	static public MLVector UnitVec(MLVector mv1) {
		MLVector mvres = new MLVector(0,0,0);		
		if(mv1.abs() <= 0)
			return mvres;
		mvres = DivMVNum(mv1, mv1.abs()); 
		return mvres;
	}
	*/
	static public MLVector ConvertToL(MDVector mdv) {
		//debugout(" MVMath() - Converting MDVector to MLVector");
		MLVector mlv = new MLVector(0,0,0);
		mlv.x1 = Math.round(mdv.x1*(int)ACCURACY);
		mlv.x2 = Math.round(mdv.x2*(int)ACCURACY);
		mlv.x3 = Math.round(mdv.x3*(int)ACCURACY);
		//debugout(" MVMath() - MDV:"+mdv.x1+","+mdv.x2+","+mdv.x3);
		//debugout(" MVMath() ->MLV:"+mlv.x1+","+mlv.x2+","+mlv.x3);
		return mlv;
	}
	
	//WARNING: use can cause failure!
	static public MDVector ConvertToD(MLVector mlv) {
		//debugout(" MVMath() - Converting MLVector to MDVector");
		MDVector mdv = new MDVector(0,0,0);
		mdv.x1 = (double)(mlv.x1/ACCURACY);
		mdv.x2 = (double)(mlv.x2/ACCURACY);
		mdv.x3 = (double)(mlv.x3/ACCURACY);
		//debugout(" MVMath() - MLV:"+mlv.x1+","+mlv.x2+","+mlv.x3);
		//debugout(" MVMath() ->MDV:"+mdv.x1+","+mdv.x2+","+mdv.x3);
		return mdv;
	}

	
}
