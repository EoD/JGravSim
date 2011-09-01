package jgravsim;

import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;


@SuppressWarnings("serial")
public class TabVisualisation extends JPanel  {
	public static final int revision = 6;

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
	JFrame jf_visual_tabs;	/* Use a JFrame for tp_visual in order to have a "full" 3d view */
	
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
		if(b_enable3d) {
			setLayout(new GridLayout());
			/*
			 * The top and front visual tab are here for compatibility only.
			 * Should be removed in the future.
			 */
			ov_vis_top = new ObjectView2D("xy");
			ov_vis_front = new ObjectView2D("xz");
			ov_vis_right = new ObjectView3D(null);
		} else {
			setLayout(new GridLayout(2,2));
			ov_vis_top = new ObjectView2D("xy");
			ov_vis_front = new ObjectView2D("xz");
			ov_vis_right = new ObjectView2D("yz");
		}
		
		pa_visual_datatab = new TabVisualisationData(myXMLParser); /* Visualisierung berechneter Daten */
		pa_visual_contrtab = new TabVisualisationControls(myXMLParser); /* Visualisierung berechneter Daten */
		pa_visual_plottab = new TabVisualisationPlot(myXMLParser); /* Visualisierung berechneter Daten */
		pa_visual_optiontab = new TabVisualisationOptions(myXMLParser); /* Visualisierung berechneter Daten */
		
		if(!b_enable3d) {
			ov_vis_top.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(111)));
			ov_vis_front.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(112)));
			ov_vis_right.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),myXMLParser.getText(113)));
		}
		
		tp_visual.addTab(myXMLParser.getText(30), pa_visual_contrtab);
		tp_visual.addTab(myXMLParser.getText(31), pa_visual_datatab);
		tp_visual.addTab(myXMLParser.getText(32), pa_visual_plottab);
		tp_visual.addTab(myXMLParser.getText(33), pa_visual_optiontab);
	
		if(b_enable3d) {
			jf_visual_tabs = new JFrame();
			jf_visual_tabs.setTitle(myXMLParser.getText(3));
			jf_visual_tabs.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			jf_visual_tabs.add(tp_visual);
			jf_visual_tabs.setPreferredSize(new Dimension(View.WIDTH_VISUALFRAME, View.HEIGHT_VISUALFRAME));
			jf_visual_tabs.pack();
			jf_visual_tabs.setLocation(View.WIDTH_VIEW-jf_visual_tabs.getWidth(), View.HEIGHT_VISUALFRAME_OFFSET);
			jf_visual_tabs.setVisible(false);
		} else {
			add(ov_vis_top);
			add(tp_visual);
			add(ov_vis_front);
		}
		add(ov_vis_right);
		
		setPlayControlsEnabled(false);
	}

	public void setGridColor(Color newColor) {
		ov_vis_front.coGridColor = newColor;
		ov_vis_right.coGridColor = newColor;
		ov_vis_top.coGridColor = newColor;
		updateViews(false);
	}
	
	public void setObjectColor(Color newColor) {
		ov_vis_front.coSpeedvecColor = newColor;
		ov_vis_right.coSpeedvecColor = newColor;
		ov_vis_top.coSpeedvecColor = newColor;
		updateViews(false);		
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
		
		
		if(ov_vis_top.getCurrentStep() != null 
				&& ov_vis_top.getCurrentStep().getMasspoints() != null
				&& ov_vis_top.getCurrentStep().getMasspoints().length > 0) {

			/* In case we got only the initial "masspoint list" entry, remove it */
			if(pa_visual_datatab.cb_Objects.getItemCount() == 1) {
				if(pa_visual_datatab.cb_Objects.getItemAt(0).getClass() == String.class)
					pa_visual_datatab.cb_Objects.removeAllItems();
			}

			/* Sort the mp array, so we can speed up the search and we get an ordered list afterwards */
			ov_vis_top.getCurrentStep().sort();
			
			int id = -1;
			try {
				/* Try to getSelectedItem(), if it fails our list is empty so we can skip the first loop */
				Masspoint_Sim mp = (Masspoint_Sim) pa_visual_datatab.cb_Objects.getSelectedItem();
				if (mp != null)
					id = mp.getID();

				/* Check the cb_Objects if masspoint is been destroyed and remove it if in that case */
				for (int k = 0; k < pa_visual_datatab.cb_Objects.getItemCount(); k++) {
					boolean bsurvived = false;
					Masspoint_Sim mp_list = (Masspoint_Sim) pa_visual_datatab.cb_Objects.getItemAt(k);

					for (int i = 0; i < ov_vis_top.getCurrentStep().getMasspoints().length; i++) {
						Masspoint_Sim mp_temp = ov_vis_top.getCurrentStep().getMasspoints()[i];
						if (mp_list.getID() == mp_temp.getID()) {
							bsurvived = true;
							break;
						}
					}
					/* In case the item in the list can't be found in the current step, remove it */
					if (!bsurvived)
						pa_visual_datatab.cb_Objects.removeItemAt(k);
				}
			} catch (RuntimeException e) {
				Controller.debugout("updateCurFrame() - cb_Objects is empty");
			}



			/* Check the if all masspoint are already in the list, if not, add them */
			for (int i = 0; i < ov_vis_top.getCurrentStep().getMasspoints().length; i++) {
				boolean binlist = false;
				Masspoint_Sim mp_temp = ov_vis_top.getCurrentStep().getMasspoints()[i];

				/* If we hit our selected masspoint, skip the search, but update the mp data */
				if (mp_temp.getID() == id) {
					pa_visual_datatab.UpdatePanels(mp_temp);
					continue;
				}

				for (int k = 0; k < pa_visual_datatab.cb_Objects.getItemCount(); k++) {
					Masspoint_Sim mp_list = (Masspoint_Sim) pa_visual_datatab.cb_Objects.getItemAt(k);
					if (mp_list.getID() == mp_temp.getID()) {
						/* We found our masspoint, so search the next one */
						binlist = true;
						break;
					} else if (mp_list.getID() > mp_temp.getID()) {
						/*
						 * Due to our ordered masspoint list, we can break the search
						 * if we haven't found it yet and the current mp_list got a
						 * higher ID then the mp_temp. In that case, we squeeze the
						 * object before position k and tell the loop that we found it.
						 */
						pa_visual_datatab.cb_Objects.insertItemAt(mp_temp, k);
						binlist = true;
						break;
					}
				}
				/* In case we haven't found it, add the item to the end of the list */
				if (!binlist)
					pa_visual_datatab.cb_Objects.addItem(mp_temp);
			}
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
		updateViews(false);
	}
	
	public void setZoom(float zoomLevel, boolean bslider) {
		ov_vis_front.iZoomLevel = zoomLevel;
		ov_vis_right.iZoomLevel = zoomLevel;
		ov_vis_top.iZoomLevel = zoomLevel;
		DecimalFormat df = new DecimalFormat("0.000");
		double zoomUnit = (Math.pow(10, (zoomLevel-3)));
		pa_visual_contrtab.la_zoomlevel.setText(myXMLParser.getText(101)+": "+zoomLevel+" (1 "+myXMLParser.getText(107)+" = "+ df.format(zoomUnit) + "km)");

		if(bslider) {
			int newzoom = View.ZOOM_MAX - Math.round(ov_vis_front.iZoomLevel / View.ZOOM_STEP) + View.ZOOM_MIN;
			//Controller.debugout("setZoom() - Changed from "+	pa_visual_contrtab.sl_zoomlevel.getValue()+" to "+newzoom);
			pa_visual_contrtab.sl_zoomlevel.setValue((newzoom));
		}
		updateViews(false);
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
		updateViews(false);
	}
	
	public void changeOffsetX(int deltaXpx) {
		//Controller.debugout("XOffset += "+deltaXpx+" (= "+pxtomm(deltaXpx)+"mm)");
		double deltaX = pxtomm(deltaXpx);
		ov_vis_front.addCoordOffsetX(deltaX);
		ov_vis_top.addCoordOffsetX(deltaX);
		updateViews(false);
	}
	
	public void changeOffsetY(int deltaYpx) {
		//Controller.debugout("YOffset += "+deltaYpx+" (= "+pxtomm(deltaYpx)+"mm)");
		double deltaY = pxtomm(deltaYpx);
		ov_vis_top.addCoordOffsetY(deltaY);
		ov_vis_right.addCoordOffsetY(deltaY);
		updateViews(false);
	}
	
	public void changeOffsetZ(int deltaZpx) {
		//Controller.debugout("ZOffset += "+deltaZpx+" (= "+pxtomm(deltaZpx)+"mm)");
		double deltaZ = pxtomm(deltaZpx);
		ov_vis_front.addCoordOffsetZ(-deltaZ);
		ov_vis_right.addCoordOffsetZ(-deltaZ);
		updateViews(false);
	}
	
	private double pxtomm(int px) {
		return MVMath.pxtod(px, ov_vis_top.iZoomLevel, ov_vis_top.iGridOffset);
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
		updateViews(false);
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
			updateViews(false);
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
		/* We have to do both max and min independently as the offset will be consider in coordtopx */
		
		long radius = MVMath.ConvertToL(mps_current[0].getAbsRadius());
		long[] max = {mps_current[0].getPos(0)+radius, mps_current[0].getPos(1)+radius, mps_current[0].getPos(2)+radius};
		long[] min = {mps_current[0].getPos(0)-radius, mps_current[0].getPos(1)-radius, mps_current[0].getPos(2)-radius};
		
		/* We first want to know the maximum and minimum value of all coordinates with i=0 being our start values*/
		for(int i=1; i < mps_current.length; i++) {
			Masspoint_Sim mp_tmp = mps_current[i];
			long radius2 = MVMath.ConvertToL(mp_tmp.getAbsRadius());
			for(int j=0; j<3; j++) {
				if(max[j] < mp_tmp.getPos(j)+radius2)
					max[j] = mp_tmp.getPos(j)+radius2;
				else if(min[j] > mp_tmp.getPos(j)-radius2)
					min[j] = mp_tmp.getPos(j)-radius2;
			}
		}
		//Controller.debugout("autoresize() - max="+max[0]+", "+max[1]+", "+max[2]);
		//Controller.debugout("autoresize() - min="+min[0]+", "+min[1]+", "+min[2]);
		
		/*
		 * We are doing a more or less complicated algorithm in order to
		 * guarantee that our masspoints are all inside the ObjectView2D.
		 * 
		 * First of all we find the maximal point max(x,y,z) and the minimal
		 * point min=(x,y,z) and transform those values for all OVs to pixels 
		 * relative to their respective OV.
		 * 
		 * All ObjectView2Ds have their coordinate system with the (0,0) being
		 * the top left corner and (d_t.width,d_t.height) being the bottom right
		 * corner.
		 * 
		 * We now move the maximum x pixel coordinate -ov.width (to the left!) and 
		 * take the negative value of it:
		 * 		*this is positive if it has been inside the ObjectView
		 * 		*this is negative if it has been outside the ObjectView
		 * and we take the minimal x pixel coordinate
		 * 		*this is positive if it has been inside the ObjectView
		 * 		*this is negative if it has been outside the ObjectView
		 * 
		 * We now take the smallest x pixel coordinate (both min and max) of all OVs, 
		 * the one which was furthest out of all OVs and save this pixel coordinate 
		 * in px[0], together with the respective OV, axe, real coordinate (sup[0]).
		 * Repeat the same for y/px[1], but change max/min depending on the 
		 * horizontal/vertical axe.
		 * 
		 * The above procedure generates new points being outside/inside the
		 * ObjectView2D. Hence we can treat the new values as usual points and 
		 * also can compare them with "0".
		 * 
		 * With all the values we got from above, we start the real resizing.
		 */
		
		double[][] max_px = {	MVMath.coordtopx(new MLVector(max[0], max[1], max[2]), ov_vis_top.cAxes, (ObjectView2D)ov_vis_top),
								MVMath.coordtopx(new MLVector(max[0], max[1], max[2]), ov_vis_front.cAxes, (ObjectView2D)ov_vis_front),
								MVMath.coordtopx(new MLVector(max[0], max[1], max[2]), ov_vis_right.cAxes, (ObjectView2D)ov_vis_right)
		};

		double[][] min_px =	{	MVMath.coordtopx(new MLVector(min[0], min[1], min[2]), ov_vis_top.cAxes, (ObjectView2D)ov_vis_top),
								MVMath.coordtopx(new MLVector(min[0], min[1], min[2]), ov_vis_front.cAxes, (ObjectView2D)ov_vis_front),
								MVMath.coordtopx(new MLVector(min[0], min[1], min[2]), ov_vis_right.cAxes, (ObjectView2D)ov_vis_right)
		};
		
		
		/*
		Controller.debugout("autoresize() - max_px={ ("+max_px[0][0]+", "+max_px[0][1]+")," +
													"("+max_px[1][0]+", "+max_px[1][1]+")," +
													"("+max_px[2][0]+", "+max_px[2][1]+"),"	);
		
		Controller.debugout("autoresize() - min_px={ ("+min_px[0][0]+", "+min_px[0][1]+")," +
													"("+min_px[1][0]+", "+min_px[1][1]+")," +
													"("+min_px[2][0]+", "+min_px[2][1]+"),"	);
		*/

		/*
		 * Initial values!
		 * This px gets start values of top.x, top.y, front.y which is equal to (x,y,z)
		 * Therefore the axes have to be (0,1,1)
		 * sup gets the minimal values
		 * and the objectviews are (top,top,front)
		 */
		double[] px = {min_px[0][0],min_px[0][1],min_px[1][1]};
		int[] axe = {0,1,1};
		long[] sup = min.clone();
		ObjectView2D[] ovs = {(ObjectView2D)ov_vis_top, (ObjectView2D)ov_vis_top, (ObjectView2D)ov_vis_front};
		
		/*
		 * Try to find values of x,y,z which are furthest away from their respective ObjectView2D.
		 * Save 
		 * 	ovs:	ObjectView where it happens
		 * 	axe:	axe of the respective ObjectView
		 * 	px:		pixel 'coordinate' of that point relative to the ObjectView
		 * 			move positive pixels as described above (-x + ov.getSize(axe))
		 * 	sup:	coordinate of the point
		 */
		
		ObjectView2D ov_tmp;
		for(int i=0; i<max_px.length; i++) {
			if(i==0)
				ov_tmp = (ObjectView2D)ov_vis_top;
			else if(i==1)
				ov_tmp = (ObjectView2D)ov_vis_front;
			else if(i==2)
				ov_tmp = (ObjectView2D)ov_vis_right;
			else
				throw new IndexOutOfBoundsException("Tried to access "+i+" at a length of "+max_px.length+", but only 0,1,2 are allowed.");

			double max_px_fixed, min_px_fixed;
			long[] max_sup_fixed, min_sup_fixed;
			for (int k = 0; k < ov_tmp.cAxes.length; k++) {
				// horizontal axe (goes from 0 to ov.width)
				if (k == 0) {
					max_px_fixed = max_px[i][k];
					min_px_fixed = min_px[i][k];
					max_sup_fixed = max;
					min_sup_fixed = min;
				}
				// vertical axe (goes from ov.height to 0)
				else {
					max_px_fixed = min_px[i][k];
					min_px_fixed = max_px[i][k];
					max_sup_fixed = min;
					min_sup_fixed = max;
				}

				switch (ov_tmp.cAxes[k]) {
				case 'x':
					if (-max_px_fixed + ov_tmp.getSize('x') < px[0]) {
						ovs[0] = ov_tmp;
						axe[0] = k;
						sup[0] = max_sup_fixed[0];
						px[0]  = -max_px_fixed + ov_tmp.getSize('x');
					} else if (min_px_fixed < px[0]) {
						ovs[0] = ov_tmp;
						axe[0] = k;
						sup[0] = min_sup_fixed[0];
						px[0]  = min_px_fixed;
					}
					break;
				case 'y':
					if (-max_px_fixed + ov_tmp.getSize('y') < px[1]) {
						ovs[1] = ov_tmp;
						axe[1] = k;
						sup[1] = max_sup_fixed[1];
						px[1]  = -max_px_fixed + ov_tmp.getSize('y');
					} else if (min_px_fixed < px[1]) {
						ovs[1] = ov_tmp;
						axe[1] = k;
						sup[1] = min_sup_fixed[1];
						px[1]  = min_px_fixed;
					}
					break;
				case 'z':
					if (-max_px_fixed + ov_tmp.getSize('z') < px[2]) {
						ovs[2] = ov_tmp;
						axe[2] = k;
						sup[2] = max_sup_fixed[2];
						px[2]  = -max_px_fixed + ov_tmp.getSize('z');
					} else if (min_px_fixed < px[2]) {
						ovs[2] = ov_tmp;
						axe[2] = k;
						sup[2] = min_sup_fixed[2];
						px[2]  = min_px_fixed;
					}
					break;
				}
			}
		}
		//Controller.debugout("autoresize() - sup="+sup[0]+", "+sup[1]+", "+sup[2]);
		//Controller.debugout("autoresize() - px="+px[0]+", "+px[1]+", "+px[2]);
		
		/* 
		 * We  check if any of the px values is < 0 (= outside, see above)
		 * We then repeat the calculation for the sup point
		 */
		if(	px[0] < 0 || px[1] < 0 || px[2] < 0) {
			
			Controller.debugout("autoresize() - Starting resize calculation - zooming out");
			
			for(int i=0; i<3; i++) {				
				while( px[i] < 0 || -px[i]+ovs[i].getSize(ovs[i].cAxes[axe[i]]) < 0) {
					//Controller.debugout("autoresize() - checking for #"+i+" on the axe "+ovs[i].cAxes[axe[i]]+" on "+String.copyValueOf(ovs[i].cAxes));
					ovs[i].iZoomLevel += 0.1;
					px[i] = MVMath.coordtopx(new MLVector(sup[0], sup[1], sup[2]), ovs[i].cAxes, ovs[i])[axe[i]];
					//Controller.debugout("autoresize() - new px="+px[0]+", "+px[1]+", "+px[2]);
				}
				/* Save the new zoomlevel on all ObjectViews */
				ov_vis_top.iZoomLevel = ovs[i].iZoomLevel;
				ov_vis_front.iZoomLevel = ovs[i].iZoomLevel;
				ov_vis_right.iZoomLevel = ovs[i].iZoomLevel;
			}
		}
		//if all points are inside of a box of size (1.0-ZOOM_THRESHOLD) (=20%) of the view, zoom in
		else if (	px[0] > (1.0-ZOOM_THRESHOLD)*ovs[0].getSize(ovs[0].cAxes[axe[0]]) &&
					px[1] > (1.0-ZOOM_THRESHOLD)*ovs[1].getSize(ovs[1].cAxes[axe[1]]) &&
					px[2] > (1.0-ZOOM_THRESHOLD)*ovs[2].getSize(ovs[2].cAxes[axe[2]])
				) {
			
			Controller.debugout("autoresize() - Starting resize calculation - zooming in");
			
			double[] ovs_sizes = {
					ovs[0].getSize(ovs[0].cAxes[axe[0]]),
					ovs[1].getSize(ovs[1].cAxes[axe[1]]),
					ovs[2].getSize(ovs[2].cAxes[axe[2]]) };
	
			while(	px[0] > (1.0-ZOOM_THRESHOLD)*ovs_sizes[0] && px[0] < ZOOM_THRESHOLD*ovs_sizes[0] &&
					px[1] > (1.0-ZOOM_THRESHOLD)*ovs_sizes[1] && px[1] < ZOOM_THRESHOLD*ovs_sizes[1] &&
					px[2] > (1.0-ZOOM_THRESHOLD)*ovs_sizes[2] && px[2] < ZOOM_THRESHOLD*ovs_sizes[2]
					) {
				ov_vis_top.iZoomLevel -= 0.1;
				ov_vis_front.iZoomLevel -= 0.1;
				ov_vis_right.iZoomLevel -= 0.1;
				
				for(int i=0; i<3; i++) 
					px[i] = MVMath.coordtopx(new MLVector(sup[0], sup[1], sup[2]), ovs[i].cAxes, ovs[i])[axe[i]];
			}
		}
		
		Controller.debugout("autoresize() - Resize finished (new zoom="+ov_vis_right.iZoomLevel+")");
		setZoom(ov_vis_right.iZoomLevel);
	}
}
