/******************************************************************
 *
 *	MediaServer for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2003-2004
 *
 *	File: IdSearchCap.java
 *
 *	Revision;
 *
 *	08/16/04
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.media.server.object.search;

import org.cybergarage.upnp.media.server.object.ContentNode;
import org.cybergarage.upnp.media.server.object.SearchCap;
import org.cybergarage.upnp.media.server.object.SearchCriteria;

public class IdSearchCap implements SearchCap
{
    public IdSearchCap()
    {}

    public String getPropertyName()
    {
        return SearchCriteria.ID;
    }

    public boolean compare(SearchCriteria searchCri, ContentNode conNode)
    {
        String searchCriID = searchCri.getValue();
        String conID = conNode.getID();
        if (searchCriID == null || conID == null)
            return false;
        if (searchCri.isEQ() == true)
            return (searchCriID.compareTo(conID) == 0) ? true : false;
        return false;

    }
}
