package gravity_sim;
import java.awt.*;
import javax.swing.*;


public class CalcView extends JFrame {
	private static final long serialVersionUID = 1L;

	Controller myController;
	JProgressBar myProgressBar;
	int Progress = 0;
	int DataCount;
	
	CalcView(int datacount, Controller MyController) {
		myController = MyController;
		DataCount = datacount;
		setLayout(new FlowLayout());
		setTitle("Rechnet ...");
		setSize(160,30);
		adjustWindow(this);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setUndecorated(true);
		myProgressBar = new JProgressBar(0,datacount);
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
		if(Progress >= DataCount) {
			setCursor(null);
			//this.dispose();
		}
	}
}
