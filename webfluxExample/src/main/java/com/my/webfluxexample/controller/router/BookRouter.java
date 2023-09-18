package com.my.webfluxexample.controller.router;

import com.my.webfluxexample.controller.handler.BookHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BookRouter {
    @Bean
    public RouterFunction<?> routeBook(BookHandler handler){
        return route()
                .POST("router/books",handler::createBook)
                .PATCH("router/books/{book-id}",handler::updateBook)
                .GET("router/books",handler::getBooks)
                .GET("router/books/{book-id}", handler::getBook)
                .build();

    }
}
