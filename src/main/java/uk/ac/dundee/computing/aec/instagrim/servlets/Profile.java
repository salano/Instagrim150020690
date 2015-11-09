/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpSession;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 *
 * @author Salano
 */
@WebServlet(name = "Profile", urlPatterns = {"/Profile","/Profile/*"})
public class Profile extends HttpServlet {
    private Cluster cluster;
    
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   

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
        synchronized(this){
            String args[] = Convertors.SplitRequestPath(request);
            boolean isuser = false;

            //get user information for display
            User currentUser = new User();
            String profileName = org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(args[2]);
            currentUser.setCluster(cluster);
            Hashtable userProfile = new Hashtable();
            PicModel tm = new PicModel();
            tm.setCluster(cluster);
            String UserName = "";
            userProfile = currentUser.getUser(org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(args[2]));
            HttpSession session=request.getSession();
            LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
            Pic p = new Pic();
            if (lg != null) 
                UserName = lg.getUsername();
            if(profileName.compareTo(UserName) == 0){               
                p = tm.getProfilePic(UserName);
                isuser = true;
            }else{
                p = tm.getProfilePic(org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(args[2]));
            }
            RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
            session.setAttribute("userProfile", userProfile);
            session.setAttribute("profilePICTURE", p);
            session.setAttribute("isuser", isuser);
            session.setAttribute("profileName", profileName);

                //PrintWriter out = response.getWriter();
                 //out.println ("Hello"+userProfile.get("emails"));
            rd.forward(request, response);
            notify();
        }

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
            String action=org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("inputsubmit"));
            Hashtable userProfile = new Hashtable();
            Set<String> data = new HashSet<String>();
            String UserName = "";
            PicModel tm = new PicModel();
            tm.setCluster(cluster);
            HttpSession session=request.getSession();
            LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
            if (lg != null) 
                UserName = lg.getUsername();

            PrintWriter out = response.getWriter();
            //out.println("User "+UserName +" name " + request.getParameter("firstname") + " last " + request.getParameter("lastname") +" age " +request.getParameter("age") + " email " + request.getParameter("email"));
            if(action.compareTo("Update") ==0 ){
                User currentUser = new User();
                currentUser.setCluster(cluster);
                currentUser.updateUser(UserName, org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("firstname")),
                                    org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("lastname")),
                                    Integer.parseInt( org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("age"))),
                                    org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("email")),
                                    org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("telephone")),
                                    org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("martial_status")),
                                    org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("street")),
                                    org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("city")),
                                    org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("zip"))
                                    );  
            Pic p = new Pic();
            p = tm.getProfilePic(UserName);
            session.setAttribute("profilePICTURE", p);
            userProfile = currentUser.getUser(UserName);        
            RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
            session.setAttribute("userProfile", userProfile);  
            session.setAttribute("profileName", UserName);
            rd.forward(request, response);
            }else{
                out.println (action);
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
