package hellojpa;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

          /*
             // 준영속 상태

             Member member = em.find(Member.class, 150L); //영속 상태
             member.setName("AAAAA");

             em.detach(member); //준영속 상태 : JPA가 관리하지 않음

             System.out.println("=======================");
             tx.commit(); // 커밋되지 않음 !!
          */

         /*
             // 기본 키 매핑 학습을 위한 로직
             Member member1 = new Member();
             member1.setUsername("A");

             Member member2 = new Member();
             member2.setUsername("B");

             Member member3 = new Member();
             member3.setUsername("C");

             System.out.println("=======================");
             em.persist(member1);
             em.persist(member2);
             em.persist(member3);
             System.out.println("=======================");

             System.out.println("member1: " + member1.getId());
             System.out.println("member2: " + member2.getId());
             System.out.println("member3: " + member3.getId());
        */

         /*
             //단방향 연관관계 매핑 : 객체 참조와 DB 외래키를 매핑
             Team team = new Team();
             team.setName("TeamA");
             em.persist(team);

             Member member = new Member();
             member.setUsername("member1");
             member.setTeam(team);
             em.persist(member);

             Member findMember = em.find(Member.class, member.getId());

             Team findTeam = findMember.getTeam();
        */

        /*

            //양방향 연관관계 매핑 : 객체 참조와 DB 외래키를 매핑

             Team team = new Team();
             team.setName("TeamA");
             em.persist(team);

             Member member = new Member();
             member.setUsername("member1");
             member.setTeam(team);
             em.persist(member);

             em.flush();
             em.clear();

             Member findMember = em.find(Member.class, member.getId());
             System.out.println(findMember.getUsername());
             List<Member> members = findMember.getTeam().getMembers();

             for (Member m : members) {
                 //DB에 flush()를 해야 밑에 출력문이 정상적으로 실행된다
                 //이유 : DB에 INSERT 쿼리를 날리지 않는다면 Team에는 Member 정보가 없기 때문에 밑에 출력문이 실행되지 않는다.
                 System.out.println("m = " + m.getUsername());
             }
         */

         /*

             //고급 매핑 : 조인 전략, 테이블 전략, 구현 클래스마다 테이블 전략

             Movie movie = new Movie();
             movie.setDirector("aaaa");
             movie.setDirector("bbbb");
             movie.setName("바람과함께사라지다");
             movie.setPrice(10000);

             em.persist(movie);

             em.flush();
             em.clear();

             Movie findMovie = em.find(Movie.class, movie.getId());
             System.out.println("findMovie: " + findMovie);
         */

         /*
             //@MappedSuperclass 적용

             Member member = new Member();
             member.setUsername("user1");
             member.setCreatedBy("kim");
             member.setCreatedDate(LocalDateTime.now());

             em.persist(member);

             em.flush();
             em.clear();

             tx.commit();

         */

         /*
             //프록시
             Member member = new Member();
             member.setUsername("hello");

             em.persist(member);

             em.flush();
             em.clear();

            // Member findMember = em.find(Member.class, member.getId());
             Member findMember = em.getReference(Member.class, member.getId());
             System.out.println("findMember = " + findMember.getClass());
             System.out.println("findMember.id = " + findMember.getId());
             System.out.println("findMEmber.username = " + findMember.getUsername());
         */

         /*
             //즉시 로딩과 지연 로딩

             Team team = new Team();
             team.setName("teamA");
             em.persist(team);

             Member member = new Member();
             member.setUsername("member1");
             member.setTeam(team);
             em.persist(member);

             em.flush();
             em.clear();

             Member findMember = em.find(Member.class, member.getId());

             System.out.println("findMember = " + findMember.getTeam().getClass());

             System.out.println("=============");
             findMember.getTeam().getName(); //지연 로딩으로 생성된 Team 프록시 객체 초기화 (실제 team을 사용하는 시점에 초기화 발생)
             System.out.println("=============");
         */

         /*

             //영속성 전이와 고아 객체 삭제
             Child child1 = new Child();
             Child child2 = new Child();

             Parent parent = new Parent();
             parent.addChild(child1);
             parent.addChild(child2);

             em.persist(parent);

             em.flush();
             em.clear();

             Parent findParent = em.find(Parent.class, parent.getId());
             findParent.getChildList().remove(0); //고아 객체 제거

          */

         /*

             //임베디드 타입 적용

             Member member = new Member();
             member.setUsername("hello");
             member.setHomeaddress(new Address("city", "street", "zipcode"));
             member.setWorkPeriod(new Period());
             em.persist(member);

         */

             //값 타입 컬렉션 사용 저장 예제

             Member member = new Member();
             member.setUsername("member1");
             member.setHomeaddress(new Address("homeCity", "street", "10000" ));

             member.getFavoriteFoods().add("치킨");
             member.getFavoriteFoods().add("족발");
             member.getFavoriteFoods().add("피자");

             //값 타입 엔티티 사용할 때
             member.getAddressHistory().add(new AddressEntity("old1", "street", "10000"));
             member.getAddressHistory().add(new AddressEntity("old2", "street", "10000"));

             em.persist(member);

             //값 타입 컬렉션 조회 예제

             em.flush();
             em.clear();

             System.out.println("======START======");
             Member findMember = em.find(Member.class, member.getId());


            //값 타입 컬렉션 사용할 때
            /*
            List<Address> addressHistory = findMember.getAddressHistory();
             for (Address address : addressHistory) {
                 System.out.println("address = " + address.getCity()); //값 타입 컬렉션은 지연 로딩 전략을 사용한다
             }
            */

             Set<String> favoriteFoods = findMember.getFavoriteFoods();
             for (String favoriteFood : favoriteFoods) {
                 System.out.println("favoriteFood = " + favoriteFood);
             }

             //값 타입 컬렉션 수정 예제

             //치킨->한식
             System.out.println("======FAVORITEFOODS======");
             findMember.getFavoriteFoods().remove("치킨");
             findMember.getFavoriteFoods().add("한식");

             //old1->new1 : 값 타입 컬렉션 사용할 때
             System.out.println("======ADDRESS======");
            /* findMember.getAddressHistory().remove(new Address("old1", "street", "10000"));
             findMember.getAddressHistory().add(new Address("new1", "street", "10000"));*/

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
