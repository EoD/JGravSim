package jgravsim;

public class Masspoint_Sim extends Masspoint {
	private int highlighted;

	public Masspoint_Sim(int iid, double mass, double radius, double speed, double speedX, double speedY, double speedZ, double accX, double accY, double accZ, long posX, long posY, long posZ) {
		super(iid, mass, radius, speed, speedX, speedY, speedZ, posX, posY, posZ);
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
		highlighted = 0;
	}
	
	public void setDrawStatus(int a) {
		highlighted = a;
	}
	
	public void setInvisible(boolean b) {
		if(b)
			highlighted = -1;
		else
			highlighted = 0;
	}
	public void setHighlighted(boolean b) {
		if(b)
			highlighted = 1;
		else
			highlighted = 0;
	}
	public int getDrawStatus() {
		return highlighted;
	}
	
	public boolean isHighlighted() {
		if(highlighted == 1)
			return true;
		else
			return false;
	}
	public boolean isInvisible() {
		if(highlighted == -1)
			return true;
		else
			return false;
	}
	
	public void setSpeedVecNotNull() {
		dSpeedVecX = 1.0;
		dSpeedVecY = 1.0;
		dSpeedVecZ = 1.0;
	}
	public boolean isSpeedVecNull() {
		if(dSpeedVecX == 0 && dSpeedVecY == 0 && dSpeedVecZ == 0)
			return true;
		else 
			return false;
	}
}
