package jgravsim;

import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;

//import com.sun.java.util.jar.pack.Attribute.Layout;

public class TabVisualisationControls extends JPanel {
	private static final long serialVersionUID = 1L;

	private int iCurFrame = 0;
	private double dCurTime = 0;
	private int iMaxFrame;
	private double dMaxTime;
	private double dstep;
	XMLParser myXMLParser;
	
	JPanel pa_vis_controls; /* Kontrollelemente für die Visualisierung */
	JPanel pa_vis_contr_top;
		JButton b_loadfile;
		JButton b_loadlast;
		JButton b_unloadfile;
		
	JPanel pa_vis_contr_center;
		JPanel pa_vis_contr_center_panel1;
			JPanel pa_vis_contr_center_panel1_sub1;
				JLabel la_fpschooser;
				JSpinner sp_fpschooser;
			JPanel pa_vis_contr_center_panel1_sub2;
				JLabel la_curloaded;
		JPanel pa_vis_contr_center_paneltime;
			JLabel la_mscounter;
			JLabel la_scounter;
			JLabel la_mincounter;
			JLabel la_hcounter;
			JLabel la_dcounter;
			JLabel la_weekcounter;
			JLabel la_monthcounter;
			JLabel la_yearcounter;
			JLabel la_decadecounter;
			JLabel la_centurycounter;
		JPanel pa_vis_contr_center_panel2;
			JLabel la_zoomlevel;
			JSlider sl_zoomlevel;
		JPanel pa_vis_contr_center_panel3;
			JLabel la_gridoffset;
			JSlider sl_gridoffset;

	JPanel pa_vis_contr_bottom;
		JPanel pa_vis_contr_bottom_row1;
			JLabel la_framecounter;
	
		JPanel pa_vis_contr_bottom_row2;
			JSlider sl_playcontr_slider;
		JPanel pa_vis_contr_bottom_row3;
			JButton b_playcontr_start;
			JButton b_playcontr_stepback;
			JButton b_playcontr_play;
			JButton b_playcontr_stop;
			JButton b_playcontr_stepforw;
			JButton b_playcontr_end;
		JPanel pa_vis_contr_bottom_row4;
			JButton b_resetoffset;
	
	public TabVisualisationControls(XMLParser parser) {
		setLayout(new GridLayout(0,1));
		myXMLParser = parser;		

		b_loadfile = new JButton(myXMLParser.getText(300));
		b_loadlast = new JButton(myXMLParser.getText(301));
		b_unloadfile = new JButton(myXMLParser.getText(302));
		
		pa_vis_controls = new JPanel(new BorderLayout());
		pa_vis_controls.setBackground(Color.white);
		pa_vis_contr_top = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pa_vis_contr_top.setBackground(Color.white);

		pa_vis_contr_top.add(b_loadfile);
		pa_vis_contr_top.add(b_loadlast);
		pa_vis_contr_top.add(b_unloadfile);
		
		pa_vis_contr_center = new JPanel(new GridLayout(0,1));
		pa_vis_contr_center.setBackground(Color.white);
		
		pa_vis_contr_center_panel1 = new JPanel(new GridLayout(1,4));
		pa_vis_contr_center_panel1.setBackground(Color.white);
		

		pa_vis_contr_center_panel1_sub1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pa_vis_contr_center_panel1_sub1.setBackground(Color.white);
		
		SpinnerNumberModel spmodel = new SpinnerNumberModel(25,1,100,1);
		sp_fpschooser = new JSpinner(spmodel);
		pa_vis_contr_center_panel1_sub1.add(sp_fpschooser);
		
		la_fpschooser = new JLabel(myXMLParser.getText(310));
		pa_vis_contr_center_panel1_sub1.add(la_fpschooser);

		pa_vis_contr_center_panel1_sub2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pa_vis_contr_center_panel1_sub2.setBackground(Color.white);
		la_curloaded = new JLabel(myXMLParser.getText(303));

		pa_vis_contr_center_panel1_sub2.add(la_curloaded);

		pa_vis_contr_center_panel1.add(pa_vis_contr_center_panel1_sub1);
		pa_vis_contr_center_panel1.add(pa_vis_contr_center_panel1_sub2);

		pa_vis_contr_center_paneltime = new JPanel(new GridLayout(2,5));
		la_mscounter = new JLabel();
		la_scounter = new JLabel();
		la_mincounter = new JLabel();
		la_hcounter = new JLabel();
		la_dcounter = new JLabel();
		la_weekcounter = new JLabel();
		la_monthcounter = new JLabel();
		la_yearcounter = new JLabel();
		la_decadecounter = new JLabel();
		la_centurycounter = new JLabel();
		pa_vis_contr_center_paneltime.add(la_centurycounter);
		pa_vis_contr_center_paneltime.add(la_decadecounter);
		pa_vis_contr_center_paneltime.add(la_yearcounter);
		pa_vis_contr_center_paneltime.add(la_monthcounter);
		pa_vis_contr_center_paneltime.add(la_weekcounter);
		pa_vis_contr_center_paneltime.add(la_dcounter);
		pa_vis_contr_center_paneltime.add(la_hcounter);
		pa_vis_contr_center_paneltime.add(la_mincounter);
		pa_vis_contr_center_paneltime.add(la_scounter);
		pa_vis_contr_center_paneltime.add(la_mscounter);
		pa_vis_contr_center_paneltime.setBackground(Color.white);
		
		pa_vis_contr_center_panel2 = new JPanel(new GridLayout(2,0));
		pa_vis_contr_center_panel2.setBackground(Color.white);
		la_zoomlevel = new JLabel(myXMLParser.getText(101)+": ");
		sl_zoomlevel = new JSlider(TabVisualisation.ZOOM_MIN,TabVisualisation.ZOOM_MAX,130);
		sl_zoomlevel.setPaintTicks(true);
		sl_zoomlevel.setMinorTickSpacing(10);
		sl_zoomlevel.setMajorTickSpacing(50);
		sl_zoomlevel.setBackground(Color.white);
		
		pa_vis_contr_center_panel2.add(la_zoomlevel);
		pa_vis_contr_center_panel2.add(sl_zoomlevel);
		
		pa_vis_contr_center_panel3 = new JPanel(new GridLayout(2,0));
		pa_vis_contr_center_panel3.setBackground(Color.white);
		la_gridoffset = new JLabel(myXMLParser.getText(102)+": ");
		sl_gridoffset = new JSlider(15,200,25);
		sl_gridoffset.setPaintTicks(true);
		sl_gridoffset.setMinorTickSpacing(10);
		sl_gridoffset.setMajorTickSpacing(50);
		sl_gridoffset.setBackground(Color.white);
		pa_vis_contr_center_panel3.add(la_gridoffset);
		pa_vis_contr_center_panel3.add(sl_gridoffset);
				
		pa_vis_contr_center.add(pa_vis_contr_center_panel1);
		pa_vis_contr_center.add(pa_vis_contr_center_paneltime);
		pa_vis_contr_center.add(pa_vis_contr_center_panel2);
		pa_vis_contr_center.add(pa_vis_contr_center_panel3);
		
		pa_vis_contr_bottom = new JPanel(new GridLayout(4,0));
		pa_vis_contr_bottom.setBackground(Color.white);
		

		b_playcontr_start = new JButton("<<");
		b_playcontr_stepback = new JButton("<");
		b_playcontr_play = new JButton(myXMLParser.getText(311));
		b_playcontr_stop = new JButton(myXMLParser.getText(312));
		b_playcontr_stepforw = new JButton(">");
		b_playcontr_end = new JButton(">>");

		b_resetoffset = new JButton(myXMLParser.getText(105));
		
		pa_vis_contr_bottom_row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pa_vis_contr_bottom_row1.setBackground(Color.white);
		la_framecounter = new JLabel();
		pa_vis_contr_bottom_row1.add(la_framecounter);
		
		pa_vis_contr_bottom_row2 = new JPanel(new GridLayout());
		pa_vis_contr_bottom_row2.setBackground(Color.white);
		sl_playcontr_slider = new JSlider(0,749,0);
		sl_playcontr_slider.setPaintTicks(true);
		sl_playcontr_slider.setMajorTickSpacing(DynamicWPTLoader.STANDARDBUFFERSIZE);
		sl_playcontr_slider.setMinorTickSpacing(DynamicWPTLoader.STANDARDBUFFERSIZE/10);
		sl_playcontr_slider.setBackground(Color.white);
		pa_vis_contr_bottom_row2.add(sl_playcontr_slider);
				
		pa_vis_contr_bottom_row3 = new JPanel(new GridLayout());
		pa_vis_contr_bottom_row3.add(b_playcontr_start);
		pa_vis_contr_bottom_row3.add(b_playcontr_stepback);
		pa_vis_contr_bottom_row3.add(b_playcontr_play);
		pa_vis_contr_bottom_row3.add(b_playcontr_stop);
		pa_vis_contr_bottom_row3.add(b_playcontr_stepforw);
		pa_vis_contr_bottom_row3.add(b_playcontr_end);
		
		pa_vis_contr_bottom_row4 = new JPanel(new GridLayout());
		pa_vis_contr_bottom_row4.add(b_resetoffset);
		
		pa_vis_contr_bottom.add(pa_vis_contr_bottom_row1);
		pa_vis_contr_bottom.add(pa_vis_contr_bottom_row2);
		pa_vis_contr_bottom.add(pa_vis_contr_bottom_row3);
		pa_vis_contr_bottom.add(pa_vis_contr_bottom_row4);
		
		pa_vis_controls.add(pa_vis_contr_top,BorderLayout.NORTH);
		pa_vis_controls.add(pa_vis_contr_center,BorderLayout.CENTER);
		pa_vis_controls.add(pa_vis_contr_bottom,BorderLayout.SOUTH);
		
		
		pa_vis_controls.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(30)));
		
		add(pa_vis_controls);

	}
	public void setCurFrame(int frame) {
		sl_playcontr_slider.setValue(frame);
		
		iCurFrame = frame;
		dCurTime = ((double)frame)*dstep;
		updateFrameCounter();
	}
	
	public int getCurFrame() {
		return iCurFrame;
	}
	
	public int getMaxFrame() {
		return iMaxFrame;
	}
	
	public double getCurTime() {
		return dCurTime;
	}	
	
	public double getMaxTime() {
		return dMaxTime;
	}
	
	public void setTimeStep(double step) {
		dstep = step;
	}
	
	public void setMaxFrame(int maxframe) {
		sl_playcontr_slider.setMaximum(maxframe);
		
		iMaxFrame = maxframe;
		dMaxTime = ((double)maxframe)*dstep;
		updateFrameCounter();
	}
	
	public void updateFrameCounter() {
		la_framecounter.setText(myXMLParser.getText(313)+" "+iCurFrame+" "+myXMLParser.getText(314)+" "+iMaxFrame+" "+myXMLParser.getText(315));
		
		String unit = "ms";
		double factor = 1.0;
		double time = dCurTime;
		DecimalFormat df = new DecimalFormat("0");
		if(dCurTime == 0)
			time = dMaxTime;

		//if(dCurTime < 1000.0*12.0*4.0*7.0*24.0*60.0*60.0*1000.0)
		unit = myXMLParser.getText(478);
		if(time >= 100.0*365.25*24.0*3600.0) {
			factor = 100.0*365.25*24.0*3600.0;
			la_centurycounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_centurycounter.setText(df.format(0)+" "+unit);

		unit = myXMLParser.getText(477);
		if(time >= 10.0*365.25*24.0*3600.0) {
			factor = 10.0*365.25*24.0*3600.0;
			la_decadecounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_decadecounter.setText(df.format(0)+" "+unit);

		unit = myXMLParser.getText(476);
		if(time >= (365.25*24.0)*3600.0) {
			factor = (365.25*24.0)*3600.0;
			la_yearcounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_yearcounter.setText(df.format(0)+" "+unit);

		unit = myXMLParser.getText(475);
		if(time >= 365.25*24.0*3600.0/12.0) {
			factor = 365.25*24.0*3600.0/12.0;
			la_monthcounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_monthcounter.setText(df.format(0)+" "+unit);

		unit = myXMLParser.getText(474);
		if(time >= 7.0*24.0*60.0*60.0) {
			factor = 7.0*24.0*60.0*60.0;
			la_weekcounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_weekcounter.setText(df.format(0)+" "+unit);

		unit = myXMLParser.getText(473);
		if(time >= 24.0*60.0*60.0) {
			factor = 24.0*60.0*60.0;
			la_dcounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_dcounter.setText(df.format(0)+" "+unit);

		unit = myXMLParser.getText(472);
		if(time >= 60.0*60.0) {
			factor = 60.0*60.0;
			la_hcounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_hcounter.setText(df.format(0)+" "+unit);

		unit = myXMLParser.getText(471);
		if(time >= 60.0) {
			factor = 60.0;
			la_mincounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_mincounter.setText(df.format(0)+" "+unit);

		unit = myXMLParser.getText(470);
		if(time >= 1.0) {
			factor = 1.0;
			la_scounter.setText(df.format((time - (time % factor))/factor)+" "+unit);
			time = time % factor;
		}
		else
			la_scounter.setText(df.format(0)+" "+unit);

		unit = myXMLParser.getText(442)+myXMLParser.getText(470);
		df.applyPattern("0.######");
		if(time >= 1.0E-3) {
			//factor = 1.0;
			la_mscounter.setText(df.format(time)+" "+unit);
			//time = time % factor;
		}
		else
			la_mscounter.setText(df.format(0)+" "+unit);		
	}

	public void enableCounter(boolean b) {
		la_centurycounter.setVisible(b);
		la_decadecounter.setVisible(b);
		la_yearcounter.setVisible(b);
		la_monthcounter.setVisible(b);
		la_weekcounter.setVisible(b);
		la_dcounter.setVisible(b);
		la_hcounter.setVisible(b);
		la_mincounter.setVisible(b);
		la_scounter.setVisible(b);
		la_mscounter.setVisible(b);
		la_framecounter.setVisible(b);		
	}	
}
