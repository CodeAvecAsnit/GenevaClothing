package com.ecomm.np.genevaecommerce.repository;

import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author : Asnit Bakhati
 */

@Repository
public interface UserRepository extends JpaRepository<UserModel,Integer> {
    Optional<UserModel> findByEmail(String email);
    UserModel findByUserName(String username);
    @Query(value = "SELECT COUNT(*) FROM cartlist WHERE user_id = :userId", nativeQuery = true)
    int countItemsInCart(@Param("userId") int userId);
}
