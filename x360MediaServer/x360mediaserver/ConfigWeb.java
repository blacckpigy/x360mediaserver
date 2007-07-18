package x360mediaserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConfigWeb
{
    private static ArrayList<ConfiguratorElement> elements                  = new ArrayList<ConfiguratorElement>();
    private static int                            nextConfiguratorElementID = 1;

    public static void start()
    {
        addElements(); // add stuff? to the xml? or to the web page i think...
    }

    /**
     * This figures out what to do for sending the web config webpage to whatever client connects.
     * @param req
     * @param resp
     */
    public static void doGet(HttpServletRequest req, HttpServletResponse resp)
    {
        try
        {
// System.out.println(req.getPathInfo());
            System.out.println("doing get in configurator: " + req.getPathInfo());
            PrintWriter writer = resp.getWriter();
            // resp.getWriter().write("<form action=\"http://127.0.0.1:7000/configure/addDir\"
            // method=\"POST\">Music Dir: <input type=\"text\" name=\"musicdir\"><br>iTunes File:
            // <input type=\"text\" name=\"itunesfile\"><br><br><input type=\"submit\"
            // value=\"Send\"></form><form action=\"http://127.0.0.1:7000/configure/addStream\"
            // method=\"POST\">Stream Name:<input type=\"text\" name=\"name\"><br>Stream URL:<input
            // type=\"text\" name=\"URL\"><br><br><input type=\"submit\" value=\"Send\"></form>");
            writer.write("<HTML>\n");
            for (ConfiguratorElement element : elements)
            {
                writer.write("<Form action=\"http://127.0.0.1:7000" + Config.getUrl("config") + "/" +
                             element.url + "\" method=\"POST\">\n");
                writer.write(element.formElement);
                writer.write("<input type=\"submit\" value=\"Send\">\n");
                writer.write("</Form>\n");
            }
            writer.write("</HTML>\n");
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * This does all the work for posting (saving) the web configuration webpage
     * @param req
     * @param resp
     */
    public static void doPost(HttpServletRequest req, HttpServletResponse resp)
    {
        System.out.println("doing post in configurator: " + req.getPathInfo());
        if (req.getRemoteHost().contains("127.0.0.1"))
        {
            debug(req.getPathInfo() + " " + req.getRemoteHost());

            HashMap<String, String> params = new HashMap<String, String>();

            try
            {
                BufferedReader reader = req.getReader();
                System.out.println("Opened reader");

                String lineData = "";

                {
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        lineData += line;
                    }
                }

                for (String data : lineData.split("&"))
                {

                    String[] option = data.split("=");
                    if (option.length > 1)
                    {
                        params.put(option[0], option[1].replace("%3A", ":").replace("%5C", "\\")
                                                       .replace("%2F", "/").replace("+", " ")
                                                       .replace("%27", "'").replace("%3F", "?")
                                                       .replace("%3D", "="));
                        System.out.println(option[0] +
                                           ": " +
                                           option[1].replace("%3A", ":").replace("%5C", "\\")
                                                    .replace("%2F", "/").replace("+", " "));
                    }
                }
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String file = req.getPathInfo().substring(req.getPathInfo().lastIndexOf("/") + 1);
            for (ConfiguratorElement element : elements)
            {
                if (file.equals(element.url))
                {
                    element.listener.process(params);
                }
            }
        }

    }

    /**
     * When you modify something in the webpage... it happens here.
     * 
     * @param formdata
     * @param listener
     */
    public static void addElement(String formdata, ConfiguratorListener listener)
    {
        ConfiguratorElement blah = new ConfiguratorElement(nextConfiguratorElementID++ + ".cgi",
                                                           formdata, listener);
        elements.add(blah);
    }

    /**
     * This sets up the webpage to configure this....
     */
    public static void addElements()
    {
        addElement("Music Dir: <input type=\"text\" name=\"musicdir\">", new ConfiguratorListener()
        {
            public void process(HashMap<String, String> formdata)
            {
                String musicDir = formdata.get("musicdir");
                if (musicDir != null)
                {
                    File file = new File(musicDir);
                    if (file.exists() && file.isDirectory())
                    {
                        // contentdir.addMusicDir(file);
                        Config.addMusicDir(musicDir);
                    }
                }
            }
        });

        addElement("iTunesFile Dir: <input type=\"text\" name=\"itunesfile\">",
                   new ConfiguratorListener()
                   {
                       public void process(HashMap<String, String> formdata)
                       {
                           String iTunesFile = formdata.get("itunesfile");
                           if (iTunesFile != null)
                           {
                               Config.setiTunesFile(iTunesFile);
                           }
                       }
                   });

        addElement("Friendly Name: <input type=\"text\" name=\"friendlyname\">",
                   new ConfiguratorListener()
                   {
                       public void process(HashMap<String, String> formdata)
                       {
                           String friendlyName = formdata.get("friendlyname");
                           if (friendlyName != null)
                           {
                               Config.setFriendlyName(friendlyName);
                           }
                       }
                   });

        addElement("Output Format:" + "<SELECT NAME=\"format\">" + "<OPTION VALUE=\"mp3\">MP3"
                  + "<OPTION VALUE=\"pcm\">PCM" + "</SELECT>", new ConfiguratorListener()
        {
            public void process(HashMap<String, String> formdata)
            {
                System.out.println("Doing pcm config");
                String formatString = formdata.get("format");

                if (formatString != null)
                {
                    debug("formatString !=null");
                    if (formatString.equals("mp3"))
                        Config.setPCMoption(false);
                    else if (formatString.equals("pcm"))
                        Config.setPCMoption(true);
                }

            }
        });

        addElement("Stream Name:<input type=\"text\" name=\"streamname\"><br>Stream URL:<input type=\"text\" name=\"URL\"><br>"
                           + "<SELECT NAME=\"format\">"
                           + "<OPTION VALUE=\"mp3\">MP3"
                           + "<OPTION VALUE=\"wma\">WMA"
                           + "<OPTION VALUE=\"generic\">Generic"
                           + "</SELECT>", new ConfiguratorListener()
                   {
                       public void process(HashMap<String, String> formdata)
                       {
                           String streamName = formdata.get("streamname");
                           String url = formdata.get("URL");
                           String formatString = formdata.get("format");
                           if (streamName != null && url != null && formatString != null)
                           {
                               try
                               {
                                   int format = -1;
                                   if (formatString.equals("mp3"))
                                       format = 0;
                                   else if (formatString.equals("wma"))
                                       format = 1;
                                   else if (formatString.equals("generic"))
                                       format = 2;

                                   if (format != -1)
                                   {
                                       Config.addStream(streamName, url, format);
                                   }
                               }
                               catch (Exception urlException)
                               {
                                   System.out.println(urlException.toString());
                               }

                           }

                       }
                   });
    }

    private static void debug(String str)
    {
        System.err.println("ConfigWeb: " + str);
    }
}

interface ConfiguratorListener
{
    void process(HashMap<String, String> formdata);
}

class ConfiguratorElement
{
    String               url;
    String               formElement;
    ConfiguratorListener listener;

    /**
     * @param url
     * @param element
     * @param listener
     */
    public ConfiguratorElement(String url, String element, ConfiguratorListener listener)
    {
        super();
        // TODO Auto-generated constructor stub
        this.url = url;
        formElement = element;
        this.listener = listener;
    }
}