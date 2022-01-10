package com.rasmoo.client.financescontroll.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.rasmoo.client.financescontroll.entity.User;
import com.rasmoo.client.financescontroll.repository.IUserRepository;
import com.rasmoo.client.financescontroll.v1.vo.Response;
import com.rasmoo.client.financescontroll.v1.vo.UserVO;

import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/usuario")
@PreAuthorize(value = "#oauth2.hasScope('nao_logado')")
public class UserController {
	
	private final IUserRepository userRepository;
	private final PasswordEncoder encoder;

	@Autowired
	public UserController(final IUserRepository userRepository, final PasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.encoder = encoder;
	}

	@PostMapping
	public ResponseEntity<Response<User>> cadastrarUsuario(@RequestBody UserVO userVo) {
		final Response<User> response = new Response<>();
		
		try {
			final User user = new User();
			
			user.setNome(userVo.getNome());
			user.getCredencial().setEmail(userVo.getEmail());
			user.getCredencial().setSenha(this.encoder.encode(userVo.getPassword()));
			
			response.setData(this.userRepository.save(user));
			
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (final Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@PatchMapping
	public ResponseEntity<Response<User>> atualizarSenha(@RequestBody final UserVO userVO) {
		final Response<User> response = new Response<>();

		try {
			final Optional<User> user = this.userRepository.findByEmail(userVO.getEmail());

			if (user.isEmpty()) {
				throw new RuntimeException("Erro");
			}

			user.get().getCredencial().setSenha(this.encoder.encode(userVO.getPassword()));

			response.setData(this.userRepository.save(user.get()));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (final Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

}
