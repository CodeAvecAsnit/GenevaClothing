package com.ecomm.np.genevaecommerce.Services;

import com.ecomm.np.genevaecommerce.DTO.UserDTO;
import com.ecomm.np.genevaecommerce.Enumerations.Role;
import com.ecomm.np.genevaecommerce.Models.Items;
import com.ecomm.np.genevaecommerce.Models.RoleTable;
import com.ecomm.np.genevaecommerce.Models.UserModel;
import com.ecomm.np.genevaecommerce.Repositories.ItemsRepository;
import com.ecomm.np.genevaecommerce.Repositories.RoleTableRepository;
import com.ecomm.np.genevaecommerce.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleTableRepository roleTableRepository;
    private final ItemsRepository itemsRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleTableRepository roleTableRepository, ItemsRepository itemsRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleTableRepository = roleTableRepository;
        this.itemsRepository = itemsRepository;
    }

    public UserModel saveUser(UserDTO userDTO) {
        UserModel userModel = UserDTO.userModelBuild(userDTO);
        Role role = Role.USER;
        RoleTable roleTable =roleTableRepository.findByRole(role);
        userModel.setRoleTable(roleTable);
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        return userRepository.save(userModel);
    }


    public String itemToCart(int userId, int itemId) {
        Optional<UserModel> user = userRepository.findById(userId);
        if(user.isPresent()){
            UserModel userModel = user.get();
            Optional<Items> item = itemsRepository.findById(itemId);
            item.ifPresent(userModel::addToCart);
            userRepository.save(userModel);
            return "Item Added to Cart";
        }

        return "User Not found";
    }

    public Set<Items> getCartItems(int user_id){
        Optional<UserModel> user = userRepository.findById(user_id);
        return user.map(UserModel::getCartList).orElse(null);
    }


    public String itemToWishList(int userId, int itemId) {
        Optional<UserModel> user = userRepository.findById(userId);
        if(user.isPresent()){
            UserModel userModel = user.get();
            Optional<Items> item = itemsRepository.findById(itemId);
            item.ifPresent(userModel::addToWishList);
            userRepository.save(userModel);
            return "Item Added to Wishlist";
        }

        return "User Not found";
    }

    public Set<Items> getWishListItems(int user_id){
        Optional<UserModel> user = userRepository.findById(user_id);
        return user.map(UserModel::getWishList).orElse(null);
    }

}

