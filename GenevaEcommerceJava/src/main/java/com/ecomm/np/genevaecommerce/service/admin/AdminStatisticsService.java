package com.ecomm.np.genevaecommerce.service.admin;

import com.ecomm.np.genevaecommerce.model.dto.AdminStatsDTO;


import java.util.List;

public interface AdminStatisticsService {
    List<Integer> findHighestSellingItems();
    AdminStatsDTO findStatsForAdmin();
}
