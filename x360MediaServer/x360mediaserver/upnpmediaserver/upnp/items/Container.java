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

package x360mediaserver.upnpmediaserver.upnp.items;


import org.cybergarage.upnp.media.server.object.ContentNode;
import org.cybergarage.upnp.media.server.object.ContentNodeList;

import x360mediaserver.upnpmediaserver.upnp.cybergarage.SongNode;


public abstract class Container {
	
	int id;
	String type;
	
	public String getType(){
		return type;
	}
	
	public void setContainerID(int id){
		this.id=id;
	}
	
	public int getContainerID(){
		return id;
	}
	
	public ContentNode getSummaryContentNode(){
		return new SongNode();
	}
	
	abstract public ContentNodeList getContentList(String serverAddress);

}
