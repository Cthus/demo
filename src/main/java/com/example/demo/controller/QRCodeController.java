package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.QRCodeService;

@RestController
@RequestMapping("/api/qrcode")
public class QRCodeController {

    @Autowired
    private QRCodeService qrCodeService;

    @GetMapping("/validate")
    public ResponseEntity<String> validateQRCode(@RequestParam String qrCode) {
    boolean isValid = qrCodeService.validateQRCode(qrCode);
    return isValid ? ResponseEntity.ok("验证通过，闸机打开") : ResponseEntity.status(HttpStatus.FORBIDDEN).body("二维码无效或已使用");
}


    @GetMapping("/generate")
    public ResponseEntity<String> generateQRCode() {
        try {
            String fileName = qrCodeService.generateQRCode();
            return ResponseEntity.ok("二维码已生成: " + fileName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("生成二维码失败");
        }
    }

    @GetMapping("/decode")
    public ResponseEntity<String> decodeQRCode() {
        try {
            String result = qrCodeService.decodeQRCode("qrcode.png");
            return ResponseEntity.ok("二维码内容：" + result);
        } catch (IOException | com.google.zxing.NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("解析二维码失败！");
        }
    }
}
