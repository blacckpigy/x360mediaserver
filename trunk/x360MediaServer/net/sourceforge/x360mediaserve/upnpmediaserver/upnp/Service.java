/**
 * one line to give the program's name and an idea of what it does.
 Copyright (C) 2006  Thomas Walker
 Copyright (C) Satoshi Konno 2002
 
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */



package net.sourceforge.x360mediaserve.upnpmediaserver.upnp;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.cybergarage.HTTPServletRequestWrapper;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.cybergarage.NewActionResponse;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.ActionList;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.control.ActionRequest;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.xml.Node;
import org.cybergarage.xml.Parser;
import org.cybergarage.xml.ParserException;



//Uses some code and classes from Cybergate

public abstract class Service {
	
	// Class to handle differend upnp services and actions
	protected String SCPDString=null;
	protected boolean SCPDisFile=false;
	String SERVICE_STRING;
	
	protected HashMap<String,Action> actionmap;
	
	public Service(){
		super();
		actionmap=new HashMap<String,Action>();
	}
	
	public void setServiceString(String string){
		SERVICE_STRING=string;
	}
	
	// Stuff borrowed from Cybergate
	
	protected Node scpdNode=null;
	
	public boolean loadSCPD(String scpdStr) throws InvalidDescriptionException
	{
		try {
			Parser parser = UPnP.getXMLParser();
			Node scpdNode = parser.parse(scpdStr);
			if (scpdNode == null)
				return false;			
			this.scpdNode=scpdNode;
			this.buildActionList();
		}
		catch (ParserException e) {
			throw new InvalidDescriptionException(e);
		}
		return true;
	}
	
	public boolean loadSCPD(File scpdFile) throws InvalidDescriptionException
	{
		try {
			Parser parser = UPnP.getXMLParser();
			Node scpdNode = parser.parse(scpdFile);
			if (scpdNode == null)
				return false;			
			this.scpdNode=scpdNode;
			this.buildActionList();
		}
		catch (ParserException e) {
			throw new InvalidDescriptionException(e);
		}
		return true;
	}
	
	public Node getSCPDNode(){
		return scpdNode;
	}
	
	public boolean buildActionList(){
		
		//ActionList actionList = new ActionList();
		Node scdpNode = getSCPDNode();
		if (scdpNode == null)
			return false;
		Node actionListNode = scdpNode.getNode(ActionList.ELEM_NAME);
		if (actionListNode == null)
			return false;
		
		//Node serviceNode = getServiceNode();
		
		//I dont think this will be needed but leaving it just to be safe for now
		Node serviceNode=null;
		// needed now :)
		
		int nNode = actionListNode.getNNodes();
		for (int n=0; n<nNode; n++) {
			Node node = actionListNode.getNode(n);
			if (Action.isActionNode(node) == false)
				continue;
			Action action = new Action(serviceNode, node);
			actionmap.put(action.getName(),action);
		} 			
		return true;
		//return actionList;
	}
	
	public Action getActionFromRequest(ActionRequest request){
		String actionName=request.getActionName();
		Action action=actionmap.get(actionName);
		
		if(action==null) return null;
		// set argument values
		action.getArgumentList().set(request.getArgumentList());
		
		
		return action;
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp){		
		
		try{
			if(req.getHeader("SOAPACTION")!=null){ // check it is a upnp action
				HTTPServletRequestWrapper actionwrapper=new HTTPServletRequestWrapper(req);
				ActionRequest actrequest=actionwrapper.getActionRequest();
				Action action=getActionFromRequest(actrequest);
				
				
				if(doAction(action,req.getLocalAddr()+":"+req.getLocalPort())){
					resp.setContentType("text/xml; charset=\"utf-8\"");						
					// if the action has been successfully done
					debug("action done, setting response");
					NewActionResponse actresponse=new NewActionResponse();
					debug("action done, response created");
					actresponse.setResponse(action,SERVICE_STRING);						
					debug("actresponse set");
					
					
					resp.setContentLength((int)actresponse.getContentLength());
					OutputStream output=resp.getOutputStream();
					output.write(actresponse.getContent());
					output.flush();
					output.close();
					
				}
			}
			else{
				handleOtherPost(req,resp);
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected void handleOtherPost(HttpServletRequest req, HttpServletResponse resp){
		
	}
	
	// method to handle actions
	protected abstract boolean doAction(Action a,String serveraddress);
	
	protected void debug(String str){
	    System.err.println("SERVICE.java : " + str);
//		try{
//			File outputFile = new File("E:\\testlog.txt");
//			PrintWriter debug = new PrintWriter(new FileWriter(outputFile,true));	
//			debug.println(System.currentTimeMillis()+" "+str);
//			debug.flush();
//			debug.close();
//		}
//		catch(Exception e){
//			
//		}
	}
	
	
	
}
