package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.MyDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(AtAnnotationTest.AtAnnotationAspect.class)
@SpringBootTest
public class AtAnnotationTest {

    @Autowired
    MemberService memberService;

    @Test
    void successAnnotation() {
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA");
        /**
         * Spring AOP는 기본적으로 JDK 동적 프록시 방식을 사용하기 때문에
         * -> 인터페이스에 선언된 메서드에만 AOP를 적용할 수 있다.
         * 따라서 MemberService 클래스에 internal() 메서드를 선언하였다.
         */
        memberService.internal(new MyDTO("유병훈"));
    }

    @Slf4j
    @Aspect
    static class AtAnnotationAspect {

        @Around("@annotation(hello.aop.member.annotation.MethodAop)")
        public Object doAtAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@annotation] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        @Around("execution(* hello.aop..*(..)) && @args(hello.aop.member.annotation.ClassAop))")
        // @args : 스프링은 모든 스프링 빈에 AOP를 적용하려고 시도한다. -> @args는 실제 객체 인스턴스가 생성되고 실행될 때 어드바이스 적용 여부를 확인할 수 있다.
        // 따라서 범위를 지정해주는 execution 포인트컷 지시자 같은 프록시 적용 대상을 축소하는 표현식과 함께 사용해야 한다.
        public Object doAtArgs(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@args] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

}
