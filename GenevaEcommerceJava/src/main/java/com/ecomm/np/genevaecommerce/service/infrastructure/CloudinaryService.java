package com.ecomm.np.genevaecommerce.service.infrastructure;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author : Asnit Bakhati
 */

public interface CloudinaryService {
    Map<?,?> uploadImage(MultipartFile file)throws RuntimeException,Exception;
    void deleteImageFromCloudinary(String publicId);
}
