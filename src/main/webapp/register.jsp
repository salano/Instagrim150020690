<%-- 
    Document   : register.jsp
    Created on : Sep 28, 2014, 6:29:51 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
          <!-- Commented by Cleveland<ul>
                
                <li><a href="/Instagrim/Images/majed">Sample Images</a></li>
            </ul> -->
        </nav>
       
        <article>
            <h3>Register as user</h3>
            
            <% 
                String msg = (String) session.getAttribute("msg");
                if(msg != null){
            %>
                    <p id="msg"><%=msg%></p>
            <% }
            %>
                
            <form method="POST"  action="Register">
                <table>
                    <tr>
                        <td><label for="firstname">First Name </label><input type="text" name="firstname" /></td>
                    </tr>
                    <tr>
                        <td><label for="lastname">Last Name </label><input type="text" name="lastname" /></td>
                    </tr>
                    <tr>
                        <td><label for="username">User Name </label><input type="text" name="username" /></td>
                    </tr>
                    <tr>
                        <td><label for="password">Password </label><input type="password" name="password" /></td>
                    </tr>
                    <tr>
                        <td><label for="cpassword">Confirm Password </label><input type="password" name="cpassword" /></td>
                    </tr>
                    <tr>
                        <td><label for="email">Email </label><input type="text" name="email" /></td>
                    </tr>
                    <tr>
                        <td><label for="age">Age </label><input type="text" name="age" /></td>
                    </tr>
                    <tr>
                        <td><label for="street">Street </label><input type="text" name="street" /></td>
                    </tr>
                    <tr>
                        <td><label for="city">City </label><input type="text" name="city" /></td>
                    </tr>	
                    <tr>
                        <td><label for="zip">Zip </label><input type="text" name="zip" /></td>
                    </tr>

                    <tr><td><input type="submit" value="Register"></td></tr>
                </table>
                
                 
            </form>

        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim150020690">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
