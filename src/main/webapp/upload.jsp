<%-- 
    Document   : upload
    Created on : Sep 22, 2014, 6:31:50 PM
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
            <h2>Your world in Black and White</h2> <br />
            <h3>Upload Pictures</h3>
        </header>
        <nav>
            <ul>
               <!-- <li class="nav"><a href="upload.jsp">Upload</a></li>
                <li class="nav"><a href="/Instagrim/Images/majed">Sample Images</a></li> -->
            </ul>
        </nav>
 
        <article>
            <h3>File Upload</h3>
            <form method="POST" enctype="multipart/form-data" action="Image">
                <table>
                    <tr><td>
                            File to upload: <input type="file" name="upfile"></td><td><input type="submit" value="Press"> to upload the file!</td></tr>

                </table>
            </form>

        </article>
        <footer >
            <ul>
                <li class="footer"><a href="/Instagrim150020690">Home</a> &nbsp;</li>
                <li>&COPY; Andy C, Sullivan C</li>
            </ul>
        </footer>
    </body>
</html>
