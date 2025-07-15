package com.ecomm.np.genevaecommerce.Services;

import com.ecomm.np.genevaecommerce.DTO.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.DTO.ListItemDTO;
import com.ecomm.np.genevaecommerce.DTO.NewCollectionDTO;
import com.ecomm.np.genevaecommerce.Enumerations.Gender;
import com.ecomm.np.genevaecommerce.Models.Collection;
import com.ecomm.np.genevaecommerce.Models.GenderTable;
import com.ecomm.np.genevaecommerce.Models.Items;
import com.ecomm.np.genevaecommerce.Repositories.CollectionRepository;
import com.ecomm.np.genevaecommerce.Repositories.GenderTableRepository;
import com.ecomm.np.genevaecommerce.Repositories.ItemsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemsService {
    private Logger logger = LoggerFactory.getLogger(ItemsService.class);

    private final ItemsRepository itemsRepository;

    private final GenderTableRepository genderTableRepository;

    private final CollectionRepository collectionRepository;


    @Autowired
    public ItemsService(ItemsRepository itemsRepository,
                        GenderTableRepository genderTableRepository,
                        CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
        this.genderTableRepository = genderTableRepository;
        this.itemsRepository = itemsRepository;
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


}
