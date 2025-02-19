package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpabook.jpashop.domain.Book;
import jpabook.jpashop.domain.Category;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

        /*

           //Category 테이블 parent_id 컬럼 동작 과정 확인

            Category category = new Category();
            category.setName("음식");

            em.persist(category);

            Category category2 = new Category();
            category2.setName("김치찌개");
            category2.setParent(category);

            em.persist(category2);

            em.flush();
            em.clear();

            Category categoryFind = em.find(Category.class, category2.getId());
            System.out.println(categoryFind.getName());

        */

            //

            Book book = new Book();
            book.setName("JPA");
            book.setAuthor("김영한");

            em.persist(book);



            tx.commit();

        } catch (Exception e) {
            tx.rollback(); //예외 발생 시 롤백 발생
        }
        finally {
            em.close();
        }

        emf.close();
    }
}



