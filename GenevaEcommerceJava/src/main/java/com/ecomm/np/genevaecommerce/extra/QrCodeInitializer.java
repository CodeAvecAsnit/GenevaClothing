//package com.ecomm.np.genevaecommerce.extra;
//
//import org.springframework.context.SmartLifecycle;
//import org.springframework.stereotype.Component;
//
//@Component
//public class QrCodeInitializer implements SmartLifecycle {
//
//    private boolean running = false;
//
//    @Override
//    public void start() {
//        try {
//            UrlQrCode qrCode = new UrlQrCode();
//            qrCode.generateQR();
//            System.out.println("QR Code generated before server started.");
//        } catch (Exception e) {
//            System.err.println("QR Code generation failed:");
//            e.printStackTrace();
//        }
//        running = true;
//    }
//
//    @Override
//    public boolean isAutoStartup() {
//        return true;
//    }
//
//    @Override
//    public void stop() {
//        running = false;
//    }
//
//    @Override
//    public boolean isRunning() {
//        return running;
//    }
//}
