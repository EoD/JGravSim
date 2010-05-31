package jgravsim;

public class Step {
	private int iStepNumber;
	private Masspoint_Sim[] mpMasspoints;
	
	private long lWPTLineNumber;
	private boolean bMassPointsLoaded;
	
	public Step(int stepNumber, long lineNumber) {
		iStepNumber = stepNumber;
		lWPTLineNumber = lineNumber;
	}

	public int getStepNumber() {
		return iStepNumber;
	}

	public Masspoint_Sim[] getMasspoints() {
		return mpMasspoints;
	}
	
	public void setMasspoint_Sim(Masspoint_Sim[] mps) {
		bMassPointsLoaded = true;
		mpMasspoints = mps;
	}
	
	public void unloadMasspoints() {
		if(bMassPointsLoaded==false) return;
		for(int i=0;i<mpMasspoints.length;i++) {
			mpMasspoints[i] = null;
		}
		mpMasspoints=null;
		bMassPointsLoaded = false;
	}
	
	public boolean isLoaded() {
		return bMassPointsLoaded;
	}
	
	public long getWPTLineNumber() {
		return lWPTLineNumber;
	}
}
