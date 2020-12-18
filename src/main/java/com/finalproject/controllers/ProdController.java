package com.finalproject.controllers;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.finalproject.DAO.ProductDAO;
import com.finalproject.DAO.ReviewDAO;
import com.finalproject.DAO.SellerDAO;
import com.finalproject.DAO.UserDAO;
import com.finalproject.POJO.GroceryProduct;
import com.finalproject.POJO.Person;
import com.finalproject.POJO.Product;
import com.finalproject.POJO.Review;
import com.finalproject.POJO.Seller;
import com.finalproject.POJO.TechProduct;
import com.finalproject.POJO.User;
import com.finalproject.utils.GroceryProductValidator;
import com.finalproject.utils.ReviewValidator;
import com.finalproject.utils.TechproductValidator;
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
public class ProdController {
	
	@Autowired
	ReviewValidator reviewValidator;
	@Autowired
	TechproductValidator techValidator;
	@Autowired
	GroceryProductValidator groceryValidator;
	@RequestMapping(value = "/seller/addTechProduct.htm", method=RequestMethod.GET)
	public String getAddTechProduct(TechProduct techProduct,ModelMap model,HttpServletRequest request) {
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
//		System.out.println((String)request.getSession().getAttribute("user"));
		model.addAttribute(techProduct);
		return "addTechProduct";
	}
	@RequestMapping(value = "/seller/addTechProduct.htm",method=RequestMethod.POST)
	public String addTechProduct(@ModelAttribute("techProduct") TechProduct techProduct,ProductDAO productdao,SellerDAO sellerdao, BindingResult result,SessionStatus status,HttpServletRequest request,HttpServletResponse response) {
		CommonsMultipartFile photo= techProduct.getPhoto();
		if(photo.getSize()!=0) {
		String fileName="img"+techProduct.getProduct_id()+Calendar.getInstance().getTimeInMillis()+photo.getContentType();
		File file= new File("C:/Users/deepa/Desktop/images",fileName);
		techProduct.setPhotoFile(fileName);
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
		String[] specs=request.getParameterValues("spec.key");
		String[] values=request.getParameterValues("spec.value");
		
		Map<String,String> specMap= new HashMap<String,String>();
		for(int i=0;i<specs.length;i++) {
		specMap.put(specs[i], values[i]);
		}
		techProduct.setSpec(specMap);
		techProduct.setType("tech");
		
		Seller seller=sellerdao.getSellerByEmail((String)request.getSession().getAttribute("user"));
		if(seller==null) {
			request.setAttribute("errormessage", "there is no seller with that id");
			return "error";
		}
		techProduct.setSeller(seller);
		techProduct.setAddedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
//		sellerdao.addProductToSeller(techProduct, seller);
		if(productdao.addProduct(techProduct))
			return "redirect: http://localhost:8080/edu/seller/account.htm";
		else {
			request.setAttribute("errormessage", "Error adding product, try with different name");
			return "error";
		}
	}
	@RequestMapping(value="/seller/addGroceryProduct.htm",method=RequestMethod.GET)
	public String getaddGroceryProduct(HttpServletRequest request,ModelMap model,GroceryProduct groceryProduct ) {
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		model.addAttribute(groceryProduct);
		return "addGroceryProduct";
	}
	@RequestMapping(value="/seller/addGroceryProduct.htm",method=RequestMethod.POST)
	public String addGroceryProduct(HttpServletRequest request,@ModelAttribute("groceryProduct") GroceryProduct groceryProduct,BindingResult results,ProductDAO productdao,SellerDAO sellerdao) {
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		CommonsMultipartFile photo= groceryProduct.getPhoto();
		if(photo.getSize()!=0) {
		String fileName="img"+groceryProduct.getProduct_id()+Calendar.getInstance().getTimeInMillis()+photo.getContentType();
		File file= new File("C:/Users/deepa/Desktop/images",fileName);
		groceryProduct.setPhotoFile(fileName);
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
		groceryProduct.setType("grocery");
		Seller seller=sellerdao.getSellerByEmail((String)request.getSession().getAttribute("user"));
		if(seller==null) {
			request.setAttribute("errormessage", "there is no seller with that id");
			return "error";
		}
		groceryProduct.setSeller(seller);
		groceryProduct.setAddedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
//		sellerdao.addProductToSeller(techProduct, seller);
		if(productdao.addProduct(groceryProduct))
			return "redirect: http://localhost:8080/edu/seller/account.htm";
		else {
			request.setAttribute("errormesssge", "Error adding product");
			return "error";
		}
	}
	@RequestMapping(value="/search.htm", method = RequestMethod.GET)
	public String searchProducts(HttpServletRequest request,ProductDAO productdao) {
//		productdao.se
		String  key= request.getParameter("search");
		String[] filterKeys= request.getParameterValues("filter");
		String sort= request.getParameter("sort");
		sort=sort==null?"name":sort;
		int page=request.getParameter("page")==null?1:Integer.parseInt(request.getParameter("page"));
		List<Product> products=productdao.getProducts(key, filterKeys, sort, page);
		request.setAttribute("products", products);
		request.setAttribute("search",key);
		request.setAttribute("page",page);
		request.setAttribute("sort", sort);
		return "searchResults";
	}
	@RequestMapping(value="/seller/deleteProduct.htm",method=RequestMethod.GET)
	public String deleteProduct(HttpServletRequest request,ProductDAO productdao,SellerDAO sellerdao) {
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		if(request.getParameter("prod_id")==null) {
			request.setAttribute("errormessage", "Invalid product id");
			return "error";
		}
//		Product product= productdao.getProductById(Integer.parseInt(request.getParameter("id")));
//		if(product==null) {
//			request.setAttribute("errormessage", "Product does not exist");
//			return "error";
//		}
		Product product= productdao.getProductById(Integer.parseInt(request.getParameter("prod_id")));
		if(product==null) {
			request.setAttribute("errormessage", "product does not exist");
			return "error";
		}
		if(!product.getSeller().getEmail().equals(request.getSession().getAttribute("user"))) {
			request.setAttribute("errormessage", "Please check product id");
			return "error";
		}
		if(!productdao.deleteProduct(Integer.parseInt(request.getParameter("prod_id")))) {
			request.setAttribute("errormessage", "error deleting product");
			return "error";
		}
		return "redirect: http://localhost:8080/edu/seller/account.htm";
	}
	@RequestMapping(value="/seller/updateProduct.htm",method=RequestMethod.GET)
	public String getupadateProduct(HttpServletRequest request,ModelMap model,Product product,ProductDAO productdao) {
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		if(request.getParameter("prod_id")==null) {
			request.setAttribute("errormessage", "Invalid product id");
			return "error";
		}
		Product prodToUpdate= productdao.getProductById(Integer.parseInt(request.getParameter("prod_id")));
		if(prodToUpdate==null) {
			request.setAttribute("errormessage", "Invalid product id");
			return "error";
		}
//		product.setName(prodToUpdate.getName());
//		product.setDescription(prodToUpdate.getDescription());
//		product.setPrice(prodToUpdate.getPrice());
//		product.setQuantity(prodToUpdate.getQuantity());
//		product.setProduct_id(prodToUpdate.getProduct_id());
//		product.setAddedAt(prodToUpdate.getAddedAt());
//		product.setSeller(prod);
		model.addAttribute("product",prodToUpdate);
		return "productUpdate";
	}
	@RequestMapping(value="/seller/updateProduct.htm",method=RequestMethod.POST)
	public String updateProduct(HttpServletRequest request,@ModelAttribute("product")Product product, BindingResult results,ProductDAO productdao) {
//		System.out.println(product.getProduct_id()+"id!!!!!");
		if(!productdao.updateProduct(product)) {
			request.setAttribute("errormessage", "Error updating product");
			return "error";
		}
		return "redirect: /edu/seller/account.htm";
	}
	@RequestMapping(value="/product/getDetails.htm",method = RequestMethod.GET)
	public String getProductDetalis(HttpServletRequest request,ProductDAO productdao) {
		if(request.getParameter("id")==null)
		{
			request.setAttribute("errormessage", "Invalid product id");
			return "error";
		}
		Product product= productdao.getProductById(Integer.parseInt(request.getParameter("id")));
		if(product==null) {
			request.setAttribute("errormessage", "Product does not exist");
			return "error";
		}
		request.setAttribute("product", product);
		return "productDetails";
	}
	@RequestMapping(value="/user/addToCart.htm",method = RequestMethod.POST)
	public String addProductToCart(HttpServletRequest request,ProductDAO productdao,UserDAO userdao) {
		Product product= productdao.getProductById(Integer.parseInt(request.getParameter("id")));
		int quantity= Integer.parseInt(request.getParameter("quantity"));
		if(product==null) {
			request.setAttribute("errormessage", "Product does not exist");
			return "error";
		}
		User user= userdao.getUserByEmail((String)request.getSession().getAttribute("user"));
		if(user==null)
		{
			request.setAttribute("errormessage", "Please sign up before continuing");
			return "error";

		}	
		if(product.getQuantity()<quantity) {
			request.setAttribute("errormessage", "Invalid quantity");
			return "error";
		}
			if(!userdao.deleteProductFromCart(user, product)) {
				request.setAttribute("errormessage", "Item is not in your cart");
				return "error";
			}
		
		
		if(!userdao.addProductToCart(user, product, quantity)) {
			request.setAttribute("errormessage", "Error Adding product to cart");
			return "error";
		}
////		
//			Map<Product,Integer> cart=user.getCart();
//			cart.put(product, value)
		return "redirect: /edu/user/cart.htm";
	}
	@RequestMapping(value="/user/deleteFromCart.htm",method = RequestMethod.GET)
	public String deleteProductFromCart(HttpServletRequest request, ProductDAO productdao,UserDAO userdao){
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		Product product= productdao.getProductById(Integer.parseInt(request.getParameter("id")));
		if(product==null) {
			request.setAttribute("errormessage", "Product does not exist");
			return "error";
		}
		User user= userdao.getUserByEmail((String)request.getSession().getAttribute("user"));
		if(user==null)
		{
			request.setAttribute("errormessage", "Please sign up before continuing");
			return "error";

		}
			if(!userdao.deleteProductFromCart(user, product)) {
				request.setAttribute("errormessage", "Item is not in your cart");
				return "error";
			}
		return "redirect: /edu/user/cart.htm";
	}
	@RequestMapping(value="/user/addReview.htm", method=RequestMethod.GET)
	public String addReview(HttpServletRequest request,ModelMap model,Review review,ProductDAO productdao) {
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		if(request.getParameter("prod_id")==null) {
			request.setAttribute("errormessage", "Invalid product id");
			return "error";
		}
		Product product= productdao.getProductById(Integer.parseInt(request.getParameter("prod_id")));
		if(product==null) {
			request.setAttribute("errormessage", "Product does not exist");
			return "error";
		}
		model.addAttribute(review);
		return "review";
	}
	@RequestMapping(value="/user/addReview.htm", method=RequestMethod.POST)
	public String processReview(HttpServletRequest request,UserDAO userdao,ProductDAO productdao,@ModelAttribute("review") Review review, BindingResult results,ReviewDAO reviewdao) {
//		System.out.println("Hello!!");
		reviewValidator.validate(review, results);
		if(results.hasErrors()) {
			System.out.print(results.getErrorCount());
			System.out.println(results.getAllErrors());
//			results.getAllErrors().stream().forEach();
			System.out.println("Hello!!");
			return "review";
		}
		
		if(request.getSession().getAttribute("user")==null) {
			request.setAttribute("errormessage","Login before continuing");
			return "error";
		}
		Product product= productdao.getProductById(Integer.parseInt(request.getParameter("prod_id")));
		User user= userdao.getUserByEmail((String)request.getSession().getAttribute("user"));
		if(user==null) {
			request.setAttribute("errormessage","Signup before continuing");
			return "error";
		}
		review.setProduct(product);
		review.setSeller(product.getSeller());
		review.setUser(user);
		review.setUsername(user.getName());
		review.setAddedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		if(!reviewdao.addReview(review)) {
			request.setAttribute("errormessage", "Error adding review");
			return "error";
		}
		return "redirect: /edu/index.htm";
	}
//	public List<Reviews>
	@RequestMapping (value="/product/getReviews.htm", method = RequestMethod.GET)
	@ResponseBody
	public String getReviews(HttpServletRequest request,ReviewDAO reviewdao) {
		List<Review> reviews= reviewdao.getReviewsByProduct(Integer.parseInt(request.getParameter("id")),(Integer.parseInt(request.getParameter("page"))));
		Gson gson= new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(reviews);
	}
}
