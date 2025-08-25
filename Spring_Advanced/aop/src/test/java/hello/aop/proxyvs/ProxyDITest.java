package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import hello.aop.proxyvs.code.ProxyDIAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"}) // JDK 동적 프록시, DI 예외 발생
// @SpringBootTest(properties = {"spring.aop.proxy-target-class=true"}) // CGLIB 프록시, DI 예외 발생 X
@Import(ProxyDIAspect.class)
public class ProxyDITest {

    @Autowired
    MemberService memberService; // JDK 동적 프록시 OK, CGLIB OK

    @Autowired
    MemberServiceImpl memberServiceImpl; // JDK 동적 프록시 X, CGLIB OK

    @Test
    void go() {
        log.info("memberService class={}", memberService.getClass());
        log.info("memberServiceImpl class={}", memberServiceImpl.getClass());

        // JDK 동적 프록시가 memberServiceImpl을 전혀 알지 못하기 때문에 프록시가 MemberServiceImpl에 주입될 수 없다.
        // CGLIB 프록시는 memberServiceImpl을 알고 있기 때문에 프록시가 MemberServiceImpl에 주입될 수 있다.
        memberServiceImpl.hello("hello");
    }
}
