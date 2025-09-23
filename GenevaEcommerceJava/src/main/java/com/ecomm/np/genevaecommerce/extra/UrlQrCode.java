package com.ecomm.np.genevaecommerce.extra;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class UrlQrCode {

    public void generateQR() throws Exception {
        String localIp = NetworkUtils.getLocalIp();
        String url = "http://" + localIp + ":5500";
        System.out.println("Generating QR for: " + url);

        String filePath = "src/main/resources/static/qrcode.png";
        File file = new File(filePath);
        if (file.exists()) {
            System.out.println("Old QR code found. Deleting...");
            if (!file.delete()) {
                throw new IOException("Failed to delete old QR code.");
            }
        }

        try {
            generateQRCode(url, filePath);
            System.out.println("QR Code generated at: " + file.getAbsolutePath());
        } catch (WriterException | IOException e) {
            System.err.println("QR Code generation failed:");
            e.printStackTrace();
        }
    }

    private void generateQRCode(String text, String filePath) throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 200, 200);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
