package x360mediaserver.newServlet;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import x360mediaserver.Config;

/** Class to handle http stuff
 * @author Tom
 *
 */
public class WebServer {
	
	Server server; // the web server
	
	MediaServer mediaServer; // the media server
	
	
	Context context;
	
	
	public WebServer(MediaServer mediaServer)
    {
        server = new Server();
        this.mediaServer = mediaServer;
        ServletHolder holder = new ServletHolder(mediaServer);
        context = new Context();
        context.setContextPath("/");
        context.addServlet(holder, "/*");
        server.addHandler(context);
    }
	
	public void start()
    {
        System.out.println("Starting Server");
// while (true) {
        try
        {
            server.start();
// break;
        }
        catch (Exception e)
        {
            Config.out("Crashing Server");
            mediaServer.getUpnpResponder().stop();
            // TODO: Have the server (when they crash) popup a warning, then auto-open the config window.
        }
// }
    }

    public void stop()
    {
		try{
			server.stop();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	public void addConnector(String host,int port)
	{
		SocketConnector connector=new SocketConnector();
		connector.setHost(host);
		connector.setPort(port);
		server.addConnector(connector);
	}
	
}
