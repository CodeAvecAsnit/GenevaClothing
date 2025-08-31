package com.ecomm.np.genevaecommerce.service.modelservice;

import com.ecomm.np.genevaecommerce.model.enumeration.Gender;
import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.entity.GenderTable;
import com.ecomm.np.genevaecommerce.repository.GenderTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenderService implements IGenderService{

    private final GenderTableRepository genderTableRepository;

    @Autowired
    public GenderService(GenderTableRepository genderTableRepository) {
        this.genderTableRepository = genderTableRepository;
    }

    @Override
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
