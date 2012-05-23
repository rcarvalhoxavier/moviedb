/**
 * 
 */
package com.moviedb.persistence;

import com.moviedb.model.Movie;
import java.io.Serializable;
import java.lang.String;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.EntityTransaction;


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
        
        Query query = entityManager.createQuery("from Movie ");

        @SuppressWarnings("unchecked")
        List<Movie> resultList = query.getResultList();

        return resultList;
    }

    public Movie buscar(String title) {
        //        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Apontamento> criteriaQuery = criteriaBuilder.createQuery(Apontamento.class);
//        Root<Apontamento> apontamento = criteriaQuery.from(Apontamento.class);
//        
//        Predicate predicate = criteriaBuilder.and();
//        
//        Order order;
//    
        
        entityManager = ConnectionFactory.getEntityManager();              
        
        Query query = entityManager.createQuery("from Movie f where f.title = ?1");
        query.setParameter(1, title);

        List<Movie> lista = query.getResultList();

        return (lista.isEmpty() ? null : lista.get(0));

    }

    public Movie buscar(String title, int year) {
        entityManager = ConnectionFactory.getEntityManager();
        
//         CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
//        Root<Movie> movie = criteriaQuery.from(Movie.class);
//        
//        Predicate predicate = criteriaBuilder.and();
        
        Query query = entityManager.createQuery("from Movie f where f.title = ?1 and f.year = ?2");
        query.setParameter(1, title);
        query.setParameter(2, year);

        List<Movie> lista = query.getResultList();        

        return (lista.isEmpty() ? null : lista.get(0));
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
