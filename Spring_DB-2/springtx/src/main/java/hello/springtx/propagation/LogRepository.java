package hello.springtx.propagation;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LogRepository {

    private final EntityManager em;

    //@Transactional
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save (Log logMessage) {
        log.info("log 저장");
        em.persist(logMessage);

        if (logMessage.getMessage().contains("로그예외")) {
            log.info("log 저장시 예외 발생");
            throw new RuntimeException("예외 발생"); //런타임 예외로 롤백이 발생하므로 em.persist(logMessage)는 의미가 없어진다.
        }

    }

    public Optional<Log> find(String message) {
        return em.createQuery("select i from Log i where i.message = :message", Log.class)
                .setParameter("message", message)
                .getResultList().stream().findAny(); //findAny() : 값이 두 개 이상이면 먼저 나온 값을 반환한다.
    }

}
