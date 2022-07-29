package projekt.simsearch;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.TermCriteria;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.ORB;
import org.opencv.imgcodecs.*;



public class FD {


	public static void start(int cluster, int type, int iter, String img) {


		//edit filepaths
		int k = cluster;
		int i = 0;
		String img1 = img;
		String filefolder = "C:/Users/saray/Desktop/SimSearch/alle_bilder/";	
		String ausgabePfad = "C:/Users/saray/Desktop/SimSearch/ausgabepfad/";
		ORB detector = ORB.create();

		
		// Erstes Bild bearbeiten
		
		Mat mat1 = Imgcodecs.imread(img1);
		MatOfKeyPoint keysImg1 = new MatOfKeyPoint();
		detector.detect(mat1, keysImg1);
		Mat descriptors1 = new Mat();
		detector.detectAndCompute(mat1, new Mat(), keysImg1, descriptors1);
		Features2d.drawKeypoints(mat1, keysImg1, mat1);
        Mat descriptors3 = new Mat();
        descriptors1.convertTo(descriptors3, CvType.CV_32F);
             
        // bei k=100 oder 75 wird makeCluster aufgerufen, bei k=0 werden descriptors genommen
        Mat cluster1 = new Mat();
        if(k != 0) {
        	cluster1 = makeCluster(descriptors3, k);
        	}
        else{
        	cluster1 = descriptors3;
        	}
        
        // Überprüft ob clustern des query Bildes möglich war, ansonsten wird dir Durchlauf abgebrochen
        if(cluster1.type() == 5) {

		//all images from file
        File folder = new File(filefolder);
		File[] allFiles = folder.listFiles();
		
		List<String> namesOfFiles = new ArrayList<String>();
		
		//loop through all pictures
		
		System.out.println("Bilder eingelesen. Analysiere:");
		for(File file : allFiles){
			
			if (file.getName().contains("jpg") || file.getName().contains("JPG")){
			namesOfFiles.add(file.getName());
			}
		}
		
		List<Float> distances = new ArrayList<Float>();
		
		// alle Bilder des Ordners in Mats wandeln
		List<Mat> allImageMats = new ArrayList<Mat>();
		
			
		for(String imageName : namesOfFiles){
			allImageMats.add(Imgcodecs.imread(filefolder+imageName));
		}

		
		Map<Float, Mat> imagesWithDistances = new HashMap<Float, Mat>();
		
		//TIME start
		long startTime = System.currentTimeMillis();
		
		for(Mat mat2 : allImageMats){
			MatOfKeyPoint keysImg2 = new MatOfKeyPoint();
			detector.detect(mat2, keysImg2);
			Mat descriptors2 = new Mat();
			detector.detectAndCompute(mat2, new Mat(), keysImg2, descriptors2);
			Features2d.drawKeypoints(mat2, keysImg2, mat2);
	        Mat descriptors4 = new Mat();
	        descriptors2.convertTo(descriptors4, CvType.CV_32F);
	    
	        // bei k=100 oder 75 wird makeCluster aufgerufen, bei k=0 werden descriptors genommen
	        Mat cluster2 = new Mat();
	        if(k != 0) {
	        	cluster2 = makeCluster(descriptors4, k);
	        	}
	        else{
	        	cluster2 = descriptors4;
	        	}
			Mat imageWithMatches = new Mat();
			
	
			// Case: Descriptor Matching Standard 
			int matchertype = type;
			if(cluster2.type() == 5) {
			MatOfDMatch matches = matchDescriptorsStandard(cluster1, cluster2, matchertype);		
			
			Features2d.drawMatches(mat1, keysImg1, mat2, keysImg2, matches, imageWithMatches);
	
			List<DMatch> matchesList = matches.toList();
			float distSum = 0;
			for(DMatch dist : matchesList) {
				distSum += dist.distance;
			}
			
			imagesWithDistances.put(distSum, imageWithMatches);
			distances.add(distSum);
			distances.sort(null);
		
			
			System.out.print(".");	}
			else {
			
		    System.out.print("Can't match");
				
			}
			
		}
		System.out.println("Beendet");	
		System.out.println("Die Distanzen sind:" + distances);
		
		//TIME end
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
				
		// gibt die besten 10 Bilder aus, je nach Durchlauf in neuen Ordnern mit genauer Bezeichnung, z.B.: "H_100_nr2_time/bild4.jpeg"
		int iteration = iter;
		while(i < 10){
			if(type==BFMatcher.BRUTEFORCE_HAMMING) {
				Imgcodecs.imwrite(ausgabePfad + "H_" + k + "nr" + iteration + "" + time + "_bild" + i + ".jpg", imagesWithDistances.get(distances.get(i)));
			}
			else {
				Imgcodecs.imwrite(ausgabePfad + "B_" + k + "nr" + iteration + "" + time + "_bild" + i + ".jpg", imagesWithDistances.get(distances.get(i)));
			}
	    
		i++;
		
		//HighGui.namedWindow("match " + i,HighGui.WINDOW_AUTOSIZE);		
		//HighGui.imshow("match " + i, imagesWithDistances.get(distances.get(i)));
		}
		
		System.out.println("That took " + time + " milliseconds");
		System.out.println("Ähnliche Bilder in Ausgabe-Ordner!");
		//HighGui.waitKey(0);
	}
        else {
        	System.out.println("Das Experiment konnte nicht durchgeführt werden");
    	}
        
	}
	
	
	private static MatOfDMatch matchDescriptorsStandard(Mat descriptors1, Mat descriptors2, int matchertype)
	{
		BFMatcher matcher = BFMatcher.create(matchertype);
		MatOfDMatch matches = new MatOfDMatch();

		matcher.match(descriptors1, descriptors2, matches);
		return matches;

	}
	
	private static Mat makeCluster(Mat descriptors, int k) {

		int attempts = 10;
		TermCriteria criteria = new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, 100, 0.1);

		Mat labels = new Mat();
		Mat centers = new Mat();
		if(descriptors.rows() < k) { 
			System.out.println("Deskriptorzeilen sind kleiner als K: " + descriptors.rows() + " < " + k); 
			    
		   		   
		} else {
			Core.kmeans(descriptors, k, labels, criteria, attempts, Core.KMEANS_PP_CENTERS, centers);			
		}
		return centers;

	}

	

}