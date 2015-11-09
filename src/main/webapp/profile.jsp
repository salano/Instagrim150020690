
<%-- 
    Document   : index
    Created on : Sep 28, 2014, 7:01:44 PM
    Author     : Administrator
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@page import="java.util.Hashtable" %>
<%@page import="java.io.PrintWriter" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
        <script type="text/javascript" src="http://code.jquery.com/jquery-2.1.4.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script>
            $(document).ready(function(){
                $('#upprofile').hide();
                $('#picsubmit').hide();
                $('.inputtd').hide();
                $('#inputsubmit').toggle();
                $("#profilepic").click(function() {
                    $('#upprofile').toggle();
                    $('#picsubmit').toggle();

                });
                $("#profiledetails").click(function() {
                    $('.inputtd').toggle();
                    $('#inputsubmit').toggle();
                    
                });
            }); 
        </script>
    </head>
    <body>
        <header>
            <h1>InstaGrim ! </h1>
            <h2>Your world in Black and White</h2>
        </header>
        <nav>
        </nav>
            <div id="ppic">
                <%
                    boolean isuser = (boolean) session.getAttribute("isuser");
                    String profileName = (String) session.getAttribute("profileName");
                    Pic p = (Pic) session.getAttribute("profilePICTURE");
                    if (p == null) {
                %>
                        <p>No Profile Picture found</p>
                <%
                    } else {
                %>
                        <a href="/Instagrim/Image/<%=p.getSUUID()%>" ><img src="/Instagrim150020690/Thumb/<%=p.getSUUID()%>"></a><br/><%

                    }
                %>
                <form method="POST" enctype="multipart/form-data" action="/Instagrim150020690/Image">
                <% if(isuser) { %>
                <a href="#" id="profilepic">Profile Picture</a>             
                Profile upload: <input type="file" name="upprofile" id="upprofile"/><br/>
                <% } %>
                <input type="hidden" name="profile_pic" value="profile_pic" />
                <input type="submit" value="Upload" id="picsubmit"/> 
                </form>
            </div>
            <div id="pbody">
               <!--Commented by Cleveland 
                <li><a href="upload.jsp">Upload</a></li>-->
                    <%
                        
                        //LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                        Hashtable rs = (Hashtable) session.getAttribute("userProfile");
                        Address address = (Address) rs.get("address");
                        String street = "";
                        String city = "";
                        int zip =0;
                        if(address != null){
                            street=  address.getStreet();
                            city= address.getCity();
                            zip = address.getZip();
                        }
                        
                    %>
                    
                   <p><%=profileName%>'s Profile</p>
                   <% if(isuser) { %>
                   
                   <a href="#" id="profiledetails">Update Profile</a>
                   <% } %>
                   <form action="/Instagrim150020690/Profile" method="POST">
                       <table>
                           <tr><td>First Name</td><td class="display"><%=rs.get("firstname")%></td><td class="inputtd"><input type="text" value="<%=rs.get("firstname")%>" name="firstname" /></td></tr>
                           <tr><td>Last Name</td><td class="display"><%=rs.get("lastname")%></td><td class="inputtd"><input type="text" value="<%=rs.get("lastname")%>" name="lastname" /></td></tr>
                           <tr><td>Age</td><td class="display"><%=rs.get("age")%></td><td class="inputtd"><input type="text" value="<%=rs.get("age")%>" name="age" /></td></tr>
                           <tr><td>Email</td><td class="display"><%=rs.get("email")%></td><td class="inputtd"><input type="text" value="<%=rs.get("email")%>" name="email" /></td></tr>
                           <tr><td>Martial Status</td><td class="display"><%=rs.get("martial_status")%></td><td class="inputtd"><input type="text" value="<%=rs.get("martial_status")%>" name="martial_status" /></td></tr>
                           <tr><td>Telephone</td><td class="display"><%=rs.get("telephone")%></td><td class="inputtd"><input type="text" value="<%=rs.get("telephone")%>" name="telephone" /></td></tr>
                           <tr><td>Street</td><td class="display"><%=street%></td><td class="inputtd"><input type="text" value="<%=street%>" name="street" /></td></tr>
                           <tr><td>City</td><td class="display"><%=city%></td><td class="inputtd"><input type="text" value="<%=city%>" name="city" /></td></tr>
                           <tr><td>Zip</td><td class="display"><%=zip%></td><td class="inputtd"><input type="text" value="<%=zip%>" name="zip" /></td></tr>

                           <tr><td colspan="3"><input type="submit" value="Update" id="inputsubmit" name="inputsubmit"/> </td></tr>
                           
                       </table>
                   </form>
                <a href="/Instagrim150020690/Images/<%=profileName%>" ><%=profileName%>'s Images</a>
             </div>       
                            
                 
                
          
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim150020690">Home</a> &nbsp;</li>
                <li>&COPY; Andy C, Sullivan C</li>
            </ul>
        </footer>
    </body>
</html>
