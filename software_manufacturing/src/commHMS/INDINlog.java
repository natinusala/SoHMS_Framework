package commHMS;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

public class INDINlog {
	public static void main(String[] args) {

		String content = "This is the text content";
		FileWriter out = null;
		
		try {
			int i=1;
			PrintWriter writer = new PrintWriter("data/INDIN2016log"+i+".txt", "UTF-8");
			writer.println("POH " +i + " log:");
			writer.close();
			try {
				out = new FileWriter("data/INDIN2016log.txt", true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			java.util.Date date= new java.util.Date();
			out.write(content + "\n");
			out.write(content + ";" + new Timestamp(date.getTime()) +"\n");
			

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
