import java.io.*;
import java.util.ArrayList;

public class File 
{
   private String filename;         // the name of the file 
   private ArrayList<String> lines; // the contents of the file
   
   /**
    * Creates an object representing filename and its contents.
    */
   public File (String filename) 
   {
       this.filename = filename; 
       try {
            // open the file
            FileInputStream fstream = new FileInputStream("C:\\Users\\61466\\eclipse-workspace\\Cards\\src\\" + filename);
            // convert fstream to a DataInputStream
            BufferedReader in = new BufferedReader(new InputStreamReader(fstream));
            // read lines one at a time
            lines = new ArrayList<>(); 
            while (in.ready()) lines.add(in.readLine());
            // close the data stream
            in.close();
           } 
       catch (Exception e) {System.err.println("File input error reading " + filename);}
   }
   
   /**
    * Returns the name of the file read.
    */
   public String getName()
   {
       return filename;
   }
   
   /**
    * Returns the contents of the file read.
    */
   public ArrayList<String> getLines()
   {
       return lines;
   }
   public static void main(String[] args) {
	   File f = new File("blackjacktest0.txt");
	   ArrayList<String> array = f.getLines();
	   System.out.println(array);
   }
}