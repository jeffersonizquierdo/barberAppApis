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

import com.barberapp.entities.Barber;

import com.barberapp.services.barber.ServiceBarber;


@CrossOrigin(origins = {"http://localhost:4200/", "https://barberappfronted.pages.dev/"})
@RestController
@RequestMapping("barber")
public class ControllerBarber {
	
	@Autowired
	private ServiceBarber barberService;
	
	
	 /////////////////// BARBER REGISTRAR   http://localhost:8080/barber/save ////////////////
	@PostMapping("/save")
	public ResponseEntity<?> create (@RequestBody Barber barber){
		
		Barber newBarber = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			
			newBarber = barberService.save(barber);
			
		} catch (DataAccessException e) {
			
			response.put("Mensaje", "Error al registrar");
			response.put("Error", e.getMessage().concat(": ").concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("menssaje","El usuario barbero ha sido creado con exito");
		response.put("Barbero: ", newBarber);
		return new ResponseEntity<Map<String, Object >>(response, HttpStatus.CREATED);
		
	}
	
	
	/////////////////// CONSULT BARBER   http://localhost:8080/barber/consult/ID ////////////////
	@GetMapping("/consult/{id}")
	public ResponseEntity<?> consultBarberId(@PathVariable(value = "id") Long id){
		
		Optional<Barber> barber = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			barber =barberService.findById(id);
			
		} catch (DataAccessException e) {
			response.put("Mensaje", "Error al hacer consulta en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (!barber.isPresent()) {
			response.put("Mensaje", "EL barbero con el ID ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return ResponseEntity.ok(barber);
	}
	
	/////////////////// UPDATE BARBER   http://localhost:8080/barber/update/ID ////////////////
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?>update(@RequestBody Barber newBarber,@PathVariable (value = "id")Long idBarber ){
		
		Optional<Barber> currentBarber = barberService.findById(idBarber);
		Optional<Barber> barberUpdate = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if(!currentBarber.isPresent()) {
			response.put("Mensaje", "No se pudo editar el barbero con el ID ".concat(idBarber.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {

			currentBarber.get().setAge(newBarber.getAge());
			currentBarber.get().setDescription(newBarber.getDescription());
			currentBarber.get().setPassword(newBarber.getPassword());
			currentBarber.get().setCellphone(newBarber.getCellphone());
			currentBarber.get().setCity(newBarber.getCity());
			currentBarber.get().setNickname(newBarber.getNickname());
			currentBarber.get().setAge(newBarber.getAge());
			currentBarber.get().setPhoto(newBarber.getPhoto());
			currentBarber.get().setQualification(newBarber.getQualification());
			currentBarber.get().setBarbershop(newBarber.getBarbershop());
			
			barberUpdate = Optional.ofNullable(barberService.save(currentBarber.get()));
			
		} catch (DataAccessException e) {
			
			response.put("Mensaje", "Error al actualizar el barbero en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("Mensaje", "El barbero ha sido actualizado");
		response.put("Barbero: ", barberUpdate);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	/////////////////// DELETE BARBER   http://localhost:8080/barber/delete/ID ////////////////
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable(value = "id")  Long id){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			
			barberService.deletById(id);
			
		} catch (DataAccessException e) {

			response.put("Mensaje", "Error al eliminar el barbero en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("Mensaje","El barbero se ha eliminado con exito! ");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
	
	
	/////////////////// CONSULT ALL BARBER   http://localhost:8080/barber/consultall ///////////////	
	@GetMapping("/consultall")
	public  List<Barber> consultAllUsers(){
		
		
		List<Barber> barbers = StreamSupport
				.stream(barberService.findAll().spliterator(), false)
				.collect(Collectors.toList());
		
		return  barbers;
	}

}