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

package net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats;

import java.io.File;
import java.io.OutputStream;
import java.net.URL;

import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.contentdirectory.FormatHandler;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.identifiers.Identifier;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.streamers.StreamStreamer;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.streamers.Streamer;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.Tag;



public class StreamFormat {
	
	private Identifier identifier;
	//private Tagger tagger;
	
	public FormatHandler formatHandler=null;
	private String iTunesKind=null;
	
	
	private StreamStreamer wmaStreamer=null;
	private StreamStreamer mp3Streamer=null;
	private StreamStreamer pcmStreamer=null;
	private StreamStreamer wavStreamer=null;
	private StreamStreamer wmvStreamer=null;
	
	private boolean isSong=false;
	private boolean isVideo=false;
	
	
	
	public boolean supportsMP3(){
		return mp3Streamer!=null;
	}
	
	
	public boolean supportsWMA(){
		return wmaStreamer!=null;
	}
	
	public boolean supportsPCM(){
		return pcmStreamer!=null;
	}
	
	public StreamFormat(FormatHandler handler,StreamStreamer mp3Streamer,StreamStreamer pcmStreamer,StreamStreamer wmaStreamer,StreamStreamer wavStreamer,boolean isSong, boolean isVideo){
		//this.identifier=identifier;		
		this.formatHandler=handler;
		this.mp3Streamer=mp3Streamer;
		this.pcmStreamer=pcmStreamer;
		this.wmaStreamer=wmaStreamer;
		this.wavStreamer=wavStreamer;
		this.isSong=isSong;
		this.isVideo=isVideo;
	}
	
	public void writeMP3toStream(String url,OutputStream os)
	{
		if(mp3Streamer!=null)
		mp3Streamer.writeToStream(url,os);
	}
	
	public void writePCMtoStream(String url,OutputStream os)
	{
		if(pcmStreamer!=null)
		pcmStreamer.writeToStream(url,os);
	}
	
	public void writeWMAtoStream(String url,OutputStream os)
	{
		if(wmaStreamer!=null)
		wmaStreamer.writeToStream(url,os);
	}
	
	public void writeWAVtoStream(String url,OutputStream os)
	{
		if(wavStreamer!=null)
		wavStreamer.writeToStream(url,os);
	}
	
	public boolean isFormat(File file)
	{
		return identifier.isFormat(file);
	}
	
	public void setHandler(FormatHandler handler)
	{
		this.formatHandler = handler;
	}
	
	protected FormatHandler getHandler()
	{
		return formatHandler;
	}


	public String getITunesKind()
	{
		return iTunesKind;
	}

	public void setITunesKind(String tunesKind)
	{
		iTunesKind = tunesKind;
	}
	
	public boolean isSong()
	{
		return this.isSong;
	}
	
	public boolean isVideo()
	{
		return this.isVideo;
	}
}
