package jgravsim;
import java.awt.*;
import javax.swing.*;


@SuppressWarnings("serial")
public class View_CalcProgress extends JFrame {
	public static final int revision = 1;

	Controller myController;
	JProgressBar myProgressBar;
	int Progress = 0;
	int DataCount = 0;
	
	View_CalcProgress(int idatacount, Controller MyController) {
		myController = MyController;
		DataCount = idatacount+1;
		setLayout(new FlowLayout());
		setTitle("Rechnet ...");
		setSize(160,30);
		adjustWindow(this);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setUndecorated(true);
		myProgressBar = new JProgressBar(0,idatacount);
		myProgressBar.setValue(0);
		myProgressBar.setStringPainted(true);
		add(myProgressBar);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setVisible(true);
	}
	
	void adjustWindow(Component frame) {	
		Point ViewPos = myController.myView.getLocation();
		Dimension ViewSize = myController.myView.getSize();
		Dimension frameSize = this.getSize();
        
		frame.setLocation(
                (2*ViewPos.x - frameSize.width  + ViewSize.width >> 1 ),
                (2*ViewPos.y - frameSize.height + ViewSize.height >> 1)
        );
	}
	
	void step() {
		myProgressBar.setValue(++Progress);
		Controller.debugout("Progress: "+Progress+" of "+DataCount);
		
		if(Progress >= DataCount) {
			setCursor(null);
			//this.dispose();
		}
	}
}
