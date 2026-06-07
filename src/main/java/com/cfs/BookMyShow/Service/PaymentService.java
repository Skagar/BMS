package com.cfs.BookMyShow.Service;

import com.cfs.BookMyShow.Exception.ResourceNotFoundException;
import com.cfs.BookMyShow.Model.Payment;
import com.cfs.BookMyShow.dto.PaymentDto;
import com.cfs.BookMyShow.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    public PaymentDto getPaymentById(Long id)
    {
        Payment payment=paymentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Payment Not found with id="+id));
        return mapToDto(payment);
    }
    public PaymentDto getPaymentByTransactionId(String transactionId)
    {
        Payment payment=paymentRepository.findByTransactionId(transactionId).orElseThrow(()->new ResourceNotFoundException("Payment Not found with transactionId="+transactionId));
        return mapToDto(payment);
    }
    public List<PaymentDto> getAllPayments()
    {
        List<Payment>payments=paymentRepository.findAll();
        return payments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    private PaymentDto mapToDto(Payment payment)
    {
        PaymentDto paymentDto=new PaymentDto();
        paymentDto.setPaymentTime(payment.getPaymentTime());
        paymentDto.setPaymentMethod(payment.getPaymentMethod());
        paymentDto.setId(payment.getId());
        paymentDto.setStatus(payment.getStatus());
        paymentDto.setAmount(payment.getAmount());
        paymentDto.setTransactionId(payment.getTransactionId());
        return paymentDto;
    }

}
