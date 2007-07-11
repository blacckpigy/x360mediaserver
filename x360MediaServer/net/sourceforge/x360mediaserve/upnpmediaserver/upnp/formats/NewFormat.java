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

import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.identifiers.Identifier;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.streamers.Streamer;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.tags.Tagger;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.contentdirectory.FormatHandler;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.Tag;



public class NewFormat {
	
	private Identifier identifier;
	private Tagger tagger;
	
	private FormatHandler handler=null;
	private String iTunesKind=null;
	
	
	private Streamer wmaStreamer=null;
	private Streamer mp3Streamer=null;
	private Streamer pcmStreamer=null;
	private Streamer wavStreamer=null;
	private Streamer wmvStreamer=null;
	
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
	
	public NewFormat(Identifier identifier,Tagger tagger,FormatHandler handler,Streamer mp3Streamer,Streamer pcmStreamer,Streamer wmaStreamer,Streamer wavStreamer,boolean isSong, boolean isVideo){
		this.identifier=identifier;
		this.tagger=tagger;
		this.handler=handler;
		this.mp3Streamer=mp3Streamer;
		this.pcmStreamer=pcmStreamer;
		this.wmaStreamer=wmaStreamer;
		this.wavStreamer=wavStreamer;
		this.isSong=isSong;
		this.isVideo=isVideo;
	}
	
	public void writeMP3toStream(File file,OutputStream os)
	{
		if(mp3Streamer!=null)
		mp3Streamer.writeToStream(file,os);
	}
	
	public void writePCMtoStream(File file,OutputStream os)
	{
		if(pcmStreamer!=null)
		pcmStreamer.writeToStream(file,os);
	}
	
	public void writeWMAtoStream(File file,OutputStream os)
	{
		if(wmaStreamer!=null)
		wmaStreamer.writeToStream(file,os);
	}
	
	public void writeWAVtoStream(File file,OutputStream os)
	{
		if(wavStreamer!=null)
		wavStreamer.writeToStream(file,os);
	}
	
	public boolean isFormat(File file)
	{
		return identifier.isFormat(file);
	}
	
	public Tag getTag(File file)
	{
		return tagger.getTag(file);
	}
	
	public void setHandler(FormatHandler handler)
	{
		this.handler = handler;
	}
	
	protected FormatHandler getHandler()
	{
		return handler;
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
