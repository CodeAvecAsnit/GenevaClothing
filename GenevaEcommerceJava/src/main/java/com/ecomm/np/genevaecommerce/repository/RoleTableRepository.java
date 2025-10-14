package com.ecomm.np.genevaecommerce.repository;

import com.ecomm.np.genevaecommerce.model.enumeration.Role;
import com.ecomm.np.genevaecommerce.model.entity.RoleTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : Asnit Bakhati
 */

@Repository
public interface RoleTableRepository extends JpaRepository<RoleTable,Integer> {
    RoleTable findByRole(Role role);
}
