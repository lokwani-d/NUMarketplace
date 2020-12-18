<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Product details</title>
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
<c:set var="product" value="${requestScope.product}"></c:set>

	<img src="/images/${product.photoFile}" alt="${product.name}" style="width:300px; height:300px;">
<h2>${product.name}</h2>
<h5>${product.description}</h5>
<h5>By: ${product.seller.name}</h5>
<h3>Price: ${product.price}</h3>
<c:if test="${product.type=='tech'}">
<table class="table">
<thead>
	<tr>
		<th>Specification</th>
		<th>Value</th>
	</tr>
</thead>
<c:forEach var="spec" items="${product.spec}">
	<tr>
		<td>${spec.key}</td>
		<td>${spec.value}</td>	
	</tr>
</c:forEach>
</table>
</c:if>
<c:set var="disabled" value=""/>
<c:if test="${product.quantity eq 0 }">
	<c:set var="disabled" value="disabled"></c:set>
	<h4 style="color:red">Out of Stock</h4>
</c:if>
<c:if test="${cookie.user ne null}">
<form action="/edu/user/addToCart.htm" method="post">
Quantity:<input type="number" min="1" max="${product.quantity}" step=1 name="quantity" value="1"/><c:if test="${product.type=='grocery'}"><p>Unit: ${product.unit}</p></c:if><br/><br/>
<input type="hidden" name="id" id="id" value="${product.product_id}" />
<input type="submit" value="Add to Cart" class="btn btn-primary ${disabled}"/>
</form>
<a class="btn btn-danger" href="/edu/user/addReview.htm?prod_id=${product.product_id}">Write Review</a>
</c:if>
<h3 id="reviews">Reviews</h3>
<div id="reviewData"></div>
<script type="text/javascript">
$("document").ready(function(){
	var id= $("input[type='hidden']").val();
	//console.log(id);
	$.ajax({
		url:"/edu/product/getReviews.htm",
		method:"GET",
		 dataType : 'json',
		 data:{id:id,page:1}
		}).done(function(results){
			if(results===null||results.length===0){
				$("<h5>No reviews Yet</h5>").appendTo($("#reviews"));
			}
			else{
				$.each(results,function(index,result){
					const html="<p>"+result.username+"</p> <p style=\"text-align:right\">"+result.addedAt+"</p>"
					+"<p>"+result.text+"</p>";
					$(html).appendTo($("#reviewData"));
				})		
			}
				
	})
})
</script>
</body>

</html>