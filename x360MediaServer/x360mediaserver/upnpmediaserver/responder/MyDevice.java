package x360mediaserver.upnpmediaserver.responder;

import java.io.File;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.ServiceList;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.xml.Node;

public class MyDevice extends Device
{
    public MyDevice(Node root, Node device)
    {
        super(root, device);
    }

    public MyDevice()
    {
    }

    public MyDevice(Node device)
    {
        super(device);
    }

    public MyDevice(File descriptionFile) throws InvalidDescriptionException
    {
        super(descriptionFile);
    }

    public MyDevice(String descriptionFileName) throws InvalidDescriptionException
    {
        super(descriptionFileName);
    }

    
    public MyServiceList getServiceList()
    {
        MyServiceList serviceList = new MyServiceList();
        Node serviceListNode = getDeviceNode().getNode(ServiceList.ELEM_NAME);
        if (serviceListNode == null)
            return serviceList;
        int nNode = serviceListNode.getNNodes();
        for (int n = 0; n < nNode; n++ )
        {
            Node node = serviceListNode.getNode(n);
            if (MyService.isServiceNode(node) == false)
                continue;
            MyService service = new MyService(node);
            serviceList.add(service);
        }
        return serviceList;
    }
}
