package com.projects;

import java.sql.*;
import java.util.Scanner;

public class InventoryManagementSystem {

	
	   

	    public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);
	        try  (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/inventory_db", "root", "Chetan@147") ;
	             Statement stmt = conn.createStatement()) {
	            createProductTable(stmt);
 
	            while (true) {
	                System.out.println("Inventory Management System");
	                System.out.println("1. Add Products");
	                System.out.println("2. Sell Product");
	                System.out.println("3. View Inventory");
	                System.out.println("4. Update Product");
	                System.out.println("5. Delete Product");
	                System.out.println("6. Exit");
	                System.out.print("Select an option: ");

	                int choice = scanner.nextInt();

	                switch (choice) {
	                    case 1:
	                        addProduct(conn, scanner);
	                        break;
	                    case 2:
	                        sellProduct(conn, scanner);
	                        break;
	                    case 3:
	                        viewInventory(conn);
	                        break;
	                    case 4:
	                    	updateProduct(conn, scanner);
	                    case 5:
	                    	deleteProduct(conn, scanner);
	                    case 6:
	                        scanner.close();
	                        System.out.println("Goodbye!");
	                        System.exit(0);
	                    default:
	                        System.out.println("Invalid choice. Please select a valid option.");
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    private static void createProductTable(Statement stmt) throws SQLException {
	        String createTableSQL = "CREATE TABLE IF NOT EXISTS products (" +
	                "id INT AUTO_INCREMENT PRIMARY KEY," +
	                "name VARCHAR(255)," +
	                "price DOUBLE," +
	                "quantity INT)";
	        stmt.executeUpdate(createTableSQL);
	    }

	    private static void addProduct(Connection conn, Scanner scanner) throws SQLException {
	        scanner.nextLine(); // Consume the newline character
	        System.out.print("Enter product name: ");
	        String name = scanner.nextLine();
	        System.out.print("Enter product price: ");
	        double price = scanner.nextDouble();
	        System.out.print("Enter initial quantity: ");
	        int quantity = scanner.nextInt();

	        String insertSQL = "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)";
	        try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
	            preparedStatement.setString(1, name);
	            preparedStatement.setDouble(2, price);
	            preparedStatement.setInt(3, quantity);
	            preparedStatement.executeUpdate();
	        }
	        System.out.println("Product added to the inventory: " + name);
	    }

	    private static void sellProduct(Connection conn, Scanner scanner) throws SQLException {
	        viewInventory(conn);
	        System.out.print("Enter the ID of the product to sell: ");
	        int productId = scanner.nextInt();
	        System.out.print("Enter quantity to sell: ");
	        int quantityToSell = scanner.nextInt();

	        String updateSQL = "UPDATE products SET quantity = quantity - ? WHERE id = ?";
	        try (PreparedStatement preparedStatement = conn.prepareStatement(updateSQL)) {
	            preparedStatement.setInt(1, quantityToSell);
	            preparedStatement.setInt(2, productId);
	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("Sale complete.");
	            } else {
	                System.out.println("Product not found or insufficient quantity.");
	            }
	        }
	    }

	    private static void viewInventory(Connection conn) throws SQLException {
	        String selectSQL = "SELECT * FROM products";
	        try (Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(selectSQL)) {
	            System.out.println("Inventory:");
	            while (rs.next()) {
	                int id = rs.getInt("id");
	                String name = rs.getString("name");
	                double price = rs.getDouble("price");
	                int quantity = rs.getInt("quantity");
	                System.out.println(id + ". " + name + " - Price: " + price + " - Quantity: " + quantity);
	            }
	        }
	    }
	    
	    private static void updateProduct(Connection conn, Scanner scanner) throws SQLException {
	        viewInventory(conn);
	        System.out.print("Enter the ID of the product to update: ");
	        int productId = scanner.nextInt();
	        scanner.nextLine(); // Consume newline character

	        System.out.println("Select the attribute to update:");
	        System.out.println("1. Name");
	        System.out.println("2. Price");
	        System.out.println("3. Quantity");
	        System.out.print("Enter the option: ");
	        int attributeChoice = scanner.nextInt();
	        scanner.nextLine(); // Consume newline character

	        String updateSQL = "";
	        String updatedAttribute = "";
	        switch (attributeChoice) {
	            case 1:
	                System.out.print("Enter the new name: ");
	                updatedAttribute = scanner.nextLine();
	                updateSQL = "UPDATE products SET name = ? WHERE id = ?";
	                break;
	            case 2:
	                System.out.print("Enter the new price: ");
	                double newPrice = scanner.nextDouble();
	                updateSQL = "UPDATE products SET price = ? WHERE id = ?";
	                updatedAttribute = Double.toString(newPrice);
	                break;
	            case 3:
	                System.out.print("Enter the new quantity: ");
	                int newQuantity = scanner.nextInt();
	                updateSQL = "UPDATE products SET quantity = ? WHERE id = ?";
	                updatedAttribute = Integer.toString(newQuantity);
	                break;
	            default:
	                System.out.println("Invalid option.");
	                return;
	        }

	        try (PreparedStatement preparedStatement = conn.prepareStatement(updateSQL)) {
	            preparedStatement.setString(1, updatedAttribute);
	            preparedStatement.setInt(2, productId);
	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("Product information updated.");
	            } else {
	                System.out.println("Product not found.");
	            }
	        }
	    }
	    
	    private static void deleteProduct(Connection conn, Scanner scanner) throws SQLException {
	        viewInventory(conn);
	        System.out.print("Enter the ID of the product to delete: ");
	        int productId = scanner.nextInt();

	        String deleteSQL = "DELETE FROM products WHERE id = ?";
	        
	        try (PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL)) {
	            preparedStatement.setInt(1, productId);
	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("Product deleted from the inventory.");
	            } else {
	                System.out.println("Product not found.");
	            }
	        }
	    }


	}

