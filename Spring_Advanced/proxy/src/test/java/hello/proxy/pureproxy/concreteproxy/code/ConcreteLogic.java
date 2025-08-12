package hello.proxy.pureproxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

// 구체 클래스 상속을 통한 프록시 생성 예제
@Slf4j
public class ConcreteLogic {

    public String operation() {
        log.info("ConcreateLogic 실행");
        return "data";
    }

}
