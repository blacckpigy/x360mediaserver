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

import java.io.OutputStream;


import org.cybergarage.upnp.media.server.object.ContentNode;
import org.cybergarage.xml.Attribute;
import org.cybergarage.xml.AttributeList;

import x360mediaserver.upnpmediaserver.upnp.cybergarage.SongNode;
import x360mediaserver.utils.StringUtils;


public class Video extends Media{
	
	static String HTTP_GET = "http-get";
	
	private FileItem fileItem;
	private Album album;
	private Artist artist;
	
	
	private Tag tag;
	
	public Video(FileItem f){
		this.fileItem=f;
		this.tag=f.getTag();		
	}
	
	public Video(){
		tag=new Tag();
	}
	
	public String getArtistString(){
		if(artist==null){
			return tag.getArtist();
		}
		else return artist.getName();
	}
	
	public String getAlbumString(){
		if(album==null){
			return tag.getAlbum();
		}
		else return album.getName();
	}
	
	public void setArtist(Artist a){
		artist=a;
	}
	
	public void setAlbum(Album a){
		album=a;
	}
	
	
	
	public int getTrackNumber(){
			return tag.getTracknumber();
	}
	

	public String getTitle() {
		//if(title==null){
		return tag.getTitle();
		//}
		//return title;
	}
	
	public void setTitle(String title) {
		tag.setTitle(title);
		//this.title = title;
	}
	
	public String getLocation(){
		return fileItem.file.toString();
	}
	
	
	public ContentNode getContentNode(String ServerAddress){
		SongNode result=new SongNode();
		
		//result.setArtist(artist.getName().replace("&","&amp;"));	
		//result.setArtist(StringUtils.getHtmlString(artist.getName()));
		//result.setTitle(this.getTitle().replace("&","&amp;"));
		result.setTitle(StringUtils.getHtmlString(this.getTitle()));
		result.setRestricted(1);
		result.setParentID((int)8);
		result.setID((int)id);
		result.setUPnPClass("object.item.videoItem");		
		//result.setAlbum(album.getName().replace("&","&amp;"));
		//result.setAlbum(StringUtils.getHtmlString(album.getName()));
		//result.setGenre("Unknown");
				
		
		{
			String protocol=HTTP_GET + ":*:" + "video/x-ms-wmv" + ":*";
			AttributeList atrrlist=new AttributeList();
			// these are probably not all needed or correct but it seems to work, should be properly worked out at some point
			atrrlist.add(new Attribute("size",""+fileItem.file.length()));		
			atrrlist.add(new Attribute("duration",tag.getTimeString()));
			atrrlist.add(new Attribute("bitrate","6936548"));
			atrrlist.add(new Attribute("sampleFrequency","44100"));
			atrrlist.add(new Attribute("bitsPerSample","16"));
			atrrlist.add(new Attribute("nrAudioChannels","2"));
			result.setResource("http://"+ServerAddress+"/service/ContentDirectory/Music/"+id+".wmv",protocol,atrrlist);
		}
		
		return result;
	}
	
	public void playPCM(OutputStream os){
		fileItem.writePCM(os);
	}
	
	public void playMP3(OutputStream os){
		fileItem.writeMP3(os);
	}
	
	public void playWMA(OutputStream os){
		fileItem.writeWMA(os);
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public FileItem getFileItem() {
		return fileItem;
	}

	public void setFileItem(FileItem fileItem) {
		this.fileItem = fileItem;
	}
	
}
