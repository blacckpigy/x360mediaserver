/******************************************************************
 *
 *	CyberLink for Java
 *
 *	Copyright (C) Satoshi Konno 2002-2004
 *
 *	File: AllowedValue.java
 *
 *	Revision:
 *
 *	03/27/04
 *		- first revision.
 *	
 ******************************************************************/

package org.cybergarage.upnp;

import org.cybergarage.xml.Node;

public class AllowedValue
{
    // //////////////////////////////////////////////
    // Constants
    // //////////////////////////////////////////////

    public final static String ELEM_NAME = "allowedValue";

    // //////////////////////////////////////////////
    // Member
    // //////////////////////////////////////////////

    private Node               allowedValueNode;

    public Node getAllowedValueNode()
    {
        return allowedValueNode;
    }

    // //////////////////////////////////////////////
    // Constructor
    // //////////////////////////////////////////////

    public AllowedValue(Node node)
    {
        allowedValueNode = node;
    }

    // //////////////////////////////////////////////
    // isAllowedValueNode
    // //////////////////////////////////////////////

    /* Stefano Lenzi */
    public AllowedValue(String string)
    {
        // TODO Test
        allowedValueNode = new Node(ELEM_NAME, string);
    }

    public static boolean isAllowedValueNode(Node node)
    {
        return ELEM_NAME.equals(node.getName());
    }

    // //////////////////////////////////////////////
    // Value
    // //////////////////////////////////////////////

    public void setValue(String value)
    {
        getAllowedValueNode().setValue(value);
    }

    public String getValue()
    {
        return getAllowedValueNode().getValue();
    }
}
