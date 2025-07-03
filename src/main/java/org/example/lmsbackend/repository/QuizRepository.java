// QuizRepository.java
// ------------------------
package org.example.lmsbackend.repository;

import org.example.lmsbackend.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
}