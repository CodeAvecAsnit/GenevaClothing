package com.ecomm.np.genevaecommerce.service.modelservice;

import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.GenderTable;
import com.ecomm.np.genevaecommerce.model.Items;
import com.ecomm.np.genevaecommerce.repository.ItemsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService implements IItemService {
    private final ItemsRepository itemsRepository;

    @Autowired
    public ItemService(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    @Transactional
    public Items findItemById(int id){
        return itemsRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("The requested item was not found."));
    }

    @Override
    @Transactional
    public void saveItem(Items item){
        itemsRepository.save(item);
    }

    @Override
    @Transactional
    public Page<Items> findGenderItems(GenderTable genderTable, Pageable pageable) {
        return itemsRepository.findByGenderTable(genderTable, pageable);
    }

    @Override
    @Transactional
    public Page<Items> findAllTheItems(Pageable page){
        return itemsRepository.findAll(page);
    }

    @Override
    @Transactional
    public List<Items> findTop10(){
        return itemsRepository.findTop10ByOrderByCreatedDateDesc();
    }

    @Override
    @Transactional
    public Long findTotalItemCount() {
        return itemsRepository.findTotalItems();
    }

    @Override
    @Transactional
    public List<Items> findAllByListOfIds(List<Integer> ids) {
        return itemsRepository.findAllById(ids);
    }
}

