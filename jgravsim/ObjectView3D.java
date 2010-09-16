package jgravsim;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.ImageException;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class ObjectView3D extends ObjectView {

	private static final long serialVersionUID = 1L;

	private BranchGroup bg_main;
	private Canvas3D canvas_main;
	private SimpleUniverse simpleU;
	private TransformGroup[] tg_masspoints;

	private static final float DEFAULT_RADIUS = 2.0f;
	private static final float CONVERT3D = 2.0E8f;

	private static final float ZOOM_CORRECTION = 0.2f;	/* Offset between Canvas3D and ObjectView2D is about 0.2f (zoom factors) */
	private static Texture2D texture_earth;
	private static Texture2D texture_bh;
	private static Texture2D texture_sun;

	private float fZoomLevel_old;
	private float fGridOffset_old;
	
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
		if(texture_sun == null) {
			String texturefile_sun = "sun.jpg";
			texture_sun = loadTexture(texturefile_sun);
		}
		
		fZoomLevel_old = iZoomLevel;
		fGridOffset_old = iGridOffset;
		// str_clickme = "";
	}
	
	public BranchGroup createSceneGraph(SimpleUniverse su) {
		debugout("createSceneGraph() - "+su.toString());
		
		// Create the root of the branch graph
		BranchGroup bg_root = new BranchGroup();
		bg_root.setCapability(BranchGroup.ALLOW_DETACH);

		///TRANSFORMATION - VP
		TransformGroup tg_vp = su.getViewingPlatform().getViewPlatformTransform();

		///TRANSFORMATION - ROTATE
		TransformGroup tg_rotate = new TransformGroup();
		tg_rotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg_rotate.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		//add coordinate axis
		tg_rotate.addChild(new Axis());
		
		if (stCurrent != null && stCurrent.getMasspoints() != null && stCurrent.getMasspoints().length > 0) {
			///TRANSOFMRATION - SCENE
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
				
				Appearance appear = new Appearance();
				appear.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
				if(masspoint.isBlackHole())
					appear.setTexture(texture_bh);
				else
					appear.setTexture(texture_earth);
				
				TransparencyAttributes transparency = new TransparencyAttributes(TransparencyAttributes.FASTEST,0.0f);
				transparency.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
				appear.setTransparencyAttributes(transparency);
				
				///TRANSOFMRATION I - MP TRANSLATION
				Transform3D t3d_mptrans = new Transform3D();
				Vector3d v3d_mptrans = postovector3d(masspoint);
				//Controller.debugout("createSceneGraph() - masspoint.getPosX()/CalcCode.LACCURACY/CONVERT3D/iZoomLevel="+masspoint.getPosX()/CalcCode.LACCURACY/CONVERT3D/iZoomLevel);
				t3d_mptrans.setTranslation(v3d_mptrans);
				t3d_mptrans.setScale( radiusToScale(masspoint.getRadius()) );
				tg_masspoints[i].setTransform(t3d_mptrans);
				
				tg_rotate.addChild(tg_masspoints[i]);
				
				//finally create the sphere itself
				Sphere sphere = new Sphere(DEFAULT_RADIUS, Primitive.GENERATE_TEXTURE_COORDS, appear);		    
				sphere.setAppearance(appear);
				tg_masspoints[i].addChild(sphere);
			}
		}
		
		// Create BoundingSphere for Mouse
		BoundingSphere mouseBounds = new BoundingSphere(new Point3d(0,0,0), 1000.0);
		
	    ///TRANSFORMATION II - SCENE ROTATION
		MouseRotate myMouseRotate = new MouseRotate(MouseBehavior.INVERT_INPUT);
		myMouseRotate.setTransformGroup(tg_rotate);
		myMouseRotate.setSchedulingBounds(mouseBounds);
		myMouseRotate.setBoundsAutoCompute(true);
		bg_root.addChild(myMouseRotate);
		
		bg_root.addChild(tg_rotate);	

	    ///TRANSFORMATION III - VP TRANSLATION
		MouseTranslate myMouseTranslate = new MouseTranslate(MouseBehavior.INVERT_INPUT);
		myMouseTranslate.setTransformGroup(tg_vp);
		myMouseTranslate.setSchedulingBounds(mouseBounds);
		bg_root.addChild(myMouseTranslate);

		resetCoordOffset3D(tg_vp);

		// Let Java 3D perform optimizations on this scene graph.
		bg_root.compile();

		return bg_root;
	}
	
	public void updateSceneGraph() {
		if(tg_masspoints == null) {
			bg_main = createSceneGraph(simpleU);
			resetZoom();
			simpleU.addBranchGraph(bg_main);
		}
		else if (stCurrent != null && stCurrent.getMasspoints() != null && stCurrent.getMasspoints().length > 0) {
			if(iZoomLevel != fZoomLevel_old || fGridOffset_old != iGridOffset) {
				setZoom3D(simpleU.getViewingPlatform().getViewPlatformTransform(), iZoomLevel-fZoomLevel_old, iGridOffset/fGridOffset_old);
				fZoomLevel_old = iZoomLevel;
				fGridOffset_old = iGridOffset;
			}
			
			Masspoint_Sim[] masspoints = stCurrent.getMasspoints();
			for(int i=0; i<masspoints.length && i<tg_masspoints.length; i++) {
				Transform3D translation = new Transform3D();
				translation.setTranslation( postovector3d(masspoints[i]) );
				translation.setScale( radiusToScale(masspoints[i].getRadius()) );
				tg_masspoints[i].setTransform(translation);
				if(masspoints[i].isBlackHole())
					changeAppearance(tg_masspoints[i], true, texture_bh);
				else if(masspoints[i].isHighlighted())
					changeAppearance(tg_masspoints[i], true, texture_sun);
				else
					changeAppearance(tg_masspoints[i], true);
			}
			//If we have more transform groups than masspoints, hide all of them
			if(tg_masspoints.length > masspoints.length) {
				for(int i=masspoints.length; i<tg_masspoints.length; i++) {
					changeAppearance(tg_masspoints[i], false);
				}
			}
			//FIXME The following case should be fixed
			else if(tg_masspoints.length < masspoints.length) {
				debugout("updateSceneGraph() - FIXME: tg_masspoints.length < masspoints.length");
			}
		}
	}

	/**
	 * Resets zoom default values. Resets fZoomLevel_old/fGridOffset_old, sets
	 * NominalViewingTransform() and zooms accordingly to iZoomLevel and
	 * iGridOffset. 
	 * ZOOM_CORRECTION guarantees that ObjectView2D and ObjectView3D have the 
	 * same level of zoom.
	 */
	public void resetZoom() {
		fZoomLevel_old = iZoomLevel;
		fGridOffset_old = iGridOffset;
		simpleU.getViewingPlatform().setNominalViewingTransform();
		setZoom3D(simpleU.getViewingPlatform().getViewPlatformTransform(), iZoomLevel-View.ZOOM_DEFAULT + ObjectView3D.ZOOM_CORRECTION, iGridOffset/View.GRID_DEFAULT);
	}

	@Override
	void displayStep(Step next) {
		stCurrent = next;
		if(stCurrent == null) {
			tg_masspoints = null;
			if(bg_main != null)
				simpleU.getLocale().removeBranchGraph(bg_main);
		}
		else {
			stCurrent.sort();
			updateSceneGraph();
		}
	}
	
	@Override
	public void repaint() {
		if(stCurrent != null) {
			updateSceneGraph();
		}
	}

	private Vector3d postovector3d(Masspoint_Sim masspoint) {
		Vector3d vector = new Vector3d(
				MVMath.ConvertToD(masspoint.getPosX())/CONVERT3D, 
				MVMath.ConvertToD(masspoint.getPosY())/CONVERT3D,  
				MVMath.ConvertToD(masspoint.getPosZ())/CONVERT3D);
		
		return vector;
	}

	private void changeAppearance(TransformGroup tg_masspoint, boolean visible) {
		if(visible == false)
			changeAppearance(tg_masspoint, false, null);
		else
			changeAppearance(tg_masspoint, true, texture_earth);
	}
	
	private void changeAppearance(TransformGroup tg_masspoint, boolean visible, Texture2D texture) {
		//There is only one child at the moment
		Sphere sphere = (Sphere) tg_masspoint.getChild(0);
		Appearance appear = sphere.getAppearance();
		
		if(appear.getTexture() != texture)
			appear.setTexture(texture);
		
		TransparencyAttributes transparency = appear.getTransparencyAttributes();
		if(visible) {
			if(transparency.getTransparency() > 0.0f)
				transparency.setTransparency(0.0f);
		} else {
			if(transparency.getTransparency() < 1.0f)
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

	@Override
	public void resetCoordOffset() {
		super.resetCoordOffset();
		resetCoordOffset3D(simpleU.getViewingPlatform().getViewPlatformTransform());
	}
	
	/**
	 * Resets the TransformGroup to (0.0f, 0.0f, old z coordinate). The
	 * coordinate center moves to the two dimensional center (usually center of
	 * Canvas3D) without changing the zoom.
	 * 
	 * @param tg
	 *            TransformGroup which should be reset
	 */
	private void resetCoordOffset3D(TransformGroup tg) {
		Transform3D t3d_vp = new Transform3D();
		tg.getTransform(t3d_vp);

		Vector3f v3f_trans = new Vector3f();
		t3d_vp.get(v3f_trans);

		/* reset x,y translation but keep z translation */
		t3d_vp.setTranslation(new Vector3f(0.0f, 0.0f, v3f_trans.z));
		tg.setTransform(t3d_vp);
	}

	/**
	 * Moves the TransformGroup to (x, y, z*zoom). The coordinate center moves
	 * out of the screen according to the zoom factor.
	 * 
	 * @param tg
	 *            TransformGroup which should be reset
	 * @param zoom
	 *            level of zoom
	 * @param gridoffset
	 *            level of small zoom
	 */
	private void setZoom3D(TransformGroup tg, double zoom, double gridoffset) {
		Transform3D t3d_vp = new Transform3D();
		tg.getTransform(t3d_vp);

		Vector3d v3d_trans = new Vector3d();
		t3d_vp.get(v3d_trans);

		/* reset x,y translation but keep z translation */
		t3d_vp.setTranslation(new Vector3d(v3d_trans.x, v3d_trans.y, v3d_trans.z*Math.pow(10, zoom)/gridoffset));
		tg.setTransform(t3d_vp);
	}
	
	private double radiusToScale(double radius) {
		return radius/CONVERT3D/DEFAULT_RADIUS;
	}
}
