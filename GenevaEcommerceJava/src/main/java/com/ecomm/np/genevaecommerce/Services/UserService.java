package com.ecomm.np.genevaecommerce.services;

import com.ecomm.np.genevaecommerce.DTO.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.DTO.PasswordDTO;
import com.ecomm.np.genevaecommerce.DTO.UserDTO;
import com.ecomm.np.genevaecommerce.DTO.WishListDTO;
import com.ecomm.np.genevaecommerce.Enumerations.Role;
import com.ecomm.np.genevaecommerce.Extras.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.Models.Items;
import com.ecomm.np.genevaecommerce.Models.RoleTable;
import com.ecomm.np.genevaecommerce.Models.UserModel;
import com.ecomm.np.genevaecommerce.Repositories.ItemsRepository;
import com.ecomm.np.genevaecommerce.Repositories.RoleTableRepository;
import com.ecomm.np.genevaecommerce.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

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

    private UserModel findById(int userId){
        return userRepository.findById(userId).
                orElseThrow(()->
                        new UsernameNotFoundException("User with id : "+userId+" was not found"));
    }

    private Items findItemById(int itemCode){
        return itemsRepository.findById(itemCode).
                orElseThrow(()->new ResourceNotFoundException("Item with code : "+" was not found"));
    }

    public UserModel saveUser(UserDTO userDTO) {
        UserModel userModel = UserDTO.userModelBuild(userDTO);
        Role role = Role.USER;
        RoleTable roleTable =roleTableRepository.findByRole(role);
        userModel.setRoleTable(roleTable);
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        return userRepository.save(userModel);
    }


    public Set<ItemDisplayDTO> getCartItems(int userId) throws UsernameNotFoundException {
        UserModel userModel = findById(userId);
        Set<Items> userItems = userModel.getCartList();
        return userItems.stream().map(ItemDisplayDTO::MapByItems).collect(Collectors.toSet());
    }


    public Set<ItemDisplayDTO> getWishListItems(int user_id)  {
            UserModel user =findById(user_id);
            Set<Items> userItems = user.getWishList();
            return userItems.stream().map(ItemDisplayDTO::MapByItems).collect(Collectors.toSet());
        }

    public int findNoOfItemsInCart(int userId){
        return userRepository.countItemsInCart(userId);
    }

    public Set<WishListDTO> getWishListFromUser(int userId)throws UsernameNotFoundException {
        UserModel user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("The user with this Id doesn't exist"));
        return user.getWishList().stream()
                    .map(item -> {
                        WishListDTO dto = WishListDTO.BuildFromItems(item);
                        dto.setInCart(user.getCartList().contains(item));
                        return dto;
                    })
                    .collect(Collectors.toSet());
    }



    public UserModel findUserById(int userId){
        return userRepository.findById(userId).orElseThrow(()->new UsernameNotFoundException("The requested User was not found"));
    }
}

