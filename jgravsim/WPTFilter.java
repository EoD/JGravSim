package jgravsim;

import java.io.File;
import javax.swing.filechooser.*;

public class WPTFilter extends FileFilter {

	private Controller myController;
	
	WPTFilter(Controller controller) {
		myController = controller;
	}
	
	public boolean accept(File file) {
		if(file.isDirectory())
			return true;

		String extension = null;
		String name = file.getName();
		int i = name.lastIndexOf('.');

		if(i > 0 && i < name.length() - 1)
			extension = name.substring(i + 1).toLowerCase();

		if(extension != null) {
			if(extension.equals(Model.FILE_ENDING))
				return true;
			else
				return false;
		}

		return false;
	}

	public String getDescription() {
		
		//provide backup method for problems
		if(myController == null)
			return Model.FILE_ENDING;
		else
			return myController.myView.myXMLParser.getText(179)+" v"+Controller.WPT_VERSION+" (*." + Model.FILE_ENDING + ")";
	}
}
