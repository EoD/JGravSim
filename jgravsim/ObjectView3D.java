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
import javax.media.j3d.TransparencyAttributes;

import javax.vecmath.*;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.ImageException;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class ObjectView3D extends ObjectView {

	private static final long serialVersionUID = 1L;

	private BranchGroup bg_main;
	private Canvas3D canvas_main;
	private SimpleUniverse simpleU;
	private TransformGroup[] tg_masspoints;
	private static Texture2D texture_earth;
	private static Texture2D texture_bh;
	
	
	ObjectView3D(Step current) {
		super();

		cAxes = new char[2];
		cAxes[0] = 'x';
		cAxes[1] = 'y';
		
		stCurrent = current;

		setLayout(new BorderLayout());
		setBackground(Color.white);
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas_main = new Canvas3D(config);
		add("Center", canvas_main);

		// SimpleUniverse is a Convenience Utility class
		simpleU = new SimpleUniverse(canvas_main);

		///TEXTURE////
		if(texture_earth == null) {
			String texturefile_earth = "earth.jpg";
			texture_earth = loadTexture(texturefile_earth);
		}
		if(texture_bh == null) {
			String texturefile_bh = "bh.jpg";
			texture_bh = loadTexture(texturefile_bh);
		}
		
		// str_clickme = "";
		//repaint();
	}
	
	public BranchGroup createSceneGraph(SimpleUniverse su) {
		debugout("createSceneGraph() - "+su.toString());
		
		// Create the root of the branch graph
		BranchGroup objRoot = new BranchGroup();
		objRoot.setCapability( BranchGroup.ALLOW_DETACH);
		TransformGroup vpTrans = null;
		BoundingSphere mouseBounds = null;

        Transform3D T3D = new Transform3D();
		vpTrans = su.getViewingPlatform().getViewPlatformTransform();
		vpTrans.getTransform(T3D);

		
		///TRANSFORMATION I
		TransformGroup objRotate = new TransformGroup();
		objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		///TRANSOFMRATION II - TRANSLATION
		if (stCurrent != null && stCurrent.getMasspoints() != null && stCurrent.getMasspoints().length > 0) {
			Masspoint_Sim[] masspoints = stCurrent.getMasspoints();
			int num_masspoints = masspoints.length;
			tg_masspoints = new TransformGroup[num_masspoints];
			
			for (int i = 0; i < num_masspoints; i++) {
				Masspoint_Sim masspoint = masspoints[i];
				tg_masspoints[i] = new TransformGroup();
				//tg_masspoints[i].setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
				tg_masspoints[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
				
				if(masspoint == null)
					continue;

				double radius = masspoint.getRadius();
				if(radius/CONVERT3D < 0.05f)
					radius = 0.05f*CONVERT3D;
				
				Appearance appear = new Appearance();
				appear.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
				if(masspoint.isBlackHole())
					appear.setTexture(texture_bh);
				else
					appear.setTexture(texture_earth);
				
				TransparencyAttributes transparency = new TransparencyAttributes(TransparencyAttributes.FASTEST,0.0f);
				transparency.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
				appear.setTransparencyAttributes(transparency);
				
				//TransformGroup objTranslationSub = new TransformGroup();
				Transform3D TranslationSub = new Transform3D();

				Vector3d vector = postovector3d(masspoint);
				//Controller.debugout("createSceneGraph() - masspoint.getPosX()/CalcCode.LACCURACY/CONVERT3D/iZoomLevel="+masspoint.getPosX()/CalcCode.LACCURACY/CONVERT3D/iZoomLevel);
				
				TranslationSub.setTranslation(vector);
				tg_masspoints[i].setTransform(TranslationSub);
				objRotate.addChild(tg_masspoints[i]);
				
				Sphere sphere = new Sphere((float)(radius/CONVERT3D/iZoomLevel),Primitive.GENERATE_TEXTURE_COORDS, appear);
				//sphere.setCapability( BranchGroup.ALLOW_DETACH);
				
				//objTranslationSub.setTransform(TranslationSub);
				//objTranslationSub.addChild(sphere);		    
				sphere.setAppearance(appear);
				tg_masspoints[i].addChild(sphere);
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
        translate.set( 0.0f, 0.03f, 0.0f);
        Transform3D translation = new Transform3D();
        translation.setTranslation(translate);
        
        T3D.mul(translation);
      
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
		if(tg_masspoints == null) {
			bg_main = createSceneGraph(simpleU);
			simpleU.getViewingPlatform().setNominalViewingTransform();
			simpleU.addBranchGraph(bg_main);
		}
		else if (stCurrent != null && stCurrent.getMasspoints() != null && stCurrent.getMasspoints().length > 0) {
			Masspoint_Sim[] masspoints = stCurrent.getMasspoints();
			for(int i=0; i<masspoints.length && i<tg_masspoints.length; i++) {
				Transform3D translation = new Transform3D();
				translation.setTranslation( postovector3d(masspoints[i]) );
				tg_masspoints[i].setTransform(translation);
				makempvisibile(tg_masspoints[i], true);
			}
			//If we have more transform groups than masspoints, hide all of them
			if(tg_masspoints.length > masspoints.length) {
				for(int i=masspoints.length; i<tg_masspoints.length; i++) {
					makempvisibile(tg_masspoints[i], false);
				}
			}
			//FIXME The following case should be fixed
			else if(tg_masspoints.length < masspoints.length) {
				debugout("updateSceneGraph() - FIXME: tg_masspoints.length < masspoints.length");
			}
		}
	}

	@Override
	void displayStep(Step next) {
		stCurrent = next;
		if(stCurrent == null) {
			tg_masspoints = null;
			simpleU.getLocale().removeBranchGraph(bg_main);
		}
		else {
			stCurrent.sort();
			updateSceneGraph();
		}
	}

	private Vector3d postovector3d(Masspoint_Sim masspoint) {
		Vector3d vector = new Vector3d(
				MVMath.ConvertToD(masspoint.getPosX())/CONVERT3D/iZoomLevel, 
				MVMath.ConvertToD(masspoint.getPosY())/CONVERT3D/iZoomLevel,  
				MVMath.ConvertToD(masspoint.getPosZ())/CONVERT3D/iZoomLevel);
		
		return vector;
	}
	
	private void makempvisibile(TransformGroup tg_masspoint, boolean visible) {
		//There is only one child at the moment
		Sphere sphere = (Sphere) tg_masspoint.getChild(0);
		Appearance appear = sphere.getAppearance();
		TransparencyAttributes transparency = appear.getTransparencyAttributes();
		if(visible) {
			//FIXME: set texture according to object state and NOT manually
			if(transparency.getTransparency() > 0.0f)				
				appear.setTexture(texture_earth);
			transparency.setTransparency(0.0f);
		}
		else {
			if(transparency.getTransparency() < 1.0f)
				appear.setTexture(null);
			transparency.setTransparency(1.0f);
		}
	}
	
	private Texture2D loadTexture(String texture_file) {
		Texture2D texture = null;
		try {
			TextureLoader texture_loader = new TextureLoader(texture_file, this);
			ImageComponent2D texture_image = texture_loader.getImage();
			if (texture_image == null) {
				debugout("Loading failed for texture "+texture_file);
			}
			
			texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, texture_image.getWidth(), texture_image.getHeight());
			texture.setImage(0, texture_image);
		}
		catch(ImageException e) {
			debugout("loadtexture() - Loading of texture "+texture_file+" failed: " + e.toString());
			return null;
		}
		return texture;
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
