package jgravsim;

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

public class TEST extends JPanel {
	public TEST() {
		setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();
		Canvas3D canvas3D = new Canvas3D(config);
		add("Center", canvas3D);
		BranchGroup scene = createSceneGraph();
		scene.compile();

		// SimpleUniverse is a Convenience Utility class
		SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

		// This moves the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		simpleU.getViewingPlatform().setNominalViewingTransform();
		simpleU.addBranchGraph(scene);
	}

	public BranchGroup createSceneGraph() {
		// Create the root of the branch graph
		BranchGroup objRoot = new BranchGroup();

		// rotate object has composite transformation matrix
		Transform3D rotate = new Transform3D();
		Transform3D tempRotate = new Transform3D();

		
		rotate.rotX(Math.PI / 4.0d);
		tempRotate.rotY(Math.PI / 5.0d);
		rotate.mul(tempRotate);

		TransformGroup objRotate = new TransformGroup(rotate);

		// Create the transform group node and initialize it to the
		// identity. Enable the TRANSFORM_WRITE capability so that
		// our behavior code can modify it at runtime. Add it to the
		// root of the subgraph.
		TransformGroup objSpin = new TransformGroup();
		objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		objRoot.addChild(objRotate);
		objRotate.addChild(objSpin);

		// Create a simple shape leaf node, add it to the scene graph.
		// ColorCube is a Convenience Utility class


		
		TransformGroup tg = new TransformGroup();
	//	TransformGroup tg2 = new TransformGroup();
		Transform3D eod = new Transform3D();
	//	Transform3D eod2 = new Transform3D();
		Cone cone = new Cone(0.2f, 0.5f);
	//	Cone cone2 = (Cone)cone.cloneTree();
		Vector3f vector = new Vector3f(-.4f,.1f , -.4f);
	//	Vector3f vector2 = new Vector3f(.4f,.1f , -.4f);
		eod.setTranslation(vector);
	//	eod2.setTranslation(vector2);
		tg.setTransform(eod);
	//	tg2.setTransform(eod2);
		tg.addChild(cone); 
	//	tg2.addChild(cone2); 
	  
	//	Sphere kugel = new Sphere(0.4f);
	//	objSpin.addChild(kugel);
		objSpin.addChild(tg);
	//	objSpin.addChild(tg2);

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
				1000.0);

		DirectionalLight light1 = new DirectionalLight(light1Color,
				light1Direction);

		light1.setInfluencingBounds(bounds);
		objRoot.addChild(light1);

		rotator.setSchedulingBounds(bounds);
		objSpin.addChild(rotator);

		return objRoot;
	} // end of createSceneGraph method of HelloJava3Dd
}
