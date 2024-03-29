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

package org.cybergarage.upnp.media.server;

import java.io.File;

import org.cybergarage.http.HTTPRequest;
import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.upnp.media.server.object.Format;
import org.cybergarage.util.MyLogger;

public class MediaServer extends Device
{
    // //////////////////////////////////////////////
    // Constants
    // //////////////////////////////////////////////

    public final static String  DEVICE_TYPE           = "urn:schemas-upnp-org:device:MediaServer:1";

    public final static int     DEFAULT_HTTP_PORT     = 38520;

    public final static String  DESCRIPTION           = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                                                        + "<root xmlns=\"urn:schemas-upnp-org:device-1-0\">\n"
                                                        + "   <specVersion>\n"
                                                        + "      <major>1</major>\n"
                                                        + "      <minor>0</minor>\n"
                                                        + "   </specVersion>\n"
                                                        + "   <device>\n"
                                                        + "      <deviceType>urn:schemas-upnp-org:device:MediaServer:1</deviceType>\n"
                                                        + "      <friendlyName>Cyber Garage Media Server</friendlyName>\n"
                                                        + "      <manufacturer>Cyber Garage</manufacturer>\n"
                                                        + "      <manufacturerURL>http://www.cybergarage.org</manufacturerURL>\n"
                                                        + "      <modelDescription>Provides content through UPnP ContentDirectory service</modelDescription>\n"
                                                        + "      <modelName>Cyber Garage Media Server</modelName>\n"
                                                        + "      <modelNumber>1.0</modelNumber>\n"
                                                        + "      <modelURL>http://www.cybergarage.org</modelURL>\n"
                                                        + "      <UDN>uuid:362d9414-31a0-48b6-b684-2b4bd38391d0</UDN>\n"
                                                        + "      <serviceList>\n"
                                                        + "         <service>\n"
                                                        + "            <serviceType>urn:schemas-upnp-org:service:ContentDirectory:1</serviceType>\n"
                                                        + "            <serviceId>urn:upnp-org:serviceId:urn:schemas-upnp-org:service:ContentDirectory</serviceId>\n"
                                                        + "            <SCPDURL>/service/ContentDirectory1.xml</SCPDURL>\n"
                                                        + "            <controlURL>/service/ContentDirectory_control</controlURL>\n"
                                                        + "            <eventSubURL>/service/ContentDirectory_event</eventSubURL>\n"
                                                        + "         </service>\n"
                                                        + "         <service>\n"
                                                        + "            <serviceType>urn:schemas-upnp-org:service:ConnectionManager:1</serviceType>\n"
                                                        + "            <serviceId>urn:upnp-org:serviceId:urn:schemas-upnp-org:service:ConnectionManager</serviceId>\n"
                                                        + "            <SCPDURL>/service/ConnectionManager1.xml</SCPDURL>\n"
                                                        + "            <controlURL>/service/ConnectionManager_control</controlURL>\n"
                                                        + "            <eventSubURL>/service/ConnectionManager_event</eventSubURL>\n"
                                                        + "         </service>\n"
                                                        + "      </serviceList>\n"
                                                        + "   </device>\n" + "</root>";

    // //////////////////////////////////////////////
    // Constructor
    // //////////////////////////////////////////////

    private final static String DESCRIPTION_FILE_NAME = "description/description.xml";
    private static MyLogger     log                   = new MyLogger(MediaServer.class);

    public MediaServer(String descriptionFileName) throws InvalidDescriptionException
    {
        super(new File(descriptionFileName));
        initialize();
    }

    public MediaServer() throws InvalidDescriptionException
    {
        this(DESCRIPTION, ContentDirectory.SCPD, ConnectionManager.SCPD);
    }

    public MediaServer(String description, String contentDirectorySCPD, String connectionManagerSCPD)
            throws InvalidDescriptionException
    {
        super();

        loadDescription(description);

        Service servConDir = getService(ContentDirectory.SERVICE_TYPE);
        servConDir.loadSCPD(contentDirectorySCPD);

        Service servConMan = getService(ConnectionManager.SERVICE_TYPE);
        servConMan.loadSCPD(connectionManagerSCPD);

        initialize();
    }

    private void initialize()
    {
        // Netwroking initialization
        UPnP.setEnable(UPnP.USE_ONLY_IPV4_ADDR);
        String firstIf = HostInterface.getHostAddress(0);
        setInterfaceAddress(firstIf);
        setHTTPPort(DEFAULT_HTTP_PORT);

        conDir = new ContentDirectory(this);
        conMan = new ConnectionManager(this);

        Service servConDir = getService(ContentDirectory.SERVICE_TYPE);
        servConDir.setActionListener(getContentDirectory());
        servConDir.setQueryListener(getContentDirectory());

        Service servConMan = getService(ConnectionManager.SERVICE_TYPE);
        servConMan.setActionListener(getConnectionManager());
        servConMan.setQueryListener(getConnectionManager());
    }

    protected void finalize()
    {
        stop();
    }

    // //////////////////////////////////////////////
    // Memeber
    // //////////////////////////////////////////////

    private ConnectionManager conMan;
    private ContentDirectory  conDir;

    public ConnectionManager getConnectionManager()
    {
        return conMan;
    }

    public ContentDirectory getContentDirectory()
    {
        return conDir;
    }

    // //////////////////////////////////////////////
    // ContentDirectory
    // //////////////////////////////////////////////

    public void addContentDirectory(Directory dir)
    {
        getContentDirectory().addDirectory(dir);
    }

    public void removeContentDirectory(String name)
    {
        getContentDirectory().removeDirectory(name);
    }

    public int getNContentDirectories()
    {
        return getContentDirectory().getNDirectories();
    }

    public Directory getContentDirectory(int n)
    {
        return getContentDirectory().getDirectory(n);
    }

    // //////////////////////////////////////////////
    // PulgIn
    // //////////////////////////////////////////////

    public boolean addPlugIn(Format format)
    {
        return getContentDirectory().addPlugIn(format);
    }

    // //////////////////////////////////////////////
    // HostAddress
    // //////////////////////////////////////////////

    public void setInterfaceAddress(String ifaddr)
    {
        HostInterface.setInterface(ifaddr);
    }

    public String getInterfaceAddress()
    {
        return HostInterface.getInterface();
    }

    // //////////////////////////////////////////////
    // HttpRequestListner (Overridded)
    // //////////////////////////////////////////////

    public void httpRequestRecieved(HTTPRequest httpReq)
    {
        String uri = httpReq.getURI();
        log.trace("uri = {0}", uri);

        if (uri.startsWith(ContentDirectory.CONTENT_EXPORT_URI) == true)
        {
            getContentDirectory().contentExportRequestRecieved(httpReq);
            return;
        }

        super.httpRequestRecieved(httpReq);
    }

    // //////////////////////////////////////////////
    // start/stop (Overided)
    // //////////////////////////////////////////////

    public boolean start()
    {
        getContentDirectory().start();
        super.start();
        return true;
    }

    public boolean stop()
    {
        getContentDirectory().stop();
        super.stop();
        return true;
    }

    // //////////////////////////////////////////////
    // update
    // //////////////////////////////////////////////

    public void update()
    {}

}
