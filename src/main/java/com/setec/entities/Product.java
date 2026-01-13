package com.setec.entities;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="tblProduct")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private Double price;
	private int qty;
	@JsonIgnore
	private String imageUrl;
	
	public double getAmount() {
		return this.price * this.qty;
	}
	
	public String getFullImageUrl() {
		return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+imageUrl;
	}
	

}
