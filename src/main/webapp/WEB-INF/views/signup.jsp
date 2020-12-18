<%-- 
    Document   : signup
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
        <title>Sign Up</title>
    </head>
    <body>
    <c:set var="command" value=""/>
    <c:choose>
    <c:when test="${requestScope.seller ne null}">
    	<c:set var="command" value="seller"/>
    </c:when>
    <c:otherwise>
	    <c:set var="command" value="user"/>
    </c:otherwise>
    </c:choose>
    <form:form modelAttribute="${command}" enctype="multipart/form-data">
    	Name <form:input path="name"/><form:errors path="name"></form:errors><br/><br/>
    	Email <form:input path="email" type="email"/><form:errors path="email"></form:errors> <br/> <br/>
    	Password <form:password path="password"/><form:errors path="password"></form:errors> <br/><br/>
    	Confirm password <form:password path="confirmPassword" /><form:errors path="confirmPassword"></form:errors><br/><br/>
    	<%-- --%>Photo <input type="file" name="photo" accept="image/*"><br/><br/>
    	<c:if test="${requestScope.seller ne null}">
    	Type <form:select path="type">
    		<form:option value="Clothing"></form:option>
    		<form:option value="Tech"></form:option>
    		<form:option value="Grocery"></form:option>
    	</form:select><form:errors path="type"></form:errors> 
    	</c:if>
    	<input type="submit" value="Sign Up"/>
    </form:form>
    </body>
</html>
