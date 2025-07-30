package hello.advanced.app.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class OrderRepositoryV0 {

    public void save(String itemId) {
        // 저장 로직
        if (itemId.equals("ex")) {
            throw new IllegalStateException("예외 발생!");
        }

        sleep(1000); // 1초(1000ms)초 지연
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace(); // 예외가 발생 시 예외의 종류, 발생 휘치, 호출된 스택을 큰솔에 출력해주는 디버깅 도구
        }
    }

}
