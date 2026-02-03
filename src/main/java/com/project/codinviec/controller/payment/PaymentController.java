package com.project.codinviec.controller.payment;

import com.project.codinviec.dto.vnpay.VNPAYCallBackResponseDTO;
import com.project.codinviec.dto.vnpay.VNPAYPaymentResponseDTO;
import com.project.codinviec.request.PageRequestCustom;
import com.project.codinviec.request.payment.PaymentRequest;
import com.project.codinviec.response.BaseResponse;
import com.project.codinviec.service.payment.PaymentService;
import com.project.codinviec.service.payment.VNPAYService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final VNPAYService vnpayService;
    @Value("${client.url}")
    private String clientUrl;


    @GetMapping
    public ResponseEntity<?> getAll(PageRequestCustom pageRequestCustom) {
        if (pageRequestCustom.getPageNumber() == 0
                && pageRequestCustom.getPageSize() == 0
                && pageRequestCustom.getKeyword() == null
                && pageRequestCustom.getSortBy() == null ){
            return ResponseEntity.ok(BaseResponse.success(paymentService.getAll(),"OK"));
        }

        return ResponseEntity.ok(BaseResponse.success(paymentService.getAllWithPage(pageRequestCustom),"OK"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(BaseResponse.success(paymentService.getById(id), "OK"));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(BaseResponse.success(paymentService.create(request), "OK"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(BaseResponse.success(paymentService.update(id, request), "OK"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        paymentService.deleteById(id);
        return ResponseEntity.ok(BaseResponse.success(null, "OK"));
    }


    @PostMapping("/vnpay/create")
    public ResponseEntity<?> createPayment(
            @Valid @RequestBody PaymentRequest request,
            HttpServletRequest httpRequest) {

        // Gọi service để tạo URL thanh toán
        VNPAYPaymentResponseDTO response = vnpayService.createPaymentUrl(request, httpRequest);

        // Trả về response thành công
        return ResponseEntity.ok(BaseResponse.success(response, "Tạo URL thanh toán thành công"));
    }


    @GetMapping("/vnpay/callback")
    public ResponseEntity<?> paymentCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String txnRef = request.getParameter("vnp_TxnRef");
        String responseCode = request.getParameter("vnp_ResponseCode");

//      Tạm sử dụng ở callback vì chưa có domain thật khi sử dụng Ipn
        VNPAYCallBackResponseDTO responseDTO = vnpayService.handleIpn(request);

        if ("00".equals(responseCode)) {
            response.sendRedirect(clientUrl+ "/status=completed");
            return ResponseEntity.ok(BaseResponse.success(responseDTO, "Thanh toán thành công!"));
        } else {
            response.sendRedirect(clientUrl+ "/status=failed");
            return ResponseEntity.ok(BaseResponse.success("", "Thanh toán thất bại!"));
        }
    }

    @PostMapping("/vnpay/ipn")
    public ResponseEntity<?> ipn(HttpServletRequest request, HttpServletResponse response) {
        VNPAYCallBackResponseDTO responseDTO = vnpayService.handleIpn(request);
        return ResponseEntity.ok(BaseResponse.success(responseDTO, "Thanh toán thành công!"));
    }
}
