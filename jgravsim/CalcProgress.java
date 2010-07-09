package jgravsim;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CalcProgress extends Thread {

	private static final boolean DEBUG = false;
	boolean loop;
	Controller myController;
	Process pcalculation;
	String percentfile;

	
	private	static void debugout(String a) {
		if(Controller.CURRENTBUILD && DEBUG)
			System.out.println(a);
	}
	
	CalcProgress(Controller myController2, String percentfile2, Process calculationp) {
		myController = myController2;
		myController.startCalcProgress(99);
		percentfile = percentfile2;
		pcalculation = calculationp;
	}
	
	public int readfile(String percentfile) {
		try {
			FileReader fr;
			BufferedReader br;
			int j=0;

			String line;
			while (loop && j+1 < 100) {
				sleep(200);
				fr = new FileReader(percentfile);
				br = new BufferedReader(fr);
				int percent = 0;
				while((line = br.readLine()) != null) {
					percent = Integer.parseInt(line);
				}
				debugout("readfile() - j="+j+" of found percent="+percent);
				while(j < percent) {
					j++;
					myController.myView_CalcProgress.step();
				}
				br.close();
				fr.close();
			}
			
			if(j+1 >= 100)
				return 100;
			
		} catch(FileNotFoundException e) {
			debugout("readfile() - INFILE_FILENOTFOUND "+percentfile);
			return Model.INFILE_FILENOTFOUND;
		} catch(IOException e) {
			debugout("readfile() - INFILE_READERROR!");
			return Model.INFILE_READERROR;
		} catch (InterruptedException e) {
			debugout("readfile() - InterruptedException1!");
			return Model.INFILE_NOERROR;
		}
		debugout("readfile() - INFILE_NOERROR 2!");
		return Model.INFILE_NOERROR; /* no error detected */
	}
	
	public void run() {
		int error = CalcCode.UNKNOWN;
		debugout("CalcProgress() - I'm starting!");
		loop = true;
		while(loop) {
			try {
				sleep(250);				
				if(readfile(percentfile) == 100) {
					error = pcalculation.exitValue();
					break;
				}
			} catch (InterruptedException e) {
				debugout("run() - InterruptedException0!");
			} catch (IllegalThreadStateException e) {
				debugout("run() - IllegalThreadStateException!");
				break;
			}
		}
		if(loop) {
			debugout("run() - error = "+error);
			halt();
			myController.CalculationFinished(error);
		}
	}
	
	public void halt() {
		loop = false;
		debugout("halt() - CalcProgress will be halted!");
		output(pcalculation);
		pcalculation.destroy();
		myController.myModel.deleteFile(Model.FILE_PERCENT);
		debugout("C++ - Calculation killed!");
	}
	
	public void output(Process calculation) {
		debugout("C++ - Input ready?");
		try {
			InputStreamReader input = new InputStreamReader(calculation.getInputStream());
			BufferedReader in = new BufferedReader( input );
			debugout("C++ - Reading input!");
			String line;
			while ((line = in.readLine()) != null) {
				debugout(line);
			}
			in.close();
			input.close();
		} catch(Exception excep) {
			debugout(excep.getMessage());
		}
	}
}