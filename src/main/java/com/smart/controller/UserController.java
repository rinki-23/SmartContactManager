package com.smart.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.dao.UserRepo;
import com.smart.entities.Contact;
import com.smart.entities.User;



@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepo userRepo;
	
	// work for all handler
	@ModelAttribute
	public void addCommonData(Model m, Principal principal) {
		// get the username from the form
				String username = principal.getName();
				System.out.println(username);
				
				// using that username we get all data from the database
				User user = userRepo.getUserByUserName(username);
				System.out.println(user);
				
				m.addAttribute("User" , user);
	}

	
	@RequestMapping("/index")
	public String dashboard() {
		return "norml/user_dashboard";
	}
	
	@GetMapping("/add-contact")
	public String add(Model m) {
		m.addAttribute("title", "Add Contact");
		m.addAttribute("contact", new Contact());
		return "norml/add-contact";
	}
}
