package com.sainik.OrderService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "Order_Details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="productId")
    private long productId;
    @Column(name="QUANTITY")
    private long quantity;
    @Column(name="ORDER_DATE")
    private Instant orderDate;
    @Column(name="ORDER_STATUS")
    private String orderStatus;
    @Column(name="AMOUNT")
    private long amount;

}
