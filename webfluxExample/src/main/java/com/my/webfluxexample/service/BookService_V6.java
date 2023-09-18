package com.my.webfluxexample.service;

import com.my.webfluxexample.domain.Book;
import com.my.webfluxexample.exception.BusinessLogicException;
import com.my.webfluxexample.exception.ExceptionCode;
import com.my.webfluxexample.repository.BookRepository;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.*;

@Service
@Validated
public class BookService_V6 {
    private final @Nonnull R2dbcEntityTemplate template;

    public BookService_V6(BookRepository repository, @Nonnull R2dbcEntityTemplate template) {
        this.template = template;
    }

    public Mono<Book> createBook(Book book){
        return verifyExistIsbn(book.getIsbn())
                .then(template.insert(book));
    }

    public Mono<Book> updateBook(Book book) {
        return findVerifiedBook(book.getBookId())
                .map(findBook -> findBook.mergeCreateNonNullField(book))
                .flatMap(updatingBook -> template.update(updatingBook));
    }

    public Mono<Book> findBook(long bookId) {
        return findVerifiedBook(bookId);
    }

    public Mono<List<Book>> findBooks(@Positive int page, @Positive int size){
        return template
                .select(Book.class)
                .count()
                .flatMap(total -> {
                    Tuple2<Long, Long> skipAndTake = getSkipAndTake(total, page, size);
                    return template
                            .select(Book.class)
                            .all()
                            .skip(skipAndTake.getT1())
                            .take(skipAndTake.getT2())
                            .collectSortedList((Book o1, Book o2) -> (int) (o1.getBookId() - o2.getBookId()));
                });
    }

    private Tuple2<Long, Long> getSkipAndTake(Long total, int movePage, int size) {
        long totalPages = (long) Math.ceil((double) total / size);
        long page = movePage > totalPages ? totalPages : movePage;
        long skip = total - (page * size) < 0 ? 0 : total - (page * size);
        long take = total - (page * size) < 0 ? total - ((page -1 * size)) : size;

        return Tuples.of(skip, take);
    }


    private Mono<Book> findVerifiedBook(long bookId){
        return template.selectOne(query(where("book_id").is(bookId)), Book.class)
                .switchIfEmpty(Mono.error(new BusinessLogicException(ExceptionCode.BOOK_NOT_FOUND)));
    }

    private Mono<Void> verifyExistIsbn(String isbn) {
        return template.selectOne(query(where("isbn").is(isbn)), Book.class)
                .flatMap(findBook -> {
                    if (findBook != null) return Mono.error(new BusinessLogicException(ExceptionCode.BOOK_EXISTS));
                    return Mono.empty();
                });
    }
}
