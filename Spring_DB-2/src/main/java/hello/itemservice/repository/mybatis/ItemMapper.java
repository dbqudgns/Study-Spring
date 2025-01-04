package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper //Mybatis가 매핑 인터페이스를 인식할 수 있는 애노테이션
//Mybatis 매핑 XML을 호출해주는 Mapper 인터페이스
public interface ItemMapper {

    void save(Item item);

    //@Param : 파라미터가 두 개 이상일 경우 써줘야 하는 애노테이션
    void update(@Param("id") Long id, @Param("updateParam") ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findAll(ItemSearchCond itemSearch);

}
