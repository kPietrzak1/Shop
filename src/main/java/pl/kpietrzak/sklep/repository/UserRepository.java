package pl.kpietrzak.sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kpietrzak.sklep.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameIgnoreCase(String username);
}

