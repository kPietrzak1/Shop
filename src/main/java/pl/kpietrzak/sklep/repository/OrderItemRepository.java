package pl.kpietrzak.sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kpietrzak.sklep.model.OrderItem;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
}

