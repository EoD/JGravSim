package jgravsim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;


@SuppressWarnings("serial")
public class TabCompute extends JPanel {
	public static final int revision = 5;
	
	JButton b_edit;
	JPanel pa_comp_middle;
		JPanel pa_comp_middlebuttons;
			JPanel pa_comp_middlecontrols; /* Mittleres Panel f�r Buttons */
				//JLabel la_headline;					
				JPanel pa_comp_zoom;
					JLabel la_zoomlevel;
					JSlider sl_zoomlevel;
				JPanel pa_comp_grid;
					JLabel la_gridoffset;
					JSlider sl_gridoffset;
				JPanel pa_comp_option2line;
					JPanel pa_comp_coloroptions;
						JButton b_colorch_grid;
						JButton b_colorch_speedvec;
						JButton b_resetoffset;
					JPanel pa_comp_chboptions;
						JCheckBox chb_vvector;
						JCheckBox chb_mpids;

					JPanel pa_comp_mpdefault;
						JComboBox cb_mpdefaults;
						JButton b_setdefaults;
						JButton b_add;
				
			JPanel pa_comp_remove;
				JComboBox cb_Objects;
				JButton b_Remove;	/* Button to delete mp */
				JButton b_clone;	/* Button to clone mp */
				JButton b_savepreset;	/* Button to save mp */
				
			TabComputeEasy pa_compute_dataeasy;		
			TabComputeAdvanced pa_compute_dataadvanced;
			//TabComputeInfos pa_compute_datainformation;
					
			JTabbedPane tp_compute = new JTabbedPane(); /* Die 3 Data-Tabs */
	
		JPanel pa_comp_middleview;
			//ObjectView3D ov_top; /* Ansicht von Oben (x,y) */
			ObjectView2D ov_top; /* Ansicht von Oben (x,y) */
			ObjectView2D ov_front; /* Ansicht von Vorne (x,z) */
	
	JPanel pa_comp_bottombuttons; /* Unteres Panel f�r Buttons */
		JButton b_reset; /* Alle MPs l�schen */
		JButton b_compute; /* Berechnung starten */
		JButton b_stop; /* Berechnung stoppen */
		//JTextField tf_filename; /* Name der Outputdatei */
		JButton b_savefile; /* Button um Daten zu speichern */
		JButton b_loadfile; /* Button um Daten zu laden */
		
	XMLParser myXMLParser;
	
	public TabCompute(XMLParser parser) {
		myXMLParser = parser;
		setLayout(new BorderLayout());

		pa_compute_dataeasy = new TabComputeEasy(myXMLParser); /* Visualisierung berechneter Daten */
		pa_compute_dataadvanced = new TabComputeAdvanced(myXMLParser); /* Visualisierung berechneter Daten */
		//pa_compute_datainformation = new TabComputeInfos(myXMLParser); /* Visualisierung berechneter Daten */

	    b_edit = pa_compute_dataadvanced.b_edit;
		
		/* Mittlerer Bereich */
		pa_comp_middle = new JPanel(new GridLayout(0,2));
		
			//* View Bereiche *//
			pa_comp_middleview = new JPanel(new GridLayout(2,0));
			
			//ov_top = new ObjectView3D(null); //
			ov_top = new ObjectView2D("xy");
			ov_front = new ObjectView2D("xz");
			
			ov_top.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(111)));
			ov_front.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(112)));
			
			pa_comp_middleview.add(ov_top);
			//pa_comp_middleview.add(ov_test);
			pa_comp_middleview.add(ov_front);
			
		pa_comp_middle.add(pa_comp_middleview);
			
			//* Mittlere Buttons *//
			pa_comp_middlebuttons  = new JPanel();//new JPanel(new GridLayout(0,1));
			pa_comp_middlebuttons.setLayout(new BoxLayout(pa_comp_middlebuttons, BoxLayout.Y_AXIS));
			
				//* Mittlere Buttons - Controls *//
				pa_comp_middlecontrols = new JPanel(new GridLayout(7,1)); //new JPanel(new GridLayout(0,1));
				pa_comp_middlecontrols.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(10)));
				
				la_zoomlevel = new JLabel(myXMLParser.getText(101)+":\t");
				/* default value has to be set HERE, (TabCompute) setZoom doesn't support it yet */
				sl_zoomlevel = new JSlider(View.ZOOM_MIN, View.ZOOM_MAX, View.ZOOM_MAX - Math.round(View.ZOOM_DEFAULT/View.ZOOM_STEP) + View.ZOOM_MIN);
				sl_zoomlevel.setPaintTicks(true);
				sl_zoomlevel.setMinorTickSpacing(View.SL_MINTICK);
				sl_zoomlevel.setMajorTickSpacing(View.SL_MAJTICK);
	
				la_gridoffset = new JLabel(myXMLParser.getText(102)+":\t");
				/* default value has be set HERE, setGridOffset doesn't support changing JSlider (yet) */
				sl_gridoffset = new JSlider(View.GRID_MIN, View.GRID_MAX, View.GRID_DEFAULT);
				sl_gridoffset.setPaintTicks(true);
				sl_gridoffset.setMinorTickSpacing(View.SL_MINTICK);
				sl_gridoffset.setMajorTickSpacing(View.SL_MAJTICK);
				

				b_colorch_grid = new JButton(myXMLParser.getText(103));
				b_colorch_speedvec = new JButton(myXMLParser.getText(104));
				b_resetoffset = new JButton(myXMLParser.getText(105));
				
				//pa_comp_zoom = new JPanel(new GridLayout(2,0));
				//pa_comp_zoom.add(la_zoomlevel);
				//pa_comp_zoom.add(sl_zoomlevel);
				
				//pa_comp_grid = new JPanel(new GridLayout(2,0));
				//pa_comp_grid.add(la_gridoffset);
				//pa_comp_grid.add(sl_gridoffset);
	
				//pa_comp_option2line = new JPanel(new GridLayout(2,1));
	
					pa_comp_coloroptions = new JPanel(new GridLayout(1,0));
					pa_comp_coloroptions.add(b_colorch_grid);
					pa_comp_coloroptions.add(b_colorch_speedvec);
					pa_comp_coloroptions.add(b_resetoffset);
					//JLabel empty = new JLabel();
					//JLabel empty2 = new JLabel();
					//pa_comp_color.add(empty);
					//pa_comp_color.add(empty2);
	
					pa_comp_chboptions = new JPanel(new FlowLayout());
					chb_vvector = new JCheckBox(myXMLParser.getText(218), true);
					chb_mpids = new JCheckBox(myXMLParser.getText(219), true);
					pa_comp_chboptions.add(chb_vvector);
					pa_comp_chboptions.add(chb_mpids);
					
					//pa_comp_option2line.add(pa_comp_coloroptions);
					//pa_comp_option2line.add(pa_comp_chboptions);
				
			    //Oject-List / Remove Button
				JLabel la_ObjectList2 = new JLabel(myXMLParser.getText(226)+":");

				pa_comp_mpdefault = new JPanel(new FlowLayout());
				cb_mpdefaults = new JComboBox();

				Controller.mpdf_moon.setName( myXMLParser.getText(500) );
				Controller.mpdf_sun.setName( myXMLParser.getText(501) );
				Controller.mpdf_mercury.setName( myXMLParser.getText(502) );
				Controller.mpdf_venus.setName( myXMLParser.getText(503) );
				Controller.mpdf_earth.setName( myXMLParser.getText(504) );
				Controller.mpdf_mars.setName( myXMLParser.getText(505) );
				Controller.mpdf_jupiter.setName( myXMLParser.getText(506) );
				Controller.mpdf_saturn.setName( myXMLParser.getText(507) );
				Controller.mpdf_uranus.setName( myXMLParser.getText(508) );
				Controller.mpdf_neptune.setName( myXMLParser.getText(509) );
				Controller.mpdf_ceres.setName( myXMLParser.getText(510) );
				Controller.mpdf_pluto.setName( myXMLParser.getText(511) );
				Controller.mpdf_eris.setName( myXMLParser.getText(512) );
				Controller.mpdf_proxima_centauri.setName( myXMLParser.getText(513) );
				Controller.mpdf_alpha_centauri_a.setName( myXMLParser.getText(514) );
				Controller.mpdf_rigel.setName( myXMLParser.getText(515) );
				Controller.mpdf_zeta_puppis.setName( myXMLParser.getText(516) );
				Controller.mpdf_sirius_b.setName( myXMLParser.getText(517) );
				Controller.mpdf_cygnus_x1.setName( myXMLParser.getText(518) );

				cb_mpdefaults.addItem(Controller.mpdf_earth);
				cb_mpdefaults.addItem(Controller.mpdf_moon);
				cb_mpdefaults.addItem(Controller.mpdf_sun);
				cb_mpdefaults.addItem(Controller.mpdf_mercury);
				cb_mpdefaults.addItem(Controller.mpdf_venus);
				cb_mpdefaults.addItem(Controller.mpdf_mars);
				cb_mpdefaults.addItem(Controller.mpdf_jupiter);
				cb_mpdefaults.addItem(Controller.mpdf_saturn);
				cb_mpdefaults.addItem(Controller.mpdf_uranus);
				cb_mpdefaults.addItem(Controller.mpdf_neptune);
				cb_mpdefaults.addItem(Controller.mpdf_ceres);
				cb_mpdefaults.addItem(Controller.mpdf_pluto);
				cb_mpdefaults.addItem(Controller.mpdf_eris);
				cb_mpdefaults.addItem(Controller.mpdf_proxima_centauri);
				cb_mpdefaults.addItem(Controller.mpdf_alpha_centauri_a);
				cb_mpdefaults.addItem(Controller.mpdf_rigel);
				cb_mpdefaults.addItem(Controller.mpdf_zeta_puppis);
				cb_mpdefaults.addItem(Controller.mpdf_sirius_b);
				cb_mpdefaults.addItem(Controller.mpdf_cygnus_x1);
				
			    b_setdefaults = new JButton(myXMLParser.getText(214));
			    b_add = new JButton(myXMLParser.getText(227));
			   
			    pa_comp_mpdefault.add(la_ObjectList2);	
				pa_comp_mpdefault.add(cb_mpdefaults);	
				//pa_comp_mpdefault.add(b_setdefaults);	
				//pa_comp_mpdefault.add(b_add);

			    //Oject-List / Remove Button

				pa_comp_remove = new JPanel();
				pa_comp_remove.setLayout(new BoxLayout(pa_comp_remove, BoxLayout.Y_AXIS));
				pa_comp_remove.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(9)));
				
				JPanel pa_comp_remove_sub = new JPanel();
				JLabel la_ObjectList = new JLabel(myXMLParser.getText(309)+":");
				la_ObjectList.setFont( new Font("Arial", Font.BOLD, 17));
				cb_Objects = new JComboBox();
				cb_Objects.addItem(myXMLParser.getText(240));
			    b_Remove = new JButton(myXMLParser.getText(206));
			    b_clone = new JButton(myXMLParser.getText(253));
			    b_savepreset = new JButton(myXMLParser.getText(254));

			    pa_comp_remove_sub.add(la_ObjectList);	
			    pa_comp_remove_sub.add(cb_Objects);	
			    pa_comp_remove_sub.add(b_Remove);
			    pa_comp_remove_sub.add(b_clone);
			    pa_comp_remove_sub.add(b_savepreset);
			    pa_comp_remove.add(pa_comp_remove_sub);
				
				//pa_comp_remove.add(new JPanel());
				
			    pa_comp_middlecontrols.add(la_zoomlevel);
			    pa_comp_middlecontrols.add(sl_zoomlevel);
			    pa_comp_middlecontrols.add(la_gridoffset);
			    pa_comp_middlecontrols.add(sl_gridoffset);
			    pa_comp_middlecontrols.add(pa_comp_coloroptions);
			    pa_comp_middlecontrols.add(pa_comp_mpdefault);
			    pa_comp_middlecontrols.add(pa_comp_chboptions);
				/*pa_comp_middlecontrols.add(pa_comp_zoom);	
				pa_comp_middlecontrols.add(pa_comp_grid);	
				pa_comp_middlecontrols.add(pa_comp_option2line);	
				pa_comp_middlecontrols.add(pa_comp_mpdefault);
				*/
			tp_compute.addTab(myXMLParser.getText(250), pa_compute_dataeasy);
			tp_compute.addTab(myXMLParser.getText(251), pa_compute_dataadvanced);
			//tp_compute.addTab(myXMLParser.getText(252), pa_compute_datainformation);
			
			pa_comp_remove.add(tp_compute);

			pa_comp_middlebuttons.add(pa_comp_middlecontrols);
			pa_comp_middlebuttons.add(pa_comp_remove);
		
		pa_comp_middle.add(pa_comp_middlebuttons);
			
		add(pa_comp_middle);
		
		/* Untere Buttons ... */
		pa_comp_bottombuttons = new JPanel(new GridLayout(1,3));
		
		JPanel pa_comp_buttombuttons1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		b_reset = new JButton(myXMLParser.getText(201));
		pa_comp_buttombuttons1.add(b_reset);
		
		JPanel pa_comp_buttombuttons2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		b_compute = new JButton(myXMLParser.getText(202));
		b_stop = new JButton(myXMLParser.getText(203));
		pa_comp_buttombuttons2.add(b_compute);
		pa_comp_buttombuttons2.add(b_stop);
		
		JPanel pa_comp_buttombuttons3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		b_savefile = new JButton(myXMLParser.getText(204));
		b_loadfile = new JButton(myXMLParser.getText(205));
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
		pa_compute_dataeasy.tf_Coordx.setEnabled(false);
		pa_compute_dataeasy.tf_Coordy.setEnabled(false);
		pa_compute_dataeasy.tf_Coordz.setEnabled(false);
		pa_compute_dataeasy.tf_Speedx_exact.setEnabled(false);
		pa_compute_dataeasy.tf_Speedy_exact.setEnabled(false);
		pa_compute_dataeasy.tf_Speedz_exact.setEnabled(false);
		pa_compute_dataeasy.sl_Speed.setEnabled(false);
		pa_compute_dataeasy.tf_Speedabs.setEnabled(false);
		pa_compute_dataeasy.tf_Mass.setEnabled(false);
		pa_compute_dataeasy.sl_Mass.setEnabled(false);
		pa_compute_dataeasy.tf_Radius.setEnabled(false);
		pa_compute_dataeasy.sl_Radius.setEnabled(false);
		pa_compute_dataeasy.chb_Dense.setEnabled(false);
		//pa_compute_dataeasy.sl_Dense.setEnabled(false);
		pa_compute_dataeasy.tf_Dense.setEnabled(false);
		pa_compute_dataadvanced.tf_Coordx.setEnabled(false);
		pa_compute_dataadvanced.tf_Coordy.setEnabled(false);
		pa_compute_dataadvanced.tf_Coordz.setEnabled(false);
		//pa_compute_dataadvanced.tf_Speedx_direction.setEnabled(false);
		//pa_compute_dataadvanced.tf_Speedy_direction.setEnabled(false);
		//pa_compute_dataadvanced.tf_Speedz_direction.setEnabled(false);
		pa_compute_dataadvanced.tf_Speedx_exact.setEnabled(false);
		pa_compute_dataadvanced.tf_Speedy_exact.setEnabled(false);
		pa_compute_dataadvanced.tf_Speedz_exact.setEnabled(false);
		pa_compute_dataadvanced.tf_Speedabs.setEnabled(false);
		pa_compute_dataadvanced.tf_Mass.setEnabled(false);
		pa_compute_dataadvanced.tf_Radius.setEnabled(false);
		pa_compute_dataadvanced.tf_Dense.setEnabled(false);
		b_reset.setEnabled(false);
		b_Remove.setEnabled(false);
		b_clone.setEnabled(false);
		b_savepreset.setEnabled(false);
		b_compute.setEnabled(false);
		b_stop.setEnabled(false);
		//tf_filename.setEnabled(false);
		b_savefile.setEnabled(false);
		b_loadfile.setEnabled(true);
		b_edit.setEnabled(false);
		cb_Objects.setEnabled(false);
		
		ov_front.str_clickme = myXMLParser.getText(8);
		ov_top.str_clickme = myXMLParser.getText(8);
	}
	public void ButtonsEdit() {
		pa_compute_dataeasy.tf_Coordx.setEnabled(true);
		pa_compute_dataeasy.tf_Coordy.setEnabled(true);
		pa_compute_dataeasy.tf_Coordz.setEnabled(true);
		pa_compute_dataeasy.tf_Speedx_exact.setEnabled(true);
		pa_compute_dataeasy.tf_Speedy_exact.setEnabled(true);
		pa_compute_dataeasy.tf_Speedz_exact.setEnabled(true);
		pa_compute_dataeasy.tf_Speedabs.setEnabled(true);
		pa_compute_dataeasy.tf_Radius.setEnabled(true);
		pa_compute_dataeasy.tf_Mass.setEnabled(true);
		pa_compute_dataeasy.tf_Dense.setEnabled(true);
		
		pa_compute_dataeasy.sl_Speed.setEnabled(false);
		pa_compute_dataeasy.sl_Mass.setEnabled(false);
		pa_compute_dataeasy.sl_Radius.setEnabled(false);
		//pa_compute_dataeasy.sl_Dense.setEnabled(false);
		
		pa_compute_dataeasy.chb_Dense.setEnabled(true);
		
		pa_compute_dataadvanced.tf_Coordx.setEnabled(true);
		pa_compute_dataadvanced.tf_Coordy.setEnabled(true);
		pa_compute_dataadvanced.tf_Coordz.setEnabled(true);
		//pa_compute_dataadvanced.tf_Speedx_direction.setEnabled(true);
		//pa_compute_dataadvanced.tf_Speedy_direction.setEnabled(true);
		//pa_compute_dataadvanced.tf_Speedz_direction.setEnabled(true);
		pa_compute_dataadvanced.tf_Speedx_exact.setEnabled(true);
		pa_compute_dataadvanced.tf_Speedy_exact.setEnabled(true);
		pa_compute_dataadvanced.tf_Speedz_exact.setEnabled(true);
		pa_compute_dataadvanced.tf_Speedabs.setEnabled(true);
		pa_compute_dataadvanced.tf_Mass.setEnabled(true);
		pa_compute_dataadvanced.tf_Radius.setEnabled(true);
		pa_compute_dataadvanced.tf_Dense.setEnabled(true);
		
		pa_compute_dataadvanced.tf_Coordx.setEditable(true);
		pa_compute_dataadvanced.tf_Coordy.setEditable(true);
		pa_compute_dataadvanced.tf_Coordz.setEditable(true);
		//pa_compute_dataadvanced.tf_Speedx_direction.setEditable(false);
		//pa_compute_dataadvanced.tf_Speedy_direction.setEditable(false);
		//pa_compute_dataadvanced.tf_Speedz_direction.setEditable(false);
		pa_compute_dataadvanced.tf_Speedx_exact.setEditable(true);
		pa_compute_dataadvanced.tf_Speedy_exact.setEditable(true);
		pa_compute_dataadvanced.tf_Speedz_exact.setEditable(true);
		pa_compute_dataadvanced.tf_Speedabs.setEditable(false);
		pa_compute_dataadvanced.tf_Radius.setEditable(true);
		pa_compute_dataadvanced.tf_Mass.setEditable(true);	
		pa_compute_dataadvanced.tf_Dense.setEditable(false);
		
		b_reset.setEnabled(false);
		b_Remove.setEnabled(false);
		b_clone.setEnabled(false);
		b_savepreset.setEnabled(false);
		b_compute.setEnabled(false);
		b_stop.setEnabled(false);
		//tf_filename.setEnabled(false);
		b_savefile.setEnabled(false);
		b_loadfile.setEnabled(false);
		cb_Objects.setEnabled(false);
		b_edit.setEnabled(true);
	}
	public void ButtonsAni() {
		pa_compute_dataeasy.tf_Coordx.setEnabled(false);
		pa_compute_dataeasy.tf_Coordy.setEnabled(false);
		pa_compute_dataeasy.tf_Coordz.setEnabled(false);
		pa_compute_dataeasy.tf_Speedx_exact.setEnabled(false);
		pa_compute_dataeasy.tf_Speedy_exact.setEnabled(false);
		pa_compute_dataeasy.tf_Speedz_exact.setEnabled(false);
		pa_compute_dataeasy.tf_Speedabs.setEnabled(false);
		pa_compute_dataeasy.tf_Mass.setEnabled(false);
		pa_compute_dataeasy.tf_Radius.setEnabled(false);
		pa_compute_dataeasy.tf_Dense.setEnabled(false);
		
		pa_compute_dataadvanced.tf_Coordx.setEnabled(false);
		pa_compute_dataadvanced.tf_Coordy.setEnabled(false);
		pa_compute_dataadvanced.tf_Coordz.setEnabled(false);
		//pa_compute_dataadvanced.tf_Speedx_direction.setEnabled(false);
		//pa_compute_dataadvanced.tf_Speedy_direction.setEnabled(false);
		//pa_compute_dataadvanced.tf_Speedz_direction.setEnabled(false);
		pa_compute_dataadvanced.tf_Speedx_exact.setEnabled(false);
		pa_compute_dataadvanced.tf_Speedy_exact.setEnabled(false);
		pa_compute_dataadvanced.tf_Speedz_exact.setEnabled(false);
		pa_compute_dataadvanced.tf_Speedabs.setEnabled(false);
		pa_compute_dataadvanced.tf_Mass.setEnabled(false);
		pa_compute_dataadvanced.tf_Radius.setEnabled(false);
		pa_compute_dataeasy.sl_Speed.setEnabled(false);
		//pa_compute_dataeasy.sl_Dense.setEnabled(false);
		pa_compute_dataeasy.sl_Mass.setEnabled(false);
		pa_compute_dataeasy.sl_Radius.setEnabled(false);
		pa_compute_dataeasy.chb_Dense.setEnabled(true);
		//pa_compute_dataadvanced.tf_Dense.setEnabled(false);
		b_reset.setEnabled(false);
		b_Remove.setEnabled(false);
		b_clone.setEnabled(false);
		b_savepreset.setEnabled(false);
		b_compute.setEnabled(false);
		b_stop.setEnabled(true);
		//tf_filename.setEnabled(false);
		b_savefile.setEnabled(false);
		b_loadfile.setEnabled(false);
		b_edit.setEnabled(false);
		cb_Objects.setEnabled(false);
	}
	public void ButtonsStd() {
		ov_front.str_clickme = "";
		ov_top.str_clickme = "";
		pa_compute_dataeasy.tf_Coordx.setEnabled(true);
		pa_compute_dataeasy.tf_Coordy.setEnabled(true);
		pa_compute_dataeasy.tf_Coordz.setEnabled(true);
		pa_compute_dataeasy.tf_Speedx_exact.setEnabled(true);
		pa_compute_dataeasy.tf_Speedy_exact.setEnabled(true);
		pa_compute_dataeasy.tf_Speedz_exact.setEnabled(true);
		pa_compute_dataeasy.tf_Speedabs.setEnabled(true);
		pa_compute_dataeasy.tf_Mass.setEnabled(true);
		pa_compute_dataeasy.tf_Radius.setEnabled(true);
		pa_compute_dataeasy.tf_Dense.setEnabled(true);
		
		pa_compute_dataeasy.tf_Coordx.setEditable(false);
		pa_compute_dataeasy.tf_Coordy.setEditable(false);
		pa_compute_dataeasy.tf_Coordz.setEditable(false);
		pa_compute_dataeasy.tf_Speedx_exact.setEditable(false);
		pa_compute_dataeasy.tf_Speedy_exact.setEditable(false);
		pa_compute_dataeasy.tf_Speedz_exact.setEditable(false);
		pa_compute_dataeasy.tf_Speedabs.setEditable(false);
		pa_compute_dataeasy.tf_Radius.setEditable(false);
		pa_compute_dataeasy.tf_Dense.setEditable(false);
		
		pa_compute_dataadvanced.tf_Coordx.setEnabled(true);
		pa_compute_dataadvanced.tf_Coordy.setEnabled(true);
		pa_compute_dataadvanced.tf_Coordz.setEnabled(true);
		//pa_compute_dataadvanced.tf_Speedx_direction.setEnabled(true);
		//pa_compute_dataadvanced.tf_Speedy_direction.setEnabled(true);
		//pa_compute_dataadvanced.tf_Speedz_direction.setEnabled(true);
		pa_compute_dataadvanced.tf_Speedx_exact.setEnabled(true);
		pa_compute_dataadvanced.tf_Speedy_exact.setEnabled(true);
		pa_compute_dataadvanced.tf_Speedz_exact.setEnabled(true);
		pa_compute_dataadvanced.tf_Speedabs.setEnabled(true);
		pa_compute_dataadvanced.tf_Mass.setEnabled(true);
		pa_compute_dataadvanced.tf_Radius.setEnabled(true);
		pa_compute_dataadvanced.tf_Dense.setEnabled(true);
		
		pa_compute_dataadvanced.tf_Coordx.setEditable(false);
		pa_compute_dataadvanced.tf_Coordy.setEditable(false);
		pa_compute_dataadvanced.tf_Coordz.setEditable(false);
		//pa_compute_dataadvanced.tf_Speedx_direction.setEditable(false);
		//pa_compute_dataadvanced.tf_Speedy_direction.setEditable(false);
		//pa_compute_dataadvanced.tf_Speedz_direction.setEditable(false);
		pa_compute_dataadvanced.tf_Speedx_exact.setEditable(false);
		pa_compute_dataadvanced.tf_Speedy_exact.setEditable(false);
		pa_compute_dataadvanced.tf_Speedz_exact.setEditable(false);
		pa_compute_dataadvanced.tf_Speedabs.setEditable(false);
		pa_compute_dataadvanced.tf_Radius.setEditable(false);
		pa_compute_dataadvanced.tf_Mass.setEditable(false);	
		pa_compute_dataadvanced.tf_Dense.setEditable(false);
		
		TFColorRem();
		pa_compute_dataeasy.sl_Speed.setEnabled(true);
		pa_compute_dataeasy.sl_Mass.setEnabled(true);
		pa_compute_dataeasy.sl_Radius.setEnabled(true);
		//pa_compute_dataeasy.sl_Dense.setEnabled(false);
		pa_compute_dataeasy.chb_Dense.setEnabled(true);
		b_reset.setEnabled(true);
		b_Remove.setEnabled(true);
		b_clone.setEnabled(true);
		b_savepreset.setEnabled(true);
		
		b_compute.setEnabled(true);
		b_stop.setEnabled(false);
	    File f = new File(Model.FILE_TEMP);
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
		pa_compute_dataeasy.tf_Coordx.setEnabled(b);
		pa_compute_dataeasy.tf_Coordy.setEnabled(b);
		pa_compute_dataeasy.tf_Coordz.setEnabled(b);
		pa_compute_dataeasy.tf_Speedx_exact.setEnabled(b);
		pa_compute_dataeasy.tf_Speedy_exact.setEnabled(b);
		pa_compute_dataeasy.tf_Speedz_exact.setEnabled(b);
		pa_compute_dataadvanced.tf_Speedabs.setEnabled(b);
		pa_compute_dataeasy.tf_Mass.setEnabled(b);
		pa_compute_dataeasy.tf_Radius.setEnabled(b);
		pa_compute_dataeasy.tf_Dense.setEnabled(b);
		
		pa_compute_dataadvanced.tf_Coordx.setEnabled(b);
		pa_compute_dataadvanced.tf_Coordy.setEnabled(b);
		pa_compute_dataadvanced.tf_Coordz.setEnabled(b);
		//pa_compute_dataadvanced.tf_Speedx_direction.setEnabled(b);
		//pa_compute_dataadvanced.tf_Speedy_direction.setEnabled(b);
		//pa_compute_dataadvanced.tf_Speedz_direction.setEnabled(b);
		pa_compute_dataadvanced.tf_Speedx_exact.setEnabled(b);
		pa_compute_dataadvanced.tf_Speedy_exact.setEnabled(b);
		pa_compute_dataadvanced.tf_Speedz_exact.setEnabled(b);
		pa_compute_dataadvanced.tf_Speedabs.setEnabled(b);
		pa_compute_dataadvanced.tf_Mass.setEnabled(b);
		pa_compute_dataadvanced.tf_Radius.setEnabled(b);
		pa_compute_dataadvanced.tf_Dense.setEnabled(b);
		
		pa_compute_dataeasy.sl_Speed.setEnabled(b);
		//pa_compute_dataeasy.sl_Dense.setEnabled(false);
		pa_compute_dataeasy.sl_Mass.setEnabled(b);
		pa_compute_dataeasy.sl_Radius.setEnabled(b);
		pa_compute_dataeasy.chb_Dense.setEnabled(b);
		
		sl_zoomlevel.setEnabled(b);
		sl_gridoffset.setEnabled(b);
		b_colorch_grid.setEnabled(b);
		b_colorch_speedvec.setEnabled(b);
		b_reset.setEnabled(b);
		b_Remove.setEnabled(b);
		b_clone.setEnabled(b);
		b_savepreset.setEnabled(b);
		b_compute.setEnabled(b);
		b_stop.setEnabled(b);
		b_savefile.setEnabled(b);
		b_loadfile.setEnabled(b);
		b_edit.setEnabled(b);
		cb_Objects.setEnabled(b);
	}
	public void TFColorStd() {
		pa_compute_dataeasy.tf_Coordx.setBackground(null);
		pa_compute_dataeasy.tf_Radius.setBackground(null);
		pa_compute_dataeasy.setBackground(null);
		pa_compute_dataeasy.tf_Coordy.setBackground(null);
		pa_compute_dataeasy.tf_Coordz.setBackground(null);
		pa_compute_dataeasy.tf_Speedx_exact.setBackground(null);
		pa_compute_dataeasy.tf_Speedy_exact.setBackground(null);
		pa_compute_dataeasy.tf_Speedz_exact.setBackground(null);
		pa_compute_dataeasy.tf_Radius.setBackground(null);
		pa_compute_dataeasy.tf_Mass.setBackground(null);	
		pa_compute_dataeasy.tf_Dense.setBackground(null);	
		
		pa_compute_dataadvanced.tf_Coordx.setBackground(Color.WHITE);
		pa_compute_dataadvanced.tf_Radius.setBackground(Color.WHITE);
		pa_compute_dataadvanced.tf_Coordx.setBackground(Color.WHITE);
		pa_compute_dataadvanced.tf_Coordy.setBackground(Color.WHITE);
		pa_compute_dataadvanced.tf_Coordz.setBackground(Color.WHITE);
		//pa_compute_dataadvanced.tf_Speedx_direction.setBackground(Color.WHITE);
		//pa_compute_dataadvanced.tf_Speedy_direction.setBackground(Color.WHITE);
		//pa_compute_dataadvanced.tf_Speedz_direction.setBackground(Color.WHITE);
		pa_compute_dataadvanced.tf_Speedx_exact.setBackground(null);
		pa_compute_dataadvanced.tf_Speedy_exact.setBackground(null);
		pa_compute_dataadvanced.tf_Speedz_exact.setBackground(null);
		pa_compute_dataadvanced.tf_Radius.setBackground(Color.WHITE);
		pa_compute_dataadvanced.tf_Mass.setBackground(Color.WHITE);	
		pa_compute_dataadvanced.tf_Dense.setBackground(null);
	}
	public void TFColorRem() {
		pa_compute_dataadvanced.tf_Coordx.setBackground(null);
		pa_compute_dataadvanced.tf_Radius.setBackground(null);
		pa_compute_dataadvanced.tf_Coordx.setBackground(null);
		pa_compute_dataadvanced.tf_Coordy.setBackground(null);
		pa_compute_dataadvanced.tf_Coordz.setBackground(null);
		//pa_compute_dataadvanced.tf_Speedx_direction.setBackground(null);
		//pa_compute_dataadvanced.tf_Speedy_direction.setBackground(null);
		//pa_compute_dataadvanced.tf_Speedz_direction.setBackground(null);
		pa_compute_dataadvanced.tf_Radius.setBackground(null);
		pa_compute_dataadvanced.tf_Mass.setBackground(null);	
	}
	
	public void setZoom(float zoomLevel) {
		ov_front.iZoomLevel = zoomLevel;
		ov_top.iZoomLevel = zoomLevel;
		DecimalFormat df = new DecimalFormat("0.0000");
		double zoomUnit = (Math.pow(10, (zoomLevel-3)));//*CalcCode.LACCURACY;
		la_zoomlevel.setText(myXMLParser.getText(101)+": "+zoomLevel+" (1 "+myXMLParser.getText(107)+" = "+ df.format(zoomUnit) + "km)");
		repaintViews();
	}
	
	public void setGridOffset(int gridOffset) {
		ov_front.iGridOffset = gridOffset;
		ov_top.iGridOffset = gridOffset;

		la_gridoffset.setText(myXMLParser.getText(102)+": "+gridOffset);
		repaintViews();
	}
	
	public void changeOffsetX(int deltaXpx) {
		//debugout("XOffset += "+deltaXpx+" (= "+pxtomm(deltaXpx)+"mm)");
		double deltaX = pxtomm(deltaXpx);
		ov_front.addCoordOffsetX(deltaX);
		ov_top.addCoordOffsetX(deltaX);
		repaintViews();
	}
	
	public void changeOffsetY(int deltaYpx) {
		//debugout("YOffset += "+deltaYpx+" (= "+pxtomm(deltaYpx)+"mm)");
		double deltaY = pxtomm(deltaYpx);
		ov_front.addCoordOffsetY(deltaY);
		ov_top.addCoordOffsetY(-deltaY);
		repaintViews();
	}
	
	public void changeOffsetZ(int deltaZpx) {
		//debugout("ZOffset += "+deltaZpx+" (= "+pxtomm(deltaZpx)+"mm)");
		double deltaZ = pxtomm(deltaZpx);
		ov_front.addCoordOffsetZ(-deltaZ);
		ov_top.addCoordOffsetZ(-deltaZ);
		repaintViews();
	}
	
	private double pxtomm(int px) {
		return MVMath.pxtod(px, ov_top.iZoomLevel, ov_top.iGridOffset);
	}
	
	public void resetOffset() {
		ov_front.resetCoordOffset();
		ov_top.resetCoordOffset();
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
	
	public void setSelectedMasspointAndRepaint(Masspoint mp) {
		setSelectedMasspoint(mp);
		repaintViews();		
	}
	public void setSelectedMasspoint(Masspoint mp) {
		ov_front.mp_selected = mp;
		ov_top.mp_selected = mp;
	}
	public void setSpeedvecEnabled(boolean state) {
		chb_vvector.setSelected(state);
		ov_front.draw_speed = state;
		ov_top.draw_speed = state;
	}
	public void setmpIDEnabled(boolean state) {
		chb_mpids.setSelected(state);
		ov_front.draw_strings = state;
		ov_top.draw_strings = state;
	}
}
