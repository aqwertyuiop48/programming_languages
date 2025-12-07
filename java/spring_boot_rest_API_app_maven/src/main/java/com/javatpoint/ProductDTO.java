package com.javatpoint;

public class ProductDTO {

    private int id;           // Auto-generated in DB
    private String name;      // Not null
    private String batch;
    private double price;
    private int quantity;

    // ─────────────── DEFAULT CONSTRUCTOR ───────────────
    public ProductDTO() {}

    // ─────────────── CONSTRUCTOR WITH FIELDS ───────────────
    public ProductDTO(String name, String batch, double price, int quantity) {
        this.name = name;
        this.batch = batch;
        this.price = price;
        this.quantity = quantity;
    }

    public ProductDTO(int id, String name, String batch, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.batch = batch;
        this.price = price;
        this.quantity = quantity;
    }

    // ─────────────── GETTERS ───────────────
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBatch() {
        return batch;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // ─────────────── SETTERS ───────────────
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
