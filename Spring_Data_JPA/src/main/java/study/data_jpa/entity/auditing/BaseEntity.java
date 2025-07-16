package study.data_jpa.entity.auditing;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

@MappedSuperclass
@Getter
// 스프링 데이터 JPA Auditing 사용
public class BaseEntity extends BaseTimeEntity {

    @CreatedBy // 생성자
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy // 수정자
    private String lastModifiedBy;

}
