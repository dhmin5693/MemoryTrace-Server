package com.memorytrace.repository;

import com.memorytrace.domain.UserBook;
import com.memorytrace.domain.UserBookPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookRepository extends JpaRepository<UserBook, UserBookPK> {

}
