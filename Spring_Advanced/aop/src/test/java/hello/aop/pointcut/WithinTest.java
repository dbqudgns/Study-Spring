package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

public class WithinTest {

    // AspectJExpressionPointcut : 포인트컷 표현식을 처리해주는 클래스
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    // Method : 메서드의 정보를 담고 있는 객체
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    // within 지시자는 해당 클래스가 매칭되면 그 안의 메서드(조인 포인트)들이 자동으로 매칭된다.

    @Test
    void withinExact() {
        pointcut.setExpression("within(hello.aop.member.MemberServiceImpl)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void withinStar() {
        pointcut.setExpression("within(hello.aop.member.*Service*)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void withinSubPackage() {
        pointcut.setExpression("within(hello.aop..*)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // within 지시자는 포인트컷 표현식에 부모 타입을 지정하면 안된다 !
    @Test
    @DisplayName("클래스에만 직접 적용, 인터페이스(부모 타입)을 선정하면 안된다.")
    void withinSuperTypeFalse() {
        pointcut.setExpression("within(hello.aop.member.MemberService)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    @DisplayName("execution은 클래스 기반, 인터페이스를 선정할 수 있다.")
    void executionSuperTypeTrue() {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
