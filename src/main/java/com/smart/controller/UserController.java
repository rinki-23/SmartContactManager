package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactREpo;
import com.smart.dao.UserRepo;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ContactREpo contactRepo;
	
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
	public String add(Model m, HttpSession session) {
		// remove message
		session.removeAttribute("message");
		m.addAttribute("title", "Add Contact");
		m.addAttribute("contact", new Contact());
		return "norml/add-contact";
	}
	
	@PostMapping("/process-contact")
	public String savedata(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,  Principal principal, HttpSession session) {
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
				// add by default photo
				contact.setImage("contact.jpg");
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
		
		//message success
		
		session.setAttribute("message", new Message("Your contact is added successfully", "alert-success"));
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error" + e.getMessage());
			// error message
			session.setAttribute("message", new Message("Something went wrong", "alert-danger"));
		}
		return "norml/add-contact";
	}
	
	@GetMapping("/view-contacts/{page}")
	public String view(@PathVariable("page") Integer page, Model m, Principal principal, HttpSession session) {
		
		m.addAttribute("title", "View Contacts");
		// first fetch the user name 
		//String name = principal.getName();
		// then fetch the user details from the databse
		//User user = userRepo.getUserByUserName(name);
		//List<Contact> contact = user.getContacts();
		
		// we want the contact of that user who is logged in
		String name = principal.getName();
		User user = userRepo.getUserByUserName(name);
		
		//current page-page
		//contact per page-5
		Pageable pageable = PageRequest.of(page, 5);
		Page<Contact> list = contactRepo.findContactsByUser(user.getId(), pageable);
		m.addAttribute("contacts", list);
		
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPage", list.getTotalPages());
		
		return "norml/view";
	}
	
	// showing specific contact details
	@GetMapping("/{cid}/contact")
	public String showContactDetails(@PathVariable("cid") Integer cid, Model m, Principal principal) {
		System.out.println(cid);
		// get the all info using id
		Optional<Contact> contact = contactRepo.findById(cid);
		Contact c = contact.get();
		
		// for security
		String username = principal.getName();
		User user= userRepo.getUserByUserName(username);
		if(user.getId() == c.getUsers().getId()) 
			m.addAttribute("contact", c);
		return "norml/contact-details";
	}
	
	// for delete the contact
	@GetMapping("/delete/{cid}")
	public String delete(@PathVariable("cid") Integer cid, Principal principal, HttpSession session) {
		// first find the id
		Optional<Contact> contactop = contactRepo.findById(cid);
		// using the id we get all info of that id
		Contact contact = contactop.get();
		
		// after get the info we use delete method to delete 
		contact.setUsers(null);
		contactRepo.delete(contact);
		System.out.println("Deleted");
	
		
		return "redirect:/user/view-contacts/0";
		
	}
	// open the update contact
	@GetMapping("/open-update/{cid}")
	public String update(@PathVariable("cid") Integer cid, Model model) {
		Optional<Contact> contactop = contactRepo.findById(cid);
		Contact contact = contactop.get();
		model.addAttribute("contact", contact);
		
		return "norml/update";
	}
	
	// update the contact
	@PostMapping("/process-update/{cid}")
	public String processupdate(@PathVariable("cid") Integer cid, @ModelAttribute("contact") Contact contact) {
		Contact oldcontact = contactRepo.findById(cid).get();
		oldcontact.setName(contact.getName());
		oldcontact.setNickname(contact.getNickname());
		oldcontact.setPhoneno(contact.getPhoneno());
		oldcontact.setEmail(contact.getEmail());
		oldcontact.setWork(contact.getWork());
		oldcontact.setDescription(contact.getDescription());
		contactRepo.save(oldcontact);
		
		
		return "redirect:/user/view-contacts/0";
	}
}
