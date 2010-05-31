package jgravsim;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class View_SpeedVector extends JFrame implements ChangeListener {
	private static final long serialVersionUID = 1L;

	XMLParser myXMLParser;
	Controller myController;
	TabCompute myTab;
	Masspoint masspoint;
	JPanel pa_top;
		JSlider vec_top;
		ObjectView2D ov_fake_top;
		JLabel la_top;
	JPanel pa_front;
		JSlider vec_front;
		ObjectView2D ov_fake_front;
		JLabel la_front;
	
	View_SpeedVector(Masspoint mp, Controller controller) {
		if(mp == null) {
			Controller.debugout("View_SpeedVector - mp==null!");
			return;
		}
		myController = controller;
		myTab = myController.myView.pa_computetab;
		myXMLParser = myController.myView.myXMLParser;
		masspoint = mp;

		Dimension dSizeSpeedSlider = new Dimension(340,42);
		Dimension dSizeObjectViews = new Dimension(100, 100);
		Dimension dSizePanels = new Dimension(
				dSizeSpeedSlider.width+20, 
				dSizeSpeedSlider.height+dSizeObjectViews.height+40);
		
		setLayout(new GridLayout(0,1));
		adjustWindow(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setUndecorated(false);
		setTitle(myXMLParser.getText(228)+" "+myXMLParser.getText(229)+masspoint.id);
		
	///////Panel Top
		pa_top = new JPanel();
		pa_top.setLayout(new FlowLayout(FlowLayout.CENTER));
		pa_top.setPreferredSize(dSizePanels);
		pa_top.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
				myXMLParser.getText(233)+" ("+String.valueOf(myTab.ov_top.cAxes)+"-"+myXMLParser.getText(228)+"):"));
		
		vec_top = new JSlider(0, 360);
		vec_top.setPaintTicks(true);
		vec_top.setMinorTickSpacing(5);
		vec_top.setMajorTickSpacing(15);
		vec_top.setPaintLabels(true);
		Hashtable<Integer, JLabel> tabletop = new Hashtable<Integer, JLabel>();
		for(int i=vec_top.getMinimum(); i<=vec_top.getMaximum(); i+=45) {
			tabletop.put( new Integer( i ), new JLabel(i+"°"));
		}
		vec_top.setLabelTable(tabletop);
		vec_top.setPreferredSize(dSizeSpeedSlider);
		vec_top.setValue((int)masspoint.getUnitSpeedAngle()[1]);
		
		ov_fake_top = new ObjectView2D(myTab.ov_top.cAxes);
		ov_fake_top.setPreferredSize(dSizeObjectViews);
		ov_fake_top.setBoxed(false);
		ov_fake_top.resetCurrentStep();
		ov_fake_top.resetAllSteps();
		ov_fake_top.iGridOffset = 12;
		ov_fake_top.mp_selected = masspoint;

		la_top = new JLabel((int)masspoint.getUnitSpeedAngle()[1]+"°");
		
		pa_top.add(vec_top);
		pa_top.add(ov_fake_top);
		pa_top.add(la_top);
		
		
	//////Panel Front
		pa_front = new JPanel();
		pa_front.setLayout(new FlowLayout(FlowLayout.CENTER));
		pa_front.setPreferredSize(dSizePanels);
		pa_front.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
				myXMLParser.getText(232)+" ("+String.valueOf(myTab.ov_front.cAxes)+"-"+myXMLParser.getText(228)+"):"));
		
		vec_front = new JSlider(0, 180);
		vec_front.setPaintTicks(true);
		vec_front.setMinorTickSpacing(5);
		vec_front.setMajorTickSpacing(15);
		vec_front.setPaintLabels(true);
		Hashtable<Integer, JLabel> tableFront = new Hashtable<Integer, JLabel>();
		for(int j=vec_front.getMinimum(); j<=vec_front.getMaximum(); j+=45) {
			tableFront.put( new Integer( j ), new JLabel(j+"°"));
		}
		vec_front.setLabelTable(tableFront);
		vec_front.setPreferredSize(dSizeSpeedSlider);
		vec_front.setValue((int)masspoint.getUnitSpeedAngle()[0]);

		ov_fake_front = new ObjectView2D(myTab.ov_front.cAxes);
		ov_fake_front.setPreferredSize(dSizeObjectViews);
		ov_fake_front.setBoxed(false);
		ov_fake_front.resetCurrentStep();
		ov_fake_front.resetAllSteps();
		ov_fake_front.iGridOffset = 12;
		ov_fake_front.mp_selected = masspoint;
		
		la_front = new JLabel((int)masspoint.getUnitSpeedAngle()[0]+"°");
		
		pa_front.add(vec_front);
		pa_front.add(ov_fake_front);
		pa_front.add(la_front);
		

		//add(new JPanel());
		add(pa_top);
		//add(new JPanel());
		add(pa_front);
		setVisible(true);
		pack();
		
		vec_top.addChangeListener(this);
		vec_front.addChangeListener(this);
	}
	
	void adjustWindow(Component frame) {	
		Point ViewPos = myTab.getLocation();
		Dimension ViewSize = myTab.getSize();
		Dimension frameSize = this.getSize();
        
		frame.setLocation(
                (2*ViewPos.x - frameSize.width  + ViewSize.width >> 1 ),
                (2*ViewPos.y - frameSize.height + ViewSize.height >> 1)
        );
	}

	//@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if(source == vec_top || source == vec_front){
			//Controller.debugout("SpeedVec - stateChanged - theta="+vec_top.getValue()+", phi="+vec_front.getValue());
			masspoint.setUnitSpeedAngle(vec_front.getValue(), vec_top.getValue());
			updateView(source);
		}
	}

	private void repaintViews() {
		ov_fake_top.repaint();
		ov_fake_front.repaint();
	}
	private void updateView(Object source) {
		vec_top.removeChangeListener(this);
		vec_front.removeChangeListener(this);
		
		if(source != vec_top)
			vec_top.setValue((int)masspoint.getUnitSpeedAngle()[1]);
		if(source != vec_front)
			vec_front.setValue((int)masspoint.getUnitSpeedAngle()[0]);
		
		la_top.setText(masspoint.getUnitSpeedAngle()[1]+"°");
		la_front.setText(masspoint.getUnitSpeedAngle()[0]+"°");
		vec_top.addChangeListener(this);
		vec_front.addChangeListener(this);
		repaintViews();
		myController.updateComputePanels(masspoint, this);
	}
}
