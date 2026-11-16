package prog5121.part.pkg1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuickChat {

    private static final Scanner INPUT = new Scanner(System.in);

  private static final List<Message> SENT_MESSAGES = new ArrayList<>();
  private static final List<Message> DISREGARDED_MESSAGES = new ArrayList<>();
  private static final List<Message> STORED_MESSAGES = new ArrayList<>();
    private static int totalSent = 0;
    private static int messageCount = 0;

    public static void main(String[] args) {

        // Replace this demonstration check with Part 1 login validation.
        if (!loginSuccessful()) {
            System.out.println("Login failed. Please try again.");
            return;
        }

        System.out.println("Welcome to QuickChat.");

        loadMessagesFromJSON();
        boolean running = true;
       
while (running) {
    System.out.println("\n1. Send Messages");
    System.out.println("2. Show recently sent messages");
    System.out.println("3. Quit");
    System.out.println("4. Stored Messages");
    System.out.print("Choose an option: ");

    String choice = INPUT.nextLine();

    switch (choice) {
        case "1" -> sendMessages();

       case "2" -> showRecentlySentMessages();

        case "3" -> {
            System.out.println("Total messages sent: " + totalSent);
            System.out.println("Goodbye!");
            running = false;
        }

        case "4" -> {
            StoredMessages manager =
                    new StoredMessages(STORED_MESSAGES);
            System.out.println("Stored messages currently available: " + STORED_MESSAGES.size());
            boolean storedRunning = true;

            while (storedRunning) {
                System.out.println("\n--- STORED MESSAGES ---");
                System.out.println("1. Display sender and recipient");
                System.out.println("2. Display longest message");
                System.out.println("3. Search by Message ID");
                System.out.println("4. Search by recipient");
                System.out.println("5. Delete message using hash");
                System.out.println("6. Display full report");
                System.out.println("7. Return to main menu");
                System.out.print("Choose an option: ");

                String storedChoice = INPUT.nextLine();

                switch (storedChoice) {
                    case "1" ->
                        manager.displaySenderAndRecipient();

                    case "2" ->
                        manager.displayLongestMessage();

                    case "3" -> {
                        System.out.print("Enter Message ID: ");
                        String id = INPUT.nextLine();
                        manager.searchByMessageID(id);
                    }

                    case "4" -> {
                        System.out.print("Enter recipient number: ");
                        String recipient = INPUT.nextLine();
                        manager.searchByRecipient(recipient);
                    }

                    
                    case "5" -> {
                    System.out.print("Enter Message Hash: ");
                    String hash = INPUT.nextLine();

                 boolean deleted = manager.deleteByMessageHash(hash);

                 if (deleted) {
                 saveMessagesToJSON();
                 System.out.println("JSON file updated successfully.");
    }
} 

                    case "6" ->
                        manager.displayFullReport();

                    case "7" ->
                        storedRunning = false;

                    default ->
                        System.out.println("Invalid option.");
                }
            }
        }

        default ->
            System.out.println("Invalid option. Choose 1, 2, 3 or 4.");
    } 
}
}

    private static boolean loginSuccessful() {
        System.out.println("Please log in using your Part 1 account.");
        System.out.print("Username: ");
        String username = INPUT.nextLine();

        System.out.print("Password: ");
        String password = INPUT.nextLine();

        // Demonstration only: replace with your real Part 1 login method.
        return !username.trim().isEmpty()
            && !password.trim().isEmpty();
    }

    private static void sendMessages() {
        System.out.print("How many messages would you like to enter? ");

        int number;

        try {
            number = Integer.parseInt(INPUT.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a whole number.");
            return;
        }

        if (number < 1) {
            System.out.println("Please enter at least one message.");
            return;
        }

        for (int i = 0; i < number; i++) {
            System.out.println("\nMessage " + (i + 1));

            System.out.print("Recipient number (+27 followed by 9 digits): ");
            String recipient = INPUT.nextLine().trim();

            System.out.print("Enter your message: ");
            String text = INPUT.nextLine();

            Message current = new Message(messageCount, recipient, text);

            if (!current.checkMessageID()) {
                System.out.println("Invalid message ID.");
                i--;
                continue;
            }

            if (!current.checkRecipientCell().startsWith(
                    "Cell phone number successfully")) {
                System.out.println(current.checkRecipientCell());
                i--;
                continue;
            }

            if (!current.checkMessageLength().equals(
                    "Message ready to send.")) {
                System.out.println(
                    "Please enter a message of less than 250 characters."
                );
                i--;
                continue;
            }

            System.out.println("\n" + current.printMessages());

            System.out.println("\n1. Send Message");
            System.out.println("2. Disregard Message");
            System.out.println("3. Store Message to send later");
            System.out.print("Choose an option: ");

            String action = INPUT.nextLine();

            switch (action) {
                case "1" -> {
                    SENT_MESSAGES.add(current);
                    totalSent++;
                    messageCount++;
                    System.out.println("Message successfully sent.");
                }

              case "2" -> {
                   DISREGARDED_MESSAGES.add(current);
                   messageCount++;
                 System.out.println("Message successfully disregarded.");
}

                case "3" -> {
                    STORED_MESSAGES.add(current);
                    messageCount++;
                    saveMessagesToJSON();
                    System.out.println("Message successfully stored.");
                }

                default -> {
                    System.out.println(
                            "Invalid choice. Message was not sent."
                    );
                    i--;
                }
            }
        }

        System.out.println("Total messages sent: " + totalSent);
    }

    private static void saveMessagesToJSON() {
        StringBuilder json = new StringBuilder("[\n");

        for (int i = 0; i < STORED_MESSAGES.size(); i++) {
            Message m = STORED_MESSAGES.get(i);

            json.append("  {\n");
            json.append("    \"messageID\": \"")
                .append(escapeJSON(m.getMessageID())).append("\",\n");
            json.append("    \"messageHash\": \"")
                .append(escapeJSON(m.getMessageHash())).append("\",\n");
            json.append("    \"recipient\": \"")
                .append(escapeJSON(m.getRecipient())).append("\",\n");
            json.append("    \"message\": \"")
                .append(escapeJSON(m.getMessage())).append("\"\n");
            json.append("  }");

            if (i < STORED_MESSAGES.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("]\n");

        try {
            Files.write(
                Path.of("messages.json"),
                json.toString().getBytes(StandardCharsets.UTF_8)
            );
        } catch (IOException e) {
            System.out.println("Could not save the messages file.");
        }
    }

    private static String escapeJSON(String value) {
        return value.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }
    private static void loadMessagesFromJSON() {
    Path filePath = Path.of("messages.json");

    if (!Files.exists(filePath)) {
        System.out.println("No saved messages file found.");
        return;
    }

    try {
        String json = Files.readString(filePath, StandardCharsets.UTF_8);

        Pattern objectPattern = Pattern.compile("\\{([^{}]*)\\}");
        Matcher objectMatcher = objectPattern.matcher(json);

        int number = 0;

        while (objectMatcher.find()) {
            String object = objectMatcher.group(1);

            String id = getJsonValue(object, "messageID");
            String hash = getJsonValue(object, "messageHash");
            String recipient = getJsonValue(object, "recipient");
            String message = getJsonValue(object, "message");

            if (id != null && hash != null &&
                recipient != null && message != null) {

                Message loadedMessage = new Message(
                    ++number, recipient, message, id, hash
                );

                STORED_MESSAGES.add(loadedMessage);
            }
        }

        System.out.println("Stored messages loaded: " + STORED_MESSAGES.size());

    } catch (IOException e) {
        System.out.println("Error loading messages: " + e.getMessage());
    }
}

private static String getJsonValue(String object, String key) {
    Pattern pattern = Pattern.compile(
        "\"" + key + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\""
    );

    Matcher matcher = pattern.matcher(object);

    if (matcher.find()) {
        return matcher.group(1)
            .replace("\\\"", "\"")
            .replace("\\\\", "\\")
            .replace("\\n", "\n")
            .replace("\\r", "\r");
    }

    return null;
}
private static void showRecentlySentMessages() {
    if (SENT_MESSAGES.isEmpty()) {
        System.out.println("No sent messages available.");
        return;
    }

    System.out.println("\n--- RECENTLY SENT MESSAGES ---");

    for (Message message : SENT_MESSAGES) {
        System.out.println("Message ID: " + message.getMessageID());
        System.out.println("Recipient: " + message.getRecipient());
        System.out.println("Message: " + message.getMessage());
        System.out.println("----------------------");
    }
}
}