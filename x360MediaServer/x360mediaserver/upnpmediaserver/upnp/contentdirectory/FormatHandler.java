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

package x360mediaserver.upnpmediaserver.upnp.contentdirectory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import x360mediaserver.upnpmediaserver.upnp.formats.NewFormat;



public class FormatHandler {
	ArrayList<NewFormat> formats=new ArrayList<NewFormat>();
	
	public Collection<NewFormat> getFormats(){
		return formats;
	}
	
	public void addFormat(NewFormat format){
		formats.add(format);
		format.setHandler(this);
	}
	
	File scriptDir=null;

	public File getScriptDir() {
		return scriptDir;
	}

	public void setScriptDir(File scriptDir) {
		this.scriptDir = scriptDir;
	}
	
	public NewFormat getFormatFromItunesKind(String kind){
		for(NewFormat format:formats){
			if(kind.equals(format.getITunesKind())){
				return format;
			}
		}
		return null;
	}
	
	private boolean usePCM=false;

	public boolean isUsePCM() {
		return usePCM;
	}

	public void setUsePCM(boolean usePCM) {
		this.usePCM = usePCM;
	}
	
}
