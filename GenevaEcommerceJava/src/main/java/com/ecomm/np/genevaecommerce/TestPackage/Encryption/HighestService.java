package com.ecomm.np.genevaecommerce.TestPackage.Encryption;

import com.ecomm.np.genevaecommerce.model.dto.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.model.entity.Items;
import com.ecomm.np.genevaecommerce.repository.ItemsRepository;
import com.ecomm.np.genevaecommerce.service.modelservice.ItemService;
import com.ecomm.np.genevaecommerce.service.modelservice.OrderItemAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HighestService {
    private final OrderItemAuditService orderItemAuditService;
    private final ItemService itemService;
    private final ItemsRepository itemsRepository;

    @Autowired
    public HighestService(OrderItemAuditService orderItemAuditService, ItemService itemService, ItemsRepository itemsRepository) {
        this.orderItemAuditService = orderItemAuditService;
        this.itemService = itemService;
        this.itemsRepository = itemsRepository;
    }

    public List<ItemDisplayDTO> findHighestSellingItems(){
        List<Integer> ids = orderItemAuditService.findTopSellingItemIds();
        List<Items> items = itemsRepository.findAllById(ids);
        return items.stream().map(ItemDisplayDTO::MapByItems).toList();
    }

    public List<ItemDisplayDTO> findRandomItems(){
        List<Items> itemList = itemService.getRandomItems();
        return itemList.stream().map(ItemDisplayDTO::MapByItems).toList();
    }
}
