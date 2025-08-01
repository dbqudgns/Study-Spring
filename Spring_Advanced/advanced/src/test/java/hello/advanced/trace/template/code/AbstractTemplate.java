package hello.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

// 템플릿 메서드 패턴
@Slf4j
public abstract class AbstractTemplate {

    // 템플릿 : 변하지 않는 부분(시간 측정)과 일부 변하는 부분을 별도로 호출해서 해결한다.
    public void execute() {
        long startTime = System.currentTimeMillis();

        // 비즈니스 로직 실행
        call(); // 상속
        // 비즈니스 로직 종료

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;

        log.info("resultTime={}", resultTime);
    }

    // 변하는 부분(핵심 로직)은 자식 클래스에 두고 상속과 오버라이딩을 사용해서 처리한다.
    protected abstract void call();
}
