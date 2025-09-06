package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.model.dto.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.entity.GenderTable;
import com.ecomm.np.genevaecommerce.model.entity.Items;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.GenderServiceImpl;
import com.ecomm.np.genevaecommerce.service.modelservice.GenderService;
import com.ecomm.np.genevaecommerce.service.modelservice.ItemService;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneralItemService {

    private final ItemService itemService;
    private final GenderService genderService;

    @Autowired
    public GeneralItemService(ItemServiceImpl itemServiceImpl, GenderServiceImpl genderServiceImpl) {
        this.itemService = itemServiceImpl;
        this.genderService = genderServiceImpl;
    }

    public ItemDisplayDTO findById(int id) throws ResourceNotFoundException {
        Items items = itemService.findItemById(id);
        return ItemDisplayDTO.MapByItems(items);
    }


    public Page<ItemDisplayDTO> findAll(Pageable pageable){
        Page<Items> page = itemService.findAllTheItems(pageable);
        return page.map(ItemDisplayDTO::MapByItems);
    }


    public Page<ItemDisplayDTO> findAll(Pageable pageable, String genderStr) throws Exception {
        GenderTable genderTable = genderService.getGenderTable(genderStr);
        Page<Items> page = itemService.findGenderItems(genderTable,pageable);
        return page.map(ItemDisplayDTO::MapByItems);
    }


    public List<ItemDisplayDTO> displayNewArrivals(){// can be moved into another controller maybe
        List<Items> newArrrivalList= itemService.findTop10();
        return newArrrivalList.stream().
                map(ItemDisplayDTO::MapByItems).
                collect(Collectors.toList());
    }
}
