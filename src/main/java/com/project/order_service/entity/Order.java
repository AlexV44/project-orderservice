package com.project.order_service.entity;

import com.project.order_service.entity.audit.BaseAuditEntity;
import com.project.order_service.entity.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import liquibase.license.User;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseAuditEntity {
    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @NotNull
    @Column(name = "user_id")
    private UUID userId;

    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private OrderStatus status = OrderStatus.PENDING;

    @NotNull
    @Column(name = "total_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPrice;

    @NotNull
    @Builder.Default
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @NotNull
    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }
}
