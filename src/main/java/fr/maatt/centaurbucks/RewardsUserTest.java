package fr.maatt.centaurbucks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RewardsUserTest {
    String datastore_file = "./src/main/java/fr/maatt/centaurbucks/test_load.json";
    RewardsDatastore ds = new RewardsDatastore(new File(datastore_file));
    public RewardsUserTest() throws IOException {}

    @Test
    public void testGetUserID() {
        int expected = 15478918;
        int actual = ds.users.get(0).getUserID();
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testGetName() {
        String expectedfirst = "Tim";
        String expectedlast = "Apple";
        String actualfirst = ds.users.get(0).getFirstName();
        String actuallast = ds.users.get(0).getLastName();
        Assertions.assertEquals(expectedfirst, actualfirst);
        Assertions.assertEquals(expectedlast, actuallast);
    }
    @Test
    public void testGetContact() {
        String expectedPhone = "+1.8005555559";
        String actualPhone = ds.users.get(0).getPhone();
        String expectedEmail = "tim.apple@apple.com";
        String actualEmail = ds.users.get(0).getEmail();
        Assertions.assertEquals(expectedPhone, actualPhone);
        Assertions.assertEquals(expectedEmail, actualEmail);
    }

    @Test
    public void testUpdateContact() {
        String expectedFirstName = "Timothy";
        String expectedLastName = "Google";
        String expectedEmail = "tim.apple@gmail.com";
        String expectedPhone = "+44.01189998819991197253";
        ds.users.get(0).updateFirstName(expectedFirstName);
        ds.users.get(0).updateLastName(expectedLastName);
        ds.users.get(0).updateEmail(expectedEmail);
        ds.users.get(0).updatePhone(expectedPhone);

        String actualFirstName = ds.users.get(0).getFirstName();
        String actualLastName = ds.users.get(0).getLastName();
        String actualEmail = ds.users.get(0).getEmail();
        String actualPhone = ds.users.get(0).getPhone();
        Assertions.assertEquals(expectedFirstName, actualFirstName);
        Assertions.assertEquals(expectedLastName, actualLastName);
        Assertions.assertEquals(expectedEmail, actualEmail);
        Assertions.assertNotEquals(expectedPhone, actualPhone);
    }

    @Test
    public void testOrderHistory() {
        ArrayList<RewardsUser.Order> expected = new ArrayList<>(); // expect empty
        ArrayList<RewardsUser.Order> actual = ds.users.get(0).getOrderHistory();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testAddOrder() throws Exception {
        int expectedPoints = 106;
        RewardsUser.OrderItem expectedItem = new RewardsUser.OrderItem(1);
        RewardsUser.OrderItem expectedItem2 = new RewardsUser.OrderItem(2);
        ArrayList<RewardsUser.OrderItem> expectedItems = new ArrayList<>();
        expectedItems.add(expectedItem);
        expectedItems.add(expectedItem2);

        RewardsUser.Order expectedOrder = new RewardsUser.Order(expectedItems, 0);
        ds.users.get(0).addOrder(expectedOrder);
        ds.users.get(0).earnPoints(expectedOrder.getPointsEarned());
        ds.users.get(0).usePoints(expectedOrder.getPointsUsed());

        // Check gained points
        double actualPoints = ds.users.get(0).getPointTotal();
        // Check order
        RewardsUser.Order actualOrder = ds.users.get(0).getOrderFromHistory(0);

        Assertions.assertEquals(expectedPoints, actualPoints);
        Assertions.assertEquals(expectedOrder, actualOrder);
    }
}
