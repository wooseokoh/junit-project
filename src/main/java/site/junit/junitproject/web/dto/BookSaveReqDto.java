package site.junit.junitproject.web.dto;

import lombok.Getter;
import lombok.Setter;
import site.junit.junitproject.domain.Book;

@Getter
@Setter // Controller에서 Setter가 호출되면서 DTO에 값이 채워짐.
public class BookSaveReqDto {
    private String title;
    private String author;

    public Book toEntity() {
        return Book.builder()
                .title(title)
                .author(author)
                .build();
    }
}
