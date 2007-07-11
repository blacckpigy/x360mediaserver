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

import java.util.Comparator;
import java.util.TreeSet;

import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.cybergarage.SongNode;
import net.sourceforge.x360mediaserve.utils.StringUtils;

import org.cybergarage.upnp.media.server.object.ContentNode;
import org.cybergarage.upnp.media.server.object.ContentNodeList;


public class Album extends Container{
	private String name="";	

	private Artist artist;

	private TreeSet<Song> songs=new TreeSet<Song>(
			new Comparator<Song>(){
				public int compare(Song o1,
						Song o2){
					int cmp=o1.getTrackNumber()-o2.getTrackNumber();
					if(cmp!=0) return cmp;
					else{
						if(o1.getTitle()==null) return 1;
						else if(o2.getTitle()==null) return -1;
						else return o1.getTitle().compareToIgnoreCase(o2.getTitle());
					}
				}
			}
	);


	public Album(String name,Artist artist){
		this.artist=artist;
		this.name=name;					
	}

	public String getName(){
		return name;
	}


	public void addSong(Song song){
		songs.add(song);
	}

	public ContentNode getSummaryContentNode(){
		SongNode result=new SongNode();


		//result.setTitle(this.getName().replace("&","&amp;"));
		result.setTitle(StringUtils.getHtmlString(this.getName()));

		result.setAttribute("childCount",""+songs.size());
		result.setRestricted(1);
		//result.setArtist(artist.getName().replace("&","&amp;"));
		result.setArtist(StringUtils.getHtmlString(artist.getName()));
		result.setParentID((int)7);
		result.setID((int)id);
		result.setUPnPClass("object.container.album.musicAlbum");							
		return result;
	}

	public ContentNodeList getContentList(String serverAddress){
		System.out.println("Getting content");
		ContentNodeList  result=new ContentNodeList();
		for(Song song:songs){
			result.add(song.getContentNode(serverAddress));
		}
		return result;
	}
}
