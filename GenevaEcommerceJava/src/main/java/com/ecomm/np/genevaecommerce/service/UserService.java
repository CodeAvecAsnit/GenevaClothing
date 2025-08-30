package com.ecomm.np.genevaecommerce.service;

import com.ecomm.np.genevaecommerce.dto.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.dto.UserDTO;
import com.ecomm.np.genevaecommerce.dto.WishListDTO;
import com.ecomm.np.genevaecommerce.enumeration.Role;
import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.*;
import com.ecomm.np.genevaecommerce.repository.CollectionRepository;
import com.ecomm.np.genevaecommerce.repository.ItemsRepository;
import com.ecomm.np.genevaecommerce.repository.RoleTableRepository;
import com.ecomm.np.genevaecommerce.repository.UserRepository;
import com.ecomm.np.genevaecommerce.serviceimpl.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleTableRepository roleTableRepository;
    private final CollectionRepository collectionRepository;
    private final ObjectMapper objectMapper;
    private final String path ="collection.json";
    private final File file = new File(path);

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleTableRepository roleTableRepository, CollectionRepository collectionRepository, ObjectMapper objectMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleTableRepository = roleTableRepository;
        this.collectionRepository = collectionRepository;
        this.objectMapper = objectMapper;
    }

    public Collection saveCollection(){
        return collectionRepository.findTopByOrderByLaunchedDateDesc();
    }

    public BestCollection bestCollection() throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("Could not locate the file: " + file.getAbsolutePath());
        }
        return objectMapper.readValue(file, BestCollection.class);
    }

    public String updateBestCollection(BestCollection collection) throws IOException {
        objectMapper.writeValue(file, collection);
        return "Success";
    }

    private UserModel findById(int userId){
        return userRepository.findById(userId).
                orElseThrow(()->
                        new UsernameNotFoundException("User with id : "+userId+" was not found"));
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

