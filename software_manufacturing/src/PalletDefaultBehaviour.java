

import java.util.HashSet;

import ResourceManagement.ResourceHolon;


public class PalletDefaultBehaviour extends Thread {
	
	public Transporter pallet;	
	
//CONSTRUCTOR------------------------------------------------------
	public PalletDefaultBehaviour(Transporter pallet) {
		this.pallet= pallet;
	}
// METHODS--------------------------------------------------------------
	@Override
	public void run() {
		
		while(pallet!= null){  // condition to eliminate thread
			
			//Take charge of Pallet trajectory
			while(pallet.associatedPH==null){
				//System.out.println("== PalletDefaultBehavior Pallet "+ pallet._RFID+ " BIP 4");

				//Request Actions while Blocked
				if(pallet.portStatus== PortPositionStatus.Blocked){
					//System.out.println("== PalletDefaultBehavior Pallet "+ pallet._RFID+ " BIP 5");
					requestDefaultAction(pallet.actualPort);
				}
				else {
					synchronized (pallet) {
						try {
							//System.out.println("== PalletDefaultBehavior Pallet "+ pallet._RFID+ " waiting");
							pallet.wait();//Wait for changes in the Pallet 
							//System.out.println("== PalletDefaultBehavior Pallet "+ pallet._RFID+ " ended waiting");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						//System.out.println("== PalletDefaultBehavior Pallet "+ pallet._RFID+ " BIP 1");
					}
					//System.out.println("== PalletDefaultBehavior Pallet "+ pallet._RFID+ " BIP 2");
				}
				//System.out.println("== PalletDefaultBehavior Pallet "+ pallet._RFID+ " BIP 3");
			}// There is a PH associated
			pallet.waitPalletLiberation();	// will wait to re take control	
			System.out.println("Pallet "+ pallet._RFID+" Liberated!");
	}//end While
 }//end run
//---------------------------------------------------------	
	private synchronized void requestDefaultAction(String port) {
		// REQUEST DEFAULT ACTION TO PORT OWNER
		HashSet<ResourceHolon>portOwners= null;
						
		for (ResourceHolon rh : portOwners) {
		
		}//end for
	
	}

}
