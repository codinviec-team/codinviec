package com.project.codinviec_core_service.service.payment;

import com.project.codinviec_core_service.dto.vnpay.VNPAYCallBackResponseDTO;
import com.project.codinviec_core_service.dto.vnpay.VNPAYPaymentResponseDTO;
import com.project.codinviec_core_service.request.payment.PaymentRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface VNPAYService {
    VNPAYPaymentResponseDTO createPaymentUrl(PaymentRequest paymentRequest, HttpServletRequest request);
    VNPAYCallBackResponseDTO handleIpn (HttpServletRequest request);
}
