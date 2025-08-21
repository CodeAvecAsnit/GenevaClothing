package com.ecomm.np.genevaecommerce.services;

import com.ecomm.np.genevaecommerce.DTO.HistoryDTO;
import com.ecomm.np.genevaecommerce.DTO.OrderDataDTO;
import com.ecomm.np.genevaecommerce.Extras.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.Models.OrderDetails;
import com.ecomm.np.genevaecommerce.Models.OrderedItems;
import com.ecomm.np.genevaecommerce.Models.UserModel;
import com.ecomm.np.genevaecommerce.Repositories.OrderItemsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class OrderHistoryService {

    private final UserService userService;
    private final Map<Integer, String> monthMap;
    private final OrderItemsRepository orderItemsRepository;
    private final Logger log = LoggerFactory.getLogger(OrderHistoryService.class);

    @Autowired
    public OrderHistoryService(UserService userService, Map<Integer, String> monthMap, OrderItemsRepository orderItemsRepository) {
        this.userService = userService;
        this.monthMap = monthMap;
        this.orderItemsRepository = orderItemsRepository;
    }

    public Page<HistoryDTO> findUserHistory(int userId, Pageable pageable) {
        UserModel userModel = userService.findUserById(userId);
        OrderDetails orderDetails = userModel.getUserOrders();

        Page<OrderedItems> orderedItemsPage = orderItemsRepository.findByOrderDetails(orderDetails, pageable);


        return orderedItemsPage.map(ot -> {    log.debug("Mapping OrderedItem: {}", ot);
            log.debug("OrderInitiatedDate: {}", ot.getOrderInitiatedDate());
            log.debug("Building DTO...");
            HistoryDTO dto = HistoryDTO.buildFromOrderItems(ot);
            log.debug("Setting order date...");
            dto.setOrderDate(buildDate(ot.getOrderInitiatedDate()));
            return dto;
        });
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
}
