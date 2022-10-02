package site.junit.junitproject.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import site.junit.junitproject.domain.Book;
import site.junit.junitproject.domain.BookRepository;
import site.junit.junitproject.service.BookService;
import site.junit.junitproject.web.dto.request.BookSaveReqDto;

// 통합테스트 (C, S, R)
// 컨트롤러만 테스트하는 것이 아님
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

    @Autowired
    private TestRestTemplate rt;

    @Autowired
    private BookRepository bookRepository;

    private static ObjectMapper om;
    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        System.out.println("1");
        om = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    // @BeforeAll // 테스트 시작전에 한번만 실행
    @BeforeEach // 각 테스트 시작전에 한번씩 실행
    public void 데이터준비() {
        String title = "junit";
        String author = "겟인데어";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();
        bookRepository.save(book);
    }

    @Test
    public void saveBook_test() throws Exception {
        // given
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("스프링1강");
        bookSaveReqDto.setAuthor("겟인데어");

        String body = om.writeValueAsString(bookSaveReqDto);
        // when
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(title).isEqualTo("스프링1강");
        assertThat(author).isEqualTo("겟인데어");

    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void getBookList_test() {
        // given

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.GET, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String title = dc.read("$.body.items[0].title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("junit");
        // assertThat(title).isEqualTo("spring"); 오류

    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void getBookOne_test() {
        // given
        Integer id = 1;
        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = rt.exchange("/api/v1/book/" + id, HttpMethod.GET, request, String.class);
        System.out.println(response.getBody());
        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String title = dc.read("$.body.title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("junit");
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void deleteBook_test() {
        // given
        Integer id = 1;
        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = rt.exchange("/api/v1/book/" + id, HttpMethod.DELETE, request, String.class);
        System.out.println("deleteBook_test()" + response.getBody());
        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        assertThat(code).isEqualTo(1);

    }

    // @Test
    // public void di_test() {
    // if (bookservice == null) {
    // System.out.println("null입니다.");
    // } else {
    // System.out.println("null아닙니다.");
    // }
    // }

}
