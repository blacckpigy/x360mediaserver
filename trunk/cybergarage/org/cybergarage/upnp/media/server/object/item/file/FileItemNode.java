/******************************************************************
 *
 *	MediaServer for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2003
 *
 *	File : FileItemNode.java
 *
 *	Revision:
 *
 *	02/12/04
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.media.server.object.item.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.cybergarage.upnp.media.server.ContentDirectory;
import org.cybergarage.upnp.media.server.object.Format;
import org.cybergarage.upnp.media.server.object.item.ItemNode;
import org.cybergarage.util.FileUtil;
import org.cybergarage.util.MyLogger;

public class FileItemNode extends ItemNode
{
    private static MyLogger log = new MyLogger(FileItemNode.class);

    // //////////////////////////////////////////////
    // Constroctor
    // //////////////////////////////////////////////

    public FileItemNode()
    {
        setFile(null);
    }

    // //////////////////////////////////////////////
    // File/TimeStamp
    // //////////////////////////////////////////////

    private File itemFile;

    public void setFile(File file)
    {
        itemFile = file;
    }

    public File getFile()
    {
        return itemFile;
    }

    public long getFileTimeStamp()
    {
        long itemFileTimeStamp = 0;
        if (itemFile != null)
        {
            try
            {
                itemFileTimeStamp = itemFile.lastModified();
            }
            catch (Exception e)
            {
                log.warn(e);
            }
        }
        return itemFileTimeStamp;
    }

    public boolean equals(File file)
    {
        if (itemFile == null)
            return false;
        return itemFile.equals(file);
    }

    // //////////////////////////////////////////////
    // Abstract methods
    // //////////////////////////////////////////////

    public byte[] getContent()
    {
        byte fileByte[] = new byte[0];
        try
        {
            fileByte = FileUtil.load(itemFile);
        }
        catch (Exception e)
        {
            log.warn(e);
        }
        return fileByte;
    }

    public long getContentLength()
    {
        return itemFile.length();
    }

    public InputStream getContentInputStream()
    {
        try
        {
            return new FileInputStream(itemFile);
        }
        catch (Exception e)
        {
            log.warn(e);
        }
        return null;
    }

    public String getMimeType()
    {
        ContentDirectory cdir = getContentDirectory();
        File itemFile = getFile();
        Format itemFormat = cdir.getFormat(itemFile);
        if (itemFormat == null)
        {
            return "*/*";
        }
        return itemFormat.getMimeType();
    }
}
