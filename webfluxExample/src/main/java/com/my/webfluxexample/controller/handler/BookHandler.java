package com.my.webfluxexample.controller.handler;

import com.my.webfluxexample.controller.validator.BookValidator;
import com.my.webfluxexample.domain.Book;
import com.my.webfluxexample.dto.BookDto;
import com.my.webfluxexample.mapper.BookMapper;
import com.my.webfluxexample.service.BookService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookHandler {
    private final BookMapper mapper;
    private final BookValidator validator;
    private final BookService service;
    public BookHandler(BookMapper mapper, BookValidator validator, BookService service) {
        this.mapper = mapper;
        this.validator = validator;
        this.service = service;
    }

    public Mono<ServerResponse> createBook(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BookDto.Post.class)
                .doOnNext(validator::validate)
                .flatMap(post -> service.createBook(mapper.bookPostToBook(post)))
                .flatMap(book -> ServerResponse
                        .created(URI.create("route/books/" + book.getBookId()))
                        .build());
    }

    public Mono<ServerResponse> updateBook(ServerRequest serverRequest) {
        final long bookId = Long.parseLong(serverRequest.pathVariable("book-id"));
        return serverRequest
                .bodyToMono(BookDto.Patch.class)
                .doOnNext(validator::validate)
                .flatMap(patch -> {
                    patch.setBookId(bookId);
                    return service.updateBook(mapper.bookPatchToBook(patch));
                })
                .flatMap(book -> ServerResponse
                        .ok()
                        .bodyValue(mapper.bookToResponse(book)));
    }


    public Mono<ServerResponse> getBooks(ServerRequest request) {
        return service.findBooks(0,0)
                .flatMap(books -> ServerResponse
                        .ok()
                        .bodyValue(mapper.booksToResponse(books)));
    }

    public Mono<ServerResponse> getBook(ServerRequest request) {
        long bookId = Long.parseLong(request.pathVariable("book-id"));
        return service.findBook(bookId)
                .flatMap(book -> ServerResponse
                        .ok()
                        .bodyValue(mapper.bookToResponse(book)));

    }
}
