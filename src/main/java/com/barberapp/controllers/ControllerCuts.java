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


import com.barberapp.entities.Cuts;
import com.barberapp.services.cuts.ServiceCuts;

@CrossOrigin(origins = {"http://localhost:4200/", "https://barberappfronted.pages.dev/"})
@RestController
@RequestMapping("cuts")
public class ControllerCuts {
	@Autowired
	private ServiceCuts cutService;
	
	
	 /////////////////// CUTS REGISTRAR   http://localhost:8080/cuts/save ////////////////
		@PostMapping("/save")
		public ResponseEntity<?> create (@RequestBody Cuts cuts){
			
			Cuts newCuts = null;
			Map<String, Object> response = new HashMap<>();
			
			try {
				
				newCuts = cutService.save(cuts);
				
			} catch (DataAccessException e) {
				
				response.put("Mensaje", "Error al hacer insert en la base de datos");
				response.put("Error", e.getMessage().concat(": ").concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			response.put("menssaje","El usuario cut ha sido creado con exito");
			response.put("Barbero: ", newCuts);
			return new ResponseEntity<Map<String, Object >>(response, HttpStatus.CREATED);
			
		}
		
		
	/////////////////// CONSULT CUTS   http://localhost:8080/cuts/consult/ID ////////////////
	@GetMapping("/consult/{id}")
	public ResponseEntity<?> consultCutsId(@PathVariable(value = "id") Long id){
	
		Optional<Cuts> cuts = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			cuts =cutService.findById(id);
			
		} catch (DataAccessException e) {
			response.put("Mensaje", "Error al hacer consulta en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (!cuts.isPresent()) {
			response.put("Mensaje", "EL cut con el ID ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
			return ResponseEntity.ok(cuts);
	}
		
	/////////////////// UPDATE CUTS   http://localhost:8080/cuts/update/ID ////////////////
		
	@PutMapping("/update/{id}")
	public ResponseEntity<?>update(@RequestBody Cuts newcuts,@PathVariable (value = "id")Long idcut ){
		
		Optional<Cuts> currentcuts = cutService.findById(idcut);
		Optional<Cuts> cutsUpdate = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if(!currentcuts.isPresent()) {
		response.put("Mensaje", "No se pudo editar el cut con el ID ".concat(idcut.toString().concat(" no existe en la base de datos")));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		try {
		
		
		currentcuts.get().setDescription(newcuts.getDescription());
		cutsUpdate = Optional.ofNullable(cutService.save(currentcuts.get()));
		
		} catch (DataAccessException e) {
		
		response.put("Mensaje", "Error al actualizar el cut en la base de datos");
		response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("Mensaje", "El cut ha sido actualizado");
		response.put("Barbero: ", cutsUpdate);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	

	/////////////////// CONSULT ALL 	CUTS   http://localhost:8080/cuts/consultall ///////////////	
	@GetMapping("/consultall")
	public  List<Cuts> consultAllUsers(){
		
		
		List<Cuts> cuts = StreamSupport
				.stream(cutService.findAll().spliterator(), false)
				.collect(Collectors.toList());
		
		return  cuts;
	}
	
	/////////////////// DELETE CUTS   http://localhost:8080/cuts/delete/ID ////////////////
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable(value = "id")  Long id){
	
		Map<String, Object> response = new HashMap<>();
		
		try {
		
			cutService.deleteById(id);
		
		} catch (DataAccessException e) {
		
		response.put("Mensaje", "Error al eliminar el cuts en la base de datos");
		response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("Mensaje","El cut se ha eliminado con exito! ");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

		
}
