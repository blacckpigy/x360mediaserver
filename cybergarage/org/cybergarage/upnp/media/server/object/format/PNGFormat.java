/******************************************************************
 *
 *	MediaServer for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2003-2004
 *
 *	File : PNGPlugIn.java
 *
 *	Revision:
 *
 *	01/25/04
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.media.server.object.format;

import java.io.File;

import javax.imageio.ImageReader;

import org.cybergarage.upnp.media.server.object.FormatObject;

public class PNGFormat extends ImageIOFormat
{
    // //////////////////////////////////////////////
    // Member
    // //////////////////////////////////////////////

    private ImageReader imgReader;

    // //////////////////////////////////////////////
    // Constroctor
    // //////////////////////////////////////////////

    public PNGFormat()
    {}

    public PNGFormat(File file)
    {
        super(file);
    }

    // //////////////////////////////////////////////
    // Abstract Methods
    // //////////////////////////////////////////////

    public boolean equals(File file)
    {
        String headerID = Header.getIDString(file, 1, 3);
        if (headerID.startsWith("PNG") == true)
            return true;
        return false;
    }

    public FormatObject createObject(File file)
    {
        return new PNGFormat(file);
    }

    public String getMimeType()
    {
        return "image/png";
    }

}
