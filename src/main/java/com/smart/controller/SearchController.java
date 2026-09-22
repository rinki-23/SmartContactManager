package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.ContactREpo;
import com.smart.dao.UserRepo;
import com.smart.entities.Contact;
import com.smart.entities.User;

@RestController
public class SearchController {
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ContactREpo contactRepo;
	
	// search handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search( @PathVariable("query") String query, Principal principal){
		
		String username = principal.getName();
		User users = userRepo.getUserByUserName(username);
		List<Contact> contacts = contactRepo.findByNameContainingAndUsers(query, users);
		return ResponseEntity.ok(contacts);
	}

}
