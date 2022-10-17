package com.flexpag.paymentscheduler;

import java.util.Calendar;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flexpag.exception.IdNotFoundException;

import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
public class PaymentController {
    
    PaymentRepository repository;

    @GetMapping("/scheduler")
    public List<Payment> getAllPayments() {
        return repository.findAll();
    }

    @GetMapping("/scheduler/{id}")
    public Payment getPaymentById(@PathVariable Long id) throws IdNotFoundException {
        return repository.findById(id).orElseThrow(IdNotFoundException :: new);
    }

    @GetMapping("/scheduler/status/{id}")
    public String getPaymentStatus(@PathVariable Long id) throws IdNotFoundException {
        return getPaymentById(id).getStatus();
    }

    @PostMapping("/scheduler") // Checks if the payment date is before the current date, if so, the method returns 0.
    public Long savePayment(@RequestBody Payment payment) {
        if(payment.getPaymentDate().getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
            repository.save(payment);
            return payment.getId();
        } else {
            return 0l;
        }
    }

    @PutMapping("/scheduler/{id}")
    public Payment updatePayment(@PathVariable Long id, @RequestBody Payment newPayment) throws IdNotFoundException {
        Payment payment = getPaymentById(id);
        if(payment.getStatus() == "pending") {
            payment.setAmount(newPayment.getAmount());
            payment.setPaymentDate(newPayment.getPaymentDate());
        }
        return repository.save(payment);
    }

    @DeleteMapping("/scheduler/{id}")
    public void deletePayment(@PathVariable Long id) throws IdNotFoundException {
        Payment payment = getPaymentById(id);
        if(payment.getStatus() == "pending") {
            repository.deleteById(id);
        }
    }
}