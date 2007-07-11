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



package net.sourceforge.x360mediaserve.upnpmediaserver.upnp.contentdirectory;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.x360mediaserve.ConfigStream;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.Service;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.Media;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.Song;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.media.server.action.BrowseAction;
import org.cybergarage.upnp.media.server.action.SearchAction;
import org.cybergarage.upnp.media.server.object.ContentNode;
import org.cybergarage.upnp.media.server.object.ContentNodeList;
import org.cybergarage.upnp.media.server.object.DIDLLite;
import org.cybergarage.xml.Attribute;
import org.cybergarage.xml.AttributeList;


public class ContentDirectory extends Service {
	
	public final static String SCPDString = 
		"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + 
		"<scpd xmlns=\"urn:schemas-upnp-org:service-1-0\">\n" + 
		"   <specVersion>\n" + 
		"      <major>1</major>\n" + 
		"      <minor>0</minor>\n" + 
		"   </specVersion>\n" + 
		"   <actionList>\n" + 
		"      <action>\n" + 
		"         <name>ExportResource</name>\n" + 
		"         <argumentList>\n" + 
		"            <argument>\n" + 
		"               <name>SourceURI</name>\n" + 
		"               <direction>in</direction>\n" + 
		"               <relatedStateVariable>A_ARG_TYPE_URI</relatedStateVariable>\n" + 
		"            </argument>\n" + 
		"            <argument>\n" + 
		"               <name>DestinationURI</name>\n" + 
		"               <direction>in</direction>\n" + 
		"               <relatedStateVariable>A_ARG_TYPE_URI</relatedStateVariable>\n" + 
		"            </argument>\n" + 
		"            <argument>\n" + 
		"               <name>TransferID</name>\n" + 
		"               <direction>out</direction>\n" + 
		"               <relatedStateVariable>A_ARG_TYPE_TransferID</relatedStateVariable>\n" + 
		"            </argument>\n" + 
		"         </argumentList>\n" + 
		"      </action>\n" + 
		"      <action>\n" + 
		"         <name>StopTransferResource</name>\n" + 
		"         <argumentList>\n" + 
		"            <argument>\n" + 
		"               <name>TransferID</name>\n" + 
		"               <direction>in</direction>\n" + 
		"               <relatedStateVariable>A_ARG_TYPE_TransferID</relatedStateVariable>\n" + 
		"            </argument>\n" + 
		"         </argumentList>\n" + 
		"      </action>\n" + 
		"      <action>\n" + 
		"         <name>DestroyObject</name>\n" + 
		"         <argumentList>\n" + 
		"            <argument>\n" + 
		"               <name>ObjectID</name>\n" + 
		"               <direction>in</direction>\n" + 
		"               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" + 
		"            </argument>\n" + 
		"         </argumentList>\n" + 
		"      </action>\n" + 
		"      <action>\n" + 
		"         <name>DeleteResource</name>\n" + 
		"         <argumentList>\n" + 
		"            <argument>\n" + 
		"               <name>ResourceURI</name>\n" + 
		"               <direction>in</direction>\n" + 
		"               <relatedStateVariable>A_ARG_TYPE_URI</relatedStateVariable>\n" + 
		"            </argument>\n" + 
		"         </argumentList>\n" + 
		"      </action>\n" + 
		"      <action>\n" + 
		"         <name>UpdateObject</name>\n" + 
		"         <argumentList>\n" + 
		"            <argument>\n" + 
		"               <name>ObjectID</name>\n" + 
		"               <direction>in</direction>\n" + 
		"               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" + 
		"            </argument>\n" + 
		"            <argument>\n" + 
		"               <name>CurrentTagValue</name>\n" + 
		"               <direction>in</direction>\n" + 
		"               <relatedStateVariable>A_ARG_TYPE_TagValueList</relatedStateVariable>\n" + 
		"            </argument>\n" + 
		"            <argument>\n" + 
		"               <name>NewTagValue</name>\n" + 
		"               <direction>in</direction>\n" + 
		"               <relatedStateVariable>A_ARG_TYPE_TagValueList</relatedStateVariable>\n" + 
		"            </argument>\n" + 
		"         </argumentList>\n" + 
		"      </action>\n" + 
		"      <action>\n" + 
		"         <name>Browse</name>\n" + 
		"         <argumentList>\n" + 
		"            <argument>\n" + 
		"               <name>ObjectID</name>\n" + 
		"               <direction>in</direction>\n" + 
		"               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" + 
		"            </argument>\n" + 
		"            <argument>\n" + 
		"               <name>BrowseFlag</name>\n" + 
		"               <direction>in</direction>\n" + 
		"               <relatedStateVariable>A_ARG_TYPE_BrowseFlag</relatedStateVariable>\n" + 
		"            </argument>\n" + 
		"            <argument>\n" + 
		"               <name>Filter</name>\n" + 
		"               <direction>in</direction>\n" + 
		"               <relatedStateVariable>A_ARG_TYPE_Filter</relatedStateVariable>\n" + 
		"            </argument>\n" + 
		"            <argument>\n" + 
		"               <name>StartingIndex</name>\n" + 
		"               <direction>in</direction>\n" + 
		"               <relatedStateVariable>A_ARG_TYPE_Index</relatedStateVariable>\n" + 
		"            </argument>\n" + 
		"            <argument>\n" + 
		"               <name>RequestedCount</name>\n" +
		"               <direction>in</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>SortCriteria</name>\n" +
		"               <direction>in</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_SortCriteria</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>Result</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_Result</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>NumberReturned</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>TotalMatches</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>UpdateID</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_UpdateID</relatedStateVariable>\n" +
		"            </argument>\n" +
		"         </argumentList>\n" +
		"      </action>\n" +
		"      <action>\n" +
		"         <name>GetTransferProgress</name>\n" +
		"         <argumentList>\n" +
		"            <argument>\n" +
		"               <name>TransferID</name>\n" +
		"               <direction>in</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_TransferID</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>TransferStatus</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_TransferStatus</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>TransferLength</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_TransferLength</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>TransferTotal</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_TransferTotal</relatedStateVariable>\n" +
		"            </argument>\n" +
		"         </argumentList>\n" +
		"      </action>\n" +
		"      <action>\n" +
		"         <name>GetSearchCapabilities</name>\n" +
		"         <argumentList>\n" +
		"            <argument>\n" +
		"               <name>SearchCaps</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>SearchCapabilities</relatedStateVariable>\n" +
		"            </argument>\n" +
		"         </argumentList>\n" +
		"      </action>\n" +
		"      <action>\n" +
		"         <name>CreateObject</name>\n" +
		"         <argumentList>\n" +
		"            <argument>\n" +
		"               <name>ContainerID</name>\n" +
		"               <direction>in</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>Elements</name>\n" +
		"               <direction>in</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_Result</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>ObjectID</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>Result</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_Result</relatedStateVariable>\n" +
		"            </argument>\n" +
		"         </argumentList>\n" +
		"      </action>\n" +
		"      <action>\n" +
		"         <name>Search</name>\n" +
		"         <argumentList>\n" +
		"            <argument>\n" +
		"               <name>ContainerID</name>\n" +
		"               <direction>in</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>SearchCriteria</name>\n" +
		"               <direction>in</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_SearchCriteria</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>Filter</name>\n" +
		"               <direction>in</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_Filter</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>StartingIndex</name>\n" +
		"               <direction>in</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_Index</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>RequestedCount</name>\n" +
		"               <direction>in</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>SortCriteria</name>\n" +
		"               <direction>in</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_SortCriteria</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>Result</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_Result</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>NumberReturned</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>TotalMatches</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_Count</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>UpdateID</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_UpdateID</relatedStateVariable>\n" +
		"            </argument>\n" +
		"         </argumentList>\n" +
		"      </action>\n" +
		"      <action>\n" +
		"         <name>GetSortCapabilities</name>\n" +
		"         <argumentList>\n" +
		"            <argument>\n" +
		"               <name>SortCaps</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>SortCapabilities</relatedStateVariable>\n" +
		"            </argument>\n" +
		"         </argumentList>\n" +
		"      </action>\n" +
		"      <action>\n" +
		"         <name>ImportResource</name>\n" +
		"         <argumentList>\n" +
		"            <argument>\n" +
		"               <name>SourceURI</name>\n" +
		"               <direction>in</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_URI</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>DestinationURI</name>\n" +
		"               <direction>in</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_URI</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>TransferID</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_TransferID</relatedStateVariable>\n" +
		"            </argument>\n" +
		"         </argumentList>\n" +
		"      </action>\n" +
		"      <action>\n" +
		"         <name>CreateReference</name>\n" +
		"         <argumentList>\n" +
		"            <argument>\n" +
		"               <name>ContainerID</name>\n" +
		"               <direction>in</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>ObjectID</name>\n" +
		"               <direction>in</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" +
		"            </argument>\n" +
		"            <argument>\n" +
		"               <name>NewID</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>A_ARG_TYPE_ObjectID</relatedStateVariable>\n" +
		"            </argument>\n" +
		"         </argumentList>\n" +
		"      </action>\n" +
		"      <action>\n" +
		"         <name>GetSystemUpdateID</name>\n" +
		"         <argumentList>\n" +
		"            <argument>\n" +
		"              <name>Id</name>\n" +
		"               <direction>out</direction>\n" +
		"               <relatedStateVariable>SystemUpdateID</relatedStateVariable>\n" +
		"            </argument>\n" +
		"         </argumentList>\n" +
		"      </action>\n" +
		"   </actionList>\n" +
		"   <serviceStateTable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_SortCriteria</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_TransferLength</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"yes\">\n" +
		"         <name>TransferIDs</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_UpdateID</name>\n" +
		"         <dataType>ui4</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_SearchCriteria</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_Filter</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"yes\">\n" +
		"         <name>ContainerUpdateIDs</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_Result</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_Index</name>\n" +
		"         <dataType>ui4</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_TransferID</name>\n" +
		"         <dataType>ui4</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_TagValueList</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_URI</name>\n" +
		"         <dataType>uri</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_ObjectID</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>SortCapabilities</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>SearchCapabilities</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_Count</name>\n" +
		"         <dataType>ui4</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_BrowseFlag</name>\n" +
		"         <dataType>string</dataType>\n" +
		"         <allowedValueList>\n" +
		"            <allowedValue>BrowseMetadata</allowedValue>\n" +
		"            <allowedValue>BrowseDirectChildren</allowedValue>\n" +
		"         </allowedValueList>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"yes\">\n" +
		"         <name>SystemUpdateID</name>\n" +
		"         <dataType>ui4</dataType>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_TransferStatus</name>\n" +
		"         <dataType>string</dataType>\n" +
		"         <allowedValueList>\n" +
		"            <allowedValue>COMPLETED</allowedValue>\n" +
		"            <allowedValue>ERROR</allowedValue>\n" +
		"            <allowedValue>IN_PROGRESS</allowedValue>\n" +
		"            <allowedValue>STOPPED</allowedValue>\n" +
		"         </allowedValueList>\n" +
		"      </stateVariable>\n" +
		"      <stateVariable sendEvents=\"no\">\n" +
		"         <name>A_ARG_TYPE_TransferTotal</name>\n" +
		"         <dataType>string</dataType>\n" +
		"      </stateVariable>\n" +
		"   </serviceStateTable>\n" +
		"</scpd>";
	public final static String SERVICE_TYPE = "urn:schemas-upnp-org:service:ContentDirectory:1";
	
	File basePath=null;
	
	MusicDB musicDB=new MusicDB();
	
	String SERVICE_STRING="urn:schemas-upnp-org:service:ContentDirectory:1";
	
	private int systemUpdateID=0;
	
	public ContentDirectory(){
		super();
		try{
			debug(System.currentTimeMillis()+ " Servlet init");
			loadSCPD(SCPDString);

			buildActionList();
			this.setServiceString(SERVICE_STRING);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	
	boolean browseActionReceived(BrowseAction browseact){
		debug("Browse action called");
		return false;
	}
	
	@Override
	protected boolean doAction(Action action,String ServerAddress) {
		// TODO Auto-generated method stub
		
		String actionName = action.getName();
		
		if (actionName.equals("Browse")) {
			BrowseAction browseAct = new BrowseAction(action);
			return browseActionReceived(browseAct);
		}
		
		else if (actionName.equals("Search")) {
			SearchAction searchAct = new SearchAction(action);
			searchActionReceived(searchAct,ServerAddress);
			return true;
//			return searchActionReceived(searchAct);
		}
		
		//@id,@parentID,dc:title,dc:date,upnp:class,res@protocolInfo
		else if (actionName.equals("GetSearchCapabilities") == true) {
			Argument searchCapsArg = action.getArgument("SearchCaps");
//			String searchCapsStr = getSearchCapabilities();
//			searchCapsArg.setValue(searchCapsStr);
			return true;
		}
		
		//dc:title,dc:date,upnp:class
		if (actionName.equals("GetSortCapabilities") == true) {
			Argument sortCapsArg = action.getArgument("SortCaps");
			return true;
		}
		
		if (actionName.equals("GetSystemUpdateID") == true) {
//			Argument idArg = action.getArgument(ID);
//			idArg.setValue(getSystemUpdateID());
			return true;
		}
		
		return false;		
	}
	

	public void doGet(HttpServletRequest req,HttpServletResponse resp){
		
		try{
			debug("Contextpath:"+req.getContextPath());
			debug("pathinfo:"+req.getPathInfo());
			debug("request uri:"+req.getRequestURI());
			debug("path translated:"+req.getPathTranslated());
			debug("Query string"+req.getQueryString());
			if(req.getPathInfo().contains("Music")){
				System.out.println("Music Requested");
				playMusic(req,resp);
			}
			else if(req.getPathInfo().contains("Dump"))
			{
				// dump music
				PrintWriter writer=resp.getWriter();
				for(Integer key:musicDB.mediaMap.keySet())
				{
					Media container=musicDB.mediaMap.get(key);
					if(container instanceof Song)
						writer.println(key+":"+((Song)container).getTitle());
					
				}
			}
			else
			{
				resp.getWriter().write(SCPDString);
			}

			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
	
	public synchronized int getSystemUpdateID()
	{
		return systemUpdateID;
	}
	
	/* (non-Javadoc)
	 * Cheap configure script until someone writes a better one
	 * @see net.sourceforge.x360mediaserve.upnpmediaserver.upnp.Service#handleOtherPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void handleOtherPost(HttpServletRequest req, HttpServletResponse resp){
		if(req.getRemoteHost().contains("127.0.0.1")){
			debug(req.getPathInfo()+" "+req.getRemoteHost());
			if(req.getPathInfo().endsWith("configure")){
				debug("Doing configure");
				System.out.println("Doing configure");
				try{
					BufferedReader reader=new BufferedReader(req.getReader());
					String str;					
					while((str=reader.readLine())!=null){
						String[] confoptions=str.split("&");
						for(String s:confoptions){
							debug(s);
							String[] option=s.split("=");
							if(option.length>1){
								if(option[0].contains("musicdir")){
									File f=new File(option[1].replace("%3A",":").replace("%5C","\\").replace("%2F","/").replace("+"," "));
									if(f.exists() && f.isDirectory()){									
										debug("Adding music dir"+f.toString());
										musicDB.addFile(f);
									}
								}
								if(option[0].contains("itunesfile")){
									File f=new File(option[1].replace("%3A",":").replace("%5C","\\").replace("%2F","/").replace("+"," "));
									System.out.println(f.toString());
									if(f.exists() && f.isFile()){									
										debug("Adding iTunes dir"+f.toString());
										musicDB.addItunesDataBase(f);
									}
								}
							}
						}
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
			}
			else if(req.getPathInfo().endsWith("addStream")){
				try
				{
					BufferedReader reader=new BufferedReader(req.getReader());
					String str;
					String urlString=null;
					String streamName=null;
					while((str=reader.readLine())!=null){
						String[] confoptions=str.split("&");
						for(String s:confoptions){
							debug(s);
							String[] option=s.split("=");
							if(option.length>1){
								if(option[0].contains("name")){
									streamName=new String(option[1].replace("%3A",":").replace("%5C","\\").replace("%2F","/").replace("+"," "));
			
								}
								if(option[0].contains("URL")){
									urlString=new String(option[1].replace("%3A",":").replace("%5C","\\").replace("%2F","/").replace("+"," "));
								}
							}
						}
					}
					
					
					if(urlString!=null && streamName!=null)
					{						
							//String url=new URL(urlString);
							musicDB.addStream(streamName,urlString,0);
							System.out.println("Added new Stream:"+streamName+" at "+urlString);
						
						
						
					}
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Adds this stream (URL) to the music database.
	 * @param streamNode
	 */
	public void addStream(ConfigStream streamNode)
    {
	     musicDB.addStream(streamNode.name,streamNode.url,streamNode.type);
    }
	
	/**
	 * Adds this stream (URL) to the music database.
	 * @param name
	 * @param url
	 * @param type
	 */
	public void addStream(String name,String url,int type){
		musicDB.addStream(name,url,type);
	}
	
	/**
	 * Adds this stream (iTunes) to the music database.
	 * @param file
	 */
	public void addiTunesDB(File file){
		musicDB.addItunesDataBase(file);
	}
	
	/**
	 * Adds this stream (files) to the music database.
	 * @param file
	 */
	public void addMusicDir(File file){
		musicDB.addFile(file);
	}
	
	
	private void playMusic(HttpServletRequest req,HttpServletResponse resp){
		String idString=req.getPathInfo().substring(req.getPathInfo().lastIndexOf("/")+1);
		
		idString=idString.substring(0,idString.indexOf("."));
		debug("idString:"+idString);
		try{			
			BufferedOutputStream os=new BufferedOutputStream(resp.getOutputStream(),10000);
			if(req.getQueryString()!=null && req.getQueryString().toLowerCase().contains("format=pcm")){
				resp.setContentType("audio/L16; rate=44100; channels=2");
				musicDB.playSongAsPCM(Integer.parseInt(idString),os);
			}
			else if(req.getPathInfo().substring(req.getPathInfo().lastIndexOf("/")+1).toLowerCase().endsWith("mp3")){
				System.out.println("Playing mp3");
				resp.setContentType("audio/mpeg");
				musicDB.playSongAsMP3(Integer.parseInt(idString),os);
			}
			else if(req.getPathInfo().substring(req.getPathInfo().lastIndexOf("/")+1).toLowerCase().endsWith("wma")){				
				resp.setContentType("audio/x-ms-wma");
				musicDB.playSongAsWMA(Integer.parseInt(idString),os);
			}
			//video
			else if(req.getPathInfo().substring(req.getPathInfo().lastIndexOf("/")+1).toLowerCase().endsWith("wmv")){				
				resp.setContentType("video/x-ms-wmv");
				resp.setHeader("Accept-Ranges","bytes");
				resp.setHeader("Content-Length", ""+Integer.MAX_VALUE*4);
				//resp.setContentLength(Integer.MAX_VALUE);				
				musicDB.playVideoAsWMV(Integer.parseInt(idString),os);
			}			
			os.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private boolean searchActionReceived(SearchAction action,String ServerAddress)
	{
		String containerID = action.getContainerID();
		String searchCriteria = action.getSearchCriteria();
		
		debug("Search started");
		ContentNodeList sortedContentNodeList = new ContentNodeList();
		musicDB.getHackSearchList(sortedContentNodeList,containerID,searchCriteria,ServerAddress);
		int nChildNodes=sortedContentNodeList.size();
		
		int startingIndex = action.getStartingIndex();
		if (startingIndex <= 0)
			startingIndex = 0;
		int requestedCount = action.getRequestedCount();
		if (requestedCount == 0)
			requestedCount = nChildNodes;
		
		DIDLLite didlLite = new DIDLLite();
		int numberReturned = 0;
		for (int n=startingIndex; (n<nChildNodes && numberReturned<requestedCount); n++) {		
			ContentNode cnode = sortedContentNodeList.getContentNode(n);
			didlLite.addContentNode(cnode);
			numberReturned++;
		}
		
		AttributeList atrrlist1=new AttributeList();	
		atrrlist1.add(new Attribute("dt:dt","ui4"));
		
		AttributeList atrrlist2=new AttributeList();
		atrrlist2.add(new Attribute("xmlns:dt","urn:schemas-microsoft-com:datatypes"));
		atrrlist2.add(new Attribute("dt:dt","ui4"));
		
		String result = didlLite.toString();		
		action.setResult(result);		
		action.setNumberReturned(numberReturned);
		action.setTotalMaches(nChildNodes);
		action.setUpdateID(getSystemUpdateID());
		return true;
	}
	
	public synchronized void updateSystemUpdateID()
	{
		systemUpdateID++;
	}
	
	public void setPCMOption(boolean option){
		musicDB.setPCMOption(option);
	}
}
