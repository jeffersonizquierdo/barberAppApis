package com.barberapp.entities;

import java.io.Serializable; 
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table (name = "barbershops")
public class Barbershop implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)private Long id;
	@Column (name = "email", nullable = false, unique = true, length = 200) private String email;
	@Column (name = "password", nullable = false) private String password;
	@Column (name = "nickname", nullable = false) private String nickname;
	@Column (name = "city", nullable = false) private String city;
	@Column (name = "cellphone", nullable = false, length = 10) private String cellphone;
	@Column (name = "type_user", nullable = false) private int typeUser;
	@Column (name = "photo", nullable = true) private String photo;
	@Column (name = "description") private String description;
	@Column (name = "location") private String location;
	@Column (name = "qualification") private Double qualification;

	@JsonIgnoreProperties(value={"owner","hibernateLazyInitializer","handler"},allowSetters = true)
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "barbershop", cascade=CascadeType.ALL)
	private List<Barber> listBarbers;
	
	@OneToMany (mappedBy = "barbershop", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Booking> bokings;

	@JsonIgnoreProperties(value={"owner","hibernateLazyInitializer","handler"},allowSetters = true)



	@OneToMany(fetch=FetchType.LAZY, mappedBy = "owner", cascade=CascadeType.ALL)
	private List<Promotions> promotion; 

	
	@JsonIgnoreProperties(value={"owner","hibernateLazyInitializer","handler"},allowSetters = true)
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "owner") 
	private List<Publication> publication;

	@JsonIgnoreProperties(value={"owner","hibernateLazyInitializer","handler"},allowSetters = true)
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "owner")
	//@OneToMany (mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) 
	private List<Images> catalogue;

	@JsonIgnoreProperties(value={"id_barbershop","hibernateLazyInitializer","handler"},allowSetters = true)
	@OneToMany(fetch=FetchType.LAZY, mappedBy = "id_barbershop", cascade=CascadeType.ALL)
	//@OneToMany (mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) 
	private List<Publication> publications;


	public Barbershop() {
		super();
		
		// TODO Auto-generated constructor stub
	}
<<<<<<< HEAD



	public Barbershop(Long id, String email, String password, String nickname, String city, String cellphone,
			int typeUser, String photo, String description, String location, Double qualification,
			List<Barber> listBarbers, List<Booking> bokings, List<Promotions> promotion, List<Publication> publication,
			List<Images> catalogue, List<Publication> publications, List<Cuts> cuts) {
=======

	public Barbershop(Long id, String email, String password, String nickname, String city, String cellphone,
			int typeUser, String photo, String description, String location, Double qualification,
			List<Barber> listBarbers, List<Booking> bokings, List<Promotions> promotion, List<Images> catalogue,
			List<Publication> publications) {
>>>>>>> 48f7f8f4e258a87fa26e21158056a66a9bebf0f2
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.city = city;
		this.cellphone = cellphone;
		this.typeUser = typeUser;
		this.photo = photo;
		this.description = description;
		this.location = location;
		this.qualification = qualification;
		this.listBarbers = listBarbers;
		this.bokings = bokings;
		this.promotion = promotion;
		this.publication = publication;
		this.catalogue = catalogue;
		this.publications = publications;
<<<<<<< HEAD
		this.cuts = cuts;
	}



=======
	
	}

>>>>>>> 48f7f8f4e258a87fa26e21158056a66a9bebf0f2
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getNickname() {
		return nickname;
	}



	public void setNickname(String nickname) {
		this.nickname = nickname;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}



	public String getCellphone() {
		return cellphone;
	}



	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}



	public int getTypeUser() {
		return typeUser;
	}



	public void setTypeUser(int typeUser) {
		this.typeUser = typeUser;
	}



	public String getPhoto() {
		return photo;
	}



	public void setPhoto(String photo) {
		this.photo = photo;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getLocation() {
		return location;
	}



	public void setLocation(String location) {
		this.location = location;
	}



	public Double getQualification() {
		return qualification;
	}



	public void setQualification(Double qualification) {
		this.qualification = qualification;
	}



	public List<Barber> getListBarbers() {
		return listBarbers;
	}



	public void setListBarbers(List<Barber> listBarbers) {
		this.listBarbers = listBarbers;
	}



	public List<Booking> getBokings() {
		return bokings;
	}



	public void setBokings(List<Booking> bokings) {
		this.bokings = bokings;
	}



	public List<Promotions> getPromotion() {
		return promotion;
	}



	public void setPromotion(List<Promotions> promotion) {
		this.promotion = promotion;
	}



	public List<Publication> getPublication() {
		return publication;
	}



	public void setPublication(List<Publication> publication) {
		this.publication = publication;
	}



	public List<Images> getCatalogue() {
		return catalogue;
	}



	public void setCatalogue(List<Images> catalogue) {
		this.catalogue = catalogue;
	}

<<<<<<< HEAD


=======
>>>>>>> 48f7f8f4e258a87fa26e21158056a66a9bebf0f2
	public List<Publication> getPublications() {
		return publications;
	}

<<<<<<< HEAD


=======
>>>>>>> 48f7f8f4e258a87fa26e21158056a66a9bebf0f2
	public void setPublications(List<Publication> publications) {
		this.publications = publications;
	}



<<<<<<< HEAD
	public List<Cuts> getCuts() {
		return cuts;
	}



	public void setCuts(List<Cuts> cuts) {
		this.cuts = cuts;
	}
	
	

	







=======
>>>>>>> 48f7f8f4e258a87fa26e21158056a66a9bebf0f2

	
	
}
