package com.ecomm.np.genevaecommerce.dto;

public class Checkers {
    private Boolean hasInCart;
    private Boolean hasWished;

    public Checkers() {
    }

    public Checkers(Boolean hasInCart, Boolean hasWished) {
        this.hasInCart = hasInCart;
        this.hasWished = hasWished;
    }

    public Boolean getHasInCart() {
        return hasInCart;
    }

    public void setHasInCart(Boolean hasInCart) {
        this.hasInCart = hasInCart;
    }

    public Boolean getHasWished() {
        return hasWished;
    }

    public void setHasWished(Boolean hasWished) {
        this.hasWished = hasWished;
    }
}
