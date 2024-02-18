package es.codeurjc.holamundo.repository;

import es.codeurjc.holamundo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
