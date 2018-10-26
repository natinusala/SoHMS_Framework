package Protocols;


import java.util.ArrayList;
import java.util.Hashtable;

import MService.MServiceImplentation;
import MService.ParametersProfile;
import MService.ProfileParameter;
import ResourceManagement.ResourceHolon;
import Crosscutting.*;
import commHMS.*;



public class Protocol_RHmodify {

	//HANDLER--------------------------------------------------------------------
	public static void handleMessage(SocketMessage message, OutBoxSender outBoxSender){
		/*	
		ResourceHolon _poste;
		
		switch (message.performative) {
			
			case "ListRHServices":
				
				
				
				//Determination du poste concerne
				_poste= new ResourceHolon();
				_poste=Initialization.df.getResourceByName(message.content);
				
					
				
				//Listing des services du RH:"+_poste.getServiceOffer().toString());
				String JSonOrder ="";
				for (MServiceImplentation _service : _poste.getOfferedServices()) {
					
					JSonOrder =_service.getmService().getName()+"%%Poste="+message.content.replace("Poste", "");
						for (ParametersProfile _serviceparamset : _service.getParametersProfile()) {
							for (ProfileParameter _serviceparam : _serviceparamset.getParamProfiles()){
								if (_serviceparam.getName().equals("Width")){
									for (String _serviceparamrangevalue : _serviceparam.getRangeValues()){
										String _serviceunit=  _serviceparamrangevalue;
									//	JSonOrder = JSonOrder +"%%Width=" +_serviceunit.getValue();
										}
								}
							}
							for (ProfileParameter _serviceparam : _serviceparamset.getParamProfiles()){
								if (_serviceparam.getName().equals("Color")){
									for (String _serviceparamrangevalue : _serviceparam.getRangeValues()){
										System.out.println("Service Parameter RangeValues :"+_serviceparamrangevalue.toString());
										String _serviceunit= _serviceparamrangevalue;
										//String xmlOrder2=xmlOrder+"%%Color=" +_serviceunit.getValue();
										if (Initialization.sil.getRHSILByClass(_poste.sil.getClass()) instanceof Poste2SIL){
										//		Poste2SIL _p2=(Poste2SIL) AppSOHMS.sil.getRHSILByClass(_poste.sil.getClass());
										//		xmlOrder2=xmlOrder2+"%%Stock="+_p2.mapBlock.get(new Pair<String, String>(_service.getmServType().getName(), _serviceunit.getValue() ));
											}
										if (AppSOHMS.sil.getRHSILByClass(_poste.sil.getClass()) instanceof Poste3SIL){
												Poste3SIL _p2=(Poste3SIL) AppSOHMS.sil.getRHSILByClass(_poste.sil.getClass());
												System.out.println("_serviceunit.getValue() "+_serviceunit.getValue().toString());
												System.out.println("_service.getmServType().getName() "+_service.getmServType().getName().toString());
												
												xmlOrder2=xmlOrder2+"%%Stock="+_p2.mapBlock.get(new Pair<String, String>(_service.getmServType().getName(), _serviceunit.getValue() ));
											}
										//Envoi de la reponse e HMI
										SocketMessage requestOrder = new SocketMessage("HMS", "HMI", "ServicesList", "XML", "Answer", "RH modify", 2, xmlOrder2);
										//Launch OutboxSender to Send Messages
										outBoxSender.sendMessage(requestOrder,false);
										}
								}
							}
						}
				}
					
					break;
			case "StockModify":
				
				//Decomposition du message
				String[] _elements=message.content.split("%%");
				
				
				//Determination du poste concerne
				_poste= new ResourceHolon();
				_poste=Initialization.df.getResourceByName(_elements[0].replace("Poste=", "Poste"));
				String _stock=_elements[1].split("=")[1];
				String _width=_elements[2].split("=")[1];
				String _color=_elements[3].split("=")[1];
				
				
					
				
				
				
				//Modif des services du RH:"+_poste.getServiceOffer().toString());
				for (MServiceImplentation _service : _poste.getOfferedServices()) {
					
					//xmlOrder=_service.getmServType().getName()+"%%Poste="+message.content.replace("Poste", "");
						for (ParameterProfileSet _serviceparamset : _service.getParameterProfileSets()) {
							boolean ok=false;
							for (Param_Profile _serviceparam : _serviceparamset.getParamProfiles()){
								if (_serviceparam.getName().equals("Width")){
									for (RangeValue _serviceparamrangevalue : _serviceparam.getRangeValues()){
										Unit _serviceunit= (Unit) _serviceparamrangevalue;
										//xmlOrder=xmlOrder+"%%Width=" +_serviceunit.getValue();
										
										System.out.println("%%Width%%Trouve="+_serviceunit.getValue()+", cherche="+_width);
										
										if (_serviceunit.getValue().equals(_width)){
											System.out.println("%%Width%%Trouve!");
											ok=true;
										}
									}
								}
							}
							if (ok){
								for (Param_Profile _serviceparam : _serviceparamset.getParamProfiles()){
									if (_serviceparam.getName().equals("Color")){
										for (RangeValue _serviceparamrangevalue : _serviceparam.getRangeValues()){
											//System.out.println("Service Parameter RangeValues :"+_serviceparamrangevalue.toString());
											Unit _serviceunit= (Unit) _serviceparamrangevalue;
											if (AppSOHMS.sil.getRHSILByClass(_poste.sil.getClass()) instanceof Poste2SIL){
												Poste2SIL _p2=(Poste2SIL) AppSOHMS.sil.getRHSILByClass(_poste.sil.getClass());
												//xmlOrder2=xmlOrder2+"%%Stock="+_p2.mapBlock.get(new Pair<String, String>(_service.getmServType().getName(), _serviceunit.getValue() ));
												if (_p2.mapBlock.get(new Pair<String, String>(_service.getmServType().getName(), _serviceunit.getValue() )).equals(_stock)){
													
													//Mise e jour du service
													_serviceunit.setValue(_color);
													//Mise e jour du SIL
													_p2.mapBlock.put(new Pair<String, String>(_service.getmServType().getName(), _serviceunit.getValue() ), _stock);
													
												}
											}
											if (AppSOHMS.sil.getRHSILByClass(_poste.sil.getClass()) instanceof Poste3SIL){
												Poste3SIL _p2=(Poste3SIL) AppSOHMS.sil.getRHSILByClass(_poste.sil.getClass());
												//xmlOrder2=xmlOrder2+"%%Stock="+_p2.mapBlock.get(new Pair<String, String>(_service.getmServType().getName(), _serviceunit.getValue() ));
												
												if (_p2.mapBlock.get(new Pair<String, String>(_service.getmServType().getName(), _serviceunit.getValue() )).equals(_stock)){
													
													//Mise e jour du service
													_serviceunit.setValue(_color);
													//Mise e jour du SIL
													_p2.mapBlock.put(new Pair<String, String>(_service.getmServType().getName(), _serviceunit.getValue() ), _stock);
													
												}
											}
										}
									
									}
								}
							}
						}
				}
				
				
				

				break;
			
			case "TransportON":
				//TODO
				 Initialization.commField.commandSender.sendMessage(message.content);
				break;
			case "TransportOFF":
				//TODO
				 Initialization.commandSender.sendMessage(message.content);
				break;
			case "Add":
				//TODO
				
				break;

			default:
				break;
			}
			*/
		}
		

}
