package hibernate;

import model.Category;
import model.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class PersonHibernateControl {
    private EntityManagerFactory emf = null;

    public PersonHibernateControl(EntityManagerFactory entityManagerFactory) {
        this.emf = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Person person) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(em.merge(person));
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPerson(person.getPersonId()) != null) {
                throw new Exception("Person " + person + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Person person) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.flush();
            person = em.merge(person);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = person.getPersonId();
                if (findPerson(id) == null) {
                    throw new Exception("The person with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Person person;
            try {
                person = em.getReference(Person.class, id);
                person.getPersonId();
                for (Category c : person.getResponsibleCategories()) {
                    c.removePerson(person);
                }
                em.merge(person);
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The person with id " + id + " no longer exists.", enfe);
            }
            em.remove(person);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Person> findPersonEntities() {
        return findPersonEntities(true, -1, -1);
    }

    public List<Person> findPersonEntities(int maxResults, int firstResult) {
        return findPersonEntities(false, maxResults, firstResult);
    }

    private List<Person> findPersonEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Person.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);

            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Person findPerson(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Person.class, id);
        } finally {
            em.close();
        }
    }

    public Person findPerson(String login) {
        EntityManager em = getEntityManager();
        for (Person p : findPersonEntities()) {
            if (p.getLoginName().equals(login)) return p;
        }
        em.close();
        return null;
    }

    public Person findPerson(String login, String password) {
        EntityManager em = getEntityManager();
        for (Person p : findPersonEntities()) {
            if (p.getLoginName().equals(login) && p.getPsw().equals(password)) return p;
        }
        em.close();
        return null;
    }

    public int getPersonCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Person> rt = cq.from(Person.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void addCategoryToPerson(int personId, int categoryId) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Person person;
            Category category;
            try {
                person = em.getReference(Person.class, personId);
                category = em.getReference(Category.class, categoryId);

                person.addCategory(category);
                category.addPerson(person);
                em.merge(category);
                em.merge(person);
                //em.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void removeCategoryFromPerson(Person person, Category category) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category cat;
            Person per;
            try {
                cat = em.getReference(Category.class, category.getCategoryId());
                per = em.getReference(Person.class, person.getPersonId());
                cat.getValidatedPeople().remove(per);
                em.merge(cat);
                em.flush();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error when removing Category from person category list", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

}
