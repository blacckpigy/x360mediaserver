/**
 * one line to give the program's name and an idea of what it does.
 Copyright (C) 2006  Thomas Walker
 
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package x360mediaserver.upnpmediaserver.upnp.cybergarage;

import org.cybergarage.upnp.Action;
import org.cybergarage.xml.Node;

public class UPNPAction extends Action{
	
	// Class to handle parsing of incoming action so we can get their parameters etc

	public UPNPAction(Node serviceNode, Node actionNode)
	{
		super(serviceNode, actionNode);
	}

	public UPNPAction(Action action)
	{
		super(action);
	}
}
