package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

// JDK 동적 프록시 사용을 위한 예제 테스트
@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void dynamicA() {
        AInterface target = new AImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target); // 동적 프록시에 적용할 핸들러 로직

        /**
         * 클래스 로더 정보, 인터페이스, 핸들러 로직을 인수로 넣어준다.
         * 클래스 로더 정보 : 프록시 클래스를 로드할 때 사용할 클래스 로더를 지정한다.
         * (클래스 로더 : .class 파일을 JVM 메모리에 불러와 CPU가 실행할 준비(클래스 로드)를 하는 과정을 수행하는 주체)
         * 인터페이스 : 프록시 객체가 구현해야 할 인터페이스 목록을 지정한다.
         * 핸들러 : 프록시 객체의 메서드가 호출될 때 실제 동작을 정의하는 객체
         */

        // 그러면 해당 인터페이스를 기반으로 동적 프록시를 생성하고 그 결과를 반환한다.
        AInterface proxy = (AInterface) Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);

        proxy.call(); // 위 프록시는 TimeInvocationHandler 로직을 실행한다.

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass()); // 동적으로 생성된 프록시 클래스 정보이다.
    }

    /** 실행순서
     * 1. 클라이언트는 JDK 동적 프록시의 call()을 호출한다.
     * 2. JDK 동적 프록시는 InvocationHandler.invoke()를 호출한다.
     * TimeInvocationHandler가 구현체로 있으므로 TimeInvocationHandler.invoke()가 호출된다.
     * 3. TimeInvocationHandler가 내부 로직을 수행하고 method.invoke(target, args)를 호출해서 target인 실제 객체(AImpl)를 호출한다.
     * 4. AImpl 인스턴스의 call()이 실행된다.
     * 5. AImpl 인스턴스의 call()의 실행이 끝나면 TimeInvocationHandler로 응답이 돌아온다. 시간 로그를 출력하고 결과를 반환한다.
     */

    @Test
    void dynamicB() {
        BInterface target = new BImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        BInterface proxy = (BInterface) Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, handler);

        proxy.call();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }

}
