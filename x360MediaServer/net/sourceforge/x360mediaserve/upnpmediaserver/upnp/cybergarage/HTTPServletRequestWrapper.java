/**
 * one line to give the program's name and an idea of what it does. Copyright (C) 2006 Thomas Walker
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should
 * have received a copy of the GNU General Public License along with this program; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package net.sourceforge.x360mediaserve.upnpmediaserver.upnp.cybergarage;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.x360mediaserve.Config;

import org.cybergarage.http.HTTPHeader;
import org.cybergarage.upnp.control.ActionRequest;

public class HTTPServletRequestWrapper
{
    private byte[] content;
    Vector         httpHeaders;
    String         interfaceString;

    public HTTPServletRequestWrapper(HttpServletRequest req)
    {
        Config.out(req);
        try
        {
            httpHeaders = new Vector();

            InputStream inputstream = req.getInputStream();
            content = new byte[req.getContentLength()];
            for (int i = 0; i < req.getContentLength(); i++ )
            {
                content[i] = (byte) inputstream.read();
            }

            Enumeration headernames = req.getHeaderNames();

            while (headernames.hasMoreElements())
            {
                String headername = (String) headernames.nextElement();
                httpHeaders.add(new HTTPHeader(headername, req.getHeader(headername)));
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // hopefully this is enough to make the action request work
    public ActionRequest getActionRequest()
    {
        ActionRequest result = new ActionRequest();
        result.setContent(content);

        Iterator i = httpHeaders.iterator();

        while (i.hasNext())
        {
            result.addHeader((HTTPHeader) i.next());
        }

        return result;
    }

    private void processcontent()
    {
    }
}
