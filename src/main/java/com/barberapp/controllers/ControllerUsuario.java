package com.barberapp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.barberapp.entities.Usuario;
import com.barberapp.services.usuario.ServiceUsuario;

@CrossOrigin(origins = {"http://localhost:4200/", "https://barberappfronted.pages.dev/"})
@RestController
@RequestMapping("usuario")
public class ControllerUsuario {
	
	@Autowired
	private ServiceUsuario serviceUsuario;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
	
	
	
	 /////////////////// USUARIO REGISTRAR   http://localhost:8080/usuario/save ////////////////
		@PostMapping("/save")
		public ResponseEntity<?> create (@RequestBody Usuario usuario){
			
			Usuario newUsuario = null;
			Map<String, Object> response = new HashMap<>();
			
			try {
				
				String hashedPassword = passwordEncoder.encode(usuario.getPassword());
				
				usuario.setPassword(hashedPassword);
				
				newUsuario = serviceUsuario.save(usuario);
				
			} catch (DataAccessException e) {
				
				response.put("mensaje", "Error al hacer insert en la base de datos");
				response.put("rrror", e.getMessage().concat(": ").concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			response.put("menssaje","El usuario ha sido creado con exito");
			response.put("usuario: ", newUsuario);
			return new ResponseEntity<Map<String, Object >>(response, HttpStatus.CREATED);
			
		}
		
		
		/////////////////// CONSULT USUARIO   http://localhost:8080/usuario/consult/ID ////////////////
		@GetMapping("/consult/{id}")
		public ResponseEntity<?> consultusuarioId(@PathVariable(value = "id") Long id){
			
			Optional<Usuario> usuario = null;
			Map<String, Object> response = new HashMap<>();
			
			try {
				usuario =serviceUsuario.findById(id);
				
			} catch (DataAccessException e) {
				response.put("mensaje", "Error al hacer consulta en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			if (!usuario.isPresent()) {
				response.put("Mensaje", "EL usuario con el ID ".concat(id.toString().concat(" no existe en la base de datos")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			
			return ResponseEntity.ok(usuario);
		}
		
		
		
		/////////////////// UPDATE USUARIO   http://localhost:8080/usuario/update/ID ////////////////
		
		@PutMapping("/update/{id}")
		public ResponseEntity<?>update(@RequestBody Usuario newUsuario,@PathVariable (value = "id")Long idUsuario ){
			
			Optional<Usuario> currentUsuario = serviceUsuario.findById(idUsuario);
			Optional<Usuario> usuarioUpdate = null;
			
			Map<String, Object> response = new HashMap<>();
			
			if(!currentUsuario.isPresent()) {
				response.put("mensaje", "No se pudo editar el usuario con el ID ".concat(idUsuario.toString().concat(" no existe en la base de datos")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			try {

				currentUsuario.get().setUsername(newUsuario.getUsername());
				currentUsuario.get().setPassword(newUsuario.getPassword());
				currentUsuario.get().setCity(newUsuario.getCity());
				currentUsuario.get().setCellphone(newUsuario.getCellphone());
				currentUsuario.get().setAge(newUsuario.getAge());
				usuarioUpdate = Optional.ofNullable(serviceUsuario.save(currentUsuario.get()));
				
			} catch (DataAccessException e) {
				
				response.put("mensaje", "Error al actualizar el uusuario en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			response.put("Mensaje", "El usaurio ha sido actualizado");
			response.put("Barbero: ", usuarioUpdate);
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		}
		
		
		
		/////////////////// DELTE USUARIO http://localhost:8080/usuario/consult/id
		/////////////////// ////////////////
		@DeleteMapping("/delete/{id}")
		public ResponseEntity<?> deletebarbershop(@PathVariable(value = "id") Long id) {
			

			Map<String, Object> response = new HashMap<>();
			

			try {
				
				serviceUsuario.deletById(id);
				
			} catch (DataAccessException e) {
				
				response.put("mensaje", "Error al eliminar el usaurio en la base de datos");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			response.put("mensaje","El usuario se ha eliminado con exito! ");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		}
		
		

		/////////////////// CONSULT ALL USUARIOS
		/////////////////// http://localhost:8080/barbershop/consult ////////////////
		@GetMapping("/consultall")
		public List<Usuario> getAllBarbershop() {

			List<Usuario> usuarios = StreamSupport.stream(serviceUsuario.findAll().spliterator(),false)
					.collect(Collectors.toList());

			return usuarios;
		}
		
		
		
		
		
		
		
		
		
		

}
