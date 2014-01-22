<%-- 
    Document   : index
    Created on : Nov 15, 2013, 5:08:17 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%@page import="javax.servlet.http.*;"%>
    <% 
    if((!session.isNew())&&session.getAttribute("user")!=null)
    {
    RequestDispatcher rd=request.getRequestDispatcher("/start.jsp");
    rd.forward(request, response);
    }
    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Coding round</title>
        <script type="text/javascript" lang="javascript" src="js/jquery.js"></script>
        <script type="text/javascript" lang="javascript" src="js/md5js.js"></script>   
        <script type="text/javascript" lang="javascript">
            function validate(){
                var uname = document.getElementById('uname').value;
                var pwd = document.getElementById('pwd').value;
                //alert(uname+' '+pwd)
                $.ajaxSetup({async:false});
                $.post('/OnlineCompiler/Login',{username:uname,password:pwd}).done(function(data){                    
                     //alert(data);                   
                });
                $.ajaxSetup({async:true});
            }
        </script>
    </head>
    <body>
    <center>
        <form method="post" action="/OnlineCompiler/Login">
            <table>
                <tr>
                    <td>Username: </td>
                    <td><input type="text" name="uname" id="uname"></td>                    
                </tr>
                <tr>
                    <td>Password: </td>
                    <td><input type="password" name="pwd" id="pwd"></td>
                </tr>
            </table>
            <input type="submit" name="login" value="Login">
        </form>
        <a href="register.jsp">Register</a>
    </center>
    </body>
</html>
