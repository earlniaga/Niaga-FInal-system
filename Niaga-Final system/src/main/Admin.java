1package main;

import config.config;
import java.util.Map;
import java.util.Scanner;

public class Admin {
    Scanner sc = new Scanner(System.in);
    config db = new config();

    /* ===========================
          VIEW FUNCTIONS
       =========================== */

    private void viewUsers() {
        String query = "SELECT * FROM tbl_user";
        String[] headers = {"ID", "Name", "Email", "Type", "Status"};
        String[] cols = {"u_id", "u_name", "u_email", "u_type", "u_status"};
        db.viewRecords(query, headers, cols);
    }

    private void viewProperties() {     
        String query = "SELECT * FROM properties";
        String[] headers = {"Property ID", "Address", "Type", "Price"};
        String[] cols = {"property_id", "address", "type", "price"};
        db.viewRecords(query, headers, cols);
    }

    private void viewTransactions() {
        String query = "SELECT * FROM transactions";
        String[] headers = {"Transaction ID", "Property ID", "Agent ID", "Date"};
        String[] cols = {"transaction_id", "property_id", "agent_id", "transaction_date"};
        db.viewRecords(query, headers, cols);
    }

    /* ===========================
          PROPERTY MANAGEMENT
       =========================== */

    private void addProperty() {
        System.out.print("Enter Address: ");
        String address = sc.nextLine();

        System.out.print("Enter Property Type (House/Lot/Condo): ");
        String type = sc.nextLine();

        System.out.print("Enter Price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        String sql = "INSERT INTO properties (address, type, price) VALUES (?, ?, ?)";
        db.addRecord(sql, address, type, price);

        System.out.println("✅ Property Added!");
    }

    private void updateProperty() {
        viewProperties();

        System.out.print("Enter Property ID to Update: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter New Address: ");
        String address = sc.nextLine();

        System.out.print("Enter New Type: ");
        String type = sc.nextLine();

        System.out.print("Enter New Price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        String sql = "UPDATE properties SET address=?, type=?, price=? WHERE property_id=?";
        db.updateRecord(sql, address, type, price, id);

        System.out.println("✅ Property Updated!");
    }

    private void deleteProperty() {
        viewProperties();

        System.out.print("Enter Property ID to Delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        String sql = "DELETE FROM properties WHERE property_id=?";
        db.deleteRecord(sql, id);

        System.out.println("🗑 Property Deleted!");
    }

    /* ===========================
          TRANSACTION MANAGEMENT
       =========================== */

    private void addTransaction() {
        System.out.println("\n=== ADD NEW TRANSACTION ===");
        
        viewProperties();
        System.out.print("Enter Property ID: ");
        int propID = sc.nextInt();

        viewUsers();
        System.out.print("Enter Agent ID: ");
        int agentID = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Transaction Date (YYYY-MM-DD): ");
        String date = sc.nextLine();

        String sql = "INSERT INTO transactions (property_id, agent_id, transaction_date) VALUES (?, ?, ?)";
        db.addRecord(sql, propID, agentID, date);

        System.out.println("✅ Transaction Recorded!");
    }

    private void deleteTransaction() {
        viewTransactions();

        System.out.print("Enter Transaction ID to Delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        String sql = "DELETE FROM transactions WHERE transaction_id=?";
        db.deleteRecord(sql, id);

        System.out.println("🗑 Transaction Deleted!");
    }

    /* ===========================
          ADMIN DASHBOARD
       =========================== */

    public void AdminDashboard(Map<String, Object> user) {
        char again;

        do {
            System.out.println("\n===== ADMIN DASHBOARD =====");
            System.out.println("Welcome Admin " + user.get("u_name"));
            System.out.println("[1] Approve Accounts");
            System.out.println("[2] View All Users");
            System.out.println("[3] Property Management");
            System.out.println("[4] Transaction Management");
            System.out.println("[5] Logout");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\n=== APPROVE PENDING ACCOUNTS ===");
                    String qry = "SELECT * FROM tbl_user WHERE u_status = 'Pending'";
                    String[] headers1 = {"ID", "Name", "Email", "Type", "Status"};
                    String[] cols1 = {"u_id", "u_name", "u_email", "u_type", "u_status"};
                    db.viewRecords(qry, headers1, cols1);

                    System.out.print("Enter User ID to Approve: ");
                    int id = sc.nextInt();
                    String sql = "UPDATE tbl_user SET u_status = ? WHERE u_id = ?";
                    db.updateRecord(sql, "Approved", id);
                    System.out.println("✅ User Approved!");
                    break;

                case 2:
                    System.out.println("\n=== ALL USER RECORDS ===");
                    viewUsers();
                    break;

                case 3:
                    propertyMenu();
                    break;

                case 4:
                    transactionMenu();
                    break;

                case 5:
                    System.out.println("🔙 Logging out...");
                    return;

                default:
                    System.out.println("❌ Invalid option. Try again.");
            }

            System.out.print("\nDo you want to continue ADMIN DASHBOARD? (Y/N): ");
            again = sc.next().charAt(0);
            sc.nextLine();

        } while (again == 'Y' || again == 'y');

        System.out.println("👋 Exiting Admin Dashboard... Goodbye!");
    }

    /* ===========================
          SUB-MENU: PROPERTY
       =========================== */

    private void propertyMenu() {
        System.out.println("\n=== PROPERTY MANAGEMENT ===");
        System.out.println("[1] View Properties");
        System.out.println("[2] Add Property");
        System.out.println("[3] Update Property");
        System.out.println("[4] Delete Property");
        System.out.print("Choose: ");
        int c = sc.nextInt();
        sc.nextLine();

        switch (c) {
            case 1: viewProperties(); break;
            case 4: deleteProperty(); break;
            default: System.out.println("Invalid");
        }
    }

    /* ===========================
          SUB-MENU: TRANSACTION
       =========================== */

    private void transactionMenu() {
        System.out.println("\n=== TRANSACTION MANAGEMENT ===");
        System.out.println("[1] View Transactions");
        System.out.println("[2] Add Transaction");
        System.out.println("[3] Delete Transaction");
        System.out.print("Choose: ");
        int c = sc.nextInt();
        sc.nextLine();

        switch (c) {
            case 1: viewTransactions(); break;
            case 2: addTransaction(); break;
            case 3: deleteTransaction(); break;
            default: System.out.println("Invalid");
        }
    }
}
