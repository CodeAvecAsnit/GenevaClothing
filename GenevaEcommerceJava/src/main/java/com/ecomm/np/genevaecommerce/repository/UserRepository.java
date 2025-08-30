package com.ecomm.np.genevaecommerce.repository;

import com.ecomm.np.genevaecommerce.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserModel,Integer> {
    UserModel findByEmail(String email);
    UserModel findByUserName(String username);
    @Query(value = "SELECT COUNT(*) FROM cartlist WHERE user_id = :userId", nativeQuery = true)
    int countItemsInCart(@Param("userId") int userId);
}
