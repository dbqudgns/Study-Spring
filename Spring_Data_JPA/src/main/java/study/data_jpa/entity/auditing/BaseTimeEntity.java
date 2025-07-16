package study.data_jpa.entity.auditing;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
// 스프링 데이터 JPA Auditing 사용
public class BaseTimeEntity {

    @CreatedDate // 생성일
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate // 수정일
    private LocalDateTime lastModifiedDate;
}
