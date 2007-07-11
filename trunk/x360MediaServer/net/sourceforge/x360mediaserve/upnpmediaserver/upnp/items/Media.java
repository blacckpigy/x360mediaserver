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

import java.io.OutputStream;

import org.cybergarage.upnp.media.server.object.ContentNode;

public abstract class Media {
	
	protected int id;
	
	
	public Media(){
		
	}
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public abstract ContentNode getContentNode(String ServerAddress);
	
	
	// to be replaced by a proper output format system
	public abstract void playPCM(OutputStream os);
	
	public abstract void playMP3(OutputStream os);
	
	public abstract void playWMA(OutputStream os);
	
	//public abstract void playWMV(OutputStream os);
	
}
