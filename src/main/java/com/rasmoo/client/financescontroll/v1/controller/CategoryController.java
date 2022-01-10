package com.rasmoo.client.financescontroll.v1.controller;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rasmoo.client.financescontroll.entity.Category;
import com.rasmoo.client.financescontroll.entity.User;
import com.rasmoo.client.financescontroll.repository.ICategoryRepository;
import com.rasmoo.client.financescontroll.repository.IUserRepository;
import com.rasmoo.client.financescontroll.v1.dto.CategoryDTO;
import com.rasmoo.client.financescontroll.v1.vo.Response;

@RestController
@RequestMapping("/v1/categorias")
@PreAuthorize(value = "#oauth2.hasScope('logado') and hasRole('ROLE_CUSTOMER')")
public class CategoryController {

	private final ICategoryRepository categoryRepository;

	private final IUserRepository userRepository;
	
	private final ModelMapper mapper;

	@Autowired
	public CategoryController(final ICategoryRepository categoryRepository, final IUserRepository userRepository) {
		this.categoryRepository = categoryRepository;
		this.userRepository = userRepository;
		this.mapper = new ModelMapper();
	}

	@PostMapping
	public ResponseEntity<Response<Category>> cadastrarCategoria(@RequestBody CategoryDTO categoria) {
		final Response<Category> response = new Response<>();
		
		try {
			final User usuario = this.findAuthUser();

			if (categoria != null && categoria.getId() == null) {
				final Category categoriaEntity = mapper.map(categoria, Category.class);
				categoriaEntity.setUser(usuario);

				final Category categoriaSalva = this.categoryRepository.save(categoriaEntity);

				response.setData(categoriaSalva);
				response.setStatusCode(HttpStatus.CREATED.value());
				
				return ResponseEntity.status(HttpStatus.CREATED).body(response);
			}

			throw new Exception();
		} catch (final Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@PutMapping
	public ResponseEntity<Response<Category>> atualizarCategoria(@RequestBody final CategoryDTO categoria) {
		final Response<Category> response = new Response<>();
		
		try {
			if (categoria != null && categoria.getId() != null) {
				final User usuario = this.findAuthUser();
				
				final Optional<Category> category = this.categoryRepository.findById(categoria.getId());
				
				if (category.isPresent() && category.get().getUser().getId().equals(usuario.getId())) {
					final Category categoryEntity = this.mapper.map(categoria, Category.class);
					categoryEntity.setUser(usuario);

					response.setData(this.categoryRepository.save(categoryEntity));
					response.setStatusCode(HttpStatus.OK.value());
					
					return ResponseEntity.status(HttpStatus.OK).body(response);
				}
			}

			throw new Exception();
		} catch (Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@GetMapping
	@PreAuthorize(value = "#oauth2.hasScope('logado') and hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<Response<List<Category>>> listarCategorias() {
		final Response<List<Category>> response = new Response<>();
		
		try {
			final User usuario = this.findAuthUser();

			response.setData(this.categoryRepository.findAllByUserId(usuario.getId()));
			response.setStatusCode(HttpStatus.OK.value());
			
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (final Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<Response<Category>> consultarCategoria(@PathVariable Long id) {
		final Response<Category> response = new Response<>();

		try {
			final Optional<Category> catogory = this.categoryRepository.findById(id);

			if (catogory.isPresent()) {
				response.setData(catogory.get());
			}

			response.setStatusCode(HttpStatus.OK.value());

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (final Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Response<Boolean>> excluirCategoria(@PathVariable Long id) {
		final Response<Boolean> response = new Response<>();

		try {
			if (this.categoryRepository.findById(id).isPresent()) {
				this.categoryRepository.deleteById(id);

				response.setData(Boolean.TRUE);
			}
			response.setStatusCode(HttpStatus.OK.value());

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (final Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	private User findAuthUser() {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		final Optional<User> usuario = this.userRepository.findByEmail(auth.getName());

		if (usuario.isEmpty()) {
			throw new RuntimeException("Usuário não encontrado.");
		}

		return usuario.get();
	}

}
