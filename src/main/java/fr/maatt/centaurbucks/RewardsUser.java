package fr.maatt.centaurbucks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RewardsUser {
    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone; // intl number formats are important !!!
    private double pointTotal;
    private ArrayList<Order> orderHistory;

    public static class OrderItem {
        public String name;
        public double cost;

        private OrderItem() {};
        /**
            id 1=Coffee,2=Mocha,3=Green tea,4=Black tea,5=Chocolat chaud,6=Hot cocoa
         */
        public OrderItem(int id) {
            switch (id) {
                case 1:
                    this.name = "Coffee";
                    this.cost = 2.00;
                    break;
                case 2:
                    this.name = "Mocha";
                    this.cost = 4.00;
                    break;
                case 3:
                    this.name = "Green tea";
                    this.cost = 2.00;
                    break;
                case 4:
                    this.name = "Black tea";
                    this.cost = 2.00;
                    break;
                case 5:
                    this.name = "Chocolat chaud";
                    this.cost = 5.00;
                    break;
                case 6:
                    this.name = "Hot chocolate";
                    this.cost = 4.50;
                    break;
            }
        }
        public OrderItem(String name, double cost) {
            this.name = name;
            this.cost = cost;
        }
    }
    public static class Order {
        private ArrayList<OrderItem> items;
        private int orderNumber;
        private String orderDate; // should be an ISO 8601 date and time
        private double total;
        private double pointsUsed;
        private double pointsEarned;

        private Order() {};
        public Order(ArrayList<OrderItem> items, int pointsUsed) throws Exception {
            this.items = items;
            this.orderNumber = (int) Math.floor(Math.random()*(99999-10000+1)+10000);
            this.orderDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'").format(new Date());

            // Calculate total
            if (items.isEmpty()) { throw new Exception(); }
            for (OrderItem item : items) {
                this.total += item.cost;
            }

            // Calculate point usage
            if (pointsUsed != 0) {
                this.pointsUsed = pointsUsed;
                this.total = this.total - ((double) pointsUsed /100);
                if (total < 0) { this.total = 0; }
                this.pointsEarned = 0;
            } else {
                this.pointsUsed = 0;
                this.pointsEarned = Math.floor(total);
            }
            // Add sales tax to total after earning or spending points
            this.total = this.total * 0.0750;
        }
        public Order(ArrayList<OrderItem> items, int orderNumber, String orderDate, double total, double pointsUsed, double pointsEarned) {
            this.items = items;
            this.orderNumber = orderNumber;
            this.orderDate = orderDate;
            this.total = total;
            this.pointsUsed = pointsUsed;
            this.pointsEarned = pointsEarned;
        }
        public ArrayList<OrderItem> getItems() { return items; }
        public OrderItem getItem(int i) { return items.get(i); }
        public double getTotal() { return total; }
        public int getOrderNumber() { return orderNumber; }
        public String getOrderDate() { return orderDate; }
        public double getPointsUsed() { return pointsUsed; }
        public double getPointsEarned() { return pointsEarned; }
    }

    private RewardsUser() {}
    public RewardsUser(String firstName, String lastName, String email, String phone) {
        // set a provably random userID for their membership card
        this.userID = (int) Math.floor(Math.random()*(99999999-10000000+1)+10000000);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.pointTotal = 100; // sign-on bonus, hooray !
        this.orderHistory = new ArrayList<>();
    }
    public RewardsUser(int userID, String firstName, String lastName, String email, String phone, double pointTotal, ArrayList<Order> orders) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.pointTotal = pointTotal;
        this.orderHistory = orders;
    }

    public int getUserID() { return userID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public double getPointTotal() { return pointTotal; }

    /**
     * getOrderFromHistory(i) gets you the i-th order from an account's
     * history. This may be useful for processing refunds or a similar process
     * @param i Order number as an index of account's history (can be checked
     *          using getOrderHistory())
     * @return i-th order from an account's history
     */
    public Order getOrderFromHistory(int i) { return orderHistory.get(i); }
    public ArrayList<Order> getOrderHistory() { return orderHistory; }

    public void updateFirstName(String firstName) { this.firstName = firstName; }
    public void updateLastName(String lastName) { this.lastName = lastName; }
    /**
     * Used for updating an account's email address.
     * @param email an email address ; ie. officers@sdcs.club
     */
    public void updateEmail(String email) {
        // Validate email address format
        // src : https://stackoverflow.com/a/24320945
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches()) { this.email = email; }
    }

    /**
     * Used for updating an account's phone number.
     * @param phone an RFC 5733 formatted phone number, i.e. +1.6193887800
     */
    public void updatePhone(String phone) {
        // E.123 is ideal, but it's not as nice on the eyes
        // and would require conversion for the GUI.
        Pattern pattern = Pattern.compile("^\\+[0-9]{1,3}\\.[0-9]{4,14}(?:x.+)?$");
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) { this.phone = phone; }
    }

    /**
     * The usePoints() function is meant to be used internally in the process of
     * adding an order. Use Order.pointsUsed to subtract points from the account.
     * @param points used to complete a transaction
     */
    public void usePoints(double points) {
        this.pointTotal -= points;
        if (pointTotal <= 0) { this.pointTotal = 0; }
    }
    /**
     * The earnPoints() function is meant to be used internally in the process of
     * adding an order. Use Order.pointsEarned to credit points to the account.
     * @param points gained from completing a transaction
     */
    public void earnPoints(double points) { this.pointTotal += points; }

    /**
     * The addOrder() function is used for adding an order to a customer's account.
     * You should use both usePoints(Order.pointsUsed) and earnPoints(Order.pointsEarned)
     * after addOrder() to charge or credit the customer for their transaction.
     * @param order an initialised Order. Order.pointsUsed and Order.pointsEarned
     *              are generated at initialisation and should be used shortly after.
     */
    public void addOrder(Order order) { this.orderHistory.add(order); }
    public void setPointTotal(double points) { this.pointTotal = points; }
}
