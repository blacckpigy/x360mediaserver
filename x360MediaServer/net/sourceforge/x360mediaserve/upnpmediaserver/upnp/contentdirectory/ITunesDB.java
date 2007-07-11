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
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import net.sourceforge.x360mediaserve.plistreader.PlistReader;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.formats.NewFormat;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.FileItem;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.Media;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.Playlist;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.Song;
import net.sourceforge.x360mediaserve.upnpmediaserver.upnp.items.Tag;


/** System to import the ItunesDB
 * @author tom
 *
 */
public class ITunesDB{

	public ITunesDB(){	
	}

	HashMap<Integer,Song> songs=new HashMap<Integer,Song>();
	ArrayList<Playlist> playlists=new ArrayList<Playlist>();

	public Collection<Song> getSongs(){
		return songs.values();
	}

	public ArrayList<Playlist> getPlaylists(){
		return playlists;
	}

	private void getSongsFromTrackDictionary(HashMap<String,Object> dictionary,FormatHandler formatHandler){

		int trackid=0;
		for(Object nextobj:dictionary.values()){
			HashMap<String,Object> songDict=(HashMap<String,Object>)nextobj;
			Song song=new Song();
			Tag tag=song.getTag();

			String trackType=null;
			String fileURL=null;
			NewFormat format=null;

			for(String key:songDict.keySet()){
				if(key.equals("Track ID")){
					trackid=(((Long)(songDict.get(key))).intValue());
				}
				else if(key.equals("Name")){
					tag.setTitle((String)(songDict.get(key)));
				}
				else if(key.equals("Artist")){
					tag.setArtist((String)(songDict.get(key)));
				}
				else if(key.equals("Composer")){

				}
				else if(key.equals("Album")){
					tag.setAlbum((String)(songDict.get(key)));
				}
				else if(key.equals("Genre")){

				}
				else if(key.equals("Kind")){
					format=formatHandler.getFormatFromItunesKind((String)(songDict.get(key)));
				}
				else if(key.equals("Size")){
					tag.setSize(((Long)(songDict.get(key))).intValue());
				}
				else if(key.equals("Total Time")){
					tag.setTime(((Long)(songDict.get(key))).intValue());
				}
				else if(key.equals("Disc Number")){

				}
				else if(key.equals("Disc Count")){

				}
				else if(key.equals("Track Number")){
					tag.setTracknumber(((Long)(songDict.get(key))).intValue());
				}
				else if(key.equals("Track Count")){
					//tag.setTracknumber((Integer)(songDict.get(key)));
				}
				else if(key.equals("Year")){
					tag.setYear(""+(Long)(songDict.get(key)));
				}
				else if(key.equals("Date Modified")){

				}
				else if(key.equals("Date Added")){

				}
				else if(key.equals("Bit Rate")){
					tag.setBitrate(((Long)(songDict.get(key))).intValue());
				}
				else if(key.equals("Sample Rate")){
					tag.setSamplerate(((Long)(songDict.get(key))).intValue());
				}
				else if(key.equals("Comments")){

				}
				else if(key.equals("Artwork Count")){

				}
				else if(key.equals("Play Count")){

				}
				else if(key.equals("Play Date")){

				}
				else if(key.equals("Play Date UTC")){

				}
				else if(key.equals("Rating")){

				}
				else if(key.equals("Release Date")){

				}
				else if(key.equals("Persistent ID")){

				}
				else if(key.equals("Track Type")){
					trackType=(String)(songDict.get(key));
				}
				else if(key.equals("File Type")){

				}
				else if(key.equals("File Creator")){

				}
				else if(key.equals("Location")){
					fileURL=(String)(songDict.get(key));
				}
				else if(key.equals("File Folder Count")){

				}
				else if(key.equals("Library Folder Count")){
				}
				else{
					String title="";
					if(tag.getTitle()!=null){
						title=tag.getTitle();
					}
					System.out.println("Unhandled Key in song dictionary:"+key+" for Song:"+title+" id:"+trackid);
				}


			}
			// now we need to create the fileitem entry
			if(trackType.equals("File")  && fileURL!=null && format!=null) // if its a file and we have a format to decode it
			{ 
				try{

					// remove first instance of localhost/ from sting
					// string hackery
					String newfileURL=fileURL.subSequence(0,7).toString()+fileURL.substring(16);

					URI uri=new URI(newfileURL);

					File file=new File(uri);
					//System.out.println(file.toString());
					if(!file.exists()){
						System.out.println("File doesnt exists:" +fileURL);
					}
					FileItem fileItem=new FileItem(file,formatHandler,format,tag);
					song.setFileItem(fileItem);

					songs.put(trackid,song);


				}
				catch(Exception e){					
					e.printStackTrace();
				}

			}
		}
	}

	private void getPlaylistsFromDictionary(ArrayList playlistArray,FormatHandler formatHandler){		
		//System.out.println("Reading playlists:"+playlistArray.size()+" "+playlistArray.getClass().toString());
		for(Object nextobj:playlistArray){
			long lastPlaylistID=0;
			try // try block so that if the playlist fails to work then it still carries on building the DB
			{	
				//System.out.println("Reading playlist");
				Playlist playlist=new Playlist();
				//System.out.println(nextobj.toString());

				HashMap<String,Object> playlistDict=(HashMap<String,Object>)nextobj;									


				for(String key:playlistDict.keySet()){
					//System.out.println(key);
					if(key.equals("Name")){					
						playlist.setName((String)(playlistDict.get(key)));
						//System.out.println("Playlist Name:"+playlist.getName());
					}																
					else if(key.equals("File Folder Count")){

					}
					else if(key.equals("Playlist ID")){
						lastPlaylistID=(Long)playlistDict.get(key);
						System.out.println("Got Playlist ID:"+lastPlaylistID);
					}
					else if(key.equals("Playlist Persistent ID")){

					}
					else if(key.equals("Smart Info")){

					}
					else if(key.equals("Smart Criteria")){

					}
					else if(key.equals("Playlist Items")){
						//System.out.println("Playlist items being read");
						ArrayList playlistSongs=new ArrayList();
						ArrayList dicts=(ArrayList)(playlistDict.get(key));
						for(Object dictobj:dicts){
							//System.out.println("Reading dictionary");
							HashMap<String,Object> dict=(HashMap<String,Object>)dictobj;
							for(String akey:dict.keySet()){
								if(akey.equals("Track ID")){
									Song dsong;
									if((dsong=songs.get(((Long)(dict.get(akey))).intValue()))!=null)
									{
										playlistSongs.add(dsong);
										//System.out.println("Adding song:"+(((Long)(dict.get(akey))))+" to Playlist: "+playlist.getName());
										//System.out.println("Adding song:"+songs.get(((Long)(dict.get(akey))).intValue()).getTag().getTitle()+" to Playlist: "+playlist.getName());
									}

								}
							}

						}

						playlist.setSongs(playlistSongs);
					}
					else{
						System.out.println("Unhandled Key in playlist dictionary:"+key);
					}


				}


				playlists.add(playlist);
								
				for(Media song:playlist.getSongs()){
					System.out.println("Adding song:"+((Song)song).getTitle()+" to Playlist:"+playlist.getName());
				}
				
			}
			catch(Exception e)
			{
				System.out.println("Error in a playlist:"+lastPlaylistID);
			}
			// now we need to create the fileitem entry

		}
	}





	public void readiTunesXML(File file,FormatHandler formatHandler){


		Object plist=PlistReader.getPlistFromFile(file);
		//System.out.println("Read plist"+plist.getClass().toString());


		if(plist instanceof HashMap){
			getSongsFromTrackDictionary((HashMap<String,Object>)((HashMap<String,Object>)plist).get("Tracks"),formatHandler);		
			getPlaylistsFromDictionary((ArrayList)((HashMap<String,Object>)plist).get("Playlists"),formatHandler);
			System.out.println("Got:"+songs.size()+" songs and "+playlists.size()+" playlists");
		}
		else{
			System.out.println("Error reading Xml file");
		}

		//return result;
	}

}
