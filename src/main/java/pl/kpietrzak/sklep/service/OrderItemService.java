package pl.kpietrzak.sklep.service;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import pl.kpietrzak.sklep.model.Order;
import pl.kpietrzak.sklep.model.OrderItem;
import pl.kpietrzak.sklep.model.Product;
import pl.kpietrzak.sklep.repository.OrderItemRepository;

import java.util.List;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderService orderService;
    private final ProductService productService;

    public OrderItemService(OrderItemRepository orderItemRepository, OrderService orderService, ProductService productService) {
        this.orderItemRepository = orderItemRepository;
        this.orderService = orderService;
        this.productService = productService;
    }

    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public OrderItem addProductToOrder(Long orderId, Long productId, Integer quantity) {
        Order order = orderService.getOrderById(orderId);
        Product product = productService.getProductById(productId);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);

        return orderItemRepository.save(orderItem);
    }

    public void deleteOrderItem(Long orderItemId) {
        if (!orderItemRepository.existsById(orderItemId)) {
            throw new ResourceNotFoundException("Order item not found with id: " + orderItemId);
        }
        orderItemRepository.deleteById(orderItemId);
    }
}

