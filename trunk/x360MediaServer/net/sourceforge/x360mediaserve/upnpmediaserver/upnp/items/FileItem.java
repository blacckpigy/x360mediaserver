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

import java.io.File;
import java.io.OutputStream;

import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.NewFormat;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.contentdirectory.FormatHandler;


public class FileItem {
	
	// class to handle file related stuff inside music items
	
	File file=null;
	NewFormat format;	
	Tag tags;
	boolean isSong=false;
	boolean isVideo=false;
	FormatHandler formatHandler;
	
	public FileItem(File file,FormatHandler formatHandler){
		
		this.file=file;
		//System.out.println("New fileitem:"+file.toString());
		this.formatHandler=formatHandler;
		process();
	}
	
	public FileItem(File file,FormatHandler formatHandler, NewFormat format,Tag tag){
		this.file=file;
		this.format=format;
		this.tags=tag;
		this.formatHandler=formatHandler;
		isSong=true;
	}
	
	private void process(){
		for(NewFormat format:formatHandler.getFormats()){
			try{
				if(format.isFormat(file)){	
					this.format=format;
					tags=format.getTag(file);
					isSong=format.isSong();
					isVideo=format.isVideo();
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public Tag getTag(){
		return tags;
	}
	
	public boolean isSong(){
		return isSong;
	}
	
	public boolean isVideo(){
		return isVideo;
	}
	
	public File getFile(){
		return file;
	}
	
	public void writePCM(OutputStream os){ // write mp3 to provided outputstream
		format.writePCMtoStream(file,os);
	}
	
	public void writeMP3(OutputStream os){ // write mp3 to provided outputstream
		format.writeMP3toStream(file,os);
	}
	
	public void writeWMA(OutputStream os){ // write mp3 to provided outputstream
		format.writeWMAtoStream(file,os);
	}
	
}
