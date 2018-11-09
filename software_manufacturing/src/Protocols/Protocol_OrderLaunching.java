package Protocols;

import Crosscutting.OutBoxSender;
import OrdersManagement.OrderManager;
import ProductManagement.ProductionOrder;
import commHMS.SocketMessage;
import Application.Application;

public class Protocol_OrderLaunching {

//HANDLER--------------------------------------------------------------------
public static void handleMessage(SocketMessage message, OutBoxSender outBoxSender){
		
		String orderJsonString = message.content;
	
	switch (message.performative) {
		
		case "RequestOrder":
			
			//	orderJsonString = XML.cleanCDATA(orderXMLString); //a modifier
				//System.out.println(orderXMLString);
	
				//Unmarchall the order
				/*
				JAXBContext jaxbContext = JAXBContext.newInstance(ProdOrder.class,LinSeq_Process.class, MServ_Inst.class); 
				ProdOrder order = (ProdOrder)XML.unmarshalXMLString(new StringReader(orderXMLString), jaxbContext);
				*/
				ProductionOrder order = null;
				
				System.out.println(">Order of Product ID:"+ order.getOrderCount()+ " has been recieved");
				
				//Launch Order Manager
				OrderManager orderManager = new OrderManager(order, outBoxSender);
			
				Application.orderManagerDict.put(orderManager.getOrderManagerName(), orderManager);
				//orderManager.launchOrder(Application.df); ???
				
				//Send back confirmation
				SocketMessage confirmation = new SocketMessage(
						orderManager.getOrderManagerName(),message.sender,"ConfirmOrder",
						"String","String","OrderLaunching",message.conversationID,"none");
				//outBoxSender.sendMessage(confirmation, false);
			
			
			break;
			
		case "ProposeEstimate":
			//TODO
			break;
		case "ConfirmOrder":
			System.out.println(">Order Confirmation Received by SOHMS");
			break;
		default:
			break;
		}
		
	}
	
//-----------------------------------------------------	
}
