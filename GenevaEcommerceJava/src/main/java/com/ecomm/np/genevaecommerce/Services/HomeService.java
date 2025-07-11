package com.ecomm.np.genevaecommerce.Services;

import com.ecomm.np.genevaecommerce.DTO.NewCollectionDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeService {

    public List<NewCollectionDTO> getALlCollections(){
        List<NewCollectionDTO> list = new ArrayList<>();
        NewCollectionDTO dto1 = new NewCollectionDTO("Averra Femme Pants","Soft-touch premium denim with a flattering fit. Averra Femme blends grace and edge for a confident, feminine look.","platedJeans.webp");
        NewCollectionDTO dto2 = new NewCollectionDTO("Marseille Cloud","A softly flowing white dress, exquisitely designed to capture elegance and grace,perfect for making a memorable impression<","marseillecloud.webp");
        NewCollectionDTO dto3 = new NewCollectionDTO("Soft Leather Jacket","A timeless leather jacket for men crafted to exude sophistication, confidence, and effortless style.","softleatherjacket.jpg");
        NewCollectionDTO dto4 = new NewCollectionDTO("Vienna Eclipse Top","An elegant black top inspired by Vienna’s timeless charm,perfectly,sleek and effortlessly chic.","avverajeans.webp");
        NewCollectionDTO dto5 = new NewCollectionDTO("Jaipur Jewels","Cool, structured, and effortlessly refined. This pink lehenga shirt echoes Jaipur’s sharp elegance. Designed for beauty and tailored for aesthetics.","jaipur.png");
        NewCollectionDTO dto6 = new NewCollectionDTO("Himalaya Crimson Sweater","Made from soft Himalayan cashmere, this red sweater offers warmth and comfort inspired by mountain trails.","redsweats.jpg");
        list.add(dto1);
        list.add(dto2);
        list.add(dto3);
        list.add(dto4);
        list.add(dto5);
        list.add(dto6);
        return list;
    }
}
