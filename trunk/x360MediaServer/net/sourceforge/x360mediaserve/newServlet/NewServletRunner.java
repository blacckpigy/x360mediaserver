package net.sourceforge.x360mediaserve.newServlet;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NewServletRunner {
	public static void main(String[] args)
	{
		try{	
			// apparently I'm meant to do this according to the GPL
			System.out.println("xbox360mediaserve, Copyright (C) 2006 Thomas Walker\n"+
					"xbox360mediaserve comes with ABSOLUTELY NO WARRANTY; for details see license\n"+
					"This is free software, and you are welcome to redistribute it\n"+
			"under certain conditions; see license for details.\n");


			System.out.println("OS Detected:"+System.getProperty("os.name"));

			
			

			MediaServer mediaServer;
			if(args.length>0){										
				// if we are given an address then use it				
				mediaServer=new MediaServer(InetAddress.getByName(args[0]).getHostAddress());
			}
			else{ 
				// use first interface as address								
				mediaServer=new MediaServer();
			}

			


		}
		catch(UnknownHostException e){
			System.out.println("The address specified was invalid");
		}
		catch(Exception e){
			System.out.println(e.toString());
			e.getStackTrace();
		}
	}

}
