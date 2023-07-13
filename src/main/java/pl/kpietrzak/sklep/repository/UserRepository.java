package pl.kpietrzak.sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kpietrzak.sklep.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameIgnoreCase(String username);
}

