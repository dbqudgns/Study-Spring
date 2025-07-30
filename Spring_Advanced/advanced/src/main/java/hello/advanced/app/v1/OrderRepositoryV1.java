package hello.advanced.app.v1;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class OrderRepositoryV1 {

    private final HelloTraceV1 traceV1;

    public void save(String itemId) {
        TraceStatus status = null;

        try {
            status = traceV1.begin("OrderRepository.save()");

            // 저장 로직
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생!");
            }

            sleep(1000); // 1초(1000ms)초 지연

            traceV1.end(status);
        } catch (Exception e) {
            traceV1.exception(status, e);
            throw e; // 예외를 꼭 던져줘야 로직 흐름에 영향을 주지 않는다.
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace(); // 예외가 발생 시 예외의 종류, 발생 휘치, 호출된 스택을 큰솔에 출력해주는 디버깅 도구
        }
    }

}
