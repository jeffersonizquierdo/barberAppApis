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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barberapp.entities.Publication;
import com.barberapp.services.Publication.ServicePublication;

@CrossOrigin(origins = {"http://localhost:4200/", "https://barberappfronted.pages.dev/"})
@RestController
@RequestMapping("publication")
public class ControllerPublication {
	
	@Autowired 
	private ServicePublication servicePublication;
	

	
	
	@GetMapping("hola")
	public String saludo() {
		
		return "saludo desde controlador publication";
	}
	/////////////////// PUBLICATION REGISTRAR http://localhost:8080/publication/save/////////////////// ////////////////
	@PostMapping("/save")
	public ResponseEntity<?> createPublication(@RequestBody Publication publication) {
	
		Publication newPublication = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
		
			newPublication = servicePublication.save(publication);
		
		} catch (DataAccessException e) {
		
			response.put("Mensaje", "Error al hacer insert en la base de datos");
			response.put("Error", e.getMessage().concat(": ")
					.concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
			response.put("menssaje", "El usuario publicasion ha sido creado con exito");
			response.put("publicacion: ", newPublication);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	/////////////////// CONSULT PUBLICATION http://localhost:8080/publication/consult/id/////////////////// ////////////////
	@GetMapping("/consult/{id}")
	
	public ResponseEntity<?> consultPublicationId(@PathVariable(value = "id") Long id) {
	
		Optional<Publication> publication = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
		
			publication = servicePublication.findById(id);
		
		} catch (Exception e) {
			response.put("Mensaje", "Error al hacer consulta en la base de datos");
			response.put("Error", e.getMessage().concat(": ")
					.concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		if (!publication.isPresent()) {
			response.put("Mensaje",
					"la publicasion con el ID ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return ResponseEntity.ok(publication);
	
	}
	
	/////////////////// CONSULT REGISTRAR http://localhost:8080/publication/update/id/////////////////// ////////////////
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updatePublication	(@RequestBody Publication newPublication, @PathVariable(value = "id") Long id) {
	
		Optional<Publication> currentPublication = servicePublication.findById(id);
		Publication PublicationUpdate = null;
		Map<String, Object> response = new HashMap<>();
		
		if(!currentPublication.isPresent()) {
		response.put("Mensaje", "No se pudo editar la publicasion con el ID ".concat(id.toString().concat(" no existe en la base de datos")));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		try {
		;
		
		currentPublication.get().setDescription(newPublication.getDescription());
		currentPublication.get().setUrl(newPublication.getUrl());
		
		PublicationUpdate = servicePublication.save(currentPublication.get());
		
		} catch (DataAccessException e) {
		
		response.put("Mensaje", "Error al actualizar la publicasion en la base de datos");
		response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("Mensaje", "la publicasion ha sido actualizado");
		response.put("Barbero: ", PublicationUpdate);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	
	/////////////////// CONSULTALL PUBLICATION http://localhost:8080/publication/consultall/////////////////// ////////////////
	@GetMapping("/consultall")
	public List<Publication> consultAllCustomers() {

		List<Publication> publication = StreamSupport.stream(servicePublication.findAll().spliterator(), false)
				.collect(Collectors.toList());

		return publication;
	}
	
	/////////////////// DELETE REGISTRAR http://localhost:8080/publication/delete/id
	/////////////////// ////////////////
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<>();
		
		try {
			
			servicePublication.deleteById(id);
			
		} catch (Exception e) {
			response.put("Mensaje", "Error al eliminar la publicasion en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("Mensaje","la publicasion se ha eliminado con exito! ");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
	
	

}