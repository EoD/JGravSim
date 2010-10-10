package jgravsim;

import java.awt.Color;
import java.awt.Graphics;

public class ObjectView2D extends ObjectView {
	private static final long serialVersionUID = 1L;

	protected static final Color COLOR_BH = Color.BLACK;
	protected static final Color COLOR_BH_SEL = new Color(0, 0, 128);
	protected static final Color COLOR_STD = Color.DARK_GRAY;
	protected static final Color COLOR_STD_SEL = Color.RED; // Color(34,139,34); // ==ForestGreen

	protected static final Color COLOR_SPEEDVEC = Color.RED;
	protected static final Color COLOR_STRING = Color.BLACK;
	protected static final Color COLOR_STRING_BRIGHT = new Color(220, 220, 220);
	protected static final Color COLOR_CLICKME = Color.ORANGE;

	ObjectView2D(String axes) {
		super();
		init( axes.charAt(0), axes.charAt(1) );
	}	
	ObjectView2D(char[] axes) {
		super();
		init(axes[0], axes[1]);
	}
	
	private void init(char axis0, char axis1) {
		cAxes = new char[] { axis0, axis1};
		coSpeedvecColor = COLOR_SPEEDVEC;
		coObjColor = COLOR_STD;
		this.setBackground(Color.white);
		str_clickme = "";
		repaint();
	}
	
//	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int centerX = getWidth()/2;
		int centerY = getHeight()/2;
		drawGrid(g,centerX,centerY);
		if(str_clickme != null && !str_clickme.isEmpty() && str_clickme != "")
			drawClickmeOverlay(g, centerX, centerY);
		
		if(stCurrent != null 
				&& stCurrent.getMasspoints() != null
				&& stCurrent.getMasspoints().length > 0) {
			Masspoint_Sim[] masspoints = stCurrent.getMasspoints();
			for (int i = 0; i < masspoints.length; i++) {
				Masspoint_Sim masspoint = masspoints[i]; 

				
				/* Speedvector */
				double mpSpeed = getVectorLength(masspoint.getSpeedX(), masspoint.getSpeedY(), masspoint.getSpeedZ());
				double factor = ((speedvecmax * mpSpeed / CalcCode.LIGHTSPEED) +5);
					
				boolean bselected = false;
				if(mp_selected != null && mp_selected.id == masspoint.getID())
					bselected = true;
				
			
				double totradius = masspoint.getRadius();
				if(masspoint.isBlackHole()) {
					if(bselected)
						coObjColor = COLOR_BH_SEL;
					else
						coObjColor = COLOR_BH;
				}
				else {
					if(bselected)
						coObjColor = COLOR_STD_SEL;
					else
						coObjColor = COLOR_STD;
				}
				
				double dRadius = MVMath.mtopx(totradius, this);
				//dRadius /= Math.pow(10.0, iZoomLevel);
				//dRadius *= iGridOffset;///CalcCode.LACCURACY;

				if (dRadius < RADIUS_MIN)
					dRadius = RADIUS_MIN;
				
				/* Masspoint */
				double[] mppos = MVMath.coordtopx(masspoint.getPos(), cAxes, this);
				int mpPosX = (int)(mppos[0]-dRadius); /* Masspoint position */
				int svPosX = 0; /* Speedvector position */
				
				if(cAxes[0] == 'x') {
					svPosX = (int) ((masspoint.getSpeedX()/mpSpeed) * factor) + mpPosX;
				} else if (cAxes[0] == 'y') {
					svPosX = (int) ((masspoint.getSpeedY()/mpSpeed) * factor) + mpPosX;
				} else {
					svPosX = (int) ((masspoint.getSpeedZ()/mpSpeed) * factor) + mpPosX;
				}
				
				int mpPosY = (int)(mppos[1]-dRadius);
				int svPosY = 0;
				
				if(cAxes[1] == 'x') {
					svPosY =  mpPosY - (int) ((masspoint.getSpeedX()/mpSpeed) * factor);
				} else if (cAxes[1] == 'y') {
					svPosY =  mpPosY - (int) ((masspoint.getSpeedY()/mpSpeed) * factor);
				} else {
					svPosY =  mpPosY - (int) ((masspoint.getSpeedZ()/mpSpeed) * factor);
				}
				if( mpPosX+2.0*dRadius > 0 && mpPosY+2.0*dRadius > 0 
						&& mpPosX < this.getWidth() 
						&& mpPosY < this.getHeight()) {
					
					//if(2.0*dRadius > this.getWidth() && 2.0*dRadius > this.getHeight())
					//	coObjColor = HIDDEN;
					
					g.setColor(coObjColor);
					g.fillArc((int)(mpPosX), (int)(mpPosY), (int)(dRadius*2.0), (int)(dRadius*2.0), 0, 360);
				}
				
				if(draw_speed) {
					g.setColor(coSpeedvecColor);
					g.drawLine(mpPosX+(int)dRadius, mpPosY+(int)dRadius, svPosX+(int)dRadius, svPosY+(int)dRadius);
				}
				if(draw_strings) {
					Color coString = coObjColor;
					int string_distance = 7;
					
					if(dRadius > 5+string_distance) {//integer is written inside the radius
						//you can't raise the brightness of black or white
						float[] hsb_color = Color.RGBtoHSB(coObjColor.getRed(), coObjColor.getGreen(), coObjColor.getBlue(), null);
						float[] hsb_lgray = Color.RGBtoHSB(Color.LIGHT_GRAY.getRed(), Color.LIGHT_GRAY.getGreen(), Color.LIGHT_GRAY.getBlue(), null);		
					
						
						if(coObjColor == Color.BLACK || hsb_color[2] <= hsb_lgray[2])
							coString = COLOR_STRING_BRIGHT;
						else if(hsb_color[2] > hsb_lgray[2]) {
							coString = COLOR_STRING;
						} else 
							coString = Color.RED;
					}

					g.setColor(coString);
					//center the string to the circle and move it string_distance pixel away from center
					g.drawString(Integer.toString(masspoint.getID()), mpPosX+(int)dRadius+string_distance, mpPosY+(int)dRadius+(5+string_distance));
				}
			}
		}
		
		if(alldots != null && alldots.length > 0) {
			for(int j=0; j < alldots.length; j++) {
				if(alldots[j] != null 
						&& alldots[j].getMasspoints() != null
						&& alldots[j].getMasspoints().length > 0) {
					Masspoint_Sim[] masspoints = alldots[j].getMasspoints();
					for (int i = 0; i < masspoints.length; i++) {
						Masspoint_Sim masspoint = masspoints[i]; 
									
						if(masspoint.isInvisible())
							continue;
						
						double dRadius = 1.0;
						
						if(masspoint.isBlackHole()) {
							if(masspoint.isHighlighted()) {
								dRadius = 1.5;
								coObjColor = COLOR_BH_SEL;
							} else
								coObjColor = COLOR_BH;
						}
						else {
							if(masspoint.isHighlighted()) {
								dRadius = 1.5;
								coObjColor = COLOR_STD_SEL;
							}
							else
								coObjColor = COLOR_STD;
						}
						
						/* Masspoint */
						int mpPosX = 0; /* Masspoint position */
						
						if(cAxes[0] == 'x') {
							mpPosX = (int) ((( MVMath.ConvertToD(masspoint.getPosX()) +dCoordOffsetX)/Math.pow(10, iZoomLevel))*iGridOffset) + centerX - (int) (dRadius);
						} 
						else if (cAxes[0] == 'y') {
							mpPosX = (int) ((( MVMath.ConvertToD(masspoint.getPosY()) +dCoordOffsetY)/Math.pow(10, iZoomLevel))*iGridOffset) + centerX - (int) (dRadius);
						}
						else {
							mpPosX = (int) ((( MVMath.ConvertToD(masspoint.getPosZ()) +dCoordOffsetZ)/Math.pow(10, iZoomLevel))*iGridOffset) + centerX - (int) (dRadius);
						}
						
						int mpPosY = 0;
						if(cAxes[1] == 'x') {
							mpPosY = centerY - (int) ((( MVMath.ConvertToD(masspoint.getPosX()) +dCoordOffsetX)/Math.pow(10, iZoomLevel))*iGridOffset) - (int) (dRadius);
						}
						else if (cAxes[1] == 'y') {
							mpPosY = centerY - (int) ((( MVMath.ConvertToD(masspoint.getPosY()) +dCoordOffsetY)/Math.pow(10, iZoomLevel))*iGridOffset) - (int) (dRadius);
						}
						else {
							mpPosY = centerY - (int) ((( MVMath.ConvertToD(masspoint.getPosZ()) +dCoordOffsetZ)/Math.pow(10, iZoomLevel))*iGridOffset) - (int) (dRadius);
						}
						
						g.setColor(coObjColor);
						g.fillArc(mpPosX, mpPosY, (int)(dRadius*2.0), (int)(dRadius*2.0), 0, 360);
					}
				}
			}
		}
		
		//this is for the speed-vec Frame
		if(mp_selected != null && alldots == null && stCurrent == null) {
			Masspoint_Sim masspoint = mp_selected.toMasspoint_Sim();

			/* Speedvector */
			if(masspoint.isSpeedVecNull()) {
				masspoint.setSpeedVecNotNull();
			}
			double factor = 30;
			
			if(masspoint.isBlackHole()) {
				coObjColor = COLOR_BH;
			} else {
				coObjColor = COLOR_STD;
			}
			
			double dRadius = 5;

			if (dRadius < RADIUS_MIN)
				dRadius = RADIUS_MIN;
			
			/* Masspoint */
			double[] mppos = MVMath.coordtopx(new MLVector(0,0,0), cAxes, this);
			
			int mpPosX = (int)(mppos[0]-dRadius); /* Masspoint position */
			int svPosX = 0; /* Speedvector position */
			int mpPosY = (int)(mppos[1]-dRadius);
			int svPosY = 0;
			float[] dSpeedAngles = mp_selected.getUnitSpeedAngle();
			double dSpeedTheta = Math.toRadians(dSpeedAngles[0]);
			double dSpeedPhi = Math.toRadians(dSpeedAngles[1]);
			
			if (cAxes[0] == 'x' && cAxes[1] == 'y') {
				svPosX =  (int)(Math.cos(dSpeedPhi) * factor) + mpPosX + (int)dRadius;
				svPosY =  mpPosY + (int)dRadius - (int)(Math.sin(dSpeedPhi) * factor);
			}
			else if (cAxes[0] == 'x' && cAxes[1] == 'z') {
				svPosX =  (int)(Math.sin(dSpeedTheta) * factor) + mpPosX + (int)dRadius;
				svPosY =  mpPosY + (int)dRadius - (int)(Math.cos(dSpeedTheta) * factor);
			}
			else {
				debugout("cAxes="+String.copyValueOf(cAxes));
				System.exit(0);
			}
			if( mpPosX+2.0*dRadius > 0 && mpPosY+2.0*dRadius > 0 
					&& mpPosX < this.getWidth() 
					&& mpPosY < this.getHeight()) {
				
				//if(2.0*dRadius > this.getWidth() && 2.0*dRadius > this.getHeight())
				//	coObjColor = HIDDEN;
				
				g.setColor(coObjColor);
				g.fillArc((int)(mpPosX), (int)(mpPosY), (int)(dRadius*2.0), (int)(dRadius*2.0), 0, 360);
			}
			
			g.setColor(coSpeedvecColor);
			g.drawLine(mpPosX+(int)dRadius, mpPosY+(int)dRadius, svPosX, svPosY);
		}
	}
	
	@Override
	void displayStep(Step next) {
		stCurrent = next;
		repaint();
	}
}
