package Protocols;

import java.util.ArrayList;
import java.util.HashSet;
import Crosscutting.*;
import MService.MServiceSpecification;
import ProductManagement.ProductionProcess;
import ResourceManagement.ResourceHolon;


public class Protocol_GetNextPathArcs {
	
//Static Methods -------------------------------------------	
	public static ArrayList<PathArc> run(ProductionProcess recipe,int processIndex, String startingPort){
		
		ArrayList<PathArc> nextPathArcs = new ArrayList<PathArc>();
		
		//Get Alternative Services
		ArrayList<MServiceSpecification> altServ= recipe.getAlternatives();


		//Generate Next Possible Path States
		ArrayList<PathState> nextPathStates= new ArrayList<PathState>();
		
			// NEXT SERVICE?
			for (int i = 0; i < altServ.size(); i++) {
				//PROVIDERS OF NEXT SERVICE?
				 HashSet<Pair<ResourceHolon,Double>> providers=null;
				//=AppSOHMS.df.getProviders(altServ.get(i));				 
				//------------------------------------------------------------------------
				//Print Providers
				 System.out.println("Number of Providers:"+ providers.size());
				 System.out.println("Of "+ altServ.get(i).toString());
				 for (Pair<ResourceHolon,Double> rh : providers) {
								System.out.println(rh.getFirst().getName());
							}
							
							//--------------------------------------------------------
				for( Pair<ResourceHolon,Double> provider: providers){
					//EACH INPUTPORT OF PROVIDER?
					for(String inPort: provider.getFirst().getInputPorts()){
						for(String outPort: provider.getFirst().getOutputPorts()){
							nextPathStates.add(new PathState(altServ.get(i), provider.getFirst(), inPort, outPort, processIndex+1, provider.getSecond()));
						}
					}
					
				}	
			}
			
		//Get  ROUTES to the Next Path State
		for (PathState nextPathState: nextPathStates){
			
		// ROUTES to next RESOURCE?
			ArrayList<TerminalSequence>routes = null;
			//= AppSOHMS.df.getRoutes_NoLoops(startingPort, nextPathState.inputPort);
			
		//Create PathArc for each route
			if( nextPathState.inputPort.equalsIgnoreCase(startingPort)) {
				nextPathArcs.add(new PathArc(null, nextPathState)); //new output arc to the list
			}
				
			for(TerminalSequence route : routes){  
				nextPathArcs.add(new PathArc(route, nextPathState)); //new output arc to the list
			}										
		}
		return nextPathArcs;
		
	}
	//---------------------------------------------------------------------------------------------------------
	
public static ArrayList<PathArc> run(MServiceSpecification service,int processIndex, String startingPort){
		
		ArrayList<PathArc> nextPathArcs = new ArrayList<PathArc>();
		
		//Get Alternative Services
		
		//Generate Next Possible Path States
		ArrayList<PathState> nextPathStates= new ArrayList<PathState>();
		
				//PROVIDERS OF NEXT SERVICE?
				 HashSet<Pair<ResourceHolon,Double>> providers =null;
						 //=AppSOHMS.df.getProviders(service);							
							//Print Providers
							System.out.println("Number of Providers:"+ providers.size());
							System.out.println("Of "+ service.toString());
							for (Pair<ResourceHolon,Double> rh : providers) {
								System.out.println(rh.getFirst().getName());
							}
							
				//--------------------------------------------------------
				for( Pair<ResourceHolon,Double> provider: providers){
					//EACH INPUTPORT OF PROVIDER?
					for(String inPort: provider.getFirst().getInputPorts()){
						for(String outPort: provider.getFirst().getOutputPorts()){
							nextPathStates.add(new PathState(service, provider.getFirst(), inPort, outPort, processIndex+1, provider.getSecond()));
						}
					}
					
				}	
			
		//Get  ROUTES to the Next Path State
		for (PathState nextPathState: nextPathStates){
			
		// ROUTES to next RESOURCE?
			TerminalSequence[] routes = null;
			//=AppSOHMS.df.getRoutes_NoLoops(startingPort, nextPathState.inputPort);
			
		//Create PathArc for each route
			if( nextPathState.inputPort.equalsIgnoreCase(startingPort)) {
				nextPathArcs.add(new PathArc(null, nextPathState)); //new output arc to the list
			}
				
			for(TerminalSequence route : routes){  
				nextPathArcs.add(new PathArc(route, nextPathState)); //new output arc to the list
			}										
		}
		return nextPathArcs;
		
	}
}
