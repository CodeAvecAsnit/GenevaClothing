package com.ecomm.np.genevaecommerce.service.admin.impl;

import com.ecomm.np.genevaecommerce.model.dto.AdminStatsDTO;
import com.ecomm.np.genevaecommerce.model.dto.WeekData;
import com.ecomm.np.genevaecommerce.service.admin.AdminStatisticsService;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.OrderItemAuditServiceImpl;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.OrderItemServiceImpl;
import com.ecomm.np.genevaecommerce.service.modelservice.ItemService;
import com.ecomm.np.genevaecommerce.service.modelservice.OrderItemAuditService;
import com.ecomm.np.genevaecommerce.service.modelservice.OrderItemService;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class AdminStatisticsServiceServiceImpl implements AdminStatisticsService {

    private final OrderItemAuditService orderItemAuditService;
    private final ItemService itemService;
    private final OrderItemService orderItemService;

    @Autowired
    public AdminStatisticsServiceServiceImpl(OrderItemAuditServiceImpl orderItemAuditServiceImpl, ItemServiceImpl itemServiceImpl, OrderItemServiceImpl orderItemService) {
        this.orderItemAuditService = orderItemAuditServiceImpl;
        this.itemService = itemServiceImpl;
        this.orderItemService = orderItemService;
    }

    @Override
    public AdminStatsDTO findStatsForAdmin(){
        AdminStatsDTO stats = new AdminStatsDTO();
        stats.setWeekData(this.findWeekOrders());
        stats.setTotalItems(this.findTotalItems());
        stats.setTotalOrders(this.totalOrders());
        stats.setTotalOrdersToday(this.findTotalOrdersToday());
        stats.setDeliveredOrders(this.ordersDelivered(false));
        stats.setNotDeliveredOrders(this.ordersDelivered(true));
        stats.setPackedOrders(this.ordersPacked(true));
        stats.setNotPackedOrders(this.ordersPacked(false));
        stats.setTotalSales(this.totalSales());
        stats.setTotalSalesToday(this.totalSoldToday());
        stats.setTotalItemsOrdered(this.findTotalItemsOrdered());
        stats.setPackedItems(this.findPackedItems(true));
        stats.setItemsToBePacked(this.findPackedItems(false));
        return stats;
    }

    @Override
    public List<Integer> findHighestSellingItems(){
        return orderItemAuditService.findTopSellingItemIds();
    }

    private int findTotalItems(){
        long val = itemService.findTotalItemCount();
        return (int)val;
    }

    private int totalOrders(){
        return orderItemService.countAllItems();
    }

    private int ordersDelivered(boolean delivered){
        return Math.toIntExact(orderItemService.findNotDeliveredCount(delivered));
    }

    private int ordersPacked(boolean packed){
        return Math.toIntExact(orderItemService.findNotPackedCount(packed));
    }

    private float totalSales(){
        return orderItemService.findTotalSalesCount();
    }

    private float totalSoldToday(){
        return orderItemService.findTotalSalesTodayCount();
    }

    private int findTotalOrdersToday(){
        return Math.toIntExact(orderItemService.findTotalOrdersTodayCount());
    }

    private List<WeekData> findWeekOrders(){
        List<WeekData> weekList = new ArrayList<>();
        for(Object[]  row :orderItemService.findOrderCountLast7DaysCount()){
            WeekData data = new WeekData();
            LocalDate localDate = ((java.sql.Date) row[0]).toLocalDate();
            data.setDay(localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            data.setTotalOrders((int)(long) row[1]);
            weekList.add(data);
        }
        return weekList;
    }

    private int findTotalItemsOrdered(){
        return orderItemAuditService.totalItemOrderedCount();
    }

    private int findPackedItems(boolean val){
        return orderItemAuditService.findTotalItemsPackedCount(val);
    }

}
