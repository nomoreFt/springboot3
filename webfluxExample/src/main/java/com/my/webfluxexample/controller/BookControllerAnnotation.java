package com.my.webfluxexample.controller;

import com.my.webfluxexample.domain.Book;
import com.my.webfluxexample.dto.BookDto;
import com.my.webfluxexample.mapper.BookMapper;
import com.my.webfluxexample.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/book")
public class BookControllerAnnotation {
    private final BookService bookService;
    private final BookMapper mapper;

    public BookControllerAnnotation(BookService bookService, BookMapper mapper) {
        this.bookService = bookService;
        this.mapper = mapper;
    }

  /*  @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono createBook(@RequestBody Mono<BookDto.Post> book) {
        Mono<Book> result = bookService.createBook(book);
        return result.flatMap(r -> Mono.just(mapper.bookToResponse(r)));
    }

    @PatchMapping("/{book-id}")
    public Mono patchBook(@PathVariable("book-id") long bookId, @RequestBody Mono<BookDto.Patch> book ) {
        Mono<Book> result = bookService.updateBook(bookId, book);
        return result.flatMap(r -> Mono.just(mapper.bookToResponse(r)));
    }

    @GetMapping("/{book-id}")
    public Mono getBook(@PathVariable("book-id") long bookId) {
        Mono<Book> result = bookService.findBook(bookId);
        return result.flatMap(r -> Mono.just(mapper.bookToResponse(r)));
    }
*/


}
