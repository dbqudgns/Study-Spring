package hello.springtx.apply;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
public class InitTxTest {

    @Autowired
    Hello hello;

    @Test
    void go() {
        //초기화 코드는 스프링이 초기화 시점에 호출한다.
    }

    @TestConfiguration
    static class InitTxTestConfig {
        @Bean
        Hello hello() {
            return new Hello();
        }
    }

    @Slf4j
    static class Hello {

        @PostConstruct //초기화 코드 : 빈 생성과 의존성 주입 후 실행된다 !!
        @Transactional
        public void initV1() { //초기화 코드가 먼저 호출되고, 다음에 트랜잭션 AOP가 적용되므로 초기화 시점에는 해당 메서드에서 트랜잭션을 획득할 수 없다 !
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init @PostConstruct tx active = {}", isActive);
        }

        @EventListener(ApplicationReadyEvent.class) //트랜잭션 AOP를 포함한 스프링 컨테이너가 완전히 생성되고 난 다음에 이벤트가 붙은 메소드를 호출한다.
        //Started InitTest in 1.932(랜덤 값) seconds ... : 스프링 컨테이너까지 완전히 생성됐음을 알려주는 로그
        @Transactional
        public void initV2() {
            log.info("Hello init ApplicationReadyEvent");
        }

    }

}
