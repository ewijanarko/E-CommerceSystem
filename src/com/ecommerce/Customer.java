package com.ecommerce;

import com.ecommerce.orders.Order;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer {
    private int customerID;
    private String name;
    private String email;
    private String address;
    // Shopping cart stores product and quantity
    private Map<Product, Integer> shoppingCart;
    private List<Order> orderHistory;

    // Constructors
    public Customer(int customerID, String name, String email, String address) {
        this.customerID = customerID;
        this.name = name;
        this.email = email;
        this.address = address;
        this.shoppingCart = new HashMap<>();
        this.orderHistory = new ArrayList<>();
    }

    // Getters and Setters
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<Product, Integer> getShoppingCart() {
        return new HashMap<>(shoppingCart); // Return a copy to prevent direct modification
    }

    public List<Order> getOrderHistory() {
        return new ArrayList<>(orderHistory); // Return a copy to prevent direct modification
    }

    // Shopping cart methods
    public void addToCart(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (!product.isInStock() || product.getStockQuantity() < quantity) {
            throw new IllegalStateException("Product is out of stock or requested quantity exceeds available stock");
        }

        // Add product to cart or update quantity if already in cart
        shoppingCart.put(product, shoppingCart.getOrDefault(product, 0) + quantity);
        System.out.println(quantity + " " + product.getName() + "(s) added to cart");
    }

    public void removeFromCart(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        if (!shoppingCart.containsKey(product)) {
            throw new IllegalStateException("Product is not in the cart");
        }

        int currentQuantity = shoppingCart.get(product);
        if (quantity >= currentQuantity) {
            shoppingCart.remove(product);
            System.out.println(product.getName() + " removed from cart");
        } else {
            shoppingCart.put(product, currentQuantity - quantity);
            System.out.println(quantity + " " + product.getName() + "(s) removed from cart");
        }
    }

    public void clearCart() {
        shoppingCart.clear();
        System.out.println("Shopping cart cleared");
    }

    public double calculateCartTotal() {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    public void viewCart() {
        if (shoppingCart.isEmpty()) {
            System.out.println("Your shopping cart is empty");
            return;
        }

        System.out.println("\n===== SHOPPING CART =====");
        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            System.out.println(product.getName() + " - $" +
                    String.format("%.2f", product.getPrice()) +
                    " x " + quantity +
                    " = $" + String.format("%.2f", product.getPrice() * quantity));
        }
        System.out.println("-------------------------");
        System.out.println("Total: $" + String.format("%.2f", calculateCartTotal()));
        System.out.println("=========================\n");
    }

    // Order methods
    public Order placeOrder() {
        if (shoppingCart.isEmpty()) {
            throw new IllegalStateException("Cannot place an order with an empty cart");
        }

        // Check if all products have sufficient stock
        for (Map.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            if (!product.isInStock() || product.getStockQuantity() < quantity) {
                throw new IllegalStateException(
                        "Cannot place order: " + product.getName() +
                                " does not have sufficient stock (requested: " + quantity +
                                ", available: " + product.getStockQuantity() + ")");
            }
        }

        // Create a copy of the shopping cart for the order
        Map<Product, Integer> orderItems = new HashMap<>(shoppingCart);

        // Update product stock
        for (Map.Entry<Product, Integer> entry : orderItems.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            product.decreaseStock(quantity);
        }

        // Create and add the order to history
        Order order = new Order(orderHistory.size() + 1, this, orderItems, calculateCartTotal());
        orderHistory.add(order);

        // Clear the cart after order is placed
        clearCart();

        return order;
    }

    @Override
    public String toString() {
        return "Customer ID: " + customerID +
                ", Name: " + name +
                ", Email: " + email +
                ", Address: " + address;
    }
}