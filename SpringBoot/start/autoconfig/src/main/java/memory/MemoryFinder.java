package memory;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

// JVM에서 메모리 정보를 실시간으로 조회하는 기능
@Slf4j
public class MemoryFinder {

    public Memory get() {
        long max = Runtime.getRuntime().maxMemory(); // JVM이 사용할 수 있는 최대 메모리
        long total = Runtime.getRuntime().totalMemory(); // JVM이 확보한 전체 메모리
        long free = Runtime.getRuntime().freeMemory(); // total 중에 사용하지 않은 메모리
        long used = total - free; // 사용중인 메모리

        return new Memory(used, max);
    }

    @PostConstruct
    public void init() {
        log.info("init memoryFinder");
    }

}
