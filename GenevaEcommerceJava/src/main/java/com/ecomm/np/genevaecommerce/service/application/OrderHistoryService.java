package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.model.dto.HistoryDTO;
import com.ecomm.np.genevaecommerce.model.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author : Asnit Bakhati
 */

public interface OrderHistoryService {
    Page<HistoryDTO> findUserHistory(int userId, Pageable pageable);

    OrderDTO findOrderData(int orderId, int userId);

    Page<HistoryDTO> findAllPagesForAdmin(Pageable pageable);

    Page<HistoryDTO> findAllPagesForAdmin(Pageable pageable, boolean isActive);

    OrderDTO findOrderDataAdmin(int orderId);
}
