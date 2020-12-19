package com.finalproject.controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.io.File;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.finalproject.utils.UserValidator;
import com.finalproject.utils.SellerLoginValidator;
import com.finalproject.utils.SellerValidator;
import com.finalproject.utils.Hash;
import com.finalproject.utils.LoginValidator;
import com.finalproject.POJO.Seller;
import com.finalproject.POJO.User;
import com.finalproject.DAO.SellerDAO;
import com.finalproject.POJO.Person;
import com.finalproject.POJO.Product;
import com.finalproject.DAO.UserDAO;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

/**
 * 
 * @author Deepak_Lokwani
 * 
 *         NUID: 001316769
 * 
 *         Project name: Finalproject 
 *         Package name: com.finalproject.controllers
 *
 */

@Controller
public class SellerController {

	public SellerController() {
	}

	@Autowired
	SellerValidator sellerValidator;

	@Autowired
	SellerLoginValidator sellerLoginValidator;


	/**
	 * 
	 * @param model
	 * @param seller
	 * @param request
	 * @return
	 * 
	 * Method to get Login
	 */
	@RequestMapping(value = { "/seller/login.htm" }, method = RequestMethod.GET)
	private String getLogin(ModelMap model, Seller seller, HttpServletRequest request) {
		if (request.getSession().getAttribute("user") != null) {
			return "redirect: /edu/index.htm";
		}
		request.setAttribute("seller", "seller");
		model.addAttribute(seller);
		return "login";
	}

	/**
	 * 
	 * @param seller
	 * @param result
	 * @param status
	 * @param sellerdao
	 * @param request
	 * @param response
	 * @return
	 * 
	 *  Post Method to handle login
	 */
	@RequestMapping(value = { "/seller/login.htm" }, method = RequestMethod.POST)
	private String handleLogin(@ModelAttribute("seller") Seller seller, BindingResult result, SessionStatus status,
			SellerDAO sellerdao, HttpServletRequest request, HttpServletResponse response) {
		sellerLoginValidator.validate(seller, result);
		if (result.hasErrors())
			return "login";
		Person person = sellerdao.getSellerByEmail(seller.getEmail());
		if (person == null) {
			request.setAttribute("errormessage",
					"User with email " + seller.getEmail() + " is not found. Please signup!!");
			return "error";
		}
		if (!Hash.checkPasssword(seller.getPassword(), person.getPassword())) {
			request.setAttribute("errormessage", "Invalid usename or password");
			return "error";
		}
		createCookie(30 * 24 * 60 * 60, "seller", person.getEmail(), "/", response);
		createCookie(30 * 24 * 60 * 60, "name", person.getName().split(" ")[0], "/", response);
		return "redirect:" + "http://localhost:8080/edu/index.htm";
	}

	/**
	 * 
	 * @param model
	 * @param seller
	 * @param request
	 * @return
	 * 
	 * Method to get signup attrinutes
	 */
	@RequestMapping(value = { "/seller/signup.htm" }, method = RequestMethod.GET)
	private String getSignup(ModelMap model, Seller seller, HttpServletRequest request) {
		if (request.getSession().getAttribute("user") != null) {
			return "redirect: /edu/index.htm";
		}
		model.addAttribute(seller);
		return "sellerSignup";
	}

	/**
	 * 
	 * @param seller
	 * @param result
	 * @param status
	 * @param sellerdao
	 * @param request
	 * @param response
	 * @return
	 * 
	 * Method to handle signups
	 */
	@RequestMapping(value = { "/seller/signup.htm" }, method = RequestMethod.POST)
	private String handleSignup(@ModelAttribute("seller") Seller seller, BindingResult result, SessionStatus status,
			SellerDAO sellerdao, HttpServletRequest request, HttpServletResponse response) {
		sellerValidator.validate(seller, result);
		if (result.hasErrors())
			return "sellerSignup";
		
		/*
		 * Handling photo in signup
		 */
		CommonsMultipartFile photo = seller.getPhoto();
		if (photo.getSize() != 0) {
			String fileName = "img" + seller.getId() + Calendar.getInstance().getTimeInMillis()
					+ photo.getContentType();
			File file = new File("C:/Users/deepa/Desktop/images", fileName);
			seller.setPhotoFile(fileName);
			try {
				photo.transferTo(file);
			} catch (IllegalStateException e) {
				e.printStackTrace();
				return "error";
			} catch (IOException e) {
				e.printStackTrace();
				return "error";
			}
		}
		seller.setCreatedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		seller.setRole("seller");
		seller.setPassword(Hash.hashPassword(seller.getPassword()));

		
		if (!sellerdao.addSeller(seller)) {
			request.setAttribute("errormessage", "Error adding User please try again!!");
			return "error";
		}
		;

		/*
		 * creating a cookie
		 */
		createCookie(30 * 24 * 60 * 60, "seller", seller.getEmail(), "/", response);
		createCookie(30 * 24 * 60 * 60, "name", seller.getName().split(" ")[0], "/", response);

		// clean up the session
		status.setComplete();
		return "redirect:" + "http://localhost:8080/edu/index.htm";
	}


	/**
	 * 
	 * @param request
	 * @return
	 * 
	 * method to delete user
	 */
	@RequestMapping(value = "/seller/delete.htm", method = RequestMethod.GET)
	public String deleteUser(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			request.setAttribute("errormessage", "Login before continuing");
			return "error";
		}
		System.out.println((String) request.getSession().getAttribute("user"));
		request.setAttribute("user", "seller");
		return "removeUser";
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param sellerdao
	 * @return 
	 * method to handle delete user
	 */
	@RequestMapping(value = "/seller/delete.htm", method = RequestMethod.POST)
	public String handleDelete(HttpServletRequest request, HttpServletResponse response, SellerDAO sellerdao) {
		Seller person = sellerdao.getSellerByEmail(request.getParameter("email"));
		if (person == null) {
			request.setAttribute("errormessage",
					"User with email " + request.getParameter("email") + " is not found. Please verify!!");
			return "error";
		}
		if (!sellerdao.deleteSeller(person)) {
			request.setAttribute("errormessage", "Error deleting seller!!");
			return "error";
		}
		deleteCookie(request, response);
		return "redirect:" + "http://localhost:8080/edu/index.htm";
	}

	/**
	 * method to get seller account details
	 * @param request
	 * @param sellerdao
	 * @return
	 */
	@RequestMapping(value = "/seller/account.htm", method = RequestMethod.GET)
	public String account(HttpServletRequest request, SellerDAO sellerdao) {
		if (request.getSession().getAttribute("user") == null) {
			request.setAttribute("errormessage", "Login before continuing");
			return "error";
		}
		Person person = sellerdao.getSellerByEmail((String) request.getSession().getAttribute("user"));
		request.setAttribute("user", person);
		return "sellerProfile";
	}

	/**
	 * method to update the seller profile
	 * @param userDao
	 * @param req
	 * @param res
	 * @param sellerDao
	 * @return
	 */
	@RequestMapping(value = "/seller/updateProfile.htm", method = RequestMethod.POST)
	public String handleUpdateProfile(UserDAO userDao, HttpServletRequest req, HttpServletResponse res,
			SellerDAO sellerDao) {
		Person person = sellerDao.getSellerByEmail((String) req.getSession().getAttribute("user"));
		if (userDao.updatePerson(person, req.getParameter("name"), req.getParameter("email"))) {
			createCookie(30 * 24 * 60 * 60, "seller", person.getEmail(), "/", res);
			createCookie(30 * 24 * 60 * 60, "name", person.getName().split(" ")[0], "/", res);
			return "redirect:" + "http://localhost:8080/edu/index.htm";

		} else {
			req.setAttribute("errormessage", "Error updating your profile. Please try with different email");
			return "error";
		}
	}
	
	/**
	 * Method to get the products of a seller
	 * @param req
	 * @param sellerDao
	 * @return
	 */
	@RequestMapping(value = "/seller/product.htm", method = RequestMethod.GET)
	@ResponseBody
	public String getProducts(HttpServletRequest req, SellerDAO sellerDao) {
		if (req.getSession().getAttribute("user") == null) {
			req.setAttribute("errormessage", "Please login before continuing");
			return "Login before continuing";
		}
		Seller seller = sellerDao.getSellerByEmail((String) req.getSession().getAttribute("user"));

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(seller.getProducts());
	}
	
	/**
	 * method to get orders of a seller
	 * @param request
	 * @param sellerdao
	 * @return
	 */
	@RequestMapping(value = "/seller/getOrders.htm", method = RequestMethod.GET)
	public String getOrders(HttpServletRequest request, SellerDAO sellerdao) {
		if (request.getSession().getAttribute("user") == null) {
			request.setAttribute("errormessage", "Login before continuing!!");
			return "error";
		}
		Seller seller = sellerdao.getSellerByEmail((String) request.getSession().getAttribute("user"));
		if (seller == null) {
			request.setAttribute("errormessage", "User does not exist");
			return "error";
		}
		request.setAttribute("orders", seller.getOrders());
		return "showOrders";
	}

	/**
	 * method to create a cookie for a session
	 * @param time
	 * @param name
	 * @param msg
	 * @param path
	 * @param response
	 */
	public void createCookie(int time, String name, String msg, String path, HttpServletResponse response) {
		Cookie c = new Cookie(name, msg);
		c.setMaxAge(time);
		c.setPath(path);
		response.addCookie(c);
	}

	/**
	 * method to delete a cookie after the logout
	 * @param request
	 * @param response
	 */
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
