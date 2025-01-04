package hello.itemservice.repository.jdbctemplate;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * NamedParameterJdbcTemplate
 * SqlParameterSource
 * - BeanPropertySqlParameterSource
 * - MapSqlParameterSource
 * Map
 *
 * BeanPropertyRowMapper
 */

@Slf4j
@Repository
public class JdbcTemplateItemRepositoryV2 implements ItemRepository {

    //NamedParameterJdbcTemplate : 이름 기반 파라미터 바인딩 JdbcTemplate
    private final NamedParameterJdbcTemplate template;

    public JdbcTemplateItemRepositoryV2(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Item save(Item item) {
        String sql = "insert into item (item_name, price, quantity) values (:itemName, :price, :quantity)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        /*
        자바빈 프로퍼티 규약(자바에서의 클래스 규약)을 통해 자동으로 파라미터 객체를 생성한다.
        예를 들어, Item 객체의 getItemName(), getPrice()가 있으면 다음과 같은 데이터를 자동으로 만들어낸다.
        key = itemName, value = item.getItemName()
        key = price, value = item.getPrice()
         */

        KeyHolder keyHolder = new GeneratedKeyHolder(); //KeyHolder : 데이터베이스에서 기본 키값을 가져올 때 사용

        template.update(sql, param, keyHolder); //param을 통해 SQL문의 매개변수를 이름(키)을 통해 값을 매칭한다.

        long key = keyHolder.getKey().longValue();
        item.setId(key);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=:itemName, price=:price, quantity=:quantity where id=:id";

        //MapSqlParameterSource() : SQL 타입을 지정할 수 있는 SQL에 특화된 기능을 제공 => 메서드 체인 가능 !
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("itemName", updateParam.getItemName())
                .addValue("price", updateParam.getPrice())
                .addValue("quantity", updateParam.getQuantity())
                .addValue("id", itemId);//updateParam에는 id 변수가 없으므로 MapSqlParameterSource()를 사용하였다.


        template.update(sql, param);

    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id = :id";

        try {
            Map<String, Object> param = Map.of("id", id); //단순히 키(이름) 값을 Map을 이용하는 방법
            Item item = template.queryForObject(sql, param, itemRowMapper());
            return Optional.of(item);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);
        /*
        자바빈 프로퍼티 규약(자바에서의 클래스 규약)을 통해 자동으로 파라미터 객체를 생성한다.
        예를 들어, ItemSearchCond 객체의 getItemName(), getPrice()가 있으면 다음과 같은 데이터를 자동으로 만들어낸다.
        key = itemName, value = cond.getItemName()
        key = price, value = cond.getPrice()
         */

        String sql = "select id, item_name, price, quantity from item";

        //동적 쿼리
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }

        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%', :itemName, '%')";
            andFlag = true;
        }

        if (maxPrice != null) {
            if(andFlag) {
                sql += " and";
            }
            sql += " price <= :maxPrice";
        }

        log.info("sql={}", sql);
        return template.query(sql, param, itemRowMapper());
    }

    private RowMapper<Item> itemRowMapper() {
        //BeanPropertyRowMapper는 ResultSet의 결과를 받아서 자바빈 규약에 맞추어 데이터를 변환한다.
       return BeanPropertyRowMapper.newInstance(Item.class); //BeanPropertyRowMapper는 언더스코어(snake_case) 표기법을 camel로 자동 변환 지원
    }


}
