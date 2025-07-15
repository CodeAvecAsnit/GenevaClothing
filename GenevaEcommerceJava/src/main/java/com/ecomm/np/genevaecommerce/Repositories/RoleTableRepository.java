package com.ecomm.np.genevaecommerce.Repositories;

import com.ecomm.np.genevaecommerce.Enumerations.Role;
import com.ecomm.np.genevaecommerce.Models.RoleTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleTableRepository extends JpaRepository<RoleTable,Integer> {
    Optional<RoleTable> findByRole(Role role);
}
