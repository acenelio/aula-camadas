package com.devsuperior.aula.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.aula.entities.User;
import com.devsuperior.aula.repositories.UserRepository;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping
	public ResponseEntity<List<User>> findAll() {
		List<User> list = userRepository.findAll();
		return ResponseEntity.ok(list);
	}

	@PostMapping
	public ResponseEntity<User> insert(@RequestBody User obj) {
		
		User user = userRepository.findByEmail(obj.getEmail());
		if (user != null) {
			return ResponseEntity.unprocessableEntity().build();
		}
		
		obj.setPassword(passwordEncoder.encode(obj.getPassword()));
		obj = userRepository.save(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);		
	}
}
