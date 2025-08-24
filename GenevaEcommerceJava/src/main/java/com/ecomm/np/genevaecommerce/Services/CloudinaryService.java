package com.ecomm.np.genevaecommerce.services;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<?,?> upload(MultipartFile file) throws RuntimeException,Exception{
        try {
            return cloudinary.uploader().upload(file.getBytes(),Map.of() );
        }catch (IOException e){
            throw new RuntimeException("Image uploading fail");
        }
    }

    public void deleteImageFromCloudinary(String publicId) {
        try {
            Map<?,?> result = cloudinary.uploader().destroy(publicId, Map.of());
            String status = (String) result.get("result");
            if (!"ok".equals(status)) {
                throw new RuntimeException("Failed to delete image. Status: " + status);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cloudinary deletion failed", e);
        }
    }

}
