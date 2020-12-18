<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>${cookie.name.value} Cart</title>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"
	integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
	crossorigin="anonymous"></script>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
	crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
	integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp"
	crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
	integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
	crossorigin="anonymous"></script>
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
	<table class="table">
		<thead>
			<tr>
				<th>Name</th>
				<th>Price</th>
				<th>Quantity</th>
				<th>Update</th>
				<th>Delete</th>
			</tr>
		</thead>
		<c:forEach var="product" items="${requestScope.cart}">
			<tr>
				<td>${product.key.name}</td>
				<td>${product.key.price*product.value}</td>
				<td>${product.value}</td>
				<td><a class="btn btn-primary" href="/edu/product/getDetails.htm?id=${product.key.product_id}">Update</a></td>
				<td><a class="btn btn-danger" href="/edu/user/deleteFromCart.htm?id=${product.key.product_id}">Delete</a></td>
			</tr>
		</c:forEach>	
	</table>
	<h4>Total: ${requestScope.total}</h4>
	<form action="/edu/user/checkout.htm" method="GET">
		<input type="submit" class="btn btn-success" value=" Proceed to Checkout"/>	
	</form>
</body>
</html>