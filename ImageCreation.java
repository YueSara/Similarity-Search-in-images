package projekt.imagecreation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageCreation {
	
	public static void main(String[] args) {
		String filefolder = "C:/Users/saray/Desktop/SimSearch/alle_bilder/";
		File folder = new File(filefolder);
		File[] allFiles = folder.listFiles();
		
		List<String> namesOfFiles = new ArrayList<String>();
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//loop through all pictures
		for(File file : allFiles){
			if (file.getName().contains("jpg")){
				namesOfFiles.add(file.getName());
				System.out.println(".");
			}
		}
		
		// alle Bilder des Ordners in Mats wandeln
		List<Mat> allImageMats = new ArrayList<Mat>();
		for(String imageName : namesOfFiles){
			allImageMats.add(Imgcodecs.imread(filefolder+imageName));
		}
		
		int i = 0;
		for(Mat mat : allImageMats){
			Mat rotated90 = new Mat();
			Mat rotated270 = new Mat();
			Mat rotated180 = new Mat();
			Mat flipped0 = new Mat();
			Mat flipped1 = new Mat();
			Core.rotate(mat, rotated90, Core.ROTATE_90_CLOCKWISE);
			Core.rotate(mat, rotated270, Core.ROTATE_90_COUNTERCLOCKWISE);
			Core.rotate(mat, rotated180, Core.ROTATE_180);
			Core.flip(mat, flipped0, 0);
			Core.flip(mat, flipped1, 1);
			Imgcodecs.imwrite(filefolder+i+"_rotated90.jpg", rotated90);
			Imgcodecs.imwrite(filefolder+i+"_rotated270.jpg", rotated270);
			Imgcodecs.imwrite(filefolder+i+"_rotated180.jpg", rotated180);
			Imgcodecs.imwrite(filefolder+i+"_flipped0.jpg", flipped0);
			Imgcodecs.imwrite(filefolder+i+"_flipped1.jpg", flipped1);
			System.out.println(i);
			i++;
			System.gc();
		}		
	}
}