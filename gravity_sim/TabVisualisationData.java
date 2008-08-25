package gravity_sim;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;


public class TabVisualisationData extends JPanel {
	private static final long serialVersionUID = 1L;	
	//static final String sunitcoord = "mm";
	//static final String sunitspeed = "m/s";
	//static final String sunitmass = "g";
	static final String sheadline = "Daten über Massenpunkt";
	static final String strtop = "Oben";
	static final String strfront = "Vorne";
	
						
					
			JPanel pa_comp_middledata; /* Mittleres Panel für Buttons */
				JPanel pa_comp_remove;
					JComboBox cb_Objects;
					JLabel la_Blackhole;
				JPanel pa_comp_coords;
					JLabel la_Coord;
					JPanel pa_comp_coord_tf;
						JTextField tf_Coordx;
						JTextField tf_Coordy;
						JTextField tf_Coordz;
				JPanel pa_comp_mass;
					//JSlider sl_Mass;
					JTextField tf_Mass;
				JPanel pa_comp_speed_exact;
					JLabel la_speed_exact;
					JPanel pa_comp_coord_exact_tf;
						JTextField tf_Speedx;
						JTextField tf_Speedy;
						JTextField tf_Speedz;
				JPanel pa_comp_speed_abs;
					JTextField tf_Speed_abs;
					//JSlider sl_Speed;
				JPanel pa_comp_radius;
					//JSlider sl_Radius;
					JTextField tf_Radius;
	
		//JPanel pa_comp_middleview;
			ObjectView ov_top; /* Ansicht von Oben (x,y) */
			ObjectView ov_front; /* Ansicht von Vorne (x,z) */
	
	public TabVisualisationData() {
		setLayout(new GridLayout(1,1));
		Dimension dSizeCoord = new Dimension(170,20);      	 
		Dimension dSizeSpeed = new Dimension(180,20);        	    
		Dimension dSizeMass = new Dimension(166,20);   	
		Dimension dSizeRadius = new Dimension(150,20);        
//		java.text.NumberFormat numberFormat = java.text.NumberFormat.getNumberInstance();
//	    NumberFormatter formatter = new NumberFormatter(numberFormat);
//	    formatter.setMinimum(new Double(0.0));
		
		/* Oberer Helpbutton */
/*		pa_comp_topbuttons = new JPanel();
		pa_comp_topbuttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		b_help = new JButton("?");
		pa_comp_topbuttons.add(b_help);
		add(pa_comp_topbuttons,BorderLayout.NORTH);
*/		
		/* Mittlerer Bereich */
		//pa_comp_middle = new JPanel(new GridLayout(0,2));
		
			//* View Bereiche *//
			//pa_comp_middleview = new JPanel(new GridLayout(2,0));
			//ov_top = new ObjectView(top,"xy");
			//ov_front = new ObjectView(front,"xz");
			
			//ov_top.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),strtop));
			//ov_front.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),strfront));
			
			//add(ov_top);
			//add(ov_front);
			//pa_comp_middleview.add(ov_top);
			//pa_comp_middleview.add(ov_front);
			
		//pa_comp_middle.add(pa_comp_middleview);
			
			//* Mittlere Buttons *//
			//pa_comp_middlebuttons  = new JPanel(new GridLayout(0,1));
			// TODO remove?
/*			Dimension dmiddlebuttons = new Dimension(pa_comp_middle.getHeight(),pa_comp_middle.getWidth()/2);
			pa_comp_middlebuttons.setSize(dmiddlebuttons);
			pa_comp_middlebuttons.setPreferredSize(dmiddlebuttons);
			pa_comp_middlebuttons.setMaximumSize(dmiddlebuttons);
			pa_comp_middlebuttons.setMinimumSize(dmiddlebuttons);
*/			
				//* Mittlere Buttons - Controls *//
/*				pa_comp_middlecontrols = new JPanel(new GridLayout(0,1)); //new JPanel(new GridLayout(0,1));
				pa_comp_middlecontrols.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Kontrollelemente"));
*/				// TODO remove?
/*				//Dimension dmiddlebuttons = new Dimension(pa_comp_middlebuttons.getHeight()/3,pa_comp_middlebuttons.getWidth());
				Controller.debugout("TabCompute() - Height:"+this.getHeight()+", Width"+pa_comp_middlebuttons.getMaximumSize());
				Dimension dmiddlebuttons = new Dimension(435,200);
				pa_comp_middlecontrols.setSize(dmiddlebuttons);
				pa_comp_middlecontrols.setPreferredSize(dmiddlebuttons);
				pa_comp_middlecontrols.setMaximumSize(dmiddlebuttons);
				pa_comp_middlecontrols.setMinimumSize(dmiddlebuttons);
*/
				
				//la_headline = new JLabel(sheadline+"s");
				//la_headline.setFont(new Font("Sans Serif", Font.BOLD, 16));
	
		//* Mittlere Buttons - Data *//
		pa_comp_middledata = new JPanel(new GridLayout(0,1));
		//pa_comp_middledata.setPreferredSize( new Dimension(2*this.getHeight(), this.getHeight()/2));
		pa_comp_middledata.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Daten"));
			
				
				la_Blackhole = new JLabel(" Schwarzes Loch ");
				la_Blackhole.setBorder(new BevelBorder(BevelBorder.RAISED));
				la_Blackhole.setHorizontalAlignment(JLabel.CENTER);
				la_Blackhole.setFont(new Font(null, Font.BOLD, 16));
				la_Blackhole.setVisible(false);
				
			    //Oject-List / Remove Button
				JLabel la_ObjectList = new JLabel("Massenpunkt:");
				cb_Objects = new JComboBox();
				cb_Objects.addItem(" MP Liste ");
				
				pa_comp_remove = new JPanel(new FlowLayout());
				pa_comp_remove.add(la_ObjectList);	
				pa_comp_remove.add(cb_Objects);	
				
				//pa_comp_middlecontrols.add(la_headline);
				pa_comp_remove.add(la_Blackhole);	
	
				pa_comp_middledata.add(pa_comp_remove);
		
			
			    //Coordinates
				la_Coord = new JLabel("Koordinaten: ");
				
				tf_Coordx = new JTextField("0.0");
				tf_Coordx.setHorizontalAlignment(JTextField.CENTER);
				tf_Coordx.setEditable(false);
				tf_Coordx.setPreferredSize(dSizeCoord);	
				tf_Coordx.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Coordx.setText("x-Koordinate in cm");
				
				tf_Coordy = new JTextField("0.0");
				tf_Coordy.setHorizontalAlignment(JTextField.CENTER);
				tf_Coordy.setEditable(false);
				tf_Coordy.setPreferredSize(dSizeCoord);	
				tf_Coordy.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Coordy.setText("y-Koordinate in cm");
				
	
				tf_Coordz = new JTextField("0.0");
				tf_Coordz.setHorizontalAlignment(JTextField.CENTER);
				tf_Coordz.setEditable(false);
				tf_Coordz.setPreferredSize(dSizeCoord);	
				tf_Coordz.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Coordz.setText("z-Koordinate in cm");
				
				
			    //Mass Slider + Eingabefeld  
	
				JLabel lMass = new JLabel("Masse: ");
				lMass.setFont(new Font("Sans Serif", Font.BOLD, 12));
				
/*			    sl_Mass = new JSlider();
				sl_Mass.setPaintTicks(true);
				sl_Mass.setMinorTickSpacing(5);
				sl_Mass.setMajorTickSpacing(10);
				sl_Mass.setPaintLabels(true);
				Hashtable<Integer, JLabel> tableMass = new Hashtable<Integer, JLabel>();
				//tableMass.put( new Integer( 10 ), new JLabel("Erde") );	//==6*10^24 kg
				tableMass.put( new Integer( 28 ), new JLabel("Sonne") );		//==2*10^30kg
				tableMass.put( new Integer( 100 ), new JLabel("Galaxie") ); 	//==10^42kg
				sl_Mass.setLabelTable(tableMass);
				sl_Mass.setValue(1);
				sl_Mass.setEnabled(false);
*/				
				tf_Mass = new JTextField("0.0");
				tf_Mass.setHorizontalAlignment(JTextField.CENTER);
				tf_Mass.setEditable(false);
//				tf_Mass.setMinimumSize(dSizeMass);
				tf_Mass.setPreferredSize(dSizeMass);	
//				tf_Mass.setBorder( new BevelBorder(BevelBorder.RAISED) );
				tf_Mass.setFont(new Font("Sans Serif", Font.BOLD, 12));
//***			tf_Mass.setEditable(true);
				tf_Mass.setText("Masse in kg");
	
//				JLabel lMassUnit = new JLabel(sunitmass);
//				lMassUnit.setFont(new Font("Sans Serif", Font.BOLD, 12));			
	
				//Speed Eingabefelder	
				la_speed_exact = new JLabel("Geschw.: ");
				la_speed_exact.setFont(new Font("Sans Serif", Font.BOLD, 12));

				tf_Speedx = new JTextField("0.0");
				tf_Speedx.setHorizontalAlignment(JTextField.CENTER);
				tf_Speedx.setEditable(false);
				tf_Speedx.setPreferredSize(dSizeSpeed);
				tf_Speedx.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Speedx.setText("x-Geschwindigkeit in m/s");
				
				tf_Speedy = new JTextField("0.0");
				tf_Speedy.setHorizontalAlignment(JTextField.CENTER);
				tf_Speedy.setEditable(false);
				tf_Speedy.setPreferredSize(dSizeSpeed);
				tf_Speedy.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Speedy.setText("y-Geschwindigkeit in m/s");
			    
				
				tf_Speedz = new JTextField("0.0");
				tf_Speedz.setHorizontalAlignment(JTextField.CENTER);
				tf_Speedz.setEditable(false);
				tf_Speedz.setPreferredSize(dSizeSpeed);
				tf_Speedz.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Speedz.setText("z-Geschwindigkeit in m/s");
	

				//Speed Slider
				JLabel la_speed_abs = new JLabel("Geschwindigkeitsbetrag: ");
				la_speed_exact.setFont(new Font("Sans Serif", Font.BOLD, 12));
	
				tf_Speed_abs = new JTextField("Geschwindigkeitsbetrag in m/s");
				tf_Speed_abs.setHorizontalAlignment(JTextField.CENTER);
				tf_Speed_abs.setEditable(false);
				tf_Speed_abs.setPreferredSize(dSizeSpeed);
				tf_Speed_abs.setFont(new Font("Sans Serif", Font.BOLD, 12));
				
/*				sl_Speed = new JSlider();
				sl_Speed.setPaintTicks(true);
				sl_Speed.setMinorTickSpacing(5);
				sl_Speed.setMajorTickSpacing(10);
				sl_Speed.setPaintLabels(true);
				Hashtable<Integer, JLabel> tableSpeed = new Hashtable<Integer, JLabel>();
				//tableSpeed.put( new Integer( 0 ), new JLabel("null") );
				//tableSpeed.put( new Integer( 12 ), new JLabel("ICE") );
				tableSpeed.put( new Integer( 40 ), new JLabel("Erde") );
				tableSpeed.put( new Integer( 100 ), new JLabel("Licht") );
				sl_Speed.setLabelTable(tableSpeed);
				sl_Speed.setValue(0);
				sl_Speed.setEnabled(false);
				
*/				
			    //Radius Slider + Eingabefeld
				JLabel lRadius = new JLabel("Radius: ");
				lRadius.setFont(new Font("Sans Serif", Font.BOLD, 12));
				
/*			    sl_Radius = new JSlider();
				sl_Radius.setPaintTicks(true);
				sl_Radius.setMinorTickSpacing(5);
				sl_Radius.setMajorTickSpacing(10);
				sl_Radius.setPaintLabels(true);
				Hashtable<Integer, JLabel> tableRadius = new Hashtable<Integer, JLabel>();
				//tableRadius.put( new Integer( 0 ), new JLabel("Schwarzes Loch") );
				tableRadius.put( new Integer( 12 ), new JLabel("Sonne") );
				//tableRadius.put( new Integer( 59 ), new JLabel("Jupiter-Orbit") );	//9.461E20 km
				//tableRadius.put( new Integer( 100 ), new JLabel("Galaxie") );	//long limit
				sl_Radius.setLabelTable(tableRadius);
				sl_Radius.setValue(1);
				sl_Radius.setEnabled(false);
*/	
				tf_Radius = new JTextField("0.0");
				tf_Radius.setHorizontalAlignment(JTextField.CENTER);
				tf_Radius.setEditable(false);
				tf_Radius.setPreferredSize(dSizeRadius);	
				tf_Radius.setFont(new Font("Sans Serif", Font.BOLD, 12));
				tf_Radius.setText("Radius in km");

				pa_comp_coords = new JPanel(new FlowLayout(FlowLayout.LEFT));
				pa_comp_coord_tf = new JPanel(new GridLayout(0,2));
				pa_comp_coord_tf.add(tf_Coordx);
				pa_comp_coord_tf.add(tf_Coordy);
				pa_comp_coord_tf.add(tf_Coordz);
				pa_comp_coords.add(la_Coord);
				pa_comp_coords.add(pa_comp_coord_tf);
				
				pa_comp_mass = new JPanel(new FlowLayout(FlowLayout.LEFT));
				pa_comp_mass.add(lMass);
				//pa_comp_mass.add(sl_Mass);
				pa_comp_mass.add(tf_Mass);
				
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
				//pa_comp_speed_abs.add(sl_Speed);
				pa_comp_speed_abs.add(tf_Speed_abs);
				
				pa_comp_radius = new JPanel(new FlowLayout(FlowLayout.LEFT));
				pa_comp_radius.add(lRadius);
				//pa_comp_radius.add(sl_Radius);
				pa_comp_radius.add(tf_Radius);
				
				pa_comp_middledata.add(pa_comp_coords);
				pa_comp_middledata.add(pa_comp_mass);
				pa_comp_middledata.add(pa_comp_speed_exact);
				pa_comp_middledata.add(pa_comp_speed_abs);
				pa_comp_middledata.add(pa_comp_radius);
				//pa_comp_middledata.add(la_Blackhole);
			
			add(pa_comp_middledata);
			
		//pa_comp_middle.add(pa_comp_middlebuttons);
			
		//add(pa_comp_middle);
	}
	
	public void repaintViews() {
		ov_front.repaint();
		ov_top.repaint();
	}

	public void ButtonsDeactive(boolean b) {
		cb_Objects.setEnabled(b);
	}
	public void TFColorStd() {
		tf_Coordx.setBackground(Color.WHITE);
		tf_Radius.setBackground(Color.WHITE);
		tf_Coordx.setBackground(Color.WHITE);
		tf_Coordy.setBackground(Color.WHITE);
		tf_Coordz.setBackground(Color.WHITE);
		tf_Speedx.setBackground(Color.WHITE);
		tf_Speedy.setBackground(Color.WHITE);
		tf_Speedz.setBackground(Color.WHITE);
		tf_Radius.setBackground(Color.WHITE);
		tf_Mass.setBackground(Color.WHITE);	
	}
	public void TFColorRem() {
		tf_Coordx.setBackground(null);
		tf_Radius.setBackground(null);
		tf_Coordx.setBackground(null);
		tf_Coordy.setBackground(null);
		tf_Coordz.setBackground(null);
		tf_Speedx.setBackground(null);
		tf_Speedy.setBackground(null);
		tf_Speedz.setBackground(null);
		tf_Radius.setBackground(null);
		tf_Mass.setBackground(null);	
	}
	
	public double pxtomm(int px) {
		return (px*Math.pow(10, ov_top.iZoomLevel))/ov_top.iGridOffset;
	}
	
	public double mmtopx(long mm) {
		return (mm*ov_top.iGridOffset/Math.pow(10, ov_top.iZoomLevel));
	}
	
/*	public void resetOffset() {
		ov_front.dCoordOffsetX = 0.0;
		ov_front.dCoordOffsetY = 0.0;
		ov_front.dCoordOffsetZ = 0.0;
		
		ov_top.dCoordOffsetX = 0.0;
		ov_top.dCoordOffsetY = 0.0;
		ov_top.dCoordOffsetZ = 0.0;
		repaintViews();
	}
	public void displayStep(Step nextStep) {
		ov_front.displayStep(nextStep);
		ov_top.displayStep(nextStep);
	}
	
	public void setGridColor(Color newColor) {
		ov_front.coGridColor = newColor;
		ov_top.coGridColor = newColor;
		repaintViews();
	}
	
	public void setObjectColor(Color newColor) {
		ov_front.coSpeedvecColor = newColor;
		ov_top.coSpeedvecColor = newColor;
		repaintViews();		
	}
*/	
	public void UpdatePanels(Masspoint_Sim mpsim) {
		Masspoint mp = new Masspoint(mpsim.getID(),mpsim.getPosX(),mpsim.getPosY(),mpsim.getPosZ());
		mp.setSpeedx(mpsim.getSpeedX());
		mp.setSpeedy(mpsim.getSpeedY());
		mp.setSpeedz(mpsim.getSpeedZ());
		mp.setMass(mpsim.getMass());
		mp.setRadius(mpsim.getRadius());
		UpdatePanels(mp);
	}
	
	public void UpdatePanels(Masspoint mp) {
		if(mp == null) {
			cb_Objects.setSelectedItem(new String("MP"));
			//debugout("UpdatePanels() - Sorry");
			return;
		}
		
		MDVector mvspeed = mp.getMDVSpeed();
		MLVector mvcoord = mp.getCoordMLV();
		
		
		tf_Coordx.setText(String.valueOf(mvcoord.x1)+" cm");
		tf_Coordy.setText(String.valueOf(mvcoord.x2)+" cm");
		tf_Coordz.setText(String.valueOf(mvcoord.x3)+" cm");
		tf_Speedx.setText(String.valueOf(mvspeed.x1)+" m/s");
		tf_Speedy.setText(String.valueOf(mvspeed.x2)+" m/s");
		tf_Speedz.setText(String.valueOf(mvspeed.x3)+" m/s");
		tf_Speed_abs.setText(String.valueOf(mp.getSpeed())+" m/s");
		tf_Mass.setText(String.valueOf(mp.getAbsMass())+" kg");
		tf_Radius.setText(String.valueOf(mp.getRadius()/CalcCode.RACCURACY)+" km");
		//sl_Speed.setValue((int)(100*Math.pow(mp.getSpeed()/CalcCode.LIGHTSPEED, 1.0/CalcCode.SSPEEDCONST)));
		//sl_Mass.setValue((int)(Math.pow(mp.getAbsMass(),1.0/CalcCode.SMASSCONST)));
		//sl_Radius.setValue((int)(Math.pow(mp.getRadius(),1.0/CalcCode.SRADIUSCONST)));

		
		if(mp.getRadius() < mp.getSchwarzschildRadius()) 
			la_Blackhole.setVisible(true);
		else if(la_Blackhole.isVisible()) 
			la_Blackhole.setVisible(false);
	}
}
