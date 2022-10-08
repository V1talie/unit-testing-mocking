package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Payment;
import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import com.endava.internship.mocking.repository.PaymentRepository;
import com.endava.internship.mocking.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {


    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private PaymentService paymentService;

    @Captor
    ArgumentCaptor<Payment> captor;

    private static User user;
    private static Payment payment;
    private static Integer id;
    private static Double amount;

    @BeforeEach
    void setUp() {
        id = 1;
        amount = 99.9;
        user = new User(id, "Igor", Status.ACTIVE);
        payment = new Payment(user.getId(), amount, "Payment from user " + user.getName());
    }

    @AfterEach
    void after(){
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(paymentRepository);
        verifyNoMoreInteractions(validationService);
    }

    @Test
    void createPayment() {

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        Payment paymentResult = paymentService.createPayment(user.getId(), amount);

        verify(validationService).validateUserId(user.getId());
        verify(validationService).validateAmount(amount);
        verify(validationService).validateUser(user);
        verify(paymentRepository).save(captor.capture());

        Payment captorResult = captor.getValue();

        assertAll(
                () -> assertEquals(payment, paymentResult),
                () -> assertThat(paymentResult).isNotNull(),
                () -> assertThat(captorResult.getMessage()).isEqualTo(paymentResult.getMessage()),
                () -> assertThat(captorResult.getAmount()).isEqualTo(paymentResult.getAmount()),
                () -> assertThat(captorResult.getUserId()).isEqualTo(paymentResult.getUserId())
        );
    }

    @Test
    void editMessage() {

        UUID uuid = payment.getPaymentId();
        String message = "Mesaj";

        when(paymentRepository.editMessage(uuid, message)).thenReturn(payment);
        Payment editPayment = paymentService.editPaymentMessage(uuid, message);

        verify(validationService).validatePaymentId(uuid);
        verify(validationService).validateMessage(message);
        verify(paymentRepository).editMessage(uuid, message);
        verifyNoInteractions(userRepository);
        assertEquals(payment, editPayment);
    }

    @Test
    void getAllByAmountExceeding() {
        Double amount = 110.0;
        List<Payment> totalPayments = Arrays.asList(
                new Payment(2, 112.2, "Payment 2"),
                new Payment(3, 250.0, "Payment 3"),
                new Payment(4, 85.3, "Payment 4")
        );

        List<Payment> expectedPayments = new ArrayList<>();
        expectedPayments.add(totalPayments.get(0));
        expectedPayments.add(totalPayments.get(1));

        when(paymentRepository.findAll()).thenReturn(totalPayments);

        List<Payment> actualPayments = paymentService.getAllByAmountExceeding(amount);
        assertEquals(expectedPayments, actualPayments);
        verify(paymentRepository).findAll();
        verifyNoInteractions(userRepository);
        verifyNoInteractions(validationService);
    }
}
