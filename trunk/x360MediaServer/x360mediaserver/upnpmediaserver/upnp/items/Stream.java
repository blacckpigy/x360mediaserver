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

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.URL;


import org.cybergarage.upnp.media.server.ConnectionManager;
import org.cybergarage.upnp.media.server.object.ContentNode;
import org.cybergarage.xml.Attribute;
import org.cybergarage.xml.AttributeList;

import x360mediaserver.upnpmediaserver.upnp.cybergarage.SongNode;
import x360mediaserver.upnpmediaserver.upnp.formats.StreamFormat;

public class Stream extends Media {
	
	String url;
	String name="Stream";
	//int type=0; // type 0=mp3 1=wma
	
	StreamFormat format;
	
	public Stream(String urlString,StreamFormat format){
		try{
			this.url=urlString;
			this.format=format;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	public Stream(String url,StreamFormat format){
//		this.url=url;
//		this.format=format;
//	}
	
//	
//	public Stream(String urlString){
//		try{
//			this.url=new URL(urlString);
//			
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//	}
//	
//	public Stream(URL url){
//		this.url=url;
//	}
	
	@Override
	public ContentNode getContentNode(String ServerAddress) {
		SongNode result=new SongNode();
		
		
		result.setArtist("Radio");
		result.setTitle(name);
		result.setRestricted(1);
		result.setParentID((int)4);
		result.setID((int)id);
		result.setUPnPClass("object.item.audioItem.musicTrack");
		
		result.setAlbum("Streams");
		result.setGenre("Unknown");
		
		String HTTP_GET=ConnectionManager.HTTP_GET;
		
		if(format.supportsPCM() && format.formatHandler.isUsePCM()){
			String protocol=HTTP_GET + ":*:" + "audio/L16" + ":*";
			AttributeList atrrlist=new AttributeList();
			// these are probably not all needed or correct but it seems to work, should be properly worked out at some point					
			atrrlist.add(new Attribute("bitrate","176400"));
			atrrlist.add(new Attribute("sampleFrequency","44100"));
			atrrlist.add(new Attribute("bitsPerSample","16"));
			atrrlist.add(new Attribute("nrAudioChannels","2"));
			result.setResource("http://"+ServerAddress+"/service/ContentDirectory/Music/"+id+".wma?format=PCM",protocol,atrrlist);
		}		
		else if(format.supportsMP3())
		{
			String protocol=HTTP_GET + ":*:" + "audio/mpeg" + ":*";
			AttributeList atrrlist=new AttributeList();
			// these are probably not all needed or correct but it seems to work, should be properly worked out at some point
			//atrrlist.add(new Attribute("size",""+fileItem.file.length()));			
			atrrlist.add(new Attribute("bitrate","16000"));
			atrrlist.add(new Attribute("sampleFrequency","44100"));
			atrrlist.add(new Attribute("bitsPerSample","4"));
			atrrlist.add(new Attribute("nrAudioChannels","2"));
			result.setResource("http://"+ServerAddress+"/service/ContentDirectory/Music/"+id+".mp3",protocol,atrrlist);
		}
		else if(format.supportsWMA()){
			String protocol=HTTP_GET + ":*:" + "audio/x-ms-wma" + ":*";
			AttributeList atrrlist=new AttributeList();
			// these are probably not all needed or correct but it seems to work, should be properly worked out at some point
			//atrrlist.add(new Attribute("size",""+fileItem.file.length()));					
			atrrlist.add(new Attribute("bitrate","36092"));
			atrrlist.add(new Attribute("sampleFrequency","44100"));
			atrrlist.add(new Attribute("bitsPerSample","16"));
			atrrlist.add(new Attribute("nrAudioChannels","2"));
			result.setResource("http://"+ServerAddress+"/service/ContentDirectory/Music/"+id+".wma",protocol,atrrlist);
		}
		
		
//		String protocol=ConnectionManager.HTTP_GET + ":*:" + "audio/mpeg" + ":*";
//		if(type==1) protocol=ConnectionManager.HTTP_GET + ":*:" + "audio/x-ms-wma" + ":*";
//		
//		
//		AttributeList atrrlist=new AttributeList();
//		// these are probably not all needed or correct but it seems to work, should be properly worked out at some point
//		atrrlist.add(new Attribute("size",""+100000));		
//		atrrlist.add(new Attribute("bitrate","160"));
//		atrrlist.add(new Attribute("sampleFrequency","44100"));
//		atrrlist.add(new Attribute("bitsPerSample","4"));
//		atrrlist.add(new Attribute("nrAudioChannels","2"));
//		
//		if(type==1) result.setResource("http://"+ServerAddress+"/service/ContentDirectory/Music/"+id+".wma",protocol,atrrlist);
//		else result.setResource("http://"+ServerAddress+"/service/ContentDirectory/Music/"+id+".mp3",protocol,atrrlist);
		
		
		
		return result;
	}
	
	public void playPCM(OutputStream os){
		format.writePCMtoStream(url, os);
		//streamPlayer.writePCM(os);
	}
	
	public void playMP3(OutputStream os){
		format.writeMP3toStream(url, os);
		//playStream(os);
	}
	
//	private void playStream(OutputStream os){
//		try{
//			System.out.println("Opening stream:"+url.toString());
//			BufferedInputStream input=new BufferedInputStream(url.openStream(),4*1024*1024);
//			byte[] data=new byte[102400];
//			int read;
//			while((read=input.read(data))!=-1){
//				os.write(data,0,read);
//				//System.out.println(read);
//			}
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		//streamPlayer.writeMP3(os,url);
//	}
	
	public void playWMA(OutputStream os){
		format.writeMP3toStream(url, os);
		//playStream(os);
		//streamPlayer.writeWMA(os);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
