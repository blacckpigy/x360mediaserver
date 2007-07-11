/**
 * one line to give the program's name and an idea of what it does. Copyright (C) 2006 Thomas Walker
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 * have received a copy of the GNU General Public License along with this program; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package x360mediaserver.newServlet;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cybergarage.upnp.device.InvalidDescriptionException;

import x360mediaserver.Config;
import x360mediaserver.ConfigWeb;
import x360mediaserver.upnpmediaserver.responder.UPNPResponder;

/**
 * A new hacked servlet that includes the other servlets. The idea being to allow a single configure
 * class to manage all of them
 * 
 * @author tom
 */
public class MediaServer extends HttpServlet
{
    // TODO maybe move servlet functionality into a smaller class to simplify things

    WebServer                       webserver;

    UPNPResponder                   upnpResponder;

    public MediaServer()
    {
        super();
        System.out.println("Starting media server servlet");
        setup();
    }

    
    public MediaServer(String externalAddress)
    {
        super();

        System.out.println("Address set to:" + externalAddress);
        Config.setAddress(externalAddress);
        setup();
    }

    
    public MediaServer(String externalAddress, int port)
    {
        super();

        System.out.println("Starting media server servlet with address:" + externalAddress +
                           " and port:" + port);
        Config.setAddress(externalAddress);
        Config.setPort(port);
        
        setup();
    }
    
    public void setup()
    {
        setupUPNPServer();
        setupWebServer();
    }
    
    private void setupUPNPServer()
    {
        try
        {
            Config.out("Setting up UPNP responder");
            upnpResponder = new UPNPResponder(Config.getDescriptionNode(), CONTENT_DIRECTORY_SCPD,
                                              CONNECTION_MANAGER_SCPD,
                                              MEDIA_RECEIVER_REGISTRAR_SCPD, Config.getAddress(),
                                              Config.getPort());
            upnpResponder.start();
        }
        catch (InvalidDescriptionException e)
        { 
            System.err.println("Problem setting up the UPNP Responder server.");
            // e.printStackTrace();
        }
    }

    private void setupWebServer()
    {
        webserver = new WebServer(this);
        int port = Config.getPort();
        webserver.addConnector(Config.getAddress(), port);
        webserver.addConnector("127.0.0.1", port);
        webserver.start();
    }

    
    
    
    // Stuff borrowed from Cybergate
    // this happens when we select "streams" to be played..
    // This is for HTTP traffic as well.
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    {
        debug("Do Post: " + req.getPathInfo());

        try
        {
            if (req.getPathInfo().contains(Config.contentDirectoryPath))
            {
                debug("Sending to content dir");
                Config.contentDirectory.doPost(req, resp);
            }
            else if (req.getPathInfo().contains(Config.mediaRecRegPath))
            {
                debug("Sending to media reciever");
                Config.mediaReceiverReg.doPost(req, resp);
            }
            else if (req.getPathInfo().contains("configure"))
            {
                System.out.println("Doing configure");
                ConfigWeb.doPost(req, resp);
            }
        }
        catch (Exception e)
        {
            debug(e.toString());
        }
    }

    /**
     * HTTP requests come in here. This is the backbone for the http webservice
     * KEYWORD NATHAN HTTP
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    {
        try
        {
            System.out.println("doGet: " + req.getPathInfo());
            if (req.getPathInfo().contains(Config.contentDirectoryPath))
            {
                System.out.println("Got path:" + Config.contentDirectoryPath);
                Config.contentDirectory.doGet(req, resp);
            }
            else if (req.getPathInfo().contains(Config.mediaRecRegPath))
            {
                System.out.println("Got media:" + Config.mediaRecRegPath);
                Config.mediaReceiverReg.doGet(req, resp);
            }
            else if (req.getPathInfo().contains(Config.configurePath))
            {
                System.out.println("Got config:" + Config.configurePath);
                ConfigWeb.doGet(req, resp);
            }
            else
            {
                System.out.println("Got ???:" + req);
                PrintWriter writer = resp.getWriter();
                writer.write("<?xml version=\"1.0\"?>\n");
                writer.write(Config.getDescriptionNode().toString());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            debug(e.toString());
        }
    }

    private void debug(String str)
    {
        System.err.println(str);
    }
    
    
    public final static String CONTENT_DIRECTORY_SCPD = 
        "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + 
        "<scpd xmlns=\"urn:schemas-upnp-org:service-1-0\">\n" + 
        "   <specVersion>\n" + 
        "      <major>1</major>\n" + 
        "      <minor>0</minor>\n" + 
        "   </specVersion>\n" + 
        "   <actionList>\n" + 
        "      <action>\n" + 
        "         <name>ExportResource</name>\n" + 
        "         <argumentList>\n" + 
        "            <argument>\n" + 
        "               <name>SourceURI</name>\n" + 
        "               <direction>in</direction>\n" + 
        "               <relatedStateVariable>A_ARG_TYPE_URI</relatedStateVariable>\n" + 
        "            </argument>\n" + 
        "            <argument>\n" + 
        "               <name>DestinationURI</name>\n" + 
        "               <direction>in</direction>\n" + 
        "               <relatedStateVariable>A_ARG_TYPE_URI</relatedStateVariable>\n" + 
        "            </argument>\n" + 
        "            <argument>\n" + 
        "               <name>TransferID</name>\n" + 
        "               <direction>out</direction>\n" + 
        "               <relatedStateVariable>A_ARG_TYPE_TransferID</relatedStateVariable>\n" + 
        "            </argument>\n" + 
        "         </argumentList>\n" + 
        "      </action>\n" + 
        "      <action>\n" + 
        "         <name>StopTransferResource</name>\n" + 
        "         <argumentList>\n" + 
        "            <argument>\n" + 
        "               <name>TransferID</name>\n" + 
        "               <direction>in</direction>\n" + 
        "               <relatedStateVariable>A_ARG_TYPE_TransferID</relatedStateVariable>\n" + 
        "            </argument>\n" + 
        "         </argumentList>\n" + 
        "      </action>\n" + 
        "      <action>\n" + 
        "         <name>DestroyObject</name>\n" + 
        "         <argumentList>\n" + 
        "            <argument>\n" + 
        "               <name>ObjectID</name>\n" + 
        "               <direction>in</direction>\n" + 
        "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" + 
        "            </argument>\n" + 
        "         </argumentList>\n" + 
        "      </action>\n" + 
        "      <action>\n" + 
        "         <name>DeleteResource</name>\n" + 
        "         <argumentList>\n" + 
        "            <argument>\n" + 
        "               <name>ResourceURI</name>\n" + 
        "               <direction>in</direction>\n" + 
        "               <relatedStateVariable>A_ARG_TYPE_URI</relatedStateVariable>\n" + 
        "            </argument>\n" + 
        "         </argumentList>\n" + 
        "      </action>\n" + 
        "      <action>\n" + 
        "         <name>UpdateObject</name>\n" + 
        "         <argumentList>\n" + 
        "            <argument>\n" + 
        "               <name>ObjectID</name>\n" + 
        "               <direction>in</direction>\n" + 
        "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" + 
        "            </argument>\n" + 
        "            <argument>\n" + 
        "               <name>CurrentTagValue</name>\n" + 
        "               <direction>in</direction>\n" + 
        "               <relatedStateVariable>A_ARG_TYPE_TagValueList</relatedStateVariable>\n" + 
        "            </argument>\n" + 
        "            <argument>\n" + 
        "               <name>NewTagValue</name>\n" + 
        "               <direction>in</direction>\n" + 
        "               <relatedStateVariable>A_ARG_TYPE_TagValueList</relatedStateVariable>\n" + 
        "            </argument>\n" + 
        "         </argumentList>\n" + 
        "      </action>\n" + 
        "      <action>\n" + 
        "         <name>Browse</name>\n" + 
        "         <argumentList>\n" + 
        "            <argument>\n" + 
        "               <name>ObjectID</name>\n" + 
        "               <direction>in</direction>\n" + 
        "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" + 
        "            </argument>\n" + 
        "            <argument>\n" + 
        "               <name>BrowseFlag</name>\n" + 
        "               <direction>in</direction>\n" + 
        "               <relatedStateVariable>A_ARG_TYPE_BrowseFlag</relatedStateVariable>\n" + 
        "            </argument>\n" + 
        "            <argument>\n" + 
        "               <name>Filter</name>\n" + 
        "               <direction>in</direction>\n" + 
        "               <relatedStateVariable>A_ARG_TYPE_Filter</relatedStateVariable>\n" + 
        "            </argument>\n" + 
        "            <argument>\n" + 
        "               <name>StartingIndex</name>\n" + 
        "               <direction>in</direction>\n" + 
        "               <relatedStateVariable>A_ARG_TYPE_Index</relatedStateVariable>\n" + 
        "            </argument>\n" + 
        "            <argument>\n" + 
        "               <name>RequestedCount</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>SortCriteria</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_SortCriteria</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>Result</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_Result</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>NumberReturned</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>TotalMatches</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>UpdateID</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_UpdateID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "         </argumentList>\n" +
        "      </action>\n" +
        "      <action>\n" +
        "         <name>GetTransferProgress</name>\n" +
        "         <argumentList>\n" +
        "            <argument>\n" +
        "               <name>TransferID</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_TransferID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>TransferStatus</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_TransferStatus</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>TransferLength</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_TransferLength</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>TransferTotal</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_TransferTotal</relatedStateVariable>\n" +
        "            </argument>\n" +
        "         </argumentList>\n" +
        "      </action>\n" +
        "      <action>\n" +
        "         <name>GetSearchCapabilities</name>\n" +
        "         <argumentList>\n" +
        "            <argument>\n" +
        "               <name>SearchCaps</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>SearchCapabilities</relatedStateVariable>\n" +
        "            </argument>\n" +
        "         </argumentList>\n" +
        "      </action>\n" +
        "      <action>\n" +
        "         <name>CreateObject</name>\n" +
        "         <argumentList>\n" +
        "            <argument>\n" +
        "               <name>ContainerID</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>Elements</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_Result</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>ObjectID</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>Result</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_Result</relatedStateVariable>\n" +
        "            </argument>\n" +
        "         </argumentList>\n" +
        "      </action>\n" +
        "      <action>\n" +
        "         <name>Search</name>\n" +
        "         <argumentList>\n" +
        "            <argument>\n" +
        "               <name>ContainerID</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>SearchCriteria</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_SearchCriteria</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>Filter</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_Filter</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>StartingIndex</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_Index</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>RequestedCount</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>SortCriteria</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_SortCriteria</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>Result</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_Result</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>NumberReturned</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>TotalMatches</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>UpdateID</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_UpdateID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "         </argumentList>\n" +
        "      </action>\n" +
        "      <action>\n" +
        "         <name>GetSortCapabilities</name>\n" +
        "         <argumentList>\n" +
        "            <argument>\n" +
        "               <name>SortCaps</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>SortCapabilities</relatedStateVariable>\n" +
        "            </argument>\n" +
        "         </argumentList>\n" +
        "      </action>\n" +
        "      <action>\n" +
        "         <name>ImportResource</name>\n" +
        "         <argumentList>\n" +
        "            <argument>\n" +
        "               <name>SourceURI</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_URI</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>DestinationURI</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_URI</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>TransferID</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_TransferID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "         </argumentList>\n" +
        "      </action>\n" +
        "      <action>\n" +
        "         <name>CreateReference</name>\n" +
        "         <argumentList>\n" +
        "            <argument>\n" +
        "               <name>ContainerID</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>ObjectID</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>NewID</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "         </argumentList>\n" +
        "      </action>\n" +
        "      <action>\n" +
        "         <name>GetSystemUpdateID</name>\n" +
        "         <argumentList>\n" +
        "            <argument>\n" +
        "              <name>Id</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>SystemUpdateID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "         </argumentList>\n" +
        "      </action>\n" +
        "   </actionList>\n" +
        "   <serviceStateTable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_SortCriteria</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_TransferLength</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"yes\">\n" +
        "         <name>TransferIDs</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_UpdateID</name>\n" +
        "         <dataType>ui4</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_SearchCriteria</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_Filter</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"yes\">\n" +
        "         <name>ContainerUpdateIDs</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_Result</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_Index</name>\n" +
        "         <dataType>ui4</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_TransferID</name>\n" +
        "         <dataType>ui4</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_TagValueList</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_URI</name>\n" +
        "         <dataType>uri</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_ObjectID</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>SortCapabilities</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>SearchCapabilities</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_Count</name>\n" +
        "         <dataType>ui4</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_BrowseFlag</name>\n" +
        "         <dataType>string</dataType>\n" +
        "         <allowedValueList>\n" +
        "            <allowedValue>BrowseMetadata</allowedValue>\n" +
        "            <allowedValue>BrowseDirectChildren</allowedValue>\n" +
        "         </allowedValueList>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"yes\">\n" +
        "         <name>SystemUpdateID</name>\n" +
        "         <dataType>ui4</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_TransferStatus</name>\n" +
        "         <dataType>string</dataType>\n" +
        "         <allowedValueList>\n" +
        "            <allowedValue>COMPLETED</allowedValue>\n" +
        "            <allowedValue>ERROR</allowedValue>\n" +
        "            <allowedValue>IN_PROGRESS</allowedValue>\n" +
        "            <allowedValue>STOPPED</allowedValue>\n" +
        "         </allowedValueList>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_TransferTotal</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "   </serviceStateTable>\n" +
        "</scpd>";
    
    public final static String MEDIA_RECEIVER_REGISTRAR_SCPD = 
        "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
        "<scpd xmlns=\"urn:schemas-upnp-org:service-1-0\">\n" +
        "   <specVersion>\n" +
        "      <major>1</major>\n" +
        "      <minor>0</minor>\n" +
        "   </specVersion>\n" +
        "   <actionList>\n" +
        "       <action>\n" +
        "         <name>IsAuthorized</name>\n" +
        "         <argumentList>\n" +
        "            <argument>\n" +
        "               <name>DeviceID</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_DeviceID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>Result</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_Result</relatedStateVariable>\n" +
        "            </argument>\n" +
        "         </argumentList>\n" +
        "      </action>\n" +
        "      <action>\n" +
        "         <name>RegisterDevice</name>\n" +
        "         <argumentList>\n" +
        "            <argument>\n" +
        "               <name>RegistrationReqMsg</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_RegistrationReqMsg</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>RegistrationRespMsg</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_RegistrationRespMsg</relatedStateVariable>\n" +
        "            </argument>\n" +
        "         </argumentList>\n" +
        "      </action>\n" +
        "      <action>\n" +
        "         <name>IsValidated</name>\n" +
        "         <argumentList>\n" +
        "            <argument>\n" +
        "               <name>DeviceID</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_DeviceID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>Result</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_Result</relatedStateVariable>\n" +
        "            </argument>\n" +
        "         </argumentList>\n" +
        "      </action>\n" +
        "   </actionList>\n" +
        "   <serviceStateTable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_DeviceID</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_Result</name>\n" +
        "         <dataType>int</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_RegistrationReqMsg</name>\n" +
        "         <dataType>bin.base64</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_RegistrationRespMsg</name>\n" +
        "         <dataType>bin.base64</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>AuthorizationGrantedUpdateID</name>\n" +
        "         <dataType>ui4</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>AuthorizationDeniedUpdateID</name>\n" +
        "         <dataType>ui4</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>ValidationSucceededUpdateID</name>\n" +
        "         <dataType>ui4</dataType>\n" +
        "      </stateVariable>\n" +        
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>ValidationRevokedUpdateID</name>\n" +
        "         <dataType>ui4</dataType>\n" +
        "      </stateVariable>\n" +            
        "   </serviceStateTable>\n" +
        "</scpd>";
    
    
    public final static String CONNECTION_MANAGER_SCPD = 
        "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
        "<scpd xmlns=\"urn:schemas-upnp-org:service-1-0\">\n" +
        "   <specVersion>\n" +
        "      <major>1</major>\n" +
        "      <minor>0</minor>\n" +
        "   </specVersion>\n" +
        "   <actionList>\n" +
        "       <action>\n" +
        "         <name>GetCurrentConnectionInfo</name>\n" +
        "         <argumentList>\n" +
        "            <argument>\n" +
        "               <name>ConnectionID</name>\n" +
        "               <direction>in</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_ConnectionID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>RcsID</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_RcsID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>AVTransportID</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_AVTransportID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>ProtocolInfo</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_ProtocolInfo</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>PeerConnectionManager</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_ConnectionManager</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>PeerConnectionID</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_ConnectionID</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>Direction</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_Direction</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>Status</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>A_ARG_TYPE_ConnectionStatus</relatedStateVariable>\n" +
        "            </argument>\n" +
        "         </argumentList>\n" +
        "      </action>\n" +
        "      <action>\n" +
        "         <name>GetProtocolInfo</name>\n" +
        "         <argumentList>\n" +
        "            <argument>\n" +
        "               <name>Source</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>SourceProtocolInfo</relatedStateVariable>\n" +
        "            </argument>\n" +
        "            <argument>\n" +
        "               <name>Sink</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>SinkProtocolInfo</relatedStateVariable>\n" +
        "            </argument>\n" +
        "         </argumentList>\n" +
        "      </action>\n" +
        "      <action>\n" +
        "         <name>GetCurrentConnectionIDs</name>\n" +
        "         <argumentList>\n" +
        "            <argument>\n" +
        "               <name>ConnectionIDs</name>\n" +
        "               <direction>out</direction>\n" +
        "               <relatedStateVariable>CurrentConnectionIDs</relatedStateVariable>\n" +
        "            </argument>\n" +
        "         </argumentList>\n" +
        "      </action>\n" +
        "   </actionList>\n" +
        "   <serviceStateTable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_ProtocolInfo</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_ConnectionStatus</name>\n" +
        "         <dataType>string</dataType>\n" +
        "         <allowedValueList>\n" +
        "            <allowedValue>OK</allowedValue>\n" +
        "            <allowedValue>ContentFormatMismatch</allowedValue>\n" +
        "            <allowedValue>InsufficientBandwidth</allowedValue>\n" +
        "            <allowedValue>UnreliableChannel</allowedValue>\n" +
        "            <allowedValue>Unknown</allowedValue>\n" +
        "         </allowedValueList>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_AVTransportID</name>\n" +
        "         <dataType>i4</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_RcsID</name>\n" +
        "         <dataType>i4</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_ConnectionID</name>\n" +
        "         <dataType>i4</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_ConnectionManager</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"yes\">\n" +
        "         <name>SourceProtocolInfo</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"yes\">\n" +
        "         <name>SinkProtocolInfo</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"no\">\n" +
        "         <name>A_ARG_TYPE_Direction</name>\n" +
        "         <dataType>string</dataType>\n" +
        "         <allowedValueList>\n" +
        "            <allowedValue>Input</allowedValue>\n" +
        "            <allowedValue>Output</allowedValue>\n" +
        "         </allowedValueList>\n" +
        "      </stateVariable>\n" +
        "      <stateVariable sendEvents=\"yes\">\n" +
        "         <name>CurrentConnectionIDs</name>\n" +
        "         <dataType>string</dataType>\n" +
        "      </stateVariable>\n" +
        "   </serviceStateTable>\n" +
        "</scpd>";
}
