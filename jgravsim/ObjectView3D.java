package jgravsim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;

import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.*;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.SimpleUniverse;

import com.sun.j3d.utils.universe.SimpleUniverse;

public class ObjectView3D extends JPanel {

	private static final long serialVersionUID = 1L;
	public static final double LIGHTSPEED = CalcCode.LIGHTSPEED; // Lightspeed
	// in m/s
	public static final Color BLACKHOLE = Color.BLACK;
	public static final Color BLACKHOLE_SELECTED = new Color(0, 0, 128); // NavyBlue
	public static final Color STANDARD = Color.DARK_GRAY;
	public static final Color STANDARD_SELECTED = Color.RED; // new
	// Color(34,139,34);
	// ==ForestGreen
	public static final Color HIDDEN = Color.LIGHT_GRAY; // gainsboro
	public static final Color SPEEDVEC = Color.RED;
	public static final Color STRING = Color.BLACK;
	public static final Color STRING_BRIGHT = new Color(220, 220, 220); // gainsboro
	public static final Color CLICKME = Color.ORANGE; // gainsboro
	public static final int speedvecmax = 55; // gainsboro
	int iLastMouseX = 0;
	int iLastMouseY = 0;

	int iGridOffset = 25; /* 25px = 1*Unit */
	double iZoomLevel = 12.0; /* pos / 10^zoomLevel */
	boolean draw_strings = true;
	boolean draw_speed = true;
	boolean boxed = true;
	Masspoint mp_selected = null;

	Color coGridColor = Color.decode("#DDDDDD");
	Color coSpeedvecColor = SPEEDVEC;
	Color coObjColor = STANDARD;
	double dCoordOffsetX = 0.0;
	double dCoordOffsetY = 0.0;
	double dCoordOffsetZ = 0.0;
	Step[] alldots;
	char[] cAxes;
	String str_clickme;

	private Step stCurrent = null;
	
	private BranchGroup bg_main;
	private Canvas3D canvas_main;
	private SimpleUniverse simpleU;
	
	ObjectView3D(Step current) {
		super();
		
		cAxes = new char[2];
		cAxes[0] = 'x';
		cAxes[1] = 'y';
		
		stCurrent = current;

		setLayout(new BorderLayout());
		setBackground(Color.white);
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();
		canvas_main = new Canvas3D(config);
		add("Center", canvas_main);
		bg_main = createSceneGraph();
		bg_main.compile();

		// SimpleUniverse is a Convenience Utility class
		simpleU = new SimpleUniverse(canvas_main);

		// This moves the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		simpleU.getViewingPlatform().setNominalViewingTransform();
		simpleU.addBranchGraph(bg_main);
		// str_clickme = "";
		//repaint();
	}
	public void updateSceneGraph() {
		remove(canvas_main);
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas_main = new Canvas3D(config);
		
		bg_main = createSceneGraph();
		bg_main.compile();

		// SimpleUniverse is a Convenience Utility class
		//simpleU = new SimpleUniverse(canvas_main);

		// This moves the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		simpleU.getViewingPlatform().setNominalViewingTransform();
		simpleU.addBranchGraph(bg_main);
	}
	
	public void repaint(Graphics g) {
		//GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		//canvas_main = new Canvas3D(config);
		//add("Center", canvas_main);
		//if(canvas_main != null) {
		//	bg_main = createSceneGraph();
		//	bg_main.compile();
			//canvas_main.
		//	canvas_main.update(g);
		//}
	}
	
	public BranchGroup createSceneGraph() {
		// Create the root of the branch graph
		BranchGroup objRoot = new BranchGroup();

		// rotate object has composite transformation matrix
		Transform3D trf_rotateX = new Transform3D();
		Transform3D trf_rotateY = new Transform3D();
		//Transform3D trf_rotateZ = new Transform3D();

		
		trf_rotateX.rotX(Math.PI / 4.0d);
		trf_rotateY.rotY(Math.PI / 5.0d);
		//trf_rotateZ.rotZ(Math.PI / 2.0d);
		trf_rotateX.mul(trf_rotateY);
		//trf_rotateX.mul(trf_rotateZ);

		TransformGroup objRotate = new TransformGroup(trf_rotateX);
		objRoot.addChild(objRotate);

		// Create the transform group node and initialize it to the
		// identity. Enable the TRANSFORM_WRITE capability so that
		// our behavior code can modify it at runtime. Add it to the
		// root of the subgraph.
		TransformGroup objSpin = new TransformGroup();
		objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		objRotate.addChild(objSpin);

		// Create a simple shape leaf node, add it to the scene graph.
		// ColorCube is a Convenience Utility class

		if (stCurrent != null && stCurrent.getMasspoints() != null && stCurrent.getMasspoints().length > 0) {
			Masspoint_Sim[] masspoints = stCurrent.getMasspoints();
			for (int i = 0; i < masspoints.length; i++) {
				Masspoint_Sim masspoint = masspoints[i];
				
				if(masspoint == null)
					continue;
				
				TransformGroup objTranslationSub = new TransformGroup();
				Transform3D TranslationSub = new Transform3D();
				Sphere sphere = new Sphere((float)(masspoint.getRadius()/1000.0/iZoomLevel));

				Vector3d vector = new Vector3d(
						masspoint.getPosX()/CalcCode.LACCURACY/1000.0/iZoomLevel, 
						masspoint.getPosY()/CalcCode.LACCURACY/1000.0/iZoomLevel,  
						masspoint.getPosZ()/CalcCode.LACCURACY/1000.0/iZoomLevel);
				
				TranslationSub.setTranslation(vector);
				objTranslationSub.setTransform(TranslationSub);
				objTranslationSub.addChild(sphere);
				objSpin.addChild(objTranslationSub);
			}
		}
		
		TransformGroup objTranslationSub = new TransformGroup();
		Transform3D TranslationSub = new Transform3D();
		Sphere sphere = new Sphere(1.0f/(float)iZoomLevel);

		Vector3d vectorx = new Vector3d(1.0/iZoomLevel,0.0,0.0);
		
		TranslationSub.setTranslation(vectorx);
		objTranslationSub.setTransform(TranslationSub);
		objTranslationSub.addChild(sphere); 
		objSpin.addChild(objTranslationSub);
		
		// Create a new Behavior object that performs the desired
		// operation on the specified transform object and add it into
		// the scene graph.
		Transform3D yAxis = new Transform3D();
		Alpha rotationAlpha = new Alpha(-1, 4000);

		RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, objSpin, yAxis, 0.0f,
				(float) Math.PI * 2.0f);

		// create a sphere centered at the origin with radius of 1
		Color3f light1Color = new Color3f(1.8f, 0.1f, 0.1f);
		Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);

		// a bounding sphere specifies a region a behavior is active
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100.0);

		DirectionalLight light1 = new DirectionalLight(light1Color,
				light1Direction);

		light1.setInfluencingBounds(bounds);
		objRoot.addChild(light1);

		rotator.setSchedulingBounds(bounds);
		objSpin.addChild(rotator);
		
		return objRoot;
	} // end of createSceneGraph method of HelloJava3Dd
	
	protected void paintComponent(Graphics g) {
		super.paintComponents(g);
		repaint(g);
		/*		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;
		// drawGrid(g, centerX, centerY);
		// if (str_clickme != null && !str_clickme.isEmpty() && str_clickme !=
		// "")
		// drawClickmeOverlay(g, centerX, centerY);

		if (stCurrent != null && stCurrent.getMasspoints() != null
				&& stCurrent.getMasspoints().length > 0) {
			Masspoint_Sim[] masspoints = stCurrent.getMasspoints();
			for (int i = 0; i < masspoints.length; i++) {
				Masspoint_Sim masspoint = masspoints[i];

				// Speedvector
				double mpSpeed = getVectorLength(masspoint.getSpeedX(),
						masspoint.getSpeedY(), masspoint.getSpeedZ());
				double factor = ((speedvecmax * mpSpeed / LIGHTSPEED) + 5);

				boolean bselected = false;
				if (mp_selected != null && mp_selected.id == masspoint.getID())
					bselected = true;

				double totradius;
				if (masspoint.isBlackHole()) {
					totradius = masspoint.getSchwarzschildRadius();
					if (bselected)
						coObjColor = BLACKHOLE_SELECTED;
					else
						coObjColor = BLACKHOLE;
				} else {
					totradius = masspoint.getRadius();
					if (bselected)
						coObjColor = STANDARD_SELECTED;
					else
						coObjColor = STANDARD;
				}

				double dRadius = MVMath.mtopx(totradius, this);
				// dRadius /= Math.pow(10.0, iZoomLevel);
				// dRadius *= iGridOffset;///CalcCode.LACCURACY;

				if (dRadius < 2)
					dRadius = 2;

				// Masspoint
				double[] mppos = MVMath.coordtopx(masspoint.getPos(), this);
				// Masspoint position
				int mpPosX = (int) (mppos[0]);
				int mpPosY = (int) (mppos[1]);
				int mpPosZ = (int) (mppos[2]);

				// Speedvector position
				int svPosX = (int) ((masspoint.getSpeedX() / mpSpeed) * factor)
						+ mpPosX;
				int svPosY = (int) ((masspoint.getSpeedY() / mpSpeed) * factor)
						+ mpPosY;
				int svPosZ = (int) ((masspoint.getSpeedZ() / mpSpeed) * factor)
						+ mpPosZ;

				// if (mpPosX + 2.0 * dRadius > 0 && mpPosY + 2.0 * dRadius > 0
				// && mpPosX < this.getWidth()
				// && mpPosY < this.getHeight()) {

				// if(2.0*dRadius > this.getWidth() && 2.0*dRadius >
				// this.getHeight())
				// coObjColor = HIDDEN;

				// g.setColor(coObjColor);
				// g.fillArc((int) (mpPosX), (int) (mpPosY),
				// (int) (dRadius * 2.0), (int) (dRadius * 2.0), 0,
				// 360);
				// }

				/*
				 * if (draw_speed) { g.setColor(coSpeedvecColor);
				 * g.drawLine(mpPosX + (int) dRadius, mpPosY + (int) dRadius,
				 * svPosX + (int) dRadius, svPosY + (int) dRadius); } if
				 * (draw_strings) { Color coString = coObjColor; int
				 * string_distance = 7;
				 * 
				 * if (dRadius > 5 + string_distance) {// integer is written //
				 * inside the radius // you can't raise the brightness of black
				 * or white float[] hsb_color =
				 * Color.RGBtoHSB(coObjColor.getRed(), coObjColor.getGreen(),
				 * coObjColor.getBlue(), null); float[] hsb_lgray =
				 * Color.RGBtoHSB(Color.LIGHT_GRAY .getRed(),
				 * Color.LIGHT_GRAY.getGreen(), Color.LIGHT_GRAY.getBlue(),
				 * null);
				 * 
				 * if (coObjColor == Color.BLACK || hsb_color[2] <=
				 * hsb_lgray[2]) coString = STRING_BRIGHT; else if (hsb_color[2] >
				 * hsb_lgray[2]) { coString = STRING; } else coString =
				 * Color.RED; }
				 * 
				 * g.setColor(coString); // center the string to the circle and
				 * move it // string_distance pixel away from center
				 * g.drawString(Integer.toString(masspoint.getID()), mpPosX +
				 * (int) dRadius + string_distance, mpPosY + (int) dRadius + (5 +
				 * string_distance)); }
				 */
			//}
		/*}

		
		 * if (alldots != null && alldots.length > 0) { for (int j = 0; j <
		 * alldots.length; j++) { if (alldots[j] != null &&
		 * alldots[j].getMasspoints() != null &&
		 * alldots[j].getMasspoints().length > 0) { Masspoint_Sim[] masspoints =
		 * alldots[j].getMasspoints(); for (int i = 0; i < masspoints.length;
		 * i++) { Masspoint_Sim masspoint = masspoints[i];
		 * 
		 * if (masspoint.isInvisible()) continue;
		 * 
		 * double dRadius = 1.0;
		 * 
		 * if (masspoint.isBlackHole()) { if (masspoint.isHighlighted()) {
		 * dRadius = 1.5; coObjColor = BLACKHOLE_SELECTED; } else coObjColor =
		 * BLACKHOLE; } else { if (masspoint.isHighlighted()) { dRadius = 1.5;
		 * coObjColor = STANDARD_SELECTED; } else coObjColor = STANDARD; }
		 * 
		 * //* Masspoint int mpPosX = 0; // Masspoint position
		 * 
		 * if (cAxes[0] == 'x') { mpPosX = (int) (((masspoint.getPosX() /
		 * CalcCode.LACCURACY + dCoordOffsetX) / Math .pow(10, iZoomLevel)) *
		 * iGridOffset) + centerX - (int) (dRadius); } else if (cAxes[0] == 'y') {
		 * mpPosX = (int) (((masspoint.getPosY() / CalcCode.LACCURACY +
		 * dCoordOffsetY) / Math .pow(10, iZoomLevel)) * iGridOffset) + centerX -
		 * (int) (dRadius); } else { mpPosX = (int) (((masspoint.getPosZ() /
		 * CalcCode.LACCURACY + dCoordOffsetZ) / Math .pow(10, iZoomLevel)) *
		 * iGridOffset) + centerX - (int) (dRadius); }
		 * 
		 * int mpPosY = 0; if (cAxes[1] == 'x') { mpPosY = centerY - (int)
		 * (((masspoint.getPosX() / CalcCode.LACCURACY + dCoordOffsetX) / Math
		 * .pow(10, iZoomLevel)) * iGridOffset) - (int) (dRadius); } else if
		 * (cAxes[1] == 'y') { mpPosY = centerY - (int) (((masspoint.getPosY() /
		 * CalcCode.LACCURACY + dCoordOffsetY) / Math .pow(10, iZoomLevel)) *
		 * iGridOffset) - (int) (dRadius); } else { mpPosY = centerY - (int)
		 * (((masspoint.getPosZ() / CalcCode.LACCURACY + dCoordOffsetZ) / Math
		 * .pow(10, iZoomLevel)) * iGridOffset) - (int) (dRadius); }
		 * 
		 * g.setColor(coObjColor); g.fillArc(mpPosX, mpPosY, (int) (dRadius *
		 * 2.0), (int) (dRadius * 2.0), 0, 360); } } } }
		 */

		// this is for the speed-vec Frame
		/*
		 * if (mp_selected != null && alldots == null && stCurrent == null) {
		 * Masspoint_Sim masspoint = mp_selected.toMasspoint_Sim();
		 *  // Speedvector if (masspoint.isSpeedVecNull()) {
		 * masspoint.setSpeedVecNotNull(); } double factor = 30;
		 * 
		 * if (masspoint.isBlackHole()) { coObjColor = BLACKHOLE; } else {
		 * coObjColor = STANDARD; }
		 * 
		 * double dRadius = 5;
		 * 
		 * if (dRadius < 2) dRadius = 2;
		 *  // Masspoint double[] mppos = MVMath.coordtopx(new MLVector(0, 0,
		 * 0), cAxes, this);
		 * 
		 * int mpPosX = (int) (mppos[0] - dRadius); // Masspoint position int
		 * svPosX = 0; // Speedvector position int mpPosY = (int) (mppos[1] -
		 * dRadius); int svPosY = 0; float[] dSpeedAngles =
		 * mp_selected.getUnitSpeedAngle(); double dSpeedTheta =
		 * Math.toRadians(dSpeedAngles[0]); double dSpeedPhi =
		 * Math.toRadians(dSpeedAngles[1]);
		 * 
		 * if (cAxes[0] == 'x' && cAxes[1] == 'y') { svPosX = (int)
		 * (Math.cos(dSpeedPhi) * factor) + mpPosX + (int) dRadius; svPosY =
		 * mpPosY + (int) dRadius - (int) (Math.sin(dSpeedPhi) * factor); } else
		 * if (cAxes[0] == 'x' && cAxes[1] == 'z') { svPosX = (int)
		 * (Math.sin(dSpeedTheta) * factor) + mpPosX + (int) dRadius; svPosY =
		 * mpPosY + (int) dRadius - (int) (Math.cos(dSpeedTheta) * factor); }
		 * else { System.out.println("cAxes=" + String.copyValueOf(cAxes));
		 * System.exit(0); } if (mpPosX + 2.0 * dRadius > 0 && mpPosY + 2.0 *
		 * dRadius > 0 && mpPosX < this.getWidth() && mpPosY < this.getHeight()) {
		 *  // if(2.0*dRadius > this.getWidth() && 2.0*dRadius > //
		 * this.getHeight()) // coObjColor = HIDDEN;
		 * 
		 * g.setColor(coObjColor); g.fillArc((int) (mpPosX), (int) (mpPosY),
		 * (int) (dRadius * 2.0), (int) (dRadius * 2.0), 0, 360); }
		 * 
		 * g.setColor(coSpeedvecColor); g.drawLine(mpPosX + (int) dRadius,
		 * mpPosY + (int) dRadius, svPosX, svPosY); }
		 */
	}

	/*
	 * void drawGrid(Graphics g, int centerX, int centerY) {
	 * g.setColor(coGridColor); g.drawLine(centerX, 0, centerX, getHeight());
	 * g.drawLine(0, centerY, getWidth(), centerY);
	 * 
	 * int gridXstart; int gridYstart;
	 * 
	 * for (gridXstart = centerX; gridXstart > 0; gridXstart -= iGridOffset) ;
	 * for (gridYstart = centerY; gridYstart > 0; gridYstart -= iGridOffset) ;
	 * 
	 * for (int offsetX = gridXstart; offsetX < getWidth(); offsetX +=
	 * iGridOffset) { for (int offsetY = gridYstart; offsetY < getHeight();
	 * offsetY += iGridOffset) { drawGridPoint(g, offsetX, offsetY); } }
	 * g.setColor(Color.black); int string_width = 10; int string_height = 20;
	 * int string_size = string_width; int string_size2 = string_width; if
	 * (!boxed) { string_size = 5; string_size2 = 5; string_width = 7;
	 * string_height = 7; } g.drawString("" + cAxes[0], this.getWidth() -
	 * string_width, centerY + string_size); g.drawString("" + cAxes[1], centerX -
	 * string_size2, string_height); }
	 */

	void drawGridPoint(Graphics g, int offsetX, int offsetY) {
		g.drawLine(offsetX, offsetY - 2, offsetX, offsetY + 2);
		g.drawLine(offsetX - 2, offsetY, offsetX + 2, offsetY);
	}

	void displayStep(Step next) {
		stCurrent = next;
		repaint();
		//updateSceneGraph();
	}

	/*
	 * void drawClickmeOverlay(Graphics g, int centerX, int centerY) { // int
	 * size = 48; Font arialFont = new Font("Arial", Font.ITALIC, 22);
	 * 
	 * AttributedString as_clickme = new AttributedString(str_clickme);
	 * as_clickme.addAttribute(TextAttribute.FONT, arialFont);
	 * as_clickme.addAttribute(TextAttribute.FOREGROUND, Color.red);
	 * 
	 * Graphics2D g2; g2 = (Graphics2D) g;
	 * g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	 * RenderingHints.VALUE_ANTIALIAS_ON);
	 * g2.setRenderingHint(RenderingHints.KEY_RENDERING,
	 * RenderingHints.VALUE_RENDER_QUALITY);
	 * 
	 * FontRenderContext frc = g2.getFontRenderContext(); TextLayout tl = new
	 * TextLayout(str_clickme, arialFont, frc); float fwidth = (float)
	 * tl.getBounds().getWidth(); float fheight = (float)
	 * tl.getBounds().getHeight();
	 * 
	 * g2.drawString(as_clickme.getIterator(), centerX - fwidth / 2, centerY +
	 * fheight / 2); }
	 */

	private double getVectorLength(double x, double y, double z) {
		return Math.sqrt(x*x + y*y + z*z);
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
		setCoordOffset(dCoordOffsetZ, dCoordOffsetY, dCoordOffsetZ+x3);
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
	
	public void setBoxed(boolean b) {
		boxed = b;
	}
}
