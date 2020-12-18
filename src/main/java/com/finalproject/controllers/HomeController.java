package com.finalproject.controllers;


import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


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
public class HomeController {
	
	/**
	 * Handles requests for the application home page.
	 */
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = {"/","/index.htm"}, method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "home";
	}
	@RequestMapping(value = "/error.htm",method=RequestMethod.GET)
	public String error(HttpServletRequest request) {
		request.setAttribute("errormessage", "something went wrong!!");
		return "error";
	}
}
