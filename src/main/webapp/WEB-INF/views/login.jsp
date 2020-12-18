<%-- 
    Document   : login
    Created on : Dec 12, 2020, 4:12:52 PM
    Author     : Deepak Lokwani
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
          <!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
        
    </head>
    <c:set var="command" value=""/>
    <c:choose>
    <c:when test="${requestScope.seller ne null}">
    	<c:set var="command" value="seller"/>
    </c:when>
    <c:otherwise>
	    <c:set var="command" value="user"/>
    </c:otherwise>
    </c:choose>
    <body>
    <nav class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">NU MARKETPLACE</a>
			</div>
			<ul class="nav navbar-nav">
				<li><a href="/edu/index.htm">Home</a></li>
			</ul>
		</div>
	</nav>
        <h3>Login!</h3>
        <form:form modelAttribute="${command}">
            Email <form:input path="email"/><form:errors path="email"></form:errors> <br/> <br/>
    		Password <form:password path="password"/><form:errors path="password"></form:errors> <br/><br/>
            <input type="submit" value="Log In"/>
        </form:form>
        <br/><br/>
       <%-- <a href="/edu/resetPassword.htm">Forgot Password?</a> --%>
    </body>
</html>
