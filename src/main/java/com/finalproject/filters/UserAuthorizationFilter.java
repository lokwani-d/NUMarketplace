package com.finalproject.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAuthorizationFilter implements Filter {

	public UserAuthorizationFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request= (HttpServletRequest)req;
		Cookie[] cookies= request.getCookies();
		String user=null;
		String seller=null;
		if(cookies!=null) {
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals("user")) {
				user=(String) cookie.getValue();
			}
			if(cookie.getName().equals("seller")) {
				seller=(String) cookie.getValue();
			}
		}
		}
		if(user!=null) {
			request.getSession().setAttribute("user", user);
			user(request,res,user,chain);
			return;
		}
		else if(seller!=null) {
			request.getSession().setAttribute("user", seller);
			seller(request,res,seller,chain);
			return;
		}
		request.getSession().setAttribute("user", null);
		chain.doFilter(request, res);
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
	public void user(HttpServletRequest request,ServletResponse res,String user,FilterChain chain) throws IOException, ServletException {
		if(request.getServletPath().indexOf("seller")>0) {
			request.setAttribute("errormessage", "Please login as seller to access this route");
			RequestDispatcher rd= request.getRequestDispatcher("../error.htm");
			rd.forward(request, res);
			return;
		}
		boolean getAccount= request.getServletPath().equals("/user/account.htm");
		boolean deleteAccount= request.getServletPath().equals("/user/delete.htm");
		boolean updateProfile= request.getServletPath().equals("/user/updateProfile.htm");
		boolean logout= request.getServletPath().equals("/user/logout.htm");
		if(getAccount||updateProfile||deleteAccount||logout) {
			if(user==null) {
				RequestDispatcher rd= request.getRequestDispatcher("login.htm");
				rd.forward(request, res);
				return;
			}

		}
		chain.doFilter(request, res);
	}
	public void seller(HttpServletRequest request,ServletResponse res,String user,FilterChain chain) throws IOException, ServletException{
		if(request.getServletPath().indexOf("user")>0) {
//			System.out.println(request.getServletPath());
			request.setAttribute("errormessage", "Please login as user to access this route");
			RequestDispatcher rd= request.getRequestDispatcher("../error.htm");
			rd.forward(request, res);
			return;
		}
		boolean getAccount= request.getServletPath().equals("/seller/account.htm");
		boolean deleteAccount= request.getServletPath().equals("/seller/delete.htm");
		boolean updateProfile= request.getServletPath().equals("/seller/updateProfile.htm");
		boolean logout= request.getServletPath().equals("/seller/logout.htm");
		if(getAccount||updateProfile||deleteAccount||logout) {
			if(user==null) {
				RequestDispatcher rd= request.getRequestDispatcher("login.htm");
				rd.forward(request, res);
				return;
			}

		}
		chain.doFilter(request, res);
	}
}
