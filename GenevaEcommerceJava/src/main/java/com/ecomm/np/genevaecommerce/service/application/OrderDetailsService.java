package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.model.dto.AddressDTO;

public interface OrderDetailsService {
    AddressDTO getAddress(int id);
    AddressDTO addOrUpdateAddress(int id, AddressDTO addressDTO);
}
