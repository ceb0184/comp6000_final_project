/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package final_project;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Cambo
 */
@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet 
{
    @Override
    protected void doPost(HttpServletRequest request, 
                          HttpServletResponse response) 
                          throws ServletException, IOException 
    {

        String url = "/index.html";
        
        // get current action
//        String action = request.getParameter("action");
//        if (action == null) 
//        {
//            action = "join";  // default action
//        }
//
//        // perform action and set URL to appropriate page
//        if (action.equals("join")) 
//        {
//            url = "/index.html";    // the "join" page
//        }
        if (request.getParameter("login") != null) 
        {                
            // get parameters from the request
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            // store data in User object and save User object in database
            User user = new User(username, password);
            
            // set User object in request object and set URL
            if (UserDatabase.isValidUser(user, response))
            {
                request.setAttribute("user", user);
                UserDatabase.setCurrentUser(user);
                url = "/store.html";   // the "store" page
            }
        }
        else if (request.getParameter("register") != null)
        {
            
            // get parameters from the request
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            // store data in User object and save User object in database
            User user = new User(username, password);
            
            UserDatabase.createUser(user, response.getWriter());
        }
        
        // forward request and response objects to specified URL
        getServletContext()
            .getRequestDispatcher(url)
            .forward(request, response);
    }    
    
    @Override
    protected void doGet(HttpServletRequest request, 
                          HttpServletResponse response) 
                          throws ServletException, IOException {
        doPost(request, response);
    }    
}
