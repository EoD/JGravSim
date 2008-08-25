package jgravsim;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class TabComputeEasy extends JPanel {

	private static final long serialVersionUID = 1L;
	
	JPanel pa_comp_middledata; /* Mittleres Panel f�r Buttons */
		JPanel pa_comp_coords;
			JLabel la_Coord;
			JPanel pa_comp_coord_tf;
				JTextField tf_Coordx;
				JTextField tf_Coordy;
				JTextField tf_Coordz;
		JPanel pa_comp_speed_exact;
			JLabel la_speed_exact;
			JPanel pa_comp_speed_exact_tf;
				JTextField tf_Speedx_exact;
				JTextField tf_Speedy_exact;
				JTextField tf_Speedz_exact;
		JPanel pa_comp_speed_abs;
			JSlider sl_Speed;
			JTextField tf_Speedabs;
		JPanel pa_comp_mass;
			JSlider sl_Mass;
			JTextField tf_Mass;
		JPanel pa_comp_radius;
			JSlider sl_Radius;
			JTextField tf_Radius;
		JPanel pa_comp_dense;
			JPanel pa_comp_dense_sub;
				JCheckBox chb_Dense = new JCheckBox();
				JTextField tf_Dense;
			JLabel la_Blackhole;
		
	public TabComputeEasy(XMLParser myXMLParser) {
		setLayout(new GridLayout(1,1));
		Dimension dSizeCoord = new Dimension(120,20);      	 
		Dimension dSizeSpeed = dSizeCoord;        	    
		Dimension dSizeMass = new Dimension(110,20);   	
		Dimension dSizeRadius = dSizeMass;      
		Dimension dSizeSlider = new Dimension(275,42);
		Dimension dSizeSpeedSlider = new Dimension(260,42);
		
			//* Mittlere Buttons - Data *//
			pa_comp_middledata = new JPanel();
			pa_comp_middledata.setLayout(new BoxLayout(pa_comp_middledata,BoxLayout.Y_AXIS));
			//pa_comp_middledata.setLayout(new BoxLayout(pa_comp_middledata, BoxLayout.Y_AXIS));
			//pa_comp_middledata.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Daten"));
			pa_comp_middledata.add(new JPanel());
		//Coordinates
			la_Coord = new JLabel(myXMLParser.getText(241)+":");
			
			tf_Coordx = new JTextField("0.0");
			tf_Coordx.setHorizontalAlignment(JTextField.CENTER);
			tf_Coordx.setEditable(false);
			tf_Coordx.setPreferredSize(dSizeCoord);	
			tf_Coordx.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Coordx.setText("x "+myXMLParser.getText(5)+" cm");
			
			tf_Coordy = new JTextField("0.0");
			tf_Coordy.setHorizontalAlignment(JTextField.CENTER);
			tf_Coordy.setEditable(false);
			tf_Coordy.setPreferredSize(dSizeCoord);	
			tf_Coordy.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Coordy.setText("y "+myXMLParser.getText(5)+" cm");
			

			tf_Coordz = new JTextField("0.0");
			tf_Coordz.setHorizontalAlignment(JTextField.CENTER);
			tf_Coordz.setEditable(false);
			tf_Coordz.setPreferredSize(dSizeCoord);	
			tf_Coordz.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Coordz.setText("z "+myXMLParser.getText(5)+" cm");
			
	

//			JLabel lMassUnit = new JLabel(sunitmass);
//			lMassUnit.setFont(new Font("Sans Serif", Font.BOLD, 12));			

		//Speed Eingabefelder	
			la_speed_exact = new JLabel(myXMLParser.getText(242)+":");
			la_speed_exact.setFont(new Font("Sans Serif", Font.BOLD, 12));

			tf_Speedx_exact = new JTextField("0.0");
			tf_Speedx_exact.setHorizontalAlignment(JTextField.CENTER);
			tf_Speedx_exact.setEditable(false);
			tf_Speedx_exact.setPreferredSize(dSizeSpeed);
			tf_Speedx_exact.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Speedx_exact.setText("x "+myXMLParser.getText(5)+" m/s");
			
			tf_Speedy_exact = new JTextField("0.0");
			tf_Speedy_exact.setHorizontalAlignment(JTextField.CENTER);
			tf_Speedy_exact.setEditable(false);
			tf_Speedy_exact.setPreferredSize(dSizeSpeed);
			tf_Speedy_exact.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Speedy_exact.setText("y "+myXMLParser.getText(5)+" m/s");
		    
			tf_Speedz_exact = new JTextField("0.0");
			tf_Speedz_exact.setHorizontalAlignment(JTextField.CENTER);
			tf_Speedz_exact.setEditable(false);
			tf_Speedz_exact.setPreferredSize(dSizeSpeed);
			tf_Speedz_exact.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Speedz_exact.setText("z "+myXMLParser.getText(5)+" m/s");


		//Speed Slider
			JLabel la_speed_abs = new JLabel(myXMLParser.getText(243)+":");
			la_speed_exact.setFont(new Font("Sans Serif", Font.BOLD, 12));
			
			sl_Speed = new JSlider();
			sl_Speed.setPaintTicks(true);
			sl_Speed.setMinorTickSpacing(5);
			sl_Speed.setMajorTickSpacing(10);
			sl_Speed.setPaintLabels(true);
			Hashtable<Integer, JLabel> tableSpeed = new Hashtable<Integer, JLabel>();
			//tableSpeed.put( new Integer( 0 ), new JLabel("null") );
			//tableSpeed.put( new Integer( 12 ), new JLabel("ICE") );
			tableSpeed.put( new Integer( 40 ), new JLabel(myXMLParser.getText(504)) );
			tableSpeed.put( new Integer( 100 ), new JLabel(myXMLParser.getText(7)) );
			sl_Speed.setLabelTable(tableSpeed);
			sl_Speed.setValue(0);
			sl_Speed.setPreferredSize(dSizeSpeedSlider);

			tf_Speedabs = new JTextField("0.0");
			tf_Speedabs.setHorizontalAlignment(JTextField.CENTER);
			tf_Speedabs.setEditable(false);
			tf_Speedabs.setPreferredSize(dSizeMass);	
			tf_Speedabs.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Speedabs.setText("|v| "+myXMLParser.getText(5)+" m/s");	
			
		//Mass Slider + Eingabefeld  
			JLabel lMass = new JLabel(myXMLParser.getText(246)+":");
			lMass.setFont(new Font("Sans Serif", Font.BOLD, 12));
			
		    sl_Mass = new JSlider();
			sl_Mass.setPaintTicks(true);
			sl_Mass.setMinorTickSpacing(5);
			sl_Mass.setMajorTickSpacing(10);
			sl_Mass.setPaintLabels(true);
			Hashtable<Integer, JLabel> tableMass = new Hashtable<Integer, JLabel>();
			//integer = log_SMASSCONST(masse)
			//integer = e^(ln(mass)/SMASSCONST)
			tableMass.put( new Integer((int) Math.exp(Math.log(CalcCode.EM)/CalcCode.SMASSCONST) ), new JLabel(myXMLParser.getText(504)) );	//==6*10^24 kg
			tableMass.put( new Integer((int) Math.exp(Math.log(CalcCode.SM)/CalcCode.SMASSCONST)  ), new JLabel(myXMLParser.getText(501)) );		//==2*10^30kg
			//tableMass.put( new Integer( 100 ), new JLabel("Galaxie") ); 	//==10^42kg
			sl_Mass.setLabelTable(tableMass);
			sl_Mass.setValue(1);
			sl_Mass.setPreferredSize(dSizeSlider);
			
			tf_Mass = new JTextField("0.0");
			tf_Mass.setHorizontalAlignment(JTextField.CENTER);
			tf_Mass.setEditable(false);
			tf_Mass.setPreferredSize(dSizeMass);	
			tf_Mass.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Mass.setText(myXMLParser.getText(246)+" "+myXMLParser.getText(5)+" kg");	
			
		//Radius Slider + Eingabefeld
			JLabel lRadius = new JLabel(myXMLParser.getText(247)+":");
			lRadius.setFont(new Font("Sans Serif", Font.BOLD, 12));
			
		    sl_Radius = new JSlider();
			sl_Radius.setPaintTicks(true);
			sl_Radius.setMinorTickSpacing(5);
			sl_Radius.setMajorTickSpacing(10);
			sl_Radius.setPaintLabels(true);
			Hashtable<Integer, JLabel> tableRadius = new Hashtable<Integer, JLabel>();
			//integer = e^(ln(radius)/SRADIUSCONST)
			tableRadius.put( new Integer((int) Math.exp(Math.log(CalcCode.ER)/CalcCode.SRADIUSCONST) ), new JLabel(myXMLParser.getText(504)) );	//==6*10^24 kg
			tableRadius.put( new Integer((int) Math.exp(Math.log(CalcCode.SR)/CalcCode.SRADIUSCONST)  ), new JLabel(myXMLParser.getText(501)) );		//==2*10^30kg
			//tableRadius.put( new Integer( 12 ), new JLabel("Sonne") );
			//tableRadius.put( new Integer( 59 ), new JLabel("Jupiter-Orbit") );	//9.461E20 km
			//tableRadius.put( new Integer( 100 ), new JLabel("Galaxie") );	//long limit
			sl_Radius.setLabelTable(tableRadius);
			sl_Radius.setValue(1);
			sl_Radius.setPreferredSize(dSizeSlider);

			tf_Radius = new JTextField("0.0");
			tf_Radius.setHorizontalAlignment(JTextField.CENTER);
			tf_Radius.setEditable(false);
			tf_Radius.setPreferredSize(dSizeRadius);	
			tf_Radius.setFont(new Font("Sans Serif", Font.BOLD, 12));
			tf_Radius.setText(myXMLParser.getText(247)+" "+myXMLParser.getText(5)+" km");


			//Dense Slider + Checkbox + Textfield	
				//chb_Dense = new JCheckBox(myXMLParser.getText(244)+":");
				//chb_Dense.setFont(new Font("Sans Serif", Font.BOLD, 12));
				JLabel la_dense = new JLabel(myXMLParser.getText(245)+": ");
				
				
				tf_Dense = new JTextField("0.0");
				tf_Dense.setHorizontalAlignment(JTextField.CENTER);
				tf_Dense.setEditable(false);
				tf_Dense.setPreferredSize(dSizeMass);	
				tf_Dense.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Dense.setText(String.format(myXMLParser.getText(231),myXMLParser.getText(245),myXMLParser.getText(460),myXMLParser.getText(456)));

				la_Blackhole = new JLabel(" "+myXMLParser.getText(106)+" ");
				la_Blackhole.setBorder(new BevelBorder(BevelBorder.RAISED));
				la_Blackhole.setHorizontalAlignment(JLabel.CENTER);
				la_Blackhole.setFont(new Font(null, Font.BOLD, 16));
				la_Blackhole.setVisible(false);
			
			pa_comp_coords = new JPanel(new FlowLayout(FlowLayout.LEFT));
			pa_comp_coord_tf = new JPanel(new GridLayout(1,0));
			pa_comp_coord_tf.add(tf_Coordx);
			pa_comp_coord_tf.add(tf_Coordy);
			pa_comp_coord_tf.add(tf_Coordz);
			pa_comp_coords.add(la_Coord);
			pa_comp_coords.add(pa_comp_coord_tf);
			
			
			pa_comp_speed_exact = new JPanel(new FlowLayout(FlowLayout.LEFT));
			pa_comp_speed_exact_tf = new JPanel(new GridLayout(1,0));
			//pa_comp_speed_exact_tf.setBorder(new BevelBorder(BevelBorder.RAISED));
			pa_comp_speed_exact_tf.add(tf_Speedx_exact);
			pa_comp_speed_exact_tf.add(tf_Speedy_exact);
			pa_comp_speed_exact_tf.add(tf_Speedz_exact);
			pa_comp_speed_exact.add(la_speed_exact);
			pa_comp_speed_exact.add(pa_comp_speed_exact_tf);
			pa_comp_speed_exact.setBorder(new BevelBorder(BevelBorder.RAISED));
			pa_comp_speed_exact.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			pa_comp_speed_abs = new JPanel(new FlowLayout(FlowLayout.LEFT));
			pa_comp_speed_abs.add(la_speed_abs);
			pa_comp_speed_abs.add(sl_Speed);
			pa_comp_speed_abs.add(tf_Speedabs);
			
			pa_comp_mass = new JPanel(new FlowLayout(FlowLayout.LEFT));
			pa_comp_mass.add(lMass);
			pa_comp_mass.add(sl_Mass);
			pa_comp_mass.add(tf_Mass);
			
			pa_comp_radius = new JPanel(new FlowLayout(FlowLayout.LEFT));
			pa_comp_radius.add(lRadius);
			pa_comp_radius.add(sl_Radius);
			pa_comp_radius.add(tf_Radius);
			
			pa_comp_dense = new JPanel(new FlowLayout(FlowLayout.LEFT));
			pa_comp_dense_sub = new JPanel(new FlowLayout(FlowLayout.LEFT));
			pa_comp_dense_sub.add(la_dense);
			//pa_comp_dense.add(sl_Dense);
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
}
