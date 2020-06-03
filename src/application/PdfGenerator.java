package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;   
import java.io.IOException;

public class PdfGenerator {
	
	private String fileName;
	
	public PdfGenerator(String fileName) {
		this.fileName = fileName;
	}
	
	public void main() {
	    try {
	      FileWriter myWriter = new FileWriter(this.fileName+".txt");
	      
	      myWriter.write("Files in Java might be tricky, but it is fun enough!");
	      myWriter.close();
	      
	      System.out.println("Successfully wrote to the file.");
	      
	    } catch (IOException e) {
	    	
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	  }
	
	
	public void getPdf() {
		main();
		try {
			File file = new File(this.fileName+".txt");
			Desktop desktop = Desktop.getDesktop(); 
			
			if(file.exists()) {
				System.out.println("ok");
				desktop.open(file);		
				}
		
			}catch(IOException e) {
				System.out.println("An error occurred.");
			     e.printStackTrace();
			}
	}
}
