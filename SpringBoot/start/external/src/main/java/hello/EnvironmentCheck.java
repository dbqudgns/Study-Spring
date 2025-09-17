package hello;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 스프링은 PropertySource라는 추상 클래스를 제공하고, 각각의 외부 설정을 조회하는 XxxPropertySource 구현체를 만든다.
 * 스프링은 로딩 시점에 필요한 PropertySource들을 생성하고, Environment에서 사용할 수 있게 연결해둔다.
 * environment.getProperty(key)를 통해서 값을 조회할 수 있다.
 */
@Slf4j
@Component
public class EnvironmentCheck {

    private final Environment env;

    public EnvironmentCheck(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void init() {
        String url = env.getProperty("url");
        String username = env.getProperty("username");
        String password = env.getProperty("password");
        log.info("env url={}", url);
        log.info("env username={}", username);
        log.info("env password={}", password);
    }
}
