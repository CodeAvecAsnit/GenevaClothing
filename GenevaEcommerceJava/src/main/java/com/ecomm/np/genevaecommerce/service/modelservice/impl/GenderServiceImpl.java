package com.ecomm.np.genevaecommerce.service.modelservice.impl;

import com.ecomm.np.genevaecommerce.model.enumeration.Gender;
import com.ecomm.np.genevaecommerce.extra.exception.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.entity.GenderTable;
import com.ecomm.np.genevaecommerce.repository.GenderTableRepository;
import com.ecomm.np.genevaecommerce.service.modelservice.GenderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenderServiceImpl implements GenderService {

    private final GenderTableRepository genderTableRepository;

    @Autowired
    public GenderServiceImpl(GenderTableRepository genderTableRepository) {
        this.genderTableRepository = genderTableRepository;
    }

    @Override
    @Transactional
    public GenderTable getGenderTable(String genderString) {
        Gender gender = parseGender(genderString);
        return genderTableRepository.findByGender(gender)
                .orElseThrow(() -> new ResourceNotFoundException("Requested Gender was not found"));
    }

    private Gender parseGender(String genderString) {
        try {
            return Gender.valueOf(genderString.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return Gender.UNISEX;
        }
    }
}
