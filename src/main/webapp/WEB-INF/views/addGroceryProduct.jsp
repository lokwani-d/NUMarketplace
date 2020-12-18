<%-- 
    Document   : addGroceryProduct.jsp
    Created on : Dec 13, 2020, 7:47:52 AM
    Author     : Deepak Lokwani
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>${cookie.name.value}| Add Product</title>
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
	<form:form modelAttribute="groceryProduct" enctype="multipart/form-data">
		Name <form:input path="name" required="required"/> <form:errors path="name"></form:errors>
		Description <form:input path="description" required="required"/> <form:errors name="description"></form:errors>
		Photo <input type="file" name="photo" accept="image/*" required="required"/> 
		Price  <input type='number'
			name="price" id="price" min="0"  step="0.1"  required="required"/> <form:errors name="price"></form:errors>
		Quantity  <input type='number'
			name="quantity" id="quantity" min="1"  step="1" required="required"/><form:errors name="quantity"></form:errors><br/>
		Unit:<form:input path="unit" required="required"/><form:errors name="unit"></form:errors>
		<input type="submit" value="Add product"/>
	</form:form> 
</body>
</html>