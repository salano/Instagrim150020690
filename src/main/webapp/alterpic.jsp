<%-- 
    Document   : alterpic
    Created on : Oct 24, 2015, 11:03:15 AM
    Author     : Salano
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <header>
            <h1>InstaGrim ! </h1>
            <h2>Your world in Black and White</h2>
        </header>
        <nav>
        </nav>
        <% 
            Pic p = (Pic) session.getAttribute("Fpic");
            if (p == null) {
        %>
                <img src="/Instagrim150020690/Image/<%=p.getSUUID()%>">
        <%
            }
        %>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim150020690">Home</a> &nbsp;</li>
                <li>&COPY; Andy C, Sullivan C</li>
            </ul>
        </footer>
    </body>
    
</html>
