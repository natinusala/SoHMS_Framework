




public class Unit implements RangeValue {
	
//ATTRIBUTS-------------------------------
	private String value;

//CONSTRUCTORS-------------------------------
	public Unit(){}
	//--------------------------
	public Unit(String value){
		this.value= value;
	}
		
//PUBLIC METHODS-------------------------------
	@Override
	public boolean matchValue(String value, String valueType){
		
		try {
			switch (valueType){
			
				case "Integer":
					
					 int ival = Integer.parseInt(value); // if not an integer then it will throw the exception
					 int iother= Integer.parseInt(this.value);
					 
					 if(ival==iother) {return true;}
					 	else {return false;}
				
				case "Double":
					
					double dVal = Double.parseDouble(value); // if not a double then it will throw the exception
					double dOther= Double.parseDouble(this.value);
					 
					 if(dVal==dOther) {return true;}
					 	else {return false;}
					 
				case "String": 
					
					if(this.value.equalsIgnoreCase(value)){return true;}
					else {return false;}				
				}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	//---------------------------------------------------
	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Unit other = (Unit) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equalsIgnoreCase(other.value))
			return false;
		return true;
	}
//PRIVATE METHODS-------------------------------

//GETTERS & SETTERS -----------------------------------
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
	

}
