/******************************************************************
 *
 *	MediaServer for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2003-2004
 *
 *	File : DefaultPlugIn.java
 *
 *	Revision:
 *
 *	02/12/04
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.media.server.object.format;

import java.io.File;

import org.cybergarage.upnp.media.server.object.Format;
import org.cybergarage.upnp.media.server.object.FormatObject;
import org.cybergarage.xml.AttributeList;

public class DefaultFormat implements Format, FormatObject
{
    // //////////////////////////////////////////////
    // Constroctor
    // //////////////////////////////////////////////

    public DefaultFormat()
    {}

    // //////////////////////////////////////////////
    // Abstract Methods
    // //////////////////////////////////////////////

    public boolean equals(File file)
    {
        return true;
    }

    public FormatObject createObject(File file)
    {
        return new DefaultFormat();
    }

    public String getMimeType()
    {
        return "*/*";
    }

    public String getMediaClass()
    {
        return "object.item";
    }

    public AttributeList getAttributeList()
    {
        return new AttributeList();
    }

    public String getTitle()
    {
        return "";
    }

    public String getCreator()
    {
        return "";
    }
}
