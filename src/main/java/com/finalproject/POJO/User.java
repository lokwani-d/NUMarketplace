/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.finalproject.POJO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


/**
 *
 * @author deepak_lokwani
 */
@Entity
@Table(name="users")
@PrimaryKeyJoinColumn(name = "person_id")
public class User extends Person {
    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(name = "user_cart")
    @MapKeyJoinColumn(name="product_id")
    private Map<Product,Integer> cart;
    @OneToMany(mappedBy="user" ,cascade = {CascadeType.MERGE,CascadeType.REMOVE})
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderBy("orderedDate")
    private List<Order> orders;
    @OneToMany(mappedBy="user", cascade = {CascadeType.MERGE,CascadeType.REMOVE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Review> reviews;
    
    public User() {
        setRole("user");
    }

	public Map<Product,Integer> getCart() {
		return cart;
	}

	public void setCart(Map<Product,Integer> cart) {
		this.cart = cart;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	

    
    
    
}
