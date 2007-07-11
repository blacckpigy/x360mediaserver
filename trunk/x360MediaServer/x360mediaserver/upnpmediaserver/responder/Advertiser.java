/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File: Advertiser.java
*
*	Revision;
*
*	12/24/03
*		- first revision.
*	06/18/04
*		- Changed to advertise every 25%-50% of the periodic notification cycle for NMPR;
*	
******************************************************************/

package x360mediaserver.upnpmediaserver.responder;

import org.cybergarage.util.ThreadCore;

public class Advertiser extends ThreadCore
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////

	public Advertiser(UPNPListener dev)
	{
		setDevice(dev);
	}
	
	////////////////////////////////////////////////
	//	Member
	////////////////////////////////////////////////

	private UPNPListener device;

	public void setDevice(UPNPListener dev)
	{
		device = dev;
	}
	
	public UPNPListener getDevice()
	{
		return device;
	}

	////////////////////////////////////////////////
	//	Thread
	////////////////////////////////////////////////
	
	public void run() 
	{
		UPNPListener dev = getDevice();
		long leaseTime = dev.getLeaseTime();
		long notifyInterval;
		while (isRunnable() == true) {
			notifyInterval = (leaseTime/4) + (long)((float)leaseTime * (Math.random() * 0.25f));
			notifyInterval *= 1000;
			try {
				Thread.sleep(notifyInterval);
			} catch (InterruptedException e) {}
			dev.announce();
		}
	}
}
