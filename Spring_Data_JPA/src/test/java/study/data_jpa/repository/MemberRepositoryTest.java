package study.data_jpa.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.entity.Member;
import study.data_jpa.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    EntityManager em;

    @Test
    void testMember(){

        //given
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(savedMember.getId()).get(); // 객체가 있을 수도 있고 없을수도 있으니 원래는 Optional로 반환함

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

        assertThat(findMember).isEqualTo(member); // JPA 엔티티 동일성 보장

    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all =memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    // Spring Data JPA 페이징 처리 : Page, Slice 반환 테스트
    @Test
    public void page() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        //when : 0번 인덱스부터 3개의 데이터를 조회하고 username을 기준으로 내림차순 정렬
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findPageByAge(10, pageRequest);
        Slice<Member> slice = memberRepository.findSliceByAge(10, pageRequest);
        Page<Member> pageJoin = memberRepository.findPageByAgeJoinQuery(10, pageRequest); // 실행 시 left join이 사용되지 않는다. => MemberRepository 주석문 참고

        //then
        List<Member> contentByPage = page.getContent(); // 조회된 데이터 반환
        assertThat(contentByPage.size()).isEqualTo(page.getSize()); // 조회된 데이터 수
        assertThat(contentByPage.size()).isEqualTo(page.getNumberOfElements()); // 현재 페이지에 나올 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5); // 전체 데이터 수 => countQuery를 통해 알 수 있다.
        assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 번호 => totalCount / limit
        assertThat(page.isFirst()).isTrue(); // 첫번째 항목인가 ?
        assertThat(page.hasNext()).isTrue(); // 다음 페이지가 있는가 ?

        List<Member> contentBySlice = slice.getContent(); // 조회된 데이터 반환
        assertThat(contentBySlice.size()).isEqualTo(slice.getSize()); // 조회된 데이터 수
        assertThat(contentBySlice.size()).isEqualTo(slice.getNumberOfElements()); // 현재 페이지에 나올 데이터 수
        assertThat(slice.getNumber()).isEqualTo(0); // 페이지 번호
        assertThat(slice.isFirst()).isTrue(); // 첫번째 항목인가 ?
        assertThat(slice.hasNext()).isTrue(); // 다음 페이지가 있는가 ?
    }

    // 스프링 데이터 JPA를 사용한 벌크성 수정 쿼리 테스트
    @Test
    public void bulkUpdate() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when : update가 된 Member 개수 반환
        int resultCount = memberRepository.bulkAgePlus(20);
        List<Member> findMembers = memberRepository.findByUsername("member5");
        Member member5 = findMembers.get(0);
        System.out.println("member5 Age = " + member5.getAge());

        //then
        assertThat(resultCount).isEqualTo(3);

    }

    @Test
    public void findMemberLazy() throws Exception {
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));

        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findMemberFetchJoin();

        //then
        for (Member member : members) {
            String teamName = member.getTeam().getName();
            System.out.println("teamName = " + teamName);
        }
    }

    // 쿼리 힌트 사용 확인 테스트
    @Test
    public void queryHint() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        //when
        Member member = memberRepository.findReadOnlyByUsername("member1");
        member.setUsername("member2");

        em.flush(); // 업데이트 Query가 실행되지 않는다.
    }


}