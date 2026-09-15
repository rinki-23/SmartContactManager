package com.smart.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepo;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepo userRepo;

	@GetMapping("/")
	public String home(Model m) {
		m.addAttribute("title", "Home Page");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model m) {
		m.addAttribute("title", "About Page");
		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("title", "Register");
		m.addAttribute("user", new User());
		return "signup";
	}
	
	//handle for registering
	@PostMapping("/do-register")
	public String register(@Valid @ModelAttribute("user") User user,BindingResult result1,  @RequestParam(value = "agreement" , defaultValue = "false") boolean agreement, Model m, HttpSession session) {
		try {
			
			if(!agreement) {
				System.out.println("You have not agreed the terms and conditions");
				throw new Exception("You have not agreed the terms and conditions");
			}
			if(result1.hasErrors()) {
				System.out.println("Errors" + result1.toString());
				m.addAttribute("user", user);
				return"signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("Agreement" + agreement);
			System.out.println("USER" + user);
			
			User result = userRepo.save(user);
			m.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfylly Registered!!", "alert-success"));
			
			return "signup";
			
		}catch(Exception e) {
			e.printStackTrace();
			m.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong!!"+e.getMessage(), "alert-danger"));
			return "signup";
			
		}
	}
	
	@GetMapping("/signin")
	public String login(Model m) {
		m.addAttribute("title", "Please login first");
		return "login";
	}
}
