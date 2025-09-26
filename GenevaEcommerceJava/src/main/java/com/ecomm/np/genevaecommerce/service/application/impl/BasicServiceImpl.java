package com.ecomm.np.genevaecommerce.service.application.impl;

import com.ecomm.np.genevaecommerce.model.dto.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.model.entity.BestCollection;
import com.ecomm.np.genevaecommerce.model.entity.Collection;
import com.ecomm.np.genevaecommerce.model.entity.Items;
import com.ecomm.np.genevaecommerce.service.application.BasicService;
import com.ecomm.np.genevaecommerce.service.modelservice.ItemService;
import com.ecomm.np.genevaecommerce.service.modelservice.CollectionService;
import com.ecomm.np.genevaecommerce.service.modelservice.OrderItemAuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BasicServiceImpl implements BasicService {
    private final CollectionService collectionService;
    private final ObjectMapper objectMapper;
    private final String path ="collection.json";
    private final File file = new File(path);
    private final ItemService itemService;
    private final OrderItemAuditService orderItemAuditService;

    @Autowired
    public BasicServiceImpl(
            @Qualifier("collectionServiceImpl") CollectionService collectionService,
            ObjectMapper objectMapper,
            @Qualifier("itemServiceImpl") ItemService itemService,
            @Qualifier("orderItemAuditServiceImpl") OrderItemAuditService orderItemAuditService) {
        this.collectionService = collectionService;
        this.objectMapper = objectMapper;
        this.itemService = itemService;
        this.orderItemAuditService = orderItemAuditService;
    }

    @Override
    public Collection saveCollection(){
        return collectionService.findLatestCollection();
    }

    @Override
    public BestCollection bestCollection() throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("Could not locate the file: " + file.getAbsolutePath());
        }
        return objectMapper.readValue(file, BestCollection.class);
    }


    @Override
    public List<ItemDisplayDTO> findHighestSellingItems(){
        List<Integer> ids = orderItemAuditService.findTopSellingItemIds();
        List<Items> items = itemService.findAllById(ids);
        return items.stream().map(ItemDisplayDTO::MapByItems).toList();
    }

    @Override
    public List<ItemDisplayDTO> findRandomItems(){
        List<Items> itemList = itemService.getRandomItems();
        return itemList.stream().map(ItemDisplayDTO::MapByItems).toList();
    }

    @Override
    public List<ItemDisplayDTO> displayNewArrivals(){// can be moved into another controller maybe
        List<Items> newArrrivalList= itemService.findTop10();
        return newArrrivalList.stream().
                map(ItemDisplayDTO::MapByItems).
                collect(Collectors.toList());
    }

}

