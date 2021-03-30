package com.devsuperior.aula.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.aula.dto.UserDTO;
import com.devsuperior.aula.dto.UserInsertDTO;
import com.devsuperior.aula.services.UserService;
import com.devsuperior.aula.services.exceptions.ServiceException;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<List<UserDTO>> findAll() {
		List<UserDTO> list = userService.findAll();
		return ResponseEntity.ok(list);
	}

	@PostMapping
	public ResponseEntity<UserDTO> insert(@RequestBody UserInsertDTO dto) {
		try {
			UserDTO obj = userService.insert(dto);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(obj.getId()).toUri();
			return ResponseEntity.created(uri).body(obj);	
		}
		catch (ServiceException e) {
			return ResponseEntity.unprocessableEntity().build();
		}
	}
}
