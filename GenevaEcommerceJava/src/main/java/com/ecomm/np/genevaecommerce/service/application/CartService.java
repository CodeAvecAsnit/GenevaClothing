package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.model.entity.Items;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.service.modelservice.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CartService {

    private final IUserService IUserService;
    private final IItemService itemService;

    @Autowired
    public CartService(UserService userServiceImpl, ItemService itemService) {
        this.IUserService = userServiceImpl;
        this.itemService = itemService;
    }

    public Boolean checkItemInCart(int userId, int itemId) {
        try {
            UserModel user = IUserService.findUserById(userId);
            Items item = itemService.findItemById(itemId);
            return user.getCartList() != null && user.getCartList().contains(item);
        } catch (Exception e) {
            return false;
        }
    }


    public Boolean checkItemInWishList(int userId, int itemId) {
        try {
            UserModel user = IUserService.findUserById(userId);
            Items item = itemService.findItemById(itemId);
            return user.getWishList() != null && user.getWishList().contains(item);
        } catch (Exception e) {
            return false;
        }
    }


    public String removeFromCart(int userId,int itemId)throws Exception{
        UserModel user = IUserService.findUserById(userId);
        Items item = itemService.findItemById(itemId);
        Set<Items> itemSet = user.getCartList();
        itemSet.remove(item);
        IUserService.saveUser(user);
        return "Item removed from the cart";
    }

    public String removeFromWishList(int userId,int itemId)throws Exception{
        UserModel user = IUserService.findUserById(userId);
        Items item = itemService.findItemById(itemId);
        Set<Items> itemSet = user.getWishList();
        itemSet.remove(item);
        IUserService.saveUser(user);
        return "Item removed from the cart";
    }


    public String addItemToCart(int userId, int itemId) throws Exception {
        UserModel user = IUserService.findUserById(userId);
        Items item = itemService.findItemById(itemId);
        Set<Items> cart = user.getCartList();
        if (cart == null) {
            cart = new HashSet<>();
            user.setCartList(cart);
        }
        if (cart.contains(item)) {
            throw new IllegalAccessException("Item already in the cart.");
        }
        cart.add(item);

        Set<UserModel> cartUsers = item.getCartUsers();
        if (cartUsers == null) {
            cartUsers = new HashSet<>();
            item.setCartUsers(cartUsers);
        }
        cartUsers.add(user);
        IUserService.saveUser(user);
        return "Added to Cart";
    }


    public String addItemToWishList(int userId, int itemId) throws Exception {
        UserModel user = IUserService.findUserById(userId);
        Items item = itemService.findItemById(itemId);
        Set<Items> wishList = user.getWishList();
        if (wishList == null) {
            wishList = new HashSet<>();
            user.setWishList(wishList);
        }
        if (wishList.contains(item)) {
            throw new IllegalAccessException("Item already in the wishlist.");
        }
        wishList.add(item);

        Set<UserModel> wishedUsers = item.getWishedUsers();
        if (wishedUsers == null) {
            wishedUsers = new HashSet<>();
            item.setWishedUsers(wishedUsers);
        }
        wishedUsers.add(user);
        IUserService.saveUser(user);
        return "Added to WishList";
    }
}
