package com.ecomm.np.genevaecommerce.extra;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import jakarta.mail.util.ByteArrayDataSource;

import java.io.ByteArrayOutputStream;


public class UrlQrCode {

    public static ByteArrayDataSource generateQR(String text) throws Exception{
        byte[] qrCodeBytes = generateQRCodeImage(text,300,300);
        return new ByteArrayDataSource(qrCodeBytes,"image/png");
    }
    public static byte[] generateQRCodeImage(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }

}
