package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class BasicValidationServiceTest {

    BasicValidationService basicValidation;

    @BeforeEach
    void setUp() {
        basicValidation = new BasicValidationService();
    }

    @Test
    void validateAmountIsNull_shouldThrowAnException() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> basicValidation.validateAmount(null));
        assertEquals("Amount must not be null", illegalArgumentException.getMessage());
    }

    @Test
    void validateAmountIsSmallerThanZero_shouldThrowAnException() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> basicValidation.validateAmount(-1.0));
        assertEquals("Amount must be greater than 0", illegalArgumentException.getMessage());
    }

    @Test
    void validateAmountIsGreaterThanZero_shouldNotThrowAnException() {
        assertDoesNotThrow(() -> basicValidation.validateAmount(2.0));
    }

    @Test
    void validatePaymentIdIsNull_shouldThrowAnException() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> basicValidation.validatePaymentId(null));
        assertEquals("Payment id must not be null", illegalArgumentException.getMessage());
    }

    @Test
    void validatePaymentIdIsNotNull_shouldNotThrowAnException() {
        UUID randomID = UUID.randomUUID();
        assertDoesNotThrow(() -> basicValidation.validatePaymentId(randomID));
    }

    @Test
    void validateUserIdIsNull_shouldThrowAnException() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> basicValidation.validateUserId(null));
        assertEquals("User id must not be null", illegalArgumentException.getMessage());
    }

    @Test
    void validateUserIdIsNotNull_shouldNotThrowAnException() {
        assertDoesNotThrow(() -> basicValidation.validateUserId(1));
    }

    @Test
    void validateUserStatusIsNotActive() {
        User user = new User(1, "Ion", Status.INACTIVE);
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> basicValidation.validateUser(user));
        assertEquals("User with id " + user.getId() + " not in ACTIVE status", illegalArgumentException.getMessage());

    }

    @Test
    void validateUserStatusIsActive_shouldNotThrowAnException() {
        User user = new User(1, "Ion", Status.ACTIVE);
        assertDoesNotThrow(() -> basicValidation.validateUser(user));
    }

    @Test
    void validateMessageIsNull_shouldThrowAnException() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> basicValidation.validateMessage(null));
        assertEquals("Payment message must not be null", illegalArgumentException.getMessage());
    }

    @Test
    void validateMessageIsNotNull_shouldNotThrowAnException() {
        assertDoesNotThrow(() -> basicValidation.validateMessage("message"));
    }
}