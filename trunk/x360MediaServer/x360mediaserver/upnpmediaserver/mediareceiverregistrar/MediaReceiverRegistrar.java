/**
 * one line to give the program's name and an idea of what it does.
 Copyright (C) 2006  Thomas Walker
 
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



package x360mediaserver.upnpmediaserver.mediareceiverregistrar;



import org.cybergarage.http.HTTPResponse;
import org.cybergarage.http.HTTPStatus;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.control.ActionRequest;
import org.cybergarage.xml.Node;

import x360mediaserver.Config;
import x360mediaserver.upnpmediaserver.upnp.MediaService;
import x360mediaserver.upnpmediaserver.upnp.cybergarage.NewActionResponse;



public class MediaReceiverRegistrar extends MediaService {
	
	// Servlet to handle the MediaReceiverRegistrar stuff in Windows Media Connect
	
	int    callcount    = 0;

    public MediaReceiverRegistrar(Node node, String serviceString)
    {
        super();
        try
        {
            debug(System.currentTimeMillis() + " X_MS_MediaReceiverRegistrar init");
            loadSCPD(node);

            buildActionList();
            this.setServiceString(serviceString);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
	
//	public void doGet(HttpServletRequest req, HttpServletResponse resp)
//    {
//	    Config.out("MEDIA RECIEVER REGISTER HTTP GET");
//        try
//        {
//            PrintWriter writer = resp.getWriter();
//            writer.write(UPnP.XML_DECLARATION + "\n" );
//            writer.write(getSCPDNode().toString());
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
	

	protected boolean doAction(Action action,String ServerAddress){
		debug("Action received:"+action.getName());
		if(action.getName().contains("IsAuthorized"))
			return isAuthorized(action);
		else if(action.getName().contains("IsValidated"))
			return isValidated(action);

		return false;
	}
	
	private boolean isValidated(Action action)
	{	
		action.getArgument("Result").setValue("1");
		return true;
	}
	
	private boolean isAuthorized(Action action)
	{	
		debug("isAuthorized received, responding");
		
		action.getArgument("Result").setValue("1");
		debug("isAuthorized received, responded with "+action.getArgument("Result").getValue());
		
		return true;
	}

	 public void doPost(ActionRequest req)
	    {

	        Config.out("DOING THE SOAP ACTION!");
	        try
	        {
//	            if (req.getHeader("SOAPACTION") != null)
//	            { // check it is a upnp action
//	                HTTPServletRequestWrapper actionwrapper = new HTTPServletRequestWrapper(req);
//	                ActionRequest actrequest = actionwrapper.getActionRequest();
	                Action action = getActionFromRequest(req);

	                if (doAction(action, req.getLocalAddress() + ":" + req.getLocalPort()))
	                {
	                    HTTPResponse resp = new HTTPResponse();
	                    resp.setContentType("text/xml");
	                    // if the action has been successfully done
	                    debug("action done, setting response");
	                    NewActionResponse actresponse = new NewActionResponse();
	                    debug("action done, response created");
	                    actresponse.setResponse(action, getSERVICE_STRING());
	                    debug("actresponse set");

	                    resp.setStatusCode(HTTPStatus.OK);
	                    resp.setContentLength((int) actresponse.getContentLength());
	                    resp.setContent(actresponse.getContent());
	                    req.post(resp);
	                }
//	            }
//	            else
//	            {
//	                handleOtherPost(req, resp);
//	            }

	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	    }
	
	protected void debug(String str){
	    System.err.println("MEDIARECVREG : " + str);
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
