package final_project;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Cambo
 */
@WebServlet("/cartServlet")
public class CartServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        
        try ( PrintWriter out = response.getWriter()) 
        {
            String[] storeResultArr = request.getParameterValues("gameSelect");
            List<String> gameSelectedList = new ArrayList<>();
            
            if (storeResultArr == null)
            {
                gameSelectedList = new ArrayList<>();
                request.setAttribute("cost", "0.00");
            }
            else
            {
                double cost = 0;
                for (String string : Arrays.asList(storeResultArr))
                {
                    String[] parsedString = string.split("\\|");
                    gameSelectedList.add(parsedString[0]);
                    cost += Double.parseDouble(parsedString[1]);
                }
                request.setAttribute("cost", String.format("%.2f", cost));
                request.setAttribute("cartNotEmpty", true);
            }
            UserDatabase.createUserCart(gameSelectedList, UserDatabase.getCurrentUser(), out);
            String url = "/checkout.jsp";
            request.setAttribute("username", UserDatabase.getCurrentUser().getUsername());
            getServletContext()
            .getRequestDispatcher(url)
            .forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
