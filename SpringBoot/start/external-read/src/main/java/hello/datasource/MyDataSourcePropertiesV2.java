package hello.datasource;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @ConfigurationProperties : 외부 설정을 자동 주입 받는 객체
 * 여기서는 외부 설정 KEY의 묶음 시작점인 my.datasource를 적어준다.
 *
 * 생성자를 만들어 두면 생성자를 통해서 설정 정보를 주입한다.
 */
@Getter
@ConfigurationProperties("my.datasource")
public class MyDataSourcePropertiesV2 {

    private String url;
    private String username;
    private String password;
    private Etc etc;

    // @DefaultValue Etc etc : etc를 찾을 수 없을 경우 Etc 객체를 생성하고 내부에 들어가는 값은 null, 0으로 비워둔다.
    public MyDataSourcePropertiesV2(String url, String username, String password, @DefaultValue Etc etc) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.etc = etc;
    }

    @Getter
    public static class Etc {
        private int maxConnection;
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
