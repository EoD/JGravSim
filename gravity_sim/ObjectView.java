package gravity_sim;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class ObjectView extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final double LIGHTSPEED = CalcCode.LIGHTSPEED; 	//Lightspeed in m/s
	int iLastMouseX = 0;
	int iLastMouseY = 0;
	
	int iGridOffset = 25; /* 25px = 1*Unit */
	double iZoomLevel = 12.0; /* pos / 10^zoomLevel */
	Color coGridColor = Color.decode("#DDDDDD");
	Color coSpeedvecColor = Color.BLACK;
	Color coObjColor = Color.BLACK;
	char [] cAxes; /* which axis to draw */
	double dCoordOffsetX = 0.0;
	double dCoordOffsetY = 0.0;
	double dCoordOffsetZ = 0.0;
	Step[] alldots;
	
	Step stCurrent = null;
	
	ObjectView(String orient, String axes) {
		super();
		cAxes = new char[2];
		cAxes[0] = axes.charAt(0); /* set "x" axis*/
		cAxes[1] = axes.charAt(1); /* set "y" axis */
		this.setBackground(Color.white);
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int centerX = getWidth()/2;
		int centerY = getHeight()/2;
		drawGrid(g,centerX,centerY);
		if(alldots != null && alldots.length > 0) {
			for(int j=0; j < alldots.length; j++) {
				if(alldots[j] != null && alldots[j].getMasspoints().length > 0) {
					Masspoint_Sim[] masspoints = alldots[j].getMasspoints();
					for (int i = 0; i < masspoints.length; i++) {
						Masspoint_Sim masspoint = masspoints[i]; 
									
						double dRadius = 1;
						
						/* Masspoint */
						int mpPosX = 0; /* Masspoint position */
						
						if(cAxes[0] == 'x') {
							mpPosX = (int) (((masspoint.getPosX()/CalcCode.LACCURACY+dCoordOffsetX)/Math.pow(10, iZoomLevel))*iGridOffset) + centerX - (int) (dRadius);
						} 
						else if (cAxes[0] == 'y') {
							mpPosX = (int) (((masspoint.getPosY()/CalcCode.LACCURACY+dCoordOffsetY)/Math.pow(10, iZoomLevel))*iGridOffset) + centerX - (int) (dRadius);
						}
						else {
							mpPosX = (int) (((masspoint.getPosZ()/CalcCode.LACCURACY+dCoordOffsetZ)/Math.pow(10, iZoomLevel))*iGridOffset) + centerX - (int) (dRadius);
						}
						
						int mpPosY = 0;
						if(cAxes[1] == 'x') {
							mpPosY = centerY - (int) (((masspoint.getPosX()/CalcCode.LACCURACY+dCoordOffsetX)/Math.pow(10, iZoomLevel))*iGridOffset) - (int) (dRadius);
						}
						else if (cAxes[1] == 'y') {
							mpPosY = centerY - (int) (((masspoint.getPosY()/CalcCode.LACCURACY+dCoordOffsetY)/Math.pow(10, iZoomLevel))*iGridOffset) - (int) (dRadius);
						}
						else {
							mpPosY = centerY - (int) (((masspoint.getPosZ()/CalcCode.LACCURACY+dCoordOffsetZ)/Math.pow(10, iZoomLevel))*iGridOffset) - (int) (dRadius);
						}
						
						g.setColor(coObjColor);
						g.fillArc(mpPosX, mpPosY, (int)dRadius*2, (int)dRadius*2, 0, 360);
						
					}
				}
			}
		}
		
		
		if(stCurrent != null && stCurrent.getMasspoints().length > 0) {
			Masspoint_Sim[] masspoints = stCurrent.getMasspoints();
			for (int i = 0; i < masspoints.length; i++) {
				Masspoint_Sim masspoint = masspoints[i]; 

				/* Speedvector */
				double mpSpeed = getVectorLength(masspoint.getSpeedX(), masspoint.getSpeedY(), masspoint.getSpeedZ());
				double factor = ((45 * mpSpeed / LIGHTSPEED) +5);
					
/*
				int color;
				
				if(Controller.GetSelectedMasspoint().id == masspoint.id)
					color = 255;
				else
					color = 210;
*/				
				double totradius;	
				if(masspoint.getSchwarzschildRadius() > masspoint.getRadius()) {
					totradius = masspoint.getSchwarzschildRadius();
					coObjColor = Color.GRAY;
				}
				else {
					totradius = masspoint.getRadius();	
					coObjColor = Color.BLACK;
				}
				
				double dRadius = ((totradius)/Math.pow(10, iZoomLevel))*iGridOffset;///CalcCode.LACCURACY;
				if(dRadius<2) dRadius=2;
				
				/* Masspoint */
				int mpPosX = 0; /* Masspoint position */
				int svPosX = 0; /* Speedvector position */
				
				if(cAxes[0] == 'x') {
					mpPosX = (int) (((masspoint.getPosX()/CalcCode.LACCURACY+dCoordOffsetX)/Math.pow(10, iZoomLevel))*iGridOffset) + centerX - (int) (dRadius);
					svPosX = (int) ((masspoint.getSpeedX()/mpSpeed) * factor) + mpPosX;
				} 
				else if (cAxes[0] == 'y') {
					mpPosX = (int) (((masspoint.getPosY()/CalcCode.LACCURACY+dCoordOffsetY)/Math.pow(10, iZoomLevel))*iGridOffset) + centerX - (int) (dRadius);
					svPosX = (int) ((masspoint.getSpeedY()/mpSpeed) * factor) + mpPosX;
				}
				else {
					mpPosX = (int) (((masspoint.getPosZ()/CalcCode.LACCURACY+dCoordOffsetZ)/Math.pow(10, iZoomLevel))*iGridOffset) + centerX - (int) (dRadius);
					svPosX = (int) ((masspoint.getSpeedZ()/mpSpeed) * factor) + mpPosX;
				}
				
				int mpPosY = 0;
				int svPosY = 0;
				if(cAxes[1] == 'x') {
					mpPosY = centerY - (int) (((masspoint.getPosX()/CalcCode.LACCURACY+dCoordOffsetX)/Math.pow(10, iZoomLevel))*iGridOffset) - (int) (dRadius);
					svPosY =  mpPosY - (int) ((masspoint.getSpeedX()/mpSpeed) * factor);
				}
				else if (cAxes[1] == 'y') {
					mpPosY = centerY - (int) (((masspoint.getPosY()/CalcCode.LACCURACY+dCoordOffsetY)/Math.pow(10, iZoomLevel))*iGridOffset) - (int) (dRadius);
					svPosY =  mpPosY - (int) ((masspoint.getSpeedY()/mpSpeed) * factor);
				}
				else {
					mpPosY = centerY - (int) (((masspoint.getPosZ()/CalcCode.LACCURACY+dCoordOffsetZ)/Math.pow(10, iZoomLevel))*iGridOffset) - (int) (dRadius);
					svPosY =  mpPosY - (int) ((masspoint.getSpeedZ()/mpSpeed) * factor);
				}
				
				g.setColor(coObjColor);
				g.fillArc(mpPosX, mpPosY, (int)dRadius*2, (int)dRadius*2, 0, 360);
				
				
				g.setColor(coSpeedvecColor);
				g.drawLine(mpPosX+(int)dRadius, mpPosY+(int)dRadius, svPosX+(int)dRadius, svPosY+(int)dRadius);
				
				g.setColor(coObjColor);
				g.drawString(Integer.toString(masspoint.getID()), mpPosX+10, mpPosY+10);
			}
		}
	}
	
	double getVectorLength(double x, double y, double z) {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	void drawGrid(Graphics g, int centerX, int centerY) {
		g.setColor(coGridColor);
		g.drawLine(centerX, 0, centerX, getHeight());
		g.drawLine(0, centerY, getWidth(), centerY);
		
		int gridXstart;
		int gridYstart;
		
		for(gridXstart=centerX;gridXstart>0;gridXstart-=iGridOffset);
		for(gridYstart=centerY;gridYstart>0;gridYstart-=iGridOffset);		
		
		for(int offsetX=gridXstart;offsetX<getWidth();offsetX+=iGridOffset) {
			for(int offsetY=gridYstart;offsetY<getHeight();offsetY+=iGridOffset) {
				drawGridPoint(g, offsetX, offsetY);
			}
		}
		g.setColor(Color.black);
		g.drawString(""+cAxes[0], this.getWidth()-10, centerY+10);
		g.drawString(""+cAxes[1], centerX-10, 20);
	}
	
	void drawGridPoint(Graphics g, int offsetX, int offsetY) {
		g.drawLine(offsetX, offsetY-2, offsetX, offsetY+2);
		g.drawLine(offsetX-2, offsetY, offsetX+2, offsetY);
	}
	
	void displayStep(Step next) {
		stCurrent = next;
		repaint();
	}	
}
