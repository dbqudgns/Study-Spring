package hello.itemservice.repository.jdbctemplate;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JdbcTemplate
 */
@Slf4j
@Repository
public class JdbcTemplateItemRepositoryV1 implements ItemRepository {

    private final JdbcTemplate template;

    public JdbcTemplateItemRepositoryV1(DataSource dataSource) { //DataSource : 데이터베이스와의 연결 정보를 가지고 있는 객체
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Item save(Item item) {
        String sql = "insert into item (item_name, price, quantity) values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder(); //KeyHolder : 데이터베이스에서 기본 키값을 가져올 때 사용

        //template.update() : 데이터를 삽입하는 작업, 반환 값은 int로 영향 받은 로우 수를 나타낸다.
        template.update(connection -> {
            //PreparedStatement : SQL 쿼리를 미리 컴파일하고 파라미터를 동적으로 설정하도록 하는 도구
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"}); //sql문을 통해 JDBC가 자동 생성된 키 값을 추적하고 반환하게 하도록 설정
            ps.setString(1, item.getItemName());
            ps.setInt(2, item.getPrice());
            ps.setInt(3, item.getQuantity());
            return ps;
        }, keyHolder); //keyHolder : 자동 생성된 키 값이 저장되는 객체

        long key = keyHolder.getKey().longValue();
        item.setId(key);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=?, price=?, quantity=? where id=?";

        //template.update() : 데이터를 삽입하는 작업, 반환 값은 int로 영향 받은 로우 수를 나타낸다.
        template.update(sql,
                updateParam.getItemName(),
                 updateParam.getPrice(),
                updateParam.getQuantity(),
                itemId);
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id = ?";

        try {
            Item item = template.queryForObject(sql, itemRowMapper(), id); //template.queryForObject() : 결과 로우가 하나일 때 사용한다.
            return Optional.of(item); //Optioanl.of()는 해당 객체가 널이 아닐 때만 사용해야 한다.
        } catch (EmptyResultDataAccessException e) { //item이 널일 때 예외 발생
            return Optional.empty(); //빈 객체를 반환한다.
        }
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        String sql = "select id, item_name, price, quantity from item";

        //동적 쿼리
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }

        boolean andFlag = false;
        List<Object> param = new ArrayList<>();
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%', ?, '%')";
            param.add(itemName);
            andFlag = true;
        }

        if (maxPrice != null) {
            if(andFlag) {
                sql += " and";
            }
            sql += " price <= ?";
            param.add(maxPrice);
        }

        log.info("sql={}", sql);
        //Object[] array = param.toArray();
        return template.query(sql, itemRowMapper(), param.toArray()); //template.query() : 결과가 하나 이상일 때 사용한다. (List<T> 형태로 반환한다)
    }

    //RowMapper는 데이터베이스의 반환 결과인 ResultSet을 객체(Item)로 변환한다.
    private RowMapper<Item> itemRowMapper() {
        return (rs, rowNum) -> { //rowNum : rs에서 현재 처리 중인 행번호
            Item item = new Item();
            item.setId(rs.getLong("id"));
            item.setItemName(rs.getString("item_name"));
            item.setPrice(rs.getInt("price"));
            item.setQuantity(rs.getInt("quantity"));
            return item;
        };
    }


}
