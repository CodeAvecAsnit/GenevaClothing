package com.ecomm.np.genevaecommerce.service;

import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.GenderTable;
import com.ecomm.np.genevaecommerce.model.Items;
import com.ecomm.np.genevaecommerce.repository.ItemsRepository;
import com.ecomm.np.genevaecommerce.serviceimpl.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemsRepository itemsRepository;

    @Autowired
    public ItemServiceImpl(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    public Items findItemById(int id){
        return itemsRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("The requested item was not found."));
    }

    public void saveItem(Items item){
        itemsRepository.save(item);
    }

    public Page<Items> findGenderItems(GenderTable genderTable, Pageable pageable) {
        return itemsRepository.findByGenderTable(genderTable, pageable);
    }

    public Page<Items> findAllTheItems(Pageable page){
        return itemsRepository.findAll(page);
    }

    public List<Items> findTop10(){
        return itemsRepository.findTop10ByOrderByCreatedDateDesc();
    }

    public Long findTotalItemCount() {
        return itemsRepository.findTotalItems();
    }

    public List<Items> findAllByListOfIds(List<Integer> ids) {
        return itemsRepository.findAllById(ids);
    }
}

