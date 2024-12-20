package com.example.project_a.model;
import jakarta.persistence.*;
@Entity
@Table(name ="categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private Integer ID;

    @Column
    private String CategoryName;

    @Column
    private String Status;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getCategoryname() {
        return CategoryName;
    }

    public void setCategoryname(String categoryName) {
        CategoryName = categoryName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
