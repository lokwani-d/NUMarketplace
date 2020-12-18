<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title><c:out value="${cookie.name.value}"/> | NU MARKETPLACE</title>
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
	<h2>Profile</h2>
	<c:set var="user" value="${requestScope.user }"></c:set>
	<form method="post" action="../user/updateProfile.htm">
		<label for="name">Username</label> 
		<input type='text'
			placeholder="Username" name="name" id="name" value="${user.getName()}" required /> <br /> <br />
		 <label for="email">Email</label> 
		 <input type='email'
			placeholder="Email" name="email" id="email" value="${user.getEmail() }" required /> <br />
		<br />
		 <input type="submit" value="Submit changes" />
	</form>
	<a class="btn btn-primary" href="/edu/user/getOrders.htm">Your Orders</a>
</body>
</html>