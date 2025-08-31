package com.ecomm.np.genevaecommerce.service.modelservice;

import com.ecomm.np.genevaecommerce.model.UserModel;

public interface IUserService {

    UserModel findUserById(int userid);

    UserModel findUserByEmail(String email);

    UserModel findUserByName(String userName);

    UserModel saveUser(UserModel user);

}
