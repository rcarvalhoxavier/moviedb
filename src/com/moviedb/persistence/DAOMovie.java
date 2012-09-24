/**
 *
 */
package com.moviedb.persistence;

import com.moviedb.model.Movie;
import java.io.Serializable;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DAOMovie implements Serializable {

    private EntityManager entityManager;

    public DAOMovie() {
        entityManager = ConnectionFactory.getEntityManager();
    }

    public void salvar(Movie obj) throws Exception {
        entityManager = ConnectionFactory.getEntityManager();
        entityManager.clear();
        EntityTransaction tran = entityManager.getTransaction();
        try {
            tran.begin();
            entityManager.merge(obj);
            tran.commit();
        } finally {
            if (tran.isActive()) {
                tran.rollback();
            }
        }
    }

    public List<Movie> listar() {

        entityManager = ConnectionFactory.getEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Movie> query = cb.createQuery(Movie.class);
        Root<Movie> root = query.from(Movie.class);

        return entityManager.createQuery(query).getResultList();
    }
    

    public List<Movie> buscar(String title, int year, String genero, boolean assistido,boolean maior,double nota) {
        entityManager = ConnectionFactory.getEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Movie> query = cb.createQuery(Movie.class);
        Root<Movie> root = query.from(Movie.class);
        
        List<Predicate> listPredicate = new ArrayList<Predicate>();

        if (title != null && !title.isEmpty()) {
            listPredicate.add(cb.like(root.<String>get("title"), title));
        }
        if (year > 0) {
            listPredicate.add(cb.equal(root.<String>get("year"), year));
        }
        if (genero != null && !genero.isEmpty()) {
            listPredicate.add(cb.like(cb.upper(root.<String>get("genres")), "%" + genero.toUpperCase() + "%"));
        }
        if (assistido) {
            listPredicate.add(cb.equal(root.<String>get("watched"), assistido));
        }
        if(nota > 0){
            if(maior){
                listPredicate.add(cb.greaterThanOrEqualTo(root.<Double>get("rating"), nota));
            }else
            {
                listPredicate.add(cb.lessThanOrEqualTo(root.<Double>get("rating"), nota));
            }                
        }
            

        if (listPredicate.size() > 0) {
            Predicate[] predicates = new Predicate[listPredicate.size()];
            query.where(listPredicate.toArray(predicates));
        }

        return entityManager.createQuery(query).getResultList();

    }
   

    public Movie buscar(Movie obj) {
        return entityManager.find(Movie.class, obj.getImdbid());

    }

    public void excluir(Movie obj) {
        entityManager.getTransaction().begin();
        entityManager.remove(obj);
        entityManager.getTransaction().commit();
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
