package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
	
	@PostMapping("/process-contact")
	public String savedata(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,  Principal principal) {
		try {
			// first we get the name of user
			String name = principal.getName();
			// from the name , we get the user information
			User user = userRepo.getUserByUserName(name);
			
			//for bidirectional mapping, we give user to contact
			contact.setUsers(user);
			// (contact to user) then add the contact in the user
			user.getContacts().add(contact);
			if(file.isEmpty()) {
				System.out.println("File is empty");
			}
			else {
		
		
		// upload the image
		contact.setImage(file.getOriginalFilename());
		File file1 = new ClassPathResource("static/img").getFile();
		Path path = Paths.get(file1.getAbsolutePath() + File.separator + file.getOriginalFilename());
		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		
		System.out.println("Image uploaded");
			}
		// update the user
		userRepo.save(user);
		
		System.out.println("Added to data base");
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error" + e.getMessage());
		}
		return "norml/add-contact";
	}
}
