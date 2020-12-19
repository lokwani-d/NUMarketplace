package com.finalproject.controllers;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.finalproject.DAO.SellerDAO;
import com.finalproject.DAO.UserDAO;
import com.finalproject.POJO.Person;
import com.finalproject.POJO.Product;
import com.finalproject.POJO.Seller;
import com.finalproject.POJO.User;
import com.finalproject.utils.Hash;
import com.finalproject.utils.LoginValidator;
import com.finalproject.utils.SellerLoginValidator;
import com.finalproject.utils.SellerValidator;
import com.finalproject.utils.UserValidator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @author Deepak_Lokwani
 * 
 * NUID: 001316769
 * 
 * Project name: Finalproject
 * Package name: com.finalproject.controllers
 *
 */

@Controller
public class SellerController {
	
	
	public SellerController() {
		// TODO Auto-generated constructor stub
		
	}
	
	@Autowired
	SellerValidator sellerValidator;
	
	@Autowired
	SellerLoginValidator sellerLoginValidator;
	
	// Get Login
	@RequestMapping(value = {"/seller/login.htm"}, method = RequestMethod.GET)
	private String getLogin(ModelMap model,Seller seller,HttpServletRequest request) {
		if(request.getSession().getAttribute("user")!=null) {
			return "redirect: /edu/index.htm";
		}
//		command object
		request.setAttribute("seller", "seller");
		model.addAttribute(seller);	
		return "login";
	}
	
	// Handle Login
	@RequestMapping(value = {"/seller/login.htm"}, method = RequestMethod.POST)
	private String handleLogin(@ModelAttribute("seller") Seller seller,BindingResult result,SessionStatus status,SellerDAO sellerdao,HttpServletRequest request,HttpServletResponse response) {
		sellerLoginValidator.validate(seller, result);
		if(result.hasErrors())
			return "login";
//		System.out.println(user.getEmail());
		Person person= sellerdao.getSellerByEmail(seller.getEmail());
		if(person==null)
		{
			request.setAttribute("errormessage", "User with email "+seller.getEmail()+" is not found. Please signup!!");
			return "error";
		}
		if(!Hash.checkPasssword(seller.getPassword(), person.getPassword())) {
			request.setAttribute("errormessage", "Invalid usename or password");
			return "error";
		}
		createCookie(30*24*60*60, "seller", person.getEmail(), "/", response);
		createCookie(30*24*60*60, "name", person.getName().split(" ")[0], "/", response);
		return "redirect:" + "http://localhost:8080/edu/index.htm";
	}

	// Get signup
	@RequestMapping(value = {"/seller/signup.htm"}, method = RequestMethod.GET)
	private String getSignup(ModelMap model,Seller seller,HttpServletRequest request) {
		if(request.getSession().getAttribute("user")!=null) {
			return "redirect: /edu/index.htm";
		}
//		command object
		model.addAttribute(seller);
		return "sellerSignup";
	}

	// Handle signup
	@RequestMapping(value = {"/seller/signup.htm"}, method = RequestMethod.POST)
	private String handleSignup(@ModelAttribute("seller") Seller seller,BindingResult result,SessionStatus status,SellerDAO sellerdao,HttpServletRequest request,HttpServletResponse response) {
//		Validate 
		
		sellerValidator.validate(seller, result);
		if(result.hasErrors())
			return "sellerSignup";
//		Handle Photo
		CommonsMultipartFile photo= seller.getPhoto();
		if(photo.getSize()!=0) {
		String fileName="img"+seller.getId()+Calendar.getInstance().getTimeInMillis()+photo.getContentType();
		File file= new File("C:/Users/deepa/Desktop/images",fileName);
		seller.setPhotoFile(fileName);
		try {
			photo.transferTo(file);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
		}
		seller.setCreatedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		
			seller.setRole("seller");
		seller.setPassword(Hash.hashPassword(seller.getPassword()));

//		Model is in request scope
		if(!sellerdao.addSeller(seller)) {
			request.setAttribute("errormessage", "Error adding User please try again!!");
			return "error";
		};
		
		createCookie(30*24*60*60, "seller", seller.getEmail(), "/", response);
		createCookie(30*24*60*60, "name", seller.getName().split(" ")[0], "/", response);
		
//		clean up session
		status.setComplete();
		return "redirect:" + "http://localhost:8080/edu/index.htm";
	}
//	Get delete form
	@RequestMapping(value="/seller/delete.htm",method=RequestMethod.GET)
	public String deleteUser(HttpServletRequest request) {
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage", "Login before continuing");
			return "error";
		}
		System.out.println((String)request.getSession().getAttribute("user"));
		request.setAttribute("user", "seller");
		return "removeUser";
	}
//	Handle delete
	@RequestMapping(value="/seller/delete.htm",method=RequestMethod.POST)
	public String handleDelete(HttpServletRequest request, HttpServletResponse response,SellerDAO sellerdao) {
		Seller person= sellerdao.getSellerByEmail(request.getParameter("email"));
		if(person==null)
		{
			request.setAttribute("errormessage", "User with email "+request.getParameter("email")+" is not found. Please verify!!");
			return "error";
		}
		if(!sellerdao.deleteSeller(person)) {
			request.setAttribute("errormessage", "Error deleting seller!!");
			return "error";
		}
		deleteCookie(request, response);
		return "redirect:" + "http://localhost:8080/edu/index.htm";
	}
//	Get Account
	@RequestMapping(value="/seller/account.htm",method=RequestMethod.GET)
	public String account(HttpServletRequest request,SellerDAO sellerdao) {
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage", "Login before continuing");
			return "error";
		}
		Person person= sellerdao.getSellerByEmail((String)request.getSession().getAttribute("user"));
		request.setAttribute("user",person);
		return "sellerProfile";
	}
//	Update Profile
	@RequestMapping(value="/seller/updateProfile.htm",method=RequestMethod.POST)
	public String handleUpdateProfile(UserDAO userdao,HttpServletRequest request,HttpServletResponse response,SellerDAO sellerdao) {
		Person person= sellerdao.getSellerByEmail((String)request.getSession().getAttribute("user"));
		if(userdao.updatePerson(person, request.getParameter("name"), request.getParameter("email")))
		{
			createCookie(30*24*60*60, "seller", person.getEmail(), "/", response);
			createCookie(30*24*60*60, "name", person.getName().split(" ")[0], "/", response);
//			request.getSession().setAttribute(name, value);
			return "redirect:" + "http://localhost:8080/edu/index.htm";
		
		}
		else {
			request.setAttribute("errormessage", "Error updating your profile. Please try with different email");
			return "error";
		}
	}
	@RequestMapping(value="/seller/product.htm", method = RequestMethod.GET)
	@ResponseBody
	public String getProducts(HttpServletRequest request,SellerDAO sellerdao){
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "Login before continuing";
		}
		Seller seller= sellerdao.getSellerByEmail((String)request.getSession().getAttribute("user"));

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(seller.getProducts());
	}
	
	@RequestMapping(value="/seller/getOrders.htm",method=RequestMethod.GET)
	public String getOrders(HttpServletRequest request,SellerDAO sellerdao) {
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage", "Login before continuing!!");
			return "error";
		}
		Seller seller= sellerdao.getSellerByEmail((String)request.getSession().getAttribute("user"));
		if(seller==null) {
			request.setAttribute("errormessage", "User does not exist");
			return "error";
		}
		request.setAttribute("orders", seller.getOrders());
		return "showOrders";
	}

	
//	Create cookie
	public void createCookie(int time, String name, String msg, String path, HttpServletResponse response) {
		Cookie c = new Cookie(name, msg);
		c.setMaxAge(time);
		c.setPath(path);
		response.addCookie(c);
	}
//	Delete Cookie
	private void deleteCookie(HttpServletRequest request, HttpServletResponse response) {
	Cookie[] cookies = request.getCookies();
	for (Cookie cookie : cookies) {
		cookie.setPath("/");
		cookie.setValue("");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
	}
	
}
