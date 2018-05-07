package OrdersManagement;

import java.io.PrintWriter;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import Crosscutting.OutBoxSender;
import ProductManagement.ProductHolon;
import ProductManagement.ProductionOrder;

 
  /* 
   * A generic Order Manager that :
   *    1-launches the PH to treat the products.
   *    2-Registers the progress on the products production process.
   */

public abstract class OrderManager {
	
	//Attributes
	protected ProductionOrder order;
	protected OutBoxSender outBoxSender;
	protected Set<ProductHolon> finishedPHs;
	protected Set<ProductHolon> activePHs;
	
	//Constructors
	public OrderManager() {
		finishedPHs = Collections.synchronizedSet(new HashSet<ProductHolon>()); // Synchronizes the acces to  this ArrayList. Must synchronize if iterated
		activePHs = Collections.synchronizedSet(new HashSet<ProductHolon>()); // Synchronizes the acces to  this ArrayList. Must synchronize if iterated
	}
	public OrderManager(ProductionOrder order, OutBoxSender outBoxSender) {
		System.out.println("order");
		this.order= order;
		this.outBoxSender= outBoxSender;
		finishedPHs = Collections.synchronizedSet(new HashSet<ProductHolon>()); // Synchronizes the acces to  this ArrayList. Must synchronize if iterated
		activePHs = Collections.synchronizedSet(new HashSet<ProductHolon>()); // Synchronizes the acces to  this ArrayList. Must synchronize if iterated
	}
	//generic Methods
	
	//a method that launchs the execution of an order. each order is a psecific to targer domain
	abstract void  launchOrder();
    public synchronized void  phIsFinised(ProductHolon ph){	
		finishedPHs.add(ph);
		activePHs.remove(ph);	
		//another speicific behavior can be added here
		// Launch new Products
		launchOrder();
     }
	
    public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		return result;
	}
	
    public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderManager other = (OrderManager) obj;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		return true;
	}	
	
    public String getOrderManagerName(){
		return ("OrderManager("+order.getProductionOrderID()+")");
	}

}
