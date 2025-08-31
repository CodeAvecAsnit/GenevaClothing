package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.model.dto.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.model.dto.WishListDTO;
import com.ecomm.np.genevaecommerce.model.entity.BestCollection;
import com.ecomm.np.genevaecommerce.model.entity.Collection;
import com.ecomm.np.genevaecommerce.model.entity.Items;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.service.modelservice.CollectionService;
import com.ecomm.np.genevaecommerce.service.modelservice.ICollectionService;
import com.ecomm.np.genevaecommerce.service.modelservice.IUserService;
import com.ecomm.np.genevaecommerce.service.modelservice.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BasicService {
    private final IUserService IUserService;
    private final ICollectionService collectionService;
    private final ObjectMapper objectMapper;
    private final String path ="collection.json";
    private final File file = new File(path);

    @Autowired
    public BasicService(UserService userServiceImpl
            , CollectionService collectionService, ObjectMapper objectMapper) {
        this.IUserService = userServiceImpl;
        this.collectionService = collectionService;
        this.objectMapper = objectMapper;
    }

    public Collection saveCollection(){
        return collectionService.findLatestCollection();
    }//can be removed

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

    public Set<ItemDisplayDTO> getCartItems(int userId) throws UsernameNotFoundException {
        UserModel userModel = IUserService.findUserById(userId);
        Set<Items> userItems = userModel.getCartList();
        return userItems.stream().map(ItemDisplayDTO::MapByItems).collect(Collectors.toSet());
    }


    public Set<WishListDTO> getWishListFromUser(int userId)throws UsernameNotFoundException {
        UserModel user = IUserService.findUserById(userId);
        return user.getWishList().stream()
                    .map(item -> {
                        WishListDTO dto = WishListDTO.BuildFromItems(item);
                        dto.setInCart(user.getCartList().contains(item));
                        return dto;
                    })
                    .collect(Collectors.toSet());
    }

}

