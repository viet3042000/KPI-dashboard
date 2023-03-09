package com.b4t.app.web.rest;

import com.b4t.app.domain.Book;
import com.b4t.app.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BookResource {

//    @Autowired
//    BookRepository bookRepository;

    @PostMapping("/books")
    public ResponseEntity<Book> onSave(@RequestBody Book book) {
//        book = bookRepository.save(book);
        return ResponseEntity.ok(book);
    }
}
