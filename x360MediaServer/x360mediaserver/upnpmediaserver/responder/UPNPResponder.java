/******************************************************************
*
*	MediaServer for CyberLink
*
*	Copyright (C) Satoshi Konno 2003
*
*	File : MediaServer.java
*
*	10/22/03
*		- first revision.
*	03/30/05
*		- Added a constructor that read the description from memory instead of the file.
*		- Changed it as the default constructor.
*
******************************************************************/

package x360mediaserver.upnpmediaserver.responder;

import org.cybergarage.http.HTTPRequest;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.util.Debug;
import org.cybergarage.xml.Node;

import x360mediaserver.Config;

public class UPNPResponder extends UPNPListener
{
	// //////////////////////////////////////////////
    // Constants
    // //////////////////////////////////////////////

    public final static String DEVICE_TYPE                           = "urn:schemas-upnp-org:device:MediaServer:1";

    public final static int    DEFAULT_HTTP_PORT                     = 4242;

    public final static String MEDIA_RECEIVER_REGISTRAR_SERVICE_TYPE = "urn:microsoft.com:service:X_MS_MediaReceiverRegistrar:1";

    public final static String CONNECTION_MANAGER_SERVICE_TYPE       = "urn:schemas-upnp-org:service:ConnectionManager:1";

    public final static String CONTENT_DIRECTORY_SERVICE_TYPE        = "urn:schemas-upnp-org:service:ContentDirectory:1";

    private String getNotifyDeviceNT()
    {
        if (isRootDevice() == false)
            return getUDN();
        return UPNP_ROOTDEVICE;
    }

    // //////////////////////////////////////////////
    // Constructor
    // //////////////////////////////////////////////

    private final static String DESCRIPTION_FILE_NAME = "description/description.xml";

    public UPNPResponder(Node description, String contentDirectorySCPD,
            String connectionManagerSCPD, String mediaReceiverRegistrarSCPD, String address,
            int port) throws InvalidDescriptionException
    {
        super(description, description.getNode("device"));

//        loadDescription(description);
        initializeLoadedDescription();
        setHTTPPort(port);

        Service servConDir = getService(CONTENT_DIRECTORY_SERVICE_TYPE);
        servConDir.loadSCPD(contentDirectorySCPD);

        Service servConMan = getService(CONNECTION_MANAGER_SERVICE_TYPE);
        servConMan.loadSCPD(connectionManagerSCPD);

        Service servmedRR = getService(MEDIA_RECEIVER_REGISTRAR_SERVICE_TYPE);
        servmedRR.loadSCPD(mediaReceiverRegistrarSCPD);

//        initialize(address, port);
    }

//    private void initialize(String address, int port)
//    {
//        // Netwroking initialization
////        UPnP.setEnable(UPnP.USE_ONLY_IPV4_ADDR);
//
////        setInterfaceAddress(address);
//
////        setHTTPPort(port);
//
//    }

    protected void finalize()
    {
        stop();
    }

    // //////////////////////////////////////////////
    // Memeber
    // //////////////////////////////////////////////

    // //////////////////////////////////////////////
    // HostAddress
    // //////////////////////////////////////////////

//    public void setInterfaceAddress(String ifaddr)
//    {
//        System.out.println(ifaddr);
//        HostInterface.setInterface(ifaddr);
//    }
//
//    public String getInterfaceAddress()
//    {
//        return HostInterface.getInterface();
//    }

    // //////////////////////////////////////////////
    // HttpRequestListner (Overridded)
    // //////////////////////////////////////////////

    public void httpRequestRecieved(HTTPRequest httpReq)
    {
        Config.out(httpReq);
        String uri = httpReq.getURI();
        Debug.message("uri = " + uri);
        System.out.println("uri = " + uri);

        super.httpRequestRecieved(httpReq);
    }

    // //////////////////////////////////////////////
    // start/stop (Overided)
    // //////////////////////////////////////////////
    public boolean start()
    {
        super.start();
        return true;
    }

    private boolean stop(boolean doByeBye)
    {
        super.stop();

        return true;
    }

    ////////////////////////////////////////////////
    // update
    ////////////////////////////////////////////////

    public void update()
    {}

}

