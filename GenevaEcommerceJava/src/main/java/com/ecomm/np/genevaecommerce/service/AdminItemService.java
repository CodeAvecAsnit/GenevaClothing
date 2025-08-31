package com.ecomm.np.genevaecommerce.service;

import com.ecomm.np.genevaecommerce.dto.AdminReadItemsDTO;
import com.ecomm.np.genevaecommerce.dto.ListItemDTO;
import com.ecomm.np.genevaecommerce.enumeration.Gender;
import com.ecomm.np.genevaecommerce.extra.DateFormat;
import com.ecomm.np.genevaecommerce.model.Collection;
import com.ecomm.np.genevaecommerce.model.Items;
import com.ecomm.np.genevaecommerce.repository.CollectionRepository;
import com.ecomm.np.genevaecommerce.repository.GenderTableRepository;
import com.ecomm.np.genevaecommerce.repository.OrderItemAuditRepository;
import com.ecomm.np.genevaecommerce.service.modelservice.CollectionService;
import com.ecomm.np.genevaecommerce.service.modelservice.ICollectionService;
import com.ecomm.np.genevaecommerce.service.modelservice.IItemService;
import com.ecomm.np.genevaecommerce.service.modelservice.ItemService;
import io.jsonwebtoken.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@Service
public class AdminItemService {

    private static final Logger log = LogManager.getLogger(AdminItemService.class);
    private final CloudinaryService cloudinaryService;
    private final IItemService iitemService;
    private final ICollectionService collectionService;
    private final GenderTableRepository genderTableRepository;
    private final OrderItemAuditRepository orderItemAuditRepository;

    @Autowired
    public AdminItemService(CloudinaryService cloudinaryService,
                            ItemService itemServiceImpl,
                            CollectionRepository collectionRepository, CollectionService collectionService,
                            GenderTableRepository genderTableRepository,
                            OrderItemAuditRepository orderItemAuditRepository) {
        this.cloudinaryService = cloudinaryService;
        this.iitemService = itemServiceImpl;
        this.collectionService = collectionService;
        this.genderTableRepository = genderTableRepository;
        this.orderItemAuditRepository = orderItemAuditRepository;
    }


    public void deleteItem(int id){
        Items item = iitemService.findItemById(id);
    }

    public void saveItem(ListItemDTO item, MultipartFile file) throws Exception {
        Map<?,?> imageMap = uploadImage(file);
        String imageUrl = (String) imageMap.get("secure_url");
        String imageId = (String) imageMap.get("public_id");
        Items newItem = ListItemDTO.ItemsMapper(item, imageUrl,imageId);
        setCollectionIfExists(newItem, item.getCollection());
        setGenderIfValid(newItem, item.getGender());
        iitemService.saveItem(newItem);
    }

    public void updateItemByAdmin(ListItemDTO dto,MultipartFile file,int integerCode) throws IOException,Exception{
        Items items = iitemService.findItemById(integerCode);
        Map<?,?> uploadedMap = uploadImage(file);
        String uploadedUrl = (String) uploadedMap.get("secure_url");
        String uploadedId = (String) uploadedMap.get("public_id");
        String oldId = items.getImageId();
        Items updatedItem = updateItem(items,dto,uploadedUrl,uploadedId);
        iitemService.saveItem(updatedItem);
        try {
            cloudinaryService.deleteImageFromCloudinary(oldId);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
    }


    public AdminReadItemsDTO readDataForAdmin(int id){
        Items item = iitemService.findItemById(id);
        AdminReadItemsDTO dto = AdminReadItemsDTO.buildFromItem(item);
        dto.setGender(item.getGenderTable().getGender());
        dto.setCollection(item.getCollection().getCollectionName());
        dto.setCartCount(item.getCartUsers().size());
        dto.setWishCount(item.getWishedUsers().size());
        dto.setCreatedDate(DateFormat.buildDate(item.getCreatedDate()));
        dto.setUpdatedDate(DateFormat.buildDate(item.getUpdatedDate()));
        dto.setTotalOrders(orderItemAuditRepository.totalOrders(id));
        return dto;
    }


    private Items updateItem(Items item,ListItemDTO dto,String imageUrl,String imageId){
        setCollectionIfExists(item,dto.getCollection());
        setGenderIfValid(item,dto.getGender());
        item.setItemName(dto.getItemName());
        item.setDescription(dto.getDescription());
        item.setStock(dto.getStock());
        item.setPrice(dto.getPrice());
        item.setImageId(imageId);
        item.setImageLink(imageUrl);
        return item;
    }


    private Map<?,?>uploadImage(MultipartFile file) throws RuntimeException {
        try {
           return cloudinaryService.upload(file);
        } catch (RuntimeException ex) {
            log.error("Image upload failed: {}", ex.getMessage());
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setCollectionIfExists(Items newItem, String collectionName) {
        try {
            Collection collection = collectionService.findCollectionByName(collectionName);
            newItem.setCollection(collection);
        } catch (Exception e) {
            log.warn("Collection not found: {}", collectionName);
        }
    }

    private void setGenderIfValid(Items newItem, String genderStr) {
        try {
            Gender gender = Gender.valueOf(genderStr);
            genderTableRepository.findByGender(gender)
                    .ifPresent(newItem::setGenderTable);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid gender '{}': {}", genderStr, ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error setting gender: {}", ex.getMessage());
        }
    }

    public void createNewCollection(String name, String description){
        collectionService.saveCollection(name,description);
    }
}
