package com.ecomm.np.genevaecommerce.services;

import com.ecomm.np.genevaecommerce.Models.BestCollection;
import com.ecomm.np.genevaecommerce.Models.Collection;
import com.ecomm.np.genevaecommerce.Repositories.CollectionRepository;
import com.ecomm.np.genevaecommerce.Repositories.ItemsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class HomeService {

    private final CollectionRepository collectionRepository;
    private final ItemsRepository itemsRepository;
    private final String path = "collection.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File file = new File(path);

    public HomeService(CollectionRepository collectionRepository, ItemsRepository itemsRepository) {
        this.collectionRepository = collectionRepository;
        this.itemsRepository = itemsRepository;
    }

//    public CollectionAndItemsDTO getALlCollections(){
//        List<NewCollectionDTO> list = new ArrayList<>();
//        NewCollectionDTO dto1 = new NewCollectionDTO("Averra Femme Pants","Soft-touch premium denim with a flattering fit. Averra Femme blends grace and edge for a confident, feminine look.","platedJeans.webp");
//        NewCollectionDTO dto2 = new NewCollectionDTO("Marseille Cloud","A softly flowing white dress, exquisitely designed to capture elegance and grace,perfect for making a memorable impression<","marseillecloud.webp");
//        NewCollectionDTO dto3 = new NewCollectionDTO("Soft Leather Jacket","A timeless leather jacket for men crafted to exude sophistication, confidence, and effortless style.","softleatherjacket.jpg");
//        NewCollectionDTO dto4 = new NewCollectionDTO("Vienna Eclipse Top","An elegant black top inspired by Vienna’s timeless charm,perfectly,sleek and effortlessly chic.","avverajeans.webp");
//        NewCollectionDTO dto5 = new NewCollectionDTO("Jaipur Jewels","Cool, structured, and effortlessly refined. This pink lehenga shirt echoes Jaipur’s sharp elegance. Designed for beauty and tailored for aesthetics.","jaipur.png");
//        NewCollectionDTO dto6 = new NewCollectionDTO("Himalaya Crimson Sweater","Made from soft Himalayan cashmere, this red sweater offers warmth and comfort inspired by mountain trails.","redsweats.jpg");
//        list.add(dto1);
//        list.add(dto2);
//        list.add(dto3);
//        list.add(dto4);
//        list.add(dto5);
//        list.add(dto6);
//        CollectionAndItemsDTO dto = new CollectionAndItemsDTO();
//        dto.setCollectionName("Mid-Summer Collection");
//        dto.setCollectionDescription("The latest collection by the best brand in the entire world. Affordable and the best quality" +
//                "in the world made by the best designer. Made by the greatest designer not a cloth but an art.");
//        dto.setCollectionItems(list);
//        return dto;
//    }
//
//    public List<Items> getItems() {
//        List<Items> list = new ArrayList<>();
//        Items item1 = new Items("Averra Femme Pants", "Soft-touch premium denim with a flattering fit. Averra Femme blends grace and edge for a confident, feminine look.", "platedJeans.webp");
//        item1.setPrice(200);
//        item1.setStock(45);
//        Items item2 = new Items("Marseille Cloud", "A softly flowing white dress, exquisitely designed to capture elegance and grace, perfect for making a memorable impression", "marseillecloud.webp");
//        item2.setPrice(150);
//        item2.setStock(25);
//        Items item3 = new Items("Soft Leather Jacket", "A timeless leather jacket for men crafted to exude sophistication, confidence, and effortless style.", "softleatherjacket.jpg");
//        item3.setPrice(100);
//        item3.setStock(70);
//        Items item4 = new Items("Vienna Eclipse Top", "An elegant black top inspired by Vienna’s timeless charm, perfectly sleek and effortlessly chic.", "avverajeans.webp");
//        item4.setPrice(99.99f);
//        item4.setStock(100);
//        Items item5 = new Items("Jaipur Jewels", "Cool, structured, and effortlessly refined. This pink lehenga shirt echoes Jaipur’s sharp elegance. Designed for beauty and tailored for aesthetics.", "jaipur.png");
//        item5.setPrice(2000);
//        item5.setStock(5);
//        Items item6 = new Items("Himalaya Crimson Sweater", "Made from soft Himalayan cashmere, this red sweater offers warmth and comfort inspired by mountain trails.", "redsweats.jpg");
//        item1.setPrice(1999.99f);
//        item1.setStock(7);
//
//        list.add(item1);
//        list.add(item2);
//        list.add(item3);
//        list.add(item4);
//        list.add(item5);
//        list.add(item6);
//        return list;
//    }
//
//
//
//    public Collection saveCollection() {
//        CollectionAndItemsDTO collectionAndItemsDTO = getALlCollections();
//        Collection collection = new Collection();
//        collection.setCollectionName(collectionAndItemsDTO.getCollectionName());
//        collection.setCollection_description(collectionAndItemsDTO.getCollectionDescription());
//        Collection saved = collectionRepository.save(collection);
//        for (Items item : getItems()){
//            item.setCollection(collection);
//            itemsRepository.save(item);
//        }
//        Optional<Collection> opt = collectionRepository.findByCollectionName(saved.getCollectionName());
//        return opt.orElse(null);
//    }

    public Collection saveCollection(){
        return collectionRepository.findTopByOrderByLaunchedDateDesc();
    }

    public BestCollection bestCollection() throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("Could not locate the file: " + file.getAbsolutePath());
        }
        return objectMapper.readValue(file, BestCollection.class);
    }

    public String updateBestCollection(BestCollection collection) throws IOException {
        objectMapper.writeValue(file, collection);
        return "Success";
    }




}
