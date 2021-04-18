package hibernate;

import model.Category;
import model.Company;
import model.FinanceSystem;
import model.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class FinanceSystemHibernateControl {
    private EntityManagerFactory emf = null;

    public FinanceSystemHibernateControl(EntityManagerFactory entityManagerFactory) {
        this.emf = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FinanceSystem financeSystem) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(financeSystem);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFinanceSystem(financeSystem.getName()) != null) {
                throw new Exception("Library " + financeSystem + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FinanceSystem financeSystem) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.flush();
            financeSystem = em.merge(financeSystem);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String name = financeSystem.getName();
                if (findFinanceSystem(name) == null) {
                    throw new Exception("Finance system with id " + financeSystem + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String name) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FinanceSystem financeSystem;
            try {
                financeSystem = em.getReference(FinanceSystem.class, name);
                financeSystem.getName();
                em.merge(financeSystem);
            } catch (EntityNotFoundException enfe) {
                throw new Exception("Finance system with name " + name + " no longer exists.", enfe);
            }
            em.remove(financeSystem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FinanceSystem> findFinanceSystemEntities() {
        return findFinanceSystemEntities(true, -1, -1);
    }

    public List<FinanceSystem> findFinanceSystemEntities(int maxResults, int firstResult) {
        return findFinanceSystemEntities(false, maxResults, firstResult);
    }

    private List<FinanceSystem> findFinanceSystemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FinanceSystem.class));
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

    public FinanceSystem findFinanceSystem(String name) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FinanceSystem.class, name);
        } finally {
            em.close();
        }
    }

    public int getFinanceSystemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FinanceSystem> rt = cq.from(FinanceSystem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void AddCategoryToFinanceSystem(FinanceSystem financeSystem, Category category) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                financeSystem.addCategory(category);
                em.merge(financeSystem);
                em.flush();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error adding category", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void AddPersonToFinanceSystem(FinanceSystem financeSystem, Person person) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                financeSystem.addPerson(person);
                em.merge(financeSystem);
                em.flush();
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

    public void AddCompanyToFinanceSystem(FinanceSystem financeSystem, Company company) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                financeSystem.addCompany(company);
                em.merge(financeSystem);
                em.flush();
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

    public void removeCategoryFromFinanceSystem(FinanceSystem financeSystem, Category category) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                financeSystem.getAllCategories().remove(category);
                em.merge(financeSystem);
                em.flush();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error when removing category", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void removePersonFromFinanceSystem(FinanceSystem financeSystem, Person person) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                financeSystem.getAllPeople().remove(person);
                em.merge(financeSystem);
                em.flush();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error when removing person", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void removeCompanyFromFinanceSystem(FinanceSystem financeSystem, Company company) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                financeSystem.getAllCompanies().remove(company);
                em.merge(financeSystem);
                em.flush();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error when removing commpany", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

}
