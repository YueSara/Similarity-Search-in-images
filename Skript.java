package projekt.simsearch;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.features2d.BFMatcher;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


import projekt.simsearch.FD;

public class Skript {
	/**
	 * durchläuft 6 Experimente je 3 Mal
	 * Experimente: mit Hamming & ORB: k = 100, k = 75, ohne Clustering
	 * 				mit Euklid & SURF: k = 100, k = 75, ohne Clustering (funktioniert SURF??)
	 * nach jedem Durchlauf neuen Ordner mit genauer Bezeichnung erstellen: z.B. "H_100_nr2_time"
	 * jede Laufzeit festhalten (im Ordnernamen?)
	 * erstmal nur für ein Suchbild
	 */
	
	public static void main(String[] args) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		int k100 = 100;
		int k75 = 75;
		int k0 = 0;
		int typeH = BFMatcher.BRUTEFORCE_HAMMING;
		int typeB = BFMatcher.BRUTEFORCE; 
		int iter1 = 1;
		int iter2 = 2;
		int iter3 = 3;
		Mat dst1 = new Mat();
		
		String pic = "sim_query_Gebirge_1.jpg";
		String img = "C:/Users/pankr/Desktop/Projekt/Query/" + pic;
		Mat matBlur = Imgcodecs.imread(img);
		Imgproc.blur(matBlur, dst1, new Size(10, 10), new Point(-1, -1));
		Imgcodecs.imwrite("C:/Users/pankr/Desktop/Projekt/Query/" + "Blur10" + pic, dst1);
		Imgproc.blur(matBlur, dst1, new Size(15, 15), new Point(-1, -1));
		Imgcodecs.imwrite("C:/Users/pankr/Desktop/Projekt/Query/" + "Blur15" + pic, dst1);
		Imgproc.blur(matBlur, dst1, new Size(20, 20), new Point(-1, -1));
		Imgcodecs.imwrite("C:/Users/pankr/Desktop/Projekt/Query/" + "Blur20" + pic, dst1);
		
		FD.start(k100, typeH, iter1, img);
		System.gc();
		FD.start(k100, typeH, iter2, img);
		System.gc();
		FD.start(k100, typeH, iter3, img);
		System.gc();
		FD.start(k75, typeH, iter1, img);
		System.gc();
		FD.start(k75, typeH, iter2, img);
		System.gc();
		FD.start(k75, typeH, iter3, img);
		System.gc();
		FD.start(k0, typeH, iter1, img);
		System.gc();
		FD.start(k0, typeH, iter2, img);
		System.gc();
		FD.start(k0, typeH, iter3, img);
		System.gc();
		FD.start(k100, typeB, iter1, img);
		System.gc();
		FD.start(k100, typeB, iter2, img);
		System.gc();
		FD.start(k100, typeB, iter3, img);
		System.gc();
		FD.start(k75, typeB, iter1, img);
		System.gc();
		FD.start(k75, typeB, iter2, img);
		System.gc();
		FD.start(k75, typeB, iter3, img);
		System.gc();
		FD.start(k0, typeB, iter1, img);
		System.gc();
		FD.start(k0, typeB, iter2, img);
		System.gc();
		FD.start(k0, typeB, iter3, img);
		System.gc();
		}
	
	
}