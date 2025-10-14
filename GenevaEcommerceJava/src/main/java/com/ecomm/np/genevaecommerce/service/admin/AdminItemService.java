package com.ecomm.np.genevaecommerce.service.admin;

import com.ecomm.np.genevaecommerce.model.dto.AdminReadItemsDTO;
import com.ecomm.np.genevaecommerce.model.dto.ListItemDTO;
import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : Asnit Bakhati
 */


public interface AdminItemService {

    @Transactional
    void deleteItem(int id);

    @Transactional
    void saveItem(ListItemDTO item, MultipartFile file) throws Exception;

    @Transactional
    void updateItemByAdmin(ListItemDTO dto, MultipartFile file, int integerCode) throws IOException,Exception;

    @Transactional
    AdminReadItemsDTO readDataForAdmin(int id);

    @Transactional
    void createNewCollection(String name, String description);
}
