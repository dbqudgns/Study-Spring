package hellojpa;

import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        //code
        try {

         /*   Member member = new Member();
            member.setId(1L);
            member.setName("HelloA");*/

            Member member = new Member();
            member.setId(2L);
            member.setName("HelloB");

            em.persist(member);

            //기존에 1L, HelloA가 DB에 저장되어 있다 가정
            Member findMember = em.find(Member.class, 1L);
            findMember.setName("HelloJPA");

            System.out.println("findMember.Id = " + findMember.getId());
            System.out.println("findMember.Name = " + findMember.getName());

//          em.remove(findMember);


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
