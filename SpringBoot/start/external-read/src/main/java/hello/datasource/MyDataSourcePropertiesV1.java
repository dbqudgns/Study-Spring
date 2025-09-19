package hello.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @ConfigurationProperties : 외부 설정을 자동 주입 받는 객체
 * 여기서는 외부 설정 KEY의 묶음 시작점인 my.datasource를 적어준다.
 *
 * 기본 주입 방식은 자바빈 프로퍼티 방식이다. Setter를 통해 주입된다.
 * 자바빈 프로퍼티 : 객체의 필드에 접근하기 위한 Getter와 Setter 메서드를 통칭하는 용어
 *
 * 단점 : Setter를 가지고 있기 때문에 실수로 값을 변경하는 문제가 발생하여 애플리케이션 전체에 심각한 버그를 유발할 수 있다.
 */
@Data
@ConfigurationProperties("my.datasource")
public class MyDataSourcePropertiesV1 {

    private String url;
    private String username;
    private String password;
    private Etc etc = new Etc();

    @Data
    public static class Etc {
        private int maxConnection;
        private Duration timeout;
        private List<String> options = new ArrayList<>();
    }

}
