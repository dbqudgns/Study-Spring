package hellojpa;

import jakarta.persistence.*;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

         try {

     /*     // JPA 연산 실행 흐름

            Member member = new Member();
            member.setId(1L);
            member.setName("HelloA");

            Member member = new Member();
            member.setId(2L);
            member.setName("HelloB");

            em.persist(member);

            Member findMember = em.find(Member.class, 1L);
            findMember.setName("HelloJPA");

            System.out.println("findMember.Id = " + findMember.getId());
            System.out.println("findMember.Name = " + findMember.getName());

            em.remove(findMember);
            tx.commit();

          */

      /*   // 영속 컨테이너에서의 1차 캐시 흐름

            //비영속
            Member member = new Member();
            member.setId(100L);
            member.setName("HelloJPA");

            //영속
            System.out.println("=== BEFORE ===");
            em.persist(member);
            // em.detach(member); //준영속
            System.out.println("=== AFTER ===");

            Member findMember = em.find(Member.class, 100L);

            System.out.println("findMemeber.id = " + findMember.getId());
            System.out.println("findMemeber.name = " + findMember.getName());

            tx.commit();
       */

        /*
            //영속 엔티티의 동일성 보장

             Member a = em.find(Member.class, 100L);
             Member b = em.find(Member.class, 100L);

             System.out.println("result = " + (a == b)); //true : 1차 캐시로 저장된 100L의 Member 객체

         */

         /*
             // 트랜잭션을 지원하는 쓰기 지연 SQL 저장소(데이터베이스 X)

             Member member1 = new Member(150L, "A");
             Member member2 = new Member(160L, "B");


             em.persist(member1);
             em.persist(member2);
             //여기까지 INSERT SQL을 데이터베이스에 보내지 않는다.

             System.out.println("=======================");

             //커밋하는 순간 데이터베이스에 INSERT SQL을 보낸다.
             tx.commit();
         */

         /*

             // 영속성 컨텍스트 : 변경 감지 (스냅샷을 이용)

             //DB에 150L Member가 있다고 가정
             Member member = em.find(Member.class, 150L);

             //영속 컨텍스트 안에 있는 엔티티 데이터 수정
             member.setName("ZZZZZ");

             System.out.println("=======================");
             tx.commit(); //데이터 변경 시 JPA에서 자동으로 UPDATE 쿼리를 보낸다.

         */

         /*

            // 쓰기 지연 SQL 저장소의 SQL문(등록, 수정, 삭제)을 DB에 반영하는 flush()

             Member member = new Member(200L, "member200");
             em.persist(member);

             em.flush(); //DB에 Insert문 쿼리문이 반영되어 동기화된다..

             System.out.println("=======================");
             tx.commit(); //커밋 되기 전에 영속성 컨텍스트에는 member가 아직 존재한다.

          */

             // 준영속 상태

             Member member = em.find(Member.class, 150L); //영속 상태
             member.setName("AAAAA");

             em.detach(member); //준영속 상태 : JPA가 관리하지 않음

             System.out.println("=======================");
             tx.commit(); // 커밋되지 않음 !!


         } catch (Exception e) {
            tx.rollback(); //예외 발생 시 롤백 발생
        }
        finally {
            em.close();
        }

        emf.close();
    }
}
