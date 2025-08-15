package com.ecomm.np.genevaecommerce.Services;




import com.ecomm.np.genevaecommerce.DTO.CollectionDTO;
import com.ecomm.np.genevaecommerce.DTO.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.DTO.ListItemDTO;
import com.ecomm.np.genevaecommerce.DTO.NewCollectionDTO;
import com.ecomm.np.genevaecommerce.Enumerations.Gender;
import com.ecomm.np.genevaecommerce.Models.Collection;
import com.ecomm.np.genevaecommerce.Models.GenderTable;
import com.ecomm.np.genevaecommerce.Models.Items;
import com.ecomm.np.genevaecommerce.Models.UserModel;
import com.ecomm.np.genevaecommerce.Repositories.CollectionRepository;
import com.ecomm.np.genevaecommerce.Repositories.GenderTableRepository;
import com.ecomm.np.genevaecommerce.Repositories.ItemsRepository;
import com.ecomm.np.genevaecommerce.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ItemsService {
    private final Random random;
    private final UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(ItemsService.class);

    private final ItemsRepository itemsRepository;

    private final GenderTableRepository genderTableRepository;

    private final CollectionRepository collectionRepository;


    @Autowired
    public ItemsService(ItemsRepository itemsRepository,
                        GenderTableRepository genderTableRepository,
                        CollectionRepository collectionRepository,
                        Random random, UserRepository userRepository) {
        this.collectionRepository = collectionRepository;
        this.genderTableRepository = genderTableRepository;
        this.itemsRepository = itemsRepository;
        this.random = random;
        this.userRepository = userRepository;
    }



    public ItemDisplayDTO findById(int id) throws Exception{
        Optional<Items> optionalItem = itemsRepository.findById(id);
        if(optionalItem.isEmpty()){
            throw new Exception("Item not found.");
        }
        Items items = optionalItem.get();
        return ItemDisplayDTO.MapByItems(items);
    }

    public Page<ItemDisplayDTO> findAll(Pageable pageable){
        Page<Items> page = itemsRepository.findAll(pageable);
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
            throw new Exception("Gender not found: " + genderStr);
        }

        GenderTable genderTable = genderTableOpt.get();

        Page<Items> page = itemsRepository.findByGenderTable(genderTable, pageable);
        return page.map(ItemDisplayDTO::MapByItems);
    }



    public Items SaveItem(ListItemDTO item) {
        Items items = ListItemDTO.ItemsMapper(item);
        Optional<Collection> collectionOptional = collectionRepository.findByCollectionName(item.getCollection());
        collectionOptional.ifPresent(items::setCollection);
        try {
            Gender gender = Gender.valueOf(item.getGender());
            Optional<GenderTable> genderTable = genderTableRepository.findByGender(gender);
            genderTable.ifPresent(items::setGenderTable);
        }catch (IllegalArgumentException ex){
            logger.error(item.getGender() + "is not a Gender in GenderTable"+ex.getMessage());
        }catch (Exception except){
            logger.error(except.getMessage());
        }
        return itemsRepository.save(items);
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
        List<Items> newArrrivalList= itemsRepository.findTop10ByOrderByCreatedDateDesc();
        return newArrrivalList.stream().
                map(ItemDisplayDTO::MapByItems).
                collect(Collectors.toList());
    }

    public CollectionDTO getLatestCollection(){
        Collection collection = collectionRepository.findTopByOrderByLaunchedDateDesc();
        String image = getRandomImage(collection.getCollectionItemList());
        return CollectionDTO.buildFromCollection(collection,image);

    }

    private String getRandomImage(List<Items> itemList){
        if(itemList==null ||itemList.isEmpty()){
            return null;
        }else{
            Items item = itemList.get(random.nextInt(itemList.size()));
            return item.getImageLink();
        }
    }

    private UserModel getUserOrThrow(int userId) throws Exception {
        return userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));
    }

    private Items getItemOrThrow(int itemId) throws Exception {
        return itemsRepository.findById(itemId)
                .orElseThrow(() -> new Exception("Item not found"));
    }


    public Boolean checkItemInCart(int userId, int itemId) {
        try {
            UserModel user = getUserOrThrow(userId);
            Items item = getItemOrThrow(itemId);
            return user.getCartList() != null && user.getCartList().contains(item);
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean checkItemInWishList(int userId, int itemId) {
        try {
            UserModel user = getUserOrThrow(userId);
            Items item = getItemOrThrow(itemId);
            return user.getWishList() != null && user.getWishList().contains(item);
        } catch (Exception e) {
            return false;
        }
    }


    public String removeFromCart(int userId,int itemId)throws Exception{
        UserModel user = getUserOrThrow(userId);
        Items item = getItemOrThrow(itemId);
        Set<Items> itemSet = user.getCartList();
        itemSet.remove(item);
        userRepository.save(user);
        return "Item removed from the cart";
    }

    public String removeFromWishList(int userId,int itemId)throws Exception{
        UserModel user = getUserOrThrow(userId);
        Items item = getItemOrThrow(itemId);
        Set<Items> itemSet = user.getWishList();
        itemSet.remove(item);
        userRepository.save(user);
        return "Item removed from the cart";
    }


    public String addItemToCart(int userId, int itemId) throws Exception {
        UserModel user = getUserOrThrow(userId);
        Items item = getItemOrThrow(itemId);
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
        Items item = getItemOrThrow(itemId);
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
