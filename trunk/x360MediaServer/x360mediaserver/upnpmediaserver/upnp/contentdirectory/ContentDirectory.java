/**
 * one line to give the program's name and an idea of what it does. Copyright (C) 2006 Thomas Walker
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 * have received a copy of the GNU General Public License along with this program; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package x360mediaserver.upnpmediaserver.upnp.contentdirectory;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.media.server.action.BrowseAction;
import org.cybergarage.upnp.media.server.action.SearchAction;
import org.cybergarage.upnp.media.server.object.ContentNode;
import org.cybergarage.upnp.media.server.object.ContentNodeList;
import org.cybergarage.upnp.media.server.object.DIDLLite;
import org.cybergarage.xml.Attribute;
import org.cybergarage.xml.AttributeList;
import org.cybergarage.xml.Node;

import x360mediaserver.Config;
import x360mediaserver.ConfigStream;
import x360mediaserver.upnpmediaserver.upnp.Service;
import x360mediaserver.upnpmediaserver.upnp.items.Media;
import x360mediaserver.upnpmediaserver.upnp.items.Song;

public class ContentDirectory extends Service
{

    File        basePath       = null;

    MusicDB     musicDB        = new MusicDB();

    private int systemUpdateID = 0;

    public ContentDirectory(Node node, String serviceString)
    {
        super();
        try
        {
            debug(System.currentTimeMillis() + " ContentDirectory init");
            loadSCPD(node);

            buildActionList();
            this.setServiceString(serviceString);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    boolean browseActionReceived(BrowseAction browseact)
    {
        debug("Browse action called");
        return false;
    }

    @Override
    protected boolean doAction(Action action, String ServerAddress)
    {
        // TODO Auto-generated method stub

        String actionName = action.getName();

        if (actionName.equals("Browse"))
        {
            BrowseAction browseAct = new BrowseAction(action);
            return browseActionReceived(browseAct);
        }

        else if (actionName.equals("Search"))
        {
            SearchAction searchAct = new SearchAction(action);
            searchActionReceived(searchAct, ServerAddress);
            return true;
// return searchActionReceived(searchAct);
        }

        // @id,@parentID,dc:title,dc:date,upnp:class,res@protocolInfo
        else if (actionName.equals("GetSearchCapabilities") == true)
        {
            Argument searchCapsArg = action.getArgument("SearchCaps");
// String searchCapsStr = getSearchCapabilities();
// searchCapsArg.setValue(searchCapsStr);
            return true;
        }

        // dc:title,dc:date,upnp:class
        if (actionName.equals("GetSortCapabilities") == true)
        {
            Argument sortCapsArg = action.getArgument("SortCaps");
            return true;
        }

        if (actionName.equals("GetSystemUpdateID") == true)
        {
// Argument idArg = action.getArgument(ID);
// idArg.setValue(getSystemUpdateID());
            return true;
        }

        return false;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    {
        Config.out("GETTING ContentDirectory");
        try
        {
            debug("CONTENT DIRECTORY HTTP GET");
            debug("Contextpath:" + req.getContextPath());
            debug("pathinfo:" + req.getPathInfo());
            debug("request uri:" + req.getRequestURI());
            debug("path translated:" + req.getPathTranslated());
            debug("Query string" + req.getQueryString());
            if (req.getPathInfo().contains("Music"))
            {
                System.out.println("Music Requested");
                playMusic(req, resp);
            }
            else if (req.getPathInfo().contains("Dump"))
            {
                // dump music
                PrintWriter writer = resp.getWriter();
                for (Integer key : musicDB.mediaMap.keySet())
                {
                    Media container = musicDB.mediaMap.get(key);
                    if (container instanceof Song)
                        writer.println(key + ":" + ((Song) container).getTitle());

                }
            }
            else
            {
                Config.out("GETTING ContentDirectory");
                PrintWriter writer = resp.getWriter();
                writer.write(UPnP.XML_DECLARATION + "\n");
                writer.write(getSCPDNode().toString());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public synchronized int getSystemUpdateID()
    {
        return systemUpdateID;
    }

    /*
     * (non-Javadoc) Cheap configure script until someone writes a better one
     * 
     * @see net.sourceforge.x360mediaserve.upnpmediaserver.upnp.Service#handleOtherPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected void handleOtherPost(HttpServletRequest req, HttpServletResponse resp)
    {
        Config.out("DOING OTHER POST ContentDirectory");
        if (req.getRemoteHost().contains("127.0.0.1"))
        {
            debug(req.getPathInfo() + " " + req.getRemoteHost());
            if (req.getPathInfo().endsWith("configure"))
            {
                debug("Doing configure");
                System.out.println("Doing configure");
                try
                {
                    BufferedReader reader = new BufferedReader(req.getReader());
                    String str;
                    while ((str = reader.readLine()) != null)
                    {
                        String[] confoptions = str.split("&");
                        for (String s : confoptions)
                        {
                            debug(s);
                            String[] option = s.split("=");
                            if (option.length > 1)
                            {
                                if (option[0].contains("musicdir"))
                                {
                                    File f = new File(option[1].replace("%3A", ":").replace("%5C",
                                                                                            "\\")
                                                               .replace("%2F", "/").replace("+",
                                                                                            " "));
                                    if (f.exists() && f.isDirectory())
                                    {
                                        debug("Adding music dir" + f.toString());
                                        musicDB.addFile(f);
                                    }
                                }
                                if (option[0].contains("itunesfile"))
                                {
                                    File f = new File(option[1].replace("%3A", ":").replace("%5C",
                                                                                            "\\")
                                                               .replace("%2F", "/").replace("+",
                                                                                            " "));
                                    System.out.println(f.toString());
                                    if (f.exists() && f.isFile())
                                    {
                                        debug("Adding iTunes dir" + f.toString());
                                        musicDB.addItunesDataBase(f);
                                    }
                                }
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            else if (req.getPathInfo().endsWith("addStream"))
            {
                try
                {
                    BufferedReader reader = new BufferedReader(req.getReader());
                    String str;
                    String urlString = null;
                    String streamName = null;
                    while ((str = reader.readLine()) != null)
                    {
                        String[] confoptions = str.split("&");
                        for (String s : confoptions)
                        {
                            debug(s);
                            String[] option = s.split("=");
                            if (option.length > 1)
                            {
                                if (option[0].contains("name"))
                                {
                                    streamName = new String(option[1].replace("%3A", ":")
                                                                     .replace("%5C", "\\")
                                                                     .replace("%2F", "/")
                                                                     .replace("+", " "));

                                }
                                if (option[0].contains("URL"))
                                {
                                    urlString = new String(option[1].replace("%3A", ":")
                                                                    .replace("%5C", "\\")
                                                                    .replace("%2F", "/")
                                                                    .replace("+", " "));
                                }
                            }
                        }
                    }

                    if (urlString != null && streamName != null)
                    {
                        // String url=new URL(urlString);
                        musicDB.addStream(streamName, urlString, 0);
                        System.out.println("Added new Stream:" + streamName + " at " + urlString);

                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Adds this stream (URL) to the music database.
     * 
     * @param streamNode
     */
    public void addStream(ConfigStream streamNode)
    {
        musicDB.addStream(streamNode.name, streamNode.url, streamNode.type);
    }

    /**
     * Adds this stream (URL) to the music database.
     * 
     * @param name
     * @param url
     * @param type
     */
    public void addStream(String name, String url, int type)
    {
        musicDB.addStream(name, url, type);
    }

    /**
     * Adds this stream (iTunes) to the music database.
     * 
     * @param file
     */
    public void addiTunesDB(File file)
    {
        musicDB.addItunesDataBase(file);
    }

    /**
     * Adds this stream (files) to the music database.
     * 
     * @param file
     */
    public void addMusicDir(File file)
    {
        musicDB.addFile(file);
    }

    private void playMusic(HttpServletRequest req, HttpServletResponse resp)
    {
        String idString = req.getPathInfo().substring(req.getPathInfo().lastIndexOf("/") + 1);

        idString = idString.substring(0, idString.indexOf("."));
        debug("idString:" + idString);
        try
        {
            BufferedOutputStream os = new BufferedOutputStream(resp.getOutputStream(), 10000);
            if (req.getQueryString() != null &&
                req.getQueryString().toLowerCase().contains("format=pcm"))
            {
                resp.setContentType("audio/L16; rate=44100; channels=2");
                musicDB.playSongAsPCM(Integer.parseInt(idString), os);
            }
            else if (req.getPathInfo().substring(req.getPathInfo().lastIndexOf("/") + 1)
                        .toLowerCase().endsWith("mp3"))
            {
                System.out.println("Playing mp3");
                resp.setContentType("audio/mpeg");
                musicDB.playSongAsMP3(Integer.parseInt(idString), os);
            }
            else if (req.getPathInfo().substring(req.getPathInfo().lastIndexOf("/") + 1)
                        .toLowerCase().endsWith("wma"))
            {
                resp.setContentType("audio/x-ms-wma");
                musicDB.playSongAsWMA(Integer.parseInt(idString), os);
            }
            // video
            else if (req.getPathInfo().substring(req.getPathInfo().lastIndexOf("/") + 1)
                        .toLowerCase().endsWith("wmv"))
            {
                resp.setContentType("video/x-ms-wmv");
                resp.setHeader("Accept-Ranges", "bytes");
                resp.setHeader("Content-Length", "" + Integer.MAX_VALUE * 4);
                // resp.setContentLength(Integer.MAX_VALUE);
                musicDB.playVideoAsWMV(Integer.parseInt(idString), os);
            }
            os.flush();
            os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean searchActionReceived(SearchAction action, String ServerAddress)
    {
        String containerID = action.getContainerID();
        String searchCriteria = action.getSearchCriteria();

        debug("Search started");
        ContentNodeList sortedContentNodeList = new ContentNodeList();
        musicDB
               .getHackSearchList(sortedContentNodeList, containerID, searchCriteria, ServerAddress);
        int nChildNodes = sortedContentNodeList.size();

        int startingIndex = action.getStartingIndex();
        if (startingIndex <= 0)
            startingIndex = 0;
        int requestedCount = action.getRequestedCount();
        if (requestedCount == 0)
            requestedCount = nChildNodes;

        DIDLLite didlLite = new DIDLLite();
        int numberReturned = 0;
        for (int n = startingIndex; (n < nChildNodes && numberReturned < requestedCount); n++ )
        {
            ContentNode cnode = sortedContentNodeList.getContentNode(n);
            didlLite.addContentNode(cnode);
            numberReturned++ ;
        }

        AttributeList atrrlist1 = new AttributeList();
        atrrlist1.add(new Attribute("dt:dt", "ui4"));

        AttributeList atrrlist2 = new AttributeList();
        atrrlist2.add(new Attribute("xmlns:dt", "urn:schemas-microsoft-com:datatypes"));
        atrrlist2.add(new Attribute("dt:dt", "ui4"));

        String result = didlLite.toString();
        action.setResult(result);
        action.setNumberReturned(numberReturned);
        action.setTotalMaches(nChildNodes);
        action.setUpdateID(getSystemUpdateID());
        return true;
    }

    public synchronized void updateSystemUpdateID()
    {
        systemUpdateID++ ;
    }

    public void setPCMOption(boolean option)
    {
        musicDB.setPCMOption(option);
    }
}
