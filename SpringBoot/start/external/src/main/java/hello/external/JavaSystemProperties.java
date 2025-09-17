package hello.external;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class JavaSystemProperties {

    public static void main(String[] args) {
        Properties properties = System.getProperties(); // 모든 자바 시스템 속성 값을 조회
        for (Object key : properties.keySet()) {
            log.info("prop {}={}", key, System.getProperty(String.valueOf(key))); // 특정 속성 값을 조회
        }

        // VM Options에 "-Durl=devdb -Dusername=dev_user -Dpassword=dev_pw" 3가지 자바 시스템 속성과 값을 추가하였다.
        String url = System.getProperty("url");
        String username = System.getProperty("username");
        String password = System.getProperty("password");

        log.info("url={}", url);
        log.info("username={}", username);
        log.info("password={}", password);
    }
}
