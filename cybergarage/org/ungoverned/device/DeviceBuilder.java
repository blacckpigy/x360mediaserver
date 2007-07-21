/*
 *
 * Created on 20-lug-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.ungoverned.device;

import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.ServiceList;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.xml.DeviceData;
import org.cybergarage.util.Debug;
import org.cybergarage.xml.Node;

/**
 * @author Kismet TODO To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class DeviceBuilder
{

    public void exec()
    {
        Debug.enabled = true;
        UPnP.setEnable(UPnP.USE_ONLY_IPV4_ADDR);
        UPnP.setEnable(UPnP.USE_LOOPBACK_ADDR);
        Node dev = new Node(Device.ELEM_NAME);
        DeviceData dd = new DeviceData();
        dd.setDescriptionURI("/gen-desc.xml");
        dev.setUserData(dd);
        Node root = new Node(RootDescription.ROOT_ELEMENT);
        root.setAttribute("xmlns", RootDescription.ROOT_ELEMENT_NAMESPACE);
        Node spec = new Node(RootDescription.SPECVERSION_ELEMENT);
        Node maj = new Node(RootDescription.MAJOR_ELEMENT);
        maj.setValue("1");
        Node min = new Node(RootDescription.MINOR_ELEMENT);
        min.setValue("0");
        spec.addNode(maj);
        spec.addNode(min);
        root.addNode(spec);
        root.addNode(dev);
        Device d = new Device(root, dev);
        d.setDeviceType("urn:schemas-upnp-org:device:DimmableLight:1");
        d.setFriendlyName("C.N.R. Dimmable Light");
        d.setManufacture("C.N.R of Pisa - I.S.T.I by Stefano Lenzi");
        d.setManufactureURL("http://www.cnr.it");
        d.setModelDescription("Drimmer Light - Low Power - 100 Candele - 25 Watt");
        d.setModelName("DL100C25W");
        d.setUDN("uuid:CNRDimmableLight");
        d.setLocation("/gen-desc.xml");
        Node s = new Node(Service.ELEM_NAME);
        Node sl = new Node(ServiceList.ELEM_NAME);
        sl.addNode(s);
        dev.addNode(sl);
        Service ser = new Service(s);
        ser.setDescriptionURL("/ser/gen-desc.xml");
        ser.setControlURL("/ser/ctrl");
        ser.setEventSubURL("/ser/event");
        ser.setSCPDURL("/ser/gen-desc.xml");
        ser.setServiceType("urn:schemas-upnp-org:service:SwitchPower:1");
        ser.setServiceID("urn:upnp-org:serviceId:SwitchPower:1");
        d.getServiceList().add(ser);
        d.start();
    }

    public static void main(String args[])
    {
        new DeviceBuilder().exec();
    }

}
