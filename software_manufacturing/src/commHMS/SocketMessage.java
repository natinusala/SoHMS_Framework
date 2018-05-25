package commHMS;

public class SocketMessage {
	
//ATTRIBUTS-------------------------------
	
	   public String sender;
       public String reciever;
	   public String performative;
	   public String encoding;
	   public String contentOntology;		
	   public String protocol;
	   public int conversationID;
	   public String content;  //TODO have to define that this is text and not to be considered as XML
	
//CONSTRUCTORS-------------------------------
	public SocketMessage(){
		
	}
	//-----------------------------------
	/**
	 * 
	 * @param sender
	 * @param reciever
	 * @param performative: Specifies the type of action that is requested with in a Protocol
	 * @param encoding:  To decode the message contents eg. XML 
	 * @param contentOntology
	 * @param protocol: Describes the type of conversation ( exchange of messages) that takes place between agents
	 * @param conversationID: identifies the conversation and should be set during launch of the conversation Thread
	 * @param content
	 */
	public SocketMessage(String sender,String reciever,String performative,
			String encoding,String contentOntology,String protocol,
			int conversationID, String content){
		
		this.sender= sender;
		this.reciever= reciever;
		this.performative= performative;
		this.encoding= encoding;
		this.contentOntology= contentOntology;
		this.protocol= protocol;
		this.conversationID= conversationID;
		this.content= content;
		
	}
	//-------------------------------------------
	public SocketMessage(SocketMessage other){
		
		this.sender= new String(other.sender);
		this.reciever= new String(other.reciever);
		this.performative= new String(other.performative);
		this.encoding= new String(other.encoding);
		this.contentOntology= new String(other.contentOntology);
		this.protocol= new String (other.protocol);
		this.conversationID= other.conversationID;
		this.content= new String(other.content);
	}

}
