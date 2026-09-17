package com.smart.dao;

import com.smart.entities.Contact;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface ContactREpo extends JpaRepository<Contact, Integer>{
	//pagination
	@Query("from Contact as c  where c.users.id =:userId")
	public List<Contact> findContactsByUser(@Param("userId") int userId);

}
