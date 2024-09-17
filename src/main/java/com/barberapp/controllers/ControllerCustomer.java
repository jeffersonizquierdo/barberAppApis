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

import com.barberapp.entities.Customer;
import com.barberapp.services.customer.ServiceCustomer;

@CrossOrigin(origins = {"http://localhost:4200/", "https://barberappfronted.pages.dev/"})
@RestController
@RequestMapping("customer")
public class ControllerCustomer {

	@Autowired(required = true)
	private ServiceCustomer serviceCustomer;

	@GetMapping("/hola")
	public String saludo() {

		return "saludo desde controlador customer";
	}

	/////////////////// CUSTOMER REGISTRAR http://localhost:8080/customer/save
	/////////////////// ////////////////
	@PostMapping("/save")
	public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {

		Customer newCustomer = null;
		Map<String, Object> response = new HashMap<>();

		try {

			newCustomer = serviceCustomer.save(customer);

		} catch (DataAccessException e) {

			response.put("Mensaje", "Error al hacer insert en la base de datos");
			response.put("Error", e.getMessage().concat(": ")
					.concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("menssaje", "El usuario cliente ha sido creado con exito");
		response.put("Barbero: ", newCustomer);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	/////////////////// CONSULT REGISTRAR http://localhost:8080/customer/consult/id
	/////////////////// ////////////////
	@GetMapping("/consult/{id}")

	public ResponseEntity<?> consultCustomerId(@PathVariable(value = "id") Long id) {

		Optional<Customer> customer = null;
		Map<String, Object> response = new HashMap<>();

		try {

			customer = serviceCustomer.findById(id);

		} catch (Exception e) {
			response.put("Mensaje", "Error al hacer consulta en la base de datos");
			response.put("Error", e.getMessage().concat(": ")
					.concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		if (customer == null) {
			response.put("Mensaje",
					"EL cliente con el ID ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok(customer);

	}

	/////////////////// CONSULT REGISTRAR http://localhost:8080/customer/update/id
	/////////////////// ////////////////
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateCustomer(@RequestBody Customer newCustomer, @PathVariable(value = "id") Long id) {

		Optional<Customer> currentCustomer = serviceCustomer.findById(id);
		Customer customerUpdate = null;
		Map<String, Object> response = new HashMap<>();
		
		if(!currentCustomer.isPresent()) {
			response.put("Mensaje", "No se pudo editar el cliente con el ID ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			currentCustomer.get().setAge(newCustomer.getAge());
			currentCustomer.get().setPassword(newCustomer.getPassword());
			currentCustomer.get().setCellphone(newCustomer.getCellphone());
			currentCustomer.get().setCity(newCustomer.getCity());
			currentCustomer.get().setNickname(newCustomer.getNickname());
			currentCustomer.get().setAge(newCustomer.getAge());
			currentCustomer.get().setPhoto(newCustomer.getPhoto());

			customerUpdate = serviceCustomer.save(currentCustomer.get());

		} catch (DataAccessException e) {

			response.put("Mensaje", "Error al actualizar el cliente en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("Mensaje", "El cliente ha sido actualizado");
		response.put("Barbero: ", customerUpdate);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	/////////////////// DELETE REGISTRAR http://localhost:8080/customer/delete/id
	/////////////////// ////////////////
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<>();
		
		try {
			
			serviceCustomer.deleteById(id);
			
		} catch (Exception e) {
			response.put("Mensaje", "Error al eliminar el cliente en la base de datos");
			response.put("Error", e.getMessage().concat(": ").concat(((NestedRuntimeException) e).getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("Mensaje","El cliente se ha eliminado con exito! ");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	/////////////////// CONSULTALL CUSTOMER http://localhost:8080/customer/consultall
	/////////////////// ////////////////
	@GetMapping("/consultall")
	public List<Customer> consultAllCustomers() {

		List<Customer> customers = StreamSupport.stream(serviceCustomer.findAll().spliterator(), false)
				.collect(Collectors.toList());

		return customers;
	}

}
