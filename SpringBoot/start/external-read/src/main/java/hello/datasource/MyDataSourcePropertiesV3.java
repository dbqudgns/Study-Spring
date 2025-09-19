package hello.datasource;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.List;

/**
 * @ConfigurationProperties : 외부 설정을 자동 주입 받는 객체
 * 여기서는 외부 설정 KEY의 묶음 시작점인 my.datasource를 적어준다.
 *
 * 자바 빈 검증기를 사용하여 숫자의 범위 및 문자의 길이를 검증할 수 있다.
 */
@Getter
@ConfigurationProperties("my.datasource")
@Validated
public class MyDataSourcePropertiesV3 {

    @NotEmpty
    private String url;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private Etc etc;

    // @DefaultValue Etc etc : etc를 찾을 수 없을 경우 Etc 객체를 생성하고 내부에 들어가는 값은 null, 0으로 비워둔다.
    public MyDataSourcePropertiesV3(String url, String username, String password, @DefaultValue Etc etc) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.etc = etc;
    }

    @Getter
    public static class Etc {
        @Min(1)
        @Max(999)
        private int maxConnection;

        @DurationMin(seconds = 1)
        @DurationMax(seconds = 60)
        private Duration timeout;

        private List<String> options;

        // @DefaultValue("DEFAULT") : options를 찾을 수 없을 경우 DEFAULT 라는 값을 사용한다.
        public Etc(int maxConnection, Duration timeout, @DefaultValue("DEFAULT") List<String> options) {
            this.maxConnection = maxConnection;
            this.timeout = timeout;
            this.options = options;
        }
    }

}
