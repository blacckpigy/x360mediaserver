/******************************************************************
 *
 *	MediaServer for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2003-2004
 *
 *	File: FormatList
 *
 *	Revision;
 *
 *	01/12/04
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.media.server.object;

import java.io.File;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

public class FormatList
{

    private static Hashtable known_formats = new Hashtable();

    public FormatList()
    {}

    public void add(Format f)
    {
        known_formats.put(f.getMimeType(), f);
    }

    public Format getFormat(String type)
    {
        return (Format) known_formats.get(type);
    }

    public Format getFormat(File file)
    {
        // assert(file != null);
        Enumeration allElements = known_formats.elements();
        while (allElements.hasMoreElements())
        {
            Format f = (Format) allElements.nextElement();
            if (f.equals(file))
            {
                return f;
            }
        }
        return null;
    }

    public Collection getAllFormats()
    {
        return known_formats.values();
    }
}
