/******************************************************************
 *
 *	CyberXML for Java
 *
 *	Copyright (C) Satoshi Konno 2004
 *
 *   Author: Markus Thurner (http://thoean.com)
 *
 *	File: JaxpParser.java
 *
 *	Revision;
 *
 *	06/15/04
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.xml.parser;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.cybergarage.xml.Node;
import org.cybergarage.xml.Parser;
import org.cybergarage.xml.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;

public class JaxpParser extends Parser
{

    public JaxpParser()
    {
        super();
    }

    // //////////////////////////////////////////////
    // parse (Node)
    // //////////////////////////////////////////////

    public org.cybergarage.xml.Node parse(org.cybergarage.xml.Node parentNode,
                                          org.w3c.dom.Node domNode, int rank)
    {
        int domNodeType = domNode.getNodeType();
// if (domNodeType != Node.ELEMENT_NODE)
// return;

        String domNodeName = domNode.getNodeName();
        String domNodeValue = domNode.getNodeValue();
        NamedNodeMap attrs = domNode.getAttributes();
        int arrrsLen = (attrs != null) ? attrs.getLength() : 0;

// Debug.message("[" + rank + "] ELEM : " + domNodeName + ", " + domNodeValue + ", type = " +
// domNodeType + ", attrs = " + arrrsLen);

        if (domNodeType == org.w3c.dom.Node.TEXT_NODE)
        {
            parentNode.setValue(domNodeValue);
            return parentNode;
        }

        if (domNodeType != org.w3c.dom.Node.ELEMENT_NODE)
            return parentNode;

        org.cybergarage.xml.Node node = new org.cybergarage.xml.Node();
        node.setName(domNodeName);
        node.setValue(domNodeValue);

        if (parentNode != null)
            parentNode.addNode(node);

        NamedNodeMap attrMap = domNode.getAttributes();
        int attrLen = attrMap.getLength();
        // Debug.message("attrLen = " + attrLen);
        for (int n = 0; n < attrLen; n++ )
        {
            org.w3c.dom.Node attr = attrMap.item(n);
            String attrName = attr.getNodeName();
            String attrValue = attr.getNodeValue();
            node.setAttribute(attrName, attrValue);
        }

        org.w3c.dom.Node child = domNode.getFirstChild();
        // Thanks for Stefano Lenzi (02/10/05)
        if (child == null)
        {
            node.setValue("");
            return node;
        }
        do
        {
            parse(node, child, rank + 1);
            child = child.getNextSibling();
        } while (child != null);
        return node;
    }

    public org.cybergarage.xml.Node parse(org.cybergarage.xml.Node parentNode,
                                          org.w3c.dom.Node domNode)
    {
        return parse(parentNode, domNode, 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.cybergarage.xml.Parser#parse(java.io.InputStream)
     */
    public Node parse(InputStream inStream) throws ParserException
    {
        org.cybergarage.xml.Node root = null;
        Document doc = null;
        try
        {
            // TODO Change this line for OSGi Service in
            /*
             * ServiceReference ref = context.getServiceReference(
             * DocumentBuilderFactory.class.getName() ); if ( ref == null ){ context.stop();
             * log.println("Unable to find XML Parser Service"); return null; } builder
             * =(DocumentBuilder) context.getService(ref);
             */
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inSrc = new InputSource(inStream);
            doc = builder.parse(inSrc);

        }
        catch (Exception e)
        {
            throw new ParserException(e);
        }

        org.w3c.dom.Element docElem = doc.getDocumentElement();
        if (docElem != null)
            root = parse(root, docElem);
/*
 * NodeList rootList = doc.getElementsByTagName("root"); Debug.message("rootList = " +
 * rootList.getLength()); if (0 < rootList.getLength()) root = parse(root, rootList.item(0));
 */

        return root;
    }

}
