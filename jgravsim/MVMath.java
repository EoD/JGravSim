package jgravsim;

import java.lang.Long;

public class MVMath {

	public static final double ACCURACY = CalcCode.LACCURACY;
	public static final int revision = 1;
	public static final boolean DEBUG = false;
	
	@SuppressWarnings("unused")
	private	static void debugout(String a) {
		if(Controller.CURRENTBUILD && DEBUG)
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
	static public long ConvertToL(double d) {
		return Math.round(d*ACCURACY);
	}
	
	static public double ConvertToD(long l) {
		return (new Long(l).doubleValue())/ACCURACY;
	}

	static public MLVector ConvertToL(MDVector mdv) {
		debugout(" MVMath() - Converting MDVector to MLVector");
		MLVector mlv = new MLVector(0,0,0);
		mlv.x1 = ConvertToL(mdv.x1);
		mlv.x2 = ConvertToL(mdv.x2);
		mlv.x3 = ConvertToL(mdv.x3);
		return mlv;
	}
	
	//WARNING: use can cause failure!
	static public MDVector ConvertToD(MLVector mlv) {
		debugout(" MVMath() - Converting MLVector to MDVector");
		MDVector mdv = new MDVector(0,0,0);
		mdv.x1 = ConvertToD(mlv.x1);
		mdv.x2 = ConvertToD(mlv.x2);
		mdv.x3 = ConvertToD(mlv.x3);
		//debugout(" MVMath() - MLV:"+mlv.x1+","+mlv.x2+","+mlv.x3);
		//debugout(" MVMath() ->MDV:"+mdv.x1+","+mdv.x2+","+mdv.x3);
		return mdv;
	}
	
	public static long pxtocoord(int a, char axe, ObjectView2D ov_xy, ObjectView2D ov_yz) {
		if(a == 0) {
			debugout("pxtomm() - a = 0");
			return 0;
		}
		
		ObjectView2D ov_front = ov_xy;
		ObjectView2D ov_top = ov_yz;
		double centerX = ov_front.getWidth()/2;
		double centerY = ov_front.getHeight()/2;
		double centerZ = ov_top.getHeight()/2;
		
		
		switch(axe) {
			case 'x': 
				debugout("pxtomm() - getCoordOffsetX()="+ov_front.getCoordOffsetX());
				double dx = (a-centerX);
				dx /= ov_front.iGridOffset;
				dx *= Math.pow(10, ov_front.iZoomLevel);
				dx -= ov_front.getCoordOffsetX();
				return ConvertToL(dx);
			case 'y': 
				debugout("pxtomm() - getCoordOffsetY()="+ov_front.getCoordOffsetY());
				double dy = (a-centerY);
				dy /= ov_front.iGridOffset;
				dy *= Math.pow(10, ov_front.iZoomLevel);
				dy += ov_front.getCoordOffsetY();
				return -ConvertToL(dy);
			case 'z': 
				debugout("pxtomm() - getCoordOffsetZ()="+ov_front.getCoordOffsetZ());
				double dz = (a-centerZ);
				dz /= ov_front.iGridOffset;
				dz *= Math.pow(10, ov_front.iZoomLevel);
				dz += ov_front.getCoordOffsetZ();
				return -ConvertToL(dz); 
			default: 
				debugout("pxtomm() - ERROR"); 
				return 0;
		}
	}
	/*public static double pxtom(int a, char axe, ObjectView2D ov_xy, ObjectView2D ov_yz) {
		if(a == 0) {
			debugout("pxtom() - a = 0");
			return 0;
		}
		
		ObjectView2D ov_front = ov_xy;
		ObjectView2D ov_top = ov_yz;
		double centerX = ov_front.getWidth()/2;
		double centerY = ov_front.getHeight()/2;
		double centerZ = ov_top.getHeight()/2;
		
		switch(axe) {
			case 'x': 
				debugout("pxtom() - getCoordOffsetX()="+ov_front.getCoordOffsetX());
				double dx = (a-centerX);
				dx /= ov_front.iGridOffset;
				dx *= Math.pow(10, ov_front.iZoomLevel);
				dx -= ov_front.getCoordOffsetX();
				return dx;
			case 'y': 
				debugout("pxtom() - getCoordOffsetY()="+ov_front.getCoordOffsetY());
				double dy = (a-centerY);
				dy /= ov_front.iGridOffset;
				dy *= Math.pow(10, ov_front.iZoomLevel);
				dy += ov_front.getCoordOffsetY();
				return -dy;
			case 'z': 
				debugout("pxtom() - getCoordOffsetZ()="+ov_front.getCoordOffsetZ());
				double dz = (a-centerZ);
				dz /= ov_front.iGridOffset;
				dz *= Math.pow(10, ov_front.iZoomLevel);
				dz += ov_front.getCoordOffsetZ();
				return -dz; 
			default: 
				debugout("pxtom() - ERROR"); 
				return 0;
		}
	}*/
	public static double[] coordtopx(MLVector ds, char[] axe, ObjectView2D ov) {
		double[] a = new double[2];
		a[0] = 0.0;
		a[1] = 0.0;
		ds = MVMath.DivMVNum(ds, CalcCode.LACCURACY);
		
		//if(ds.isNull()) {
		//	debugout("pxtomm() - a = 0");
		//	return a;
		//}
		
		switch(axe[0]) {
			case 'x': 
				double centerX = ov.getWidth()/2;
				debugout("mtopx() - getCoordOffsetX()="+ov.getCoordOffsetX());
				double ax = (ds.x1+ov.getCoordOffsetX()-1);
				ax /= Math.pow(10, ov.iZoomLevel);
				ax *= ov.iGridOffset;
				ax += centerX;
				a[0] = ax;
				break;
			case 'y': 
				double centerY = ov.getWidth()/2;
				debugout("mtopx() - getCoordOffsetY()="+ov.getCoordOffsetY());
				double ay = (ds.x2+ov.getCoordOffsetY()-1);
				ay /= Math.pow(10, ov.iZoomLevel);
				ay *= ov.iGridOffset;
				ay += centerY;
				a[0] = ay;
				break;
			case 'z': 
				double centerZ = ov.getWidth()/2;
				debugout("mtopx() - getCoordOffsetZ()="+ov.getCoordOffsetZ());
				double az = (ds.x3+ov.getCoordOffsetZ()-1);
				az /= Math.pow(10, ov.iZoomLevel);
				az *= ov.iGridOffset;
				az += centerZ;
				a[0] = az;
				break;
			default: 
				debugout("mtopx() - ERROR"); 
				return a;
		}
		switch(axe[1]) {
		case 'x': 
			double centerX = ov.getHeight()/2;
			debugout("mtopx() - getCoordOffsetX()="+ov.getCoordOffsetX());
			double ax = (ds.x1+ov.getCoordOffsetX()-1);
			ax /= Math.pow(10, ov.iZoomLevel);
			ax *= ov.iGridOffset;
			ax = centerX - ax;
			a[1] = ax;
			break;
		case 'y': 
			double centerY = ov.getHeight()/2;
			debugout("mtopx() - getCoordOffsetY()="+ov.getCoordOffsetY());
			double ay = (ds.x2+ov.getCoordOffsetY()-1);
			ay /= Math.pow(10, ov.iZoomLevel);
			ay *= ov.iGridOffset;
			ay = centerY - ay;
			a[1] = ay;
			break;
		case 'z': 
			double centerZ = ov.getHeight()/2;
			debugout("mtopx() - getCoordOffsetZ()="+ov.getCoordOffsetZ());
			double az = (ds.x3+ov.getCoordOffsetZ()-1);
			az /= Math.pow(10, ov.iZoomLevel);
			az *= ov.iGridOffset;
			az = centerZ - az;
			a[1] = az;
			break;
		default: 
			debugout("mtopx() - ERROR"); 
			return a;
		}
		return a;
	}
	public static double[] coordtopx(MLVector ds, ObjectView3D ov) {
		double[] a = new double[3];
		a[0] = 0.0;
		a[1] = 0.0;
		a[2] = 0.0;
		ds = MVMath.DivMVNum(ds, CalcCode.LACCURACY);
		
		//if(ds.isNull()) {
		//	debugout("pxtomm() - a = 0");
		//	return a;
		//}
		
		double centerX = ov.getWidth()/2;
		debugout("mtopx() - getCoordOffsetX()="+ov.getCoordOffsetX());
		double ax = (ds.x1+ov.getCoordOffsetX()-1);
		ax /= Math.pow(10, ov.iZoomLevel);
		ax *= ov.iGridOffset;
		ax += centerX;
		a[0] = ax;
			
		double centerY = ov.getHeight()/2;
		debugout("mtopx() - getCoordOffsetY()="+ov.getCoordOffsetY());
		double ay = (ds.x2+ov.getCoordOffsetY()-1);
		ay /= Math.pow(10, ov.iZoomLevel);
		ay *= ov.iGridOffset;
		ay = centerY - ay;
		a[1] = ay;
			
		double centerZ = ov.getHeight()/2;
		debugout("mtopx() - getCoordOffsetZ()="+ov.getCoordOffsetZ());
		double az = (ds.x3+ov.getCoordOffsetZ()-1);
		az /= Math.pow(10, ov.iZoomLevel);
		az *= ov.iGridOffset;
		az = centerZ - az;
		a[2] = az;
			
		return a;
	}
	
	public static double pxtom(float a, ObjectView2D ov) {
		if(a == 0) {
			debugout("pxtom() - a = 0");
			return 0;
		}
		
		double ds = a;
		ds /= ov.iGridOffset;
		ds *= Math.pow(10, ov.iZoomLevel);
		debugout("pxtom() - a="+a+", ds="+ds);
		return ds;
	}
	
	
	public static double mtopx(double a, ObjectView2D ov) {
		if(a == 0) {
			debugout("pxtom() - a = 0");
			return 0;
		}
		
		double ds = a;
		ds *= ov.iGridOffset;
		ds /= Math.pow(10, ov.iZoomLevel);
		debugout("pxtom() - a="+a+", ds="+ds);
		return ds;
	}
	public static double mtopx(double a, ObjectView3D ov) {
		if(a == 0) {
			debugout("pxtom() - a = 0");
			return 0;
		}
		
		double ds = a;
		ds *= ov.iGridOffset;
		ds /= Math.pow(10, ov.iZoomLevel);
		debugout("pxtom() - a="+a+", ds="+ds);
		return ds;
	}
}
