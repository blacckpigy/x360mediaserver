package net.sourceforge.x360mediaserve.newServlet;

import java.net.BindException;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

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
		server= new Server();
		this.mediaServer=mediaServer;
		ServletHolder holder=new ServletHolder(mediaServer);
		context= new Context();
		context.setContextPath("/");
		context.addServlet(holder, "/*");
		server.addHandler(context);
	}
	
	public void start(){
			System.out.println("Starting Server");
				try
                {
                    server.start();
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
	}
	
	public void stop(){
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
