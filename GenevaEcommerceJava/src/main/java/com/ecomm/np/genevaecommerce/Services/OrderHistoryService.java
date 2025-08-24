package com.ecomm.np.genevaecommerce.services;

import com.ecomm.np.genevaecommerce.DTO.HistoryDTO;
import com.ecomm.np.genevaecommerce.DTO.OrderDataDTO;
import com.ecomm.np.genevaecommerce.Extras.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.Models.OrderDetails;
import com.ecomm.np.genevaecommerce.Models.OrderItemAudit;
import com.ecomm.np.genevaecommerce.Models.OrderedItems;
import com.ecomm.np.genevaecommerce.Models.UserModel;
import com.ecomm.np.genevaecommerce.Repositories.OrderItemAuditRepository;
import com.ecomm.np.genevaecommerce.Repositories.OrderItemsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderHistoryService {

    private final UserService userService;
    private final Map<Integer, String> monthMap;
    private final OrderItemsRepository orderItemsRepository;
    private final Logger log = LoggerFactory.getLogger(OrderHistoryService.class);
    private final OrderItemAuditRepository orderItemAuditRepository;

    @Autowired
    public OrderHistoryService(UserService userService, Map<Integer, String> monthMap, OrderItemsRepository orderItemsRepository, OrderItemAuditRepository orderItemAuditRepository) {
        this.userService = userService;
        this.monthMap = monthMap;
        this.orderItemsRepository = orderItemsRepository;
        this.orderItemAuditRepository = orderItemAuditRepository;
    }

    public Page<HistoryDTO> findUserHistory(int userId, Pageable pageable) {
        UserModel userModel = userService.findUserById(userId);
        OrderDetails orderDetails = userModel.getUserOrders();
        Page<OrderedItems> orderedItemsPage = orderItemsRepository.findByOrderDetails(orderDetails, pageable);
        return transform(orderedItemsPage);
    }


    public OrderDataDTO findOrderData(int orderId,int userId){
        OrderedItems orderedItems = orderItemsRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Requested order was not found"));
        int UserId = orderedItems.getOrderDetails().getUser().getUserId();
        if(UserId!=userId){
            throw new AccessDeniedException("Access Denied");
        }
        OrderDataDTO dto = OrderDataDTO.buildFromOrderItems(orderedItems);
        dto.setOrderDate(buildDate(orderedItems.getOrderInitiatedDate()));
        return dto;
    }


    private Page<HistoryDTO> transform(Page<OrderedItems> orderedItemsPage){
        return orderedItemsPage.map(ot -> {
            HistoryDTO dto = HistoryDTO.buildFromOrderItems(ot);
            dto.setOrderDate(buildDate(ot.getOrderInitiatedDate()));
            return dto;
        });
    }


    public Page<HistoryDTO> findAllPagesForAdmin(Pageable pageable){
        Page<OrderedItems> orderedItemsPage = orderItemsRepository.findAll(pageable);
        return transform(orderedItemsPage);
    }

    public Page<HistoryDTO> findAllPagesForAdmin(Pageable pageable,boolean isActive){
        Page<OrderedItems> orderedItemsPage = orderItemsRepository.findByMainActive(isActive,pageable);
        return transform(orderedItemsPage);
    }

    public OrderDataDTO findOrderDataAdmin(int orderId){
        OrderedItems orderedItems = findOrderedItem(orderId);
        OrderDataDTO dto = OrderDataDTO.buildFromOrderItems(orderedItems);
        dto.setOrderDate(buildDate(orderedItems.getOrderInitiatedDate()));
        return dto;
    }

    public boolean setDeliveredAdmin(int orderId){
        OrderedItems orderedItems = findOrderedItem(orderId);
        if(!orderedItems.isProcessed()){
            throw new RuntimeException("Cannot set delivered because order is yet to be packed");
        }
        if(!orderedItems.isMainActive()){throw new RuntimeException("Order has already been delivered");}
        orderedItems.setMainActive(false);
        orderItemsRepository.save(orderedItems);
        return true;
    }

    public boolean setPackedByAdmin(int orderItemCode){
        OrderItemAudit oa = findOrderedItemAudit(orderItemCode);
        if(oa.isPacked()) return true;
        oa.setPacked(true);
        orderItemAuditRepository.save(oa);
        if(checkAllPacked(oa.getOrderedItems())){
            setOrderPacked(oa.getOrderedItems());
        }
        return true;
    }

    public boolean setAllPackedAdmin(int orderCode){
        OrderedItems oI = findOrderedItem(orderCode);
        if(checkAllPacked(oI)) {
            setOrderPacked(oI);
            return true;
        }
        List<OrderItemAudit> olList = oI.getOrderItemAuditList();
        olList.forEach(oa->oa.setPacked(true));
        orderItemAuditRepository.saveAll(olList);
        setOrderPacked(oI);
        return true;
    }


    private OrderedItems findOrderedItem(int orderItemCode){
        return orderItemsRepository.findById(orderItemCode).orElseThrow(
                ()-> new ResourceNotFoundException("The requested ordered item was not found"));

    }

    private OrderItemAudit findOrderedItemAudit(int orderItemCode){
        return orderItemAuditRepository.findById(orderItemCode).orElseThrow(
                ()-> new ResourceNotFoundException("The requested ordered item was not found"));

    }

    public boolean checkAllPacked(OrderedItems orderedItems){
        return orderedItems.getOrderItemAuditList().stream()
                .allMatch(OrderItemAudit::isPacked);
    }

    public String buildDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "Unknown date";
        }
        int day = localDateTime.getDayOfMonth();
        int month = localDateTime.getMonthValue();
        int year = localDateTime.getYear();
        String suffix = getDaySuffix(day);
        String monthName = monthMap.get(month);
        return String.format("%d%s, %s %d", day, suffix, monthName, year);
    }

    private String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        return switch (day % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    private void setOrderPacked(OrderedItems orderedItems){
        if(!orderedItems.isProcessed()){
            orderedItems.setProcessed(true);
            orderItemsRepository.save(orderedItems);
        }
    }
}
