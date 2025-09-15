package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.dto.ItemDisplayDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GeneralItemService {
    ItemDisplayDTO findById(int id) throws ResourceNotFoundException;

    Page<ItemDisplayDTO> findAll(Pageable pageable);

    Page<ItemDisplayDTO> findAll(Pageable pageable, String genderStr) throws Exception;

    List<ItemDisplayDTO> displayNewArrivals();
}
