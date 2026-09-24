package com.smart.dao;


import com.smart.entities.Contact;
import com.smart.entities.User;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface ContactREpo extends JpaRepository<Contact, Integer>{
	//pagination
	@Query("from Contact as c  where c.users.id =:userId")
//	public List<Contact> findContactsByUser(@Param("userId") int userId);
	
	public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);

	public List<Contact> findByNameContainingAndUsers(String name, User users);
	
	public Contact findByEmail(String email);
}
