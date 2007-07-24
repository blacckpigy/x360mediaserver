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

import java.net.BindException;

import org.cybergarage.upnp.device.InvalidDescriptionException;

import x360mediaserver.Config;
import x360mediaserver.upnpmediaserver.responder.UPNPResponder;

/**
 * A new hacked servlet that includes the other servlets. The idea being to allow a single configure
 * class to manage all of them
 * 
 * @author tom
 */
public class MediaServer
{
    // TODO maybe move servlet functionality into a smaller class to simplify things

    private UPNPResponder                   upnpResponder;

    public MediaServer() throws BindException
    {
        super();
        System.out.println("Starting media server servlet");
        setup();
    }

    
    public MediaServer(String externalAddress) throws BindException
    {
        super();

        System.out.println("Address set to:" + externalAddress);
        Config.setAddress(externalAddress);
        setup();
    }

    
    public MediaServer(String externalAddress, int port) throws BindException
    {
        super();

        System.out.println("Starting media server servlet with address:" + externalAddress +
                           " and port:" + port);
        Config.setAddress(externalAddress);
        Config.setPort(port);
        
        setup();
    }
    
    public void setup() throws BindException
    {
        setupUPNPServer();
//        setupWebServer();
    }
    
    private void setupUPNPServer()
    {
        try
        {
            Config.out("Setting up UPNP responder");
            upnpResponder = new UPNPResponder(Config.getDescriptionNode(), Config.getContent_directoryNode(),
                                              Config.getConnection_managerNode(),
                                              Config.getMedia_reciever_registrarNode(), Config.getAddress(),
                                              Config.getPort());
            
            // Don't forget to shutdown the server after starting it up.... (also the webserver... -- have to "flush bufferes?")
            upnpResponder.start(); 
        }
        catch (InvalidDescriptionException e)
        { 
            System.err.println("Problem setting up the UPNP Responder server.");
            // e.printStackTrace();
        }
    }

//    private void setupWebServer() throws BindException
//    {
//        webserver = new WebServer(this);
//        int port = Config.getPort();
//        webserver.addConnector(Config.getAddress(), port);
//        webserver.addConnector("127.0.0.1", port);
//        webserver.start();
//    }
//
//    
//    
//    
//    // Stuff borrowed from Cybergate
//    // this happens when we select "streams" to be played..
//    // This is for HTTP traffic as well.
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//    {
//        debug("MEDIA SERVER: Do Post:" + req.getPathInfo());
//
//        try
//        {
////            if (req.getPathInfo().contains("ContentDirectory"))
////            {
////                debug("Sending to content dir");
////                Config.getContentDirectory().doPost(req, resp);
////            }
////            else if (req.getPathInfo().contains("X_MS_MediaReceiverRegistrar"))
////            {
////                debug("Sending to media reciever");
////                Config.getMediaReceiverReg().doPost(req, resp);
////            }
//            else if (req.getPathInfo().contains("configure"))
//            {
//                System.out.println("Doing configure");
//                ConfigWeb.doPost(req, resp);
//            }
//        }
//        catch (Exception e)
//        {
//            debug(e.toString());
//        }
//    }

//    /**
//     * HTTP requests come in here. This is the backbone for the http webservice
//     * KEYWORD NATHAN HTTP
//     */
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//    {
//        System.out.println("MEDIA SERVER: doGet: " + req.getPathInfo());
//        try
//        {
//            if (req.getPathInfo().contains("ContentDirectory"))
//            {
//                Config.out("Got path:" + Config.getUrl("ContentDirectory"));
//                Config.getContentDirectory().doGet(req, resp);
//            }
//            else if (req.getPathInfo().contains("X_MS_MediaReceiverRegistrar"))
//            {
//                Config.out("Got media:" + Config.getUrl("X_MS_MediaReceiverRegistrar"));
//                Config.getMediaReceiverReg().doGet(req, resp);
//            }
//            else if (req.getPathInfo().contains("config"))
//            {
//                Config.out("Got config:" + Config.getUrl("config"));
//                ConfigWeb.doGet(req, resp);
//            }
//            else // if (req.getPathInfo().contains(Config.getUrl("MediaServer")))
//            {
//                Config.out("Got device description:" + req);
//                PrintWriter writer = resp.getWriter();
//                writer.write(UPnP.XML_DECLARATION + "\n");
//                writer.write(Config.getDescriptionNode().toString());
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            debug(e.toString());
//        }
//    }

    private void debug(String str)
    {
        System.err.println(str);
    }
    
    public UPNPResponder getUpnpResponder()
    {
        return upnpResponder;
    }
}
