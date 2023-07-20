package pl.kpietrzak.sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kpietrzak.sklep.model.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
