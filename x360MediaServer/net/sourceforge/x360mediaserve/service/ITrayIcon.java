package net.sourceforge.x360mediaserve.service;

/**
 * Interface for assistance in the creation of the system tray icon.
 * @author robinson
 *
 */
public interface ITrayIcon
{
    
    /**
     * Necessary hook provided in order to properly setup and configure the tray icon mouse actions.
     */
    public void setupTray();
    
    /**
     * Necessary hook provided in order to properly setup and configure the tray icon drop down menu.
     */
    public void setupMenu();
}
