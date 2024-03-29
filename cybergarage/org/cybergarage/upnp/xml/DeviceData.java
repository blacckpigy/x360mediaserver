/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002-2003
 *
 *	File: DeviceData.java
 *
 *	Revision;
 *
 *	03/28/03
 *		- first revision.
 *	12/25/03
 *		- Added Advertiser functions.
 *
 ******************************************************************/

package org.cybergarage.upnp.xml;

import java.io.File;
import java.net.InetAddress;

import org.cybergarage.http.HTTPServerList;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.device.Advertiser;
import org.cybergarage.upnp.ssdp.SSDP;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.upnp.ssdp.SSDPSearchSocketList;
import org.cybergarage.util.ListenerList;

public class DeviceData extends NodeData
{
    public DeviceData()
    {}

    // //////////////////////////////////////////////
    // description
    // //////////////////////////////////////////////

    private String descriptionURI  = null;
    private File   descriptionFile = null;

    public File getDescriptionFile()
    {
        return descriptionFile;
    }

    public String getDescriptionURI()
    {
        return descriptionURI;
    }

    public void setDescriptionFile(File descriptionFile)
    {
        this.descriptionFile = descriptionFile;
    }

    public void setDescriptionURI(String descriptionURI)
    {
        this.descriptionURI = descriptionURI;
    }

    // //////////////////////////////////////////////
    // description
    // //////////////////////////////////////////////

    private String location = "";

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    // //////////////////////////////////////////////
    // LeaseTime
    // //////////////////////////////////////////////

    private int leaseTime = Device.DEFAULT_LEASE_TIME;

    public int getLeaseTime()
    {
        return leaseTime;
    }

    public void setLeaseTime(int val)
    {
        leaseTime = val;
    }

    // //////////////////////////////////////////////
    // HTTPServer
    // //////////////////////////////////////////////

    private HTTPServerList httpServerList = null;

    public HTTPServerList getHTTPServerList()
    {
        if (this.httpServerList == null)
        {
            this.httpServerList = new HTTPServerList(this.httpBinds, this.httpPort);
        }
        return this.httpServerList;
    }

    private InetAddress[] httpBinds = null;

    public void setHTTPBindAddress(InetAddress[] inets)
    {
        this.httpBinds = inets;
    }

    public InetAddress[] getHTTPBindAddress()
    {
        return this.httpBinds;
    }

    // //////////////////////////////////////////////
    // httpPort
    // //////////////////////////////////////////////

    private int httpPort = Device.HTTP_DEFAULT_PORT;

    public int getHTTPPort()
    {
        return httpPort;
    }

    public void setHTTPPort(int port)
    {
        httpPort = port;
    }

    // //////////////////////////////////////////////
    // controlActionListenerList
    // //////////////////////////////////////////////

    private ListenerList controlActionListenerList = new ListenerList();

    public ListenerList getControlActionListenerList()
    {
        return controlActionListenerList;
    }

/*
 * public void setControlActionListenerList(ListenerList controlActionListenerList) {
 * this.controlActionListenerList = controlActionListenerList; }
 */

    // //////////////////////////////////////////////
    // SSDPSearchSocket
    // //////////////////////////////////////////////
    private SSDPSearchSocketList ssdpSearchSocketList = null;
    private String               ssdpMulticastIPv4    = SSDP.ADDRESS;
    private String               ssdpMulticastIPv6    = SSDP.getIPv6Address();
    private int                  ssdpPort             = SSDP.PORT;
    private InetAddress[]        ssdpBinds            = null;

    public SSDPSearchSocketList getSSDPSearchSocketList()
    {
        if (this.ssdpSearchSocketList == null)
        {
            this.ssdpSearchSocketList = new SSDPSearchSocketList(this.ssdpBinds, ssdpPort,
                                                                 ssdpMulticastIPv4,
                                                                 ssdpMulticastIPv6);
        }
        return ssdpSearchSocketList;
    }

    /**
     * @param port
     *            The port to use for binding the SSDP service
     */
    public void setSSDPPort(int port)
    {
        this.ssdpPort = port;
    }

    /**
     * @return The port to use for binding the SSDP service
     */
    public int getSSDPPort()
    {
        return this.ssdpPort;
    }

    /**
     * @param inets
     *            The <tt>InetAddress</tt> that will be binded for this service. Use
     *            <code>null</code> for the default behaviur
     */
    public void setSSDPBindAddress(InetAddress[] inets)
    {
        this.ssdpBinds = inets;
    }

    /**
     * @return inets The <tt>InetAddress</tt> that will be binded for this service
     *         <code>null</code> means that defulat behaviur will be used
     */
    public InetAddress[] getSSDPBindAddress()
    {
        return this.ssdpBinds;
    }

    /**
     * @param ip
     *            The IPv4 address used for Multicast comunication
     */
    public void setMulticastIPv4Address(String ip)
    {
        this.ssdpMulticastIPv4 = ip;
    }

    /**
     * @return The IPv4 address used for Multicast comunication
     */
    public String getMulticastIPv4Address()
    {
        return this.ssdpMulticastIPv4;
    }

    /**
     * @param ip
     *            The IPv address used for Multicast comunication
     */
    public void setMulticastIPv6Address(String ip)
    {
        this.ssdpMulticastIPv6 = ip;
    }

    /**
     * @return The IPv address used for Multicast comunication
     */
    public String getMulticastIPv6Address()
    {
        return this.ssdpMulticastIPv6;
    }

    // //////////////////////////////////////////////
    // SSDPPacket
    // //////////////////////////////////////////////

    private SSDPPacket ssdpPacket = null;

    public SSDPPacket getSSDPPacket()
    {
        return ssdpPacket;
    }

    public void setSSDPPacket(SSDPPacket packet)
    {
        ssdpPacket = packet;
    }

    // //////////////////////////////////////////////
    // Advertiser
    // //////////////////////////////////////////////

    private Advertiser advertiser = null;

    public void setAdvertiser(Advertiser adv)
    {
        advertiser = adv;
    }

    public Advertiser getAdvertiser()
    {
        return advertiser;
    }

}
