import java.util.concurrent.ConcurrentHashMap;

import OrdersManagement.ROH;
import ResourceManagement.ResourceHolon;

public class BufferedRH extends ResourceHolon{
	
	//ATTRIBUTS-------------------------------
		private int capacity;
		private int miliPerUnit;
		
	//CONSTRUCTORS-------------------------------
		public BufferedRH(){
			super();
			this.roh= null; 
			this.portSchedules= null;
		}
		//-----------------------------------------
		
		public BufferedRH (String name,String technology, String category, String textDescription, int capacity) {
			this();
			this.name= name;
			this.technology=technology;
			this.category=category;
			this.textDescription=textDescription;	
			this.capacity= capacity;
			initPortScheules();
			//This type of resource has by default this behaviors
			@SuppressWarnings("unused")
			ROH associatedR_OH = null;/// creates and launches the ROH of this Resource
			
		}
		
//--------------------------------------------------------------------	
		/**
		 * RH are identified based on their type not in their Service offer
		 */
		@Override
		public boolean equals(Object obj){
			return super.equals(obj); // Maybe this is not needed (redundant)
		}

		public int getCapacity() {
			return capacity;
		}

		public void setCapacity(int capacity) {
			this.capacity = capacity;
		}

		public int getMiliPerUnit() {
			return miliPerUnit;
		}

		public void setMiliPerUnit(int miliPerUnit) {
			this.miliPerUnit = miliPerUnit;
		}	
}
