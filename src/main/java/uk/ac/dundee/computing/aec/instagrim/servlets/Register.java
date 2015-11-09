/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import org.apache.commons.lang3.StringEscapeUtils.*;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {
    Cluster cluster=null;
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
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
        synchronized(this){
            String firstname= org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("firstname"));
            String lastname=org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("lastname"));
            String username=org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("username"));
            String password=org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("password"));
            String cpassword=org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("cpassword"));
            String email=org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("email")); 
            String age=org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("age"));
            String street =org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("street"));
            String city =org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("city"));
            String zip =org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("zip"));

            User us=new User();
            us.setCluster(cluster);
            if(password.compareTo(cpassword) !=0 || password.isEmpty())
            {

                RequestDispatcher rd = request.getRequestDispatcher("/register.jsp");
                HttpSession session=request.getSession();
                session.setAttribute("msg", "Unmatched Password");
                rd.forward(request, response);
            
            }else if(us.userExist(username)){
                RequestDispatcher rd = request.getRequestDispatcher("/register.jsp");
                HttpSession session=request.getSession();
                session.setAttribute("msg", "User in use");
                rd.forward(request, response);
            
            }else{
                us.RegisterUser(username, password,firstname,lastname,email,age,street,city,zip);

                response.sendRedirect("/Instagrim150020690/");
            }
            
            notify();
        }
        
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
