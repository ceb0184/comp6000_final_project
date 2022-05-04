package final_project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

/**
 *
 * @author Cambo
 */
class UserDatabase
{
    private static User currentUser;
    
    public static boolean isValidUser(User user, HttpServletResponse response)
    {
        try 
        {
            DocumentBuilderFactory docFactory =
            DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(UserDatabase.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "\\..\\..\\database\\user_database.xml");
            
            Element rootElement = doc.getDocumentElement();
            NodeList userNodes = rootElement.getChildNodes();
            
            for (int i=0; i<userNodes.getLength(); i++)
            {
                boolean validUsername = false;
                boolean validPassword = false;
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
                        if (userNodeChild.getFirstChild().getNodeValue().equals(user.getUsername()))
                        {
                            validUsername = true;
                        }
                    }
                    if (userNodeChild.getNodeName().equals("Password"))
                    {
                        if (userNodeChild.getFirstChild().getNodeValue().equals(user.getPassword()))
                        {
                            validPassword = true;
                        }
                    }
                }
                if (validPassword && validUsername)
                {
                    return true;
                }
            }
        } 
        catch (ParserConfigurationException | SAXException | IOException ex) 
        {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static User getCurrentUser() 
    {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) 
    {
        UserDatabase.currentUser = currentUser;
    }

    public static void createUserCart(List<String> parameterValues, User user, PrintWriter out) 
    {
        try 
        {
            DocumentBuilderFactory docFactory =
            DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(UserDatabase.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "\\..\\..\\database\\user_database.xml");
            
            Element rootElement = doc.getDocumentElement();
            Node userNode = findUserNodeByName(rootElement, user.getUsername(), out);
            
            // Find cart node
            
            DocumentTraversal trav = (DocumentTraversal) doc;
            
            NodeIterator it = trav.createNodeIterator(userNode, NodeFilter.SHOW_ELEMENT, null, true);
            
            Node cartNodeCurrent = null;
            
            for (Node node = it.nextNode(); node != null; node = it.nextNode())
            {
                if (node.getNodeName().equals("Cart"))
                {
                    cartNodeCurrent = node;
                }
            }
            
            // Create new elements
            Element cartNodeNew = doc.createElement("Cart");
            for (String itemName: parameterValues)
            {
                Element itemNode = doc.createElement("item");
                itemNode.setTextContent(itemName);
                cartNodeNew.appendChild(itemNode);
            }
            
            userNode.replaceChild(cartNodeNew, cartNodeCurrent);
            
            Transformer tFormer = TransformerFactory.newInstance().newTransformer();
            tFormer.setOutputProperty(OutputKeys.METHOD, "xml");
            Source source = new DOMSource(doc);
            
            StreamResult result = new StreamResult(new File(UserDatabase.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "\\..\\..\\database\\user_database.xml"));
            tFormer.transform(source, result);
        } 
        catch (ParserConfigurationException | SAXException | IOException | IllegalArgumentException | TransformerConfigurationException ex) 
        {
            out.println(ex.getMessage());
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) 
        {
            out.println(ex.getMessage());
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex)
        {
            ex.printStackTrace(out);
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void createUser(User user, PrintWriter out)
    {
        try 
        {
            DocumentBuilderFactory docFactory =
            DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(UserDatabase.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "\\..\\..\\database\\user_database.xml");
            
            Element rootElement = doc.getDocumentElement();
            
            Element newUser = doc.createElement("User");
            
            Element username = doc.createElement("Username");
            username.setTextContent(user.getUsername());
            out.println(user.getUsername());
            
            Element password = doc.createElement("Password");
            password.setTextContent(user.getPassword());
            
            Element cart = doc.createElement("Cart");
            
            newUser.appendChild(username);
            newUser.appendChild(password);
            newUser.appendChild(cart);
            
            rootElement.appendChild(newUser);
            
            Transformer tFormer = TransformerFactory.newInstance().newTransformer();
            tFormer.setOutputProperty(OutputKeys.METHOD, "xml");
            Source source = new DOMSource(doc);
            
            StreamResult result = new StreamResult(new File(UserDatabase.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "\\..\\..\\database\\user_database.xml"));

            tFormer.transform(source, result);
            
        } 
        catch (Exception ex)
        {
            ex.printStackTrace(out);
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static private Node findUserNodeByName(Element rootElement, String username, PrintWriter out)
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

}
