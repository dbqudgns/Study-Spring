package hello.advanced.trace.strategy.code.strategy;

// 전략 패턴은 변하는 부분(핵심 로직)을 Strategy라는 인터페이스를 만들고 해당 인터페이스를 구현하도록 해서 문제를 해결한다.
public interface Strategy {
    void call();
}
