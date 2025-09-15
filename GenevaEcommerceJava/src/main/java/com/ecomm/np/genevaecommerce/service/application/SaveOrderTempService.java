package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.extra.OutOfStockException;
import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.dto.CheckDTO;
import com.ecomm.np.genevaecommerce.model.dto.QuantityItemDTO;

import java.util.List;

public interface SaveOrderTempService {
    int processAndSaveRequest(List<QuantityItemDTO> itemQuantities)throws OutOfStockException, ResourceNotFoundException;

    int processSingleItem(int itemId, int quantity, String size);

    CheckDTO fetchCheckPage(int code, int userId);
}
