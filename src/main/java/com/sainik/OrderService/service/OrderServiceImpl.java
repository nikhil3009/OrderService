package com.sainik.OrderService.service;

import com.sainik.OrderService.entity.Order;
import com.sainik.OrderService.external.client.PaymentService;
import com.sainik.OrderService.external.client.ProductService;
import com.sainik.OrderService.external.client.Request.PaymentRequest;
import com.sainik.OrderService.external.client.Request.ProductResponse;
import com.sainik.OrderService.model.OrderRequest;
import com.sainik.OrderService.model.OrderResponse;
import com.sainik.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public long placeOrder(OrderRequest orderRequest) {
        log.info("placing order:",orderRequest);
        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());
        Order order = Order.builder().amount(orderRequest.getTotalAmount()).orderStatus("CREATED").productId(orderRequest.getProductId())
                .orderDate(Instant.now()).quantity(orderRequest.getQuantity()).build();

        order = orderRepository.save(order);
log.info("calling payment service to do payment");

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();
        String orderStatus = null;
        try{
            paymentService.doPayment(paymentRequest);
            log.info("Payment is successfull,order status changes to successfull");
            orderStatus = "PLACED";

        }
        catch(Exception e){
            log.info("Payment is unsuccessfull,order status changes to failed");
            orderStatus = "FAILED";

        }
    order.setOrderStatus(orderStatus);
        log.info("order created");
        orderRepository.save(order);
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()->new RuntimeException("failed to retrieve order"));

        ProductResponse productResponse = restTemplate.getForObject("http://PRODUCT-SERVICE/product/" + order.getProductId(),
                ProductResponse.class);

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder().
                productName(productResponse.getProductName()).productId(productResponse.getProductId())
                .price(productResponse.getPrice()).quantity(productResponse.getQuantity()).
                build();


        OrderResponse orderResponse = OrderResponse.builder().orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .amount(order.getAmount()).productDetails(productDetails).build();
        return orderResponse;
    }
}
