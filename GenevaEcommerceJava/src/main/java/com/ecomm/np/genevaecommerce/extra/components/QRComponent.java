package com.ecomm.np.genevaecommerce.extra.components;

import com.ecomm.np.genevaecommerce.extra.util.UrlQrCode;
import com.ecomm.np.genevaecommerce.extra.util.NetworkUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * @author : Asnit Bakhati
 */

@Component
public class QRComponent {

    private byte[] qrByteArray;

    @PostConstruct
    private void init() {
        String localIp;
        try {
            localIp = NetworkUtils.getLocalIp();
        }catch (Exception ex){
            localIp="localhost";
        }
        String url = "http://" + localIp + ":5500";
        try {
            qrByteArray = UrlQrCode.generateQRCodeImage(url, 400, 400);
        }catch (Exception except){
            qrByteArray = null;
        }
    }

    public byte[] getQrByteArray() {
        return qrByteArray;
    }
}
