package com.finalproject.DAO;


import com.finalproject.POJO.ClothingProduct;
import com.finalproject.POJO.GroceryProduct;
import com.finalproject.POJO.Person;
import com.finalproject.POJO.Product;
import com.finalproject.POJO.Review;
import com.finalproject.POJO.Seller;
import com.finalproject.POJO.TechProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
public class ProductDAO {
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
	public ProductDAO() {
		
	}
	public boolean addProduct(Product product) {
		try {
			beginTransaction();
			getEntityManager().persist(product);
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
	public List<Product> getProducts(String key,String[] filterKeys,String sort,int page) {
		try {
		beginTransaction();
		   CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		   CriteriaQuery<Product> criteria= builder.createQuery(Product.class);
		   Root<Product> productRoot= criteria.from(Product.class);
		   criteria.select(productRoot);
		   criteria.distinct(true);
		   List<Predicate> criteriaPredicates= new ArrayList<Predicate>();
//		   System.out.println(key);
		   if(key!=null) {
			   ParameterExpression<String> pe= builder.parameter(String.class,"key");
			   criteriaPredicates.add(builder.like(productRoot.<String>get("name"),pe));
		   }
		   if(filterKeys!=null) {
			   for(int i=0;i<filterKeys.length;i++) {
				   ParameterExpression<String> pe= builder.parameter(String.class,"type"+i);
				   criteriaPredicates.add(builder.equal(productRoot.<String>get("type"),pe));
			   }
		   }
		   List<Order> sortOrders=new ArrayList<Order>();
		   if(sort!=null) {
			   if(sort.equals("name"))
			   sortOrders.add(builder.asc(productRoot.get(sort)));  
			   else {
				   sortOrders.add(builder.desc(productRoot.get(sort)));
			   }
		   }
//		   sortOrders.add(builder.asc(productRoot.get("name")));
		   criteria.orderBy(sortOrders);
		   Predicate[] predicatesArray= new Predicate[criteriaPredicates.size()];
		   for(int i=0;i<criteriaPredicates.size();i++) {
			   predicatesArray[i]=criteriaPredicates.get(i);
		   }
		   criteria.where(builder.and(predicatesArray));
		   TypedQuery<Product> query=getEntityManager().createQuery(criteria);
		   if(key!=null) {
			   query.setParameter("key", key+"%");
		   }
		   if(filterKeys!=null) {
			   for(int i=0;i<filterKeys.length;i++) {
				   query.setParameter("type"+i, filterKeys[i]);
			   }
		   }
		   int pageSize=2;
		   query.setFirstResult((page-1)*pageSize);
		   query.setMaxResults(pageSize);
		   List<Product> products= query.getResultList();
		  commit();
		  return products;
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
	public boolean deleteProduct(int id) {
		try {
			beginTransaction();
			Product product= getEntityManager().find(Product.class, id);
//			To remove child in bidirectional relationship we have to remove child first
			for(Review review :product.getReviews()) {
				review.setProduct(null);
				getEntityManager().remove(getEntityManager().merge(review));
			} 
			getEntityManager().remove(product);
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
	public Product getProductById(int id) {
		
		try {
			beginTransaction();
			   CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			   CriteriaQuery<Product> criteria= builder.createQuery(Product.class);
			   Root<Product> sellerRoot= criteria.from(Product.class);
			   criteria.select(sellerRoot);
			   criteria.where(builder.equal(sellerRoot.get("product_id"), id));
			   Product product= getEntityManager().createQuery(criteria).getSingleResult();
			  commit();
			  return product;
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
	public boolean updateProductQuantity(Product product,int quantity) {
		try {
			beginTransaction();
			product.setQuantity(product.getQuantity()-quantity);
			getEntityManager().merge(product);
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
	public boolean updateProduct(Product product) {
		try {
			beginTransaction();
			Product prod=getEntityManager().find(Product.class, product.getProduct_id());
			prod.setName(product.getName());
			prod.setDescription(product.getDescription());
			prod.setPrice(product.getPrice());
			prod.setQuantity(product.getQuantity());
			getEntityManager().merge(prod);
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
