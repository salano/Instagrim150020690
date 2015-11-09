<%-- 
    Document   : users
    Created on : Oct 13, 2015, 6:02:22 PM
    Author     : Salano
--%>
<%@page import="java.util.Hashtable" %>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
            <h2>Your world in Black and White</h2> <br />
            <h3>Active users</h3>
        </header>
        <nav>
            
        </nav>
            <%
                java.util.LinkedList<Person> users = (java.util.LinkedList<Person>) session.getAttribute("Users");
                if( users ==null){
            %>
            <p> No Users Found</p>
            <%
        } else { %>
        <ul><%
            Iterator<Person> iterator;
            iterator = users.iterator();
            while (iterator.hasNext()) {
                Person p = (Person) iterator.next();
        %>
        <li><a href="/Instagrim150020690/Profile/<%=p.getUsername()%>"><%=p.getFirstname()+" "+p.getLastname() %></a></li><%
            }
       }
        %>
        </ul>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim150020690">Home</a> &nbsp;</li>
                <li>&COPY; Andy C, Sullivan C</li>
            </ul>
        </footer>
    </body>
</html>
