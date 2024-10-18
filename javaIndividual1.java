
import java.io.*;
import java.util.*;

class MenuItem implements Serializable {
    private String name;
    private double price;

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Item: " + name + ", Price: $" + price;
    }
}

class Order {
    private int orderNumber;
    private String customerName;
    private LinkedList<MenuItem> items;

    public Order(int orderNumber, String customerName) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.items = new LinkedList<>();
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public double getTotalPrice() {
        double total = 0;
        for (MenuItem item : items) {
            total += item.getPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Order #" + orderNumber + " for " + customerName + " with total price: $" + getTotalPrice();
    }
}

 class RestaurantManagementSystem {
    private LinkedList<MenuItem> menu;
    private LinkedList<Order> orders;
    private static final String MENU_FILE = "menu.dat";
    private static final String ORDERS_FILE = "orders.dat";

    public RestaurantManagementSystem() {
        menu = new LinkedList<>();
        orders = new LinkedList<>();
    }

    public void addItemToMenu(MenuItem item) {
        menu.add(item);
    }

    public void displayMenu() {
        System.out.println("\nMenu:");
        for (MenuItem item : menu) {
            System.out.println(item);
        }
    }

    public void placeOrder(Order order) {
        orders.add(order);
        System.out.println("Order placed: " + order);
    }

    public void listOrders() {
        System.out.println("\nOrders:");
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    public static void saveToFile(LinkedList<?> data, String filename) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(data);
            System.out.println("Data saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static LinkedList<?> loadFromFile(String filename) {
        LinkedList<?> data = new LinkedList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            data = (LinkedList<?>) inputStream.readObject();
            System.out.println("Data loaded from " + filename);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
        return data;
    }

    public static void main(String[] args) {
        RestaurantManagementSystem restaurant = new RestaurantManagementSystem();
        Scanner scanner = new Scanner(System.in);

        // Load menu and orders from files or initialize them
        LinkedList<MenuItem> menu = (LinkedList<MenuItem>) loadFromFile(MENU_FILE);
        LinkedList<Order> orders = (LinkedList<Order>) loadFromFile(ORDERS_FILE);

        if (menu.isEmpty()) {
            // Initialize the menu with some items
            menu.add(new MenuItem("Burger", 5.99));
            menu.add(new MenuItem("Pizza", 7.99));
            menu.add(new MenuItem("Salad", 4.99));
            menu.add(new MenuItem("Soda", 1.99));
        }

        if (orders.isEmpty()) {
            // Initialize with some sample orders
            Order sampleOrder1 = new Order(1, "John");
            sampleOrder1.addItem(menu.get(0));
            sampleOrder1.addItem(menu.get(3));

            Order sampleOrder2 = new Order(2, "Alice");
            sampleOrder2.addItem(menu.get(1));
            sampleOrder2.addItem(menu.get(2));

            orders.add(sampleOrder1);
            orders.add(sampleOrder2);
        }

        restaurant.menu = menu;
        restaurant.orders = orders;

        while (true) {
            System.out.println("\nRestaurant Management System Menu:");
            System.out.println("1. Display Menu");
            System.out.println("2. Place Order");
            System.out.println("3. List Orders");
            System.out.println("4. Save Data");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        restaurant.displayMenu();
                        break;
                    case 2:
                        System.out.print("Enter customer name: ");
                        String customerName = scanner.nextLine();
                        Order newOrder = new Order(orders.size() + 1, customerName);

                        while (true) {
                            restaurant.displayMenu();
                            System.out.print("Enter item name to add to the order (or type 'done' to finish): ");
                            String itemName = scanner.nextLine();

                            if (itemName.equalsIgnoreCase("done")) {
                                break;
                            }

                            boolean found = false;
                            for (MenuItem item : menu) {
                                if (item.getName().equalsIgnoreCase(itemName)) {
                                    newOrder.addItem(item);
                                    found = true;
                                    break;
                                }
                            }

                            if (!found) {
                                System.out.println("Item not found in the menu.");
                            }
                        }

                        restaurant.placeOrder(newOrder);
                        break;
                    case 3:
                        restaurant.listOrders();
                        break;
                    case 4:
                        saveToFile(restaurant.menu, MENU_FILE);
                        saveToFile(restaurant.orders, ORDERS_FILE);
                        break;
                    case 5:
                        System.out.println("Exiting Restaurant Management System. Have a great day!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a valid choice.");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }
}
