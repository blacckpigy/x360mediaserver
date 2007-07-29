package x360mediaserver.upnpmediaserver.upnp;

import java.io.IOException;
import java.io.OutputStream;

import org.cybergarage.http.HTTPResponse;
import org.cybergarage.http.HTTPSocket;

public class StreamingHttpResponse extends HTTPResponse
{
    // //////////////////////////////////////////////
    // Socket
    // //////////////////////////////////////////////

    private HTTPSocket httpSocket = null;

    public StreamingHttpResponse(HTTPSocket socket)
    {
        super();
        setSocket(socket);
    }

    public void setSocket(HTTPSocket value)
    {
        httpSocket = value;
    }

    public HTTPSocket getSocket()
    {
        return httpSocket;
    }

    public OutputStream getContentOutputStream()
    {
        try
        {
            return getSocket().getSocket().getOutputStream();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.cybergarage.http.HTTPPacket#set(org.cybergarage.http.HTTPSocket)
     */
    @Override
    protected boolean set(HTTPSocket httpSock)
    {
        // TODO Auto-generated method stub
        return super.set(httpSock);
    }
}
