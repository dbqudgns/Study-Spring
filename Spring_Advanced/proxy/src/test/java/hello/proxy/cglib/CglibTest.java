package hello.proxy.cglib;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

// CGLIB : 바이트코드를 조작해서 동적으로 클래스를 생성하는 기술을 제공
// 인터페이스가 없어도 구체 클래스만 가지고 동적 프록시를 만들어낼 수 있다.
@Slf4j
public class CglibTest {

    @Test
    void cglib() {
        ConcreteService target = new ConcreteService();

        Enhancer enhancer = new Enhancer(); // CGLIB는 Enhancer를 사용해서 프록시를 생성한다.

        // CGLIB는 구체 클래스를 상속 받아서 프록시를 생성할 수 있다.
        // 어떤 구체 클래스를 상속 받을지 지정한다.
        enhancer.setSuperclass(ConcreteService.class);

        // 프록시에 적용할 실행 로직을 할당한다.
        enhancer.setCallback(new TimeMethodInterceptor(target));

        // 프록시를 생성한다.
        // enhancer.setSuperclass(ConcreteService.class)에서 지정한 클래스를 상속 받아서 프록시가 만들어진다.
        ConcreteService proxy = (ConcreteService) enhancer.create();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call();

    }

}
