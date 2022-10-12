package com.endava.internship.mocking.repository;

import com.endava.internship.mocking.model.Payment;
import com.endava.internship.mocking.repository.InMemPaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemPaymentRepositoryTest {

    private InMemPaymentRepository paymentRepository;

    private Payment payment;

    @BeforeEach
    void setUp() {
        paymentRepository = new InMemPaymentRepository();
        payment = new Payment(1, 20.0, "Payment 1");
    }

    @Test
    void findByIdWithNullID_shouldThrowAnException() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> paymentRepository.findById(null));
        assertEquals("Payment id must not be null", illegalArgumentException.getMessage());
    }

    @Test
    void findByIDWithValidID_shouldReturnPayment() {
        paymentRepository.save(payment);
        Optional<Payment> existingPayment = paymentRepository.findById(payment.getPaymentId());
        assertThat(Optional.of(payment)).isEqualTo(existingPayment);
    }

    @Test
    void findByIDWithoutValidID_shouldReturnEmptyOptional() {
        Optional<Payment> existingPayment = paymentRepository.findById(payment.getPaymentId());
        assertThat(Optional.empty()).isEqualTo(existingPayment);
    }

    @Test
    void findAll_shouldReturnAListWithPayments() {
        Payment payment1 = new Payment(1, 20.0, "Payment 1");
        Payment payment2 = new Payment(2, 22.0, "Payment 2");
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment1);
        paymentList.add(payment2);

        List<Payment> expectedList = paymentRepository.findAll();
        assertThat(paymentList).isEqualTo(expectedList);
    }

    @Test
    void saveNullPayment_shouldThrowAnException() {
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> paymentRepository.save(null));
        assertEquals("Payment must not be null", illegalArgumentException.getMessage());
    }

    @Test
    void saveAnExistingPayment_shouldThrowAnException() {
        paymentRepository.save(payment);
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> paymentRepository.save(payment));
        assertEquals("Payment with id " + payment.getPaymentId() + "already saved", illegalArgumentException.getMessage());
    }

    @Test
    void saveAValidPayment_shouldSaveIt() {
        Payment expectedPayment = paymentRepository.save(payment);
        assertThat(payment).isEqualTo(expectedPayment);
    }

    @Test
    void editMessageOfNotExistingPayment_shouldThrowAnException() {
        UUID uuid = UUID.randomUUID();
        String message = "Message";
        final NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class,
                () -> paymentRepository.editMessage(uuid, message));
        assertEquals("Payment with id " + uuid + " not found", noSuchElementException.getMessage());
    }

    @Test
    void editMessageOfExistingPayment_shouldReturnAPaymentWithChangedMessage() {
        paymentRepository.save(payment);
        UUID uuid = payment.getPaymentId();
        String message = "Message";
        Payment changedPayment = paymentRepository.editMessage(uuid, message);
        assertThat(changedPayment.getMessage()).isEqualTo("Message");
    }
}
