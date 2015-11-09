<%-- 
    Document   : index
    Created on : Sep 28, 2014, 7:01:44 PM
    Author     : Administrator
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <header>
            <h1>InstaGrim ! </h1>
            <h2>Your world in Black and White</h2>
        </header>
        <nav>
            <!--Commented by Cleveland 
                <li><a href="upload.jsp">Upload</a></li>-->
                    <%
                        
                        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                        if (lg != null) {
                            if (lg.getlogedin()) {
                    %>
                   <h3>Welcome <%=lg.getUsername()%></h3>
                <p><a href="/Instagrim150020690/Images/<%=lg.getUsername()%>">Your Images</a></p>
                <p><a href="/Instagrim150020690/Profile/<%=lg.getUsername()%>"> Your Profile </a></p>
                <p><a href="/Instagrim150020690/Users">Users </a></p>
                <p><a href="/Instagrim150020690/Logout">Logout <%=lg.getUsername()%> </a></p>
                
                
                    <%}
                        }else{
                    %>
               <ul>
                 <li><a href="register.jsp">Register</a></li>
                <li><a href="login.jsp">Login</a></li>
                <%
                                        
                            
                       }
                %>
            </ul>
        </nav>
        <footer>
            <ul>
                <!--<li class="footer"><a href="/Instagrim">Home</a>&nbsp;</li>-->
                <li>&COPY; Andy C, Sullivan C</li>
            </ul>
        </footer>
    </body>
</html>
