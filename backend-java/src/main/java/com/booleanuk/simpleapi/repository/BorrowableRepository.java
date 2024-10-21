package com.booleanuk.simpleapi.repository;

import com.booleanuk.simpleapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowableRepository extends JpaRepository<Book, Integer> {
}
