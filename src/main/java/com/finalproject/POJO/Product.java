/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.finalproject.POJO;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.google.gson.annotations.Expose;

/**
 *
 * @author deepak_lokwani
 */
@Entity
@Table(name="products")
@Inheritance(strategy = InheritanceType.JOINED)
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Expose
    private int product_id;
	@Column(nullable = false, unique = true)
	@Expose
    private String name;
	@Column(nullable = false)
	@Expose
    private float price;
	@Column(nullable = false)
	@Expose
    private int quantity;
	@Expose
    private String description;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name="seller_id")
    @LazyCollection(LazyCollectionOption.FALSE)
	private Seller seller;
    @OneToMany(mappedBy="product",cascade = {CascadeType.MERGE,CascadeType.REMOVE},orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Review> reviews;
    @Transient
	private CommonsMultipartFile photo;
    @Expose
    private String photoFile;
    private String type;
    private Timestamp addedAt;
    public Product() {
    }

    public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
	
    public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public CommonsMultipartFile getPhoto() {
		return photo;
	}

	public void setPhoto(CommonsMultipartFile photo) {
		this.photo = photo;
	}

	public String getPhotoFile() {
		return photoFile;
	}

	public void setPhotoFile(String photoFile) {
		this.photoFile = photoFile;
	}

	public Timestamp getAddedAt() {
		return addedAt;
	}

	public void setAddedAt(Timestamp addedAt) {
		this.addedAt = addedAt;
	}

}
