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

import java.util.Vector;

public class ServiceList extends Vector<MyService>
{
	////////////////////////////////////////////////
	//	Constants
	////////////////////////////////////////////////
	
    private static final long serialVersionUID = 1L;
    public final static String ELEM_NAME = "serviceList";

	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////
	
	public ServiceList() 
	{
	}
	
	////////////////////////////////////////////////
	//	Methods
	////////////////////////////////////////////////
	
	public MyService getService(int n)
	{
		try
        {
            return get(n);
        }
        catch (Exception e)
        { }
        return null;
	}
}

