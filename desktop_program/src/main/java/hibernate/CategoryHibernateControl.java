package hibernate;

import model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

public class CategoryHibernateControl {
    private EntityManagerFactory emf = null;

    public CategoryHibernateControl(EntityManagerFactory entityManagerFactory) {
        this.emf = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Category category) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(em.merge(category));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Category category) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            //em.flush();
            em.merge(category);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void remove(int id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category category = null;

            try {
                category = em.getReference(Category.class, id);
                category.getCategoryId();
            } catch (Exception e) {
                e.printStackTrace();
            }
            em.remove(category);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void AddIncomeToCategory(Category category, Income income) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                //category.addIncome(income);
                em.merge(category);
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

    public void AddExpenseToCategory(Category category, Expense expense) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                //category.addExpense(expense);
                em.merge(category);
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

    public void removeIncomeFromCategory(Category category, Income income) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                category.getIncome().remove(income);
                em.merge(category);
                em.flush();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error when removing income", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void removeExpenseFromCategory(Category category, Expense expense) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                category.getExpense().remove(expense);
                em.merge(category);
                em.flush();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error when removing expense", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void removeSubcatFromCategory(Category category, Category subcategory) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                category.getSubcats().remove(subcategory);
                em.merge(category);
                em.flush();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("Error when removing subcategory", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Category findCategory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }


    public Category findCategory(String name) {
        EntityManager em = getEntityManager();
        for (Category c : findCategoryEntities()) {
            if (c.getName().equals(name)) return c;
        }
        em.close();
        return null;
    }

    public List<Category> findCategoryEntities() {
        return findCategoryEntities(true, -1, -1);
    }

    public List<Category> findCategoryEntities(int maxResults, int firstResult) {
        return findCategoryEntities(false, maxResults, firstResult);
    }

    private List<Category> findCategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Category.class));
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

    public void clearCategoryUsers(Category category) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category cat;
            try {

                cat = em.getReference(Category.class, category.getCategoryId());
                cat.getValidatedCompanies().clear();
                cat.getValidatedPeople().clear();
                em.merge(cat);
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

    public List<Company> getValidCompanies(Category category) {
        EntityManager em = null;
        em = getEntityManager();
        em.getTransaction().begin();
        Category cat;
        try {
            cat = em.getReference(Category.class, category.getCategoryId());
            List<Company> companies = new ArrayList<>(cat.getValidatedCompanies());
            if (companies.size() > 0)
                return companies;
        } finally {
            em.close();
        }
        return null;
    }

    public List<Person> getValidPeople(Category category) {
        EntityManager em = null;
        em = getEntityManager();
        em.getTransaction().begin();
        Category cat;
        try {
            cat = em.getReference(Category.class, category.getCategoryId());
            List<Person> people = new ArrayList<>(cat.getValidatedPeople());
            if (people.size() > 0)
                return people;
        } finally {
            em.close();
        }
        return null;
    }
}
