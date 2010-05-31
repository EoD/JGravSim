package jgravsim;

import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;


public class TabVisualisation extends JPanel  {
	public static final long serialVersionUID = 42L;

	public static final int ZOOM_MAX = 162;
	public static final int ZOOM_MIN = CalcCode.SZOOMCONST;
	public static final float ZOOM_THRESHOLD = 0.8f;
	private int data_plot;
	private int dynamic_iCurMax;
	private DynamicWPTLoader loader_plot;
	
	public boolean b_enable3d;
	
	ObjectView ov_vis_top;
	ObjectView ov_vis_front;
	ObjectView ov_vis_right;
	
	TabVisualisationData pa_visual_datatab;		
	TabVisualisationControls pa_visual_contrtab;
	TabVisualisationPlot pa_visual_plottab;
	TabVisualisationOptions pa_visual_optiontab;
		
	JTabbedPane tp_visual = new JTabbedPane(); /* Die Tabs werden hierrin dargestellt ... */
	XMLParser myXMLParser;
	
	public TabVisualisation(XMLParser parser, boolean b_3d) {
		myXMLParser = parser;
		b_enable3d = b_3d;
		initializeVariables();
	}
	
	public TabVisualisation(XMLParser parser) {
		myXMLParser = parser;
		b_enable3d = false;
		initializeVariables();
	}
	
	private void initializeVariables() {
		setLayout(new GridLayout(2,2));
		
		if(b_enable3d) {
			ov_vis_top = new ObjectView2D("xy");
			ov_vis_front = new ObjectView2D("xz");
			ov_vis_right = new ObjectView3D(null);
		} else {
			ov_vis_top = new ObjectView2D("xy");
			ov_vis_front = new ObjectView2D("xz");
			ov_vis_right = new ObjectView2D("yz");
		}
		
		pa_visual_datatab = new TabVisualisationData(myXMLParser); /* Visualisierung berechneter Daten */
		pa_visual_contrtab = new TabVisualisationControls(myXMLParser); /* Visualisierung berechneter Daten */
		pa_visual_plottab = new TabVisualisationPlot(myXMLParser); /* Visualisierung berechneter Daten */
		pa_visual_optiontab = new TabVisualisationOptions(myXMLParser); /* Visualisierung berechneter Daten */
		
		ov_vis_top.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(111)));
		ov_vis_front.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(112)));
		ov_vis_right.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(113)));
		
		tp_visual.addTab(myXMLParser.getText(30), pa_visual_contrtab);
		tp_visual.addTab(myXMLParser.getText(31), pa_visual_datatab);
		tp_visual.addTab(myXMLParser.getText(32), pa_visual_plottab);
		tp_visual.addTab(myXMLParser.getText(33), pa_visual_optiontab);
		
		add(ov_vis_top);
		add(tp_visual);
		add(ov_vis_front);
		add(ov_vis_right);
		
		setPlayControlsEnabled(false);
	}

	public void setGridColor(Color newColor) {
		ov_vis_front.coGridColor = newColor;
		ov_vis_right.coGridColor = newColor;
		ov_vis_top.coGridColor = newColor;
		updateViews();
	}
	
	public void setObjectColor(Color newColor) {
		ov_vis_front.coSpeedvecColor = newColor;
		ov_vis_right.coSpeedvecColor = newColor;
		ov_vis_top.coSpeedvecColor = newColor;
		updateViews();		
	}
	
	public void updateViews() {
		updateCurFrame();
	}

	public void updateViews(boolean bresize) {
		if(bresize && pa_visual_optiontab.chb_autoresize.isSelected())
			autoresize();
		updateCurFrame();
	}
	public void repaintViews() {
		ov_vis_top.repaint();
		ov_vis_front.repaint();
		ov_vis_right.repaint();
	}
	public void setPlayControlsEnabled(boolean state) {
		pa_visual_contrtab.b_unloadfile.setEnabled(state);
		pa_visual_contrtab.sl_playcontr_slider.setEnabled(state);
		pa_visual_contrtab.sp_fpschooser.setEnabled(state);
		pa_visual_contrtab.b_playcontr_start.setEnabled(state);
		pa_visual_contrtab.b_playcontr_end.setEnabled(state);
		pa_visual_contrtab.b_playcontr_play.setEnabled(state);
		pa_visual_contrtab.b_playcontr_stepback.setEnabled(state);
		pa_visual_contrtab.b_playcontr_stepforw.setEnabled(state);
		pa_visual_contrtab.b_playcontr_stop.setEnabled(state);
		pa_visual_datatab.buttonsInactive(state);
		pa_visual_plottab.buttonsInactive(state);
	}
	
	private void updateCurFrame() {
		if(data_plot > 0)
			if(loader_plot.iCurMax > dynamic_iCurMax)
				drawPlot(loader_plot, data_plot, false);
		
		pa_visual_plottab.updateVisualplotoptions(ov_vis_top.getCurrentStep());
		ov_vis_top.repaint();
		ov_vis_front.repaint();
		ov_vis_right.repaint();
		pa_visual_datatab.cb_Objects.removeAllItems();
		
		if(ov_vis_top.getCurrentStep() != null 
				&& ov_vis_top.getCurrentStep().getMasspoints() != null
				&& ov_vis_top.getCurrentStep().getMasspoints().length > 0) {
			for(int i=0;i<ov_vis_top.getCurrentStep().getMasspoints().length;i++)
				pa_visual_datatab.cb_Objects.addItem(ov_vis_top.getCurrentStep().getMasspoints()[i]);
			
			pa_visual_datatab.cb_Objects.setSelectedIndex(0);
		}
	}
	
	public void setCurFrame(int frame) {
		pa_visual_contrtab.sl_playcontr_slider.setValue(frame);
		
		pa_visual_contrtab.setCurFrame(frame);
		pa_visual_contrtab.updateFrameCounter();
	}
	
	public int getCurFrame() {
		return pa_visual_contrtab.getCurFrame();
	}
	
	public int getMaxFrame() {
		return pa_visual_contrtab.getMaxFrame();
	}
	
	public double getCurTime() {
		return pa_visual_contrtab.getCurTime();
	}	
	
	public double getMaxTime() {
		return pa_visual_contrtab.getMaxTime();
	}
	
	public void setTimeStep(double step) {
		pa_visual_contrtab.setTimeStep(step);
	}
	
	public void setMaxFrame(int maxframe) {
		pa_visual_contrtab.sl_playcontr_slider.setMaximum(maxframe);
		
		pa_visual_contrtab.setMaxFrame(maxframe);
		pa_visual_contrtab.updateFrameCounter();
	}
	
	public void displayStep(Step nextStep) {
		ov_vis_front.displayStep(nextStep);
		ov_vis_right.displayStep(nextStep);
		ov_vis_top.displayStep(nextStep);
		updateCurFrame();
	}
	
	public void setZoom(float zoomLevel, boolean bslider) {
		ov_vis_front.iZoomLevel = zoomLevel;
		ov_vis_right.iZoomLevel = zoomLevel;
		ov_vis_top.iZoomLevel = zoomLevel;
		DecimalFormat df = new DecimalFormat("0.000");
		double zoomUnit = (Math.pow(10, (zoomLevel-3)));
		pa_visual_contrtab.la_zoomlevel.setText(myXMLParser.getText(101)+": "+zoomLevel+" (1 "+myXMLParser.getText(107)+" = "+ df.format(zoomUnit) + "km)");

		if(bslider) {
			int newzoom = ZOOM_MAX - Math.round(ov_vis_front.iZoomLevel*Controller.ZOOMLEVEL) + ZOOM_MIN;
			//Controller.debugout("setZoom() - Changed from "+	pa_visual_contrtab.sl_zoomlevel.getValue()+" to "+newzoom);
			pa_visual_contrtab.sl_zoomlevel.setValue((newzoom));
		}
		updateCurFrame();
	}

	public void setZoom(float zoomLevel) {
		setZoom(zoomLevel, true);
	}
	
	public void addZoom(float delta) {
		setZoom(ov_vis_front.iZoomLevel + delta, true);
	}
	
	public void setGridOffset(int gridOffset) {
		ov_vis_front.iGridOffset = gridOffset;
		ov_vis_right.iGridOffset = gridOffset;
		ov_vis_top.iGridOffset = gridOffset;
		
		pa_visual_contrtab.la_gridoffset.setText(myXMLParser.getText(102)+": "+gridOffset);
		updateCurFrame();
	}
	
	public void changeOffsetX(int deltaXpx) {
		//Controller.debugout("XOffset += "+deltaXpx+" (= "+pxtomm(deltaXpx)+"mm)");
		double deltaX = pxtomm(deltaXpx);
		ov_vis_front.addCoordOffsetX(deltaX);
		ov_vis_top.addCoordOffsetX(deltaX);
		updateCurFrame();
	}
	
	public void changeOffsetY(int deltaYpx) {
		//Controller.debugout("YOffset += "+deltaYpx+" (= "+pxtomm(deltaYpx)+"mm)");
		double deltaY = pxtomm(deltaYpx);
		ov_vis_top.addCoordOffsetY(-deltaY);
		ov_vis_right.addCoordOffsetY(deltaY);
		updateCurFrame();
	}
	
	public void changeOffsetZ(int deltaZpx) {
		//Controller.debugout("ZOffset += "+deltaZpx+" (= "+pxtomm(deltaZpx)+"mm)");
		double deltaZ = pxtomm(deltaZpx);
		ov_vis_front.addCoordOffsetZ(-deltaZ);
		ov_vis_right.addCoordOffsetZ(-deltaZ);
		updateCurFrame();
	}
	
	public double pxtomm(int px) {
		return (px*Math.pow(10, ov_vis_top.iZoomLevel))/ov_vis_top.iGridOffset;
	}
	
	public void resetOffset() {
		ov_vis_front.resetCoordOffset();
		ov_vis_right.resetCoordOffset();
		ov_vis_top.resetCoordOffset();
		updateCurFrame();
	}

	public void enableCounter(boolean b) {
		pa_visual_contrtab.la_centurycounter.setVisible(b);
		pa_visual_contrtab.la_decadecounter.setVisible(b);
		pa_visual_contrtab.la_yearcounter.setVisible(b);
		pa_visual_contrtab.la_monthcounter.setVisible(b);
		pa_visual_contrtab.la_weekcounter.setVisible(b);
		pa_visual_contrtab.la_dcounter.setVisible(b);
		pa_visual_contrtab.la_hcounter.setVisible(b);
		pa_visual_contrtab.la_mincounter.setVisible(b);
		pa_visual_contrtab.la_scounter.setVisible(b);
		pa_visual_contrtab.la_mscounter.setVisible(b);
		pa_visual_contrtab.la_framecounter.setVisible(b);		
	}

	public void removePlot() {
		pa_visual_plottab.cb_plot_dense.setSelectedIndex(3);
		ov_vis_top.alldots = null;
		ov_vis_front.alldots = null;
		ov_vis_right.alldots = null;
		updateViews();
	}
	public void drawPlot(DynamicWPTLoader dynamicLoader, int data, boolean repaint) {
		data_plot = data;
		loader_plot = dynamicLoader;
		dynamic_iCurMax = dynamicLoader.iCurMax;
		int dynamic_iCurMin;
		int steps = dynamic_iCurMax;
		
		if(repaint)
			dynamic_iCurMin = getCurFrame();
		else
			dynamic_iCurMin = dynamicLoader.iCurMin;
		
		steps -= dynamic_iCurMin;

		if(getMaxFrame() > DynamicWPTLoader.STANDARDBUFFERSIZE && repaint) {
			JOptionPane.showMessageDialog(this,String.format(myXMLParser.getText(175),dynamic_iCurMin, dynamicLoader.iCurMax)
								,myXMLParser.getText(173),JOptionPane.INFORMATION_MESSAGE);
		}
		steps /= data;
		
		ov_vis_top.alldots   = 	new Step[steps];
		ov_vis_front.alldots = 	new Step[steps];
		ov_vis_right.alldots = 	new Step[steps];
		for(int k=0;k<steps;k++) {
			if(dynamic_iCurMin + k*data > dynamic_iCurMax)
				break;
			ov_vis_top.alldots[k] 	= dynamicLoader.getStep(dynamic_iCurMin + k*data);
			ov_vis_front.alldots[k] = dynamicLoader.getStep(dynamic_iCurMin + k*data);
			ov_vis_right.alldots[k] = dynamicLoader.getStep(dynamic_iCurMin + k*data);
		}
		pa_visual_plottab.cb_plot_dense.setSelectedIndex((int)Math.log10(data)+1);

		pa_visual_plottab.updateDrawStatus(dynamicLoader.steps, ov_vis_front.getCurrentStep());
		if(repaint)
			updateViews();
	}
	public void setSpeedvecEnabled(boolean state) {
		pa_visual_optiontab.chb_vvector.setSelected(state);
		ov_vis_front.draw_speed = state;
		ov_vis_right.draw_speed = state;
		ov_vis_top.draw_speed = state;
	}
	public void setmpIDEnabled(boolean state) {
		pa_visual_optiontab.chb_mpids.setSelected(state);
		ov_vis_front.draw_strings = state;
		ov_vis_right.draw_strings = state;
		ov_vis_top.draw_strings = state;
	}
	
	public boolean Is3dEnabled() {
		return b_enable3d;
	}
	
	/**
	 * Resizes the visual objectview2d windows automatically according to
	 * position and radiuses of all masspoints.<br><br>
	 * 
	 * If objects are outside of the view, it zooms out as long as some 
	 * objects are still outside.
	 * The value of <code>iZoomLevel</code> is 0.1+minimum.<br><br>
	 * 
	 * If objects are further in (<code>Dimensions*ZOOM_THRESHOLD</code>)
	 * zoom in as long as no object crossed the border.
	 * The value of <code>iZoomLevel</code> is minimum.
	 * 
     */
	public void autoresize() {
		
		if(Is3dEnabled()) {
			Controller.debugout("autoresize() - Not ready for 3d, yet. Disabling!");
			return;
		}
		
		Masspoint_Sim[] mps_current = ov_vis_top.getCurrentStep().getMasspoints();
		long max_x = 0, max_y = 0, max_z = 0;
		int size = mps_current.length;
		
		for(int i=0; i < size; i++) {
			Masspoint_Sim mp_tmp = mps_current[i];
			long radius2 = MVMath.ConvertToL(mp_tmp.getAbsRadius());
			
			if( Math.abs(mp_tmp.getPosX())+radius2 > max_x)
				max_x = Math.abs(mp_tmp.getPosX())+radius2;
			
			if( Math.abs(mp_tmp.getPosY())+radius2 > max_y)
				max_y = Math.abs(mp_tmp.getPosY())+radius2;
			
			if( Math.abs(mp_tmp.getPosZ())+radius2 > max_z)
				max_z = Math.abs(mp_tmp.getPosZ())+radius2;		
		}
		Dimension d_t = ov_vis_top.getSize();
		Controller.debugout("autoresize() - This are the dt: h="+d_t.height+", w="+d_t.width);
		Dimension d_f = ov_vis_front.getSize();
		Dimension d_r = ov_vis_right.getSize();
		
		double[] px_t = MVMath.coordtopx(new MLVector(max_x, max_y, max_z), ov_vis_top.cAxes, (ObjectView2D)ov_vis_top);
		double[] px_f = MVMath.coordtopx(new MLVector(max_x, max_y, max_z), ov_vis_front.cAxes, (ObjectView2D)ov_vis_front);
		double[] px_r= MVMath.coordtopx(new MLVector(max_x, max_y, max_z), ov_vis_right.cAxes, (ObjectView2D)ov_vis_right);

		//if any point is outside of the maximum width/height of the view, zoom out
		if(	px_t[1] > d_t.height || px_t[0] > d_t.width ||
			px_f[1] > d_f.height || px_f[0] > d_f.width ||
			px_r[1] > d_r.height || px_r[0] > d_r.width	) {
			
			Controller.debugout("autoresize() - Starting negative calculation");
	
			px_t = MVMath.coordtopx(new MLVector(max_x, max_y, max_z), ov_vis_top.cAxes, (ObjectView2D)ov_vis_top);
			while( px_t[1] > d_t.height || px_t[0] > d_t.width ) {
				px_t = MVMath.coordtopx(new MLVector(max_x, max_y, max_z), ov_vis_top.cAxes, (ObjectView2D)ov_vis_top);
				ov_vis_top.iZoomLevel += 0.1;
			}
			
			ov_vis_front.iZoomLevel = ov_vis_top.iZoomLevel;
			ov_vis_right.iZoomLevel = ov_vis_top.iZoomLevel;
			px_f = MVMath.coordtopx(new MLVector(max_x, max_y, max_z), ov_vis_front.cAxes, (ObjectView2D)ov_vis_front);
			while( px_f[1] > d_f.height || px_f[0] > d_f.width ) {
				px_f = MVMath.coordtopx(new MLVector(max_x, max_y, max_z), ov_vis_front.cAxes, (ObjectView2D)ov_vis_front);
				ov_vis_front.iZoomLevel += 0.1;
				
			}
	
			ov_vis_right.iZoomLevel = ov_vis_top.iZoomLevel;
			px_r= MVMath.coordtopx(new MLVector(max_x, max_y, max_z), ov_vis_right.cAxes, (ObjectView2D)ov_vis_right);
			while( px_r[1] > d_r.height || px_r[0] > d_r.width ) {
				px_r = MVMath.coordtopx(new MLVector(max_x, max_y, max_z), ov_vis_right.cAxes, (ObjectView2D)ov_vis_right);
				ov_vis_right.iZoomLevel += 0.1;
			}
		}
		//if all points are inside of ZOOM_THRESHOLD (=80%) of the view, zoom in
		else if (	px_t[1] < ZOOM_THRESHOLD*d_t.height && px_t[0] < ZOOM_THRESHOLD*d_t.width &&
					px_f[1] < ZOOM_THRESHOLD*d_f.height && px_f[0] < ZOOM_THRESHOLD*d_f.width &&
					px_r[1] < ZOOM_THRESHOLD*d_r.height && px_r[0] < ZOOM_THRESHOLD*d_r.width	) {
			
			Controller.debugout("autoresize() - Starting positive calculation.");
			d_t.setSize( ZOOM_THRESHOLD*d_t.width, ZOOM_THRESHOLD*d_t.height);
			d_f.setSize( ZOOM_THRESHOLD*d_f.width, ZOOM_THRESHOLD*d_f.height);
			d_r.setSize( ZOOM_THRESHOLD*d_r.width, ZOOM_THRESHOLD*d_r.height);
			
			px_t = MVMath.coordtopx(new MLVector(max_x, max_y, max_z), ov_vis_top.cAxes, (ObjectView2D)ov_vis_top);
			px_f = MVMath.coordtopx(new MLVector(max_x, max_y, max_z), ov_vis_front.cAxes, (ObjectView2D)ov_vis_front);
			px_r= MVMath.coordtopx(new MLVector(max_x, max_y, max_z), ov_vis_right.cAxes, (ObjectView2D)ov_vis_right);
			
			//do all at once in order to be sure all are still inside the ZOOM_THRESHOLD
			while(	px_t[1] < d_t.height && px_t[0] < d_t.width &&
					px_f[1] < d_f.height && px_f[0] < d_f.width &&
					px_r[1] < d_r.height && px_r[0] < d_r.width ) {
				ov_vis_top.iZoomLevel -= 0.1;
				ov_vis_front.iZoomLevel -= 0.1;
				ov_vis_right.iZoomLevel -= 0.1;
				px_t = MVMath.coordtopx(new MLVector(max_x, max_y, max_z), ov_vis_top.cAxes, (ObjectView2D)ov_vis_top);
				px_f = MVMath.coordtopx(new MLVector(max_x, max_y, max_z), ov_vis_front.cAxes, (ObjectView2D)ov_vis_front);
				px_r = MVMath.coordtopx(new MLVector(max_x, max_y, max_z), ov_vis_right.cAxes, (ObjectView2D)ov_vis_right);
			}
		}
		
		Controller.debugout("autoresize() - Resize finished (new zoom="+ov_vis_right.iZoomLevel+")");
		setZoom(ov_vis_right.iZoomLevel);
	}
}