package com.example.project_a.model;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name ="carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    public BigDecimal getTotal() {
        return BigDecimal.valueOf(quantity)
                .multiply(BigDecimal.valueOf(this.product.getPrice()))
                .setScale(2, RoundingMode.HALF_UP); // Ensures 2 decimal places
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer ID) {
        this.id = ID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        if (this.product != null) {
            if (product.getId() == this.product.getId()) {
                this.product = product;
                this.quantity++;
            }else {
                this.product = product;
                this.quantity = 1;
            }
        }else {
            this.product = product;
            this.quantity = 1;
        }

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

