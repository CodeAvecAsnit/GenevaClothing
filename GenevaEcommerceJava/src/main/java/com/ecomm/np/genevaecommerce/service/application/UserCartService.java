package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.model.dto.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.model.dto.WishListDTO;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

public interface UserCartService {

    public Set<WishListDTO> getWishListFromUser(int userId)throws UsernameNotFoundException;

    public Set<ItemDisplayDTO> getCartItems(int userId) throws UsernameNotFoundException;


}
