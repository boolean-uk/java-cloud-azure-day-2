package com.booleanuk.simpleapi.controller;

import com.booleanuk.simpleapi.model.*;
import com.booleanuk.simpleapi.repository.BorrowableRepository;
import com.booleanuk.simpleapi.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("library")
public class Controller {
    @Autowired
    private BorrowableRepository repository;

    @PostMapping("book")
    public ResponseEntity<ApiResponse<?>> createBook (@RequestBody Book bookDetails) {
        if (!bookDetails.isValid()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not create the specified book, please check that all fields are correct.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Book book = new Book(bookDetails.getTitle(), bookDetails.getAuthor(), bookDetails.isBorrowed());

        ApiResponse<Book> response = new ApiResponse<>("success", repository.save(book));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/borrow")
    public ResponseEntity<ApiResponse<?>> borrowItem(@PathVariable int id) {
        Optional<Book> boo = this.repository.findById(id);
        if (boo.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", String.format("No item with id %d found.", id));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Book book = boo.get();
        if (book.isBorrowed()) {
            ApiResponse<String> response = new ApiResponse<>("error", String.format("Item with id %d is currently borrowed already.", id));
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        book.setBorrowed(true);

        this.repository.save(book);

        ApiResponse<Book> response = new ApiResponse<>("success", book);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<ApiResponse<?>> returnItem(@PathVariable int id) {
        Optional<Book> boo = this.repository.findById(id);
        if (boo.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", String.format("No item with id %d found.", id));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Book book = boo.get();
        if (!book.isBorrowed()) {
            ApiResponse<String> response = new ApiResponse<>("error", String.format("Item with id %d is currently borrowed already.", id));
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        book.setBorrowed(false);

        this.repository.save(book);

        ApiResponse<Book> response = new ApiResponse<>("success", book);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Book>>> readAll() {
        List<Book> books = this.repository.findAll();
        ApiResponse<List<Book>> response = new ApiResponse<>("success", books);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<?>> readBook(@PathVariable int id) {
        Optional<Book> boo = this.repository.findById(id);

        if (boo.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", String.format("No item with id %d found.", id));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Book book = boo.get();
        ApiResponse<Book> response = new ApiResponse<>("success", repository.save(book));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<?>> updateBook (@PathVariable int id, @RequestBody Book bookDetails) {
        Optional<Book> boo = this.repository.findById(id);

        if (boo.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", String.format("No book with id %d found.", id));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if (!bookDetails.isValid()) {
            ApiResponse<String> response = new ApiResponse<>("error", "Could not create the specified book, please check that all fields are correct.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Book book = boo.get();
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setBorrowed(bookDetails.isBorrowed());

        ApiResponse<Book> response = new ApiResponse<>("success", repository.save(book));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<?>> delete (@PathVariable int id) {
        Optional<Book> boo = this.repository.findById(id);

        if (boo.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>("error", String.format("No item with id %d found.", id));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Book book = boo.get();
        repository.delete(book);
        ApiResponse<Book> response = new ApiResponse<>("success", book);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
