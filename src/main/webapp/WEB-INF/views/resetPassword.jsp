<%-- 
    Document   : resetPassword
    Created on : Dec 12, 2020, 4:12:52 PM
    Author     : Deepak Lokwani
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reset Password</title>
    </head>
    <body>
        <h3>Reset Password</h3>
        <form method="post" action="/edu/updatePassword.htm">
            <label for="pword">Password</label>
            <input type='password' placeholder='password' name="pword" id="pword" required/>
            <br/><br/>
            <label for="cpword">Confirm Password</label>
            <input type='text' placeholder="Confirm password" name="cpword" id="cpword" required/>
            <br/><br/>
            <input type="hidden" value="updatePassword" name="action"/>
            <input type="hidden" value="${requestScope.email}" name="email"/>
            <input type="submit" value="Reset"/>
        </form>
    </body>
</html>
