/**
 * one line to give the program's name and an idea of what it does.
 Copyright (C) 2006  Thomas Walker

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */


import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import x360mediaserver.newServlet.MediaServer;
import x360mediaserver.service.Service;


/** Class to run server
 * @author Tom
 *
 */
public class Run {
	public static void main(String[] args)
	{
	    System.out.println("xbox360mediaserve, Copyright (C) 2006 Thomas Walker\n"+
				"xbox360mediaserve comes with ABSOLUTELY NO WARRANTY; for details see license\n"+
				"This is free software, and you are welcome to redistribute it\n"+
		"under certain conditions; see license for details.\n");


		System.out.println("OS Detected:" + System.getProperty("os.name"));

        MediaServer mediaServer;
        if (args.length > 0)
        {
            // if we are given an address then use it
            try
            {
                mediaServer = new MediaServer(InetAddress.getByName(args[0]).getHostAddress());
            }
            catch (BindException e)
            {
            }
            catch (UnknownHostException e)
            {
                System.out.println("Address supplied not valid");
            }
        }
        else
        {
            try
            {
                // use first interface as address
                mediaServer = new MediaServer();
            }
            catch (BindException e)
            {
            }
        }
    }
}
