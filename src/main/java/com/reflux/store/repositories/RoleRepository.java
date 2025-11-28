package com.reflux.store.repositories;
import com.reflux.store.enums.UserRoleEnum;
import com.reflux.store.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>  {
    Optional<Role> findRoleByName(UserRoleEnum name);
}
