package com.finalproject.controllers;

import java.io.File;
import java.util.ArrayList;
import java.sql.Timestamp;

import java.io.IOException;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.client.HttpServerErrorException;
import com.finalproject.DAO.ProductDAO;

import com.finalproject.DAO.SellerDAO;
import com.finalproject.DAO.ReviewDAO;
import com.finalproject.DAO.UserDAO;

import com.finalproject.POJO.Person;
import com.finalproject.POJO.GroceryProduct;

import com.finalproject.POJO.Review;
import com.finalproject.POJO.Product;

import com.finalproject.POJO.TechProduct;
import com.finalproject.POJO.Seller;

import com.finalproject.utils.GroceryProductValidator;
import com.finalproject.POJO.User;

import com.finalproject.utils.TechproductValidator;
import com.finalproject.utils.ReviewValidator;
import com.google.gson.Gson;

import com.google.gson.GsonBuilder;

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
public class ProdController {

	@Autowired
	ReviewValidator reviewValidator;
	@Autowired
	TechproductValidator techValidator;
	@Autowired
	GroceryProductValidator groceryValidator;

	/**
	 * method to add a technical product
	 * 
	 * @param technicalProduct
	 * @param modelMap
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/seller/addTechProduct.htm", method = RequestMethod.GET)
	public String getAddTechnicalProduct(TechProduct technicalProduct, ModelMap modelMap, HttpServletRequest req) {
		if (req.getSession().getAttribute("user") == null) {
			req.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		modelMap.addAttribute(technicalProduct);
		return "addTechProduct";
	}

	/**
	 * method to handle the method to add a technical product
	 * 
	 * @param technicalProduct
	 * @param productDao
	 * @param sellerDao
	 * @param bindingResult
	 * @param sessionStatus
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/seller/addTechProduct.htm", method = RequestMethod.POST)
	public String addTechnicalProduct(@ModelAttribute("techProduct") TechProduct technicalProduct,
			ProductDAO productDao, SellerDAO sellerDao, BindingResult bindingResult, SessionStatus sessionStatus,
			HttpServletRequest req, HttpServletResponse res) {
		CommonsMultipartFile iconPic = technicalProduct.getPhoto();
		if (iconPic.getSize() != 0) {
			String fileName = "img" + technicalProduct.getProduct_id() + Calendar.getInstance().getTimeInMillis()
					+ iconPic.getContentType();
			File photoFile = new File("C:/Users/deepa/Desktop/images", fileName);
			technicalProduct.setPhotoFile(fileName);
			try {
				iconPic.transferTo(photoFile);
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
		String[] specs = req.getParameterValues("spec.key");
		String[] values = req.getParameterValues("spec.value");

		Map<String, String> specificationMap = new HashMap<String, String>();
		for (int i = 0; i < specs.length; i++) {
			specificationMap.put(specs[i], values[i]);
		}
		technicalProduct.setSpec(specificationMap);
		technicalProduct.setType("tech");

		Seller seller = sellerDao.getSellerByEmail((String) req.getSession().getAttribute("user"));
		if (seller == null) {
			req.setAttribute("errormessage", "there is no seller with that id");
			return "error";
		}
		technicalProduct.setSeller(seller);
		technicalProduct.setAddedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));

		if (productDao.addProduct(technicalProduct))
			return "redirect: http://localhost:8080/edu/seller/account.htm";
		else {
			req.setAttribute("errormessage", "Error: Try adding a different product");
			return "error";
		}
	}

	/**
	 * Method to add a grocery product
	 * 
	 * @param req
	 * @param model
	 * @param grocProd
	 * @return
	 */
	@RequestMapping(value = "/seller/addGroceryProduct.htm", method = RequestMethod.GET)
	public String getAddGrocProd(HttpServletRequest req, ModelMap model, GroceryProduct grocProd) {
		if (req.getSession().getAttribute("user") == null) {
			req.setAttribute("errormessage", "Please login, to continue having any access to the page");
			return "error";
		}
		model.addAttribute(grocProd);
		return "addGroceryProduct";
	}

	/**
	 * Method to add a grocery product by a seller
	 * 
	 * @param request
	 * @param grocProd
	 * @param results
	 * @param productdao
	 * @param sellerdao
	 * @return
	 */
	@RequestMapping(value = "/seller/addGroceryProduct.htm", method = RequestMethod.POST)
	public String addGrocProd(HttpServletRequest request, @ModelAttribute("groceryProduct") GroceryProduct grocProd,
			BindingResult results, ProductDAO productdao, SellerDAO sellerdao) {
		if (request.getSession().getAttribute("user") == null) {
			request.setAttribute("errormessage", "Please login, to continue having any access to the page");
			return "error";
		}
		/*
		 * handling a picture file
		 */
		CommonsMultipartFile iconPic = grocProd.getPhoto();
		if (iconPic.getSize() != 0) {
			String fileName = "img" + grocProd.getProduct_id() + Calendar.getInstance().getTimeInMillis()
					+ iconPic.getContentType();
			File file = new File("C:/Users/deepa/Desktop/images", fileName);
			grocProd.setPhotoFile(fileName);
			try {
				iconPic.transferTo(file);
			} catch (IllegalStateException e) {
				e.printStackTrace();
				return "error";
			} catch (IOException e) {
				e.printStackTrace();
				return "error";
			}
		}
		grocProd.setType("grocery");
		Seller seller = sellerdao.getSellerByEmail((String) request.getSession().getAttribute("user"));
		if (seller == null) {
			request.setAttribute("errormessage", "there is no seller with that id");
			return "error";
		}
		grocProd.setSeller(seller);
		grocProd.setAddedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		if (productdao.addProduct(grocProd))
			return "redirect: http://localhost:8080/edu/seller/account.htm";
		else {
			request.setAttribute("errormesssge", "Error adding product");
			return "error";
		}
	}

	/**
	 * method to handle a search product
	 * 
	 * @param req
	 * @param prodDao
	 * @return
	 */
	@RequestMapping(value = "/search.htm", method = RequestMethod.GET)
	public String searchProduct(HttpServletRequest req, ProductDAO prodDao) {

		String key = req.getParameter("search");
		String[] filterKeys = req.getParameterValues("filter");
		String sort = req.getParameter("sort");
		sort = sort == null ? "name" : sort;
		int page = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
		List<Product> products = prodDao.getProducts(key, filterKeys, sort, page);
		req.setAttribute("products", products);
		req.setAttribute("search", key);
		req.setAttribute("page", page);
		req.setAttribute("sort", sort);
		return "searchResults";
	}

	/**
	 * method to handle a delete product
	 * 
	 * @param req
	 * @param prodDao
	 * @param sellerdao
	 * @return
	 */
	@RequestMapping(value = "/seller/deleteProduct.htm", method = RequestMethod.GET)
	public String deleteProd(HttpServletRequest req, ProductDAO prodDao, SellerDAO sellerdao) {
		if (req.getSession().getAttribute("user") == null) {
			req.setAttribute("errormessage", "Please login, to continue having any access to the page");
			return "error";
		}
		if (req.getParameter("prod_id") == null) {
			req.setAttribute("errormessage", "Invalid id for product");
			return "error";
		}

		Product product = prodDao.getProductById(Integer.parseInt(req.getParameter("prod_id")));
		if (product == null) {
			req.setAttribute("errormessage", "this product does not seem to exist");
			return "error";
		}
		if (!product.getSeller().getEmail().equals(req.getSession().getAttribute("user"))) {
			req.setAttribute("errormessage", "Please verify your product id");
			return "error";
		}
		if (!prodDao.deleteProduct(Integer.parseInt(req.getParameter("prod_id")))) {
			req.setAttribute("errormessage", "error deleting this product, please tryagain later");
			return "error";
		}
		return "redirect: http://localhost:8080/edu/seller/account.htm";
	}

	/**
	 * method to update a product
	 * 
	 * @param request
	 * @param model
	 * @param product
	 * @param productdao
	 * @return
	 */
	@RequestMapping(value = "/seller/updateProduct.htm", method = RequestMethod.GET)
	public String getUpdateProd(HttpServletRequest request, ModelMap model, Product product, ProductDAO productdao) {
		if (request.getSession().getAttribute("user") == null) {
			request.setAttribute("errormessage", "Please login, to continue having any access to the page");
			return "error";
		}
		if (request.getParameter("prod_id") == null) {
			request.setAttribute("errormessage", "Invalid product id");
			return "error";
		}
		Product productToUpdate = productdao.getProductById(Integer.parseInt(request.getParameter("prod_id")));
		if (productToUpdate == null) {
			request.setAttribute("errormessage", "Invalid product id");
			return "error";
		}

		model.addAttribute("product", productToUpdate);
		return "productUpdate";
	}

	/**
	 * method to update a product by a seller
	 * 
	 * @param request
	 * @param product
	 * @param results
	 * @param productdao
	 * @return
	 */
	@RequestMapping(value = "/seller/updateProduct.htm", method = RequestMethod.POST)
	public String updateProduct(HttpServletRequest request, @ModelAttribute("product") Product product,
			BindingResult results, ProductDAO productdao) {

		if (!productdao.updateProduct(product)) {
			request.setAttribute("errormessage", "Error updating product");
			return "error";
		}
		return "redirect: /edu/seller/account.htm";
	}

	/**
	 * method to get a product
	 * 
	 * @param request
	 * @param prodDao
	 * @return
	 */
	@RequestMapping(value = "/product/getDetails.htm", method = RequestMethod.GET)
	public String getProductDetalis(HttpServletRequest request, ProductDAO prodDao) {
		if (request.getParameter("id") == null) {
			request.setAttribute("errormessage", "Invalid product id");
			return "error";
		}
		Product product = prodDao.getProductById(Integer.parseInt(request.getParameter("id")));
		if (product == null) {
			request.setAttribute("errormessage", "Product does not exist");
			return "error";
		}
		request.setAttribute("product", product);
		return "productDetails";
	}

	/**
	 * method to add a product to the cart
	 * 
	 * @param request
	 * @param productdao
	 * @param userdao
	 * @return
	 */
	@RequestMapping(value = "/user/addToCart.htm", method = RequestMethod.POST)
	public String addProdsToCart(HttpServletRequest request, ProductDAO productdao, UserDAO userdao) {
		Product product = productdao.getProductById(Integer.parseInt(request.getParameter("id")));
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		if (product == null) {
			request.setAttribute("errormessage", "This product does not seem to exist");
			return "error";
		}
		User user = userdao.getUserByEmail((String) request.getSession().getAttribute("user"));
		if (user == null) {
			request.setAttribute("errormessage", "Please sign up here before further continuing");
			return "error";

		}
		if (product.getQuantity() < quantity) {
			request.setAttribute("errormessage", "Invalid quantity");
			return "error";
		}
		if (!userdao.deleteProductFromCart(user, product)) {
			request.setAttribute("errormessage", "Item is not in your cart");
			return "error";
		}

		if (!userdao.addProductToCart(user, product, quantity)) {
			request.setAttribute("errormessage", "Error Adding product to cart");
			return "error";
		}

		return "redirect: /edu/user/cart.htm";
	}

	/**
	 * method to delete a product from the cart
	 * 
	 * @param request
	 * @param productdao
	 * @param userdao
	 * @return
	 */
	@RequestMapping(value = "/user/deleteFromCart.htm", method = RequestMethod.GET)
	public String delProdsFromCart(HttpServletRequest request, ProductDAO productdao, UserDAO userdao) {
		if (request.getSession().getAttribute("user") == null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		Product product = productdao.getProductById(Integer.parseInt(request.getParameter("id")));
		if (product == null) {
			request.setAttribute("errormessage", "PThis product does not seem to exist");
			return "error";
		}
		User user = userdao.getUserByEmail((String) request.getSession().getAttribute("user"));
		if (user == null) {
			request.setAttribute("errormessage", "Please sign up here before further continuing");
			return "error";

		}
		if (!userdao.deleteProductFromCart(user, product)) {
			request.setAttribute("errormessage", "Item is not in your cart");
			return "error";
		}
		return "redirect: /edu/user/cart.htm";
	}

	/**
	 * method to add a review to the product
	 * 
	 * @param request
	 * @param model
	 * @param review
	 * @param productdao
	 * @return
	 */
	@RequestMapping(value = "/user/addReview.htm", method = RequestMethod.GET)
	public String addReviews(HttpServletRequest request, ModelMap model, Review review, ProductDAO productdao) {
		if (request.getSession().getAttribute("user") == null) {
			request.setAttribute("errormessage", "Please login before continuing");
			return "error";
		}
		if (request.getParameter("prod_id") == null) {
			request.setAttribute("errormessage", "Invalid product id");
			return "error";
		}
		Product product = productdao.getProductById(Integer.parseInt(request.getParameter("prod_id")));
		if (product == null) {
			request.setAttribute("errormessage", "Product does not exist");
			return "error";
		}
		model.addAttribute(review);
		return "review";
	}

	/**
	 * method to process a review
	 * 
	 * @param req
	 * @param userdao
	 * @param productdao
	 * @param review
	 * @param results
	 * @param reviewdao
	 * @return
	 */
	@RequestMapping(value = "/user/addReview.htm", method = RequestMethod.POST)
	public String processReviews(HttpServletRequest req, UserDAO userdao, ProductDAO productdao,
			@ModelAttribute("review") Review review, BindingResult results, ReviewDAO reviewdao) {
		reviewValidator.validate(review, results);
		if (results.hasErrors()) {
			System.out.print(results.getErrorCount());
			System.out.println(results.getAllErrors());

			System.out.println("Hello!!");
			return "review";
		}

		if (req.getSession().getAttribute("user") == null) {
			req.setAttribute("errormessage", "Login before continuing");
			return "error";
		}
		Product product = productdao.getProductById(Integer.parseInt(req.getParameter("prod_id")));
		User user = userdao.getUserByEmail((String) req.getSession().getAttribute("user"));
		if (user == null) {
			req.setAttribute("errormessage", "Signup before continuing");
			return "error";
		}
		review.setProduct(product);
		review.setSeller(product.getSeller());
		review.setUser(user);
		review.setUsername(user.getName());
		review.setAddedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		if (!reviewdao.addReview(review)) {
			req.setAttribute("errormessage", "Error adding review");
			return "error";
		}
		return "redirect: /edu/index.htm";
	}

	/**
	 * method to get a review
	 * 
	 * @param request
	 * @param reviewDao
	 * @return
	 */
	@RequestMapping(value = "/product/getReviews.htm", method = RequestMethod.GET)
	@ResponseBody
	public String getReviews(HttpServletRequest request, ReviewDAO reviewDao) {
		List<Review> reviewList = reviewDao.getReviewsByProduct(Integer.parseInt(request.getParameter("id")),
				(Integer.parseInt(request.getParameter("page"))));
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(reviewList);
	}
}
