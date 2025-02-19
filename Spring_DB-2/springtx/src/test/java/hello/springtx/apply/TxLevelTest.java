package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
public class TxLevelTest {

    @Autowired
    LevelService service;

    @Test
    void orderTest() {
        service.write();
        service.read();
    }

    @TestConfiguration
    static class TxApplyLevelConfig {

        @Bean
        LevelService levelService() {
            return new LevelService();
        }
    }

    @Slf4j
    @Transactional(readOnly=true) //readOnly = true : 쓰기만 가능
    static class LevelService {

            @Transactional(readOnly=false) //readOnly = false : 읽기/쓰기 가능
            public void write() {
                log.info("write");
                printTxInfo();
            }

            public void read() {
                log.info("call read");
                printTxInfo();
            }

            private void printTxInfo() {
                boolean txActive = TransactionSynchronizationManager.isActualTransactionActive(); //현재 쓰레드에 트랜잭션이 적용되어 있는지 확인할 수 있는 기능
                log.info("tx active={}", txActive);

                boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly(); //현재 트랜잭션에 적용된 readOnly 옵션의 값을 반환한다.
                log.info("read only={}", readOnly);
            }
    }
}

