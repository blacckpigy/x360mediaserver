/******************************************************************
 *
 *	CyberUPnP for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: ServiceList.java
 *
 *	Revision;
 *
 *	12/04/02
 *		- first revision.
 *	06/18/03
 *		- Added caching a ArrayIndexOfBound exception.
 *
 ******************************************************************/

package x360mediaserver.upnpmediaserver.responder;

import org.cybergarage.upnp.ServiceList;

public class MyServiceList extends ServiceList
{
    // //////////////////////////////////////////////
    // Methods
    // //////////////////////////////////////////////

    public MyService getService(int n)
    {
        Object obj = null;
        try
        {
            obj = get(n);
        }
        catch (Exception e)
        {
        }
        ;
        return (MyService) obj;
    }
}
