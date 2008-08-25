package jgravsim;

public class MLVector {
	
	long x1;
	long x2;
	long x3;
	
	public MLVector(long k1, long k2, long k3) {
		x1 = k1;
		x2 = k2;
		x3 = k3;
	}
	
	public void setData(long y1, long y2, long y3) {
		x1 = y1;
		x2 = y2;
		x3 = y3;
	}
	
	public long abs() {
		long dres = 0;
		dres = (long)Math.sqrt(x1*x1 + x2*x2 + x3*x3);
		return dres;
	}
	
	public MDVector UnitVec() {
		MDVector mvres = new MDVector(0,0,0);
		double d = abs();		
		//stop before 0/0
		if(abs() <= 0)
			return mvres;
		mvres.x1 = (x1 / d);
		mvres.x2 = (x2 / d);
		mvres.x3 = (x3 / d);
		return mvres;
	}
	
	public long[] GetCoords() {
		long[] coordinates = new long[3];
		coordinates[0] = x1;
		coordinates[1] = x2;
		coordinates[2] = x3;
		return coordinates;
	}
	public boolean isNull() {
		if(x1 == 0 && x2 == 0 && x3 == 0)
			return true;
		else
			return false;
	}
}