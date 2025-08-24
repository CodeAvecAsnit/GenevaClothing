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

    public Map upload(MultipartFile file) throws IOException{
        try {
            return cloudinary.uploader().upload(file.getBytes(),Map.of() );
        }catch (IOException e){
            throw new RuntimeException("Image uploading fail");
        }
    }
}
