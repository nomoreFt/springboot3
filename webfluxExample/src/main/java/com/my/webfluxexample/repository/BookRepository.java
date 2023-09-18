package com.my.webfluxexample.repository;

import com.my.webfluxexample.domain.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookRepository extends ReactiveCrudRepository<Book, Long>{
    Mono<Book> findByIsbn(String isbn);

    Flux<Book> findAllBy(Pageable pageable);
}
