/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.finalproject.DAO;

import com.finalproject.POJO.Person;
import com.finalproject.POJO.Product;
import com.finalproject.POJO.Seller;
import com.finalproject.POJO.User;
import com.finalproject.utils.Hash;

import java.sql.Timestamp;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
/**
 *
 * @author deepak_lokwani
 */
public class UserDAO {
   private static EntityManagerFactory entityManagerFactory= Persistence.createEntityManagerFactory("orm");
   private EntityManager entityManager= null;
   public EntityManager getEntityManager() {
	   if(entityManager==null||!entityManager.isOpen()) {
		   entityManager=entityManagerFactory.createEntityManager();
	   }
	   return entityManager;
   }
   public UserDAO() {
	   
   }
   public void beginTransaction() {
	   getEntityManager().getTransaction().begin();;
   }
   public void commit() {
	   getEntityManager().getTransaction().commit();
   }
   public void rollback() {
	   getEntityManager().getTransaction().rollback();
   }
   public void close() {
	   getEntityManager().close();
   }
   public boolean addUser(User user) {
	   try {
		   beginTransaction();
		   getEntityManager().persist(user);
		   commit();
		   
		   return true;
	   }
	   catch(Exception e) {
		   e.printStackTrace();
		   rollback();
		   return false;
	   }
	   finally {
		   close();
	   }
	   
   }
   public User getUserByEmail(String email) {
	   User person=null;
	   try {
		   beginTransaction();
		   CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		   CriteriaQuery<User> criteria= builder.createQuery(User.class);
		   Root<User> personRoot= criteria.from(User.class);
		   criteria.select(personRoot);
		   criteria.where(builder.equal(personRoot.get("email"), email));
		   person= getEntityManager().createQuery(criteria).getSingleResult();
		  commit();
		  return person;
	   }
	   catch (Exception e) {
		// TODO: handle exception
		  e.printStackTrace();
		  rollback();
		  return null;
	}
	   finally {
		   close();
		   
	   }
	   
   }
   public Person getPersonByEmail(String email) {
	   Person person=null;
	   try {
		   beginTransaction();
		   CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		   CriteriaQuery<Person> criteria= builder.createQuery(Person.class);
		   Root<Person> personRoot= criteria.from(Person.class);
		   criteria.select(personRoot);
		   criteria.where(builder.equal(personRoot.get("email"), email));
		   person= getEntityManager().createQuery(criteria).getSingleResult();
		  commit();
		  return person;
	   }
	   catch (Exception e) {
		// TODO: handle exception
		  e.printStackTrace();
		  rollback();
		  return null;
	}
	   finally {
		   close();
		   
	   }
	   
   }
   public Person getPersonByResetToken(String token) {
	   Person person=null;
	   try {
		   beginTransaction();
		   CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		   CriteriaQuery<Person> criteria= builder.createQuery(Person.class);
		   Root<Person> personRoot= criteria.from(Person.class);
		   criteria.select(personRoot);
		   criteria.where(builder.equal(personRoot.get("resetToken"), token));
		   person= getEntityManager().createQuery(criteria).getSingleResult();
		  commit();
		  return person;
	   }
	   catch (Exception e) {
		// TODO: handle exception
		  e.printStackTrace();
		  rollback();
		  return null;
	}
	   finally {
		   close();
		   
	   }
	   
   }
   public boolean deletePerson(Person person) {
	   try {
		   beginTransaction();
		   getEntityManager().remove(person);
		   commit();
		  return true;
	   }
	   catch (Exception e) {
		// TODO: handle exception
		   e.printStackTrace();
			  rollback();
			  return false;
	}
	   finally {
		   close();
	   }
   }
   public boolean updatePerson(Person person,String name,String email) {
	   try {
		   beginTransaction();
		   person.setEmail(email);
		   person.setName(name);
		   getEntityManager().merge(person);
		   commit();
		   return true;
	   }
	   catch (Exception e) {
		// TODO: handle exception
		   e.printStackTrace();
		   rollback();
		   return false;
	}
	   finally {
		   close();
	   }
   }
   public boolean mergePerson(Person person) {
	   try {
		   beginTransaction();
		   getEntityManager().merge(person);
		   commit();
		   return true;
	   }
	   catch (Exception e) {
		// TODO: handle exception
		   e.printStackTrace();
		   rollback();
		   return false;
	}
	   finally {
		   close();
	   }
   }
   public boolean addProductToCart(User user,Product product,int quantity) {
	   try {
		   beginTransaction();
		   Map<Product,Integer> cart= user.getCart();
		   cart.put(product,quantity);
		   user.setCart(cart);
		   getEntityManager().merge(user);
		   commit();
		   return true;
	   }
	   catch (Exception e) {
		// TODO: handle exception
		   e.printStackTrace();
		   rollback();
		   return false;
	}
	   finally {
		   close();
	   }
   }
   public boolean deleteProductFromCart(User user,Product product) {
	   try {
		   beginTransaction();
		   Map<Product,Integer> cart= user.getCart();
		   for(Product prod: cart.keySet()) {
			   if(prod.getProduct_id()==product.getProduct_id()) {
				   product=prod;
			   }
		   }
		   cart.remove(product);
		   user.setCart(cart);
		   getEntityManager().merge(user);
		   commit();
		   return true;
	   }
	   catch (Exception e) {
		// TODO: handle exception
		   e.printStackTrace();
		   rollback();
		   return false;
	}
	   finally {
		   close();
	   }
   }
   public boolean clearCart(User user) {
	   try {
		   beginTransaction();
		   Map<Product,Integer> map= new HashMap<Product,Integer>();
		   user.setCart(map);
		   getEntityManager().merge(user);
		   commit();
		   return true;
	   }
	   catch (Exception e) {
		// TODO: handle exception
		   e.printStackTrace();
		   rollback();
		   return false;
	}
	   finally {
		   close();
	}
   }
//   public 
}
