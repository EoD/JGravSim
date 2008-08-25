package jgravsim;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class TabComputeAdvanced extends JPanel {

	private static final long serialVersionUID = 1L;
		
		JPanel pa_comp_topdata; /* Oberes Panel für Buttons */
			JButton b_edit;
			JPanel pa_comp_coords;
				JLabel la_Coord;
				JPanel pa_comp_coord_tf;
					JTextField tf_Coordx;
					JTextField tf_Coordy;
					JTextField tf_Coordz;
	
			//JPanel pa_comp_speed_direction;
			//	JLabel la_speed_direction;
			//	JPanel pa_comp_coord_direction_tf;
			//		JTextField tf_Speedx_direction;
			//		JTextField tf_Speedy_direction;
			//		JTextField tf_Speedz_direction;	
			JPanel pa_comp_speed_abs;
				JTextField tf_Speedabs;
			JPanel pa_comp_speed_exact;
				JLabel la_speed_exact;
				JPanel pa_comp_coord_exact_tf;
					JTextField tf_Speedx_exact;
					JTextField tf_Speedy_exact;
					JTextField tf_Speedz_exact;
					
			JPanel pa_comp_dense;
			JPanel pa_comp_mass;
			JTextField tf_Mass;
			JPanel pa_comp_radius;
				JTextField tf_Radius;
			JCheckBox chb_Dense = new JCheckBox();
				JTextField tf_Dense;
				JLabel la_Blackhole;
			
	public TabComputeAdvanced(XMLParser myXMLParser) {
		setLayout(new GridLayout(1,1));
		Dimension dSizeCoord = new Dimension(185,20);      	 
		Dimension dSizeSpeed = new Dimension(195,20);        	    
		Dimension dSizeMass = new Dimension(220,20);   	
		Dimension dSizeRadius = new Dimension(220,20);  
		Dimension dSizeDense = new Dimension(220,20);         
		
			//* Mittlere Buttons - Data *//
			pa_comp_topdata = new JPanel();
			pa_comp_topdata.setLayout(new BoxLayout(pa_comp_topdata, BoxLayout.Y_AXIS));
			//pa_comp_middledata.setPreferredSize( new Dimension(2*this.getHeight(), this.getHeight()/2));
			//pa_comp_topdata.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Daten"));
			pa_comp_topdata.add(new JPanel());
			
			b_edit = new JButton(myXMLParser.getText(207));	
			
		    //Coordinates
			la_Coord = new JLabel(myXMLParser.getText(241)+": ");
			
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
			
			
		    //Mass Slider + Eingabefeld  

			JLabel lMass = new JLabel(myXMLParser.getText(246)+": ");
			lMass.setFont(new Font("Sans Serif", Font.BOLD, 12));
			
			tf_Mass = new JTextField("0.0");
			tf_Mass.setHorizontalAlignment(JTextField.CENTER);
			tf_Mass.setEditable(false);
//			tf_Mass.setMinimumSize(dSizeMass);
			tf_Mass.setPreferredSize(dSizeMass);	
//			tf_Mass.setBorder( new BevelBorder(BevelBorder.RAISED) );
			tf_Mass.setFont(new Font("Sans Serif", Font.BOLD, 12));
//***				tf_Mass.setEditable(true);
			tf_Mass.setText(myXMLParser.getText(246)+" in kg");

//			JLabel lMassUnit = new JLabel(sunitmass);
//			lMassUnit.setFont(new Font("Sans Serif", Font.BOLD, 12));			

			//Speed Eingabefelder	
			/*la_speed_direction = new JLabel("Geschw.: ");
			la_speed_direction.setFont(new Font("Sans Serif", Font.BOLD, 12));

			tf_Speedx_direction = new JTextField("0.0");
			tf_Speedx_direction.setHorizontalAlignment(JTextField.CENTER);
			tf_Speedx_direction.setEditable(false);
			tf_Speedx_direction.setPreferredSize(dSizeSpeed);
			tf_Speedx_direction.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Speedx_direction.setText("x-Geschwindigkeit in m/s");
			
			tf_Speedy_direction = new JTextField("0.0");
			tf_Speedy_direction.setHorizontalAlignment(JTextField.CENTER);
			tf_Speedy_direction.setEditable(false);
			tf_Speedy_direction.setPreferredSize(dSizeSpeed);
			tf_Speedy_direction.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Speedy_direction.setText("y-Geschwindigkeit in m/s");
		    
			
			tf_Speedz_direction = new JTextField("0.0");
			tf_Speedz_direction.setHorizontalAlignment(JTextField.CENTER);
			tf_Speedz_direction.setEditable(false);
			tf_Speedz_direction.setPreferredSize(dSizeSpeed);
			tf_Speedz_direction.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Speedz_direction.setText("z-Geschwindigkeit in m/s");
			*/
			
			//Speed Slider
			JLabel la_speed_abs = new JLabel(myXMLParser.getText(243)+" ");
			la_speed_abs.setFont(new Font("Sans Serif", Font.BOLD, 12));
			
			tf_Speedabs = new JTextField("0.0");
			tf_Speedabs.setHorizontalAlignment(JTextField.CENTER);
			tf_Speedabs.setEditable(false);
			tf_Speedabs.setPreferredSize(dSizeSpeed);
			tf_Speedabs.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Speedabs.setText("|v| in m/s");

			//Speed Eingabefelder	
			la_speed_exact = new JLabel(myXMLParser.getText(242)+": ");
			la_speed_exact.setFont(new Font("Sans Serif", Font.BOLD, 12));

			tf_Speedx_exact = new JTextField("0.0");
			tf_Speedx_exact.setHorizontalAlignment(JTextField.CENTER);
			tf_Speedx_exact.setEditable(false);
			tf_Speedx_exact.setPreferredSize(dSizeSpeed);
			tf_Speedx_exact.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Speedx_exact.setText("x in m/s");
			
			tf_Speedy_exact = new JTextField("0.0");
			tf_Speedy_exact.setHorizontalAlignment(JTextField.CENTER);
			tf_Speedy_exact.setEditable(false);
			tf_Speedy_exact.setPreferredSize(dSizeSpeed);
			tf_Speedy_exact.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Speedy_exact.setText("y in m/s");
			
			tf_Speedz_exact = new JTextField("0.0");
			tf_Speedz_exact.setHorizontalAlignment(JTextField.CENTER);
			tf_Speedz_exact.setEditable(false);
			tf_Speedz_exact.setPreferredSize(dSizeSpeed);
			tf_Speedz_exact.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Speedz_exact.setText("z in m/s");
			
			
		    //Radius Slider + Eingabefeld
			JLabel lRadius = new JLabel(myXMLParser.getText(247)+": ");
			lRadius.setFont(new Font("Sans Serif", Font.BOLD, 12));

			tf_Radius = new JTextField("0.0");
			tf_Radius.setHorizontalAlignment(JTextField.CENTER);
			tf_Radius.setEditable(false);
			tf_Radius.setPreferredSize(dSizeRadius);	
			tf_Radius.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Radius.setText(myXMLParser.getText(247)+" in km");


			//Dense Slider + Checkbox + Textfield	
				//chb_Dense = new JCheckBox("feste Dichte:");
				//chb_Dense.setFont(new Font("Sans Serif", Font.BOLD, 12));
				JLabel la_dense = new JLabel(myXMLParser.getText(245)+": ");
				
				tf_Dense = new JTextField("0.0");
				tf_Dense.setHorizontalAlignment(JTextField.CENTER);
				tf_Dense.setEditable(false);
				tf_Dense.setPreferredSize(dSizeDense);	
				tf_Dense.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Dense.setText(myXMLParser.getText(245)+" in kg/m");	
				
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
			
			/*pa_comp_speed_direction = new JPanel(new FlowLayout(FlowLayout.LEFT));
			pa_comp_coord_direction_tf = new JPanel(new GridLayout(2,2));
			pa_comp_coord_direction_tf.add(tf_Speedx_direction);
			pa_comp_coord_direction_tf.add(tf_Speedy_direction);
			pa_comp_coord_direction_tf.add(tf_Speedz_direction);
			pa_comp_speed_direction.add(la_speed_direction);
			pa_comp_speed_direction.add(pa_comp_coord_direction_tf);
			*/
			
			pa_comp_speed_abs = new JPanel(new FlowLayout(FlowLayout.LEFT));
			pa_comp_speed_abs.add(la_speed_abs);
			pa_comp_speed_abs.add(tf_Speedabs);


			pa_comp_speed_exact = new JPanel(new FlowLayout(FlowLayout.LEFT));
			pa_comp_coord_exact_tf = new JPanel(new GridLayout(2,2));
			pa_comp_coord_exact_tf.add(tf_Speedx_exact);
			pa_comp_coord_exact_tf.add(tf_Speedy_exact);
			pa_comp_coord_exact_tf.add(tf_Speedz_exact);
			pa_comp_speed_exact.add(la_speed_exact);
			pa_comp_speed_exact.add(pa_comp_coord_exact_tf);
			
			pa_comp_mass = new JPanel(new FlowLayout(FlowLayout.LEFT));
			pa_comp_mass.add(lMass);
			pa_comp_mass.add(tf_Mass);
			
			pa_comp_radius = new JPanel(new FlowLayout(FlowLayout.LEFT));
			pa_comp_radius.add(lRadius);
			pa_comp_radius.add(tf_Radius);

			pa_comp_dense = new JPanel(new FlowLayout(FlowLayout.LEFT));
			//pa_comp_dense.add(chb_Dense);
			pa_comp_dense.add(la_dense);
			pa_comp_dense.add(tf_Dense);
			pa_comp_dense.add(la_Blackhole);

			pa_comp_topdata.add(b_edit);
			pa_comp_topdata.add(pa_comp_coords);
			//pa_comp_topdata.add(pa_comp_speed_direction);
			pa_comp_topdata.add(pa_comp_speed_abs);
			pa_comp_topdata.add(pa_comp_speed_exact);
			pa_comp_topdata.add(pa_comp_mass);
			pa_comp_topdata.add(pa_comp_radius);
			pa_comp_topdata.add(pa_comp_dense);
			//pa_comp_middledata.add(la_Blackhole);
			
			add(pa_comp_topdata);
	}
}
