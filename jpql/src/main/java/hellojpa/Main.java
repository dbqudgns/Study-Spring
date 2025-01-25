package hellojpa;

import hellojpa.jpql.Member;
import hellojpa.jpql.MemberDTO;
import hellojpa.jpql.Team;
import jakarta.persistence.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

        /*
            //JPQL 기초 문법

            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);

            TypedQuery<Member> query1 = em.createQuery("select m from Member as m", Member.class);
            List<Member> resultList = query1.getResultList(); //List 컬렉션 반환

            TypedQuery<Member> query2 = em.createQuery("select m from Member as m where m.id = 1L", Member.class);
            Member result = query2.getSingleResult(); //결과가 정확히 하나, 단일 객체 반환

            TypedQuery<Member> query3 = em.createQuery("select m from Member as m where m.username = :username", Member.class);
            query3.setParameter("username", "member1"); //파라미터 바인딩 : 이름 기준
            Member singleResult = query3.getSingleResult();
         */

         /*
            //프로젝션 : 여러 값 조회

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            //1. Object[] 타입으로 조회
            List<Object[]> resultList = em.createQuery("select m.username, m.age from Member as m")
                    .getResultList();

            Object[] result = resultList.get(0);
            System.out.println("username = " + result[0]);
            System.out.println("age = " + result[1]);

            //2. new 명령어로 조회
            List<MemberDTO> resultNew = em.createQuery("select new hellojpa.jpql.MemberDTO(m.username, m.age) from Member as m", MemberDTO.class)
                    .getResultList(); //패키지 명을 포함한 전체 클래스 명 입력, 순서와 타입이 일치하는 생성자 필요

            MemberDTO memberDTO = resultNew.get(0);
            System.out.println("username = " + memberDTO.getUsername());
            System.out.println("age = " + memberDTO.getAge());

            */

            /*

            //페이징 : 사용자가 요청한 데이터 중 일부를 원하는 정렬 방식으로 보여주는 방식

            for(int i = 0; i<100; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }

            List<Member> resultList = em.createQuery("select m from Member m order by m.age desc", Member.class)
                            .setFirstResult(0) //시작 위치 : 0번
                                    .setMaxResults(10) // 조회할 데이터 수 : 10개
                                            .getResultList();

            System.out.println("resultList.size() = " + resultList.size());
            for(Member member : resultList) {
                System.out.println("member" + member.getId() + " = " + member);
            }

            */

            /*

            // 조인

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("teamA");
            member.setAge(10);
            member.setTeam(team);
            em.persist(member);

            // 내부 조인 : String query = "select m from Member m inner join m.team t";
            // 외부 조인 : String query = "select m from Member m left outer join m.team t";
            String query = "select m from Member m, Team t where m.username=t.name"; //세타 조인
            List<Member> result = em.createQuery(query, Member.class)
                            .getResultList();

            System.out.println("result.size() = " + result.size());

             */

            /*

            // 조건식 - CASE 식

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("관리자");
            member.setAge(10);
            member.setTeam(team);
            em.persist(member);

            //r기본 CASE 식
            String query = "select " +
                                    "case when m.age <= 10 then '학생요금' " +
                                    "     when m.age >= 60 then '경로요금' " +
                                    "     else '일반요금' " +
                                    "end " +
                           "from Member m";

            //COALESCE : 하나씩 조회해서 NULL이면 지정된 값으로 반환
            String query2 = "select coalesce(m.username, '이름 없는 회원') from Member m";

            //NULLIF : 두 값이 같으면 NULL 반환, 다르면 첫번째 값 반환
            String query3 = "select nullif(m.username, '관리자') from Member m";

            List<String> resultList = em.createQuery(query3, String.class).getResultList();

            for (String s : resultList) {
                System.out.println("s = " + s);
            }

            */

            /*

            //JPQL 기본 함수

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("0123");
            member.setAge(10);
            member.setTeam(team);
            em.persist(member);

            String queryConcat = "select concat('a', 'b') from Member m";
            String querySubstring = "select substring(m.username, 2, 1) from Member m"; //2번째부터 한 개를 짜름 : 결과 => 1
            String queryLocate = "select locate('de','abcdefg') from Member m"; //abcdefg에서 de가 어느 위치에 있는지 : 결과 => 4 / 반환할 때 Integer로 반환

            List<String> resultList_String = em.createQuery(querySubstring, String.class).getResultList();
            List<Integer> resultList_Integer = em.createQuery(queryLocate, Integer.class).getResultList();

            for(String s : resultList_String) {
                System.out.println("s = " + s);
            }

            for(Integer i : resultList_Integer) {
                System.out.println("i = " + i);
            }

            //SIZE : 컬렉션의 크기를 반환
            String querySize = "select size(t.members) from Team t";

            List<Integer> resultList = em.createQuery(querySize, Integer.class).getResultList();

            for(Integer i : resultList) {
                System.out.println("i = " + i);
            }

            //사용자 정의함수 호출
            String queryUserFunction = "select function('group_concat', m.username) from Member m";
            List<String> resultList2 = em.createQuery(queryUserFunction, String.class).getResultList();

            for(String userFunction : resultList2) {
                System.out.println("userFunction = " + userFunction);
            }

            */


            //패치 조인 : 연관된 엔티티나 컬렉션을 SQL 한번에 함께 조회하는 기능

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamB);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            String query = "select m From Member m join fetch m.team"; //엔티티 패치 조인 적용 inner join
            String query2 = "select t From Team t join fetch t.members"; //컬렉션 패치 조인 적용

            List<Member> resultMember = em.createQuery(query, Member.class).getResultList();
            List<Team> resultTeam = em.createQuery(query2, Team.class).getResultList();

            //resultMember는 프록시 객체가 아닌 실제 Member 엔티티를 가져온다. 이유 : 패치 조인으로 인해
            for(Member member : resultMember) {
                System.out.println("username = " + member.getUsername() + "," + "teamName = " + member.getTeam().getName());
            }

            for(Team team : resultTeam) {
                System.out.println("team = " + team.getName() + " | members = " + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("-> username = " + member.getUsername() + ", member = " + member);
                }
            }



            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}