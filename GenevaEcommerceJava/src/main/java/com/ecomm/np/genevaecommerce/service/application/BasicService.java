package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.model.dto.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.model.dto.WishListDTO;
import com.ecomm.np.genevaecommerce.model.entity.BestCollection;
import com.ecomm.np.genevaecommerce.model.entity.Collection;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.Set;

public interface BasicService {
    Collection saveCollection();

    BestCollection bestCollection() throws IOException;

    String updateBestCollection(BestCollection collection) throws IOException;

    Set<ItemDisplayDTO> getCartItems(int userId) throws UsernameNotFoundException;

    Set<WishListDTO> getWishListFromUser(int userId)throws UsernameNotFoundException;
}
