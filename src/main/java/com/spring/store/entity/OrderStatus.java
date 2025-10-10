package com.spring.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orderStatus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private OrderStatusCode code;

    @Column(nullable = false, length = 255)
    private String description;
}
