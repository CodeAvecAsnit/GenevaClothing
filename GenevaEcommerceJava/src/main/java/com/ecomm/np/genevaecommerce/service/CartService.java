package com.ecomm.np.genevaecommerce.service;

import com.ecomm.np.genevaecommerce.dto.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.dto.NewCollectionDTO;
import com.ecomm.np.genevaecommerce.enumeration.Gender;
import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.Collection;
import com.ecomm.np.genevaecommerce.model.GenderTable;
import com.ecomm.np.genevaecommerce.model.Items;
import com.ecomm.np.genevaecommerce.model.UserModel;
import com.ecomm.np.genevaecommerce.repository.CollectionRepository;
import com.ecomm.np.genevaecommerce.repository.GenderTableRepository;
import com.ecomm.np.genevaecommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(CartService.class);
    private final ItemServiceImpl itemService;
    private final GenderTableRepository genderTableRepository;
    private final CollectionRepository collectionRepository;

    @Autowired
    public CartService(GenderTableRepository genderTableRepository,
                       CollectionRepository collectionRepository,
                       UserRepository userRepository, ItemServiceImpl itemService) {
        this.collectionRepository = collectionRepository;
        this.genderTableRepository = genderTableRepository;
        this.userRepository = userRepository;
        this.itemService = itemService;
    }


    public ItemDisplayDTO findById(int id) throws ResourceNotFoundException{
        Items items = itemService.findItemById(id);
        return ItemDisplayDTO.MapByItems(items);
    }


    public Page<ItemDisplayDTO> findAll(Pageable pageable){
        Page<Items> page = itemService.findAllTheItems(pageable);
        return page.map(ItemDisplayDTO::MapByItems);
    }


    public Page<ItemDisplayDTO> findAll(Pageable pageable, String genderStr) throws Exception {
        Gender gender;
        try {
            gender = Gender.valueOf(genderStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid gender value: " + genderStr);
        }

        Optional<GenderTable> genderTableOpt = genderTableRepository.findByGender(gender);
        if (genderTableOpt.isEmpty()) {
            throw new ResourceNotFoundException("Gender not found: " + genderStr);
        }
        GenderTable genderTable = genderTableOpt.get();
        Page<Items> page = itemService.findGenderItems(genderTable,pageable);
        return page.map(ItemDisplayDTO::MapByItems);
    }

    public List<NewCollectionDTO> findNewCollection() {
        Collection collection = collectionRepository.findTopByOrderByLaunchedDateDesc();
        if (collection == null || collection.getCollectionItemList() == null) {
            return Collection.emptyList();
        }
        return collection.getCollectionItemList()
                .stream()
                .map(NewCollectionDTO::buildFromItem)
                .collect(Collectors.toList());
    }


    public List<ItemDisplayDTO> displayItems(String gender){
        try {
            Gender gender1 = Gender.valueOf(gender);
            Optional<GenderTable> table = genderTableRepository.findByGender(gender1);
            if(table.isPresent()){
                return table.get().getItemList().
                        stream().map(ItemDisplayDTO::MapByItems).
                        collect(Collectors.toList());
            }
        }catch (IllegalArgumentException ex){
            logger.error("THE GENDER DOES NOT EXIST");
        }
    return null;
    }


    public List<ItemDisplayDTO> displayNewArrivals(){
        List<Items> newArrrivalList= itemService.findTop10();
        return newArrrivalList.stream().
                map(ItemDisplayDTO::MapByItems).
                collect(Collectors.toList());
    }


    private UserModel getUserOrThrow(int userId) throws Exception {
        return userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));
    }


    public Boolean checkItemInCart(int userId, int itemId) {
        try {
            UserModel user = getUserOrThrow(userId);
            Items item = itemService.findItemById(itemId);
            return user.getCartList() != null && user.getCartList().contains(item);
        } catch (Exception e) {
            return false;
        }
    }


    public Boolean checkItemInWishList(int userId, int itemId) {
        try {
            UserModel user = getUserOrThrow(userId);
            Items item = itemService.findItemById(itemId);
            return user.getWishList() != null && user.getWishList().contains(item);
        } catch (Exception e) {
            return false;
        }
    }


    public String removeFromCart(int userId,int itemId)throws Exception{
        UserModel user = getUserOrThrow(userId);
        Items item = itemService.findItemById(itemId);
        Set<Items> itemSet = user.getCartList();
        itemSet.remove(item);
        userRepository.save(user);
        return "Item removed from the cart";
    }

    public String removeFromWishList(int userId,int itemId)throws Exception{
        UserModel user = getUserOrThrow(userId);
        Items item = itemService.findItemById(itemId);
        Set<Items> itemSet = user.getWishList();
        itemSet.remove(item);
        userRepository.save(user);
        return "Item removed from the cart";
    }


    public String addItemToCart(int userId, int itemId) throws Exception {
        UserModel user = getUserOrThrow(userId);
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
        userRepository.save(user);
        return "Added to Cart";
    }


    public String addItemToWishList(int userId, int itemId) throws Exception {
        UserModel user = getUserOrThrow(userId);
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
        userRepository.save(user);
        return "Added to WishList";
    }
}
