package hello.advanced.trace.strategy.code.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 변하지 않는 부분(로그 트레이스)는 Context에 둔다.
 * Strategy의 구현체를 변경하거나 새로 만들어도 Context 코드에는 영향을 주지 않는다.
 */
@Slf4j
@RequiredArgsConstructor
public class ContextV1 {

    private Strategy strategy;

    public ContextV1(Strategy strategy) {
        this.strategy = strategy;
    }

    public void execute() {
        long startTime = System.currentTimeMillis();

        // 비즈니스 로직 실행
        strategy.call(); // 위임
        // 비즈니스 로직 종료

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

}
