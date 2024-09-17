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

import com.barberapp.entities.Barbershop;
import com.barberapp.entities.Bonding;
import com.barberapp.services.bonding.ServiceBonding;

@CrossOrigin(origins = {"http://localhost:4200/", "https://barberappfronted.pages.dev/"})
@RestController
@RequestMapping("/bonding")
public class ControllerBonding {
	
	
	@Autowired
	private ServiceBonding serviceBonding;
	
	
	@GetMapping("hola")
	public String saludo() {

		return "Saludo desde controlado bonding";
	}

	
	
	/////////////////// BONDING REGISTRAR http://localhost:8080/bonding/save
	/////////////////// ////////////////
	@PostMapping("/save")
	public ResponseEntity<?> createBonding(@RequestBody Bonding bonding) {

		Bonding newBonding = null;
		Map<String, Object> response = new HashMap<>();

		try {
			
			
			newBonding = serviceBonding.save(bonding);


		} catch (DataAccessException e) {

			response.put("mensaje", "Error al hacer insert en la base de datos");
			response.put("error", e.getMessage().concat(": ")
					.concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("menssaje", "la vincualcion ha sido creado con exito");
		response.put("bonding: ", newBonding);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}
	
	
	
	
	/////////////////// CONSULT BONDING
	/////////////////// http://localhost:8080/bonding/consult/id ////////////////
	@GetMapping("/consult/{id}")
	public ResponseEntity<?> consultBondingId(@PathVariable(value = "id") Long id) {

		Optional<Bonding> bonding = null;
		Map<String, Object> response = new HashMap<>();

		try {

			bonding = serviceBonding.findById(id);

		} catch (DataAccessException e) {

			response.put("mensaje", "Error al hacer consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ")
					.concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (!bonding.isPresent()) {

			response.put("Mensaje",
					"La vinculacion con el ID ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok(bonding);
	}
	
	
	
	
	/////////////////// CONSULT BONDING
	/////////////////// http://localhost:8080/bonding/update/id ////////////////
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateBonding(@RequestBody Bonding newBonding,
			@PathVariable(value = "id") Long id) {

		Optional<Bonding> currentBonding = serviceBonding.findById(id);
		Bonding bondingUPdate = null;
		Map<String, Object> response = new HashMap<>();

		if (!currentBonding.isPresent()) {
			response.put("mensaje", "No se pudo editar la vinculacion con el ID "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {

			currentBonding.get().setAcceptance(newBonding.getAcceptance());

			bondingUPdate = serviceBonding.save(currentBonding.get());

		} catch (DataAccessException e) {
			response.put("Mensaje", "Error al actualizar la vinculacion en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("Mensaje", "la vinculacion ha sido actualizada");
		response.put("bonding: ", bondingUPdate);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	
	
	

	/////////////////// DELTE BONDING http://localhost:8080/bonding/consult

	/////////////////// DELTE BONDING http://localhost:8080/bonding/delete/id

	/////////////////// ////////////////
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteBondgin(@PathVariable(value = "id") Long id) {
		

		Map<String, Object> response = new HashMap<>();
		

		try {
			serviceBonding.deleteById(id);
			
		} catch (DataAccessException e) {
			
			response.put("mensaje", "Error al eliminar la vinculacion en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","La vinculacion se ha eliminado con exito! ");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	/////////////////// CONSULT ALL BARBERSHOP
	/////////////////// http://localhost:8080/bonding/consult ////////////////
	@GetMapping("/consultall")
	public List<Bonding> getAllBondings() {

		List<Bonding> bondings = StreamSupport.stream(serviceBonding.findAll().spliterator(), false)
				.collect(Collectors.toList());
		
		System.out.println(bondings);

		return bondings;
	}
	

}
