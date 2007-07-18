package x360mediaserver;

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

import x360mediaserver.upnpmediaserver.mediareceiverregistrar.MediaReceiverRegistrar;
import x360mediaserver.upnpmediaserver.upnp.contentdirectory.ContentDirectory;
import x360mediaserver.utils.StringUtils;

public class ConfigXML
{
    public static final String FILES_DIR       = "files";      
    public static final String CONFIG_XML      = "/config.xml";
    public static final String DESCRIPTION_XML = "/description.xml";
    public static final String CONTENT_DIRECTORY_XML = "/content_directory.xml";
    public static final String MEDIA_RECIEVER_REGISTRAR_XML = "/media_reciever_registrar.xml";
    public static final String CONNECTION_MANAGER_XML = "/connection_manager.xml";

    protected static Node      configNode            = new Node("Configuration");
    protected static File      configFile            = new File(FILES_DIR + CONFIG_XML);

    private static Node        descriptionNode       = null;
    protected static File      descriptionFile       = new File(FILES_DIR + DESCRIPTION_XML);
    
    private static Node        content_directoryNode       = null;
    protected static File      content_directoryFile       = new File(FILES_DIR + CONTENT_DIRECTORY_XML);
    
    private static Node        media_reciever_registrarNode       = null;
    protected static File      media_reciever_registrarFile       = new File(FILES_DIR + MEDIA_RECIEVER_REGISTRAR_XML);
    
    private static Node        connection_managerNode       = null;
    private static File      connection_managerFile       = new File(FILES_DIR + CONNECTION_MANAGER_XML);
    
    
    private static ContentDirectory       contentDirectory = null;
    private static MediaReceiverRegistrar mediaReceiverReg = null;

    
    
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
            // then if it's bad, then there is NOTHING we can do!!
            System.err.println("Error loading description file. CRAP!");
            System.exit(0);
        }

        try
        {
            content_directoryNode = parser.parse(content_directoryFile);
            if (content_directoryNode == null)
                System.err.println("Error loading content_directoryNode file. CRAP!" + Description.NOROOT_EXCEPTION);
        }
        catch (ParserException e)
        {
            System.err.println("Error loading content_directoryNode file. CRAP!");
        }
        
        try
        {
            media_reciever_registrarNode = parser.parse(media_reciever_registrarFile);
            if (media_reciever_registrarNode == null)
                System.err.println("Error loading media_reciever_registrarFile file. CRAP!" + Description.NOROOT_EXCEPTION);
        }
        catch (ParserException e)
        {
            System.err.println("Error loading media_reciever_registrarFile file. CRAP!");
        }
        
        try
        {
            connection_managerNode = parser.parse(connection_managerFile);
            if (connection_managerNode == null)
                System.err.println("Error loading connection_managerFile file. CRAP!" + Description.NOROOT_EXCEPTION);
        }
        catch (ParserException e)
        {
            System.err.println("Error loading connection_managerFile file. CRAP!");
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


        contentDirectory = new ContentDirectory(content_directoryNode, Config.getUrl("ContentDirectory"));
        mediaReceiverReg = new MediaReceiverRegistrar(media_reciever_registrarNode, Config.getUrl("X_MS_MediaReceiverRegistrar"));

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
     * @param nodeValue
     * @param nodeName
     */
    protected static void setupNode(String nodeValue, String nodeName)
    {
        Node node = configNode.getNode(nodeName);
        if (node == null)
        {
            node = new Node(nodeName);
            configNode.addNode(node);
        }
        if (!node.getValue().equals(nodeValue))
        {
            node.setValue(nodeValue);
            saveConfig();
        }
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

    /**
     * @return the content_directoryNode
     */
    public static Node getContent_directoryNode()
    {
        return content_directoryNode;
    }

    /**
     * @return the media_reciever_registrarNode
     */
    public static Node getMedia_reciever_registrarNode()
    {
        return media_reciever_registrarNode;
    }

    /**
     * @return the connection_managerNode
     */
    public static Node getConnection_managerNode()
    {
        return connection_managerNode;
    }
    
    /**
     * @return the content directory
     */
    public static ContentDirectory getContentDirectory()
    {
        return contentDirectory;
    }
    
    /**
     * @return the media reciever registry.
     */
    public static MediaReceiverRegistrar getMediaReceiverReg()
    {
        return mediaReceiverReg;
    }
    
}
