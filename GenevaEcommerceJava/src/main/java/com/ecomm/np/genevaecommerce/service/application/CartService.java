package com.ecomm.np.genevaecommerce.service.application;

public interface CartService {
    Boolean checkItemInCart(int userId, int itemId);

    Boolean checkItemInWishList(int userId, int itemId);

    String removeFromCart(int userId, int itemId)throws Exception;

    String removeFromWishList(int userId, int itemId)throws Exception;

    String addItemToCart(int userId, int itemId) throws Exception;

    String addItemToWishList(int userId, int itemId) throws Exception;
}
