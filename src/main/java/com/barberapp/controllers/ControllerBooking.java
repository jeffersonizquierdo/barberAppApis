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

import com.barberapp.entities.Booking;

import com.barberapp.services.booking.ServiceBooking;

@CrossOrigin(origins = {"http://localhost:4200/", "https://barberappfronted.pages.dev/"})
@RestController
@RequestMapping("booking")
public class ControllerBooking {
	
@Autowired(required = true) private ServiceBooking serviceBooking;
	
	@GetMapping("hola")
	public String saludo() {
		
		return "saludo desde controlador Booking";
	}
	
	
	 /////////////////// BOKING REGISTRAR   http://localhost:8080/booking/save ////////////////
		@PostMapping("/save")
		public ResponseEntity<?> create (@RequestBody Booking booking){
			
			Booking newBooking = null;
			Map<String, Object> response = new HashMap<>();
			
			try {
				
				newBooking = serviceBooking.save(booking);
				
			} catch (DataAccessException e) {
				
				response.put("Mensaje", "Error al hacer insert en la base de datos");
				response.put("Error", e.getMessage().concat(": ").concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			response.put("menssaje","El booking  ha sido creado con exito");
			response.put("Barbero: ", newBooking);
			return new ResponseEntity<Map<String, Object >>(response, HttpStatus.CREATED);
			
		}
		
		
	/////////////////// CONSULT BOOKING   http://localhost:8080/booking/consult/ID ////////////////
		@GetMapping("/consult/{id}")
		public ResponseEntity<?> consultBopkingId(@PathVariable(value = "id") Long id){
		
			Optional<Booking> booking = null;
			Map<String, Object> response = new HashMap<>();
			
			try {
				booking =serviceBooking.findById(id);
				
			} catch (DataAccessException e) {
				response.put("Mensaje", "Error al hacer consulta en la base de datos");
				response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			if (!booking.isPresent()) {
				response.put("Mensaje", "EL booking con el ID ".concat(id.toString().concat(" no existe en la base de datos")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			
				return ResponseEntity.ok(booking);
		}
			
	
	/////////////////// UPDATE BOOKING   http://localhost:8080/booking/update/ID ////////////////
		@PutMapping("/update/{id}")
		public ResponseEntity<?>update(@RequestBody Booking newBooking,@PathVariable (value = "id")Long idbooking ){
			
			Optional<Booking> currentbooking = serviceBooking.findById(idbooking);
			Optional<Booking> BookingUpdate = null;
			
			Map<String, Object> response = new HashMap<>();
			
			if(!currentbooking.isPresent()) {
			response.put("Mensaje", "No se pudo editar el booking con el ID ".concat(idbooking.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			try {
			
		
			
			currentbooking.get().setReservationDate(newBooking.getReservationDate());
			currentbooking.get().setCompleted(newBooking.getCompleted());
			currentbooking.get().setCancelled(newBooking.getCancelled());
			currentbooking.get().setScore(newBooking.getScore());
			
			BookingUpdate = Optional.ofNullable(serviceBooking.save(currentbooking.get()));
			
			} catch (DataAccessException e) {
			
			response.put("Mensaje", "Error al actualizar el booking en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			response.put("Mensaje", "El booking ha sido actualizado");
			response.put("Barbero: ", BookingUpdate);
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		}
	

	
	/////////////////// CONSULT ALL USER   http://localhost:8080/booking/consultall ///////////////

		@GetMapping("/consultall")
		public  List<Booking> consultAllUsers(){
			
			
			List<Booking> booking = StreamSupport
					.stream(serviceBooking.findAll().spliterator(), false)
					.collect(Collectors.toList());
			
			return  booking;
		}


		/////////////////// DELETE BOOKING   http://localhost:8080/booking/delete/ID ////////////////
		@DeleteMapping("/delete/{id}")
		public ResponseEntity<?> deleteUser(@PathVariable(value = "id")  Long id){
		
			Map<String, Object> response = new HashMap<>();
			
			try {
			
				serviceBooking.deleteById(id);
			
			} catch (DataAccessException e) {
			
			response.put("Mensaje", "Error al eliminar el booking en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			response.put("Mensaje","El booking se ha eliminado con exito! ");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		}
	

}
