package com.ecomm.np.genevaecommerce.dto;

import com.ecomm.np.genevaecommerce.model.Items;

public class WishListDTO {
    private int itemId;
    private String itemName;
    private float itemPrice;
    private boolean available;
    private boolean inCart;
    private String imageLink;

    public WishListDTO() {
        this.available=false;
        this.inCart=false;
    }

    public WishListDTO(int itemId, String itemName, float itemPrice, boolean available, boolean inCart,String imageLink) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.available = available;
        this.inCart = inCart;
        this.imageLink = imageLink;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public static WishListDTO BuildFromItems(Items item){
        WishListDTO wishListDTO = new WishListDTO();
        wishListDTO.setAvailable(item.getStock()>0);
        wishListDTO.setItemId(item.getItemCode());
        wishListDTO.setItemName(item.getItemName());
        wishListDTO.setItemPrice(item.getPrice());
        wishListDTO.setImageLink(item.getImageLink());
        return wishListDTO;
    }
}
