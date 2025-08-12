package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// 리플렉션 이해를 위한 예제 적용
// 리플렉션 : 클래스나 메서드의 메타정보를 사용해서 동적으로 호출하는 메서드를 변경하는 기술
@Slf4j
public class ReflectionTest {

    // 리플렉션 적용 전
    @Test
    void reflection0() {
        Hello target = new Hello();

        // 공통 로직 1 시작
        log.info("start");
        String result1 = target.callA(); // 호출하는 메서드가 다르다.
        log.info("result={}", result1);
        // 공통 로직 1 종료

        // 공통 로직2 시작
        log.info("start");
        String result2 = target.callB(); // 호출하는 메서드가 다르다.
        log.info("result={}", result2);
        // 공통 로직2 종료
    }

    // 리플랙선 적용 후
    @Test
    void reflection1() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        // 클래스 정보 : 클래스 메타 정보를 획득한다.
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();

        // callA 메서드 정보
        log.info("start");
        Method methodCallA = classHello.getMethod("callA"); // 해당 클래스의 callA 메서드 메타정보를 획득한다.
        Object result1 = methodCallA.invoke(target); // 획득한 메서드 메타정보로 실제 인스턴스(target)의 메서드를 호출한다.
        log.info("result1={}", result1);

        // callB 메서드 정보
        log.info("start");
        Method methodCallB = classHello.getMethod("callB"); // 해당 클래스의 callB 메서드 메타정보를 획득한다.
        Object result2 = methodCallB.invoke(target); // 획득한 메서드 메타정보로 실제 인스턴스(target)의 메서드를 호출한다.
        log.info("result2={}", result2);
    }

    // reflection1의 중요한 핵심은 클래스나 메서드 정보를 동적으로 변경할 수 있다는 점이다!

    // 리플렉션 적용 후 : 공통 로직1, 공통 로직2를 한번에 처리할 수 있는 통합된 공통 처리로직 dynamicCall(~, ~)
    @Test
    void reflection2() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();

        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);

        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);
    }

    private void dynamicCall(Method method, Object target) throws InvocationTargetException, IllegalAccessException {
        log.info("start");
        Object result = method.invoke(target);
        log.info("result={}", result);
    }

    // 리플렉션은 프레임워크 개발이나 또는 매우 일반적인 공통 처리가 필요할 때 주의해서 사용하자
    // 왜냐하면, 리플렉션 기술은 런타임에 동작하기 때문에 컴파일 시점에 오류를 잡을 수 없다.

    @Slf4j
    static class Hello {
        public String callA() {
            log.info("callA");
            return "A";
        }

        public String callB() {
            log.info("callB");
            return "B";
        }
    }

}
