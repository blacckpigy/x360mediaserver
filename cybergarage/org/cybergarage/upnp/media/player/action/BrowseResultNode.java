/******************************************************************
 *
 *	MediaPlayer for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2005
 *
 *	File : BrowseAction.java
 *
 *	09/26/05
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.media.player.action;

import java.io.InputStream;

import org.cybergarage.upnp.media.server.object.item.ItemNode;

public class BrowseResultNode extends ItemNode
{
    // //////////////////////////////////////////////
    // Constroctor
    // //////////////////////////////////////////////

    public BrowseResultNode()
    {}

    // //////////////////////////////////////////////
    // Abstract methods
    // //////////////////////////////////////////////

    public long getContentLength()
    {
        return 0;
    }

    public InputStream getContentInputStream()
    {
        return null;
    }

    public String getMimeType()
    {
        return "*/*";
    }
}
