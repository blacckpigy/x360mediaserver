/******************************************************************
 *
 *	CyberXML for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: Parser.java
 *
 *	Revision;
 *
 *	11/26/03
 *		- first revision.
 *	03/30/05
 *		- Change parse(String) to use StringBufferInputStream instead of URL.
 *
 ******************************************************************/

package org.cybergarage.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.cybergarage.http.HTTP;

public abstract class Parser
{
    // //////////////////////////////////////////////
    // Constructor
    // //////////////////////////////////////////////

    public Parser()
    {}

    // //////////////////////////////////////////////
    // parse
    // //////////////////////////////////////////////

    public abstract Node parse(InputStream inStream) throws ParserException;

    // //////////////////////////////////////////////
    // parse (URL)
    // //////////////////////////////////////////////

    public Node parse(URL locationURL) throws ParserException
    {
        try
        {
            HttpURLConnection urlCon = (HttpURLConnection) locationURL.openConnection();
            urlCon.setRequestMethod("GET");
            urlCon.setRequestProperty(HTTP.CONTENT_LENGTH, "0");
            InputStream urlIn = urlCon.getInputStream();

            Node rootElem = parse(urlIn);

            urlIn.close();
            urlCon.disconnect();

            return rootElem;

        }
        catch (Exception e)
        {
            throw new ParserException(e);
        }
        /*
         * String host = locationURL.getHost(); int port = locationURL.getPort(); String uri =
         * locationURL.getPath(); HTTPRequest httpReq = new HTTPRequest();
         * httpReq.setMethod(HTTP.GET); httpReq.setURI(uri); HTTPResponse httpRes =
         * httpReq.post(host, port); if (httpRes.isSuccessful() == false) throw new
         * ParserException(locationURL.toString()); String content = new
         * String(httpRes.getContent()); StringBufferInputStream strBuf = new
         * StringBufferInputStream(content); return parse(strBuf);
         */
    }

    // //////////////////////////////////////////////
    // parse (File)
    // //////////////////////////////////////////////

    public Node parse(File descriptionFile) throws ParserException
    {
        try
        {
            InputStream fileIn = new FileInputStream(descriptionFile);
            Node root = parse(fileIn);
            fileIn.close();
            return root;

        }
        catch (Exception e)
        {
            throw new ParserException(e);
        }
    }

    // //////////////////////////////////////////////
    // parse (Memory)
    // //////////////////////////////////////////////

    public Node parse(String descr) throws ParserException
    {
        try
        {
            ByteArrayInputStream decrIn = new ByteArrayInputStream(descr.getBytes());
            Node root = parse(decrIn);
            return root;
        }
        catch (Exception e)
        {
            throw new ParserException(e);
        }
    }

}
