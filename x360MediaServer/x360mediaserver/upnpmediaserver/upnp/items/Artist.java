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

import java.util.ArrayList;


import org.cybergarage.upnp.media.server.object.ContentNode;
import org.cybergarage.upnp.media.server.object.ContentNodeList;

import x360mediaserver.upnpmediaserver.upnp.cybergarage.SongNode;
import x360mediaserver.utils.StringUtils;


public class Artist extends Container{
	String name="";
	
	ArrayList<Album> albums=new ArrayList<Album>();
	ArrayList<Song> songs=new ArrayList<Song>();
	
	
	public Artist(String name){
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
	
	
	
	public void addAlbum(Album album){
		albums.add(album);
	}
	
	public void addSong(Song song){
		songs.add(song);
	}
	
	// get content node -- big dirty hack for the Xbox360
	public ContentNode getSummaryContentNode(){
		SongNode result = new SongNode();		
				
		result.setTitle(StringUtils.getHtmlString(this.getName()));
		result.setAttribute("childCount",""+albums.size());
		result.setParentID((int)6);
		result.setID((int)id);
		result.setUPnPClass("object.container.person.musicArtist");
		
		return result;
	}
	
	public ContentNodeList getContentList(String serverAddress){
//		System.out.println("Getting content");
//		System.out.println("Name:"+name+" has "+albums.size()+" albums");
		ContentNodeList  result=new ContentNodeList();
		for(Song song:songs){
			result.add(song.getContentNode(serverAddress));
//			System.out.println("getting content");
		}
//		System.out.println("Name:"+name+" has "+albums.size()+" albums");
		return result;
	}
	
	public ContentNodeList getAlbumContentList(){
//		System.out.println("Getting content");
//		System.out.println("Name:"+name+" has "+albums.size()+" albums");
		ContentNodeList  result=new ContentNodeList();
		for(Album album:albums){
			result.add(album.getSummaryContentNode());
//			System.out.println("getting content");
		}
//		System.out.println("Name:"+name+" has "+albums.size()+" albums");
		return result;
	}
	
}
