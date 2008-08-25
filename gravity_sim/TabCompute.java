package gravity_sim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;


public class TabCompute extends JPanel {
	private static final long serialVersionUID = 1L;	
	//static final String sunitcoord = "mm";
	//static final String sunitspeed = "m/s";
	//static final String sunitmass = "g";
	static final String sheadline = "Daten über Massenpunkt";
	static final String top = "Oben";
	static final String front = "Vorne";

//	JPanel pa_comp_topbuttons; /* Oberes Panel für Hilfebutton */
//		JButton b_help;	
	
	JPanel pa_comp_middle;
		JPanel pa_comp_middlebuttons;
			JPanel pa_comp_middlecontrols; /* Mittleres Panel für Buttons */
				//JLabel la_headline;					
				JPanel pa_comp_zoom;
					JLabel la_zoomlevel;
					JSlider sl_zoomlevel;
				JPanel pa_comp_grid;
					JLabel la_gridoffset;
					JSlider sl_gridoffset;
				JPanel pa_comp_color;
					JButton b_colorch_grid = new JButton("Rasterfarbe");
					JButton b_colorch_speedvec = new JButton("v-Vektor Farbe");
					JButton b_resetoffset = new JButton("Verschiebung zurücksetzten");
						
				JPanel pa_comp_remove;
					JComboBox cb_Objects;
					JButton b_Remove;	/* Button um MP zu löschen */
					JButton b_edit;			
				JLabel la_Blackhole;
					
			JPanel pa_comp_middledata; /* Mittleres Panel für Buttons */
				JPanel pa_comp_coords;
					JLabel la_Coord;
					JPanel pa_comp_coord_tf;
						JTextField tf_Coordx;
						JTextField tf_Coordy;
						JTextField tf_Coordz;
				JPanel pa_comp_mass;
					JSlider sl_Mass;
					JTextField tf_Mass;
				JPanel pa_comp_speed_exact;
					JLabel la_speed_exact;
					JPanel pa_comp_coord_exact_tf;
						JTextField tf_Speedx;
						JTextField tf_Speedy;
						JTextField tf_Speedz;
				JPanel pa_comp_speed_abs;
					JSlider sl_Speed;
				JPanel pa_comp_radius;
					JSlider sl_Radius;
					JTextField tf_Radius;
	
		JPanel pa_comp_middleview;
			ObjectView ov_top; /* Ansicht von Oben (x,y) */
			ObjectView ov_front; /* Ansicht von Vorne (x,z) */
	
	JPanel pa_comp_bottombuttons; /* Unteres Panel für Buttons */
		JButton b_reset; /* Alle MPs löschen */
		JButton b_compute; /* Berechnung starten */
		JButton b_stop; /* Berechnung stoppen */
		//JTextField tf_filename; /* Name der Outputdatei */
		JButton b_savefile; /* Button um Daten zu speichern */
		JButton b_loadfile; /* Button um Daten zu laden */
	
	public TabCompute() {
		setLayout(new BorderLayout());
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
		pa_comp_middle = new JPanel(new GridLayout(0,2));
		
			//* View Bereiche *//
			pa_comp_middleview = new JPanel(new GridLayout(2,0));
			ov_top = new ObjectView(top,"xy");
			ov_front = new ObjectView(front,"xz");
			
			ov_top.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),top));
			ov_front.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),front));
			
			pa_comp_middleview.add(ov_top);
			pa_comp_middleview.add(ov_front);
			
		pa_comp_middle.add(pa_comp_middleview);
			
			//* Mittlere Buttons *//
			pa_comp_middlebuttons  = new JPanel(new GridLayout(0,1));
			// TODO remove?
/*			Dimension dmiddlebuttons = new Dimension(pa_comp_middle.getHeight(),pa_comp_middle.getWidth()/2);
			pa_comp_middlebuttons.setSize(dmiddlebuttons);
			pa_comp_middlebuttons.setPreferredSize(dmiddlebuttons);
			pa_comp_middlebuttons.setMaximumSize(dmiddlebuttons);
			pa_comp_middlebuttons.setMinimumSize(dmiddlebuttons);
*/			
				//* Mittlere Buttons - Controls *//
				pa_comp_middlecontrols = new JPanel(new GridLayout(0,1)); //new JPanel(new GridLayout(0,1));
				pa_comp_middlecontrols.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Kontrollelemente"));
				// TODO remove?
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
			
				
				la_zoomlevel = new JLabel("Vergrößerung:  ");
				sl_zoomlevel = new JSlider(10,158,130);
				sl_zoomlevel.setPaintTicks(true);
				sl_zoomlevel.setMinorTickSpacing(10);
				sl_zoomlevel.setMajorTickSpacing(50);
	
				la_gridoffset = new JLabel("Rastergröße: ");
				sl_gridoffset = new JSlider(15,200,25);
				sl_gridoffset.setPaintTicks(true);
				sl_gridoffset.setMinorTickSpacing(10);
				sl_gridoffset.setMajorTickSpacing(50);
				
				pa_comp_zoom = new JPanel(new GridLayout(2,0));
				pa_comp_zoom.add(la_zoomlevel);
				pa_comp_zoom.add(sl_zoomlevel);
				
				pa_comp_grid = new JPanel(new GridLayout(2,0));
				pa_comp_grid.add(la_gridoffset);
				pa_comp_grid.add(sl_gridoffset);
	
				pa_comp_color = new JPanel(new GridLayout(2,3));
				pa_comp_color.add(b_colorch_grid);
				pa_comp_color.add(b_colorch_speedvec);
				pa_comp_color.add(b_resetoffset);
				JLabel empty = new JLabel();
				JLabel empty2 = new JLabel();
				pa_comp_color.add(empty);
				pa_comp_color.add(empty2);
				
				
				la_Blackhole = new JLabel(" Schwarzes Loch ");
				la_Blackhole.setBorder(new BevelBorder(BevelBorder.RAISED));
				la_Blackhole.setHorizontalAlignment(JLabel.CENTER);
				la_Blackhole.setFont(new Font(null, Font.BOLD, 16));
				la_Blackhole.setVisible(false);
				
			    //Oject-List / Remove Button
				JLabel la_ObjectList = new JLabel("Massenpunkte:");
				cb_Objects = new JComboBox();
				cb_Objects.addItem(" MP Liste ");
			    b_Remove = new JButton("Entfernen");
				b_edit = new JButton("Werte bearbeiten");	
				
				pa_comp_remove = new JPanel(new FlowLayout());
				pa_comp_remove.add(la_ObjectList);	
				pa_comp_remove.add(cb_Objects);	
				pa_comp_remove.add(b_Remove);
				pa_comp_remove.add(b_edit);
				
				//pa_comp_middlecontrols.add(la_headline);
				pa_comp_middlecontrols.add(pa_comp_zoom);	
				pa_comp_middlecontrols.add(pa_comp_grid);	
				pa_comp_middlecontrols.add(pa_comp_color);	
				pa_comp_middlecontrols.add(la_Blackhole);	
				pa_comp_middlecontrols.add(pa_comp_remove);
	
			pa_comp_middlebuttons.add(pa_comp_middlecontrols);
		
				//* Mittlere Buttons - Data *//
				pa_comp_middledata = new JPanel(new GridLayout(0,1));
				//pa_comp_middledata.setPreferredSize( new Dimension(2*this.getHeight(), this.getHeight()/2));
				pa_comp_middledata.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Daten"));
				
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
				
			    sl_Mass = new JSlider();
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
				
				tf_Mass = new JTextField("0.0");
				tf_Mass.setHorizontalAlignment(JTextField.CENTER);
				tf_Mass.setEditable(false);
//				tf_Mass.setMinimumSize(dSizeMass);
				tf_Mass.setPreferredSize(dSizeMass);	
//				tf_Mass.setBorder( new BevelBorder(BevelBorder.RAISED) );
				tf_Mass.setFont(new Font("Sans Serif", Font.BOLD, 12));
//***				tf_Mass.setEditable(true);
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
				
				sl_Speed = new JSlider();
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
				
				
			    //Radius Slider + Eingabefeld
				JLabel lRadius = new JLabel("Radius: ");
				lRadius.setFont(new Font("Sans Serif", Font.BOLD, 12));
				
			    sl_Radius = new JSlider();
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
				pa_comp_mass.add(sl_Mass);
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
				pa_comp_speed_abs.add(sl_Speed);
				
				pa_comp_radius = new JPanel(new FlowLayout(FlowLayout.LEFT));
				pa_comp_radius.add(lRadius);
				pa_comp_radius.add(sl_Radius);
				pa_comp_radius.add(tf_Radius);
				
				pa_comp_middledata.add(pa_comp_coords);
				pa_comp_middledata.add(pa_comp_mass);
				pa_comp_middledata.add(pa_comp_speed_exact);
				pa_comp_middledata.add(pa_comp_speed_abs);
				pa_comp_middledata.add(pa_comp_radius);
				//pa_comp_middledata.add(la_Blackhole);
			
			pa_comp_middlebuttons.add(pa_comp_middledata);
			
		pa_comp_middle.add(pa_comp_middlebuttons);
			
		add(pa_comp_middle);
		
		/* Untere Buttons ... */
		pa_comp_bottombuttons = new JPanel(new GridLayout(1,3));
		
		JPanel pa_comp_buttombuttons1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		b_reset = new JButton("Zurücksetzen");
		pa_comp_buttombuttons1.add(b_reset);
		
		JPanel pa_comp_buttombuttons2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		b_compute = new JButton("Berechnung starten");
		b_stop = new JButton("Stop");
		pa_comp_buttombuttons2.add(b_compute);
		pa_comp_buttombuttons2.add(b_stop);
		
		JPanel pa_comp_buttombuttons3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		b_savefile = new JButton("Berechnung speichern");
		b_loadfile = new JButton("Datensatz laden");
		pa_comp_buttombuttons3.add(b_savefile);
		pa_comp_buttombuttons3.add(b_loadfile);
		
		pa_comp_bottombuttons.add(pa_comp_buttombuttons1);
		pa_comp_bottombuttons.add(pa_comp_buttombuttons2);
		pa_comp_bottombuttons.add(pa_comp_buttombuttons3);
		add(pa_comp_bottombuttons,BorderLayout.SOUTH);
	}
	
	public void repaintViews() {
		ov_front.repaint();
		ov_top.repaint();
	}

	//Button-States
	public void ButtonsStart() {
		tf_Coordx.setEnabled(false);
		tf_Coordy.setEnabled(false);
		tf_Coordz.setEnabled(false);
		tf_Speedx.setEnabled(false);
		tf_Speedy.setEnabled(false);
		tf_Speedz.setEnabled(false);
		sl_Speed.setEnabled(false);
		tf_Mass.setEnabled(false);
		sl_Mass.setEnabled(false);
		tf_Radius.setEnabled(false);
		sl_Radius.setEnabled(false);
		b_reset.setEnabled(false);
		b_Remove.setEnabled(false);
		b_compute.setEnabled(false);
		b_stop.setEnabled(false);
		//tf_filename.setEnabled(false);
		b_savefile.setEnabled(false);
		b_loadfile.setEnabled(true);
		b_edit.setEnabled(false);
		cb_Objects.setEnabled(false);
		// TODO remove!
//		tf_Mass.setBackground(Color.RED);
//		tf_Radius.setBackground(Color.GREEN);
	}
	public void ButtonsEdit() {
		tf_Coordx.setEnabled(true);
		tf_Coordy.setEnabled(true);
		tf_Coordz.setEnabled(true);
		tf_Speedx.setEnabled(true);
		tf_Speedy.setEnabled(true);
		tf_Speedz.setEnabled(true);
		tf_Radius.setEnabled(true);
		tf_Mass.setEnabled(true);
		tf_Coordx.setEditable(true);
		tf_Coordy.setEditable(true);
		tf_Coordz.setEditable(true);
		tf_Speedx.setEditable(true);
		tf_Speedy.setEditable(true);
		tf_Speedz.setEditable(true);
		tf_Radius.setEditable(true);
		tf_Mass.setEditable(true);	
		sl_Speed.setEnabled(true);
		sl_Mass.setEnabled(true);
		sl_Radius.setEnabled(true);
		b_reset.setEnabled(false);
		b_Remove.setEnabled(false);
		b_compute.setEnabled(false);
		b_stop.setEnabled(false);
		//tf_filename.setEnabled(false);
		b_savefile.setEnabled(false);
		b_loadfile.setEnabled(false);
		cb_Objects.setEnabled(false);
		b_edit.setEnabled(true);
	}
	public void ButtonsAni() {
		tf_Coordx.setEnabled(false);
		tf_Coordy.setEnabled(false);
		tf_Coordz.setEnabled(false);
		tf_Speedx.setEnabled(false);
		tf_Speedy.setEnabled(false);
		tf_Speedz.setEnabled(false);
		tf_Mass.setEnabled(false);
		tf_Radius.setEnabled(false);
		sl_Speed.setEnabled(false);
		sl_Mass.setEnabled(false);
		sl_Radius.setEnabled(false);
		b_reset.setEnabled(false);
		b_Remove.setEnabled(false);
		b_compute.setEnabled(false);
		b_stop.setEnabled(true);
		//tf_filename.setEnabled(false);
		b_savefile.setEnabled(false);
		b_loadfile.setEnabled(false);
		b_edit.setEnabled(false);
		cb_Objects.setEnabled(false);
	}
	public void ButtonsStd() {
		tf_Coordx.setEnabled(true);
		tf_Coordy.setEnabled(true);
		tf_Coordz.setEnabled(true);
		tf_Speedx.setEnabled(true);
		tf_Speedy.setEnabled(true);
		tf_Speedz.setEnabled(true);
		tf_Mass.setEnabled(true);
		tf_Radius.setEnabled(true);
		tf_Coordx.setEditable(false);
		tf_Coordy.setEditable(false);
		tf_Coordz.setEditable(false);
		tf_Speedx.setEditable(false);
		tf_Speedy.setEditable(false);
		tf_Speedz.setEditable(false);
		tf_Radius.setEditable(false);
		TFColorRem();
		tf_Mass.setEditable(false);	
		sl_Speed.setEnabled(true);
		sl_Mass.setEnabled(true);
		sl_Radius.setEnabled(true);
		b_reset.setEnabled(true);
		b_Remove.setEnabled(true);
		b_compute.setEnabled(true);
		b_stop.setEnabled(false);
	    File f = new File(Model.Defaultname);
	    if (!f.exists()) {
	    	//debugout("ButtonsStd() - no such file or directory: "+Model.Defaultname);
			//tf_filename.setEnabled(false);
			b_savefile.setEnabled(false);
	    }
	    else {
			//tf_filename.setEnabled(true);
			b_savefile.setEnabled(true);
	    }
		b_loadfile.setEnabled(true);
		cb_Objects.setEnabled(true);
		b_edit.setEnabled(true);
		//b_edit.setText("Werte bearbeiten");
	}

	public void ButtonsDeactive(boolean b) {
		tf_Coordx.setEnabled(b);
		tf_Coordy.setEnabled(b);
		tf_Coordz.setEnabled(b);
		tf_Speedx.setEnabled(b);
		tf_Speedy.setEnabled(b);
		tf_Speedz.setEnabled(b);
		tf_Mass.setEnabled(b);
		tf_Radius.setEnabled(b);
		sl_zoomlevel.setEnabled(b);
		sl_gridoffset.setEnabled(b);
		sl_Speed.setEnabled(b);
		sl_Mass.setEnabled(b);
		sl_Radius.setEnabled(b);
		b_colorch_grid.setEnabled(b);
		b_colorch_speedvec.setEnabled(b);
		b_reset.setEnabled(b);
		b_Remove.setEnabled(b);
		b_compute.setEnabled(b);
		b_stop.setEnabled(b);
		b_savefile.setEnabled(b);
		b_loadfile.setEnabled(b);
		b_edit.setEnabled(b);
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
	
	public void setZoom(double zoomLevel) {
		ov_front.iZoomLevel = zoomLevel;
		ov_top.iZoomLevel = zoomLevel;
		DecimalFormat df = new DecimalFormat("0.0000");
		double zoomUnit = (Math.pow(10, (zoomLevel-3)));//*CalcCode.LACCURACY;
		la_zoomlevel.setText("Vergrößerung: "+zoomLevel+" (1 Einheit = "+ df.format(zoomUnit) + "km)");
		repaintViews();
	}
	
	public void setGridOffset(int gridOffset) {
		ov_front.iGridOffset = gridOffset;
		ov_top.iGridOffset = gridOffset;
		
		//***la_gridoffset.setText("Rastergröße: "+gridOffset);
		repaintViews();
	}
	
	public void changeOffsetX(int deltaXpx) {
		//System.out.println("XOffset += "+deltaXpx+" (= "+pxtomm(deltaXpx)+"mm)");
		double deltaX = pxtomm(deltaXpx);
		ov_front.dCoordOffsetX += deltaX;
		ov_top.dCoordOffsetX += deltaX;
		repaintViews();
	}
	
	public void changeOffsetY(int deltaYpx) {
		//System.out.println("YOffset += "+deltaYpx+" (= "+pxtomm(deltaYpx)+"mm)");
		double deltaY = pxtomm(deltaYpx);
		ov_front.dCoordOffsetY -= deltaY;
		ov_top.dCoordOffsetY -= deltaY;
		repaintViews();
	}
	
	public void changeOffsetZ(int deltaZpx) {
		//System.out.println("ZOffset += "+deltaZpx+" (= "+pxtomm(deltaZpx)+"mm)");
		double deltaZ = pxtomm(deltaZpx);
		ov_front.dCoordOffsetZ -= deltaZ;
		ov_top.dCoordOffsetZ -= deltaZ;
		repaintViews();
	}
	
	public double pxtomm(int px) {
		return (px*Math.pow(10, ov_top.iZoomLevel))/ov_top.iGridOffset;
	}
	
	public double mmtopx(long mm) {
		return (mm*ov_top.iGridOffset/Math.pow(10, ov_top.iZoomLevel));
	}
	
	public void resetOffset() {
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
}
