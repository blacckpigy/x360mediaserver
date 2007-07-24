/******************************************************************
*
*	CyberLink for Java
*
*	Copyright (C) Satoshi Konno 2002-2003
*
*	File: Service.java
*
*	Revision;
*
*	11/28/02
*		- first revision.
*	04/12/02
*		- Holmes, Arran C <acholm@essex.ac.uk>
*		- Fixed SERVICE_ID constant instead of "serviceId".
*	06/17/03
*		- Added notifyAllStateVariables().
*	09/03/03
*		- Giordano Sassaroli <sassarol@cefriel.it>
*		- Problem : The device does not accepts request for services when control or subscription urls are absolute
*		- Error : device methods, when requests are received, search for services that have a controlUrl (or eventSubUrl) equal to the request URI
*		          but request URI must be relative, so they cannot equal absolute urls
*	09/03/03
*		- Steven Yen
*		- description: to retrieve service information based on information in URLBase and SCPDURL
*		- problem: not able to retrieve service information when URLBase is missing and SCPDURL is relative
*		- fix: modify to retrieve host information from Header's Location (required) field and update the
*		       BaseURL tag in the xml so subsequent information retrieval can be done (Steven Yen, 8.27.2003)
*		- note: 1. in the case that Header's Location field combine with SCPDURL is not able to retrieve proper 
*		          information, updating BaseURL would not hurt, since exception will be thrown with or without update.
*		        2. this problem was discovered when using PC running MS win XP with ICS enabled (gateway). 
*		          It seems that  root device xml file does not have BaseURL and SCPDURL are all relative.
*		        3. UPnP device architecture states that BaseURL is optional and SCPDURL may be relative as 
*		          specified by UPnP vendor, so MS does not seem to violate the rule.
*	10/22/03
*		- Added setActionListener().
*	01/04/04
*		- Changed about new QueryListener interface.
*	01/06/04
*		- Moved the following methods to StateVariable class.
*		  getQueryListener() 
*		  setQueryListener() 
*		  performQueryListener()
*		- Added new setQueryListener() to set a listner to all state variables.
*	07/02/04
*		- Added serviceSearchResponse().
*		- Deleted getLocationURL().
*		- Fixed announce() to set the root device URL to the LOCATION field.
*	07/31/04
*		- Changed notify() to remove the expired subscribers and not to remove the invalid response subscribers for NMPR.
*	10/29/04
*		- Fixed a bug when notify() removes the expired devices().
*	03/23/05
*		- Added loadSCPD() to load the description from memory.
*	03/30/05
*		- Added isSCPDURL().
*		- Removed setDescriptionURL() and getDescriptionURL()
*	03/31/05
*		- Added getSCPDData().
* 	04/25/05
*		- Thanks for Mikael Hakman <mhakman@dkab.net>
* 		- Changed getSCPDData() to add a XML declaration at first line.
*	06/21/05
*		- Changed notify() to continue when the subscriber is null.
*
******************************************************************/

package x360mediaserver.upnpmediaserver.responder;

import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.xml.ServiceData;
import org.cybergarage.xml.Node;

public class MyService extends Service
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////

	public MyService(Node node)
	{
		super(node);
	}

	////////////////////////////////////////////////
	//	SCPD node
	////////////////////////////////////////////////

	// robinson
	public boolean loadSCPD(Node scpdNode)
    {
        ServiceData data = getServiceData();
        data.setSCPDNode(scpdNode);
        
        return true;
    }

	////////////////////////////////////////////////
	//	UserData
	////////////////////////////////////////////////

	private ServiceData getServiceData()
	{
		Node node = getServiceNode();
		ServiceData userData = (ServiceData)node.getUserData();
		if (userData == null) {
			userData = new ServiceData();
			node.setUserData(userData);
			userData.setNode(node);
		}
		return userData;
	}
}
