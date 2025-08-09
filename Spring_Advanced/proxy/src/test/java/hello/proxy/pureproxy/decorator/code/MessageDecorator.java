package hello.proxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

// 부가기능 : 응답 값을 꾸며주는 데코레이터
@Slf4j
public class MessageDecorator implements Component {

    private Component component;

    public MessageDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("MessageDecorator 실행");

        String result = component.operation();
        String decoResult = "*****" + result + "*****";

        log.info("MessageDecorator 꾸미기 적용 전 = {}, 적용 후 = {}", result, decoResult);

        return decoResult;
    }
}
