<%-- 
    Document   : home.jsp
    Created on : Dec 12, 2020, 7:39:52 PM
    Author     : Deepak Lokwani
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@page import="javax.servlet.http.Cookie" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>NU MARKETPLACE | HOME</title>
        <script
			  src="https://code.jquery.com/jquery-3.4.1.min.js"
			  integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
			  crossorigin="anonymous">
        </script>
		<!-- Latest compiled and minified CSS -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
		
		<!-- Optional theme -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
		
		<!-- Latest compiled and minified JavaScript -->
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    </head>
    
    <body style="background-color: black">
        <header style="text-align: center; color: Grey; padding:0px 20px">An online shopping experience of a kind</header>
       
        <h1 style="text-align: center; color: Orange">NU MARKETPLACE</h1>
        <c:set var="logged_in_user" value="${cookie.name}"/>
        <c:set var="user" value=""/>
        <c:choose>
        	<c:when test="${cookie.seller ne null }">
        		<c:set var="user" value="seller"/>
        	</c:when>
        	<c:otherwise>
        		<c:set var="user" value="user"></c:set>
        	</c:otherwise>
        </c:choose>
        
        <nav class="navbar navbar-default">
        	<div class="container-fluid">
        		<div class="navbar-header" style="padding:0px 25px; margin: 0px">
        			<a class="navbar-brand" href="/edu/index.htm">Home</a>
        		</div>
        		<ul class="nav navbar-nav">
        		
			        <c:choose>
			            <c:when test="${logged_in_user==null||logged_in_user.value==''}">
			                <li style="padding:0px 20px"><a href="http://localhost:8080/edu/user/signup.htm">Customer SignUp</a></li>
			                <li style="padding:0px 20px"><a href="http://localhost:8080/edu/user/login.htm">Customer Login</a></li> 
			                <li style="padding:0px 20px"><a href="http://localhost:8080/edu/seller/signup.htm">Seller SignUp</a></li>
			                <li style="padding:0px 20px"><a href="http://localhost:8080/edu/seller/login.htm">Seller Login</a></li>
			            </c:when>
			            
			            <c:otherwise>
			                <li style="padding:0px 20px"><a href="#">Hello <c:out value="${logged_in_user.value}"/></a></li>
			                <li style="padding:0px 20px"><a href="http://localhost:8080/edu/logout.htm">Logout</a></li> 
							<li style="padding:0px 20px"><a href="http://localhost:8080/edu/${user}/account.htm">Account</a></li> 
			            </c:otherwise>
			        </c:choose>
			        <c:if test="${cookie.user ne null }">
			        	<li style="padding:0px 20px"><a href="http://localhost:8080/edu/user/cart.htm">Cart</a></li>
			        </c:if>
        		</ul>
        	</div>
        </nav>
        <form style="padding:0px 20px" action="/edu/search.htm" method="GET">
        <input style="border: none;
				    padding: 12px;
				    font-size: 16px;
				    opacity:0.6;"
				     type="text" id="search" name="search" placeholder="Search items"/>
        <input style="border: none;
				    padding: 12px;
				    font-size: 16px;
				    opacity:0.6;"
				     type="submit" value="Search"/> 
        </form>
    </body>
</html>
