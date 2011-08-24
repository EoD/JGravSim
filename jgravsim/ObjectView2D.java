package jgravsim;

import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class ObjectView2D extends ObjectView {
	public static final int revision = 1;
	
	ObjectView2D(String axes) {
		super();
		cAxes = new char[2];
		cAxes[0] = axes.charAt(0); /* set "x" axis*/
		cAxes[1] = axes.charAt(1); /* set "y" axis */
		this.setBackground(Color.white);
		str_clickme = "";
		repaint();
	}	
	ObjectView2D(char[] axes) {
		super();
		cAxes = new char[2];
		cAxes = axes;
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
				double factor = ((speedvecmax * mpSpeed / LIGHTSPEED) +5);
					
				boolean bselected = false;
				if(mp_selected != null && mp_selected.getID() == masspoint.getID())
					bselected = true;
				
			
				double totradius = masspoint.getRadius();
				if(masspoint.isBlackHole()) {
					if(bselected)
						coObjColor = BLACKHOLE_SELECTED;
					else
						coObjColor = BLACKHOLE;
				}
				else {
					if(bselected)
						coObjColor = STANDARD_SELECTED;
					else
						coObjColor = STANDARD;
				}
				
				double dRadius = MVMath.dtopx(totradius, iZoomLevel, iGridOffset);
				//dRadius /= Math.pow(10.0, iZoomLevel);
				//dRadius *= iGridOffset;///CalcCode.LACCURACY;

				if(dRadius<2) 
					dRadius=2;
				
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
							coString = STRING_BRIGHT;
						else if(hsb_color[2] > hsb_lgray[2]) {
							coString = STRING;
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
								coObjColor = BLACKHOLE_SELECTED;
							} else
								coObjColor = BLACKHOLE;
						}
						else {
							if(masspoint.isHighlighted()) {
								dRadius = 1.5;
								coObjColor = STANDARD_SELECTED;
							}
							else
								coObjColor = STANDARD;
						}
						
						/* Masspoint */
						int mpPosX = 0; /* Masspoint position */
						
						if(cAxes[0] == 'x') {
							mpPosX = (int) MVMath.dtopx( MVMath.ConvertToD(masspoint.getPosX()) + dCoordOffsetX, iZoomLevel, iGridOffset) + centerX - (int) (dRadius);
						} 
						else if (cAxes[0] == 'y') {
							mpPosX = (int) MVMath.dtopx( MVMath.ConvertToD(masspoint.getPosY()) + dCoordOffsetY, iZoomLevel, iGridOffset)  + centerX - (int) (dRadius);
						}
						else {
							mpPosX = (int) MVMath.dtopx( MVMath.ConvertToD(masspoint.getPosZ()) + dCoordOffsetZ, iZoomLevel, iGridOffset)  + centerX - (int) (dRadius);
						}
						
						int mpPosY = 0;
						if(cAxes[1] == 'x') {
							mpPosY = centerY - (int) MVMath.dtopx( MVMath.ConvertToD(masspoint.getPosX()) + dCoordOffsetX, iZoomLevel, iGridOffset) - (int) (dRadius);
						}
						else if (cAxes[1] == 'y') {
							mpPosY = centerY - (int) MVMath.dtopx( MVMath.ConvertToD(masspoint.getPosY()) + dCoordOffsetY, iZoomLevel, iGridOffset) - (int) (dRadius);
						}
						else {
							mpPosY = centerY - (int) MVMath.dtopx( MVMath.ConvertToD(masspoint.getPosZ()) + dCoordOffsetZ, iZoomLevel, iGridOffset) - (int) (dRadius);
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
				coObjColor = BLACKHOLE;
			} else {
				coObjColor = STANDARD;
			}
			
			double dRadius = 5;

			if(dRadius<2) 
				dRadius=2;
			
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

	public int getSize(char axe) {
		int size = -1;
		for(int i=0; i<cAxes.length; i++) {
			if(cAxes[i] == axe)
				size = i==0?getSize().width:getSize().height; 
		}
		
		if(size==-1)
			throw new IndexOutOfBoundsException("checked for "+axe+" on my axes "+String.copyValueOf(cAxes));
		
		return size;
	}
}
