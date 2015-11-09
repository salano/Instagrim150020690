<%-- 
    Document   : comments
    Created on : Oct 12, 2015, 5:15:22 PM
    Author     : Salano
--%>
<%@page import="java.util.*"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
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
            <h2>Your world in Black and White</h2>
        </header>
        <nav></nav>
        <div>
            <%
                
                java.util.UUID picid = (java.util.UUID) session.getAttribute("picid");
                if (picid != null) {%>
                    <img src="/Instagrim150020690/Thumb/<%=picid%>" alt="pic not loaded" /><%
                }
                Pic p = (Pic) session.getAttribute("pic");
                if(p != null && picid==null){ %>
                    <img src="/Instagrim150020690/Thumb/<%=p.getSUUID()%>" alt="pic not loaded" /><%
                
                    java.util.LinkedList<Comment> comments = p.getComments();
                    if(comments !=null){%>
                        <ul>
                        <%
               
                        Iterator<Comment> comment_iterator;
                        comment_iterator = comments.iterator();
                        while (comment_iterator.hasNext()){
                            Comment c = (Comment) comment_iterator.next();
                        %>
                        <li><%=c.getCommenText() %> <br /> Posted:<%=c.getDate() %> By:<i> <%=c.getUserName()%> </i> <br />  <a href="/Instagrim150020690/Comment/Delete/<%=c.getCommenId()%>/<%=p.getSUUID()%>" > Remove</a></li> <%
                        }
                        %>
                        </ul>
                  <%
                    }else{ %>
                        <p> No Comments yet</p>
                    <%   
                    }
                    
                }else{
                    %>
                    
                <% }
            %>
        </div>
        <form method="post" action="/Instagrim150020690/Comment">
            <textarea name="commentbox"></textarea>
            <% if(picid != null){%>
            <input type="hidden" name="picid" value="<%=picid%>" /><br /><%
            }else{ %>
            <input type="hidden" name="picid" value="<%=p.getSUUID()%>" /><br /><%
            } %>
            <input type="submit" name="submit" value="Submit" />
        </form>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim150020690">Home</a>&nbsp;</li>
                <li>&COPY; Andy C, Sullivan C</li>
            </ul>
        </footer>
    </body>
</html>
