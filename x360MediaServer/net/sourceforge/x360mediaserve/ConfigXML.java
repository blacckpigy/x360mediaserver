package net.sourceforge.x360mediaserve;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.device.Description;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.xml.Node;
import org.cybergarage.xml.Parser;
import org.cybergarage.xml.ParserException;

public class ConfigXML
{
    public static final String FILES_CONFIG_XML      = "files/config.xml";
    public static final String FILES_DESCRIPTION_XML = "files/description.xml";

    protected static Node      configNode            = new Node("Configuration");
    protected static File      configFile            = new File(FILES_CONFIG_XML);
    private static Node        descriptionNode       = null;
    protected static File      descriptionFile       = new File(FILES_DESCRIPTION_XML);

    public static void loadConfig()
    {
        Parser parser = UPnP.getXMLParser();
        try
        {
            descriptionNode = parser.parse(descriptionFile);
            if (descriptionNode == null)
                System.err.println("Error loading description file. CRAP!" + Description.NOROOT_EXCEPTION);
        }
        catch (ParserException e)
        {
            System.err.println("Error loading description file. CRAP!");
        }

        try
        {
            configNode = parser.parse(configFile);
        }
        catch (ParserException e)
        {
            System.err.println("Error loading saved variables, using defaults.");
            Config.resetConfig();
        }

        Config.verifyDescriptionXML();
        ConfigWeb.start();
    }

    protected static void saveConfig()
    {
        saveConfig(configFile, configNode);
    }
    
    /**
     * This will try to save the config XML file to the disk.
     */
    protected static void saveConfig(File file, Node node)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.write(node.toString());
            writer.close();
        }
        catch (IOException e)
        {
            System.err.println("Open of " + file.toString() + " AND/OR save unsuccesful.");
        }
    }

    /**
     * single location to handle the node creation/setting and the config saving.
     * 
     * @param nodeName
     * @param nodeType
     */
    protected static void setupNode(String nodeName, String nodeType)
    {
        Node node = configNode.getNode(nodeType);
        if (node == null)
        {
            node = new Node(nodeType);
            configNode.addNode(node);
        }
        node.setValue(nodeName);
        saveConfig();
    }

    /**
     * @return the description Node
     */
    public static Node getDescriptionNode()
    {
        return descriptionNode;
    }
    
    /**
     * @return the description file
     */
    public static File getDescriptionFile()
    {
        return descriptionFile;
    }
}
