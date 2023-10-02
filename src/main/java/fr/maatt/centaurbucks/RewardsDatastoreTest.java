package fr.maatt.centaurbucks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RewardsDatastoreTest {
    @Test
    public void testLoadDatastore() throws IOException {
        RewardsUser expected = new RewardsUser(
                15478918,
                "Tim",
                "Apple",
                "tim.apple@apple.com",
                "+1.8005555559",
                100.0,
                new ArrayList<>()
        );
        String datastore_file = "./src/main/java/fr/maatt/centaurbucks/test_load.json";
        RewardsDatastore ds = new RewardsDatastore(new File(datastore_file));
        RewardsUser actual = ds.users.get(0);

        compareUsers(expected, actual);
    }

    @Test
    public void testGetUserFromId() throws IOException {
        RewardsUser expected = new RewardsUser(
                15478918,
                "Tim",
                "Apple",
                "tim.apple@apple.com",
                "+1.8005555559",
                100.0,
                new ArrayList<>()
        );
        String datastore_file = "./src/main/java/fr/maatt/centaurbucks/test_load.json";
        RewardsDatastore ds = new RewardsDatastore(new File(datastore_file));
        RewardsUser actual = ds.getUser(15478918);

        compareUsers(expected, actual);
    }

    private void compareUsers(RewardsUser expected, RewardsUser actual) {
        Assertions.assertEquals(expected.getUserID(), actual.getUserID());
        Assertions.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assertions.assertEquals(expected.getLastName(), actual.getLastName());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPhone(), actual.getPhone());
        Assertions.assertEquals(expected.getPointTotal(), actual.getPointTotal());
        Assertions.assertEquals(expected.getOrderHistory(), actual.getOrderHistory());
    }

    @Test
    public void testSaveDatastore() throws IOException {
        String expected = "[{\"userID\":15478918,\"firstName\":\"Tim\",\"lastName\":\"Apple\",\"email\":\"tim.apple@apple.com\",phone:\"+1.8005555559\",\"pointTotal\":100,\"orderHistory\":[]}]";

        String datastore_file = "./src/main/java/fr/maatt/centaurbucks/test_save.json";
        RewardsDatastore ds = new RewardsDatastore(new File(datastore_file));
        ds.users.add(new RewardsUser(
                15478918,
                "Tim",
                "Apple",
                "tim.apple@apple.com",
                "+1.8005555559",
                100.0,
                new ArrayList<>()
        ));
        ds.save();

        // TODO: Add missing test
    }
}
