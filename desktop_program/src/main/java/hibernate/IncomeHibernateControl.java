package hibernate;

import model.Category;
import model.Income;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

public class IncomeHibernateControl {
    private EntityManagerFactory emf = null;

    public IncomeHibernateControl(EntityManagerFactory entityManagerFactory) {
        this.emf = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Income income) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(em.merge(income));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Income income) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(income);
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
            Income income = null;

            try {
                income = em.getReference(Income.class, id);
                income.getId();
            } catch (Exception e) {
                e.printStackTrace();
            }
            em.remove(income);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Income> getIncomeList() {
        return getIncomeList(true, -1, -1);
    }

    public List<Income> getIncomeList(boolean all, int maxRes, int firstRes) {

        EntityManager em = getEntityManager();
        try {

            CriteriaQuery criteriaQuery = em.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(Income.class));
            Query query = em.createQuery(criteriaQuery);

            if (!all) {
                query.setMaxResults(maxRes);
                query.setFirstResult(firstRes);
            }
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public Income findIncome(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Income.class, id);
        } finally {
            em.close();
        }
    }

    public Income findIncome(String desc) {
        EntityManager em = getEntityManager();
        for (Income i : getIncomeList()) {
            if (i.getDescription().equals(desc)) return i;
        }
        em.close();
        return null;
    }

    public List<Income> getIncomeListInCategory(Category category) {
        EntityManager em = getEntityManager();
        List<Income> incList = new ArrayList<>();
        for (Income i : getIncomeList()) {
            if (i.getCategory().getName().equals(category.getName())) {
                incList.add(i);
            }
        }
        em.close();
        if (incList.size() == 0) return null;
        else return incList;
    }

}
