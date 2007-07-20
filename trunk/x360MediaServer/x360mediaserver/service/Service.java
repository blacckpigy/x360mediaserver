package x360mediaserver.service;

import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;

import x360mediaserver.Config;
import x360mediaserver.newServlet.MediaServer;
import x360mediaserver.service.AddressEntryWindow;
import x360mediaserver.service.TrayIcon;

/**
 * This class is the GUI front end to the x360MediaServe application.
 * @author robinson
 *
 */
public class Service extends TrayIcon
{
    private static final String DEFAULT_TRAY_NAME = "x360 MediaServe";
    private MediaServer mediaServer;
    
    public static void main(String...args)
    {
        Config.loadConfig();
        // TODO: Start the webserver automatically, so we can do the config thing remotely. (once the "tray icon" starts..
//        try
//        {
//           new MediaServer();
//        }
//        catch (BindException e)
//        { 
//        }
        new Service();
    }

    /**
     * initialises and creates the service tray icon.
     */
    public Service()
    {
        this(DEFAULT_TRAY_NAME);
    }
    public Service(String trayText)
    {
        super(trayText);
    }

    

    public void start()
    {
        try
        {
            mediaServer = new MediaServer();
        }
        catch (BindException e)
        {
            System.out.println("Address supplied not valid");
        }
    }

    public void start(String addy)
    {
        try
        {
            mediaServer = new MediaServer(InetAddress.getByName(addy).getHostAddress());
        }
        catch (BindException e)
        {
            System.out.println("Address supplied not valid");
        }
        catch (UnknownHostException e)
        {
            System.out.println("Address supplied not valid");
        }
    }

    /**
     * Stops the media server by properly closing the UPNP and web servers
     */
    public void stop()
    {
        if (mediaServer == null)
            return;
        
        if (mediaServer.getUpnpResponder() != null)
            mediaServer.getUpnpResponder().stop();
        
        if (mediaServer.getWebserver() != null)
            mediaServer.getWebserver().stop();
    }
    
    public void setupMenu()
    {
        MenuItem menuItem = new MenuItem(getMenu(), SWT.PUSH);
        menuItem.setText("Start Media Server");
        menuItem.addListener(SWT.Selection, new Listener()
        {
            public void handleEvent(Event e)
            {
                start();
                getTray().setImage(getRunningIcon());
            }
        });
        
        menuItem = new MenuItem(getMenu(), SWT.PUSH);
        menuItem.setText("Edit Media Server Address");
        menuItem.addListener(SWT.Selection, new Listener()
        {
            public void handleEvent(Event e)
            {
                AddressEntryWindow addressWindow = new AddressEntryWindow(getShell());
                // TODO: need a way to send the Config XML to the webserver....
                // then.. restart the webserver?
            }
        });
        
        menuItem = new MenuItem(getMenu(), SWT.PUSH);
        menuItem.setText("Connect to Media Server");
        menuItem.addListener(SWT.Selection, new Listener()
        {
            public void handleEvent(Event e)
            {
//                AddressEntryWindow addressWindow = new AddressEntryWindow(getShell());
//                String address = addressWindow.getAddress();
//                if (address != null && address.length() > 1)
//                    StartMediaServer(address);
            }
        });
        
        
        
        menuItem = new MenuItem(getMenu(), SWT.SEPARATOR);

        menuItem = new MenuItem(getMenu(), SWT.PUSH);
        menuItem.setText("Quit");
        menuItem.addListener(SWT.Selection, new Listener()
        {
            public void handleEvent(Event e)
            {
                if (mediaServer != null)
                {
                    stop();
                }
                getDisplay().dispose();
                getShell().dispose();
                System.exit(0);
            }
        });

//        menuItem = new MenuItem(getMenu(), SWT.PUSH);
//        menuItem.setText("Show Tooltip");
//        // Add tooltip visibility to menu item.
//        menuItem.addListener(SWT.Selection, new Listener()
//        {
//            public void handleEvent(Event e)
//            {
//                // Set the tooltip location manually.
//                tip.setLocation(100, 100);
//                tip.setVisible(true);
//            }
//        });
    }
}
