package x360mediaserver.service;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

/**
 * Class for the creation of the system tray icon for the application. This class is expected to be
 * extended.
 * 
 * @author robinson
 */
public class TrayIcon implements ITrayIcon
{
    private Display  display     = Display.getDefault();
    private Shell    shell;
    private ToolTip  tip;
    private TrayItem systemTrayEntry;
    private Menu     menu;

    private Image    startIcon   = new Image(display,Service.class.getResourceAsStream("../../files/icon.png"));
    private Image    runningIcon = new Image(display,Service.class.getResourceAsStream("../../files/icon running.png"));

    /**
     * Creates a tray icon with the specified text
     * 
     * @param trayText
     */
    public TrayIcon(String trayText)
    {
        shell = new Shell(display);
        Tray trayBar = display.getSystemTray();
        tip = new ToolTip(shell, SWT.BALLOON | SWT.ICON_INFORMATION);

        if (trayBar != null)
        {
            systemTrayEntry = new TrayItem(trayBar, SWT.NONE);

            systemTrayEntry.setToolTip(tip);
            systemTrayEntry.setToolTipText(trayText);
            systemTrayEntry.setImage(startIcon);

            // off for now because it's annoying...
            // setTooltip(trayText, trayText + " service started!");

            menu = new Menu(getShell(), SWT.POP_UP);

            setupTray();
            setupMenu();
        }
        while ( !shell.isDisposed())
        {
            if ( !display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }

    /**
     * sets the tool-tip title, message, and location
     * 
     * @param title
     * @param message
     * @param location
     */
    public void setTooltip(String title, String message, int x, int y)
    {
        setTooltip(title, message, new Point(x, y));
    }

    /**
     * sets the tool-tip title, message, and location
     * 
     * @param title
     * @param message
     * @param location
     */
    public void setTooltip(String title, String message, Point location)
    {
        tip.setLocation(location);
        setTooltip(title, message);
    }

    /**
     * sets the tool-tip title and message
     * 
     * @param title
     * @param message
     */
    public void setTooltip(String title, String message)
    {
        tip.setText(title);
        setTooltipMessage(message);
    }

    /**
     * sets the tool-tip message
     * 
     * @param message
     */
    public void setTooltipMessage(String message)
    {
        tip.setMessage(message);
        tip.setVisible(true);
    }

    /**
     * @return the system tray icon
     */
    public TrayItem getTray()
    {
        return systemTrayEntry;
    }

    /**
     * @return the display associated with the tray icon
     */
    public Display getDisplay()
    {
        return display;
    }

    /**
     * @return the shell associated with the tray icon
     */
    public Shell getShell()
    {
        return shell;
    }

    /**
     * @return the tool-tip associated with the tray icon
     */
    public ToolTip getTip()
    {
        return tip;
    }

    public Menu getMenu()
    {
        return menu;
    }

    /**
     * @return the start icon
     */
    public Image getStartIcon()
    {
        return startIcon;
    }

    /**
     * @return the running icon
     */
    public Image getRunningIcon()
    {
        return runningIcon;
    }

    /**
     * Method to properly setup the drop down menu so that it has the "quit" entry.
     */
    public void setupMenu()
    {
        MenuItem menuItem = new MenuItem(menu, SWT.SEPARATOR);

        menuItem = new MenuItem(menu, SWT.PUSH);
        menuItem.setText("Quit");
        menuItem.addListener(SWT.Selection, new Listener()
        {
            public void handleEvent(Event e)
            {
                display.dispose();
                System.exit(0);
            }
        });
    }

    /**
     * Method to properly setup the tray icon mouse actions.
     */
    public void setupTray()
    {
        // right click action
        systemTrayEntry.addListener(SWT.MenuDetect, new Listener()
        {
            public void handleEvent(Event event)
            {
                display.dispose();
                System.exit(0);
            }
        });

        // Add menu detection listener to tray icon.
        systemTrayEntry.addListener(SWT.Selection, new Listener()
        {
            public void handleEvent(Event event)
            {
                menu.setVisible(true);
            }
        });
    }
}
