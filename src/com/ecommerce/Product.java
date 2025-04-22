package com.ecommerce;

public class Product {
    private int productID;
    private String name;
    private double price;
    private String description;
    private int stockQuantity;

    // Constructors
    public Product(int productID, String name, double price, String description, int stockQuantity) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price >= 0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity >= 0) {
            this.stockQuantity = stockQuantity;
        } else {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
    }

    // Method to check if product is in stock
    public boolean isInStock() {
        return stockQuantity > 0;
    }

    // Method to decrease stock when product is purchased
    public boolean decreaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        if (stockQuantity >= quantity) {
            stockQuantity -= quantity;
            return true;
        }
        return false;
    }

    // Method to increase stock when products are restocked
    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        stockQuantity += quantity;
    }

    @Override
    public String toString() {
        return "Product ID: " + productID +
                ", Name: " + name +
                ", Price: $" + String.format("%.2f", price) +
                ", Description: " + description +
                ", Stock Quantity: " + stockQuantity;
    }
}