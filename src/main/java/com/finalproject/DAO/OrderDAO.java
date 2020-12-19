package com.finalproject.DAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.finalproject.POJO.Order;

/**
 * 
 * @author Deepak_Lokwani
 * 
 * NUID: 001316769
 * 
 * Project name: Finalproject
 * Package name: com.finalproject.DAO
 *
 */
public class OrderDAO {

	public OrderDAO() {
		// TODO Auto-generated constructor stub
	}
	private static EntityManagerFactory entityManagerFactory= Persistence.createEntityManagerFactory("orm");
	   private EntityManager entityManager= null;
	   public EntityManager getEntityManager() {
		   if(entityManager==null||!entityManager.isOpen()) {
			   entityManager=entityManagerFactory.createEntityManager();
		   }
		   return entityManager;
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
	   public boolean addOrder(Order order) {
		   try {
			 beginTransaction();
			 getEntityManager().persist(order);
			 System.out.println("done!!!!!");
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
}
