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


package x360mediaserver.upnpmediaserver.upnp.formats.streamers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/** Handles files that are already in the correct format
 * @author tom
 *
 */
public class Native implements Streamer {
	
	
	public void writeMP3toStream(File file,OutputStream os){
	    writeToStream(file,os);
	}
	
	public void writePCMtoStream(File file,OutputStream os){		
		writeToStream(file,os);
	}		
	
	public void writeWMAtoStream(File file,OutputStream os){
		writeToStream(file,os);
	}

    public void writeToStream(File file, OutputStream os)
    {
        StreamThreader thread = new StreamThreader(file, os);
        thread.start();
    }

}
