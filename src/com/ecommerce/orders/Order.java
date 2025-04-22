package com.ecommerce.orders;

import com.ecommerce.Customer;
import com.ecommerce.Product;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private int orderID;
    private Customer customer;
    private Map<Product, Integer> orderedProducts;
    private double orderTotal;
    private LocalDateTime orderDate;
    private OrderStatus status;

    // Enum for order status
    public enum OrderStatus {
        PENDING,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }

    // Constructors
    public Order(int orderID, Customer customer, Map<Product, Integer> orderedProducts, double orderTotal) {
        this.orderID = orderID;
        this.customer = customer;
        this.orderedProducts = new HashMap<>(orderedProducts);
        this.orderTotal = orderTotal;
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }

    // Getters and Setters
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Map<Product, Integer> getOrderedProducts() {
        return new HashMap<>(orderedProducts); // Return a copy to prevent direct modification
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
        System.out.println("Order #" + orderID + " status updated to: " + status);
    }

    // Order management methods
    public void cancelOrder() {
        if (status == OrderStatus.PENDING || status == OrderStatus.PROCESSING) {
            status = OrderStatus.CANCELLED;

            // Return products to inventory
            for (Map.Entry<Product, Integer> entry : orderedProducts.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                product.increaseStock(quantity);
            }

            System.out.println("Order #" + orderID + " has been cancelled");
        } else {
            throw new IllegalStateException("Cannot cancel order. Current status: " + status);
        }
    }

    public void processOrder() {
        if (status == OrderStatus.PENDING) {
            status = OrderStatus.PROCESSING;
            System.out.println("Order #" + orderID + " is now being processed");
        } else {
            throw new IllegalStateException("Cannot process order. Current status: " + status);
        }
    }

    public void shipOrder() {
        if (status == OrderStatus.PROCESSING) {
            status = OrderStatus.SHIPPED;
            System.out.println("Order #" + orderID + " has been shipped");
        } else {
            throw new IllegalStateException("Cannot ship order. Current status: " + status);
        }
    }

    public void deliverOrder() {
        if (status == OrderStatus.SHIPPED) {
            status = OrderStatus.DELIVERED;
            System.out.println("Order #" + orderID + " has been delivered");
        } else {
            throw new IllegalStateException("Cannot mark order as delivered. Current status: " + status);
        }
    }

    // Generate order summary
    public void generateOrderSummary() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("\n========== ORDER SUMMARY ==========");
        System.out.println("Order ID: " + orderID);
        System.out.println("Date: " + orderDate.format(formatter));
        System.out.println("Status: " + status);
        System.out.println("Customer: " + customer.getName() + " (ID: " + customer.getCustomerID() + ")");
        System.out.println("Shipping Address: " + customer.getAddress());

        System.out.println("\nORDERED ITEMS:");
        for (Map.Entry<Product, Integer> entry : orderedProducts.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            System.out.println(product.getName() + " - $" +
                    String.format("%.2f", product.getPrice()) +
                    " x " + quantity +
                    " = $" + String.format("%.2f", product.getPrice() * quantity));
        }

        System.out.println("\nOrder Total: $" + String.format("%.2f", orderTotal));
        System.out.println("====================================\n");
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Order #" + orderID +
                " | Date: " + orderDate.format(formatter) +
                " | Customer: " + customer.getName() +
                " | Status: " + status +
                " | Total: $" + String.format("%.2f", orderTotal);
    }
}