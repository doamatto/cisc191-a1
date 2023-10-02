package fr.maatt.centaurbucks;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class RewardsDatastore {
    public ArrayList<RewardsUser> users;
    public File datastore;
    final ObjectMapper mapper = new ObjectMapper();

    public RewardsDatastore(File datastore) throws IOException {
        this.datastore = datastore;

        ObjectMapper mapper = new ObjectMapper();
        this.users = new ArrayList<>(Arrays.asList(mapper.readValue(
                datastore,
                RewardsUser[].class
        )));
    }

    /**
     * Updates the datastore on disk with that in memory. Creates a disk backup prior to saving
     * to prevent data corruption. Backup often.
     * @throws IOException if it cannot create a new file called datastore_loc+".bak" or if it cannot update the existing datastore_loc.
     */
    public void save() throws IOException {
        boolean flag = datastore.renameTo(new File(datastore.getAbsoluteFile()+".bak"));
        if (flag) {
            this.mapper.writeValue(
                    new File(datastore.getCanonicalPath()),
                    users
            );
        } else { System.out.println("[ERROR:DATASTORE] Unable to safely update datastore"); }
    }

    /**
     * Gets a user based on their User ID.
     * @param userId ID of user being searched
     * @return User profile, if found. Null, otherwise.
     */
    public RewardsUser getUser(int userId) {
        for (RewardsUser user : users) {
            if (user.getUserID() == userId) {
                return user;
            }
        }
        return null;
    }

    /**
     * Gets an order based on its Order ID.
     * @param orderId ID of order being searched
     * @return Order, if found. Null, otherwise.
     */
    public RewardsUser.Order getOrder(int orderId) {
        for (RewardsUser user : users) {
            for (RewardsUser.Order order : user.getOrderHistory()) {
                if (order.getOrderNumber() == orderId) {
                    return order;
                }
            }
        }
        return null;
    }

    /**
     * Creates a new user with
     * @return ID of user created
     */
    public int createUser(String firstName, String lastName, String email, String phone) {
        users.add(new RewardsUser(firstName, lastName, email, phone));
        return users.get(users.size()-1).getUserID();
    }
    public void removeUser(int userId) { users.removeIf(user -> user.getUserID() == userId); }
}
