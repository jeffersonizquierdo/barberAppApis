package com.barberapp.controllers;


import java.util.HashMap;  
import java.util.ArrayList;
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

import com.barberapp.entities.Barber;
import com.barberapp.entities.Barbershop;
import com.barberapp.entities.Images;
import com.barberapp.entities.Promotions;
import com.barberapp.entities.Publication;
import com.barberapp.services.barbershop.ServiceBarbershop;



@CrossOrigin(origins = {"http://localhost:4200/", "https://barberappfronted.pages.dev/"})
@RestController
@RequestMapping("/barbershop")
public class ControllerBarbershop {

	@Autowired
	private ServiceBarbershop serviceBarbershop;

	@GetMapping("hola")
	public String saludo() {

		return "Saludo desde controlado barbershop";
	}

	/////////////////// BARBERSHOP REGISTRAR http://localhost:8080/barbershop/save
	/////////////////// ////////////////
	@PostMapping("/save")
	public ResponseEntity<?> createBarbershop(@RequestBody Barbershop barbershop) {

		Barbershop newBarbershop = null;
		Map<String, Object> response = new HashMap<>();

		try {
			
			
			newBarbershop = serviceBarbershop.save(barbershop);


		} catch (DataAccessException e) {

			response.put("Mensaje", "Error al hacer insert en la base de datos");
			response.put("Error", e.getMessage().concat(": ")
					.concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("menssaje", "El usuario barberia ha sido creado con exito");
		response.put("Barberia: ", newBarbershop);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	/////////////////// CONSULT BARBERSHOP
	/////////////////// http://localhost:8080/barbershop/consult/id ////////////////
	@GetMapping("/consult/{id}")
	public ResponseEntity<?> consultBarbershopId(@PathVariable(value = "id") Long id) {

		Optional<Barbershop> barbershop = null;
		Map<String, Object> response = new HashMap<>();

		try {

			barbershop = serviceBarbershop.findById(id);

		} catch (DataAccessException e) {

			response.put("Mensaje", "Error al hacer consulta en la base de datos");
			response.put("Error", e.getMessage().concat(": ")
					.concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (!barbershop.isPresent()) {

			response.put("Mensaje",
					"La barberia con el ID ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok(barbershop);
	}

	/////////////////// UPDATE BARBERSHOP http://localhost:8080/barbershop/update/id
	/////////////////// ////////////////
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateBarbershop(@RequestBody Barbershop newBarbershop,
			@PathVariable(value = "id") Long id) {

		Optional<Barbershop> currentBarbershop = serviceBarbershop.findById(id);
		Barbershop barbershopUpdate = null;
		Map<String, Object> response = new HashMap<>();

		if (!currentBarbershop.isPresent()) {
			response.put("Mensaje", "No se pudo editar la barberia con el ID "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {

			currentBarbershop.get().setDescription(newBarbershop.getDescription());
			currentBarbershop.get().setPassword(newBarbershop.getPassword());
			currentBarbershop.get().setCellphone(newBarbershop.getCellphone());
			currentBarbershop.get().setCity(newBarbershop.getCity());
			currentBarbershop.get().setNickname(newBarbershop.getNickname());
			currentBarbershop.get().setPhoto(newBarbershop.getPhoto());
			currentBarbershop.get().setLocation(newBarbershop.getLocation());
			currentBarbershop.get().setQualification(newBarbershop.getQualification());

			barbershopUpdate = serviceBarbershop.save(currentBarbershop.get());

		} catch (DataAccessException e) {
			response.put("Mensaje", "Error al actualizar la barberia en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("Mensaje", "El cliente ha sido actualizado");
		response.put("Barbero: ", barbershopUpdate);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	/////////////////// DELTE BARBERSHOP http://localhost:8080/barbershop/consult/id
	/////////////////// ////////////////
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deletebarbershop(@PathVariable(value = "id") Long id) {
		

		Map<String, Object> response = new HashMap<>();
		

		try {
			serviceBarbershop.deleteById(id);
			
		} catch (DataAccessException e) {
			
			response.put("Mensaje", "Error al eliminar la barberia en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("Mensaje","La barberia se ha eliminado con exito! ");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	/////////////////// CONSULT ALL BARBERSHOP
	/////////////////// http://localhost:8080/barbershop/consult ////////////////
	@GetMapping("/consultall")
	public List<Barbershop> getAllBarbershop() {

		List<Barbershop> barbershops = StreamSupport.stream(serviceBarbershop.findAll().spliterator(), false)
				.collect(Collectors.toList());

		return barbershops;
	}
	
	
/////////////////// CONSULT BARBER BARBERSHOP
/////////////////// http://localhost:8080/barbershop/consultbarber/id ////////////////
	@GetMapping("/consultbarber/{id}")
	public List<Barber> consultBarbersId(@PathVariable(value = "id") Long id) {
	
	
		Optional<Barbershop> barbershop = null;
		Map<String, Object> response = new HashMap<>();
	
	try {
	
		barbershop = serviceBarbershop.findById(id);
	
	} catch (DataAccessException e) {
		List<Barber> barber;
		response.put("Mensaje", "Error al hacer consulta en la base de datos");
		response.put("Error", e.getMessage().concat(": ")
		.concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
		return barber= new ArrayList<Barber>()  ;
	}
	
	if (!barbershop.isPresent()) {
		List<Barber> barber;
		response.put("Mensaje",
		"La barberia con el ID ".concat(id.toString().concat(" no existe en la base de datos")));
		return barber= new ArrayList<Barber>();
	}
	
	return barbershop.get().getListBarbers();
	}

	
/////////////////// CONSULT CATALOGUE BARBERSHOP
/////////////////// http://localhost:8080/barbershop/consultCatalogue/id ////////////////
	@GetMapping("/consultCatalogue/{id}")
	public List<Images> consultCatalogueBarbershopId(@PathVariable(value = "id") Long id) {
	
	
	Optional<Barbershop> barbershop = null;
	Map<String, Object> response = new HashMap<>();
	
	try {
	
		barbershop = serviceBarbershop.findById(id);
	
	} catch (DataAccessException e) {
	List<Images> catalogue;
	response.put("Mensaje", "Error al hacer consulta en la base de datos");
	response.put("Error", e.getMessage().concat(": ")
			.concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
	return catalogue= new ArrayList<Images>()  ;
	}
	
	if (!barbershop.isPresent()) {
		List<Images> catalogue;
	response.put("Mensaje",
			"La barberia con el ID ".concat(id.toString().concat(" no existe en la base de datos")));
	return catalogue= new ArrayList<Images>();
	}
	
	return barbershop.get().getCatalogue();
	}
	
	/////////////////// CONSULT PROMOTIO BARBERSHOP
	/////////////////// http://localhost:8080/barbershop/consultpromotion/id ////////////////
	@GetMapping("/consultpromotion/{id}")
	public List<Promotions> consultPromotionBarbershopId(@PathVariable(value = "id") Long id) {
	
	
	Optional<Barbershop> barbershop = null;
	Map<String, Object> response = new HashMap<>();
	
	try {
	
	barbershop = serviceBarbershop.findById(id);
	
	} catch (DataAccessException e) {
	List<Promotions> promotions;
	response.put("Mensaje", "Error al hacer consulta en la base de datos");
	response.put("Error", e.getMessage().concat(": ")
	.concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
	return promotions= new ArrayList<Promotions>()  ;
	}
	
	if (!barbershop.isPresent()) {
		List<Promotions> promotions;
	response.put("Mensaje",
	"La barberia con el ID ".concat(id.toString().concat(" no existe en la base de datos")));
	return promotions= new ArrayList<Promotions>();
	}
	return barbershop.get().getPromotion();
	
	
	}
	
	/////////////////// CONSULT PUBLICATION BARBERSHOP
	/////////////////// http://localhost:8080/barbershop/consultpublication/id ////////////////
	@GetMapping("/consultpublication/{id}")
	public List<Publication> consultpublicatiopnId(@PathVariable(value = "id") Long id) {
	
	
	Optional<Barbershop> barbershop = null;
	Map<String, Object> response = new HashMap<>();
	
	try {
	
		barbershop = serviceBarbershop.findById(id);
		
	} catch (DataAccessException e) {
		List<Publication> publication;
		response.put("Mensaje", "Error al hacer consulta en la base de datos");
		response.put("Error", e.getMessage().concat(": ")
		.concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
		return publication= new ArrayList<Publication>()  ;
	}
	
		if (!barbershop.isPresent()) {
			List<Publication> publication;
		response.put("Mensaje",
		"La barberia con el ID ".concat(id.toString().concat(" no existe en la base de datos")));
		return publication= new ArrayList<Publication>();
	}
		
		return barbershop.get().getPublications();
	}
	
	
}
