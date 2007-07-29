package x360mediaserver.upnpmediaserver.responder;

import java.io.File;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;
import org.cybergarage.upnp.ServiceList;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.upnp.xml.DeviceData;
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
    
 // //////////////////////////////////////////////
    // deviceList
    // //////////////////////////////////////////////

    public MyDeviceList getDeviceList()
    {
        MyDeviceList devList = new MyDeviceList();
        Node devListNode = getDeviceNode().getNode(DeviceList.ELEM_NAME);
        if (devListNode == null)
            return devList;
        int nNode = devListNode.getNNodes();
        for (int n = 0; n < nNode; n++ )
        {
            Node node = devListNode.getNode(n);
            if (Device.isDeviceNode(node) == false)
                continue;
            MyDevice dev = new MyDevice(node);
            devList.add(dev);
        }
        return devList;
    }
    
    public MyDevice getDevice(String name)
    {
        MyDeviceList devList = getDeviceList();
        int devCnt = devList.size();
        for (int n = 0; n < devCnt; n++ )
        {
            MyDevice dev = devList.getDevice(n);
            if (dev.isDevice(name) == true)
                return dev;
            MyDevice cdev = dev.getDevice(name);
            if (cdev != null)
                return cdev;
        }
        return null;
    }

    public MyDevice getDeviceByDescriptionURI(String uri)
    {
        MyDeviceList devList = getDeviceList();
        int devCnt = devList.size();
        for (int n = 0; n < devCnt; n++ )
        {
            MyDevice dev = devList.getDevice(n);
            if (dev.isDescriptionURI(uri) == true)
                return dev;
            MyDevice cdev = dev.getDeviceByDescriptionURI(uri);
            if (cdev != null)
                return cdev;
        }
        return null;
    }
    
    private boolean isDescriptionURI(String uri)
    {
        String descriptionURI = getDescriptionURI();
        if (uri == null || descriptionURI == null)
            return false;
        return descriptionURI.equals(uri);
    }
    
    private String getDescriptionURI()
    {
        return getDeviceData().getDescriptionURI();
    }
    
 // //////////////////////////////////////////////
    // UserData
    // //////////////////////////////////////////////

    private DeviceData getDeviceData()
    {
        Node node = getDeviceNode();
        DeviceData userData = (DeviceData) node.getUserData();
        if (userData == null)
        {
            userData = new DeviceData();
            node.setUserData(userData);
            userData.setNode(node);
        }
        return userData;
    }
}
