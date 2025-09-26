package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.extra.exception.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.dto.ItemDisplayDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GeneralItemService {
    ItemDisplayDTO findById(int id) throws ResourceNotFoundException;

    Page<ItemDisplayDTO> findAll(Pageable pageable);

    Page<ItemDisplayDTO> findAll(Pageable pageable, String genderStr) throws Exception;

}
