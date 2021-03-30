package com.devsuperior.aula.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.devsuperior.aula.dto.UserDTO;
import com.devsuperior.aula.dto.UserInsertDTO;
import com.devsuperior.aula.entities.User;
import com.devsuperior.aula.repositories.UserRepository;
import com.devsuperior.aula.services.exceptions.ServiceException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public List<UserDTO> findAll() {
		List<User> list = userRepository.findAll();
		return list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
	} 
	
	public UserDTO insert(UserInsertDTO dto) {
		User user = userRepository.findByEmail(dto.getEmail());
		if (user != null) {
			throw new ServiceException("Email já existe");
		}
		
		User obj = new User();
		obj.setName(dto.getName());
		obj.setEmail(dto.getEmail());
		obj.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		obj = userRepository.save(obj);
		
		return new UserDTO(obj);
	}
}
