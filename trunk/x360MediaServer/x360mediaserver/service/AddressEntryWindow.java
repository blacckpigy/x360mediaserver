package x360mediaserver.service;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
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

import x360mediaserver.Config;

public class AddressEntryWindow 
{
    private static final String COMPUTERS_NAME_OR_IP_ADDRESS = Messages.getString("AddressEntryWindow.0"); //$NON-NLS-1$
    private static final String ADDRESS_CHANGE_WINDOW        = Messages.getString("AddressEntryWindow.1"); //$NON-NLS-1$
    private static final String CANCEL                       = Messages.getString("AddressEntryWindow.2"); //$NON-NLS-1$
    private static final String ACCEPT                       = Messages.getString("AddressEntryWindow.3"); //$NON-NLS-1$
    private static final String SETUP_NETWORK_ADDRESS        = Messages.getString("AddressEntryWindow.4"); //$NON-NLS-1$

    private Text                nicAddressText, nicPortText;
    private Toaster             parent;
    private Text friendlyNameText;

    public AddressEntryWindow(Shell shell)
    {
        Display display = shell.getDisplay();
        parent = new Toaster(shell, new ToasterOptions(ADDRESS_CHANGE_WINDOW));
        
        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        
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

        // What do I want?
        // name
        // address : port
        // PCMOutput
        // MusicDir
        // iTunesFile
        // Streams
        
        Composite nameGrid = makeCompositeSection(white, top, 2);
        
        Label friendlyNameLabel = new Label(nameGrid, SWT.NONE);
        friendlyNameLabel.setText("Name : ");
        friendlyNameLabel.setBackground(white);
        friendlyNameLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        
        friendlyNameText = new Text(nameGrid, SWT.BORDER);
        friendlyNameText.setText(Config.getFriendlyName());
        friendlyNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        
        
        
        Label nicAddressLabel = new Label(top, SWT.NONE);
        nicAddressLabel.setText(COMPUTERS_NAME_OR_IP_ADDRESS);
        nicAddressLabel.setBackground(white);
        nicAddressLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));

        
        /*
         * SETUP THE ADDY:PORT GRID
         */
        Composite addyPortGrid = makeCompositeSection(white, top, 4);
        
        
        nicAddressText = new Text(addyPortGrid, SWT.BORDER);
        nicAddressText.setText(Config.getAddress());
        nicAddressText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        Label nicPortLabel = new Label(addyPortGrid, SWT.NONE);
        nicPortLabel.setText(":");
        nicPortLabel.setBackground(white);
        nicPortLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

        nicPortText = new Text(addyPortGrid, SWT.BORDER);
        nicPortText.setText(Config.getPort().toString());
        nicPortText.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        // Only allow digits here...
        nicPortText.addListener (SWT.Verify, new Listener () {
            public void handleEvent (Event e) {
                String string = e.text;
                char [] chars = new char [string.length ()];
                string.getChars (0, chars.length, chars, 0);
                for (int i=0; i<chars.length; i++) {
                    if (!('0' <= chars [i] && chars [i] <= '9')) {
                        e.doit = false;
                        return;
                    }
                }
            }
        });

        /*
         * SETUP THE BUTTON GRID
         */
        Composite buttonGrid = makeCompositeSection(white, top, 5);

        Button acceptButton = new Button(buttonGrid, SWT.PUSH);
        acceptButton.setText(ACCEPT);
        acceptButton.setBackground(white);
        acceptButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                Config.setAddress(nicAddressText.getText());
                Config.setPort(Integer.parseInt(nicPortText.getText()));
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
                parent.close();
            }
        });
       
        parent.open();
    }

    /**
     * Creates a section composite.
     * @param white
     * @param composite
     * @param gridNum
     * @return
     */
    private Composite makeCompositeSection(Color white, Composite composite, int gridNum)
    {
        Composite addyPortGrid = new Composite(composite, SWT.NONE);
        GridLayout layout = new GridLayout(gridNum, false);
        layout.marginWidth = 0;
        addyPortGrid.setLayout(layout);
        addyPortGrid.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        addyPortGrid.setBackground(white);
        
        return addyPortGrid;
    }
}
