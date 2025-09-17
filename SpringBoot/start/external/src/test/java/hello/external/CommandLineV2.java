package hello.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

import java.util.List;
import java.util.Set;

/**
 * 커맨드 라인 옵션 인수 : 스프링이 커맨드 라인 인수를 key=value 형식으로 편리하게 사용할 수 있도록 표준 방식을 정의
 * -(dash) 2개를 연결해서 시작하면 key=value 형식으로 정하고 이것을 커맨드 라인 옵션 인수라고 한다. => --key=value
 * 커맨드 라인 옵션 인수는 자바 언어의 표준 기능이 아닌 스프링이 편리함을 위해 제공하는 기능이다 !!
 */
@Slf4j
public class CommandLineV2 {

    public static void main(String[] args) {
        for (String arg : args) {
            log.info("arg {}", arg);
        }

        // ApplicationArguments : 커맨드 라인 옵션 인수를 규격대로 파싱해서 편리하게 사용할 수 있다.
        ApplicationArguments appArgs = new DefaultApplicationArguments(args);
        log.info("SourceArgs = {}", List.of(appArgs.getSourceArgs()));
        log.info("NonOptionArgs = {}", appArgs.getNonOptionArgs());
        log.info("OptionNames = {}", appArgs.getOptionNames());

        Set<String> optionNames = appArgs.getOptionNames();
        for (String optionName : optionNames) {
            log.info("option args {}={}", optionName, appArgs.getOptionValues(optionName));
        }

        // getOptionValues("key")의 반환 값이 List인 이유 : 하나의 key에 여러 값을 저장할 수 있다.
        List<String> url = appArgs.getOptionValues("url");
        List<String> username = appArgs.getOptionValues("username");
        List<String> password = appArgs.getOptionValues("password");
        List<String> mode = appArgs.getOptionValues("mode");

        log.info("url={}", url);
        log.info("username={}", username);
        log.info("password={}", password);
        log.info("mode={}", mode);
    }

}
