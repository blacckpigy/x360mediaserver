package x360mediaserver.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;

import x360mediaserver.Config;
import x360mediaserver.newServlet.MediaServer;

/**
 * This class is the GUI front end to the x360MediaServe application.
 * @author robinson
 *
 */
public class Service extends TrayIcon
{
    private static final String DEFAULT_TRAY_NAME = "x360 MediaServe";
    
    public static void main(String[] args)
    {
        Config.loadConfig();
        new MediaServer();
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

    

    public void StartMediaServer()
    {
        MediaServer mediaServer = new MediaServer();
    }

    public void StartMediaServer(String addy)
    {
        try
        {
            MediaServer mediaServer = new MediaServer(InetAddress.getByName(addy).getHostAddress());
        }
        catch (UnknownHostException e)
        {
            System.out.println("Address supplied not valid");
        }
    }

    public void setupMenu()
    {
        MenuItem menuItem = new MenuItem(getMenu(), SWT.PUSH);
        menuItem.setText("Start Media Server");
        menuItem.addListener(SWT.Selection, new Listener()
        {
            public void handleEvent(Event e)
            {
                StartMediaServer();
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
        super.setupMenu();

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
