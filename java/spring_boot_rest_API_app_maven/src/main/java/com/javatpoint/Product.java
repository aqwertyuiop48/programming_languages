package com.javatpoint;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // Auto-increment ID
    private int id;

    @NotNull
    @Column(name = "product_name", nullable = false)       // Not null column
    private String pname;

    @Column(name = "batch_no")
    private String batchno;

    private double price;

    @Column(name = "quantity")
    private int noofproduct;

    // Default constructor (required by JPA)
    public Product() {
    }

    // Constructor using fields (id removed because it auto-generates)
    public Product(String pname, String batchno, double price, int noofproduct) {
        this.pname = pname;
        this.batchno = batchno;
        this.price = price;
        this.noofproduct = noofproduct;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getPname() {
        return pname;
    }
    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getBatchno() {
        return batchno;
    }
    public void setBatchno(String batchno) {
        this.batchno = batchno;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public int getNoofproduct() {
        return noofproduct;
    }
    public void setNoofproduct(int noofproduct) {
        this.noofproduct = noofproduct;
    }
}
