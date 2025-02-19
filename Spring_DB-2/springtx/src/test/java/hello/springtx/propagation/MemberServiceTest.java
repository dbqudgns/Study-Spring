package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LogRepository logRepository;

    /**
     * memberService @Transactional : OFF
     * memberRepository @Transactional : ON
     * logRepository @Transactional : ON
     */
    @Test //두 개의 트랜잭션 : 커밋
    void outerTxOff_success() {
        //given
        String username = "outerTxOff_success";

        //when
        memberService.joinV1(username);

        //when: 모든 데이터가 정상 저장된다.
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());

    }

    /**
     * memberService @Transactional : OFF
     * memberRepository @Transactional : ON
     * logRepository @Transactional : ON Exception
     */
    @Test // 두 개의 트랜잭션 : 롤백
    void outerTxOff_fail() {
        //given
        String username = "로그예외_outerTxOff_fail"; //LogRepository에서 런타임 예외 발생 ( "로그예외" 포함 )


        //when
        assertThatThrownBy(() -> memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        //when: 완전히 롤백되지 않고, member 데이터가 남아서 저장된다.
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());

    }

    /**
     * memberService @Transactional : ON
     * memberRepository @Transactional : OFF
     * logRepository @Transactional : OFF
     */
    @Test //단일 트랜잭션
    void single() {

        //given
        String username = "single";

        //when
        memberService.joinV1(username);

        //when: 모든 데이터가 정상 저장된다.
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());

    }


    /**
     * memberService @Transactional : ON
     * memberRepository @Transactional : ON
     * logRepository @Transactional : ON
     */
    @Test //물리트랜잭션 내에 모든 트랜잭션 커밋
    void outerTxOn_success() {
        //given
        String username = "outerTxOn_success";

        //when
        memberService.joinV1(username);

        //when: 모든 데이터가 정상 저장된다.
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());

    }

    /**
     * memberService @Transactional : ON
     * memberRepository @Transactional : ON
     * logRepository @Transactional : ON Exception
     */
    @Test //logRepository 내부 트랜잭션에서 롤백이 일어날 경우
    void outerTxOn_fail() {
        //given
        String username = "로그예외_outerTxOn_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        //when: 외부 트랜잭션에 런타임 예외가 던져져 롤백된다. => 어차피 롤백 됐으므로 rollbackOnly 설정은 참고하지 않는다.
        assertTrue(memberRepository.find(username).isEmpty());
        assertTrue(logRepository.find(username).isEmpty());

    }

    /**
     * memberService @Transactional : ON
     * memberRepository @Transactional : ON
     * logRepository @Transactional : ON Exception
     */
    @Test //서비스에서 예외를 처리하여 커밋을 요청할 경우 => 커넥션의 readOnly 설정으로 인해 롤백된다.
    void recoverException_fail() {
        //given
        String username = "로그예외_recoverException_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV2(username))
                .isInstanceOf(UnexpectedRollbackException.class); //커넥션의 readOnly 설정으로 인해 해당 롤백 예외가 발생한다.

        //when: 모든 데이터가 롤백된다.
        assertTrue(memberRepository.find(username).isEmpty());
        assertTrue(logRepository.find(username).isEmpty());

    }

    /**
     * memberService @Transactional : ON
     * memberRepository @Transactional : ON
     * logRepository @Transactional(REQUIRES_NEW) Exception
     */
    @Test //logRepository에 REQUIRES_NEW를 사용해 다른 물리 트랜잭션 및 커넥션을 사용
    void recoverException_success() {
        //given
        String username = "로그예외_recoverException_success";

        //when
        memberService.joinV2(username);

        //when: member 저장 , log 롤백
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());

    }


}