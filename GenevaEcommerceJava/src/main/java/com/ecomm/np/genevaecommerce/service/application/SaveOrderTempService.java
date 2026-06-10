package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.extra.exception.OutOfStockException;
import com.ecomm.np.genevaecommerce.extra.exception.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.dto.CheckDTO;
import com.ecomm.np.genevaecommerce.model.dto.QuantityItemDTO;

import java.util.List;

/**
 * @author : Asnit Bakhati
 */

public interface SaveOrderTempService {
    int processAndSaveRequest(List<QuantityItemDTO> itemQuantities)throws OutOfStockException, ResourceNotFoundException;

    int processSingleItem(int itemId, int quantity, String size);

    CheckDTO fetchCheckPage(int code, int userId);
}
