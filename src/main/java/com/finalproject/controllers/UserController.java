package com.finalproject.controllers;


import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.stereotype.Controller;

import com.finalproject.utils.LoginValidator;
import com.finalproject.utils.OrderValidator;
import com.finalproject.utils.Mail;
import com.finalproject.utils.RandomTokenGenerator;
import com.finalproject.utils.UserValidator;
import com.finalproject.POJO.User;
import com.finalproject.POJO.Seller;
import com.finalproject.POJO.Product;
import com.finalproject.POJO.Person;
import com.finalproject.DAO.OrderDAO;
import com.finalproject.POJO.Order;
import com.finalproject.DAO.UserDAO;
import com.finalproject.DAO.SellerDAO;
import com.finalproject.DAO.ProductDAO;
import com.finalproject.utils.Hash;

import java.util.HashSet;

import java.util.Map;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import java.util.Calendar;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

	@Autowired
	UserValidator validator;
	@Autowired
	LoginValidator loginVaidator;
	@Autowired
	OrderValidator orderValidator;

	/**
	 * Method to get login
	 * 
	 * @param model
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/user/login.htm" }, method = RequestMethod.GET)
	private String getLogin(ModelMap model, User user, HttpServletRequest request) {

		if (request.getSession().getAttribute("user") != null) {
			return "redirect: /edu/index.htm";
		}
		model.addAttribute(user);

		return "login";
	}

	/**
	 * Method to handle login of a user
	 * 
	 * @param user
	 * @param bindingResult
	 * @param status
	 * @param userDao
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = { "/user/login.htm" }, method = RequestMethod.POST)
	private String handleLogin(@ModelAttribute("user") User user, BindingResult bindingResult, SessionStatus status,
			UserDAO userDao, HttpServletRequest req, HttpServletResponse res) {

		loginVaidator.validate(user, bindingResult);
		if (bindingResult.hasErrors())
			return "login";
		Person person = userDao.getUserByEmail(user.getEmail());
		if (person == null) {
			req.setAttribute("errormessage",
					"User with following email " + user.getEmail() + " is not found. Please signup and try again!!");
			return "error";
		}
		if (!Hash.checkPasssword(user.getPassword(), person.getPassword())) {
			req.setAttribute("errormessage", "Invalid usename or password! Try again!");
			return "error";
		}
		person.getPassword();
		createCookie(30 * 24 * 60 * 60, "user", person.getEmail(), "/", res);
		createCookie(30 * 24 * 60 * 60, "name", person.getName().split(" ")[0], "/", res);
		status.setComplete();
		return "redirect:" + "http://localhost:8080/edu/index.htm";
	}

	/**
	 * method to get the signup attributes of a user
	 * 
	 * @param model
	 * @param user
	 * @param req
	 * @return
	 */
	@RequestMapping(value = { "/user/signup.htm" }, method = RequestMethod.GET)
	private String getSignup(ModelMap model, User user, HttpServletRequest req) {

		if (req.getSession().getAttribute("user") != null) {
			return "redirect: /edu/index.htm";
		}
		model.addAttribute(user);
		return "signup";
	}

	/**
	 * method to handle the sign up of a user
	 * 
	 * @param user
	 * @param bindingResult
	 * @param status
	 * @param userdao
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = { "/user/signup.htm" }, method = RequestMethod.POST)
	private String handleSignup(@ModelAttribute("user") User user, BindingResult bindingResult, SessionStatus status,
			UserDAO userdao, HttpServletRequest req, HttpServletResponse res) {

		validator.validate(user, bindingResult);
		if (bindingResult.hasErrors())
			return "signup";

		/**
		 * handle the photo upload
		 */
		CommonsMultipartFile iconPhoto = user.getPhoto();
		if (iconPhoto.getSize() != 0) {
			String picfileName = "img" + user.getId() + Calendar.getInstance().getTimeInMillis()
					+ iconPhoto.getContentType();
			File file = new File("C:/Users/deepa/Desktop/images", picfileName);
			user.setPhotoFile(picfileName);
			try {
				iconPhoto.transferTo(file);
			} catch (IllegalStateException e) {
				e.printStackTrace();
				return "error";
			} catch (IOException e) {
				e.printStackTrace();
				return "error";
			}
		}
		user.setCreatedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		user.setRole("user");
		user.setPassword(Hash.hashPassword(user.getPassword()));

		/**
		 * putting model in my request scope
		 */
		if (!userdao.addUser(user)) {
			req.setAttribute("errormessage", "Please try again with different email!! Error adding User ");
			return "error";
		}
		;

		/**
		 * creating a session based on cookies
		 */
		createCookie(30 * 24 * 60 * 60, "user", user.getEmail(), "/", res);
		createCookie(30 * 24 * 60 * 60, "name", user.getName().split(" ")[0], "/", res);

		/**
		 * cleaning up a session
		 */
		status.setComplete();
		return "redirect:" + "http://localhost:8080/edu/index.htm";
	}

	/**
	 * method to delete a user
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/user/delete.htm", method = RequestMethod.GET)
	public String deleteUser(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") == null) {
			request.setAttribute("errormessage", "Login before continuing");
			return "error";
		}
		request.setAttribute("user", "user");
		return "removeUser";
	}

	/**
	 * method to delete a user
	 * 
	 * @param request
	 * @param response
	 * @param userDao
	 * @return
	 */
	@RequestMapping(value = "/user/delete.htm", method = RequestMethod.POST)
	public String handleDelete(HttpServletRequest request, 
			HttpServletResponse response, 
			UserDAO userDao) {
		Person person = userDao.getUserByEmail(request.getParameter("email"));
		if (person == null) {
			request.setAttribute("errormessage",
					"User with email " + request.getParameter("email") + " is not found. Please verify!!");
			return "error";
		}
		userDao.deletePerson(person);
		deleteCookie(request, response);
		return "redirect:" + "http://localhost:8080/edu/index.htm";
	}

	/**
	 * method to get an account of a user
	 * 
	 * @param request
	 * @param userdao
	 * @return
	 */
	@RequestMapping(value = "/user/account.htm", method = RequestMethod.GET)
	public String account(HttpServletRequest request, UserDAO userdao) {
		if (request.getSession().getAttribute("user") == null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		Person person = userdao.getUserByEmail((String) request.getSession().getAttribute("user"));
		if (person == null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		request.setAttribute("user", person);
		return "userprofile";
	}

	/**
	 * method to update a profile for the user
	 * 
	 * @param userdao
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/user/updateProfile.htm", method = RequestMethod.POST)
	public String handleUpdateProfile(UserDAO userdao, HttpServletRequest request, HttpServletResponse response) {
		Person person = userdao.getUserByEmail((String) request.getSession().getAttribute("user"));
		if ((request.getParameter("name").length() == 0 || request.getParameter("email").length() == 0)) {
			request.setAttribute("errormessage", "Error updating your profile. Please insert valid inputs");
			return "error";

		}
		if (userdao.updatePerson(person, request.getParameter("name"), request.getParameter("email"))) {
			createCookie(30 * 24 * 60 * 60, "user", person.getEmail(), "/", response);
			createCookie(30 * 24 * 60 * 60, "name", person.getName().split(" ")[0], "/", response);
			return "redirect:" + "http://localhost:8080/edu/index.htm";

		} else {
			request.setAttribute("errormessage", "Error updating your profile. Please try with different email");
			return "error";
		}
	}

	/**
	 * method to handle a logout of a user
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/logout.htm", method = RequestMethod.GET)
	public String handleLogout(HttpServletRequest request, HttpServletResponse response) {
		deleteCookie(request, response);
		return "redirect:" + "http://localhost:8080/edu/index.htm";
	}

	/**
	 * method to get cart of a current user
	 * 
	 * @param request
	 * @param userdao
	 * @return
	 */
	@RequestMapping(value = "/user/cart.htm", method = RequestMethod.GET)
	public String getCart(HttpServletRequest request, UserDAO userdao) {
		if (request.getSession().getAttribute("user") == null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		User user = userdao.getUserByEmail((String) request.getSession().getAttribute("user"));
		request.setAttribute("cart", user.getCart());
		request.setAttribute("total", calculateCartTotal(user.getCart()));
		return "cart";
	}

	/**
	 * method to calculate the total amount
	 * 
	 * @param cart
	 * @return
	 */
	public float calculateCartTotal(Map<Product, Integer> cart) {
		float sum = 0;
		for (Product prod : cart.keySet()) {
			sum += (prod.getPrice() * cart.get(prod));
		}
		return sum;
	}

	/**
	 * method to handle a checkout of an order for an user
	 * 
	 * @param request
	 * @param userdao
	 * @param model
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/user/checkout.htm", method = RequestMethod.GET)
	public String getCheckOut(HttpServletRequest request, UserDAO userdao, ModelMap model, Order order) {
		if (request.getSession().getAttribute("user") == null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		User user = userdao.getUserByEmail((String) request.getSession().getAttribute("user"));
		if (user.getCart().size() == 0) {
			request.setAttribute("errormessage",
					"You have nothing in your cart. Please add to cart and proceed to checkout!!");
			return "error";
		}
		model.addAttribute(order);
		request.setAttribute("total", calculateCartTotal(user.getCart()));
		return "checkout";
	}

	/**
	 * method to process an order checkout based on the order detail object
	 * 
	 * @param order
	 * @param result
	 * @param status
	 * @param request
	 * @param userdao
	 * @param productdao
	 * @param sellerdao
	 * @param orderdao
	 * @return
	 */
	@RequestMapping(value = "/user/checkout.htm", method = RequestMethod.POST)
	public String processCheckout(@ModelAttribute("order") Order order, BindingResult result, SessionStatus status,
			HttpServletRequest request, UserDAO userdao, ProductDAO productdao, SellerDAO sellerdao,
			OrderDAO orderdao) {
		orderValidator.validate(order, result);
		if (result.hasErrors()) {
			return "checkout";
		}
		User user = userdao.getUserByEmail((String) request.getSession().getAttribute("user"));

		/**
		 * checking if all the quantity are available
		 */
		System.out.println(user.getCart().size() + "first!!!!!!!");
		for (Product product : user.getCart().keySet()) {
			Product prod = productdao.getProductById(product.getProduct_id());
			if (prod.getQuantity() < user.getCart().get(product)) {
				request.setAttribute("errormessage",
						product.getName() + "is not available in given quantity.Please update your cart");
				return "error";
			}
		}

		/**
		 * creating an order object
		 */
		order.setOrderedDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		order.setUser(user);
		Set<Seller> sellerList = new HashSet<Seller>();
		for (Product product : user.getCart().keySet()) {
			Seller seller = product.getSeller();
			productdao.updateProductQuantity(product, user.getCart().get(product));
			sellerList.add(seller);
		}
		order.setSellers(sellerList);
		order.setProducts(user.getCart());
		System.out.println(user.getCart().size() + "second!!!!!!!");
		order.setTotalPrice(calculateCartTotal(user.getCart()));
		if (!orderdao.addOrder(order)) {
			request.setAttribute("errormessage", "Error placing order");
			return "error";
		}

		// Clear cart on checkout
		userdao.clearCart(user);

		/**
		 * order added to the user, seller and the database persisted
		 */

		return "redirect: /edu/index.htm";
	}

	/**
	 * method to get user orders
	 * 
	 * @param request
	 * @param userdao
	 * @return
	 */
	@RequestMapping(value = "/user/getOrders.htm", method = RequestMethod.GET)
	public String getUserOrders(HttpServletRequest request, UserDAO userdao) {
		if (request.getSession().getAttribute("user") == null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		User user = userdao.getUserByEmail((String) request.getSession().getAttribute("user"));
		if (user == null) {
			request.setAttribute("errormessage",
					"User" + (String) request.getSession().getAttribute("user") + "does not exist");
			return "error";
		}
		request.setAttribute("orders", user.getOrders());
		return "showOrders";
	}

	/**
	 * method to create a session using a cookie
	 * 
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
	 * method to delete a cookie when logged out
	 * 
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

	/**
	 * method to email the user to reset the password when they forget the password
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/resetPassword.htm", method = RequestMethod.GET)
	private String getEmailToResetPage(HttpServletRequest request) {
		if (request.getSession().getAttribute("user") != null) {
			request.setAttribute("errormessage", "Please logout to reset password");
			return "error";
		}
		return "emailToReset";
	}

	/**
	 * method to invoke an smtp server
	 * 
	 * @param request
	 * @param response
	 * @param userdao
	 * @return
	 */
	@RequestMapping(value = "/resetPassword.htm", method = RequestMethod.POST)
	private String sendMailForReset(HttpServletRequest request, HttpServletResponse response, UserDAO userdao) {
		if (request.getSession().getAttribute("user") != null) {
			request.setAttribute("errormessage", "Please logout to reset password");
			return "error";
		}
		String email = request.getParameter("email");
		Person person = userdao.getPersonByEmail(email);
		if (person == null) {
			request.setAttribute("errormessage", "User does not exist");
			return "error";
		} else {
			Mail mail = new Mail(email);
			String subject = "Reset Password";
			String token = RandomTokenGenerator.gernerate();
			person.setResetToken(token);
			person.setResetExpiresAt(new Timestamp(Calendar.getInstance().getTimeInMillis() + (10 * 60 * 1000)));
			userdao.mergePerson(person);
			mail.sendMail(subject, token);
		}
		return "redirect: /edu/index.htm";
	}

	/**
	 * method to get a reset password call request
	 * 
	 * @param request
	 * @param response
	 * @param userdao
	 * @return
	 */
	@RequestMapping(value = "/updatePassword.htm", method = RequestMethod.GET)
	private String getResetPassword(HttpServletRequest request, HttpServletResponse response, UserDAO userdao) {
		if (request.getSession().getAttribute("user") != null) {
			request.setAttribute("errormessage", "Please logout to reset password");
			return "error";
		}
		if (request.getParameter("token") == null) {
			request.setAttribute("errormessage", "No token found");
			return "error";
		}
		String token = (String) request.getParameter("token");
		Person person = userdao.getPersonByResetToken(token);
		if (person == null) {
			request.setAttribute("errormessage", "Invalid token");
			return "error";
		}
		Timestamp currentTs = new Timestamp(Calendar.getInstance().getTimeInMillis());
		if (currentTs.compareTo(person.getResetExpiresAt()) <= 0) {
			request.setAttribute("email", person.getEmail());
			return "resetPassword";
		} else {
			request.setAttribute("errormessage", "Token expired");
			return "error";
		}
	}

	/**
	 * method to update password of a user
	 * 
	 * @param request
	 * @param response
	 * @param userdao
	 * @return
	 */
	@RequestMapping(value = "/updatePassword.htm", method = RequestMethod.POST)
	private String updatePassword(HttpServletRequest request, HttpServletResponse response, UserDAO userdao) {
		String password = request.getParameter("pword");
		String confirmPassword = request.getParameter("cpword");
		if (!password.equals(confirmPassword)) {
			request.setAttribute("errormessage", "passwords does not match");
			return "error";
		}
		password = Hash.hashPassword(password);
		Person person = userdao.getPersonByEmail(request.getParameter("email"));
		if (person == null) {
			request.setAttribute("errormessage", "Invalid email");
			return "error";
		}
		person.setPassword(password);
		person.setResetExpiresAt(null);
		person.setResetToken(null);
		if (userdao.mergePerson(person))
			return "redirect:" + "http://localhost:8080/edu/index.htm";
		else {
			request.setAttribute("errormessage", "Error updating email, please try again");
			return "error";
		}
	}

}