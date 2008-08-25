package jgravsim;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class View_PlotOption extends JPanel implements ItemListener {
	private static final long serialVersionUID = 1L;
	public static boolean DEBUG = true;
	
	Masspoint_Sim mp_reference;
	String str_name;
	ButtonGroup g;
	JRadioButton rb_option0;
	JRadioButton rb_option1;
	JRadioButton rb_option2;
	XMLParser myXMLParser;
		
	View_PlotOption(Masspoint_Sim mp, XMLParser parser) {
		mp_reference = mp;
		myXMLParser = parser;
		if(mp == null) {
			debugout("VisualisationPlotOption - NULL masspoint");
			return;
		}
		
		JPanel pa_main = new JPanel();
		pa_main.setLayout(new GridLayout(1,3));
		
		rb_option0 = new JRadioButton( myXMLParser.getText(304) ); 
		rb_option1 = new JRadioButton( myXMLParser.getText(305) ); 
		rb_option2 = new JRadioButton( myXMLParser.getText(306) );
		switch(mp.getDrawStatus()) {
			case -1: rb_option2.setSelected(true); break;
			case  1: rb_option1.setSelected(true); break;
			default: rb_option0.setSelected(true); break;
		}
		rb_option0.addItemListener(this);
		rb_option1.addItemListener(this);
		rb_option2.addItemListener(this);
		
		g = new ButtonGroup(); 
		g.add( rb_option0 ); 
		g.add( rb_option1 ); 
		g.add( rb_option2 ); 
		
		if(mp_reference.getID() < 10)
			str_name = " ";
		else
			str_name = "";
		
		str_name = myXMLParser.getText(9)+" "+str_name+mp_reference.getID()+": ";
		JLabel la_name = new JLabel(str_name);
		pa_main.add(rb_option0);
		pa_main.add(rb_option1);
		pa_main.add(rb_option2);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(la_name);
		add(pa_main);
		setVisible(true);
	}
	
	private static void debugout(String a) {
		if(DEBUG && Controller.MAINDEBUG)
			System.out.println(a);
	}	
	
	@Override
	public String toString() {
		return str_name;
	}
	
	//@Override
	public void itemStateChanged(ItemEvent e) {
		JRadioButton source = (JRadioButton)e.getSource();
		  
		if(source == rb_option0 || source == rb_option1 || source == rb_option2) {
			//to avoid too much calls of setDrawStatus
			if(!source.isSelected())
				return;
			
			if(rb_option0.isSelected() && mp_reference.getDrawStatus() != 0) {
				mp_reference.setDrawStatus(0);
			} else if(rb_option1.isSelected() && mp_reference.getDrawStatus() != 1) {
				mp_reference.setDrawStatus(1);
			} else if(rb_option2.isSelected() && mp_reference.getDrawStatus() != -1) {
				mp_reference.setDrawStatus(-1);
			}
			
			debugout("itemStateChanged() - JRadioButton "+mp_reference.getID()+" changed. New selection is:"+mp_reference.getDrawStatus());
		}
		
	}
}
