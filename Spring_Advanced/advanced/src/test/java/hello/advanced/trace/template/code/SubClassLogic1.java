package hello.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

// 다형성 활용
@Slf4j
public class SubClassLogic1 extends AbstractTemplate {

    @Override
    protected void call() {
        log.info("비즈니스 로직1 실행");
    }
}
