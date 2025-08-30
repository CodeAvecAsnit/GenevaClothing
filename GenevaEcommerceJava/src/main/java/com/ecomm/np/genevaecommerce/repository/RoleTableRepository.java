package com.ecomm.np.genevaecommerce.repository;

import com.ecomm.np.genevaecommerce.enumeration.Role;
import com.ecomm.np.genevaecommerce.model.RoleTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleTableRepository extends JpaRepository<RoleTable,Integer> {
    RoleTable findByRole(Role role);
}
