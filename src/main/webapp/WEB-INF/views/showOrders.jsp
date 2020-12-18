<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>${cookie.name.value}|Orders</title>
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
				<th>Order date</th>
				<th>Products</th>
				<th>Price</th>
				<c:choose>
				<c:when test="${cookie.seller eq null}">
				<th>Sellers</th>
				</c:when> 
				<c:otherwise>
					<th>User</th>
				</c:otherwise>
				</c:choose>
			</tr>
		</thead>
		<c:forEach var="order" items="${requestScope.orders}">
			<tr>
				<td>${order.orderedDate}</td>
				<c:forEach items="${order.products}" var="product" varStatus="item">
  						<c:set var="productstring" value="${item.first ? '' : productstring} ${product.key.name}-${product.value}" />
				</c:forEach>
				<td>${productstring}</td>
				<td>${order.totalPrice}</td>
				<c:choose>
					<c:when test="${cookie.seller eq null}">
						<c:forEach items="${order.sellers}" var="seller" varStatus="item">
					<c:set var="sellerString" value="${item.first ? '' : sellerString} ${seller.name}" ></c:set>
				</c:forEach>
				<td>${sellerString}</td>
					</c:when>
					<c:otherwise>
						<td>${order.user.name}</td>
					</c:otherwise>
				</c:choose>
				
			</tr>
		</c:forEach>
	</table>
</body>
</html>