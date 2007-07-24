package x360mediaserver.upnpmediaserver.responder;

import java.util.Calendar;

import org.cybergarage.http.HTTPRequest;
import org.cybergarage.http.HTTPResponse;
import org.cybergarage.http.HTTPStatus;
import org.cybergarage.soap.SOAPResponse;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.ActionList;
import org.cybergarage.upnp.ArgumentList;
import org.cybergarage.upnp.DeviceList;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.UPnPStatus;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.ActionRequest;
import org.cybergarage.upnp.control.ActionResponse;
import org.cybergarage.upnp.control.ControlRequest;
import org.cybergarage.upnp.control.ControlResponse;
import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.control.QueryRequest;
import org.cybergarage.upnp.device.NTS;
import org.cybergarage.upnp.device.ST;
import org.cybergarage.upnp.ssdp.SSDPNotifyRequest;
import org.cybergarage.upnp.ssdp.SSDPNotifySocket;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.upnp.ssdp.SSDPSearchResponse;
import org.cybergarage.upnp.ssdp.SSDPSearchResponseSocket;
import org.cybergarage.upnp.xml.DeviceData;
import org.cybergarage.util.Debug;
import org.cybergarage.util.FileUtil;
import org.cybergarage.util.TimerUtil;
import org.cybergarage.xml.Node;
import org.cybergarage.xml.XML;

import x360mediaserver.Config;

public class UPNPListener extends MyDevice
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////

	// NOTE: I must make sure to have a correct UDN set or the failchecks will bug the system!
    public UPNPListener(Node root, Node device)
	{
		super(root, device);
	}

	public UPNPListener(Node device)
	{
		this(null, device);
	}

	public UPNPListener()
	{
	    this(null, null);
	}
	
	////////////////////////////////////////////////
	//	Root Device
	////////////////////////////////////////////////
	
	public UPNPListener getRootDevice()
	{
		Node rootNode = getRootNode();
		if (rootNode == null)
			return null;
		Node devNode = rootNode.getNode(UPNPListener.ELEM_NAME);
		if (devNode == null)
			return null;
		return new UPNPListener(rootNode, devNode);
	}

	////////////////////////////////////////////////
	//	UserData
	////////////////////////////////////////////////

	private DeviceData getDeviceData()
	{
		Node node = getDeviceNode();
		DeviceData userData = (DeviceData)node.getUserData();
		if (userData == null) {
			userData = new DeviceData();
			node.setUserData(userData);
			userData.setNode(node);
		}
		return userData;
	}
	
	
	public UPNPListener getDeviceByDescriptionURI(String uri)
    {
        DeviceList devList = getDeviceList();
        int devCnt = devList.size();
        for (int n=0; n<devCnt; n++) {
            UPNPListener dev = (UPNPListener) devList.getDevice(n);
            if (dev.isDescriptionURI(uri) == true)
                return dev;
            UPNPListener cdev = dev.getDeviceByDescriptionURI(uri);
            if (cdev != null)
                return cdev;
        }
        return null;
    }
	
	////////////////////////////////////////////////
	//	Description
	////////////////////////////////////////////////

	private String getDescriptionURI()
	{
		return getDeviceData().getDescriptionURI();
	}

	private boolean isDescriptionURI(String uri)
	{
		String descriptionURI = getDescriptionURI();
		if (uri == null || descriptionURI == null)
			return false;
		return descriptionURI.equals(uri);
	}

	/**
	 * This has been somewhat modified from the original class.
	 * Sets the description and lease time for the device
	 */
	protected boolean initializeLoadedDescription()
	{
	    getDeviceData().setDescriptionURI(DEFAULT_DESCRIPTION_URI);
		getDeviceData().setLeaseTime(DEFAULT_LEASE_TIME);
				
		return true;
	}
	

	/**
	 * This has been modified from the original...
	 * there is no ... device list..
	 */
	public MyService getService(String name)
	{
		MyServiceList serviceList = getServiceList();
		int serviceCnt = serviceList.size();
		for (int n=0; n<serviceCnt; n++) {
		    MyService service =serviceList.getService(n);
			if (service.isService(name) == true)
				return service;
		}
		
	
		
		return null;
	}

   /**
     * This has been modified from the original...
     * there is no ... device list..
     */
	public MyService getServiceBySCPDURL(String searchUrl)
	{
		MyServiceList serviceList = getServiceList();
		int serviceCnt = serviceList.size();
		for (int n=0; n<serviceCnt; n++) {
		    MyService service = serviceList.getService(n);
			if (service.isSCPDURL(searchUrl) == true)
				return service;
		}
		
	
		return null;
	}

   /**
     * This has been modified from the original...
     * there is no ... device list..
     */
	public MyService getServiceByControlURL(String searchUrl)
	{
		MyServiceList serviceList = getServiceList();
		int serviceCnt = serviceList.size();
		for (int n=0; n<serviceCnt; n++) {
			MyService service = serviceList.getService(n);
			if (service.isControlURL(searchUrl) == true)
				return service;
		}
		
		
		return null;
	}

   /**
     * This has been modified from the original...
     * there is no ... device list..
     */
	public MyService getServiceByEventSubURL(String searchUrl)
	{
		MyServiceList serviceList = getServiceList();
		int serviceCnt = serviceList.size();
		for (int n=0; n<serviceCnt; n++) {
		    MyService service = serviceList.getService(n);
			if (service.isEventSubURL(searchUrl) == true)
				return service;
		}
		

		return null;
	}
	
    /**
     * This has been modified from the original...
     * there is no ... device list..
     */
	public MyService getSubscriberService(String uuid)
	{
		MyServiceList serviceList = getServiceList();
		int serviceCnt = serviceList.size();
		for (int n=0; n<serviceCnt; n++) {
		    MyService service = serviceList.getService(n);
			String sid = service.getSID();
			if (uuid.equals(sid) == true)
				return service;
		}
		

		
		return null;
	}

	////////////////////////////////////////////////
	//	StateVariable
	////////////////////////////////////////////////

   /**
     * This has been modified from the original...
     * there is no ... device list..
     */
	public StateVariable getStateVariable(String serviceType, String name)
	{
		if (serviceType == null && name == null)
			return null;
		
		MyServiceList serviceList = getServiceList();
		int serviceCnt = serviceList.size();
		for (int n=0; n<serviceCnt; n++) {
		    MyService service = serviceList.getService(n);
			// Thanks for Theo Beisch (11/09/04)
			if (serviceType != null) {
				if (service.getServiceType().equals(serviceType) == false)
					continue;
			}
			StateVariable stateVar = service.getStateVariable(name);
			if (stateVar != null)
				return stateVar;
		}
		

		
		return null;
	}

	////////////////////////////////////////////////
	//	Action
	////////////////////////////////////////////////

	/**
     * This has been modified from the original...
     * there is no ... device list..
     */
	public Action getAction(String name)
	{
		MyServiceList serviceList = getServiceList();
		int serviceCnt = serviceList.size();
		for (int n=0; n<serviceCnt; n++) {
		    MyService service = serviceList.getService(n);
			ActionList actionList = service.getActionList();
			int actionCnt = actionList.size();
			for (int i=0; i<actionCnt; i++) {
				Action action = (Action)actionList.getAction(i);
				String actionName = action.getName();
				if (actionName == null)
					continue;
				if (actionName.equals(name) == true)
					return action;
			}
		}
		

		
		return null;
	}

	private String getNotifyDeviceNT()
	{
		if (isRootDevice() == false)
			return getUDN();			
		return UPNP_ROOTDEVICE;
	}

	private String getNotifyDeviceUSN()
	{
		if (isRootDevice() == false)
			return getUDN();			
		return getUDN() + "::" + UPNP_ROOTDEVICE;
	}

	private String getNotifyDeviceTypeNT()
	{
		return getDeviceType();
	}

	private String getNotifyDeviceTypeUSN()
	{
		return getUDN() + "::" + getDeviceType();
	}
	
   /**
     * This has been modified from the original...
     * there is no ... device list..
     */
	public void announce(String bindAddr)
	{
		String devLocation = getLocationURL(bindAddr);
		
		SSDPNotifySocket ssdpSock = new SSDPNotifySocket(bindAddr);

		SSDPNotifyRequest ssdpReq = new SSDPNotifyRequest();
		ssdpReq.setServer(UPnP.getServerName());
		ssdpReq.setLeaseTime(getLeaseTime());
		ssdpReq.setLocation(devLocation);
		ssdpReq.setNTS(NTS.ALIVE);
		
		// uuid:device-UUID(::upnp:rootdevice)* 
		if (isRootDevice() == true) {
			String devNT = getNotifyDeviceNT();			
			String devUSN = getNotifyDeviceUSN();
			ssdpReq.setNT(devNT);
			ssdpReq.setUSN(devUSN);
			ssdpSock.post(ssdpReq);
		}
		
		// uuid:device-UUID::urn:schemas-upnp-org:device:deviceType:v 
		String devNT = getNotifyDeviceTypeNT();			
		String devUSN = getNotifyDeviceTypeUSN();
		ssdpReq.setNT(devNT);
		ssdpReq.setUSN(devUSN);
		ssdpSock.post(ssdpReq);
		
		// Thanks for Mikael Hakman (04/25/05)
		ssdpSock.close();
		
		MyServiceList serviceList = getServiceList();
		int serviceCnt = serviceList.size();
		for (int n=0; n<serviceCnt; n++) {
		    MyService service = serviceList.getService(n);
			service.announce(bindAddr);
		}


	}

   /**
     * This has been modified from the original...
     * there is no ... device list..
     */
	public void byebye(String bindAddr)
	{
		SSDPNotifySocket ssdpSock = new SSDPNotifySocket(bindAddr);
		
		SSDPNotifyRequest ssdpReq = new SSDPNotifyRequest();
		ssdpReq.setNTS(NTS.BYEBYE);
		
		// uuid:device-UUID(::upnp:rootdevice)* 
		if (isRootDevice() == true) {
			String devNT = getNotifyDeviceNT();			
			String devUSN = getNotifyDeviceUSN();
			ssdpReq.setNT(devNT);
			ssdpReq.setUSN(devUSN);
			ssdpSock.post(ssdpReq);
		}
		
		// uuid:device-UUID::urn:schemas-upnp-org:device:deviceType:v 
		String devNT = getNotifyDeviceTypeNT();			
		String devUSN = getNotifyDeviceTypeUSN();
		ssdpReq.setNT(devNT);
		ssdpReq.setUSN(devUSN);
		ssdpSock.post(ssdpReq);

		// Thanks for Mikael Hakman (04/25/05)
		ssdpSock.close();
		
		MyServiceList serviceList = getServiceList();
		int serviceCnt = serviceList.size();
		for (int n=0; n<serviceCnt; n++) {
		    MyService service = serviceList.getService(n);
			service.byebye(bindAddr);
		}


	}

	////////////////////////////////////////////////
	//	Search
	////////////////////////////////////////////////

	
	// This happens when we try to connect to the computer (but NOT discover it.)
	//tawsi
	public boolean postSearchResponse(SSDPPacket ssdpPacket, String st, String usn)
	{
		Config.out("UPNP POST SEARCH RESPONSE:");
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
//		ssdpRes.setMYNAME("YOHOHO");

		int mx = ssdpPacket.getMX();
		TimerUtil.waitRandom(mx * 1000);
		
		String remoteAddr = ssdpPacket.getRemoteAddress();
		int remotePort = ssdpPacket.getRemotePort();
		
		SSDPSearchResponseSocket ssdpResSock = new SSDPSearchResponseSocket();
		
		 System.err.print(ssdpRes);
		Config.out(remoteAddr + " : " + remotePort + "\n***************");
		int ssdpCount = getSSDPAnnounceCount();
		for (int i=0; i<ssdpCount; i++)
			ssdpResSock.post(remoteAddr, remotePort, ssdpRes);
			
		return true;
	}
	
	/**
	 * This method is SPECIFIC to work with the XBOX360. (DISCOVERY, CONNECTING)
	 */
	public void deviceSearchResponse(SSDPPacket ssdpPacket)
    {       
        //System.out.println("using mediaservers search response");
        String ssdpST = ssdpPacket.getST();

        if (ssdpST == null)
            return;

        boolean isRootDevice = isRootDevice();
        // twinked version.
        String devUSN = getUDN(); 
//        if (isRootDevice == true)
//            devUSN += "::" + USN.ROOTDEVICE;
        
        if (ST.isAllDevice(ssdpST) == true) {           
            String devNT = getNotifyDeviceNT();         
            int repeatCnt = (isRootDevice == true) ? 3 : 2;
            for (int n=0; n<repeatCnt; n++)
                postSearchResponse(ssdpPacket, devNT, devUSN);
        }
        else if (ST.isRootDevice(ssdpST) == true) {
            if (isRootDevice == true)
                postSearchResponse(ssdpPacket, ST.ROOT_DEVICE, devUSN);
        }
        else if (ST.isUUIDDevice(ssdpST) == true) {
            String devUDN = getUDN();
            if (ssdpST.equals(devUDN) == true)
                postSearchResponse(ssdpPacket, devUDN, devUSN);
        }
        else if (ST.isURNDevice(ssdpST) == true) {
            String devType= getDeviceType();
            if (ssdpST.equals(devType) == true) {
                // Thanks for Mikael Hakman (04/25/05)
                devUSN = getUDN() + "::" + devType;
                postSearchResponse(ssdpPacket, devType, devUSN);
            }
        }
        
        // The service (Content Directory) is what replies to the XBOX for DISCOVERY
        MyServiceList serviceList = getServiceList();
        int serviceCnt = serviceList.size();
        for (int n=0; n<serviceCnt; n++) {
            MyService service = serviceList.getService(n);
            service.serviceSearchResponse(ssdpPacket);
        }
    }
	
	// This lets everything work for HTTP device received stuff. (also for connecting the xbox)
	public void deviceSearchReceived(SSDPPacket ssdpPacket)
	{
		deviceSearchResponse(ssdpPacket);
	}
	
	// This is the HTTP main server. ALL requests come through here (so far...) 
	public void httpRequestRecieved(HTTPRequest httpReq)
    {
        Config.out("HTTP REQUEST REVICED BY THE SYSTEM");
	    if (Debug.isOn() == true)
            httpReq.print();
    
        if (httpReq.isGetRequest() == true) {
            httpGetRequestRecieved(httpReq);
            return;
        }
        if (httpReq.isPostRequest() == true) {
            httpPostRequestRecieved(httpReq);
            return;
        }

        if (httpReq.isSubscribeRequest() == true || httpReq.isUnsubscribeRequest() == true) {
            super.httpRequestRecieved(httpReq);
            return;
        }

        httpReq.returnBadRequest();
    }
	
	private void httpGetRequestRecieved(HTTPRequest httpReq)
    {
        String uri = httpReq.getURI();
        Debug.message("httpGetRequestRecieved = " + uri);
        if (uri == null) {
            httpReq.returnBadRequest();
            return;
        }
                    
        UPNPListener embDev;
        MyService embService;
        
        byte fileByte[] = new byte[0];
        if (isDescriptionURI(uri) == true) {
            String localAddr = httpReq.getLocalAddress();
            fileByte = getDescriptionData(localAddr);
        }
        else if ((embDev = getDeviceByDescriptionURI(uri)) != null) {
            String localAddr = httpReq.getLocalAddress();
            fileByte = embDev.getDescriptionData(localAddr);
        }
        else if ((embService = getServiceBySCPDURL(uri)) != null) {
            fileByte = embService.getSCPDData();
        }
        else {
            httpReq.returnBadRequest();
            return;
        }
        
        HTTPResponse httpRes = new HTTPResponse();
        if (FileUtil.isXMLFileName(uri) == true)
            httpRes.setContentType(XML.CONTENT_TYPE);
        httpRes.setStatusCode(HTTPStatus.OK);
        httpRes.setContent(fileByte);

        httpReq.post(httpRes);
    }
	
	private void httpPostRequestRecieved(HTTPRequest httpReq)
    {
        if (httpReq.isSOAPAction() == true) {
            //SOAPRequest soapReq = new SOAPRequest(httpReq);
            soapActionRecieved(httpReq);
            return;
        }
        httpReq.returnBadRequest();
    }
	
	
	
	
	
	
	
	
	
	
	
	

	private synchronized byte[] getDescriptionData(String host)
	{
		//Changed by tom
		//if (isNMPRMode() == false)
//			updateURLBase(host);
		Node rootNode = getRootNode();
		if (rootNode == null)
			return new byte[0];
		// Thanks for Mikael Hakman (04/25/05)
		String desc = new String();
		desc += UPnP.XML_DECLARATION;
		desc += "\n";
		desc += rootNode.toString();
		return desc.getBytes();
	}
	
	
	////////////////////////////////////////////////
    //  SOAP
    ////////////////////////////////////////////////

    private void soapBadActionRecieved(HTTPRequest soapReq)
    {
        SOAPResponse soapRes = new SOAPResponse();
        soapRes.setStatusCode(HTTPStatus.BAD_REQUEST);
        soapReq.post(soapRes);
    }

    private void soapActionRecieved(HTTPRequest soapReq)
    {
        String uri = soapReq.getURI();
        MyService ctlService = getServiceByControlURL(uri);
        if (ctlService != null)  {
            ActionRequest crlReq = new ActionRequest(soapReq);
            deviceControlRequestRecieved(crlReq, ctlService);
            return;
        }
        soapBadActionRecieved(soapReq);
    }

    ////////////////////////////////////////////////
    //  controlAction
    ////////////////////////////////////////////////

    private void deviceControlRequestRecieved(ControlRequest ctlReq, MyService service)
    {
        if (ctlReq.isQueryControl() == true)
            deviceQueryControlRecieved(new QueryRequest(ctlReq), service);
        else
            deviceActionControlRecieved(new ActionRequest(ctlReq), service);
    }

    private void invalidActionControlRecieved(ControlRequest ctlReq)
    {
        ControlResponse actRes = new ActionResponse();
        actRes.setFaultResponse(UPnPStatus.INVALID_ACTION);
        ctlReq.post(actRes);
    }

    private void deviceActionControlRecieved(ActionRequest ctlReq, MyService service)
    {
        if (Debug.isOn() == true)
            ctlReq.print();
            
        String actionName = ctlReq.getActionName();
        Action action = service.getAction(actionName);
        if (action == null) {
            invalidActionControlRecieved(ctlReq);
            return;
        }
        else
        {
            String type = service.getServiceNode().getNode("serviceType").getValue();
            if (type.contains("ContentDirectory"))
            {
                Config.out("Sending to content dir");
                Config.getContentDirectory().doPost(ctlReq);
            }
//            else if (type.contains("X_MS_MediaReceiverRegistrar"))
//            {
//                Config.out("Sending to media reciever");
//                Config.getMediaReceiverReg().doPost(ctlReq);
//            }
            MyService realService = getService(type);
//            realService.
            String actionName2 = "asds";
            
        }
        
        ArgumentList actionArgList = action.getArgumentList();
        ArgumentList reqArgList = ctlReq.getArgumentList();
        actionArgList.set(reqArgList);
        if (action.performActionListener(ctlReq) == false)
            invalidActionControlRecieved(ctlReq);
    }

    private void deviceQueryControlRecieved(QueryRequest ctlReq, MyService service)
    {
        if (Debug.isOn() == true)
            ctlReq.print();
        String varName = ctlReq.getVarName();
        if (service.hasStateVariable(varName) == false) {
            invalidActionControlRecieved(ctlReq);
            return;
        }
        StateVariable stateVar = getStateVariable(varName);
        if (stateVar.performQueryListener(ctlReq) == false)
            invalidActionControlRecieved(ctlReq);
    }
    
//	private void httpGetRequestRecieved(HTTPRequest httpReq)
//	{
//		Config.out("UPNP HTTP REQUEST RECIEVED");
//	    String uri = httpReq.getURI();
//		Debug.message("httpGetRequestRecieved = " + uri);
//		if (uri == null) {
//			httpReq.returnBadRequest();
//			return;
//		}
//					
//		UPNPListener embDev;
//		Service embService;
//		
//		byte fileByte[] = new byte[0];
//		if (isDescriptionURI(uri) == true) {
//			String localAddr = httpReq.getLocalAddress();
//			fileByte = getDescriptionData(localAddr);
//		}
//		else if ((embService = getServiceBySCPDURL(uri)) != null) {
//			fileByte = embService.getSCPDData();
//		}
//		else {
//			httpReq.returnBadRequest();
//			return;
//		}
//		
//		HTTPResponse httpRes = new HTTPResponse();
//		if (FileUtil.isXMLFileName(uri) == true)
//			httpRes.setContentType(XML.CONTENT_TYPE);
//		httpRes.setStatusCode(HTTPStatus.OK);
//		httpRes.setContent(fileByte);
////tawsi loop
//		httpReq.post(httpRes);
//	}

	////////////////////////////////////////////////
	// Acion/QueryListener (includeSubDevices)
	////////////////////////////////////////////////

	/**
	 * minus devices...
	 */
	// Thanks for Mikael Hakman (04/25/05)
	public void setActionListener(ActionListener listener, boolean includeSubDevices) 
	{
		setActionListener(listener);
	}
	
	/**
     * minus devices...
     */
	// Thanks for Mikael Hakman (04/25/05)
	public void setQueryListener(QueryListener listener, boolean includeSubDevices) 
	{
		setQueryListener(listener);		
	}
}

