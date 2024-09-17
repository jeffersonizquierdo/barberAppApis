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
import com.barberapp.entities.Images;
import com.barberapp.services.barbershop.ServiceBarbershop;
import com.barberapp.services.images.ServiceImages;


@CrossOrigin(origins = {"http://localhost:4200/", "https://barberappfronted.pages.dev/"})
@RestController
@RequestMapping("images")
public class ControllerImages {

	@Autowired
	private ServiceImages serviceImages;
	
	@Autowired private ServiceBarbershop serviceBarbershop;
		
	
	// Create a new Image  http://localhost:8080/images/save
	@PostMapping("/save")
	public ResponseEntity<?> create(@RequestBody Images images) {
		
		Images newImages = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			newImages= serviceImages.save(images);
			
		}catch (DataAccessException e) {
			
			response.put("Mensaje", "Error al hacer insert en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
			response.put("menssaje","El catalogo ha sido creado con exito");
			response.put("Barbero: ", newImages);
			return new ResponseEntity<Map<String, Object >>(response, HttpStatus.CREATED);
			
		}
	
	// read an image http://localhost:8080/images/consult/id
	@GetMapping("/consult/{id}")
	public ResponseEntity<?> read(@PathVariable(value = "id") Long id) {

		Optional<Images> images = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			images =serviceImages.findById(id);
			
		} catch (DataAccessException e) {
			response.put("Mensaje", "Error al hacer consulta en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (!images.isPresent()) {
			response.put("Mensaje", "EL catalogo con el ID ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok(images);
	}

	
	// update an image http://localhost:8080/images/update/id
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@RequestBody Images imagesDetails, @PathVariable(value = "id") Long ImagesId) {
		
		Optional<Images> images = serviceImages.findById(ImagesId);
		Optional<Images> imagesUpdate = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if (!images.isPresent()) {
			response.put("Mensaje", "No se pudo editar el catalogo con el ID ".concat(ImagesId.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try {
			images.get().setName(imagesDetails.getName());
			images.get().setId(imagesDetails.getId());
			images.get().setDescription(imagesDetails.getDescription());
			images.get().setUrl(imagesDetails.getUrl());
			
			imagesUpdate = Optional.ofNullable(serviceImages.save(images.get()));
		
		}catch (DataAccessException e) {
			response.put("Mensaje", "Error al actualizar el catalogo en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("Mensaje", "El catalogo ha sido actualizado");
		response.put("Barbero: ", imagesUpdate);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	// delete an image http://localhost:8080/images/delete/id
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete (@PathVariable(value = "id") Long id){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			serviceImages.deleteById(id);
			
		} catch (DataAccessException e) {
			response.put("Mensaje", "Error al eliminar el catalogo en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		response.put("Mensaje","El catalogo se ha eliminado con exito! ");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	
	/////////////////// CONSULTALL CUSTOMER http://localhost:8080/images/consutlall/id
	/////////////////// ////////////////
	@GetMapping("/catalogue")
	public List<Images> consultCatalogue(){
		

		List<Images> catalogue = StreamSupport.stream(serviceImages.findAll().spliterator(), false)
				.collect(Collectors.toList());
		
		return catalogue;
		
	}

	@GetMapping("/consultall")
	public  List<Images> consultAllPromotions(){
		
		
		List<Images> images = StreamSupport
				.stream(serviceImages.findAll().spliterator(), false)
				.collect(Collectors.toList());
		
		return  images;
	}
}
