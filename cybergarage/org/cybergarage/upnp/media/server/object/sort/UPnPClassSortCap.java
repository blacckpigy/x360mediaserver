/******************************************************************
 *
 *	MediaServer for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2003-2004
 *
 *	File: UPnPClassSortCap.java
 *
 *	Revision;
 *
 *	02/03/04
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.media.server.object.sort;

import org.cybergarage.upnp.media.server.UPnP;
import org.cybergarage.upnp.media.server.object.ContentNode;
import org.cybergarage.upnp.media.server.object.SortCap;

public class UPnPClassSortCap implements SortCap
{
    public UPnPClassSortCap()
    {}

    public String getType()
    {
        return UPnP.CLASS;
    }

    public int compare(ContentNode conNode1, ContentNode conNode2)
    {
        if (conNode1 == null || conNode2 == null)
            return 0;
        String upnpClass1 = conNode1.getUPnPClass();
        String upnpClass2 = conNode2.getUPnPClass();
        if (upnpClass1 == null || upnpClass2 == null)
            return 0;
        return upnpClass1.compareTo(upnpClass2);
    }
}
