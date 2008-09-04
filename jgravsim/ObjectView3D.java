package jgravsim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import javax.swing.JPanel;
import javax.vecmath.*;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class ObjectView3D extends JPanel {

	private static final long serialVersionUID = 1L;
	public static final double LIGHTSPEED = CalcCode.LIGHTSPEED; // Lightspeed
	// in m/s
	public static final Color BLACKHOLE = Color.BLACK;
	public static final Color BLACKHOLE_SELECTED = new Color(0, 0, 128); // NavyBlue
	public static final Color STANDARD = Color.DARK_GRAY;
	public static final Color STANDARD_SELECTED = Color.RED; // new
	public static final float CONVERT3D = 2.0E7f; // new
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

		// SimpleUniverse is a Convenience Utility class
		simpleU = new SimpleUniverse(canvas_main);

		bg_main = createSceneGraph(simpleU);

		// This will move the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		simpleU.getViewingPlatform().setNominalViewingTransform();
		simpleU.addBranchGraph(bg_main);
		// str_clickme = "";
		//repaint();
	}
	public BranchGroup createSceneGraph(SimpleUniverse su) {
		// Create the root of the branch graph
		BranchGroup objRoot = new BranchGroup();
		objRoot.setCapability( BranchGroup.ALLOW_DETACH);
		TransformGroup vpTrans = null;
		BoundingSphere mouseBounds = null;

		vpTrans = su.getViewingPlatform().getViewPlatformTransform();
		
		Appearance appear = new Appearance();

		///TEXTURE////
		String filename = "earth.jpg";
		TextureLoader loader = new TextureLoader(filename, this);
		ImageComponent2D image = loader.getImage();

		if (image == null) {
			System.out.println("load failed for texture: " + filename);
		}

		// can't use parameterless constuctor
		Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
		texture.setImage(0, image);

		appear.setTexture(texture);
		
		
		
		///TRANSFORMATION I
		TransformGroup objRotate = new TransformGroup();
		objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		///TRANSOFMRATION II - TRANSLATION
		if (stCurrent != null && stCurrent.getMasspoints() != null && stCurrent.getMasspoints().length > 0) {
			Masspoint_Sim[] masspoints = stCurrent.getMasspoints();
			for (int i = 0; i < masspoints.length; i++) {
				Masspoint_Sim masspoint = masspoints[i];
				
				if(masspoint == null)
					continue;
				
				//TransformGroup objTranslationSub = new TransformGroup();
				Transform3D TranslationSub = new Transform3D();

				Vector3d vector = new Vector3d(
						masspoint.getPosX()/CalcCode.LACCURACY/CONVERT3D/iZoomLevel, 
						masspoint.getPosY()/CalcCode.LACCURACY/CONVERT3D/iZoomLevel,  
						masspoint.getPosZ()/CalcCode.LACCURACY/CONVERT3D/iZoomLevel);
				
				//Controller.debugout("createSceneGraph() - masspoint.getPosX()/CalcCode.LACCURACY/CONVERT3D/iZoomLevel="+masspoint.getPosX()/CalcCode.LACCURACY/CONVERT3D/iZoomLevel);
				
				TranslationSub.setTranslation(vector);
			    TransformGroup yoyoTGT1 = new TransformGroup(TranslationSub);
			    objRotate.addChild(yoyoTGT1);
				
				Sphere sphere = new Sphere((float)(masspoint.getRadius()/CONVERT3D/iZoomLevel),Primitive.GENERATE_TEXTURE_COORDS, appear);
				sphere.setCapability( BranchGroup.ALLOW_DETACH);
				
				//objTranslationSub.setTransform(TranslationSub);
				//objTranslationSub.addChild(sphere);		    
				sphere.setAppearance(appear);
			    yoyoTGT1.addChild(sphere);
			}
		}
	    ///TRANSFORMATION III - ROTATION

	    //bjRotate.addChild(translate);
		//objRotate.addChild(new Sphere(sphere_size, Primitive.GENERATE_TEXTURE_COORDS, appear));
		
		objRotate.addChild(new Axis());

		mouseBounds = new BoundingSphere(new Point3d(0,0,0), 1000.0);
		
		objRoot.addChild(objRotate);		
		
		MouseRotate myMouseRotate = new MouseRotate(MouseBehavior.INVERT_INPUT);
		myMouseRotate.setTransformGroup(objRotate);
		//myMouseRotate.set
		myMouseRotate.setSchedulingBounds(mouseBounds);
		myMouseRotate.setBoundsAutoCompute(true);
		objRoot.addChild(myMouseRotate);

		MouseTranslate myMouseTranslate = new MouseTranslate(
				MouseBehavior.INVERT_INPUT);
		myMouseTranslate.setTransformGroup(vpTrans);
		myMouseTranslate.setSchedulingBounds(mouseBounds);
		objRoot.addChild(myMouseTranslate);

		MouseZoom myMouseZoom = new MouseZoom(MouseBehavior.INVERT_INPUT);
		myMouseZoom.setTransformGroup(vpTrans);
		myMouseZoom.setSchedulingBounds(mouseBounds);
		objRoot.addChild(myMouseZoom);

		
        Vector3f translate = new Vector3f();
        Transform3D T3D = new Transform3D();
        translate.set( 0.0f, 0.03f, 0.0f);
        T3D.setTranslation(translate);
        vpTrans.setTransform(T3D);
        KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(vpTrans);
        keyNavBeh.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
        objRoot.addChild(keyNavBeh);
		
		// Let Java 3D perform optimizations on this scene graph.
		objRoot.compile();

		return objRoot;
	}
	
	
	
	public void updateSceneGraph() {
		BranchGroup bg_new = createSceneGraph(simpleU);
		//bg_main.compile();
		simpleU.getLocale().replaceBranchGraph(bg_main, bg_new);
		bg_main = bg_new;
		simpleU.getViewingPlatform().setNominalViewingTransform();

	}
	
	
	
	void drawGridPoint(Graphics g, int offsetX, int offsetY) {
		g.drawLine(offsetX, offsetY - 2, offsetX, offsetY + 2);
		g.drawLine(offsetX - 2, offsetY, offsetX + 2, offsetY);
	}

	void displayStep(Step next) {
		stCurrent = next;
		//repaint();
		updateSceneGraph();
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
