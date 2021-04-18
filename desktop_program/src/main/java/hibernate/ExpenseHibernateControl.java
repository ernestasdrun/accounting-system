package hibernate;

import model.Category;
import model.Expense;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

public class ExpenseHibernateControl {
    private EntityManagerFactory emf = null;

    public ExpenseHibernateControl(EntityManagerFactory entityManagerFactory) {
        this.emf = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Expense expense) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(em.merge(expense));
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Expense expense) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(expense);
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

            Expense expense = null;

            try {
                expense = em.find(Expense.class, id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            em.remove(expense);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Expense> getExpenseList() {
        return getExpenseList(true, -1, -1);
    }

    public List<Expense> getExpenseList(boolean all, int maxRes, int firstRes) {

        EntityManager em = getEntityManager();
        try {

            CriteriaQuery criteriaQuery = em.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(Expense.class));
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

    public Expense findExpense(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Expense.class, id);
        } finally {
            em.close();
        }
    }

    public Expense findExpense(String desc) {
        EntityManager em = getEntityManager();
        for (Expense e : getExpenseList()) {
            if (e.getDescription().equals(desc)) return e;
        }
        em.close();
        return null;
    }

    public List<Expense> getExpenseListInCategory(Category category) {
        EntityManager em = getEntityManager();
        List<Expense> expList = new ArrayList<>();
        for (Expense e : getExpenseList()) {
            if (e.getCategory().getName().equals(category.getName())) {
                expList.add(e);
            }
        }
        em.close();
        if (expList.size() == 0) return null;
        else return expList;
    }

}
