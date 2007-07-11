/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File: ActionResponse.java
*
*	Revision;
*
*	01/29/03
*		- first revision.
*	09/02/03
*		- Giordano Sassaroli <sassarol@cefriel.it>
*		- Problem : Action Responses do not contain the mandatory header field EXT
*		- Error : ActionResponse class does not set the EXT header
*	
******************************************************************/

package x360mediaserver.upnpmediaserver.upnp.cybergarage;

import org.cybergarage.http.HTTP;
import org.cybergarage.http.HTTPStatus;
import org.cybergarage.soap.SOAP;
import org.cybergarage.soap.SOAPResponse;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ArgumentList;
import org.cybergarage.xml.Node;

public class NewActionResponse extends org.cybergarage.upnp.control.ControlResponse
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	public NewActionResponse()
	{
		setHeader(HTTP.EXT, "");
	}

	public NewActionResponse(SOAPResponse soapRes)
	{
		super(soapRes);
		setHeader(HTTP.EXT, "");
	}


	////////////////////////////////////////////////
	//	Response
	////////////////////////////////////////////////

	public void setResponse(Action action,String serviceString)
	{
		debug("hello");
		setStatusCode(HTTPStatus.OK);
		debug("set status");
		Node bodyNode = getBodyNode();
		debug("got bodyNode");
		Node resNode = createResponseNode(action,serviceString);
		
		debug("got resNode");
		bodyNode.addNode(resNode);
		debug("added body node");
		Node envNode = getEnvelopeNode();
		setContent(envNode);
	}
	
	public void setResponse(Action action,String serviceString,String methodns)
	{
		debug("hello");
		setStatusCode(HTTPStatus.OK);
		debug("set status");
		Node bodyNode = getBodyNode();
		debug("got bodyNode");
		Node resNode = createResponseNode(action,serviceString,methodns);
		
		debug("got resNode");
		bodyNode.addNode(resNode);
		debug("added body node");
		Node envNode = getEnvelopeNode();
		setContent(envNode);
	}
	
	protected void debug(String str){
//		try{
//			File outputFile = new File("E:\\testlog2.txt");
//			PrintWriter debug = new PrintWriter(new FileWriter(outputFile,true));	
//			debug.println(System.currentTimeMillis()+" ActionResponse "+str);
//			debug.flush();
//			debug.close();
//		}
//		catch(Exception e){
//			System.out.println(e.toString());
//		}
	}
	

	private Node createResponseNode(Action action,String serviceString)
	{
		String actionName = action.getName();
		debug("got name");
		//Node actionNameResNode = new Node(SOAP.METHODNS + SOAP.DELIM + actionName + SOAP.RESPONSE);
		Node actionNameResNode = new Node(SOAP.METHODNS + SOAP.DELIM + actionName + SOAP.RESPONSE);
		debug("got actionNameResNode");
		//Service service = action.getService();
		//if (service != null) {
//			actionNameResNode.setAttribute(
//				"xmlns:" + SOAP.METHODNS,
//				"urn:microsoft.com:service:X_MS_MediaReceiverRegistrar:1");
		actionNameResNode.setAttribute(
				"xmlns:" + SOAP.METHODNS,
				serviceString);

		//}
		debug("got service");
		
		ArgumentList argList = action.getArgumentList();
		int nArgs = argList.size();
		for (int n=0; n<nArgs; n++) {
			Argument arg = argList.getArgument(n);
			if (arg.isOutDirection() == false)
				continue;
			Node argNode = new Node();
			argNode.setName(arg.getName());
//			if(arg.getAttributeList()!=null)
//			for(Object i: arg.getAttributeList()){
//				argNode.setAttribute(((Attribute)i).getName(),((Attribute)i).getValue());
//			}
			argNode.setValue(arg.getValue());
			actionNameResNode.addNode(argNode);
		}
		
		return actionNameResNode;
	}
	
	private Node createResponseNode(Action action,String serviceString,String METHODNS)
	{
		String actionName = action.getName();
		debug("got name");
		//Node actionNameResNode = new Node(SOAP.METHODNS + SOAP.DELIM + actionName + SOAP.RESPONSE);
		Node actionNameResNode = new Node(METHODNS + SOAP.DELIM + actionName + SOAP.RESPONSE);
		debug("got actionNameResNode");
		//Service service = action.getService();
		//if (service != null) {
//			actionNameResNode.setAttribute(
//				"xmlns:" + SOAP.METHODNS,
//				"urn:microsoft.com:service:X_MS_MediaReceiverRegistrar:1");
		actionNameResNode.setAttribute(
				"xmlns:" + METHODNS,
				serviceString);

		//}
		debug("got service");
		
		ArgumentList argList = action.getArgumentList();
		int nArgs = argList.size();
		for (int n=0; n<nArgs; n++) {
			Argument arg = argList.getArgument(n);
			if (arg.isOutDirection() == false)
				continue;
			Node argNode = new Node();
			argNode.setName(arg.getName());
			argNode.setValue(arg.getValue());
			actionNameResNode.addNode(argNode);
		}
		
		return actionNameResNode;
	}

	////////////////////////////////////////////////
	//	getResponse
	////////////////////////////////////////////////

	private Node getActionResponseNode()
	{
		Node bodyNode = getBodyNode();
		if (bodyNode == null || bodyNode.hasNodes() == false)
			return null;
		return bodyNode.getNode(0);
	}
	
	
	public ArgumentList getResponse()
	{
		ArgumentList argList = new ArgumentList();
		
		Node resNode = getActionResponseNode();
		if (resNode == null)
			return argList;
			
		int nArgs = resNode.getNNodes();
		for (int n=0; n<nArgs; n++) {
			Node node = resNode.getNode(n);
			String name = node.getName();
			String value = node.getValue();
			Argument arg = new Argument(name, value);
			argList.add(arg);
		}
		
		return argList;
	}
}
