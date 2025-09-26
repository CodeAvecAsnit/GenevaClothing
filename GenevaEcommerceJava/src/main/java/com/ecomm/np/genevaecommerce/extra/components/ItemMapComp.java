package com.ecomm.np.genevaecommerce.extra.components;

import com.ecomm.np.genevaecommerce.model.dto.QuantityItemDTO;
import com.ecomm.np.genevaecommerce.model.entity.Items;
import com.ecomm.np.genevaecommerce.service.modelservice.ItemService;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.ItemServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ItemMapComp {
    private final ItemService itemService;

    public ItemMapComp(ItemServiceImpl itemServiceImpl){
        this.itemService = itemServiceImpl;
    }

    public Map<Integer, Items> getItemMap(List<QuantityItemDTO> itemQuantities){
        if(itemQuantities==null||itemQuantities.isEmpty()) return null;
        List<Integer> ids = itemQuantities.stream().map(QuantityItemDTO::getItemCode).toList();
        return itemService.findAllByListOfIds(ids).stream().collect(Collectors.toMap(Items::getItemCode, x->x));
    }

    public Items findItemById(int itemCode){
        return itemService.findItemById(itemCode);
    }
}
