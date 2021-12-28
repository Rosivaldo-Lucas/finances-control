package com.rasmoo.client.financescontroll.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rasmoo.client.financescontroll.entity.User;
import com.rasmoo.client.financescontroll.repository.IUserRepository;
import com.rasmoo.client.financescontroll.v1.vo.Response;
import com.rasmoo.client.financescontroll.v1.vo.UserVO;

@RestController
@RequestMapping(value = "/v1/usuario")
public class UserController {
	
	@Autowired
	private IUserRepository userRepository;
	
	@PostMapping
	public ResponseEntity<Response<User>> cadastrarUsuario(@RequestBody UserVO userVo) {
		Response<User> response = new Response<>();
		
		try {
			
			User user = new User();
			
			user.setNome(userVo.getNome());
			user.getCredencial().setEmail(userVo.getEmail());
			user.getCredencial().setSenha(userVo.getPassword());
			
			response.setData(this.userRepository.save(user));
			
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
			
		} catch (Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}
	
}
