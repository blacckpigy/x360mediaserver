/******************************************************************
 *
 *	MediaServer for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2003-2004
 *
 *	File : ImageIOPlugIn.java
 *
 *	Revision:
 *
 *	01/25/04
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.media.server.object.format;

import java.io.File;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.cybergarage.upnp.media.server.object.Format;
import org.cybergarage.upnp.media.server.object.FormatObject;
import org.cybergarage.upnp.media.server.object.item.ItemNode;
import org.cybergarage.util.MyLogger;
import org.cybergarage.xml.Attribute;
import org.cybergarage.xml.AttributeList;

public abstract class ImageIOFormat extends Header implements Format, FormatObject
{
    // //////////////////////////////////////////////
    // Member
    // //////////////////////////////////////////////

    private File            imgFile;
    private ImageReader     imgReader;
    private static MyLogger log = new MyLogger(ImageIOFormat.class);

    // //////////////////////////////////////////////
    // Constroctor
    // //////////////////////////////////////////////

    public ImageIOFormat()
    {
        imgFile = null;
        imgReader = null;
    }

    public ImageIOFormat(File file)
    {
        imgFile = file;
        Iterator readers = ImageIO.getImageReadersBySuffix(Header.getSuffix(file));
        if (readers.hasNext() == false)
            return;
        imgReader = (ImageReader) readers.next();
        try
        {
            ImageInputStream stream = ImageIO.createImageInputStream(file);
            imgReader.setInput(stream);
        }
        catch (Exception e)
        {
            log.warn(e);
        }
    }

    // //////////////////////////////////////////////
    // Abstract Methods
    // //////////////////////////////////////////////

    public abstract boolean equals(File file);

    public abstract FormatObject createObject(File file);

    public abstract String getMimeType();

    public String getMediaClass()
    {
        return "object.item.imageItem.photo";
    }

    public AttributeList getAttributeList()
    {
        AttributeList attrList = new AttributeList();

        try
        {
            // Resolution (Width x Height)
            int imgWidth = imgReader.getWidth(0);
            int imgHeight = imgReader.getHeight(0);
            String resStr = Integer.toString(imgWidth) + "x" + Integer.toString(imgHeight);
            Attribute resAttr = new Attribute(ItemNode.RESOLUTION, resStr);
            attrList.add(resAttr);

            // Size
            long fsize = imgFile.length();
            Attribute sizeStr = new Attribute(ItemNode.SIZE, Long.toString(fsize));
            attrList.add(sizeStr);
        }
        catch (Exception e)
        {
            log.warn(e);
        }

        return attrList;
    }

    public String getTitle()
    {
        String fname = imgFile.getName();
        int idx = fname.lastIndexOf(".");
        if (idx < 0)
            return "";
        String title = fname.substring(0, idx);
        return title;
    }

    public String getCreator()
    {
        return "";
    }

    // //////////////////////////////////////////////
    // print
    // //////////////////////////////////////////////

    public void print()
    {}

    // //////////////////////////////////////////////
    // main
    // //////////////////////////////////////////////
    /*
     * public static void main(String args[]) { Debug.on(); try { ID3 id3 = new ID3(new
     * File("C:/eclipse/workspace/upnp-media-server/images/SampleBGM01.mp3")); id3.print(); } catch
     * (Exception e) { log.warn(e); } }
     */
}
