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

import com.barberapp.entities.Promotions;
import com.barberapp.services.promotions.ServicePromotions;

@CrossOrigin(origins = {"http://localhost:4200/", "https://barberappfronted.pages.dev/"})
@RestController
@RequestMapping("promotions")
public class ControllerPromotions {

	
	@Autowired
	private ServicePromotions promotionsService;
	
	 /////////////////// BARBER REGISTRAR   http://localhost:8080/promotions/save ////////////////
	@PostMapping("/save")
	public ResponseEntity<?> create (@RequestBody Promotions promotions){
		
		Promotions newPromotions = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			
			newPromotions = promotionsService.save(promotions);
			
		} catch (DataAccessException e) {
			
			response.put("Mensaje", "Error al hacer insert en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("menssaje","la promocion ha sido creado con exito");
		response.put("promocion: ", newPromotions);
		return new ResponseEntity<Map<String, Object >>(response, HttpStatus.CREATED);
		
	}
	
	
	/////////////////// CONSULT BARBER   http://localhost:8080/promotions/consult/ID ////////////////
	@GetMapping("/consult/{id}")
	public ResponseEntity<?> consultPromotionsId(@PathVariable(value = "id") Long id){
		
		Optional<Promotions> promotions = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			promotions =promotionsService.findById(id);
			
		} catch (DataAccessException e) {
			response.put("Mensaje", "Error al hacer consulta en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (!promotions.isPresent()) {
			response.put("Mensaje", "la promocion con el ID ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return ResponseEntity.ok(promotions);
	}
	
	/////////////////// UPDATE BARBER   http://localhost:8080/promotions/update/ID ////////////////
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?>update(@RequestBody Promotions newPromotions,@PathVariable (value = "id")Long idPromotions ){
		
		Optional<Promotions> currentPromotions = promotionsService.findById(idPromotions);
		Optional<Promotions> promotionsUpdate = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if(!currentPromotions.isPresent()) {
			response.put("Mensaje", "No se pudo editar la promoción con el ID ".concat(idPromotions.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {

			currentPromotions.get().setName(newPromotions.getName());
			currentPromotions.get().setDescription(newPromotions.getDescription());
			currentPromotions.get().setUrl(newPromotions.getUrl());
			
			
			promotionsUpdate = Optional.ofNullable(promotionsService.save(currentPromotions.get()));
			
		} catch (DataAccessException e) {
			
			response.put("Mensaje", "Error al actualizar la promocion en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("Mensaje", "La Promocion ha sido actualizado");
		response.put("Promocion: ", promotionsUpdate);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	/////////////////// DELETE BARBER   http://localhost:8080/promotions/delete/ID ////////////////
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deletePromotions(@PathVariable(value = "id")  Long id){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			
			promotionsService.deleteById(id);
			
		} catch (DataAccessException e) {

			response.put("Mensaje", "Error al eliminar la promocion en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("Mensaje","La promocion se ha eliminado con exito! ");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
	
	
	/////////////////// CONSULT ALL BARBER   http://localhost:8080/promotions/consultall ///////////////	
	@GetMapping("/consultall")
	public  List<Promotions> consultAllPromotions(){
		
		
		List<Promotions> promotions = StreamSupport
				.stream(promotionsService.findAll().spliterator(), false)
				.collect(Collectors.toList());
		
		return  promotions;
	}

}
