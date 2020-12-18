/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.finalproject.POJO;

import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author deepak_lokwani
 */
@Entity
@Table(name="clothing_products")
@PrimaryKeyJoinColumn(name="product_id")
public class ClothingProduct extends Product {
	@ElementCollection()
	@LazyCollection(LazyCollectionOption.FALSE)
    private List<String> size;
	@ElementCollection()
	@LazyCollection(LazyCollectionOption.FALSE)
	@CollectionTable(name= "cloth_spec")
    private Map<String,String> spec;
    

	public ClothingProduct() {
		
    }  
  


    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }

    public Map<String, String> getSpec() {
        return spec;
    }

    public void setSpec(Map<String, String> spec) {
        this.spec = spec;
    }
    
}
