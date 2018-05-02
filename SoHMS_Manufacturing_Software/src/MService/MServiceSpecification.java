package MService;

import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

public class MServiceSpecification{
	
	//ATTRIBUTS-------------------------------
	private static int counter=0;
	private static int maxInst=1000;
	
	private int id;
	private MService mServType;
    private ArrayList<Parameter> parameters;
	private ArrayList<ParametersProfile> attributeProfiles;
	
	
//CONSTRUCTORS-------------------------------
	public MServiceSpecification(){
		this.id= (counter % maxInst) +1; // rotational queue
		counter= this.id;
	}
	
	
//PUBLIC METHODS-------------------------------
	
	public String toString() {
		String str = "Service Name: "+mServType.getName()+" ServiceID: " + id +"\nParameters:";
		
		for (int i = 0; i < parameters.size(); i++) {
			str= str.concat(parameters.get(i).toString());
		}
		 return str;
	}
	//--------------------------------------
	/**
	 * Returns the parameter object with the specified name. 
	 * Null if it does not exist.
	 * @param paramName
	 * @return 
	 */
	
	//-----------------------------------------------------------
	public Parameter getParameterByName(String name) {
		
		for (int i = 0; i < parameters.size(); i++) {
			Parameter p= parameters.get(i); // Cast  to use equals @ Parameter class
			if(p.getName().equalsIgnoreCase(name)){ 
				return parameters.get(i);
			}
		}		
		return null;
	}
	//----------------------------------------------------
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mServType == null) ? 0 : mServType.hashCode());
		//result = prime * result + Arrays.hashCode(parameters);
		return result;
	}
	//-----------------------------------------
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MServiceSpecification other = (MServiceSpecification) obj;
		if (mServType == null) {
			if (other.mServType != null)
				return false;
		} else if (!mServType.equals(other.mServType))
			return false;
		return true;
	}
/*	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(attributeProfiles);
		result = prime * result + id;
		result = prime * result
				+ ((mServType == null) ? 0 : mServType.hashCode());
		result = prime * result + Arrays.hashCode(parameters);
		return result;
	}
	//-------------------------------------------
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MServ_Inst other = (MServ_Inst) obj;
		if (!Arrays.equals(attributeProfiles, other.attributeProfiles))
			return false;
		if (id != other.id)
			return false;
		if (mServType == null) {
			if (other.mServType != null)
				return false;
		} else if (!mServType.equals(other.mServType))
			return false;
		if (!Arrays.equals(parameters, other.parameters))
			return false;
		return true;
	}
	*/
	//PRIVATE METHODS-------------------------------

	
	//SETTERS -----------------------------------
	public void setId(int id) {
		this.id = id;
	}
	
	public static void setCounter(int counter) {
		MServiceSpecification.counter = counter;
	}
	public static void setMaxInst(int maxInst) {
		MServiceSpecification.maxInst = maxInst;
	}
	public void setMServType(MService mServType) {
		this.mServType = mServType;
	}
	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}
	public void setAttributeProfiles(ArrayList<ParametersProfile> attributeProfiles) {
		this.attributeProfiles = attributeProfiles;
	}
	
	//GETTERS -----------------------------------
	public int getId() {
		return id;
	}
	public static int getCounter() {
		return counter;
	}
	public static int getMaxInst() {
		return maxInst;
	}
	public MService getMServType() {
		return mServType;
	}
	public ArrayList<Parameter> getParameters() {
		return parameters;
	}
	public ArrayList<ParametersProfile> getAttributeProfiles() {
		return attributeProfiles;
	}
	
	
	
		
}

