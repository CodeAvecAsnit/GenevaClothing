package com.ecomm.np.genevaecommerce.service.admin;

import com.ecomm.np.genevaecommerce.model.dto.AdminReadItemsDTO;
import com.ecomm.np.genevaecommerce.model.dto.ListItemDTO;
import com.ecomm.np.genevaecommerce.extra.DateFormat;
import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.entity.Collection;
import com.ecomm.np.genevaecommerce.model.entity.Items;
import com.ecomm.np.genevaecommerce.repository.OrderItemAuditRepository;
import com.ecomm.np.genevaecommerce.service.infrastructure.CloudinaryService;
import com.ecomm.np.genevaecommerce.service.modelservice.*;
import io.jsonwebtoken.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@Service
public class AdminItemService {// fix this very tightly coupled

    private static final Logger log = LogManager.getLogger(AdminItemService.class);
    private final CloudinaryService cloudinaryService;
    private final IItemService iitemService;
    private final ICollectionService collectionService;
    private final IGenderService genderService;
    private final IOrderItemAuditService orderItemAuditService;

    @Autowired
    public AdminItemService(CloudinaryService cloudinaryService,
                            ItemService itemServiceImpl,
                            CollectionService collectionService,
                            GenderService genderService,
                            OrderItemAuditService orderItemAuditService) {
        this.cloudinaryService = cloudinaryService;
        this.iitemService = itemServiceImpl;
        this.collectionService = collectionService;
        this.genderService = genderService;
        this.orderItemAuditService = orderItemAuditService;
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
        dto.setTotalOrders(orderItemAuditService.totalOrders(id));
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
            newItem.setGenderTable(genderService.getGenderTable(genderStr));
        } catch (ResourceNotFoundException ex) {
            log.error("Invalid gender '{}': {}", genderStr, ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error setting gender: {}", ex.getMessage());
        }
    }

    public void createNewCollection(String name, String description){
        collectionService.saveCollection(name,description);
    }
}
