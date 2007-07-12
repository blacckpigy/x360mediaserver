package x360mediaserver.service;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Toaster extends Shell
{
    private Display display;
    private boolean blockExecutionOnOpen = true;

    public Toaster(Shell shell, ToasterOptions options)
    {
        super(shell, SWT.APPLICATION_MODAL | SWT.ON_TOP);
        setText(options.title);
        setSize(options.width, options.height);

        this.blockExecutionOnOpen = options.blockExecutionOnOpen;

        display = shell.getDisplay();

        // this lets the escape key close this window.
        addListener(SWT.Traverse, new Listener()
        {
            public void handleEvent(Event event)
            {
                switch (event.detail)
                {
                    case SWT.TRAVERSE_ESCAPE:
                        close();
                        event.detail = SWT.TRAVERSE_NONE;
                        event.doit = false;
                        break;
                }
            }
        });

        Rectangle size = display.getClientArea();
        int displayWidth = size.width;
        if (display.getCursorLocation().x < (size.width / 2))
            displayWidth = 0;

        int xCoord = 30;
        if (display.getCursorLocation().y > (size.height / 2))
            xCoord = size.height - getSize().y;

        setLocation(displayWidth, xCoord);

        addDisposeListener(new DisposeListener()
        {
            public void widgetDisposed(DisposeEvent event)
            {
                slide(true);
            }

        });
    }

    // this is so i can override the open() method.
    @Override
    public void checkSubclass()
    {}

    // this is the only way i can force the shell to animate when it opens.
    // i couldn't figure out how to do it with listeners.
    /**
     * Moves the receiver to the top of the drawing order for the display on which it was created
     * (so that all other shells on that display, which are not the receiver's children will be
     * drawn behind it), marks it visible, sets the focus and asks the window manager to make the
     * shell active.
     * 
     * @exception SWTException
     *                <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created
     *                the receiver</li>
     *                </ul>
     * @see Control#moveAbove
     * @see Control#setFocus
     * @see Control#setVisible
     * @see Display#getActiveShell
     * @see Decorations#setDefaultButton
     * @see Shell#setActive
     * @see Shell#forceActive
     */
    @Override
    public void open()
    {
        super.open();
        slide(false);

        // this will wait until we make a decision until passing control back to the thing that
        // created this window..
        if (blockExecutionOnOpen)
            while ( !isDisposed())
                if ( !display.readAndDispatch())
                    display.sleep();
    }

    /**
     * allows the window to be animated.
     * 
     * @param reverse
     *            if false, it goes right->left. if true, it goes left->right
     */
    public void slide(boolean reverse)
    {
        slide(reverse, 0);
    }

    /**
     * allows the window to be animated.
     * 
     * @param reverse
     *            if false, it goes right->left. if true, it goes left->right
     * @param speed
     *            how much time to wait between "frames" in the sliding animation.
     */
    public void slide(boolean reverse, final int speed)
    {
        final int rate = 2;
        final int direction = reverse ? rate : -rate;

        display.syncExec(new Runnable()
        {
            public void run()
            {
                for (int i = 0; i < getClientArea().width / rate; i++ )
                {
                    setLocation(getLocation().x + direction, getLocation().y);
                    if (speed > 0)
                    {
                        try
                        {
                            Thread.sleep(speed);
                        }
                        catch (InterruptedException e)
                        {
                        }
                    }
                    update();
                }
            }
        });
    }
}
