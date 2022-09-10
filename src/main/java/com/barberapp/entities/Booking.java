package com.barberapp.entities;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table (name = "booking ")
public class Booking implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long  id;

	@JsonIgnoreProperties(value={"bokings","hibernateLazyInitializer","handler"},allowSetters = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="barber", referencedColumnName = "id")
	private Barber barber;
	
	@Column(name="reservation_date")
	private Date reservation_date;

	@JsonIgnoreProperties(value={"bokings","hibernateLazyInitializer","handler"},allowSetters = true)
	@ManyToOne @JoinColumn (name = "barbershop", referencedColumnName = "id")
	private Barbershop barbershop;

	@JsonIgnoreProperties(value={"bokings","hibernateLazyInitializer","handler"},allowSetters = true)
	@ManyToOne @JoinColumn (name = "customer", referencedColumnName = "id")
	private Customer customer;
	
	@Column(name="completed")
    private Boolean completed ;


	public Booking() {
		super();
	}


	public Booking(Long id, Barber barber, Date reservation_date, Barbershop barbershop, Customer customer,
			Boolean completed) {
		super();
		this.id = id;
		this.barber = barber;
		this.reservation_date = reservation_date;
		this.barbershop = barbershop;
		this.customer = customer;
		this.completed = completed;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Barber getBarber() {
		return barber;
	}


	public void setBarber(Barber barber) {
		this.barber = barber;
	}


	public Date getReservation_date() {
		return reservation_date;
	}


	public void setReservation_date(Date reservation_date) {
		this.reservation_date = reservation_date;
	}


	public Barbershop getBarbershop() {
		return barbershop;
	}


	public void setBarbershop(Barbershop barbershop) {
		this.barbershop = barbershop;
	}


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public Boolean getCompleted() {
		return completed;
	}


	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}


	@Override
	public String toString() {
		return "Booking [id=" + id + ", barber=" + barber + ", reservation_date=" + reservation_date + ", barbershop="
				+ barbershop + ", customer=" + customer + ", completed=" + completed + "]";
	}
	
}
