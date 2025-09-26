package com.ecomm.np.genevaecommerce.service.admin.impl;

import com.ecomm.np.genevaecommerce.model.dto.AdminReadItemsDTO;
import com.ecomm.np.genevaecommerce.model.dto.ListItemDTO;
import com.ecomm.np.genevaecommerce.extra.util.DateFormat;
import com.ecomm.np.genevaecommerce.extra.exception.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.entity.Collection;
import com.ecomm.np.genevaecommerce.model.entity.Items;
import com.ecomm.np.genevaecommerce.service.admin.AdminItemService;
import com.ecomm.np.genevaecommerce.service.infrastructure.CloudinaryService;
import com.ecomm.np.genevaecommerce.service.modelservice.CollectionService;
import com.ecomm.np.genevaecommerce.service.modelservice.GenderService;
import com.ecomm.np.genevaecommerce.service.modelservice.ItemService;
import com.ecomm.np.genevaecommerce.service.modelservice.OrderItemAuditService;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.CollectionServiceImpl;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.GenderServiceImpl;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.ItemServiceImpl;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.OrderItemAuditServiceImpl;
import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@Service
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminItemServiceImpl implements AdminItemService {

    private static final Logger log = LoggerFactory.getLogger(AdminItemServiceImpl.class);
    private final CloudinaryService cloudinaryService;
    private final ItemService itemService;
    private final CollectionService collectionService;
    private final GenderService genderService;
    private final OrderItemAuditService orderItemAuditService;

    @Autowired
    public AdminItemServiceImpl(@Qualifier("cloudinaryServiceImpl") CloudinaryService cloudinaryService,
                                @Qualifier("itemServiceImpl") ItemServiceImpl itemService,
                                @Qualifier("collectionServiceImpl") CollectionServiceImpl collectionService,
                                @Qualifier("genderServiceImpl") GenderServiceImpl genderService,
                                @Qualifier("orderItemAuditServiceImpl") OrderItemAuditServiceImpl orderItemAuditService) {
        this.cloudinaryService = cloudinaryService;
        this.itemService = itemService;
        this.collectionService = collectionService;
        this.genderService = genderService;
        this.orderItemAuditService = orderItemAuditService;
    }


    @Override
    @Transactional
    public void deleteItem(int id){
        Items item = itemService.findItemById(id);
    }

    @Override
    @Transactional
    public void saveItem(ListItemDTO item, MultipartFile file) throws Exception {
        Map<?,?> imageMap = uploadImage(file);
        String imageUrl = (String) imageMap.get("secure_url");
        String imageId = (String) imageMap.get("public_id");
        Items newItem = ListItemDTO.ItemsMapper(item, imageUrl,imageId);
        setCollectionIfExists(newItem, item.getCollection());
        setGenderIfValid(newItem, item.getGender());
        itemService.saveItem(newItem);
    }

    @Override
    @Transactional
    public void updateItemByAdmin(ListItemDTO dto,MultipartFile file,int integerCode) throws IOException,Exception{
        Items items = itemService.findItemById(integerCode);
        Map<?,?> uploadedMap = uploadImage(file);
        String uploadedUrl = (String) uploadedMap.get("secure_url");
        String uploadedId = (String) uploadedMap.get("public_id");
        String oldId = items.getImageId();
        Items updatedItem = updateItem(items,dto,uploadedUrl,uploadedId);
        itemService.saveItem(updatedItem);

        cleanupOldImageSafely(oldId);
    }

    @Override
    @Transactional
    public AdminReadItemsDTO readDataForAdmin(int id){
        Items item = itemService.findItemById(id);
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

    @Override
    @Transactional
    public void createNewCollection(String name, String description){
        collectionService.saveCollection(name,description);
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
           return cloudinaryService.uploadImage(file);
        } catch (RuntimeException ex) {
            log.error("Image uploadImage failed: {}", ex.getMessage());
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

    private void cleanupOldImageSafely(String oldImageId) {
        if (oldImageId != null) {
            try {
                cloudinaryService.deleteImageFromCloudinary(oldImageId);
                log.info("Successfully deleted old image: {}", oldImageId);
            } catch (Exception ex) {
                log.warn("Failed to delete old image {}: {}. Database update succeeded.", oldImageId, ex.getMessage());
            }
        }
    }
}
