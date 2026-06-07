package com.cfs.BookMyShow.Controller;

import com.cfs.BookMyShow.Service.PaymentService;
import com.cfs.BookMyShow.dto.PaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long id)
    {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping
    public ResponseEntity<List<PaymentDto>> getAllPayments()
    {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentDto> getPaymentByTransactionId(@PathVariable String transactionId)
    {
        return ResponseEntity.ok(paymentService.getPaymentByTransactionId(transactionId));
    }
}
