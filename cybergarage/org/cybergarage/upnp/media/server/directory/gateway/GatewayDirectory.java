/******************************************************************
 *
 *	MediaServer for CyberLink
 *
 *	Copyright (C) Satoshi Konno 2004
 *
 *	File : FileDirectory
 *
 *	Revision:
 *
 *	09/15/05
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.upnp.media.server.directory.gateway;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;
import org.cybergarage.upnp.media.player.MediaPlayer;
import org.cybergarage.upnp.media.server.Directory;
import org.cybergarage.upnp.media.server.MediaServer;
import org.cybergarage.upnp.media.server.object.ContentNode;
import org.cybergarage.upnp.media.server.object.container.ContainerNode;
import org.cybergarage.util.MyLogger;

public class GatewayDirectory extends Directory
{
    private static MyLogger     log  = new MyLogger(GatewayDirectory.class);
    // //////////////////////////////////////////////
    // Constants
    // //////////////////////////////////////////////

    private final static String NAME = "CyberMediaGate";

    // //////////////////////////////////////////////
    // Constructor
    // //////////////////////////////////////////////

    public GatewayDirectory(String name)
    {
        super(name);

        MediaPlayer mplayer = getMediaPlayer();
        mplayer.start();
    }

    public GatewayDirectory()
    {
        this(NAME);
    }

    // //////////////////////////////////////////////
    // MediaPlayer
    // //////////////////////////////////////////////

    private MediaPlayer mplayer = new MediaPlayer();

    public MediaPlayer getMediaPlayer()
    {
        return mplayer;
    }

/*
 * //////////////////////////////////////////////// // Path
 * //////////////////////////////////////////////// private String path; public void setPath(String
 * value) { path = value; } public String getPath() { return path; }
 * //////////////////////////////////////////////// // create/updateItemNode
 * //////////////////////////////////////////////// private boolean updateItemNode(FileItemNode
 * itemNode, File file) { Format format = getContentDirectory().getFormat(file); if (format == null)
 * return false; FormatObject formatObj = format.createObject(file); // File/TimeStamp
 * itemNode.setFile(file); // Title String title = formatObj.getTitle(); if (0 < title.length())
 * itemNode.setTitle(title); // Creator String creator = formatObj.getCreator(); if (0 <
 * creator.length()) itemNode.setCreator(creator); // Media Class String mediaClass =
 * format.getMediaClass(); if (0 < mediaClass.length()) itemNode.setUPnPClass(mediaClass); // Date
 * long lastModTime = file.lastModified(); itemNode.setDate(lastModTime); // Storatge Used try {
 * long fileSize = file.length(); itemNode.setStorageUsed(fileSize); } catch (Exception e) {
 * log.warn(e); } // ProtocolInfo String mimeType = format.getMimeType(); String protocol =
 * ConnectionManager.HTTP_GET + ":*:" + mimeType + ":*"; String id = itemNode.getID(); String url =
 * getContentDirectory().getContentExportURL(id); AttributeList objAttrList =
 * formatObj.getAttributeList(); itemNode.setResource(url, protocol, objAttrList); // Update
 * SystemUpdateID getContentDirectory().updateSystemUpdateID(); return true; } private FileItemNode
 * createCompareItemNode(File file) { Format format = getContentDirectory().getFormat(file); if
 * (format == null) return null; FileItemNode itemNode = new FileItemNode(); itemNode.setFile(file);
 * return itemNode; } //////////////////////////////////////////////// // FileList
 * //////////////////////////////////////////////// private int getDirectoryItemNodeList(File
 * dirFile, FileItemNodeList itemNodeList) { File childFile[] = dirFile.listFiles(); int fileCnt =
 * childFile.length; for (int n=0; n<fileCnt; n++) { File file = childFile[n]; if
 * (file.isDirectory() == true) { getDirectoryItemNodeList(file, itemNodeList); continue; } if
 * (file.isFile() == true) { FileItemNode itemNode = createCompareItemNode(file); if (itemNode ==
 * null) continue; itemNodeList.add(itemNode); } } return itemNodeList.size(); } private
 * FileItemNodeList getCurrentDirectoryItemNodeList() { FileItemNodeList itemNodeList = new
 * FileItemNodeList(); String path = getPath(); File pathFile = new File(path);
 * getDirectoryItemNodeList(pathFile, itemNodeList); return itemNodeList; }
 * //////////////////////////////////////////////// // updateItemNodeList
 * //////////////////////////////////////////////// private FileItemNode getItemNode(File file) {
 * int nContents = getNContentNodes(); for (int n=0; n<nContents; n++) { ContentNode cnode =
 * getContentNode(n); if ((cnode instanceof FileItemNode) == false) continue; FileItemNode itemNode =
 * (FileItemNode)cnode; if (itemNode.equals(file) == true) return itemNode; } return null; } private
 * void addItemNode(FileItemNode itemNode) { addContentNode(itemNode); } private boolean
 * updateItemNodeList(FileItemNode newItemNode) { File newItemNodeFile = newItemNode.getFile();
 * FileItemNode currItemNode = getItemNode(newItemNodeFile); if (currItemNode == null) { int
 * newItemID = getContentDirectory().getNextItemID(); newItemNode.setID(newItemID);
 * updateItemNode(newItemNode, newItemNodeFile); addItemNode(newItemNode); return true; } long
 * currTimeStamp = currItemNode.getFileTimeStamp(); long newTimeStamp =
 * newItemNode.getFileTimeStamp(); if (currTimeStamp == newTimeStamp) return false;
 * updateItemNode(currItemNode, newItemNodeFile); return true; } private void updateItemNodeList() {
 * boolean updateFlag = false; // Checking Deleted Items int nContents = getNContentNodes();
 * ContentNode cnode[] = new ContentNode[nContents]; for (int n=0; n<nContents; n++) cnode[n] =
 * getContentNode(n); for (int n=0; n<nContents; n++) { if ((cnode[n] instanceof FileItemNode) ==
 * false) continue; FileItemNode itemNode = (FileItemNode)cnode[n]; File itemFile =
 * itemNode.getFile(); if (itemFile == null) continue; if (itemFile.exists() == false) {
 * removeContentNode(cnode[n]); updateFlag = true; } } // Checking Added or Updated Items
 * FileItemNodeList itemNodeList = getCurrentDirectoryItemNodeList(); int itemNodeCnt =
 * itemNodeList.size(); for (int n=0; n<itemNodeCnt; n++) { FileItemNode itemNode =
 * itemNodeList.getFileItemNode(n); if (updateItemNodeList(itemNode) == true) updateFlag = true; }
 * nContents = getNContentNodes(); setChildCount(nContents); if (updateFlag == true)
 * getContentDirectory().updateSystemUpdateID(); }
 */

    // //////////////////////////////////////////////
    // update
    // //////////////////////////////////////////////
    private boolean updateMediaServerList()
    {
        boolean updateFlag = false;

        MediaPlayer mplayer = getMediaPlayer();

        DeviceList devList = mplayer.getDeviceList();
        int devCnt = devList.size();
        for (int n = 0; n < devCnt; n++ )
        {
            Device dev = devList.getDevice(n);
            if ( !dev.isDeviceType(MediaServer.DEVICE_TYPE))
                continue;
            if (log.isDebugEnabled())
            {
                log.debug("[{0}]  {1} {2} {3}", new Object[] {String.valueOf(n),
                        dev.getFriendlyName(), String.valueOf(dev.getLeaseTime()),
                        String.valueOf(dev.getElapsedTime())});
            }
        }

        int nContents = getNContentNodes();
        ContentNode cnode[] = new ContentNode[nContents];
        for (int n = 0; n < nContents; n++ )
            cnode[n] = getContentNode(n);
        for (int n = 0; n < nContents; n++ )
        {
            String containerName = cnode[n].getName();
            if ( !mplayer.hasDevice(containerName))
            {
                ContainerNode mserverNode = new ContainerNode();
                mserverNode.setName(containerName);
                addContentNode(mserverNode);
            }
        }

        return updateFlag;
    }

    // //////////////////////////////////////////////
    // update
    // //////////////////////////////////////////////

    public boolean update()
    {
        return updateMediaServerList();
    }
}
