<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Seller Profile</title>
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
				<a class="navbar-brand" href="#">NU Marketplace</a>
			</div>
			<ul class="nav navbar-nav">
				<li><a href="/edu/index.htm">Home</a></li>
			</ul>
		</div>
	</nav>
		<h2>Profile</h2>
	<c:set var="user" value="${requestScope.user }"></c:set>
	<form method="post" action="../seller/updateProfile.htm">
		<label for="name">Username</label> 
		<input type='text'
			placeholder="Username" name="name" id="name" value="${cookie.name.value}" required /> <br /> <br />
		 <label for="email">Email</label> 
		 <input type='email'
			placeholder="Email" name="email" id="email" value="${cookie.seller.value}" required /> <br />
		<br />
		 <input type="submit" value="Submit changes" />
	</form>
	<h2>Add new Products</h2>
	<c:if test="${user.type=='Tech'}">
	<a class="btn btn-primary" href="/edu/seller/addTechProduct.htm">Add Product</a>
	</c:if>
	<c:if test="${user.type=='Grocery'}">
	<a class="btn btn-primary" href="/edu/seller/addGroceryProduct.htm">Add Product</a>
	</c:if>
	<h2>Products</h2>
	<table class="table"  style="margin: 30px">
	<thead>
	<tr>
	<th>Id</th>
	<th>Name</th>
	<th>Price</th>
	<th>Quantity</th>
	<th>Update</th>
	<th>Delete</th>	
	</tr>
	</thead>
	</table>
	<a class="btn btn-primary" href="/edu/seller/getOrders.htm">View Orders</a>
</body>
<script type="text/javascript">
	$("document").ready(function(){
		$.ajax({
			url:"/edu/seller/product.htm",
			method:"GET",
			 dataType : 'json',
			}).done(function(results){
				console.log(results);
			$.each(results,function(index,result){
				console.log(result);
				const html="<tr style=\"margin-bottom:10px;\"><td>"+result["product_id"]+"</td>"+
				"<td>"+result.name+"</td>"+
				"<td>"+result.price+"</td>"+
				"<td>"+result.quantity+"</td>"+
				"<td><a class=\"btn btn-primary\" href=\"/edu/seller/updateProduct.htm?prod_id="+result["product_id"]+"\">update</a></td>"+
				"<td><a class=\"btn btn-danger\" href=\"/edu/seller/deleteProduct.htm?prod_id="+result["product_id"]+"\">delete</a></td>"+
				+"</tr>"
				$(html).appendTo($(".table"))
			})
		})
	})
</script>
</html>