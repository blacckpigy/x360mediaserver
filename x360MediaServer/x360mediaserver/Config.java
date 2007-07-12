package x360mediaserver;

import java.io.File;
import java.util.ArrayList;

import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.xml.Node;

import x360mediaserver.upnpmediaserver.mediareceiverregistrar.MediaReceiverRegistrar;
import x360mediaserver.upnpmediaserver.upnp.contentdirectory.ContentDirectory;


/**
 * This class provides for the global configuration and config accessor methods for the application.
 * @author robinson
 */
public class Config extends ConfigXML
{
    private static final long            serialVersionUID     = 1L;

    private static final int             DEFAULT_PORT         = 7000;

    private static final String          FRIENDLY_NAME        = "friendlyName";
    private static final String          ADDRESS              = "ExternalAddress";
    private static final String          PORT                 = "Port";
    private static final String          PCMOUTPUT            = "PCMOutput";
    private static final String          MUSIC_DIR            = "MusicDir";
    private static final String          ITUNES_FILE          = "iTunesFile";
    private static final String          STREAMS              = "Streams";
    private static final String          STRING_UDN           = "UDN";

    public static String                 contentDirectoryPath = "/service/ContentDirectory";
    public static ContentDirectory       contentDirectory     = new ContentDirectory();

    public static String                 mediaRecRegPath      = "/service/MediaReceiverRegistrar";
    public static MediaReceiverRegistrar mediaReceiverReg     = new MediaReceiverRegistrar();
    
    public static String                 configurePath        = "/configure";

    public Config()
    {}

    public static void loadConfig()
    {
        ConfigXML.loadConfig();
        // TODO: have an option set to use IPV4/6 etc.
        UPnP.setEnable(UPnP.USE_ONLY_IPV4_ADDR);
    }
    
    public static void resetConfig()
    {
        setUDN(UPnP.createUUID());
        setFriendlyName("x360 MediaServer");
        setPCMoption(false);
        resetExternalAddress();
        setPort(DEFAULT_PORT);
    }

    /**
     * This is necessary to properly setup the description.xml file with the proper data.
     */
    public static void verifyDescriptionXML()
    {
        getDescriptionNode().getNode("device").setNode(STRING_UDN, getUDN());
        getDescriptionNode().getNode("device").setNode(FRIENDLY_NAME,
                                                       getFriendlyName() + ":1: Windows Media Connect");
        // add the music streams to the music database. 
        for (ConfigStream streamNode : getStreams())
            contentDirectory.addStream(streamNode);   
        
        // TODO: Add the artists to the music database...
        setMusicDir(getMusicDir());
        setiTunesFile(getiTunesFile());
        
        saveConfig(descriptionFile, getDescriptionNode());
    }
    

    /**
     * This sets the name of the server.
     * 
     * @param friendlyName
     */
    public static void setFriendlyName(String friendlyName)
    {
        setupNode(friendlyName, FRIENDLY_NAME);
        getDescriptionNode().getNode("device").setNode(FRIENDLY_NAME,
                                                       friendlyName + ":1: Windows Media Connect");
    }

    /**
     * Returns the value of the friendly name for the server.
     * 
     * @return
     */
    public static String getFriendlyName()
    {
        return configNode.getNode(FRIENDLY_NAME).getValue();
    }

    /**
     * This will set the UDN for this server.
     * 
     * @param UDN
     */
    public static void setUDN(String UDN)
    {
        setupNode(UDN, STRING_UDN);
        getDescriptionNode().getNode("device").setNode(STRING_UDN, UDN);
    }

    /**
     * Returns the value of the UDN name for the server.
     * 
     * @return
     */
    public static String getUDN()
    {
        return configNode.getNode(STRING_UDN).getValue();
    }

    /**
     * Resets the external address for the server.
     */
    public static void resetExternalAddress()
    {
        int nHostAddrs = HostInterface.getNHostAddresses();
        for (int n = 0; n < nHostAddrs; n++ )
        {
            String bindAddr = HostInterface.getHostAddress(n);
            if (bindAddr == null || bindAddr.length() <= 0)
                continue;
            setAddress(bindAddr);
        }
    }

    /**
     * Sets the external NIC address for the server (in addition to localhost/127.0.0.1...)..
     * 
     * @param address
     */
    public static void setAddress(String address)
    {
        setupNode(address, ADDRESS);
        HostInterface.setInterface(address);
        getDescriptionNode().getNode("device").setNode(ADDRESS, address);
    }

    /**
     * Returns the value of the external NIC address for the server.
     * 
     * @return
     */
    public static String getAddress()
    {
        return configNode.getNode(ADDRESS).getValue();
    }

    /**
     * Sets the NIC TCP port for the server.
     * The UDP port is statically set to be 1900. 
     * 
     * @param address
     */
    public static void setPort(int port)
    {
        setupNode(Integer.toString(port), PORT);
        getDescriptionNode().getNode("device").setNode(PORT, Integer.toString(port));
    }

    /**
     * Returns the value of the NIC port for the server.
     * 
     * @return
     */
    public static Integer getPort()
    {
        int value = DEFAULT_PORT;
        try
        {
            value = Integer.parseInt(configNode.getNode(PORT).getValue());
        }
        catch (NumberFormatException e)
        {
        }
        return value;
    }

    /**
     * If 1 (true) then ALL sound output from this server will be output as PCM.
     * This is because the Xbox360 MP3 player has been known to crash games sometimes.
     * 
     * @param option
     */
    public static void setPCMoption(boolean option)
    {
        setupNode(option ? "1" : "0", PCMOUTPUT);
        contentDirectory.setPCMOption(option);
    }

    /**
     * Returns the value of the PCM value for the server.
     * 
     * @return
     */
    public static Integer getPCMoption()
    {
        int value = 0;
        try
        {
            value = Integer.parseInt(configNode.getNode(PCMOUTPUT).getValue());
        }
        catch (NumberFormatException e)
        {
        }
        return value;
    }

    /**
     * This specifies what music directory will be served. 
     * TODO: need to allow multiple directories to be served?
     * 
     * @param musicDir
     */
    public static void setMusicDir(String musicDir)
    {
        File file = new File(musicDir);
        if (file.exists() && file.isDirectory())
        {
            setupNode(musicDir, MUSIC_DIR);
            contentDirectory.addMusicDir(file);
        }
        else
        {
            System.out.print("Music Dir file path wrong:" + file.toString());
        }
    }

    /**
     * Returns the music directory set for the server.
     * 
     * @return
     */
    public static String getMusicDir()
    {
        Node node = configNode.getNode(MUSIC_DIR);
        return node != null ? node.getValue() : "";
    }

    /**
     * this will set the iTunes file to be served.
     * 
     * @param iTunesFile
     */
    public static void setiTunesFile(String iTunesFile)
    {
        File file = new File(iTunesFile);
        if (file.exists() && file.isFile())
        {
            setupNode(iTunesFile, ITUNES_FILE);
            contentDirectory.addiTunesDB(file);
        }
        else
        {
            System.out.print("iTunes XML file path wrong:" + file.toString());
        }
    }

    /**
     * Returns the music iTunes file for the server.
     * 
     * @return
     */
    public static String getiTunesFile()
    {
        Node node = configNode.getNode(ITUNES_FILE);
        return node != null ? node.getValue() : "";
    }

    /**
     * This adds music Streams to the server.
     * 
     * @param name
     * @param url
     * @param type
     */
    public static void addStream(String name, String url, int type)
    {
        Node streamNodes = configNode.getNode(STREAMS);
        if (streamNodes == null)
        {
            streamNodes = new Node(STREAMS);
            configNode.addNode(streamNodes);
        }
        Node singleStreamNode = new Node("Stream");

        Node nameNode = new Node("Name");
        nameNode.setValue(name);
        singleStreamNode.addNode(nameNode);

        Node urlNode = new Node("URL");
        urlNode.setValue(url.toString());
        singleStreamNode.addNode(urlNode);

        Node typeNode = new Node("Type");
        typeNode.setValue(type);
        singleStreamNode.addNode(typeNode);

        streamNodes.addNode(singleStreamNode);

        contentDirectory.addStream(name, url, type);
        saveConfig();
    }

    /**
     * This returns the stream nodes as an arayList.
     * @return ArrayList<ConfigStream>
     */
    public static ArrayList<ConfigStream> getStreams()
    {
        ArrayList<ConfigStream> streams = new ArrayList<ConfigStream>();

        for (int i = 0; i < configNode.getNNodes(); i++ )
        {
            Node node = configNode.getNode(i);
            if (node.getName().equals(STREAMS))
                for (int j = 0; j < node.getNNodes(); j++ )
                {
                    ConfigStream streamNode = processStreamNode(node.getNode(j));
                    if (streamNode != null)
                        streams.add(streamNode);
                }
        }
        return streams;
    }

    /**
     * Process (parses) a node from a "streams" node and returns it
     * such as:
     * <Name>di.fm - House</Name> 
     * <URL>http://scfire-nyk0l-2.stream.aol.com:80/stream/1007</URL> 
     * <Type>0</Type> 
     * 
     * @param streamNode
     */
    private static ConfigStream processStreamNode(Node streamNode)
    {
        try
        {
            Node tmpnode = null;
            ConfigStream streamData = new ConfigStream();

            if ((tmpnode = streamNode.getNode("URL")) != null)
                streamData.url = tmpnode.getValue();

            if ((tmpnode = streamNode.getNode("Name")) != null)
                streamData.name = tmpnode.getValue();

            if ((tmpnode = streamNode.getNode("Type")) != null)
                streamData.type = Integer.parseInt(tmpnode.getValue());

            if (streamData.url != null && streamData.name != null && streamData.type != -1)
                return streamData;
        }
        catch (NumberFormatException e)
        {
            System.err.println("Error parsing XML stream data!!");
            e.printStackTrace();
        }
        return null;
    }
    
    public static void out(Object string)
    {
        System.out.println(string.toString());
    }
    
    public static void toaster(Object string)
    {
        System.err.println(string.toString());
    }
}
