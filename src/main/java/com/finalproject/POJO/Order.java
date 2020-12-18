/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.finalproject.POJO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author deepak_lokwani
 */
@Entity
@Table(name="orders")
public class Order {
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@GeneratedValue(
	    strategy= GenerationType.AUTO,
	    generator="native"
	)
	@GenericGenerator(
	    name = "native",
	    strategy = "native"
	)
	private int id;
    private Timestamp orderedDate;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private User user;
    @ManyToMany(cascade = {CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Seller> sellers;
    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @MapKeyJoinColumn(name="product_id")
    private Map<Product,Integer> products;
    private float totalPrice;
    private String cardNumber;
    @Column(nullable=false)
    private String billingDetails;
    public Order() {
    }
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Timestamp getOrderedDate() {
		return orderedDate;
	}
	public void setOrderedDate(Timestamp orderedDate) {
		this.orderedDate = orderedDate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Set<Seller> getSellers() {
		return sellers;
	}
	public void setSellers(Set<Seller> sellers) {
		this.sellers = sellers;
	}
	public Map<Product, Integer> getProducts() {
		return products;
	}
	public void setProducts(Map<Product, Integer> products) {
		this.products = products;
	}
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getBillingDetails() {
		return billingDetails;
	}
	public void setBillingDetails(String billingDetails) {
		this.billingDetails = billingDetails;
	}
	


}