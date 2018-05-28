public class Interval implements RangeValue {
	
	//ATTRIBUTS-------------------------------
		private String min;
	    private String max;
	
	//CONSTRUCTORS-------------------------------
	public Interval(){
		
	}
	//-------------------------------------
	/**
	 * Both max and min take the same value.
	 * @param val
	 */
	public Interval(String val){
		//Verify if it is Integer or Double
		
		try {
			Double.parseDouble(val); // Verify indirectly if val is a Number. If not it will throw exception. 
			this.min=val;
			this.max=val;
		} catch (NumberFormatException e) {
			System.out.println("Error: Value entered is not an Integer or Double");
		}
		
	}
	//---------------------------------------------
	public Interval(String min, String max){
		try {
			Double.parseDouble(min); // Verify indirectly if val is a Number. If not it will throw exception.
			Double.parseDouble(max);
			
			this.min=min;
			this.max=max;
		} catch (NumberFormatException e) {
			System.out.println("Error: Value entered is not an Integer or Double");
		}
		
	}
	//PUBLIC METHODS-------------------------------
	/**
	 * Verifies if the VALUE is inside the limits of the present interval.
	 * @param value 
	 * @return TRUE if value inside the limits, FALSE if not or error in ValueType
	 */
	public boolean matchValue(String value, String valueType){
		
		try {
			switch (valueType){
			
				case "Integer":
					
					 int ival = Integer.parseInt(value); // if not an integer then it will throw the exception
					 int imin= Integer.parseInt(this.min);
					 int imax= Integer.parseInt(this.max);	
					 
					 if(ival>=imin && ival<=imax) {return true;}
					 	else {return false;}
				
				case "Double":
					
					 double dval = Double.parseDouble(value); // if not an integer then it will throw the exception
					 double dmin= Double.parseDouble(this.min);
					 double dmax= Double.parseDouble(this.max);	
					 
					 if(dval>=dmin && dval<=dmax) {return true;}
					 	else {return false;}
			
				
				}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		return false;
		
		}
	//----------------------------------------------------------
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Interval other = (Interval) obj;
		if (max == null) {
			if (other.max != null)
				return false;
		} else if (!max.equals(other.max))
			return false;
		if (min == null) {
			if (other.min != null)
				return false;
		} else if (!min.equals(other.min))
			return false;
		return true;
	}
	



}
