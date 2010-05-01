package jgravsim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.event.MouseWheelListener;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import javax.vecmath.*;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class ObjectView3D extends ObjectView {

	private static final long serialVersionUID = 1L;

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
		

		///TEXTURE////
		String texturefile_earth = "earth.jpg";
		String texturefile_bh = "bh.jpg";
		TextureLoader loader_earth = new TextureLoader(texturefile_earth, this);
		TextureLoader loader_bh = new TextureLoader(texturefile_bh, this);
		ImageComponent2D image_earth = loader_earth.getImage();
		ImageComponent2D image_bh = loader_bh.getImage();

		if (image_earth == null || image_bh == null) {
			System.out.println("load failed for texture: " + texturefile_earth);
		}

		// can't use parameterless constuctor
		Texture2D texture_earth = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image_earth.getWidth(), image_earth.getHeight());
		texture_earth.setImage(0, image_earth);
		
		Texture2D texture_bh = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image_bh.getWidth(), image_bh.getHeight());
		texture_bh.setImage(0, image_bh);
		
		
		
		
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

				Appearance appear = new Appearance();
				double radius = masspoint.getRadius();
				if(radius/CONVERT3D < 0.05f)
					radius = 0.05f*CONVERT3D;
				
				if(masspoint.isBlackHole()) {
					//Controller.debugout("Object3DView - Blackhole");
					appear.setTexture(texture_bh);
				}
				else {
					//Controller.debugout("Object3DView - Earth");
					appear.setTexture(texture_earth);	
				}
				
				//TransformGroup objTranslationSub = new TransformGroup();
				Transform3D TranslationSub = new Transform3D();

				Vector3d vector = new Vector3d(
						MVMath.ConvertToD(masspoint.getPosX())/CONVERT3D/iZoomLevel, 
						MVMath.ConvertToD(masspoint.getPosY())/CONVERT3D/iZoomLevel,  
						MVMath.ConvertToD(masspoint.getPosZ())/CONVERT3D/iZoomLevel);
				
				//Controller.debugout("createSceneGraph() - masspoint.getPosX()/CalcCode.LACCURACY/CONVERT3D/iZoomLevel="+masspoint.getPosX()/CalcCode.LACCURACY/CONVERT3D/iZoomLevel);
				
				TranslationSub.setTranslation(vector);
				TransformGroup yoyoTGT1 = new TransformGroup(TranslationSub);
				objRotate.addChild(yoyoTGT1);
				
				Sphere sphere = new Sphere((float)(radius/CONVERT3D/iZoomLevel),Primitive.GENERATE_TEXTURE_COORDS, appear);
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

		MouseTranslate myMouseTranslate = new MouseTranslate(MouseBehavior.INVERT_INPUT);
		myMouseTranslate.setTransformGroup(vpTrans);
		myMouseTranslate.setSchedulingBounds(mouseBounds);
		objRoot.addChild(myMouseTranslate);

		MouseWheelZoom myMouseZoom = new MouseWheelZoom();
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
	
	@Override
	public synchronized void addMouseWheelListener(MouseWheelListener l) {
	}
	
	public void updateSceneGraph() {
		BranchGroup bg_new = createSceneGraph(simpleU);
		//bg_main.compile();
		simpleU.getLocale().replaceBranchGraph(bg_main, bg_new);
		bg_main = bg_new;
		simpleU.getViewingPlatform().setNominalViewingTransform();

	}

	@Override
	void displayStep(Step next) {
		stCurrent = next;
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
}
