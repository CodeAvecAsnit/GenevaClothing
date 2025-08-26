package com.ecomm.np.genevaecommerce.services;

import com.ecomm.np.genevaecommerce.DTO.AdminStatsDTO;
import com.ecomm.np.genevaecommerce.DTO.WeekData;
import com.ecomm.np.genevaecommerce.Repositories.ItemsRepository;
import com.ecomm.np.genevaecommerce.Repositories.OrderItemAuditRepository;
import com.ecomm.np.genevaecommerce.Repositories.OrderItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jmx.ParentAwareNamingStrategy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class AdminStatisticsService {


    private final OrderItemAuditRepository orderItemAuditRepository;
    private final ItemsRepository itemsRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ParentAwareNamingStrategy objectNamingStrategy;

    @Autowired
    public AdminStatisticsService(OrderItemAuditRepository orderItemAuditRepository, ItemsRepository itemsRepository, OrderItemsRepository orderItemsRepository, ParentAwareNamingStrategy objectNamingStrategy) {
        this.orderItemAuditRepository = orderItemAuditRepository;
        this.itemsRepository = itemsRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.objectNamingStrategy = objectNamingStrategy;
    }

    public Map<String,Integer> getWeekData(){
        Map<String,Integer> map = new HashMap<>();
        map.put("Sunday",400);
        map.put("Monday",300);
        map.put("Tuesday",350);
        map.put("Wednesday",300);
        map.put("Thursday",250);
        map.put("Friday",450);
        map.put("Saturday",150);
        return map;
    }

    public List<Integer> findHighestSellingItems(){
        return orderItemAuditRepository.findTopSellingItemCodes();
    }


    public int findTotalItems(){
        return (int)itemsRepository.count();
    }

    public int totalOrders(){
        return (int) orderItemsRepository.count();
    }

    private int ordersDelivered(boolean delivered){
        return Math.toIntExact(orderItemsRepository.findNotDelivered(delivered));
    }

    private int ordersPacked(boolean packed){
        return Math.toIntExact(orderItemsRepository.findNotPacked(packed));
    }

    private float totalSales(){
        return orderItemsRepository.findTotalSales();
    }

    private float totalSoldToday(){
        return orderItemsRepository.findTotalSalesToday();
    }

    private int findTotalOrdersToday(){
        return Math.toIntExact(orderItemsRepository.findTotalOrdersToday());
    }

    public List<WeekData> findWeekOrders(){
        List<WeekData> weekList = new ArrayList<>();
        for(Object[]  row :orderItemsRepository.findOrderCountLast7Days()){
            WeekData data = new WeekData();
            LocalDate localDate = ((java.sql.Date) row[0]).toLocalDate();
            data.setDay(localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            data.setTotalOrders((int)(long) row[1]);
            weekList.add(data);
        }
        return weekList;
    }
    private int findTotalItemsOrdered(){
        return orderItemAuditRepository.totalItemOrdered();
    }

    private int findPackedItems(boolean val){
        return orderItemAuditRepository.findTotalItemsPacked(val);
    }

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
        stats.setTotalItems(this.findTotalItemsOrdered());
        stats.setPackedItems(this.findPackedItems(true));
        stats.setPackedItems(this.findPackedItems(false));
        return stats;
    }
}
