package pl.kpietrzak.sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kpietrzak.sklep.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}

