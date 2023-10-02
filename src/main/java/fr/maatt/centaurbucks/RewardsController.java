package fr.maatt.centaurbucks;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class RewardsController {
    public TextField firstName;
    public TextField lastName;
    public Text pointTotal;
    public TextField email;
    public TextField phone;
    public Button updateUser;
    public Button removeUser;
    public HBox orderHistory;
    public TextField searchUserID;
    public Button tendOrder;
    public Text orderNumber;
    public Text orderTotal;
    public TextArea pointsUsed;
    private RewardsDatastore ds;

    private void spawnDatastore() throws IOException {
        ds = new RewardsDatastore(new File("./src/main/java/fr/maatt/centaurbucks/datastore.json"));
    }
    private RewardsUser loadUser(int userId) throws IOException {
        if (ds == null) { spawnDatastore(); }
        if (userId == 0) {
            new Alert(Alert.AlertType.ERROR, "Missing customer ID").showAndWait();
            return null;
        }
        return ds.getUser(userId);
    }

    protected void loadUserData(RewardsUser user) {
        // Organise data locally
        String fName = user.getFirstName();
        String lName = user.getLastName();
        String emailAddress = user.getEmail();
        String phoneNumber = user.getPhone();
        double points = user.getPointTotal();
        ArrayList<RewardsUser.Order> history = user.getOrderHistory().isEmpty() ?
                new ArrayList<>() :
                user.getOrderHistory();

        // Paint user data in GUI and show controls
        firstName.setText(fName);
        lastName.setText(lName);
        email.setText(emailAddress);
        phone.setText(phoneNumber);
        pointTotal.setText(String.valueOf(points));

        ObservableList<Node> childs = orderHistory.getChildren();
        Text labelTitle = new Text("Order history :");
        childs.clear();
        childs.add(labelTitle);
        // Load order history
        for (RewardsUser.Order order : history) {
            String orderDate = order.getOrderDate();
            int orderNumber = order.getOrderNumber();
            double orderTotal = order.getTotal();

            double orderPointsUsed = order.getPointsUsed();
            double orderPointsEarned = order.getPointsEarned();
            double changeInPoints = orderPointsEarned - orderPointsUsed;

            Label labelOrder = new Label(String.format("Order no. %s for %.7f (%s)", orderNumber, orderTotal, orderDate));
            Label labelPointsChange = new Label(String.valueOf(changeInPoints));
            HBox purchase = new HBox();
            purchase.getChildren().add(labelOrder);
            purchase.getChildren().add(labelPointsChange);
            childs.add(purchase);
        }
    }

    @FXML
    protected void onSearchBtnClick() throws IOException {
        if (searchUserID.getText().isBlank()) { return; }
        // Get user ID from text box
        RewardsUser user = loadUser(Integer.parseInt(searchUserID.getText()));
        if (user == null) {
            new Alert(Alert.AlertType.ERROR, "Customer does not exist.").showAndWait();
            return;
        }
        orderHistory.getChildren().clear();
        loadUserData(user);
    }

    @FXML
    protected void onUpdateUserClick() throws IOException {
        if (searchUserID.getText().isBlank()) { return; }
        // Get user ID from text box
        RewardsUser user = loadUser(Integer.parseInt(searchUserID.getText()));
        if (user == null) {
            new Alert(Alert.AlertType.ERROR, "Customer does not exist.").showAndWait();
            return;
        }

        try {
            user.updateFirstName(firstName.getText());
            user.updateLastName(lastName.getText());
            user.updateEmail(email.getText());
            user.updatePhone(phone.getText());

            ds.save();
        } catch (Exception e) {
            System.out.printf("%s", e);
            new Alert(Alert.AlertType.ERROR, "An error occurred while trying to update the user. Check the system logs for more information.").showAndWait();
            return;
        }

        new Alert(Alert.AlertType.INFORMATION, "User successfully updated").showAndWait();
    }

    @FXML
    protected void onRemoveUserClick() throws IOException {
        if (searchUserID.getText().isBlank()) { return; }
        // Get user ID from text box
        RewardsUser user = loadUser(Integer.parseInt(searchUserID.getText()));
        if (user == null) {
            new Alert(Alert.AlertType.ERROR, "Customer does not exist.").showAndWait();
            return;
        }

        Optional<ButtonType> res = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user ? It is not reversible.").showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            ds.removeUser(user.getUserID());
            ds.save();
            RewardsUser clearData = new RewardsUser(0, "", "", "", "", 0.0, new ArrayList<>());
            searchUserID.setText("");
            loadUserData(clearData);
            orderHistory.getChildren().clear();
            new Alert(Alert.AlertType.INFORMATION, "User removed successfully").showAndWait();
        }

    }

    @FXML
    protected void onAddUserClick() throws IOException {
        if (ds == null) { spawnDatastore(); }
        int userId = ds.createUser("John", "Doe", "replace.me@email.com", "+41.0229172900");
        loadUserData(ds.getUser(userId));
        searchUserID.setText(String.valueOf(userId));
        ds.save();
        new Alert(Alert.AlertType.INFORMATION, "User created successfully.").showAndWait();
    }

    @FXML
    protected void onStartOrderClick() throws IOException {
        // Generate order number
        int orderNum = (int) Math.floor(Math.random()*(99999-10000+1)+10000);
        while (ds.getOrder(orderNum) != null) {
            orderNum = (int) Math.floor(Math.random()*(99999-10000+1)+10000);
        }
        orderNumber.setText(String.valueOf(orderNum));

        // Load pop-up stage
        // TODO:
    }

    @FXML
    protected void onTendOrderClick() throws IOException {
        if (searchUserID.getText().isBlank()) { return; }
        // Get user ID from text box
        RewardsUser user = loadUser(Integer.parseInt(searchUserID.getText()));
        if (user == null) {
            new Alert(Alert.AlertType.ERROR, "Customer does not exist.").showAndWait();
            return;
        }

        try {
            // TODO: Finish adding support for tending order
            RewardsUser.Order order = new RewardsUser.Order(
                orderItems,
                Integer.parseInt(pointsUsed.getText())
            );

            user.addOrder(order);
            user.usePoints(order.getPointsUsed());
            user.earnPoints(order.getPointsEarned());
            ds.save();
        } catch (Exception e) {
            System.out.printf("%s", e);
            new Alert(Alert.AlertType.ERROR, "An error occurred while trying to update the user. Check the system logs for more information.").showAndWait();
            return;
        }
        new Alert(Alert.AlertType.INFORMATION, "User successfully updated").showAndWait();
    }
}