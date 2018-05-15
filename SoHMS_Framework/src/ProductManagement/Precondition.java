package ProductManagement;

public class Precondition {

	//ATTRIBUTS-------------------------------
		private Object operand1;
		private Object operand2;
		private String operator;

	//CONSTRUCTORS-------------------------------
		public Precondition(){}
		
		public Precondition ( Object operand1, String operator, Object operand2){
			this.operand1= operand1;
			this.operator= operator;
			this.operand2 = operand2;
		}
	//PUBLIC METHODS-------------------------------
		public boolean isSatisfied(){ // TODO Add the function to evaluate the Preconditions and Reconsider the nature of operands.

			return true;
			
		}

	//PRIVATE METHODS-------------------------------

	//GETTERS & SETTERS -----------------------------------
		public Object getOperand1() {
			return operand1;
		}

		public Object getOperand2() {
			return operand2;
		}

		public String getOperator() {
			return operator;
		}

		public void setOperand1(Object operand1) {
			this.operand1 = operand1;
		}

		public void setOperand2(Object operand2) {
			this.operand2 = operand2;
		}

		public void setOperator(String operator) {
			this.operator = operator;
		}

		
	}