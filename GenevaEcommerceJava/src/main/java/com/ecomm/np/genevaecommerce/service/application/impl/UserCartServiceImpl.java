package com.ecomm.np.genevaecommerce.service.application.impl;

import com.ecomm.np.genevaecommerce.model.dto.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.model.dto.WishListDTO;
import com.ecomm.np.genevaecommerce.model.entity.Items;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.service.application.UserCartService;
import com.ecomm.np.genevaecommerce.service.modelservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : Asnit Bakhati
 */

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION,proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserCartServiceImpl implements UserCartService {
    private final UserService userService;

    @Autowired
    public UserCartServiceImpl(UserService userService) {
        this.userService = userService;
    }


    @Override
    public Set<ItemDisplayDTO> getCartItems(int userId) throws UsernameNotFoundException {
        UserModel userModel = userService.findUserById(userId);
        Set<Items> userItems = userModel.getCartList();
        return userItems.stream().map(ItemDisplayDTO::MapByItems).collect(Collectors.toSet());
    }

    @Override
    public Set<WishListDTO> getWishListFromUser(int userId)throws UsernameNotFoundException {
        UserModel user = userService.findUserById(userId);
        return user.getWishList().stream()
                .map(item -> {
                    WishListDTO dto = WishListDTO.BuildFromItems(item);
                    dto.setInCart(user.getCartList().contains(item));
                    return dto;
                })
                .collect(Collectors.toSet());
    }
}
