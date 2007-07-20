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
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.util.Debug;
import org.cybergarage.xml.Node;

import x360mediaserver.Config;

public class UPNPResponder extends UPNPListener
{
	// //////////////////////////////////////////////
    // Constants
    // //////////////////////////////////////////////

    private final static String DEVICE_TYPE                           = "urn:schemas-upnp-org:device:MediaServer:1";

    private final static String MEDIA_RECEIVER_REGISTRAR_SERVICE_TYPE = "urn:microsoft.com:service:X_MS_MediaReceiverRegistrar:1";

    private final static String CONNECTION_MANAGER_SERVICE_TYPE       = "urn:schemas-upnp-org:service:ConnectionManager:1";

    private final static String CONTENT_DIRECTORY_SERVICE_TYPE        = "urn:schemas-upnp-org:service:ContentDirectory:1";

    private String getNotifyDeviceNT()
    {
        if (isRootDevice() == false)
            return getUDN();
        return UPNP_ROOTDEVICE;
    }

    // //////////////////////////////////////////////
    // Constructor
    // //////////////////////////////////////////////

    public UPNPResponder(Node description, Node contentDirectorySCPD,
                         Node connectionManagerSCPD, Node mediaReceiverRegistrarSCPD, String address,
                         int port) throws InvalidDescriptionException
    {
        super(description, description.getNode("device"));

        initializeLoadedDescription();
        setHTTPPort(port);

        
        Service servConDir = getService(CONTENT_DIRECTORY_SERVICE_TYPE);
        servConDir.loadSCPD(contentDirectorySCPD.toString());

        Service servConMan = getService(CONNECTION_MANAGER_SERVICE_TYPE);
        servConMan.loadSCPD(connectionManagerSCPD.toString());

        Service servmedRR = getService(MEDIA_RECEIVER_REGISTRAR_SERVICE_TYPE);
        servmedRR.loadSCPD(mediaReceiverRegistrarSCPD.toString());

    }

    protected void finalize()
    {
        stop();
    }

    public void httpRequestRecieved(HTTPRequest httpReq)
    {
        Config.out("REQUESTING: " + httpReq);
        String uri = httpReq.getURI();
        Debug.message("uri = " + uri);
        System.out.println("uri = " + uri);

        super.httpRequestRecieved(httpReq);
    }
    
    ////////////////////////////////////////////////
    // update
    ////////////////////////////////////////////////

    public void update()
    {}

}

