


import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import sohmsPlateform.app.AppSOHMS;
import sohmsPlateform.outils.XML;
import sohmsPlateform.productHolon.OrderManager;
import mService.LinSeq_Process;
import mService.MServ_Inst;
import mService.ProdOrder;
import commHMS.OutBoxSender;
import commHMS.SocketMessage;

public class Protocol_OrderLaunching {

//HANDLER--------------------------------------------------------------------
public static void handleMessage(SocketMessage message, OutBoxSender outBoxSender){
		
		String orderXMLString = message.content;
	
	switch (message.performative) {
		
		case "RequestOrder":
			
			try {
				orderXMLString = XML.cleanCDATA(orderXMLString);
				//System.out.println(orderXMLString);
	
				//Unmarchall the order
				JAXBContext jaxbContext = JAXBContext.newInstance(ProdOrder.class,LinSeq_Process.class, MServ_Inst.class); 
				ProdOrder order = (ProdOrder)XML.unmarshalXMLString(new StringReader(orderXMLString), jaxbContext);
				
				System.out.println(">Order of Product ID:"+ order.getOrderID()+ " has been recieved");
				
				//Launch Order Manager
				OrderManager orderManager = new OrderManager(outBoxSender, order);
				AppSOHMS.orderManagerDict.put(orderManager.getOrderManagerName(), orderManager);
				orderManager.launchOrder(); 
				
				//Send back confirmation
				SocketMessage confirmation = new SocketMessage(
						orderManager.getOrderManagerName(),message.sender,"ConfirmOrder",
						"String","String","OrderLaunching",message.conversationID,"none");
				outBoxSender.sendMessage(confirmation, false);
				
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
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
