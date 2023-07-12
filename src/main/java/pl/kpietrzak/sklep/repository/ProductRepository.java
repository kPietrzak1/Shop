package pl.kpietrzak.sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kpietrzak.sklep.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

