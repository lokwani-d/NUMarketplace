<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Sign up as Seller</title>
<script
  src="https://code.jquery.com/jquery-3.4.1.min.js"
  integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
  crossorigin="anonymous"></script>
  <!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
</head>
<body>
 <form:form modelAttribute="seller" enctype="multipart/form-data">
    	Name <form:input path="name"/><form:errors path="name"></form:errors><br/><br/>
    	Email <form:input path="email" type="email"/><form:errors path="email"></form:errors> <br/> <br/>
    	Password <form:password path="password"/><form:errors path="password"></form:errors> <br/><br/>
    	Confirm password <form:password path="confirmPassword" /><form:errors path="confirmPassword"></form:errors><br/><br/>
    	Photo <input type="file" name="photo" accept="image/*"><br/><br/>
    	<c:if test="${requestScope.seller ne null}">
    	Type <form:select path="type">
    		<form:option value="Tech"></form:option>
    		<form:option value="Grocery"></form:option>
    	</form:select><form:errors path="type"></form:errors> 
    	</c:if>
    	<input type="submit" value="Sign Up"/>
    </form:form>
</body>
</html>