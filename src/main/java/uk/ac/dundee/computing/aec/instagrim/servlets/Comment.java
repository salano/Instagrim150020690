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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.models.CommentModel;
import java.util.UUID;
import javax.servlet.ServletConfig;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 *
 * @author Salano
 */
@WebServlet(name = "Comment", urlPatterns = {"/Comment","/Comment/*"})
public class Comment extends HttpServlet {
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
            if(args[2].equalsIgnoreCase("Delete")){
                CommentModel cm = new CommentModel();
                cm.setCluster(cluster);
                cm.removeComment(UUID.fromString(args[3]));
                args[3]= args[4];
            }

            /*PrintWriter out = response.getWriter();
            out.write(args[3]);*/
            HttpSession session=request.getSession();
            RequestDispatcher rd = request.getRequestDispatcher("/comments.jsp");
            session.setAttribute("picid", UUID.fromString(args[3]));
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
            java.util.UUID picid = UUID.fromString(org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("picid")).trim());
            String comment_text = org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(request.getParameter("commentbox")).trim();
            HttpSession session=request.getSession();
            LoggedIn lg= (LoggedIn)session.getAttribute("LoggedIn");
            String username="";
            if (lg.getlogedin()){
                username=lg.getUsername();
            }
            CommentModel cm = new CommentModel();
            cm.setCluster(cluster);
            Pic pic = new Pic();
            cm.addComment(picid, username, comment_text);
            pic.setUUID(picid);
            pic.addComments(cm.getComment(picid));       
            RequestDispatcher rd = request.getRequestDispatcher("/comments.jsp");
            session.setAttribute("pic", pic);
            rd.forward(request, response);
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
