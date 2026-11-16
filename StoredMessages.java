
package prog5121.part.pkg1;

import java.util.ArrayList;
import java.util.List;

public class StoredMessages {

    private List<Message> storedMessages = new ArrayList<>();

    public StoredMessages(List<Message> storedMessages) {
        if (storedMessages != null) {
            this.storedMessages = storedMessages;
        }
    }

    // 1. Display sender and recipient
    public void displaySenderAndRecipient() {

        if (storedMessages.isEmpty()) {
            System.out.println("There are no stored messages.");
            return;
        }

        for (Message message : storedMessages) {
            System.out.println("Sender: You");
            System.out.println("Recipient: " + message.getRecipient());
            System.out.println("----------------------");
        }
    }

    // 2. Display the longest stored message
    public void displayLongestMessage() {

        if (storedMessages.isEmpty()) {
            System.out.println("There are no stored messages.");
            return;
        }

        Message longestMessage = storedMessages.get(0);

        for (Message message : storedMessages) {
            if (message.getMessage().length()
                    > longestMessage.getMessage().length()) {
                longestMessage = message;
            }
        }

        System.out.println("Longest Stored Message:");
        System.out.println(longestMessage.getMessage());
    }

    // 3. Search using Message ID
    public void searchByMessageID(String messageID) {

        for (Message message : storedMessages) {
            if (message.getMessageID().equals(messageID)) {
                System.out.println("Recipient: " + message.getRecipient());
                System.out.println("Message: " + message.getMessage());
                return;
            }
        }

        System.out.println("Message ID not found.");
    }

    // 4. Search messages using recipient number
    public void searchByRecipient(String recipient) {

        boolean found = false;

        for (Message message : storedMessages) {
            if (message.getRecipient().equals(recipient)) {
                System.out.println("Message: " + message.getMessage());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No messages found for this recipient.");
        }
    }

    // 5. Delete a message using its Message Hash
    public boolean deleteByMessageHash(String messageHash) {

        for (Message message : storedMessages) {
            if (message.getMessageHash().equals(messageHash)) {
                storedMessages.remove(message);
                System.out.println("Message successfully deleted.");
                return true;
            }
        }

        System.out.println("Message hash not found.");
        return false;
    }

    // 6. Display a full report
    public void displayFullReport() {

        if (storedMessages.isEmpty()) {
            System.out.println("There are no stored messages.");
            return;
        }

        for (Message message : storedMessages) {
            System.out.println("----------------------");
            System.out.println("Message ID: " + message.getMessageID());
            System.out.println("Message Hash: " + message.getMessageHash());
            System.out.println("Recipient: " + message.getRecipient());
            System.out.println("Message: " + message.getMessage());
        }

        System.out.println("----------------------");
    }
}
