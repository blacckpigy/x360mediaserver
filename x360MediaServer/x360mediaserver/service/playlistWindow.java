package x360mediaserver.service;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class playlistWindow
{
    private static final String ADDRESS_CHANGE_WINDOW = "Address Change Window";
    private static final String CANCEL                = "Cancel";
    private static final String ACCEPT                = "Accept";
    private static final String SETUP_NETWORK_ADDRESS = "This is where you want to specify the correct network address of your computer. "
                                                        + "This is only necessary if x360MediaServe doesn't recognize your network address.";

    private Text                nicAddressText;

    public playlistWindow(Shell shell)
    {
        Display display = shell.getDisplay();
        Color white = display.getSystemColor(SWT.COLOR_WHITE);

        final Shell parent = new Shell(shell, SWT.APPLICATION_MODAL | SWT.ON_TOP);
        // this lets the escape key close this window.
        parent.addListener(SWT.Traverse, new Listener()
        {
            public void handleEvent(Event event)
            {
                switch (event.detail)
                {
                    case SWT.TRAVERSE_ESCAPE:
                        parent.close();
                        event.detail = SWT.TRAVERSE_NONE;
                        event.doit = false;
                        break;
                }
            }
        });

        parent.setText(ADDRESS_CHANGE_WINDOW);
        parent.setSize(300, 180);

        Rectangle size = display.getClientArea();
        // This positions the window near the system tray, so it is easier to find.
        int width = size.width - parent.getSize().x;
        if (display.getCursorLocation().y > size.height / 2)
            parent.setLocation(width, display.getCursorLocation().y);
        else
            parent.setLocation(width, size.height - parent.getSize().y + 20);

        FillLayout fillLayout = new FillLayout();
        fillLayout.type = SWT.VERTICAL;
        parent.setLayout(fillLayout);

        /*
         * TOP area.
         */
        Composite top = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(1, true);
        top.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        top.setBackground(white);
        top.setLayout(layout);

        Label topLabel = new Label(top, SWT.FILL | SWT.WRAP);
        topLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
        topLabel.setBackground(white);
        topLabel.setText(SETUP_NETWORK_ADDRESS);

        Label spacer;
        // Add spacer
        spacer = new Label(top, SWT.NONE);
        spacer.setBackground(white);
        spacer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label nicAddressLabel = new Label(top, SWT.NONE);
        nicAddressLabel.setText("Please enter your computers name or IP address:");
        nicAddressLabel.setBackground(white);
        nicAddressLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));

        nicAddressText = new Text(top, SWT.BORDER);
        nicAddressText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        /*
         * SETUP THE BUTTON GRID
         */
        Composite buttonGrid = new Composite(top, SWT.NONE);
        layout = new GridLayout(5, false);
        buttonGrid.setLayout(layout);
        buttonGrid.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
        buttonGrid.setBackground(white);

        Button acceptButton = new Button(buttonGrid, SWT.PUSH);
        acceptButton.setText(ACCEPT);
        acceptButton.setBackground(white);
        acceptButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                parent.close();
            }
        });

        Button cancelButton = new Button(buttonGrid, SWT.PUSH);
        cancelButton.setText(CANCEL);
        cancelButton.setBackground(white);
        cancelButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                nicAddressText.setText("");
                parent.close();
            }
        });

        parent.open();
    }

    public String getAddress()
    {
        return nicAddressText.getText();
    }
}
