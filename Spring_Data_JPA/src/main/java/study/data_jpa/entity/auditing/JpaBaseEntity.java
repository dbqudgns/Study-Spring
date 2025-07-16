package study.data_jpa.entity.auditing;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

@MappedSuperclass // JPA에서 공통 필드를 다른 엔티티에 상속시키기 위한 베이스 클래스
// 순수 JPA 사용
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @PrePersist // 엔티티가 처음 저장되기 직전에 실행되는 메서드에 붙이는 어노테이션
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate // 엔티티가 DB에 수정되기 직전에 실행되는 메서드에 붙이는 어노테이션
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
