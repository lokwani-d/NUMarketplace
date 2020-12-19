<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Your Search results</title>
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
	<form action="/edu/search.htm" method="GET">
		<input style="border: gray 1px solid;
				    padding: 12px;
				    font-size: 16px;
				    opacity:0.95;" 
				    type="text" id="search" name="search" placeholder="Search here"
			value="${requestScope.search}" />
			 <input style="border: none;
				    padding: 12px;
				    font-size: 16px;
				    opacity:0.95;" 
				     type="submit" value="Search" /><br />
		<br /> 
		<label style="border: none;
				    padding: 12px;
				    font-size: 16px;
				    opacity:0.95;" 
				    for="filter">Filter</label> 
		<input style="border: none;
				    padding: 12px;
				    font-size: 16px;
				    opacity:0.95;" 
				    type="checkbox"
			name="filter" id="filter" value="tech"> Tech 
		<input style="border: none;
				    padding: 12px;
				    font-size: 16px;
				    opacity:0.95;" 
					type="checkbox" name="filter" 
					id="filter" value="grocery"> Grocery 
		<input style="border: none;
				    padding: 12px;
				    font-size: 16px;
				    opacity:0.95;" 
				    type="checkbox" name="filter" id="filter"
					value="clothing"> Clothing 
		<input style="border: none;
				    padding: 12px;
				    font-size: 16px;
				    opacity:0.95;" 
				    type="submit"
			value="Filter" /> <br /><br />
			
			Sort by:<select style="border: none;
				    padding: 12px;
				    font-size: 16px;
				    opacity:0.95;"  id="sort" name="sort" >
			<c:if test="${requestScope.sort =='name' }">
			<option value="name" selected="selected">Name</option>
			<option value="addedAt">Added Date</option>
			</c:if>
			<c:if test="${requestScope.sort =='addedAt'}">
			<option value="name">Name</option>
			<option value="addedAt" selected="selected">Added Date</option>
			</c:if>
			
			
		</select> <input style="border: none;
				    padding: 12px;
				    font-size: 16px;
				    opacity:0.95;" 
				    type="submit" value="Sort" /> <br /><br />
		Page:<input style="border: none;
				    padding: 12px;
				    font-size: 16px;
				   opacity:0.95;" 
				    type="submit" id="prev" value="Previous"/>
			<input style="border: gray 1px solid;
				    padding: 12px;
				    font-size: 16px;
				    opacity:0.95;" 
				    type="number" id="page" name="page" min="1" value="${requestScope.page}"/>
			<input style="border: none;
				    padding: 12px;
				    font-size: 16px;
				     opacity:0.95;" 
				    type="submit" id="go"/> 
				    <input style="border: none;
				    padding: 12px;
				    font-size: 16px;
				   opacity:0.95;" 
				    type="submit" id="next" value="Next"/>
	</form>
	<div style="display:flex; direction:row; flex-wrap:wrap;">
	<c:if test="${requestScope.products.size() eq 0}">
		<p id="noRes" style="text-align: centre">No results found,please navigate back</p>
	</c:if>
	<c:forEach var="product" items="${requestScope.products }">
		<div class="card" style="width: 18rem;">
			<img class="card-img-top" src="/images/${product.photoFile}" alt="${product.name}" style="width:100px;height:150px">
			<div class="card-body">
				<h4 class="card-title">${product.name}</h4>
				<h4>${product.price}$</h4>
				<p class="card-text">${product.description}</p>
				<a href="/edu/product/getDetails.htm?id=${product.product_id}" class="btn btn-primary">See Details</a>
			</div>
		</div>
	</c:forEach>
	</div>
	<script>
		$("document").ready(function(){
		$("#next").click(function(event){
			$("#page").val(parseInt($("#page").val(),10)+1);
			console.log($("#page").val());
			return true;
		})
		if(parseInt($("#page").val(),10)<=1){
			$("#prev").prop("disabled",true);
		}
		$("#prev").click(function(event){
			$("#page").prop("disabled",false);
			$("#page").val(parseInt($("#page").val(),10)-1);
			console.log($("#page").val());
			return true;
		})
		const el=$("#noRes").length>0;
		if(el){
			$("#page").prop("disabled",true);
			$("#go").prop("disabled",true);
			$("#next").prop("disabled",true);
		}
		else{
			$("#page").prop("disabled",false);
			
		}
	})
	</script>
</body>
</html>