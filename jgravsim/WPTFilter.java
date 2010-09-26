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

		int i = name.lastIndexOf(Model.FILE_ENDING);

		if (i > 0) {
			if (i == name.length() - Model.FILE_ENDING.length())
				return true;
			else if (i < name.length() - Model.FILE_ENDING.length() - 1) {
				/* take the string after "wpt" + "." (dot) */
				extension = name.substring(i + Model.FILE_ENDING.length() + 1).toLowerCase();
				if (extension.equals(Model.FILE_ENDING_GZIP)
						|| extension.equals(Model.FILE_ENDING_ZIP))
					return true;
			}
		}

		return false;
	}

	public String getDescription() {
		
		//provide backup method for problems
		if(myController == null)
			return Model.FILE_ENDING;
		else
			return myController.myView.myXMLParser.getText(179) + " v" + Controller.WPT_VERSION
					+ " (*." + Model.FILE_ENDING
					+ ", *." + Model.FILE_ENDING + "." + Model.FILE_ENDING_GZIP
					+ ", *." + Model.FILE_ENDING + "." + Model.FILE_ENDING_ZIP + ")";
	}
}
