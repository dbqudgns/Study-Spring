package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item.Album;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Item.Movie;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void 단일_조회() throws Exception {

        //given
        Book book = new Book();
        book.setAuthor("한강");

        //when
        itemRepository.save(book);
        Item saveBook = itemRepository.findOne(book.getId());

        //then
        assertEquals(book, saveBook);

    }

    @Test
    void 전체_조회() throws Exception {

        //given
        Album album = new Album();
        album.setArtist("안유진");

        Movie movie = new Movie();
        movie.setDirector("봉준호");

        //when
        itemRepository.save(album);
        itemRepository.save(movie);
        List<Item> allItem = itemRepository.findAll();

        //then
        assertEquals(2, allItem.size());

    }
}