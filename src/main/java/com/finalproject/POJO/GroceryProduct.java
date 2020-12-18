/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.finalproject.POJO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author deepak_lokwani
 */
@Entity
@Table(name="grocery_products")
@PrimaryKeyJoinColumn(name="product_id")
public class GroceryProduct extends Product {
	@Column(nullable = false)
    private String unit;

    public GroceryProduct() {
    }
   
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    
}

