package pl.kpietrzak.sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kpietrzak.sklep.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryIdIn(List<Long> categoryIds);

}

