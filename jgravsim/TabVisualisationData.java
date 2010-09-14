package jgravsim;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;


@SuppressWarnings("serial")
public class TabVisualisationData extends JPanel {
	public static final int revision = 1;
	
	XMLParser myXMLParser;
	
			JPanel pa_comp_middledata; /* Mittleres Panel für Buttons */
				JPanel pa_comp_remove;
					JComboBox cb_Objects;
				JPanel pa_comp_coords;
					JLabel la_Coord;
					JPanel pa_comp_coord_tf;
						JTextField tf_Coordx;
						JTextField tf_Coordy;
						JTextField tf_Coordz;
				JPanel pa_comp_speed_exact;
					JLabel la_speed_exact;
					JPanel pa_comp_coord_exact_tf;
						JTextField tf_Speedx;
						JTextField tf_Speedy;
						JTextField tf_Speedz;
				JPanel pa_comp_speed_abs;
					JTextField tf_Speed_abs;

				JPanel pa_comp_mass;
					JTextField tf_Mass;
				JPanel pa_comp_radius;
					JPanel pa_comp_radius_sub;
						JTextField tf_Radius;
					JLabel la_Schwarzschild;
				JPanel pa_comp_dense;
					JPanel pa_comp_dense_sub;
						JTextField tf_Dense;
					JLabel la_Blackhole;
					
	
		//JPanel pa_comp_middleview;
			ObjectView2D ov_top; /* Ansicht von Oben (x,y) */
			ObjectView2D ov_front; /* Ansicht von Vorne (x,z) */
	
	public TabVisualisationData(XMLParser parser) {		
		myXMLParser = parser;
		
		setLayout(new GridLayout(1,1));
		Dimension dSizeCoord = new Dimension(185,20);
		Dimension dSizeSpeed = new Dimension(195,20);
		Dimension dSizeMass = new Dimension(220,20);
		Dimension dSizeRadius = new Dimension(220,20);
		Dimension dSizeDense = new Dimension(220,20);
		
		//* Mittlere Buttons - Data *//
		pa_comp_middledata = new JPanel();
		pa_comp_middledata.setLayout(new BoxLayout(pa_comp_middledata, BoxLayout.Y_AXIS));
		//pa_comp_middledata.setPreferredSize( new Dimension(2*this.getHeight(), this.getHeight()/2));
		pa_comp_middledata.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(24)));

			    //Oject-List / Remove Button
				JLabel la_ObjectList = new JLabel(myXMLParser.getText(240)+":");
				cb_Objects = new JComboBox();
				cb_Objects.addItem(myXMLParser.getText(309));
				
				pa_comp_remove = new JPanel(new FlowLayout());
				pa_comp_remove.add(la_ObjectList);	
				pa_comp_remove.add(cb_Objects);	
				
				//pa_comp_middlecontrols.add(la_headline);
	
				pa_comp_middledata.add(pa_comp_remove);
		
			    //Coordinates
				la_Coord = new JLabel(myXMLParser.getText(241)+":");
				
				tf_Coordx = new JTextField("0.0");
				tf_Coordx.setHorizontalAlignment(JTextField.CENTER);
				tf_Coordx.setEditable(false);
				tf_Coordx.setPreferredSize(dSizeCoord);	
				tf_Coordx.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Coordx.setText("x in cm");
				
				tf_Coordy = new JTextField("0.0");
				tf_Coordy.setHorizontalAlignment(JTextField.CENTER);
				tf_Coordy.setEditable(false);
				tf_Coordy.setPreferredSize(dSizeCoord);	
				tf_Coordy.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Coordy.setText("y in cm");
				
	
				tf_Coordz = new JTextField("0.0");
				tf_Coordz.setHorizontalAlignment(JTextField.CENTER);
				tf_Coordz.setEditable(false);
				tf_Coordz.setPreferredSize(dSizeCoord);	
				tf_Coordz.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Coordz.setText("z in cm");
						
	
				//Speed Eingabefelder	
				la_speed_exact = new JLabel(myXMLParser.getText(242)+":");
				la_speed_exact.setFont(new Font("Sans Serif", Font.BOLD, 12));

				tf_Speedx = new JTextField("0.0");
				tf_Speedx.setHorizontalAlignment(JTextField.CENTER);
				tf_Speedx.setEditable(false);
				tf_Speedx.setPreferredSize(dSizeSpeed);
				tf_Speedx.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Speedx.setText("x in m/s");
				
				tf_Speedy = new JTextField("0.0");
				tf_Speedy.setHorizontalAlignment(JTextField.CENTER);
				tf_Speedy.setEditable(false);
				tf_Speedy.setPreferredSize(dSizeSpeed);
				tf_Speedy.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Speedy.setText("y in m/s");
			    
				
				tf_Speedz = new JTextField("0.0");
				tf_Speedz.setHorizontalAlignment(JTextField.CENTER);
				tf_Speedz.setEditable(false);
				tf_Speedz.setPreferredSize(dSizeSpeed);
				tf_Speedz.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Speedz.setText("z in m/s");
	

				//Speed Slider
				JLabel la_speed_abs = new JLabel(myXMLParser.getText(243)+":");
				la_speed_exact.setFont(new Font("Sans Serif", Font.BOLD, 12));
	
				tf_Speed_abs = new JTextField(myXMLParser.getText(243)+" in m/s");
				tf_Speed_abs.setHorizontalAlignment(JTextField.CENTER);
				tf_Speed_abs.setEditable(false);
				tf_Speed_abs.setPreferredSize(dSizeSpeed);
				tf_Speed_abs.setFont(new Font("Sans Serif", Font.BOLD, 12));
				
			    //Mass Eingabefeld  
	
				JLabel lMass = new JLabel(myXMLParser.getText(246)+":");
				lMass.setFont(new Font("Sans Serif", Font.BOLD, 12));
			
				tf_Mass = new JTextField("0.0");
				tf_Mass.setHorizontalAlignment(JTextField.CENTER);
				tf_Mass.setEditable(false);
				tf_Mass.setPreferredSize(dSizeMass);	
				tf_Mass.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Mass.setText(myXMLParser.getText(246)+" in kg");
	
				
			    //Radius Eingabefeld
				JLabel lRadius = new JLabel(myXMLParser.getText(247)+":");
				lRadius.setFont(new Font("Sans Serif", Font.BOLD, 12));

				tf_Radius = new JTextField("0.0");
				tf_Radius.setHorizontalAlignment(JTextField.CENTER);
				tf_Radius.setEditable(false);
				tf_Radius.setPreferredSize(dSizeRadius);	
				tf_Radius.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Radius.setText(myXMLParser.getText(247)+" in km");

				la_Schwarzschild = new JLabel("("+myXMLParser.getText(108)+")");
				la_Schwarzschild.setHorizontalAlignment(JLabel.CENTER);
				la_Schwarzschild.setFont(new Font(null, Font.BOLD, 14));
				la_Schwarzschild.setVisible(false);
				
				
			    //Dichte Eingabefeld
				JLabel lDense = new JLabel(myXMLParser.getText(245)+":");
				lRadius.setFont(new Font("Sans Serif", Font.BOLD, 12));

				tf_Dense = new JTextField("0.0");
				tf_Dense.setHorizontalAlignment(JTextField.CENTER);
				tf_Dense.setEditable(false);
				tf_Dense.setPreferredSize(dSizeDense);	
				tf_Dense.setFont(new Font("Sans Serif", Font.BOLD, 12));
				//Dense in kg/m³
				tf_Dense.setText(String.format(myXMLParser.getText(231),myXMLParser.getText(245),myXMLParser.getText(460),myXMLParser.getText(456)));

				la_Blackhole = new JLabel(" "+myXMLParser.getText(106)+" ");
				la_Blackhole.setBorder(new BevelBorder(BevelBorder.RAISED));
				la_Blackhole.setHorizontalAlignment(JLabel.CENTER);
				la_Blackhole.setFont(new Font(null, Font.BOLD, 16));
				la_Blackhole.setVisible(false);
				
				pa_comp_coords = new JPanel(new FlowLayout(FlowLayout.LEFT));
				pa_comp_coord_tf = new JPanel(new GridLayout(0,2));
				pa_comp_coord_tf.add(tf_Coordx);
				pa_comp_coord_tf.add(tf_Coordy);
				pa_comp_coord_tf.add(tf_Coordz);
				pa_comp_coords.add(la_Coord);
				pa_comp_coords.add(pa_comp_coord_tf);
								
				pa_comp_coords = new JPanel(new FlowLayout(FlowLayout.LEFT));
				pa_comp_coord_tf = new JPanel(new GridLayout(0,2));
				pa_comp_coord_tf.add(tf_Coordx);
				pa_comp_coord_tf.add(tf_Coordy);
				pa_comp_coord_tf.add(tf_Coordz);
				pa_comp_coords.add(la_Coord);
				pa_comp_coords.add(pa_comp_coord_tf);
				
				pa_comp_speed_exact = new JPanel(new FlowLayout(FlowLayout.LEFT));
				pa_comp_coord_exact_tf = new JPanel(new GridLayout(2,2));
				pa_comp_coord_exact_tf.add(tf_Speedx);
				pa_comp_coord_exact_tf.add(tf_Speedy);
				pa_comp_coord_exact_tf.add(tf_Speedz);
				pa_comp_speed_exact.add(la_speed_exact);
				pa_comp_speed_exact.add(pa_comp_coord_exact_tf);

				pa_comp_speed_abs = new JPanel(new FlowLayout(FlowLayout.LEFT));
				pa_comp_speed_abs.add(la_speed_abs);
				pa_comp_speed_abs.add(tf_Speed_abs);

				pa_comp_mass = new JPanel(new FlowLayout(FlowLayout.LEFT));
				pa_comp_mass.add(lMass);;
				pa_comp_mass.add(tf_Mass);
				

				pa_comp_radius = new JPanel(new FlowLayout(FlowLayout.LEFT));
				pa_comp_radius_sub = new JPanel(new FlowLayout(FlowLayout.LEFT));
				pa_comp_radius_sub.add(lRadius);
				pa_comp_radius_sub.add(tf_Radius);
				pa_comp_radius.add(pa_comp_radius_sub);	
				pa_comp_radius.add(la_Schwarzschild);	
				
				pa_comp_dense = new JPanel(new FlowLayout(FlowLayout.LEFT));
				pa_comp_dense_sub = new JPanel(new FlowLayout(FlowLayout.LEFT));
				pa_comp_dense_sub.add(lDense);
				pa_comp_dense_sub.add(tf_Dense);
				pa_comp_dense.add(pa_comp_dense_sub);	
				pa_comp_dense.add(la_Blackhole);	
				
				
				pa_comp_middledata.add(pa_comp_coords);
				pa_comp_middledata.add(pa_comp_speed_exact);
				pa_comp_middledata.add(pa_comp_speed_abs);
				pa_comp_middledata.add(pa_comp_mass);
				pa_comp_middledata.add(pa_comp_radius);
				pa_comp_middledata.add(pa_comp_dense);
			
			add(pa_comp_middledata);
	}
	
	public void repaintViews() {
		ov_front.repaint();
		ov_top.repaint();
	}

	public void buttonsInactive(boolean b) {
		cb_Objects.setEnabled(b);
	}

	public double pxtomm(int px) {
		return (px*Math.pow(10, ov_top.iZoomLevel))/ov_top.iGridOffset;
	}
	
	public double mmtopx(long mm) {
		return (mm*ov_top.iGridOffset/Math.pow(10, ov_top.iZoomLevel));
	}
	
	public void UpdatePanels(Masspoint_Sim mpsim) {
		Masspoint mp = new Masspoint(mpsim.getID(),mpsim.getPosX(),mpsim.getPosY(),mpsim.getPosZ());
		mp.setSpeedx(mpsim.getSpeedX());
		mp.setSpeedy(mpsim.getSpeedY());
		mp.setSpeedz(mpsim.getSpeedZ());
		mp.setMass(mpsim.getMass());
		mp.setAbsRadius(mpsim.getAbsRadius());
		UpdatePanels(mp);
	}
	
	public void UpdatePanels(Masspoint mp) {
		if(mp == null) {
			cb_Objects.setSelectedItem(new String(myXMLParser.getText(309)));
			//debugout("UpdatePanels() - Sorry");
			return;
		}
		
		MDVector mvspeed = mp.getMDVSpeed();
		MLVector mvcoord = mp.getPos();
		tf_Coordx.setText(InterpretInput.niceInput_Length(mvcoord.x1, myXMLParser));
		tf_Coordy.setText(InterpretInput.niceInput_Length(mvcoord.x2, myXMLParser));
		tf_Coordz.setText(InterpretInput.niceInput_Length(mvcoord.x3, myXMLParser));
		tf_Speedx.setText(InterpretInput.niceInput_Speed(mvspeed.x1, myXMLParser));
		tf_Speedy.setText(InterpretInput.niceInput_Speed(mvspeed.x2, myXMLParser));
		tf_Speedz.setText(InterpretInput.niceInput_Speed(mvspeed.x3, myXMLParser));
		tf_Speed_abs.setText(InterpretInput.niceInput_Speed(mp.getSpeed(), myXMLParser));
		tf_Mass.setText(InterpretInput.niceInput_Mass(mp.getAbsMass(), myXMLParser));
		
		tf_Radius.setText(InterpretInput.niceInput_Length(mp.getRadius(), myXMLParser));
		if(mp.isBlackHole()) {
			la_Schwarzschild.setVisible(true);
			la_Blackhole.setVisible(true);
			tf_Dense.setText(myXMLParser.getText(6));
		}
		else {
			if(la_Blackhole.isVisible()) 
				la_Blackhole.setVisible(false);
			if(la_Schwarzschild.isVisible()) 
				la_Schwarzschild.setVisible(false);
			tf_Dense.setText(InterpretInput.niceInput_Density(mp.getDensity(), myXMLParser));
		}
	}
}
