/**
 * one line to give the program's name and an idea of what it does. Copyright (C) 2006 Thomas Walker
 * Copyright (C) Satoshi Konno 2002 This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later version. This program
 * is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin
 * Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package x360mediaserver.upnpmediaserver.upnp;

import java.util.Calendar;
import java.util.HashMap;

import org.cybergarage.http.HTTPResponse;
import org.cybergarage.http.HTTPStatus;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.ActionList;
import org.cybergarage.upnp.control.ActionRequest;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.upnp.ssdp.SSDPSearchResponse;
import org.cybergarage.upnp.ssdp.SSDPSearchResponseSocket;
import org.cybergarage.util.TimerUtil;
import org.cybergarage.xml.Node;

import x360mediaserver.Config;
import x360mediaserver.upnpmediaserver.responder.UPNPListener;
import x360mediaserver.upnpmediaserver.upnp.cybergarage.NewActionResponse;

// Uses some code and classes from Cybergate

public abstract class MediaService extends UPNPListener
{

    // Class to handle different upnp services and actions
    String                            SERVICE_STRING;

    protected HashMap<String, Action> actionmap;

    /**
     * @return the sERVICE_STRING
     */
    public String getSERVICE_STRING()
    {
        return SERVICE_STRING;
    }

    public MediaService()
    {
        super();
        actionmap = new HashMap<String, Action>();
    }

    public void setServiceString(String string)
    {
        SERVICE_STRING = string;
    }

    // Stuff borrowed from Cybergate
    protected Node scpdNode = null;

    public boolean loadSCPD(Node scpdNode)
    {
        this.scpdNode = scpdNode;
        this.buildActionList();

        return true;
    }

    public Node getSCPDNode()
    {
        return this.scpdNode;
    }

    public boolean buildActionList()
    {
        Config.out("media service building action list!");
        // ActionList actionList = new ActionList();
        Node scdpNode = getSCPDNode();
        if (scdpNode == null)
            return false;
        Node actionListNode = scdpNode.getNode(ActionList.ELEM_NAME);
        if (actionListNode == null)
            return false;

        // Node serviceNode = getServiceNode();

        // I dont think this will be needed but leaving it just to be safe for now
        Node serviceNode = null;
        // needed now :)

        int nNode = actionListNode.getNNodes();
        for (int n = 0; n < nNode; n++ )
        {
            Node node = actionListNode.getNode(n);
            if (Action.isActionNode(node) == false)
                continue;
            Action action = new Action(serviceNode, node);
            actionmap.put(action.getName(), action);
        }
        return true;
        // return actionList;
    }

    public Action getActionFromRequest(ActionRequest request)
    {
        String actionName = request.getActionName();
        Action action = actionmap.get(actionName);

        if (action == null)
            return null;
        // set argument values
        action.getArgumentList().set(request.getArgumentList());

        return action;
    }
    
    public abstract void doPost(ActionRequest req);
    

//    protected void handleOtherPost(HttpServletRequest req, HttpServletResponse resp)
//    {
//        Config.out("DOING THE POST SOAP ACTION!");
//    }

    // method to handle actions
    protected abstract boolean doAction(Action a, String serveraddress);

    protected void debug(String str)
    {
        System.err.println("abstract SERVICE.java : " + str);
// try{
// File outputFile = new File("E:\\testlog.txt");
// PrintWriter debug = new PrintWriter(new FileWriter(outputFile,true));
// debug.println(System.currentTimeMillis()+" "+str);
// debug.flush();
// debug.close();
// }
// catch(Exception e){
//			
// }
    }
    
 // This happens when we try to connect to the computer (but NOT discover it.)
    //tawsi
    public boolean postSearchResponse(SSDPPacket ssdpPacket, String st, String usn)
    {
        Config.out("MEDIASERVICE POST SEARCH RESPONSE: \n" + ssdpPacket.toString());
        String localAddr = ssdpPacket.getLocalAddress();
        UPNPListener rootDev = getRootDevice();
        String rootDevLocation = rootDev.getLocationURL(localAddr);
        
        SSDPSearchResponse ssdpRes = new SSDPSearchResponse();
        ssdpRes.setLeaseTime(getLeaseTime());
        ssdpRes.setDate(Calendar.getInstance());
        ssdpRes.setST(st);
        ssdpRes.setUSN(usn);
        ssdpRes.setLocation(rootDevLocation);
        // Thanks for Brent Hills (10/20/04)
//        ssdpRes.setMYNAME("YOHOHO");

        int mx = ssdpPacket.getMX();
        TimerUtil.waitRandom(mx * 1000);
        
        String remoteAddr = ssdpPacket.getRemoteAddress();
        int remotePort = ssdpPacket.getRemotePort();
        
        SSDPSearchResponseSocket ssdpResSock = new SSDPSearchResponseSocket();
        
        Config.out(ssdpRes);
//      int ssdpCount = getSSDPAnnounceCount();
//      for (int i=0; i<ssdpCount; i++)
            ssdpResSock.post(remoteAddr, remotePort, ssdpRes);
            
        return true;
    }
}
