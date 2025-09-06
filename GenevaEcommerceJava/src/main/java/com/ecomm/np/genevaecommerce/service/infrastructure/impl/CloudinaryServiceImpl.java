package com.ecomm.np.genevaecommerce.service.infrastructure.impl;

import com.cloudinary.Cloudinary;
import com.ecomm.np.genevaecommerce.service.infrastructure.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Map<?,?> uploadImage(MultipartFile file) throws RuntimeException,Exception{
        try {
            return cloudinary.uploader().upload(file.getBytes(),Map.of() );
        }catch (IOException e){
            throw new RuntimeException("Image uploading fail");
        }
    }



    @Override
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
