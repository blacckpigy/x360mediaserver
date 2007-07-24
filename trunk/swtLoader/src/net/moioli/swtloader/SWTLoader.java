/**
 * SWT Autoloader - everything in a jar 
 * 
 * Copyright (C) 2005 by Silvio Moioli, Nathan Robinson *
 * silvio@moioli.net, nathanrobinson@gmail.com *
 * A brief tutorial on how to use this class is provided in the readme file included in 
 * the distribution package. Use of this software is subject to the terms in the LICENSE.txt file
 **/
package net.moioli.swtloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Enables SWT auto-loading from the jar file. A brief tutorial on how to use this class is provided
 * in the readme file included in the distribution package.
 * 
 * @author Silvio Moioli, Nathan Robinson
 * @version 1.0
 */
public class SWTLoader
{

    public static void main(String... args)
    {
        new SWTLoader();
    }

    /**
     * Constructs a new loader.
     */
    public SWTLoader()
    {
        // System.err.println("Starting...");
        try
        {
            String hostFileString = this.getClass()
                                        .getProtectionDomain()
                                        .getCodeSource()
                                        .getLocation()
                                        .getFile();

            String substring = hostFileString.substring(0, hostFileString.lastIndexOf("/"));

            // make the correct temp directory.
            substring += "/.tmp";
            new File(substring).mkdirs();

            File hostFile = new File(URLDecoder.decode(hostFileString, "UTF-8")).getAbsoluteFile();
            File libDir = new File(URLDecoder.decode(substring, "UTF-8")).getAbsoluteFile();

            // System.err.println("HOST: " + hostFile);
            // System.out.println("JAR : " + jarFile);

            load(hostFile, libDir);
        }
        catch (LoadingException e)
        {
            System.out.println("Error while loading SWT. Traceback: ");
            e.printStackTrace();
            System.exit(1);
        }
        catch (UnsupportedEncodingException e)
        {
            // cant get here..
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Loads SWT (if it's not loaded already).
     * 
     * @param hostFile
     * @param libDir
     * @throws LoadingException
     * @throws IOException
     * @throws InterruptedException
     */
    private void load(File hostFile, File libDir) throws LoadingException, IOException,
            InterruptedException
    {
        if (this.isInstalled() == false)
        {
            this.extract(hostFile, libDir);

            // FileOutputStream fos = new FileOutputStream("TEST");
            String[] command = {"java", "-Djava.library.path=" + libDir, "-jar",
                    hostFile.toString()};
            Process proc = Runtime.getRuntime().exec(command);

            // any error message?
            // StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");

            // any output?
            // StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT",
            // fos);

            // kick them off
            // errorGobbler.start();
            // outputGobbler.start();

            // any error???
            // int exitVal = proc.waitFor();
            // System.out.println("ExitValue: " + exitVal);
            // fos.flush();
            // fos.close();
            System.exit(0);
        }
    }

    /**
     * Extracts the SWT native files in a temporary directory.
     */
    private void extract(File jarFile, File libDir) throws LoadingException
    {
        // if we have previously extracted... no need to do it again.
        if (libDir != null && libDir.list().length > 0)
            return;

        try
        {
            ZipFile zipJarFile = new ZipFile(jarFile);
            Enumeration jarEntries = zipJarFile.entries();
            while (jarEntries.hasMoreElements())
            {
                ZipEntry jarEntry = (ZipEntry) jarEntries.nextElement();
                String Filename = jarEntry.getName()
                                          .substring(jarEntry.getName().lastIndexOf("/") + 1);
                // System.err.println("LOOKING: " + pathname);
                if (Filename.startsWith("swt-") && Filename.endsWith(".zip"))
                {
                    InputStream fileInZip = zipJarFile.getInputStream(jarEntry);
                    File insideZipFileSaved = saveFile(fileInZip, Filename, libDir);

                    // System.out.println("BLARG:" + insideZipFileSaved.getAbsolutePath());
                    ZipFile internalZipFile = new ZipFile(insideZipFileSaved);
                    Enumeration internalZipEntries = internalZipFile.entries();
                    while (internalZipEntries.hasMoreElements())
                    {
                        ZipEntry internalEntry = (ZipEntry) internalZipEntries.nextElement();
                        String internalFilename = internalEntry.getName()
                                                               .substring(internalEntry.getName()
                                                                                       .lastIndexOf("/") + 1);

                        // System.err.println("LOOKING INSIDE: " + pathname);
                        if (internalFilename.endsWith(".so") || internalFilename.endsWith(".dll") ||
                            internalFilename.endsWith(".jnilib"))
                        {
                            InputStream fileWayInZip = internalZipFile.getInputStream(internalEntry);
                            saveFile(fileWayInZip, internalFilename, libDir);
                        }
                    }
                    internalZipFile.close();
                    insideZipFileSaved.delete(); // no need to hang on to this.
                    break;
                }
            }
            zipJarFile.close();
        }
        catch (ZipException e)
        {
            throw new LoadingException(e);
        }
        catch (IOException e)
        {
            throw new LoadingException(e);
        }
        catch (IllegalStateException e)
        {
            throw new LoadingException(e);
        }
    }

    private File saveFile(InputStream in, String fileName, File libDir) throws IOException,
            FileNotFoundException, LoadingException
    {
        // System.err.println("SAVING!!): " + pathname);
        // System.err.println("SWT!!): " + libDir);
        File outFile = new File(libDir, fileName);
        FileOutputStream out = new FileOutputStream(outFile);
        byte[] buf = new byte[1024];
        while (true)
        {
            int nRead = in.read(buf, 0, buf.length);
            if (nRead <= 0)
                break;
            out.write(buf, 0, nRead);
        }
        in.close();
        out.close();
        return outFile;
    }

    /**
     * Checks if SWT isn't already loaded.
     * 
     * @return true if SWT is loaded.
     */
    private boolean isInstalled()
    {
        // System.err.println("PATH: " + System.getProperty("java.library.path"));
        try
        {
            // see if the class can load...
            Class.forName("org.eclipse.swt.widgets.Display");
        }
        catch (ClassNotFoundException e)
        {
            // System.err.println("ClassNotFoundException");
            return false;
        }
        catch (NoClassDefFoundError e)
        {
            // System.err.println("NoClassDefFoundError");
            return false;
        }
        catch (UnsatisfiedLinkError e)
        {
            // System.err.println("UnsatisfiedLinkError");
            return false;
        }
        // System.out.println("SWT loaded successfully.");
        return true;
    }
}

class StreamGobbler extends Thread
{
    InputStream  is;
    String       type;
    OutputStream os;

    StreamGobbler(InputStream is, String type)
    {
        this(is, type, null);
    }

    StreamGobbler(InputStream is, String type, OutputStream redirect)
    {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }

    public void run()
    {
        try
        {
            PrintWriter pw = null;
            if (os != null)
                pw = new PrintWriter(os);

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null)
            {
                if (pw != null)
                    pw.println(line);
                System.out.println(type + ">" + line);
            }
            if (pw != null)
                pw.flush();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}