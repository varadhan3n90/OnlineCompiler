<%-- 
    Document   : register
    Created on : Nov 15, 2013, 6:17:53 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Coding registration</title>
    </head>
    <body>
    <center>
        <form method="post" action="/OnlineCompiler/Register">
            <table>
                <tr>
                    <td>Username: </td>
                    <td><input type="text" name="uname" id="uname"></td>                    
                </tr>
                <tr>
                    <td>Passowrd: </td>
                    <td><input type="password" name="pwd" id="pwd"></td>
                </tr>
                <tr>
                    <td>Your name: </td>
                    <td><input type="text" name="realname" id="realname"></td>
                </tr>
            </table>
            <input type="submit" name="login" value="Register">
        </form>
        </center>
    </body>
</html>
