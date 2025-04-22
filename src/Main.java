import com.ecommerce.Product;
import com.ecommerce.Customer;
import com.ecommerce.orders.Order;
import com.ecommerce.orders.Order.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Create sample products
        List<Product> productCatalog = new ArrayList<>();
        productCatalog.add(new Product(1, "Laptop", 999.99, "High-performance laptop with 16GB RAM", 10));
        productCatalog.add(new Product(2, "Smartphone", 699.99, "Latest model with 128GB storage", 15));
        productCatalog.add(new Product(3, "Headphones", 149.99, "Wireless noise-cancelling headphones", 20));
        productCatalog.add(new Product(4, "Tablet", 349.99, "10-inch tablet with 64GB storage", 8));
        productCatalog.add(new Product(5, "Smartwatch", 199.99, "Fitness tracker with heart rate monitor", 12));

        // Create a customer
        Customer customer = new Customer(1, "John Doe", "john.doe@example.com", "123 Main St, Anytown, USA");

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n===== E-COMMERCE SYSTEM DEMO =====");
            System.out.println("1. Browse Products");
            System.out.println("2. Add Product to Cart");
            System.out.println("3. Remove Product from Cart");
            System.out.println("4. View Shopping Cart");
            System.out.println("5. Place Order");
            System.out.println("6. View Order History");
            System.out.println("7. Process Last Order (Change Status)");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    browseProducts(productCatalog);
                    break;
                case 2:
                    addToCart(customer, productCatalog, scanner);
                    break;
                case 3:
                    removeFromCart(customer, scanner);
                    break;
                case 4:
                    customer.viewCart();
                    break;
                case 5:
                    placeOrder(customer);
                    break;
                case 6:
                    viewOrderHistory(customer);
                    break;
                case 7:
                    processLastOrder(customer);
                    break;
                case 8:
                    exit = true;
                    System.out.println("Thank you for using our E-Commerce System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void browseProducts(List<Product> productCatalog) {
        System.out.println("\n===== PRODUCT CATALOG =====");
        for (Product product : productCatalog) {
            System.out.println(product);
        }
        System.out.println("==========================\n");
    }

    private static void addToCart(Customer customer, List<Product> productCatalog, Scanner scanner) {
        browseProducts(productCatalog);

        try {
            System.out.print("Enter Product ID to add to cart: ");
            int productId = Integer.parseInt(scanner.nextLine());

            Product selectedProduct = findProductById(productCatalog, productId);
            if (selectedProduct == null) {
                System.out.println("Product not found.");
                return;
            }

            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            customer.addToCart(selectedProduct, quantity);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void removeFromCart(Customer customer, Scanner scanner) {
        if (customer.getShoppingCart().isEmpty()) {
            System.out.println("Your shopping cart is empty.");
            return;
        }

        customer.viewCart();

        try {
            System.out.print("Enter Product ID to remove from cart: ");
            int productId = Integer.parseInt(scanner.nextLine());

            Product productToRemove = null;
            for (Product product : customer.getShoppingCart().keySet()) {
                if (product.getProductID() == productId) {
                    productToRemove = product;
                    break;
                }
            }

            if (productToRemove == null) {
                System.out.println("Product not found in cart.");
                return;
            }

            System.out.print("Enter quantity to remove (or 0 for all): ");
            int quantity = Integer.parseInt(scanner.nextLine());

            if (quantity == 0) {
                quantity = customer.getShoppingCart().get(productToRemove);
            }

            customer.removeFromCart(productToRemove, quantity);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void placeOrder(Customer customer) {
        if (customer.getShoppingCart().isEmpty()) {
            System.out.println("Your shopping cart is empty. Add some products before placing an order.");
            return;
        }

        try {
            Order order = customer.placeOrder();
            System.out.println("Order placed successfully!");
            order.generateOrderSummary();
        } catch (IllegalStateException e) {
            System.out.println("Error placing order: " + e.getMessage());
        }
    }

    private static void viewOrderHistory(Customer customer) {
        List<Order> orderHistory = customer.getOrderHistory();

        if (orderHistory.isEmpty()) {
            System.out.println("You have no order history.");
            return;
        }

        System.out.println("\n===== ORDER HISTORY =====");
        for (Order order : orderHistory) {
            System.out.println(order);
        }

        System.out.print("\nEnter Order ID to view details (or 0 to return): ");
        Scanner scanner = new Scanner(System.in);
        try {
            int orderId = Integer.parseInt(scanner.nextLine());
            if (orderId != 0) {
                Order selectedOrder = null;
                for (Order order : orderHistory) {
                    if (order.getOrderID() == orderId) {
                        selectedOrder = order;
                        break;
                    }
                }

                if (selectedOrder != null) {
                    selectedOrder.generateOrderSummary();
                } else {
                    System.out.println("Order not found.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private static void processLastOrder(Customer customer) {
        List<Order> orderHistory = customer.getOrderHistory();

        if (orderHistory.isEmpty()) {
            System.out.println("No orders to process.");
            return;
        }

        Order lastOrder = orderHistory.get(orderHistory.size() - 1);
        System.out.println("Last Order: " + lastOrder);

        System.out.println("\nCurrent Status: " + lastOrder.getStatus());
        System.out.println("1. Process Order (PENDING → PROCESSING)");
        System.out.println("2. Ship Order (PROCESSING → SHIPPED)");
        System.out.println("3. Deliver Order (SHIPPED → DELIVERED)");
        System.out.println("4. Cancel Order");
        System.out.print("Enter your choice: ");

        Scanner scanner = new Scanner(System.in);
        try {
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    try {
                        lastOrder.processOrder();
                    } catch (IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        lastOrder.shipOrder();
                    } catch (IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        lastOrder.deliverOrder();
                    } catch (IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        lastOrder.cancelOrder();
                    } catch (IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private static Product findProductById(List<Product> productCatalog, int productId) {
        for (Product product : productCatalog) {
            if (product.getProductID() == productId) {
                return product;
            }
        }
        return null;
    }
}