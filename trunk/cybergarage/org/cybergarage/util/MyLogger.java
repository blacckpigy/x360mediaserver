package org.cybergarage.util;

import java.util.logging.*;
import java.util.Properties;
import java.io.IOException;
import java.text.MessageFormat;

public class MyLogger
{
    static final String LOG_FILE        = "clinkjava.log";
    protected Logger logger = null;
    private static Level DEFAULT_LEVEL  = Level.INFO;

    public static final int TRACE     = 0;
    public static final int DEBUG     = 1;
    public static final int INFO      = 2;
    public static final int WARN      = 3;
    public static final int ERROR     = 4;
    public static final int FATAL     = 5;
    public static final int ALL       = 6;

    private static void init_logger_jdk14()
    {
        try {
            FileHandler fh = new FileHandler("%h/" + LOG_FILE);
            fh.setFormatter(new SimpleFormatter());
            Logger rootLogger = Logger.getLogger( "" );
            rootLogger.addHandler(fh);
            rootLogger.setLevel(DEFAULT_LEVEL);
            //rootLogger.info("Log file is {0}/{1}", System.getProperty("user.home"), LOG_FILE);
        } catch (IOException e) {
            System.err.println("Unable to initialize logging to file");
        }
    }

    static {
        init_logger_jdk14();
        Properties sysProps = System.getProperties();
        int lev = WARN;
        try {
            String debugLevel = sysProps.getProperty("debug", String.valueOf(WARN));
             lev = Integer.parseInt(debugLevel);

        } catch (Exception e) {
            System.err.println("Unable to parse debug property: " + e);
        }
        setLevel(lev);
    }

    public MyLogger(Class clazz) {
        this(clazz.getName());
    }

    public MyLogger(String name) {
        logger = Logger.getLogger(name);
    }


    public static void setLevel(int lev)
    {
        switch (lev) {
            case DEBUG:
                DEFAULT_LEVEL = Level.FINE;
                break;
            case ALL:
            case TRACE:
                DEFAULT_LEVEL = Level.FINEST;
                break;
            case INFO:
                DEFAULT_LEVEL = Level.INFO;
                break;
            case WARN:
                DEFAULT_LEVEL = Level.WARNING;
                break;
            case ERROR:
            case FATAL:
                DEFAULT_LEVEL = Level.SEVERE;
                break;
        }
        System.out.println("Setting level: " + DEFAULT_LEVEL);
        Logger.getLogger( "" ).setLevel(DEFAULT_LEVEL);
    }

    private void log(Level level, Object msg)
    {
        if (logger.isLoggable(level)) {
            Throwable dummyException = new Throwable();
            StackTraceElement locations[] = dummyException.getStackTrace();
            // Caller will be the third element
            String cname = "unknown";
            String method = "unknown";
            if (locations != null && locations.length > 2) {
                StackTraceElement caller = locations[2];
                cname = caller.getClassName();
                method = caller.getMethodName();
            }

            try {
                MessageFormat mf = new MessageFormat("{0}");
                logger.logp(level, cname, method, mf.format(new Object[]{msg}));
            } catch (IllegalArgumentException iae) {
                logger.logp(level, cname, method, "Bad args: " + msg);
            }
        }
    }

    private void log(Level level, String format, Object args[])
    {
        if (logger.isLoggable(level)) {
            // Hack (?) to get the stack trace.
            Throwable dummyException = new Throwable();
            StackTraceElement locations[] = dummyException.getStackTrace();
            // Caller will be the third element
            String cname = "unknown";
            String method = "unknown";
            if (locations != null && locations.length > 2) {
                StackTraceElement caller = locations[2];
                cname = caller.getClassName();
                method = caller.getMethodName();
            }

            try {
                MessageFormat mf = new MessageFormat(format);
                logger.logp(level, cname, method, mf.format(args));
            } catch (IllegalArgumentException iae) {
                int i;
                StringBuffer sb = new StringBuffer();
                sb.append("(bad format, fixed) ");
                for (i=0; i< args.length; i++) {
                    sb.append("{");
                    sb.append(i);
                    sb.append("} ");
                }
                MessageFormat mf = new MessageFormat(sb.toString());
                logger.logp(level, cname, method, mf.format(args));
            }
        }
    }



    public void debug(String format, Object msg1)
    {
        log(Level.FINE, format, new Object[]{msg1});
    }


    public void debug(String format, Object msg1, Object msg2)
    {
        log(Level.FINE, format, new Object[]{msg1, msg2});
    }


    public void debug(Object message)
    {
        log(Level.FINE, message);
    }

    public void error(Object msg1)
    {
        log(Level.SEVERE, msg1);
    }

    public void error(Object msg1, Object msg2)
    {
        log(Level.SEVERE, "{0} ({1})", new Object[] {msg1, msg2});
    }

    public void fatal(Object msg1)
    {
        log(Level.SEVERE, msg1);
    }

    public void fatal(Object msg1, Object msg2)
    {
        log(Level.SEVERE, "{0} ({1})", new Object[]{msg1, msg2});
    }

    public Logger getLogger()
    {
        return (this.logger);
    }

    public void info(Object message)
    {
        log(Level.INFO, message);
    }

    public void info(String format, Object msg1, Object msg2)
    {
        log(Level.INFO, format, new Object[]{msg1, msg2});
    }

    public void info(String format, Object msg1)
    {
        log(Level.INFO, format, new Object[]{msg1});
    }

    public boolean isDebugEnabled()
    {
        return (logger.isLoggable(Level.FINE));
    }

    public boolean isErrorEnabled()
    {
        return (logger.isLoggable(Level.SEVERE));
    }

    public boolean isFatalEnabled()
    {
        return (logger.isLoggable(Level.SEVERE));
    }

    public boolean isInfoEnabled()
    {
        return (logger.isLoggable(Level.INFO));
    }

    public boolean isTraceEnabled()
    {
        return (logger.isLoggable(Level.FINEST));
    }

    public boolean isWarnEnabled()
    {
        return (logger.isLoggable(Level.WARNING));
    }

    public void trace(Object message)
    {
        log(Level.FINEST, message);
    }

    public void trace(Object msg1, Object msg2)
    {
        log(Level.FINEST, "{0} ({1})", new Object[]{msg1, msg2});
    }

    public void trace(String format, Object[] args)
    {
        log(Level.FINEST, format, args);
    }

    public void warn(String format, Object[] args)
    {
        log(Level.WARNING, format, args);
    }

    public void warn(Object message)
    {
        log(Level.WARNING, message);
    }

    public void warn(Object msg1, Object msg2)
    {
        log(Level.WARNING, "{0} ({1})", new Object[]{msg1, msg2});
    }

    public int getLoglevel()
    {
        return logger.getParent().getLevel().intValue();
    }

    public String getLoglevelName()
    {
        return logger.getParent().getLevel().getName();
    }

    public void setLogLevel(int level)
    {
        logger.getParent().setLevel(Level.parse(level + ""));
    }

}
