package com.turno.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.turno.userblog.model.User;

@Repository
public class UserDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void saveOrUpdate(User user) {
		entityManager.persist(user);
	}

	@Transactional()
	public List<User> findAllActive() { // fetch all users respective to users who are active
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(root.get("active"), true));
		query.select(root).where(predicates.toArray(new Predicate[] {}));
		TypedQuery<User> q = entityManager.createQuery(query);
		return q.getResultList();
	}

	@Transactional()
	public List<User> findAllIncludingSoftDeleted() { //fetch all users irrespectively
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);
		List<Predicate> predicates = new ArrayList<Predicate>();
		query.select(root).where(predicates.toArray(new Predicate[] {}));
		TypedQuery<User> q = entityManager.createQuery(query);
		return q.getResultList();
	}

	@Transactional()
	public User findById(String id) { // fetch user with respective user id
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(root.get("id"), id));
		query.select(root).where(predicates.toArray(new Predicate[] {}));
		TypedQuery<User> q = entityManager.createQuery(query);
		return q.getSingleResult();
	}
	
	@Transactional()
	public void delete(User user) {
		entityManager.remove(user);
	}
}
