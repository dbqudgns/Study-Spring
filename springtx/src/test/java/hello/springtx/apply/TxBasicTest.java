package hello.springtx.apply;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class TxBasicTest {

    @Autowired
    BasicService basicService; //basicService 객체 대신에 트랜잭션을 처리해주는 프록시 객체가 스프링 빈에 등록되고 주입된다.

    @Test
    void proxyCheck() {
        log.info("aop class={}", basicService.getClass()); //TxBasicTest$BasicService$$SpringCGLIB$$0 => (프록시 객체)
        assertThat(AopUtils.isAopProxy(basicService)).isTrue(); //AOP 프록시 객체 적용 여부 확인
    }

    @Test
    void txTest() {
        basicService.tx();
        basicService.nonTx();
    }

    @TestConfiguration
    static class TxApplyBasicConfig {
        @Bean
        BasicService basicService() {
            return new BasicService();
        }
    }

    @Slf4j
    static class BasicService {

        @Transactional
        public void tx() {
            log.info("call tx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            //현재 쓰레드에 트랜잭션이 적용되어 있는지 확인할 수 있는 기능
            log.info("tx active={}", txActive);
        }

        public void nonTx() {
            log.info("call nonTx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            //현재 쓰레드에 트랜잭션이 적용되어 있는지 확인할 수 있는 기능
            log.info("tx active={}", txActive);
        }
    }

}
