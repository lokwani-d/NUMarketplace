package com.finalproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.finalproject.DAO.OrderDAO;
import com.finalproject.DAO.ProductDAO;
import com.finalproject.DAO.SellerDAO;
import com.finalproject.DAO.UserDAO;
import com.finalproject.POJO.Order;
import com.finalproject.POJO.Person;
import com.finalproject.POJO.Product;
import com.finalproject.POJO.Seller;
import com.finalproject.POJO.User;
import com.finalproject.utils.Hash;
import com.finalproject.utils.LoginValidator;
import com.finalproject.utils.Mail;
import com.finalproject.utils.OrderValidator;
import com.finalproject.utils.RandomTokenGenerator;
import com.finalproject.utils.UserValidator;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	// Get Login
	@RequestMapping(value = {"/user/login.htm"}, method = RequestMethod.GET)
	private String getLogin(ModelMap model,User user,HttpServletRequest request) {
//		command object
		if(request.getSession().getAttribute("user")!=null) {
			return "redirect: /edu/index.htm";
		}
		model.addAttribute(user);	
		
		return "login";
	}
	
	// Handle Login
	@RequestMapping(value = {"/user/login.htm"}, method = RequestMethod.POST)
	private String handleLogin(@ModelAttribute("user") User user,BindingResult result,SessionStatus status,UserDAO userdao,HttpServletRequest request,HttpServletResponse response) {
		loginVaidator.validate(user, result);
		if(result.hasErrors())
			return "login";
//		System.out.println(request.getServletPath().indexOf("user"));
		Person person= userdao.getUserByEmail(user.getEmail());
		if(person==null)
		{
			request.setAttribute("errormessage", "User with email "+user.getEmail()+" is not found. Please signup!!");
			return "error";
		}
		if(!Hash.checkPasssword(user.getPassword(), person.getPassword())) {
			request.setAttribute("errormessage", "Invalid usename or password");
			return "error";
		}
		person.getPassword();
		createCookie(30*24*60*60, "user", person.getEmail(), "/", response);
		createCookie(30*24*60*60, "name", person.getName().split(" ")[0], "/", response);
		status.setComplete();
		return "redirect:" + "http://localhost:8080/edu/index.htm";
	}

	// Get signup
	@RequestMapping(value = {"/user/signup.htm"}, method = RequestMethod.GET)
	private String getSignup(ModelMap model,User user,HttpServletRequest request) {
		
//		command object
		if(request.getSession().getAttribute("user")!=null) {
			return "redirect: /edu/index.htm";
		}
		model.addAttribute(user);
		return "signup";
	}

	// Handle signup
	@RequestMapping(value = {"/user/signup.htm"}, method = RequestMethod.POST)
	private String handleSignup(@ModelAttribute("user") User user,BindingResult result,SessionStatus status,UserDAO userdao,HttpServletRequest request,HttpServletResponse response) {
//		Validate 
		
		validator.validate(user, result);
		if(result.hasErrors())
			return "signup";
//		Handle Photo
		CommonsMultipartFile photo= user.getPhoto();
		if(photo.getSize()!=0) {
		String fileName="img"+user.getId()+Calendar.getInstance().getTimeInMillis()+photo.getContentType();
		File file= new File("C:/Users/deepa/Desktop/images",fileName);
		user.setPhotoFile(fileName);
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
		user.setCreatedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		
			user.setRole("user");
		user.setPassword(Hash.hashPassword(user.getPassword()));

//		Model is in request scope
		if(!userdao.addUser(user)) {
			request.setAttribute("errormessage", "Error adding User please try again with different email!!");
			return "error";
		};
		
		createCookie(30*24*60*60, "user", user.getEmail(), "/", response);
		createCookie(30*24*60*60, "name", user.getName().split(" ")[0], "/", response);
		
//		clean up session
		status.setComplete();
		return "redirect:" + "http://localhost:8080/edu/index.htm";
	}
//	Get delete form
	@RequestMapping(value="/user/delete.htm",method=RequestMethod.GET)
	public String deleteUser(HttpServletRequest request) {
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage", "Login before continuing");
			return "error";
		}
		request.setAttribute("user", "user");
		return "removeUser";
	}
//	Handle delete
	@RequestMapping(value="/user/delete.htm",method=RequestMethod.POST)
	public String handleDelete(HttpServletRequest request, HttpServletResponse response,UserDAO userdao) {
		Person person= userdao.getUserByEmail(request.getParameter("email"));
		if(person==null)
		{
			request.setAttribute("errormessage", "User with email "+request.getParameter("email")+" is not found. Please verify!!");
			return "error";
		}
		userdao.deletePerson(person);
		deleteCookie(request, response);
		return "redirect:" + "http://localhost:8080/edu/index.htm";
	}
//	Get Account
	@RequestMapping(value="/user/account.htm",method=RequestMethod.GET)
	public String account(HttpServletRequest request,UserDAO userdao) {
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		Person person= userdao.getUserByEmail((String)request.getSession().getAttribute("user"));
		if(person==null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		request.setAttribute("user",person);
		return "userprofile";
	}
//	Update Profile
	@RequestMapping(value="/user/updateProfile.htm",method=RequestMethod.POST)
	public String handleUpdateProfile(UserDAO userdao,HttpServletRequest request,HttpServletResponse response) {
		Person person= userdao.getUserByEmail((String)request.getSession().getAttribute("user"));
		if((request.getParameter("name").length()==0||request.getParameter("email").length()==0)){
			request.setAttribute("errormessage", "Error updating your profile. Please insert valid inputs");
			return "error";

		}
		if(userdao.updatePerson(person, request.getParameter("name"), request.getParameter("email")))
		{
			createCookie(30*24*60*60, "user", person.getEmail(), "/", response);
			createCookie(30*24*60*60, "name", person.getName().split(" ")[0], "/", response);
//			request.getSession().setAttribute(name, value);
			return "redirect:" + "http://localhost:8080/edu/index.htm";
		
		}
		else {
			request.setAttribute("errormessage", "Error updating your profile. Please try with different email");
			return "error";
		}
	}
	@RequestMapping(value="/logout.htm",method=RequestMethod.GET)
	public String handleLogout(HttpServletRequest request,HttpServletResponse response) {
		deleteCookie(request, response);
		return "redirect:" + "http://localhost:8080/edu/index.htm";
	}
	@RequestMapping(value="/user/cart.htm",method=RequestMethod.GET)
	public String getCart(HttpServletRequest request,UserDAO userdao) {
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		User user= userdao.getUserByEmail((String)request.getSession().getAttribute("user"));
		request.setAttribute("cart", user.getCart());
		request.setAttribute("total", calculateCartTotal(user.getCart()));
		return "cart";
	}
	public float calculateCartTotal(Map<Product,Integer> cart) {
		float sum=0;
		for(Product prod : cart.keySet()) {
			sum+=(prod.getPrice()*cart.get(prod));
		}
		return sum;
	}
	@RequestMapping(value="/user/checkout.htm",method=RequestMethod.GET)
	public String getCheckOut(HttpServletRequest request,UserDAO userdao,ModelMap model,Order order) {
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		User user= userdao.getUserByEmail((String)request.getSession().getAttribute("user"));
		if(user.getCart().size()==0) {
			request.setAttribute("errormessage","You have nothing in your cart. Please add to cart and proceed to checkout!!");
			return "error";
		}
		model.addAttribute(order);
		request.setAttribute("total", calculateCartTotal(user.getCart()));
		return "checkout";
	}
	@RequestMapping(value="/user/checkout.htm",method=RequestMethod.POST)
	public String processCheckout(@ModelAttribute("order") Order order,BindingResult result,SessionStatus status,HttpServletRequest request,UserDAO userdao,ProductDAO productdao,SellerDAO sellerdao,OrderDAO orderdao) {
		orderValidator.validate(order, result);
		if(result.hasErrors()) {
			return "checkout";
		}
		User user= userdao.getUserByEmail((String)request.getSession().getAttribute("user"));
//		Check all the products are available with the given quantity
		System.out.println(user.getCart().size()+"first!!!!!!!");
		for(Product product: user.getCart().keySet()) {
			Product prod= productdao.getProductById(product.getProduct_id());
			if(prod.getQuantity()<user.getCart().get(product)) {
				request.setAttribute("errormessage", product.getName()+"is not available in given quantity.Please update your cart");
				return "error";
			}
		}
//		Create order object
		order.setOrderedDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		order.setUser(user);
		Set<Seller> sellerList= new HashSet<Seller>();
		for(Product product:user.getCart().keySet()) {
			Seller seller= product.getSeller();
			productdao.updateProductQuantity(product, user.getCart().get(product));
			sellerList.add(seller);
		}
		order.setSellers(sellerList);
		order.setProducts(user.getCart());
		System.out.println(user.getCart().size()+"second!!!!!!!");
		order.setTotalPrice(calculateCartTotal(user.getCart()));
		if(!orderdao.addOrder(order)) {
			request.setAttribute("errormessage", "Error placing order");
			return "error";
		}
//		Clear cart
		userdao.clearCart(user);
		
//		Add order to user
//		Add order to seller
//		Persist order
		
		return "redirect: /edu/index.htm";
	}
	@RequestMapping(value="/user/getOrders.htm",method=RequestMethod.GET)
	public String getUserOrders(HttpServletRequest request,UserDAO userdao) {
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		User user= userdao.getUserByEmail((String)request.getSession().getAttribute("user"));
		if(user==null) {
			request.setAttribute("errormessage", "User"+(String)request.getSession().getAttribute("user")+"does not exist");
			return "error";
		}
		request.setAttribute("orders", user.getOrders());
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
	@RequestMapping(value="/resetPassword.htm",method=RequestMethod.GET)
	private String getEmailToResetPage(HttpServletRequest request){
		if(request.getSession().getAttribute("user")!=null) {
			request.setAttribute("errormessage", "Please logout to reset password");
			return "error";
		}
		return "emailToReset";
	}

//
	@RequestMapping(value="/resetPassword.htm",method=RequestMethod.POST)
	private String sendMail(HttpServletRequest request, HttpServletResponse response,UserDAO userdao) {
		if(request.getSession().getAttribute("user")!=null) {
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
			person.setResetExpiresAt(new Timestamp(Calendar.getInstance().getTimeInMillis()+(10*60*1000)));
			userdao.mergePerson(person);
			mail.sendMail(subject, token);
		}
		return "redirect: /edu/index.htm";
	}
//

//
	@RequestMapping(value="/updatePassword.htm",method = RequestMethod.GET)
	private String getResetPassword(HttpServletRequest request, HttpServletResponse response,UserDAO userdao) {
		if(request.getSession().getAttribute("user")!=null) {
			request.setAttribute("errormessage", "Please logout to reset password");
			return "error";
		}
		if(request.getParameter("token")==null) {
			request.setAttribute("errormessage", "No token found");
			return "error";
		}
		String token= (String)request.getParameter("token");
		Person person= userdao.getPersonByResetToken(token);
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
	@RequestMapping(value="/updatePassword.htm",method = RequestMethod.POST)
	private String updatePassword(HttpServletRequest request, HttpServletResponse response,UserDAO userdao) {
		String password = request.getParameter("pword");
		String confirmPassword = request.getParameter("cpword");
		if (!password.equals(confirmPassword)) {
			request.setAttribute("errormessage", "passwords does not match");
			return "error";
		}
		password = Hash.hashPassword(password);
		Person person= userdao.getPersonByEmail(request.getParameter("email"));
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