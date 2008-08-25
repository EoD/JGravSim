package gravity_sim;

public class MDVector {
	
	double x1;
	double x2;
	double x3;
	
	public MDVector(double k1, double k2, double k3) {
		x1 = k1;
		x2 = k2;
		x3 = k3;
	}
	
	public void setData(double y1, double y2, double y3) {
		x1 = y1;
		x2 = y2;
		x3 = y3;
	}
	
	public double abs() {
		double dres = 0.0;
		dres = Math.sqrt(x1*x1 + x2*x2 + x3*x3);
		return dres;
	}
	
	public MDVector UnitVec() {
		MDVector mvres = new MDVector(0.0,0.0,0.0);
		double d = abs();
		//stop before 0/0
		if(abs() <= 0)
			return mvres;
		
		mvres.x1 = x1 / d;
		mvres.x2 = x2 / d;
		mvres.x3 = x3 / d;
		return mvres;
	}
	
	public double[] GetCoords() {
		double[] coordinates = new double[3];
		coordinates[0] = x1;
		coordinates[1] = x2;
		coordinates[2] = x3;
		return coordinates;
	}
}