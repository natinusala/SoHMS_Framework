package Generation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ResourceManagement.ResourceHolon;

public class SILGenerator {

	//Attribtues
	ArrayList<ResourceHolon> resources;

	//constructor 
	public SILGenerator(ArrayList<ResourceHolon> resourceCloud) throws IOException {
		this.resources = resourceCloud;
		generateSIL();
	}
	
	//methods
	public void generateSIL() throws IOException {
		for (int i = 0; i < resources.size(); i++) {
		    File f = new File("src/SIL/"+resources.get(i).getName()+"_SIL.java");
		    f.createNewFile();
		    String content = 
			    	"package SIL;\n"
			    	+ "\n"
			    	+ "import Crosscutting.*;\n"
			    	+ "import mservice.*;\r\n"
			    	+ "import OrdersManagement.*;\n"
			    	+ "import ProductManagement.*;\n"
			    	+ "import ResourceManagement.*;\n"
			    	+ "\n"			    	
			    	+ "public class "+resources.get(i).getName()+"_SIL implements RH_SIL{\n"
			    			+ "\n"
			    			+ "@Override\r\n" + 
			    			"public boolean sendServiceToField(MServiceSpecification service, ProductHolon client, int method) {\r\n" + 
			    			"	// TODO Auto-generated method stub\r\n" + 
			    			"		return false;\r\n" + 
			    			"	}\r\n" + 
			    			"\r\n" + 
			    			"	@Override\r\n" + 
			    			"	public Pair<Integer, Long> getFastestMethod(MServiceImplentation serviceImp) {\r\n" + 
			    			"		// TODO Auto-generated method stub\r\n" + 
			    			"		return null;\r\n" + 
			    			"	}\r\n" + 
			    			"\r\n" + 
			    			"	@Override\r\n" + 
			    			"	public long getMethodTime(Integer methodID) {\r\n" + 
			    			"		// TODO Auto-generated method stub\r\n" + 
			    			"		return 0;\r\n" + 
			    			"	}\r\n" + 
			    			"\r\n" + 
			    			"	@Override\r\n" + 
			    			"	public long getSetupTime(int currentSetup, int setup) {\r\n" + 
			    			"		// TODO Auto-generated method stub\r\n" + 
			    			"		return 0;\r\n" + 
			    			"	}\r\n" + 
			    			"\r\n" + 
			    			"	@Override\r\n" + 
			    			"	public MServiceSpecification defaultAction(Transporter transporter, String inputPort) {\r\n" + 
			    			"		// TODO Auto-generated method stub\r\n" + 
			    			"		return null;\r\n" + 
			    			"	}\r\n" + 
			    			"\r\n" + 
			    			"	@Override\r\n" + 
			    			"	public void changeSetup(int setup) {\r\n" + 
			    			"		// TODO Auto-generated method stub\r\n" + 
			    			"		\r\n" + 
			    			"	}\r\n" + 
			    			"\r\n" + 
			    			"	@Override\r\n" + 
			    			"	public void oK() {\r\n" + 
			    			"		// TODO Auto-generated method stub\r\n" + 
			    			"		\r\n" + 
			    			"	}\r\n" + 
			    			"\r\n" + 
			    			"	@Override\r\n" + 
			    			"	public void start() {\r\n" + 
			    			"		// TODO Auto-generated method stub\r\n" + 
			    			"		\r\n" + 
			    			"	}\r\n" + 
			    			"\r\n" + 
			    			"	@Override\r\n" + 
			    			"	public void end() {\r\n" + 
			    			"		// TODO Auto-generated method stub\r\n" + 
			    			"		\r\n" + 
			    			"	}\r\n" + 
			    			"\r\n" + 
			    			"	@Override\r\n" + 
			    			"	public ROH getROH() {\r\n" + 
			    			"		// TODO Auto-generated method stub\r\n" + 
			    			"		return null;\r\n" + 
			    			"	}\r\n" + 
			    			"\r\n" + 
			    			"	@Override\r\n" + 
			    			"	public String getDefaultDestination() {\r\n" + 
			    			"		// TODO Auto-generated method stub\r\n" + 
			    			"		return null;\r\n" + 
			    			"	}\n"
			    			+ "}";
		    BufferedWriter bw = new BufferedWriter( new FileWriter(f));
		    bw.write(content);
		    bw.close();
		}
	}
	
	public static void main(String[] args) {

	}

}
