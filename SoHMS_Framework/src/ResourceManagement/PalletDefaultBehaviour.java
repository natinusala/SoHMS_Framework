package ResourceManagement;

import java.util.HashSet;

import DirectoryFacilitator.DirectoryFacilitator;


public class PalletDefaultBehaviour extends Thread {
	
	//Attributes
	private Transporter pallet;	

	//Getters and Setters
    public Transporter getPallet() {
		return pallet;
	}


	public void setPallet(Transporter pallet) {
		this.pallet = pallet;
	}


	//CONSTRUCTOR
	public PalletDefaultBehaviour(Transporter pallet) {
		this.pallet= pallet;
	}
   
	
	// METHODS
	@Override
	public void run() {
		while(pallet!= null){  // condition to eliminate thread
			//Take charge of Pallet trajectory
			while(pallet.associatedPH==null){
				//System.out.println("== PalletDefaultBehavior Pallet "+ pallet._RFID+ " BIP 4");

				//Request Actions while Blocked
				if(pallet.portStatus== Transporter.TransporterState.BLOCKED){
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
		HashSet<ResourceHolon>portOwners=DirectoryFacilitator.getPortOwners(port);
		String newPort= null;
						
		for (ResourceHolon rh : portOwners) {
			//System.out.println("PalletDefaultBehaviour: rh "+rh.getName()+" NbExec="+rh.roh.numOfCurrentExecutions);
			// Check that there is still no PH assigned
			if(pallet.associatedPH!= null) break; 
			  	// Request Transfer
				 newPort = rh.roh.requestDefaultTransfer(pallet, port);
				// If successful transfer
				 if(newPort!= null){
					pallet.actualPort= newPort;
					break; // if null then ask another port owner for its default action.
			 	}
		}//end for
	
	}

}
