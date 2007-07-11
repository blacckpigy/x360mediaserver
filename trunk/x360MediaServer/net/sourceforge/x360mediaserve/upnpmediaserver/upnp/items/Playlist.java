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

package net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.cybergarage.SongNode;

import org.cybergarage.upnp.media.server.object.ContentNode;
import org.cybergarage.upnp.media.server.object.ContentNodeList;

public class Playlist extends Container {


	String name="";
	//ArrayList<Media> songs=new ArrayList<Media>();

	List<Media> songsList=java.util.Collections.synchronizedList((new ArrayList<Media>()));


	public void addSong(Media song){
		songsList.add(song);
	}

	public void setSongs(List<Media> songs){
		//this.songs=songs;
		songsList.addAll(songs);		
	}

	public List<Media> getSongs(){
		return this.songsList;
	}

	@Override
	public ContentNodeList getContentList(String serverAddress){
		System.out.println("Getting content");

		ContentNodeList  result=new ContentNodeList();

		synchronized(songsList){			
			for(Media song:songsList){
				result.add(song.getContentNode(serverAddress));
			}						
		}
		return result;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}



	public ContentNode getSummaryContentNode(){
		SongNode result=new SongNode();


		result.setTitle(this.getName().replace("&","&amp;"));
		result.setAttribute("childCount",""+songsList.size());
		result.setRestricted(1);
		result.setPlaylist(name.replace("&","&amp;"));
		result.setParentID("F");
		result.setID((int)id);
		result.setUPnPClass("object.container.playlistContainer");							
		return result;
	}


}
