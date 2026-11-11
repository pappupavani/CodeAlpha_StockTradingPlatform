import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Stock {
    String name;
    double price;

    Stock(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

class Transaction {
    String type;
    String stockName;
    int quantity;
    double amount;
    String date;

    Transaction(String type, String stockName,
                int quantity, double amount) {
        this.type = type;
        this.stockName = stockName;
        this.quantity = quantity;
        this.amount = amount;

        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        this.date = LocalDateTime.now().format(formatter);
    }

    void display() {
        System.out.println(type + " | " + stockName
            + " | Quantity: " + quantity
            + " | Amount: Rs." + amount
            + " | " + date);
    }
}

public class Main {

    static Scanner sc = new Scanner(System.in);

    static Stock[] stocks = {
        new Stock("TCS", 3500),
        new Stock("INFOSYS", 1500),
        new Stock("WIPRO", 600),
        new Stock("RELIANCE", 2800)
    };

    static int[] holdings = new int[stocks.length];

    static ArrayList<Transaction> history =
        new ArrayList<>();

    static double balance = 100000;
    static final double INITIAL_BALANCE = 100000;

    public static void main(String[] args) {

        System.out.println("==============================");
        System.out.println("    STOCK TRADING PLATFORM");
        System.out.println("==============================");

        int choice;

        do {
            System.out.println("\n1. View Market Stocks");
            System.out.println("2. Buy Stocks");
            System.out.println("3. Sell Stocks");
            System.out.println("4. View Portfolio");
            System.out.println("5. Transaction History");
            System.out.println("6. Exit");

            choice = readNumber("Enter your choice: ");

            switch (choice) {

                case 1:
                    viewStocks();
                    break;

                case 2:
                    buyStocks();
                    break;

                case 3:
                    sellStocks();
                    break;

                case 4:
                    viewPortfolio();
                    break;

                case 5:
                    viewHistory();
                    break;

                case 6:
                    System.out.println(
                        "Thank you for using Stock Trading Platform!"
                    );
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 6);

        sc.close();
    }

    static int readNumber(String message) {
        while (true) {
            System.out.print(message);

            if (!sc.hasNextLine()) {
                return 6;
            }

            String input = sc.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    static void viewStocks() {

        System.out.println("\n----- MARKET STOCKS -----");

        for (int i = 0; i < stocks.length; i++) {
            System.out.println((i + 1) + ". "
                + stocks[i].name + " - Rs."
                + stocks[i].price);
        }
    }

    static void buyStocks() {

        viewStocks();

        int choice = readNumber("Select stock number: ");

        if (choice < 1 || choice > stocks.length) {
            System.out.println("Invalid stock selection.");
            return;
        }

        int quantity = readNumber("Enter quantity: ");

        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        Stock selected = stocks[choice - 1];

        double total = selected.price * quantity;

        if (total <= balance) {

            balance -= total;
            holdings[choice - 1] += quantity;

            history.add(new Transaction(
                "BUY", selected.name, quantity, total
            ));

            System.out.println("Stocks purchased successfully!");
            System.out.println("Total Amount: Rs." + total);
            System.out.println("Remaining Balance: Rs." + balance);

        } else {
            System.out.println("Insufficient balance!");
        }
    }

    static void sellStocks() {

        viewStocks();

        int choice = readNumber("Select stock number: ");

        if (choice < 1 || choice > stocks.length) {
            System.out.println("Invalid stock selection.");
            return;
        }

        int quantity = readNumber("Enter quantity to sell: ");

        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        int index = choice - 1;
        Stock selected = stocks[index];

        if (quantity <= holdings[index]) {

            double total = selected.price * quantity;

            holdings[index] -= quantity;
            balance += total;

            history.add(new Transaction(
                "SELL", selected.name, quantity, total
            ));

            System.out.println("Stocks sold successfully!");
            System.out.println("Amount received: Rs." + total);
            System.out.println("Current Balance: Rs." + balance);

        } else {
            System.out.println("Insufficient shares in portfolio!");
        }
    }

    static void viewPortfolio() {

        System.out.println("\n----- YOUR PORTFOLIO -----");

        double stockValue = 0;

        for (int i = 0; i < stocks.length; i++) {

            double value = holdings[i] * stocks[i].price;
            stockValue += value;

            System.out.println(stocks[i].name
                + " : " + holdings[i]
                + " shares | Value: Rs." + value);
        }

        double totalValue = balance + stockValue;
        double profitLoss = totalValue - INITIAL_BALANCE;

        System.out.println("--------------------------");
        System.out.println("Available Balance: Rs." + balance);
        System.out.println("Stock Value: Rs." + stockValue);
        System.out.println("Total Portfolio Value: Rs." + totalValue);
        System.out.println("Profit/Loss: Rs." + profitLoss);
    }

    static void viewHistory() {

        System.out.println("\n----- TRANSACTION HISTORY -----");

        if (history.isEmpty()) {
            System.out.println("No transactions available.");
            return;
        }

        for (Transaction t : history) {
            t.display();
        }
    }
}