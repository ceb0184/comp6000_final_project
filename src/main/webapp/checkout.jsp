<%-- 
    Document   : checkout
    Created on : Apr 27, 2022, 3:42:57 PM
    Author     : Cambo
--%>

<%@page import="org.w3c.dom.traversal.NodeIterator"%>
<%@page import="org.w3c.dom.traversal.NodeFilter"%>
<%@page import="org.w3c.dom.traversal.DocumentTraversal"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="javax.xml.parsers.DocumentBuilder"%>
<%@page import="javax.xml.parsers.DocumentBuilderFactory"%>
<%@page import="javax.xml.parsers.DocumentBuilderFactory"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="java.util.List"%>
<%@page import="org.w3c.dom.NodeList"%>
<%@page import="org.w3c.dom.Element"%>
<%@page contentType="text/html" pageEncoding="windows-1252"%>

<%
    DocumentBuilderFactory docFactory
            = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    Document doc = docBuilder.parse("http://localhost:8080/final_project/database/user_database.xml");
    String username = (String) request.getAttribute("username");
    String cost = (String)request.getAttribute("cost");
%>
<%!
    Node findUserNodeByName(Element rootElement, String username)
    {
        NodeList userNodes = rootElement.getChildNodes();

        for (int i = 0; i < userNodes.getLength(); i++) 
        {
            Node userNode = userNodes.item(i);
            if (userNode.getNodeName().equals("#text")) 
            {
                continue;
            }
            NodeList userNodeValues = userNode.getChildNodes();
            for (int j = 0; j < userNodeValues.getLength(); j++) 
            {
                Node userNodeChild = userNodeValues.item(j);
                if (userNodeChild.getNodeName().equals("#text")) 
                {
                    continue;
                }
                if (userNodeChild.getNodeName().equals("Username")) 
                {
                    if (userNodeChild.getFirstChild().getNodeValue().equals(username)) 
                    {
                        return userNode;
                    }
                }
            }
        }
        
        throw new IllegalArgumentException(username + ": not found");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <link rel="stylesheet" href="http://localhost:8080/final_project/checkout_style.css">
        <title>Checkout</title>
    </head>
    <body>
        
        <%
            if (request.getAttribute("cartNotEmpty") == null)
            {
        %>
                <h1>Your cart is currently empty!</h1>
                <form action="store.html" method="post">
                    <input type="submit" value="Continue Shopping" class="cart-return-button">
                </form>
        <%
            return;
            }
        %>
        <h1>Here is your shopping cart:</h1><br><br>
        <%
            Element rootElement = doc.getDocumentElement();
            Node userNode = findUserNodeByName(rootElement, username);
            
            DocumentTraversal trav = (DocumentTraversal) doc;
            
            NodeIterator it = trav.createNodeIterator(userNode, NodeFilter.SHOW_ELEMENT, null, true);
            
            for (Node node = it.nextNode(); node != null; node = it.nextNode())
            {
                if (node.getNodeName().equals("Cart"))
                {
                    NodeIterator cartIterator = trav.createNodeIterator(node, NodeFilter.SHOW_TEXT, null, true);
                    
                    for (Node itemNode = cartIterator.nextNode(); itemNode != null; itemNode = cartIterator.nextNode())
                    {
        %>
        <h2><%= itemNode.getNodeValue() %></h2><br><br><br>
        <%
                    }
                }
            }
        %>
        <h2>Cart Total: <%= cost %></h2>
       
        <form action="store.html" method="post"">
            <input type="submit" value="Continue Shopping" class="cart-return-button">
            <input type="button" id="purchaseButton" value="Complete Purchase" class="cart-return-button" onclick="document.getElementById('submitText').style.visibility = 'visible'; 
                                                                                                                   document.getElementById('purchaseButton').style.visibility = 'hidden'">  
        </form><br><br><br><br><br>
        
        <h2 id="submitText" style="visibility: hidden">Thank you for your purchase!</h2>
    </body>
</html>
