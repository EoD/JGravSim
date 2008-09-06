package jgravsim;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;
import javax.swing.JPanel;

public class ObjectView2D extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final double LIGHTSPEED = CalcCode.LIGHTSPEED; 	//Lightspeed in m/s
	public static final Color BLACKHOLE = Color.BLACK;
	public static final Color BLACKHOLE_SELECTED = new Color(0,0,128);	//NavyBlue
	public static final Color STANDARD = Color.DARK_GRAY;
	public static final Color STANDARD_SELECTED = Color.RED; //new Color(34,139,34);	//ForestGreen
	public static final Color HIDDEN = Color.LIGHT_GRAY;	//gainsboro
	public static final Color SPEEDVEC = Color.RED;
	public static final Color STRING = Color.BLACK;
	public static final Color STRING_BRIGHT = new Color(220,220,220);	//gainsboro
	public static final Color CLICKME = Color.ORANGE;	//gainsboro
	public static final int speedvecmax = 55;	//gainsboro
	int iLastMouseX = 0;
	int iLastMouseY = 0;
	
	int iGridOffset = 25; /* 25px = 1*Unit */
	double iZoomLevel = 12.0; /* pos / 10^zoomLevel */
	boolean draw_strings = true;
	boolean draw_speed =  true;
	boolean boxed = true;
	Masspoint mp_selected = null;
	
	Color coGridColor = Color.decode("#DDDDDD");
	Color coSpeedvecColor = SPEEDVEC;
	Color coObjColor = STANDARD;
	char [] cAxes; /* which axis to draw */
	private double dCoordOffsetX = 0.0;
	private double dCoordOffsetY = 0.0;
	private double dCoordOffsetZ = 0.0;
	Step[] alldots;
	String str_clickme;
		
	private Step stCurrent = null;
	
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
				if(mp_selected != null && mp_selected.id == masspoint.getID())
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
				
				double dRadius = MVMath.mtopx(totradius, this);
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
				System.out.println("cAxes="+String.copyValueOf(cAxes));
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
	
	private double getVectorLength(double x, double y, double z) {
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
		int string_width = 10;
		int string_height = 20;
		int string_size = string_width;
		int string_size2 = string_width;
		if(!boxed) {
			string_size = 5;
			string_size2 = 5;
			string_width = 7;
			string_height = 7;
		}
		g.drawString(""+cAxes[0], this.getWidth()-string_width, centerY+string_size);
		g.drawString(""+cAxes[1], centerX-string_size2, string_height);
	}
	
	void drawGridPoint(Graphics g, int offsetX, int offsetY) {
		g.drawLine(offsetX, offsetY-2, offsetX, offsetY+2);
		g.drawLine(offsetX-2, offsetY, offsetX+2, offsetY);
	}
	
	void displayStep(Step next) {
		stCurrent = next;
		repaint();
	}	
	
	void drawClickmeOverlay(Graphics g, int centerX, int centerY) {
        //int size = 48;
		Font arialFont = new Font("Arial", Font.ITALIC, 22);

		AttributedString as_clickme = new AttributedString(str_clickme); 
		as_clickme.addAttribute(TextAttribute.FONT, arialFont);
	    as_clickme.addAttribute(TextAttribute.FOREGROUND, Color.red);  

        Graphics2D g2;
        g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        FontRenderContext frc = g2.getFontRenderContext();
    	TextLayout tl = new TextLayout(str_clickme, arialFont, frc);
        float fwidth = (float)tl.getBounds().getWidth();
        float fheight = (float) tl.getBounds().getHeight();
        
       /* while(fwidth+20 > this.getWidth()) {
        	size--;
        	arialFont = new Font("Arial", Font.ITALIC, size);
        	frc = g2.getFontRenderContext();
        	tl = new TextLayout(str_clickme, arialFont, frc);
        	fwidth = (float) tl.getBounds().getWidth();
        }
        System.out.println("drawClickmeOverlay() - size="+size);
        */
        
        g2.drawString(as_clickme.getIterator(), centerX-fwidth/2, centerY+fheight/2);
	}

	public void setCoordOffset(double x1, double x2, double x3) {
		dCoordOffsetX = x1;
		dCoordOffsetY = x2;
		dCoordOffsetZ = x3;
	}
	public void setCoordOffset(double[] offset) {
		if (offset.length != 3)
			return;
		setCoordOffset(offset[0], offset[1], offset[2]);
	}
	public void setCoordOffset(MDVector offset) {
		setCoordOffset(offset.x1, offset.x2, offset.x3);
	}
	public void resetCoordOffset() {
		setCoordOffset(0.0, 0.0, 0.0);
	}
	public void addCoordOffsetX(double x1) {
		setCoordOffset(dCoordOffsetX+x1, dCoordOffsetY, dCoordOffsetZ);
	}
	public void addCoordOffsetY(double x2) {
		setCoordOffset(dCoordOffsetX, dCoordOffsetY+x2, dCoordOffsetZ);
	}
	public void addCoordOffsetZ(double x3) {
		setCoordOffset(dCoordOffsetX, dCoordOffsetY, dCoordOffsetZ+x3);
	}
	public double getCoordOffsetX() {
		return dCoordOffsetX;
	}
	public double getCoordOffsetY() {
		return dCoordOffsetY;
	}
	public double getCoordOffsetZ() {
		return dCoordOffsetZ;
	}
	
	public void resetAllSteps() {
		alldots = null;
	}
	public void resetCurrentStep() {
		stCurrent = null;
	}
	public Step getCurrentStep() {
		return stCurrent;
	}
	
	public void setBoxed(boolean b){
		boxed = b;
	}
}
