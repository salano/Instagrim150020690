<%-- 
    Document   : UsersPics
    Created on : Sep 24, 2014, 2:52:48 PM
    Author     : Administrator
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
    </head>
    <body>
        <header>
        
        <h1>InstaGrim ! </h1>
        <h2>Your world in Black and White</h2>
        </header>
        
        <nav>
            <ul>
                <li class="nav"><a href="/Instagrim150020690/upload.jsp">Upload</a></li>
                <!--<li class="nav"><a href="/Instagrim/Images/majed">Sample Images</a></li>-->
            </ul>
        </nav>
 
        <article>
            <h1>Your Pics</h1>
        <%
            LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");             
            java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("Pics");
            if (lsPics == null) {
        %>
        <p>No Pictures found</p>
        <%
        } else {
            Iterator<Pic> iterator;
            iterator = lsPics.iterator();
            while (iterator.hasNext()) {
                Pic p = (Pic) iterator.next();
                int size =0;
                if( p.getLikes() !=null)
                    size  = p.getLikes().size();
        %>
        <p>
            
        <a href="/Instagrim150020690/Image/<%=p.getSUUID()%>" ><img src="/Instagrim150020690/Thumb/<%=p.getSUUID()%>"></a><br/>
        <table id="piclinks">
            <tr>
                <td><a href="/Instagrim150020690/Pimage/<%=lg.getUsername()%>/<%=p.getSUUID()%>">Set as profile pic</a></td>
                <td><a href="/Instagrim150020690/Images/Likes/<%=lg.getUsername()%>/<%=p.getSUUID()%>"><%=size%> likes</a></td>
                <td><a href="/Instagrim150020690/alter/<%=lg.getUsername()%>/<%=p.getSUUID()%>">Alter picture</a></td>
                <td><a href="/Instagrim150020690/Image/Comments/<%=p.getSUUID()%>">View Comments</a></td>
                <td><a href="/Instagrim150020690/Comment/Add/<%=p.getSUUID()%>">Add Comment</a></td>
            </tr>
        </table><% 
                    
              
               
            }
         }
        %>
        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim150020690">Home</a> &nbsp;</li>
                <li>&COPY; Andy C, Sullivan C</li>
            </ul>
        </footer>
    </body>
</html>
