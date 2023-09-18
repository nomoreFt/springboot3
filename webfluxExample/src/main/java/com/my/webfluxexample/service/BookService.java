package com.my.webfluxexample.service;

import com.my.webfluxexample.domain.Book;
import com.my.webfluxexample.exception.BusinessLogicException;
import com.my.webfluxexample.exception.ExceptionCode;
import com.my.webfluxexample.repository.BookRepository;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Mono<Book> createBook(Book book){
        return verifyExistIsbn(book.getIsbn())
                .then(repository.save(book));
    }

    public Mono<Book> updateBook(Book book) {
        return findVerifiedBook(book.getBookId())
                .map(findBook -> findBook.mergeCreateNonNullField(book))
                .flatMap(updatingBook -> repository.save(updatingBook));
    }

    public Mono<Book> findBook(long bookId) {
        return findVerifiedBook(bookId);
    }

    public Mono<List<Book>> findBooks(@Positive int page, @Positive int size){
        return repository.findAllBy(
                PageRequest
                .of(page-1
                        ,size
                        ,Sort.by("memberId").descending()))
                .collectList();
    }


    public Mono<PageImpl<Book>> findBooks2(@Positive int page, @Positive int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("memberId").descending());

        Mono<Long> totalCount = repository.count();
        Flux<Book> bookFlux = repository.findAllBy(pageRequest);

        return Mono.zip(bookFlux.collectList(), totalCount)
                .map(tuple -> {
                    List<Book> books = tuple.getT1();
                    Long count = tuple.getT2();
                    return new PageImpl<>(books, pageRequest, count);
                });
    }


    private Mono<Book> findVerifiedBook(long bookId){
        return repository.findById(bookId)
                .switchIfEmpty(Mono.error(new BusinessLogicException(ExceptionCode.BOOK_NOT_FOUND)));
    }

    private Mono<Void> verifyExistIsbn(String isbn) {
        return repository.findByIsbn(isbn)
                .flatMap(findBook -> {
                    if (findBook != null) return Mono.error(new BusinessLogicException(ExceptionCode.BOOK_EXISTS));
                    return Mono.empty();
                });
    }
}
