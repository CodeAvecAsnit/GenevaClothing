package com.ecomm.np.genevaecommerce.extra.exception;

public class OrderPublishException extends RuntimeException{

    public OrderPublishException(String message,Exception ex){
        super(message,ex);
    }
}
