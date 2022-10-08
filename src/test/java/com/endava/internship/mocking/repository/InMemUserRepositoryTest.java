package com.endava.internship.mocking.repository;

import com.endava.internship.mocking.repository.InMemUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class InMemUserRepositoryTest {
    InMemUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new InMemUserRepository();
    }

    @Test
    void InMemUserRepositoryFindByIdWithNull_shouldThrowAnException() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> userRepository.findById(null));
        assertEquals("User id must not be null", illegalArgumentException.getMessage());
    }

    @Test
    void InMemUserRepositoryFindById_shouldNotThrowAnException() {
        assertDoesNotThrow(() -> userRepository.findById(1));
    }
}
