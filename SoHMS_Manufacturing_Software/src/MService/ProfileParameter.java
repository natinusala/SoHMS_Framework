package MService;

import java.util.ArrayList;
import java.util.HashSet;

enum RangeType{
	Catalogue,
	Interval;
}
public class ProfileParameter extends Parameter{
 
	//Attributes
	protected RangeType rangeType;
	protected ArrayList<String>  rangeValues;
	
	
	//Constructor
	public ProfileParameter(){
		this.rangeValues= new ArrayList<String>();
	}
	public ProfileParameter(Parameter param, RangeType rangetype){
		super(param);
		this.rangeType= rangetype;
		this.rangeValues= new ArrayList<String>();
	}
	
	//Getters 
	public RangeType getRangeType() {
		return rangeType;
	}
	public ArrayList<String> getRangeValues() {
		return rangeValues;
	}
	
	//Setters
	public void setRangeType(RangeType rangeType) {
		this.rangeType = rangeType;
	}
	
	public void setRangeValues(ArrayList<String> rangeValues) {
		this.rangeValues = rangeValues;
	}
	
	//methods
	public  void addValueToRange(String value) {
		this.rangeValues.add(value);
	}
	
	public boolean matchParam(Parameter p){ 
	    if(isSameParamType(p)){ // check if both refer to the same Parameter Type
						 
				for (String obj : rangeValues) { // see if  there is an interval in which it fits
					
					// Check if the value fits (Interval and Unit)
					if (p.getValue().matches(this.value))  
						return true;
				} 
				// There is no range value that fits
				return false; 
	    }
		return false; // Profile and Specification do not refer to the same Parameter
	}
}
