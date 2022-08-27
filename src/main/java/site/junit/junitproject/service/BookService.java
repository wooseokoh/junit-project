package site.junit.junitproject.service;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import site.junit.junitproject.domain.Book;
import site.junit.junitproject.domain.BookRepository;
import site.junit.junitproject.web.dto.BookRespDto;
import site.junit.junitproject.web.dto.BookSaveReqDto;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    // 1. 책등록
    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto 책등록하기(BookSaveReqDto dto) {
        Book bookPS = bookRepository.save(dto.toEntity());
        return new BookRespDto().toDto(bookPS);
    }

    // 2. 책목록보기
    public List<BookRespDto> 책목록보기() {

        List<BookRespDto> dtos = bookRepository.findAll().stream()
                // .map((bookPS) -> bookPS.toDto())
                .map(Book::toDto)
                .collect(Collectors.toList());

        dtos.stream().forEach((b) -> {
            System.out.println(b.getId());
            System.out.println(b.getTitle());
            System.out.println("============ 서비스 레이어");
        });

        return dtos;
    }
}
