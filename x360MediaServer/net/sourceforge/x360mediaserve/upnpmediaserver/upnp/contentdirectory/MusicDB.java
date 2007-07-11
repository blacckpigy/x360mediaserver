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


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.NewFormat;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.StreamFormat;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.identifiers.ExtensionIdentifier;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.streamers.External;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.streamers.ExternalFile;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.streamers.Native;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.streamers.StreamExternal;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.streamers.StreamNative;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.tags.EntaggedTagger;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.tags.FileTagger;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.tags.MP4Tagger;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.Album;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.Artist;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.Container;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.FileItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.Media;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.Playlist;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.Song;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.Stream;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.Video;

import org.cybergarage.upnp.media.server.object.ContentNodeList;

/** Basic Music Store
 * @author Tom
 *
 */
public class MusicDB {
	
	
	
	TreeMap<String,Album> albumMap;
	TreeMap<String,Artist> artistMap;
	HashMap<Integer,Media> mediaMap;
	HashMap<Integer,Container> containerMap;
	Playlist streamPlaylist;
	
	TreeSet<Song> songSet;
	TreeSet<Video> videoSet;
	int nextContainerID=4241;
	int nextSongID=4241;
	
	HashMap<Integer,Playlist> playLists;
	
	
	FormatHandler formatHandler=new FormatHandler();
	
	StreamFormat[] streamFormats=new StreamFormat[3];
	
	public void reset(){
		containerMap = new HashMap<Integer,Container>();
		albumMap = new TreeMap<String,Album>();
		artistMap= new TreeMap<String,Artist>();
		playLists= new HashMap<Integer,Playlist>();	
		songSet=new TreeSet<Song>(			
				new Comparator<Song>(){
					public int compare(Song o1,
							Song o2){
						
						if(o1.getTitle()==null) return 1;
						else if(o2.getTitle()==null) return -1;
						else return o1.getTitle().compareToIgnoreCase(o2.getTitle());
					}
				});
		
		videoSet=new TreeSet<Video>(			
				new Comparator<Video>(){
					public int compare(Video o1,
							Video o2){
						
						if(o1.getTitle()==null) return 1;
						else if(o2.getTitle()==null) return -1;
						else return o1.getTitle().compareToIgnoreCase(o2.getTitle());
					}
				});
		
		mediaMap=new HashMap<Integer,Media>();
	}
	
	
	public MusicDB(){
		reset();
		
		initFormats();
		
		
			try {
				if(System.getProperty("os.name").toLowerCase().contains("windows")){
					formatHandler.setScriptDir(new File((new File(".").getCanonicalPath())+"\\ScriptDir"));							
				}
				else{
					formatHandler.setScriptDir(new File((new File(".").getCanonicalPath())+"/ScriptDir"));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();				
			}
			
		
		addArtist("");
		addStreamPlaylist();
	}
	
	// add album to the DB , probably needs to be updated to handle compilations
	
	/** Setup formats
	 * 
	 */
	private void initFormats(){
		formatHandler.setUsePCM(false);
		//mp3
		NewFormat newformat=new NewFormat(new ExtensionIdentifier("mp3"),new EntaggedTagger(),formatHandler,new Native(),null,null,null,true,false);
		newformat.setITunesKind("MPEG audio file");
		formatHandler.addFormat(newformat);
		//aac
		newformat=new NewFormat(new ExtensionIdentifier("m4a"),new MP4Tagger(),formatHandler,new External("m4amp3",formatHandler),new External("m4apcm",formatHandler),null,null,true,false);
		newformat.setITunesKind("AAC audio file");
		formatHandler.addFormat(newformat);
		//flac
		formatHandler.addFormat(new NewFormat(new ExtensionIdentifier("flac"),new EntaggedTagger(),formatHandler,null,new External("flacpcm",formatHandler),null,null,true,false));
		//ogg
		formatHandler.addFormat(new NewFormat(new ExtensionIdentifier("ogg"),new EntaggedTagger(),formatHandler,new External("oggmp3",formatHandler),null,null,null,true,false));
		//wma
		formatHandler.addFormat(new NewFormat(new ExtensionIdentifier("wma"),new EntaggedTagger(),formatHandler,null,null,new Native(),null,true,false));
		formatHandler.addFormat(new NewFormat(new ExtensionIdentifier("wmv"),new FileTagger(),formatHandler,null,null,new Native(),null,false,true));
		formatHandler.addFormat(new NewFormat(new ExtensionIdentifier("avi"),new FileTagger(),formatHandler,null,null,new External("aviwmv",formatHandler),null,false,true));
		
		
		streamFormats=new StreamFormat[3];
		// mp3 streams
		streamFormats[0]=new StreamFormat(formatHandler,new StreamNative(),null,null,null,true, false);		
		streamFormats[1]=new StreamFormat(formatHandler,null,null,new StreamNative(),null,true, false);
		streamFormats[2]=new StreamFormat(formatHandler,new StreamExternal("playstreamasmp3",formatHandler),new StreamExternal("playstreamaspcm",formatHandler),null,null,true, false);
	}
	
	/** Create an album with the given name and artist
	 * @param albumString Name of Album
	 * @param artist Artist
	 * @return The created album
	 */
	private Album addAlbum(String albumString,Artist artist){
		System.out.println("New Album:"+albumString);
		if(albumString==null){
			albumString=""; // handle no album
			artist=findArtist("");
		}
		
		
		Album newalbum=new Album(albumString,artist);
		
		newalbum.setContainerID(nextContainerID());
		//System.out.println("Adding album:"+albumString);
		albumMap.put(albumString.toLowerCase().trim(),newalbum);
		containerMap.put(newalbum.getContainerID(),newalbum);
		artist.addAlbum(newalbum);
		//System.out.println("Added album:"+albumString+" "+newalbum.getContainerID());
		
		return newalbum;
	}
	
	
	/** Add an Artist
	 * @param artistString Artist's name
	 * @return The Artist
	 */
	private Artist addArtist(String artistString){
		Artist artist=new Artist(artistString);
		System.out.println("Adding artist:"+artistString);
		artist.setContainerID(nextContainerID());
		artistMap.put(artistString.toLowerCase().trim(),artist);
		containerMap.put(artist.getContainerID(),artist);
		return artist;
	}
	
	
	// function to add Song to library
	/** Add Song 
	 * @param file 
	 * @return
	 */
	private boolean addSong(FileItem file){
		Song newsong=new Song(file);
		return addSong(newsong);		
	}
	
	/** Add song to DB
	 * @param newsong Song to add
	 * @return
	 */
	protected boolean addSong(Song newsong){
		String artistString=newsong.getArtistString();
		Artist artist=findArtist(artistString);
		if(artist==null){ // if the artist wasn't found in the library
			artist=addArtist(artistString);
		}
		String albumString=newsong.getAlbumString();
		Album album=findAlbum(albumString);
		if(album==null){
			album=addAlbum(albumString,artist);
		}
		
		
		newsong.setAlbum(album);	
		newsong.setArtist(artist);		
		newsong.setId(nextSongID());
		//	debug("adding song to album "+file.getFile().toString());
		album.addSong(newsong);
		//debug("adding song to artist "+file.getFile().toString());
		artist.addSong(newsong);
		//debug("adding song to idlist "+file.getFile().toString());
		mediaMap.put(newsong.getId(),newsong);
		
			//songMap.put(newsong.getTitle().toLowerCase().trim(),newsong);
			songSet.add(newsong);
		
		return true;
	}
	
	/** Add Video 
	 * @param file 
	 * @return
	 */
	private boolean addVideo(FileItem file){
		
		System.out.println("Adding video");
		Video newVideo=new Video(file);
		System.out.println("Video item created with title:"+newVideo.getTitle()+" "+newVideo.getId());
		return addVideo(newVideo);
	}
	
	/** Add video to DB
	 * @param newsong Song to add
	 * @return
	 */
	protected boolean addVideo(Video newvideo){				
						
		
				
		newvideo.setId(nextSongID());
		//	debug("adding song to album "+file.getFile().toString());		
		//debug("adding song to idlist "+file.getFile().toString());
		mediaMap.put(newvideo.getId(),newvideo);
		try{
			//songMap.put(newsong.getTitle().toLowerCase().trim(),newsong);
			videoSet.add(newvideo);
		}
		catch(Exception e){
			System.out.println("Exception in add video");
			debug(e.toString());
			e.printStackTrace();
			System.out.println(e.toString());
			
		}
		return true;
	}
	
	
	
	/** Find album entry
	 * @param Album string to match 
	 * @return corresponding album or null if no album exists
	 */
	private Album findAlbum(String album){
		if(album==null) album=""; // handle no album
		String albumName=album.toLowerCase().trim();
		return albumMap.get(albumName);
	}
	
	// function to find artist in library, should probably do stuff like matching variations on name e.g The, capitals etc
	private Artist findArtist(String artist){
		if(artist==null) artist="";
		String artistName=artist.toLowerCase().trim();
		
		return artistMap.get(artistName);
	}
	
	private int nextContainerID(){
		return nextContainerID++;
	}
	
	private int nextSongID(){
		return nextSongID++;
	}
	
	public void addFile(File file){ // add file to the database
		
		if(file.isDirectory()){ // if it is a directory then add its children			
			try{
				System.out.println("Got directory:"+file.getCanonicalPath());
				File[] filearray=file.listFiles();
				for(int i=0;i<filearray.length;i++){
					if(filearray[i].canRead()) addFile(filearray[i]); // if we can't read the file then ignore it
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}			
		}
		else{ // if its a file
			if(!file.getName().startsWith("._")){ // drop annoying hidden mac files 
				FileItem fileItem=new FileItem(file,formatHandler);
				System.out.println("Got File:"+file);
				if(fileItem.isSong())
					addSong(fileItem);
				else if(fileItem.isVideo())
				{
					System.out.println("It's a video");
					addVideo(fileItem);
				}
			}
		}
		
	}
	
	public void addPlaylist(Playlist playlist){
		System.out.println("Adding playlist:"+playlist.getName());
		playlist.setContainerID(nextContainerID());
		playLists.put(playlist.getContainerID(),playlist);
		containerMap.put(playlist.getContainerID(),playlist);		
	}
	
	public void addItunesDataBase(File file){
		
		
		ITunesDB iTunesDB=new ITunesDB();		
		iTunesDB.readiTunesXML(file,formatHandler);	
		for(Song song:iTunesDB.getSongs()){
			addSong(song);
		}		
		for(Playlist playlist:iTunesDB.getPlaylists()){
			addPlaylist(playlist);
		}
		
		
	}
	
	public void playSongAsPCM(int id,OutputStream os){
		System.out.println("playing song "+id);
		mediaMap.get(id).playPCM(os);
	}
	
	public void playSongAsMP3(int id,OutputStream os){
		System.out.println("playing song "+id);
		mediaMap.get(id).playMP3(os);
	}
	
	public void playSongAsWMA(int id,OutputStream os){
		System.out.println("playing song "+id);
		mediaMap.get(id).playWMA(os);
	}
	
	public void playVideoAsWMV(int id,OutputStream os){
		System.out.println("playing video "+id);
		mediaMap.get(id).playWMA(os);
	}
	
	
	protected void debug(String str){
//		try{
//		File outputFile = new File("E:\\testlog.txt");
//		PrintWriter debug = new PrintWriter(new FileWriter(outputFile,true));	
//		debug.println(System.currentTimeMillis()+" "+str);
//		debug.flush();
//		debug.close();
//		}
//		catch(Exception e){
//		
//		}
	}
	
	public void performFullSearch(ContentNodeList contentlist,String searchstring){
		if(searchstring.contains("upnp:artist")){
			String tmp=searchstring.substring(searchstring.indexOf("upnp:artist"));
			tmp=tmp.substring(tmp.indexOf("\"")+1);
			tmp=tmp.substring(0,tmp.indexOf("\""));
			debug("got "+tmp+" from "+searchstring);
			Artist artist=artistMap.get(tmp.toLowerCase().trim());
			if(artist!=null) contentlist.addAll(artist.getAlbumContentList());
		}
		
	}
	
	public void getHackSearchList(ContentNodeList contentlist,String containerID,String searchCriList,String serverAddress)
	{
		
		
		
		
		if(containerID.equals("1")){		
			performFullSearch(contentlist,searchCriList);
		}
		else if(containerID.equals("4")){
			//case 4:
			for(Song song:songSet){
				contentlist.add(song.getContentNode(serverAddress));
				//System.out.println("Adding song:"+song.getTitle());
				//System.out.println(contentlist.size());
			}
		}
		else if(containerID.equals("6")){
			//case 6: // artists
			for(Artist artist:artistMap.values()){
				if(artist.getName()!="")
					contentlist.add(artist.getSummaryContentNode());
			}
		}
		else if(containerID.equals("7")){
			//case 7: // albums
			for(Album album:albumMap.values()){
				//contentlist.add(album.getContentNode());
				if(album.getName()!="")
					contentlist.add(album.getSummaryContentNode());
			}
		}
		else if(containerID.equals("8")){ // video stuff
			System.out.println("Printing video list");
			for(Video video:videoSet)
			{
				System.out.println(video.getTitle());
				contentlist.add(video.getContentNode(serverAddress));
				System.out.println(video.getContentNode(serverAddress));
			}			
		}
		else if(containerID.equals("F")){
			for(Playlist playlist:playLists.values()){
				contentlist.add(playlist.getSummaryContentNode());
			}
		}
		else{
			//default:
			int containerNumber=Integer.parseInt(containerID);
			debug("getting content "+containerNumber);
			Container cont=containerMap.get(containerNumber);
			debug(containerNumber+" got");
			if(cont==null) debug("got null");
			contentlist.addAll(cont.getContentList(serverAddress));
			
			
		}
		
	}
	
	
	public void setScriptDir(File f){
		formatHandler.setScriptDir(f);
	}
	
	
	
	
	// hack to add a test stream
	
	private void addStreamPlaylist()
	{
		streamPlaylist=new Playlist();
		streamPlaylist.setName("Streams");
		
		addPlaylist(streamPlaylist);
	}
	
	public void addStream(Stream stream){
		stream.setId(nextSongID());
		mediaMap.put(stream.getId(),stream);
		streamPlaylist.addSong(stream);
	}
//	
//	public void addStream(String name,URL streamURL)
//	{
//		Stream stream=new Stream(streamURL);
//		stream.setName(name);
//		addStream(stream);
//	}
	
	public void addStream(String name,String streamURL, int type)
	{
		System.out.println(name+" "+streamURL.toString()+" "+type);
		Stream stream=new Stream(streamURL,this.streamFormats[type]);
		stream.setName(name);
		addStream(stream);
	}
	
	public void setPCMOption(boolean option){
		formatHandler.setUsePCM(option);
	}
	
	
}
