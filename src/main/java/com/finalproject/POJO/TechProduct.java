/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.finalproject.POJO;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author deepak_lokwani
 */
@Entity
@Table(name="tech_products")
@PrimaryKeyJoinColumn(name="product_id")
public class TechProduct extends Product {
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="tech_sepc")
    private Map<String,String> spec;


    public TechProduct() {
    }

    public Map<String,String> getSpec() {
        return spec;
    }

    public void setSpec(Map<String,String> spec) {
        this.spec = spec;
    }
    
}
