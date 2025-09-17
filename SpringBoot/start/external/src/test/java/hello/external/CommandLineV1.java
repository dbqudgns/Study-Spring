package hello.external;

import lombok.extern.slf4j.Slf4j;

/**
 * 커맨드 라인 인수 : 애플리케이션 실행 시점에 외부 설정값을 main(args) 메서드의 args 파라미터로 전달하는 방법
 * 커맨드 라인 인수는 스페이스로 구분한다.
 * java -jar app.jar dataA dataB -> [dataA, dataB] 2개
 * java -jar app.jar url=devdb -> [url=devdb] 1개
 * url=devdb 이라는 단어를 개발자가 직접 파싱해야 하는 번거로움이 있다.
 */
@Slf4j
public class CommandLineV1 {

    public static void main(String[] args) {
        for (String arg : args) {
            log.info("arg {}", arg);
        }
    }

}
