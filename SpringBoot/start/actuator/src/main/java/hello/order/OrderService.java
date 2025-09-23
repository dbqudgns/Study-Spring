package hello.order;

import java.util.concurrent.atomic.AtomicInteger;

public interface OrderService {
    void order();
    void cancel();
    // AtomicInteger : 자바 멀티 스레드 환경에서 동시성 문제를 해결하기 위해 사용되는 클래스
    AtomicInteger getStock();
}
