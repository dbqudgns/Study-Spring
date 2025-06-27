package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository_Old;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 스프링 부트 띄우고 테스트 진행 (이게 없으면 @Autowired 다 실패)
@Transactional // 테스트에서 트랜잭션은 테스트가 끝날 시 강제로 롤백시킨다.
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository_Old memberRepositoryOld;

    @Test
    //@Rollback(false) => DB에 저장된 데이터를 확인하고 싶으면 Rollback를 false로 설정하면 됨
    void 회원가입() throws Exception {

        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        assertEquals(member, memberRepositoryOld.findOne(saveId));
    }

    @Test
    void 중복_회원_예외() throws Exception {

        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);

        //then
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    }

}