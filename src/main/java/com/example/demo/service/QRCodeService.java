package com.example.demo.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import  java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

@Service  // 添加 @Service 让 Spring 管理
public class QRCodeService {

    public String gernerateQRCodeText() {
        return "MetroTicket—"+UUID.randomUUID().toString();
    }

    // 生成二维码
    public String generateQRCode() throws Exception {
        // 生成唯一的二维码内容
        String qrText = "MetroTicket-" + UUID.randomUUID().toString();

        // 设置二维码大小
        int width = 300;
        int height = 300;
        
        // 生成二维码
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, width, height);
        
        // 生成唯一的文件名
        String fileName = "qrcode-" + System.currentTimeMillis() + ".png"; 
        Path path = FileSystems.getDefault().getPath(fileName);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return fileName; // 返回二维码文件名
    }

    // 解析二维码
    public String decodeQRCode(String filePath) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(new File(filePath));
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }

    private final Set<String> validTickets = new HashSet<>(); // 存储已生成的二维码

    public String saveQRCode() throws Exception {
        String qrText = "MetroTicket-" + UUID.randomUUID().toString();
        validTickets.add(qrText); // 记录到存储
        return qrText;
    }

    public boolean validateQRCode(String qrCode) {
        if (validTickets.contains(qrCode)) {
            validTickets.remove(qrCode); // 票只能用一次
            return true; // 允许通行
        }
        return false; // 票无效或已使用
    }
}
