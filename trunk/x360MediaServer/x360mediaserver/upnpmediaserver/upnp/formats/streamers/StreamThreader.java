package x360mediaserver.upnpmediaserver.upnp.formats.streamers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class StreamThreader implements Runnable
{

    private Thread thread = null;
    private final File file;
    private final OutputStream os;

    public StreamThreader(File file, OutputStream os)
    {
        this.file = file;
        this.os = os;
    }
    
    /** Copies a given file to the OutputStream
     * @param file
     * @param os
     */
    public void writeToStream(){
        BufferedInputStream is=null;
        try{                
            is=new BufferedInputStream(new FileInputStream(file));
            byte input[]=new byte[4096];
            int bytesread;
            while((bytesread=is.read(input))!=-1){
                
                os.write(input,0,bytesread);
            }
            
            
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        finally
        {
            if(is!=null) 
                try{
                    is.close();
                    stop();
                }
            catch(Exception e){
                
            }
        }
    }
    
    /**
     * Start the network data collecting agent.
     */
    public synchronized void start()
    {
        if (thread == null)
            thread = new Thread(this, "File Reader");
        thread.start();
    }

    /**
     * Stop the network data collecting agent.
     */
    public void stop()
    {
        if (thread != null)
            thread = null;
    }

    /**
     * This is the run method and runs if/when we connect to the server
     */
    public void run()
    {
        try
        {
            while (thread != null)
            {
                writeToStream();
            }
        }
        catch (OutOfMemoryError e)
        {
            System.out.println("ERROR: RAN OUT OF MEMORY");
            stop();
        }
    }

}
