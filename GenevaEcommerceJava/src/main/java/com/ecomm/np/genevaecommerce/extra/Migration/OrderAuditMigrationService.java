package com.ecomm.np.genevaecommerce.extra.Migration;

import com.ecomm.np.genevaecommerce.model.entity.Items;
import com.ecomm.np.genevaecommerce.model.entity.OrderItemAudit;
import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;
import com.ecomm.np.genevaecommerce.repository.OrderItemAuditRepository;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.ItemServiceImpl;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.OrderItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderAuditMigrationService {

    private final OrderItemAuditRepository orderItemAuditRepository;
    private final ItemServiceImpl itemServiceImpl;
    private final OrderItemServiceImpl orderItemServiceImpl;

    @Autowired
    public OrderAuditMigrationService(OrderItemAuditRepository orderItemAuditRepository, ItemServiceImpl itemServiceImpl, OrderItemServiceImpl orderItemServiceImpl) {
        this.orderItemAuditRepository = orderItemAuditRepository;
        this.itemServiceImpl = itemServiceImpl;
        this.orderItemServiceImpl = orderItemServiceImpl;
    }

    public OrderItemAudit MapAndSave(OrderItemAuditMapper mapper){
        OrderItemAudit orderItemAudit = new OrderItemAudit();
        orderItemAudit.setActive(mapper.isActive());
        orderItemAudit.setPacked(mapper.isPacked());
        orderItemAudit.setDelivered(mapper.isDelivered());
        orderItemAudit.setItemPrice(mapper.getItemPrice());
        orderItemAudit.setQuantity(mapper.getQuantity());
        orderItemAudit.setSize(mapper.getSize());
        Items item = itemServiceImpl.findItemById(mapper.getItemCode());
        orderItemAudit.setItem(item);
        OrderedItems orderedItems = orderItemServiceImpl.findOrderedItemsById(mapper.getOrderItemId());
        orderItemAudit.setOrderedItems(orderedItems);
        return orderItemAuditRepository.save(orderItemAudit);

    }

}
