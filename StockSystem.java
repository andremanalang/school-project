import java.io.*;
import java.util.*;

abstract class Record implements Serializable {
    protected int code;
    protected String title;

    public Record(int code, String title) {
        this.code = code;
        this.title = title;
    }

    public int getCode() {
        return code;
    }

    public abstract String toString();
}

class StockItem extends Record {
    private int quantity;
    private double price;

    public StockItem(int code, String title, int quantity, double price) {
        super(code, title);
        this.quantity = quantity;
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toString() {
        return "Code: " + code +
               ", Name: " + title +
               ", Quantity: " + quantity +
               ", Price: " + price;
    }
}

public class StockSystem {

    static final String DATA_FILE = "StockData.bin";
    static ArrayList<StockItem> items = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadData();

        while (true) {
            System.out.println("\n=== STOCK SYSTEM ===");
            System.out.println("1. Add Item");
            System.out.println("2. View Items");
            System.out.println("3. Update Item");
            System.out.println("4. Exit");
            System.out.print("Choose: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addItem();
                case 2 -> viewItems();
                case 3 -> updateItem();
                case 4 -> {
                    saveData();
                    System.out.println("\nGoodbye!");
                    return;
                }
                default -> System.out.println("\nInvalid choice!");
            }
        }
    }

    static void addItem() {
        System.out.print("Enter Code: ");
        int code = sc.nextInt();
        sc.nextLine();

        for (StockItem s : items) {
            if (s.getCode() == code) {
                System.out.println("\nItem code already exists!");
                return;
            }
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        items.add(new StockItem(code, name, qty, price));
        saveData();
        System.out.println("\nItem added successfully!");
    }

    static void viewItems() {
        if (items.isEmpty()) {
            System.out.println("\nNo items in stock.");
            return;
        }

        System.out.println("\n=== ALL ITEMS ===");
        for (StockItem s : items) {
            System.out.println(s);
        }
    }

    static void updateItem() {
        System.out.print("Enter Item Code to update: ");
        int code = sc.nextInt();
        sc.nextLine();

        for (StockItem s : items) {
            if (s.getCode() == code) {

                System.out.print("New Name: ");
                s.setTitle(sc.nextLine());

                System.out.print("New Quantity: ");
                s.setQuantity(sc.nextInt());
                sc.nextLine();

                System.out.print("New Price: ");
                s.setPrice(sc.nextDouble());
                sc.nextLine();

                saveData();
                System.out.println("Item updated!");
                return;
            }
        }

        System.out.println("Item not found.");
    }

    static void saveData() {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(items);
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    static void loadData() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            items = (ArrayList<StockItem>) in.readObject();
        } catch (Exception e) {
            items = new ArrayList<>();
        }
    }
}