/******************************************************************
 *
 *	MediaServer for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2003
 *
 *	File : ItemNode.java
 *
 *	Revision:
 *
 *	10/22/03
 *		- first revision.
 *	01/28/04
 *		- Added file and timestamp parameters.
 *
 ******************************************************************/

package org.cybergarage.upnp.media.server.object.item;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.cybergarage.upnp.media.server.DC;
import org.cybergarage.upnp.media.server.UPnP;
import org.cybergarage.upnp.media.server.object.ContentNode;
import org.cybergarage.upnp.media.server.object.DIDLLite;
import org.cybergarage.util.MyLogger;
import org.cybergarage.xml.Attribute;
import org.cybergarage.xml.AttributeList;
import org.cybergarage.xml.Node;

public abstract class ItemNode extends ContentNode
{
    // //////////////////////////////////////////////
    // Constants
    // //////////////////////////////////////////////

    public final static String NAME          = "item";

    public final static String RES           = "res";

    public final static String PROTOCOL_INFO = "protocolInfo";

    public final static String SIZE          = "size";
    public final static String IMPORT_URI    = "importUri";
    public final static String COLOR_DEPTH   = "colorDepth";
    public final static String RESOLUTION    = "resolution";

    private static MyLogger    log           = new MyLogger(ItemNode.class);

    // //////////////////////////////////////////////
    // Constroctor
    // //////////////////////////////////////////////

    public ItemNode()
    {
        setID( -1);
        setName(NAME);
        setStorageMedium(UNKNOWN);
        setWriteStatus(UNKNOWN);
    }

    // //////////////////////////////////////////////
    // isItemNode
    // //////////////////////////////////////////// //

    public final static boolean isItemNode(Node node)
    {
        String name = node.getName();
        if (name == null)
            return false;
        return name.equals(NAME);
    }

    // //////////////////////////////////////////////
    // Child node
    // //////////////////////////////////////////////

    public void addContentNode(ContentNode node)
    {
        addNode(node);
        node.setParentID(getID());
        node.setContentDirectory(getContentDirectory());
    }

    public boolean removeContentNode(ContentNode node)
    {
        return removeNode(node);
    }

    // //////////////////////////////////////////////
    // dc:creator
    // //////////////////////////////////////////////

    private final static String DATE_FORMAT = "yyyy-MM-dd";

    public void setDate(String value)
    {
        setProperty(DC.DATE, value);
    }

    public String getDate()
    {
        return getPropertyValue(DC.DATE);
    }

    private static DateFormat df = new SimpleDateFormat(DATE_FORMAT);

    public void setDate(long dateTime)
    {
        try
        {
            // DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
            // DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
            String dateStr = df.format(new Date(dateTime));
            setDate(dateStr);
        }
        catch (Exception e)
        {
            log.warn(e);
        }
    }

    public long getDateTime()
    {
        String dateStr = getDate();
        if (dateStr == null || dateStr.length() < 10)
            return 0;
        try
        {
            Date date = df.parse(dateStr);
            return date.getTime();
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    // //////////////////////////////////////////////
    // dc:creator
    // //////////////////////////////////////////////

    public void setCreator(String name)
    {
        setProperty(DC.CREATOR, name);
    }

    public String getCreator()
    {
        return getPropertyValue(DC.CREATOR);
    }

    // //////////////////////////////////////////////
    // upnp:storageMedium
    // //////////////////////////////////////////////

    public void setStorageMedium(String value)
    {
        setProperty(UPnP.STORAGE_MEDIUM, value);
    }

    public String getStorageMedium()
    {
        return getPropertyValue(UPnP.STORAGE_MEDIUM);
    }

    // //////////////////////////////////////////////
    // upnp:storageUsed
    // //////////////////////////////////////////////

    public void setStorageUsed(long value)
    {
        setProperty(UPnP.STORAGE_USED, value);
    }

    public long getStorageUsed()
    {
        return getPropertyLongValue(UPnP.STORAGE_USED);
    }

    // //////////////////////////////////////////////
    // Res
    // //////////////////////////////////////////////

    public void setResource(String url, String protocolInfo, AttributeList attrList)
    {
        setProperty(DIDLLite.RES, url);

        setPropertyAttribure(DIDLLite.RES, DIDLLite.RES_PROTOCOLINFO, protocolInfo);
        int attrCnt = attrList.size();
        for (int n = 0; n < attrCnt; n++ )
        {
            Attribute attr = attrList.getAttribute(n);
            String name = attr.getName();
            String value = attr.getValue();
            setPropertyAttribure(DIDLLite.RES, name, value);
        }
    }

    public void setResource(String url, String protocolInfo)
    {
        setResource(url, protocolInfo, new AttributeList());
    }

    public String getResource()
    {
        return getPropertyValue(DIDLLite.RES);
    }

    public String getProtocolInfo()
    {
        return getPropertyAttribureValue(DIDLLite.RES, DIDLLite.RES_PROTOCOLINFO);
    }

    // //////////////////////////////////////////////
    // Abstract methods
    // //////////////////////////////////////////////

    // public abstract byte[] getContent();
    public abstract long getContentLength();

    public abstract InputStream getContentInputStream();

    public abstract String getMimeType();

}
