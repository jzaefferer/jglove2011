package jGlove.monitor;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class GraphTest {

	/**
	 * Hinweis: Die Visualisierung eines Graphen ist noch recht fragil und unsicher.
	 * Allgemeine, bekannte Probleme:
	 * 	- bei Veränderungen der Größe des Rahmens, während das Programm läuft, wird eine IllegalArgumentException geworfen.
	 *  - Darstellungsfehler, wenn die Parameter von den Default-Einstellungen abweichen
	 */
	
//	private static int values = 50;
//	private static int resolution = 10; // Für Längenauflösung
//	private static int positioneerXScale = 20;
//	private static int positioneerYScale = 100;
	
	/**
	 * Anmerkung zu den Variablen:
	 * 
	 * Die Scale-Variablen dienen nur zur Darstellung der Grösse des Rasters.
	 * 
	 * Die resolution-Variable dient der "Längen"-Auflösung. Als Werte können
	 * technisch bedingt nur 10er Potenzen genutzt werden.
	 * 
	 * Die values-Variable ist für den "Wusel"-Faktor zuständig und beschreibt
	 * die Anzahl der anzuzeigenden Werte (das Fenster wird entsprechend ver-
	 * größert oder verkleinert).
	 * Die Anzahl der Werte sollte aus technischen Gründen die Anzahl von 10
	 * nicht unterschreiten (bei Anzahl 0 wird das Programm abgebrochen).
	 * @param args
	 */
	
	public static void main(String[] args) {
		final Display display = Display.getDefault();
		final Shell shell = new Shell(display);
				
		shell.setText("GraphTest");
		shell.setSize(400,400);
		//shell.setSize(((values*resolution)+(positioneerXScale*2)-2),((positioneerYScale*2)+(positioneerXScale*4)+6));
//		shell.setSize(((values * resolution) + (positioneerXScale))+8,26+(positioneerYScale * 2) + (positioneerXScale * 2));
//		shell.setMinimumSize(200,200);
//		shell.setMinimumSize(((values * resolution) + (positioneerXScale))+8,26+(positioneerYScale * 2) + (positioneerXScale * 2));
//		shell.setLayout(new GridLayout());
//		shell.setSize(((vales*resolution)+(positioneerXScale*2)+2),((positioneerYScale*2)+(positioneerXScale*2))+21+positioneerXScale);
/*
		(values * resolution) + (positioneerXScale))+8 = 218
		24+(positioneerYScale * 2) + (positioneerXScale * 2) = 244 -> 216 / -18
*/
//		GraphThreadTest gt = new GraphThreadTest(shell,values);
		//g.grid();
//		gt.start();		
		
		shell.open();
		
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) display.sleep();
		}
//		gt.interrupt();
		display.dispose();
		
	}

}
