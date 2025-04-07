/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.chat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import shop.context.DBcontext;
import shop.model.Chat;

/**
 *
 * @author Admin
 */
public class ChatDAO extends DBcontext {

    private static final int ADMIN_ID = 1;

    public String getCustomerName(int customerID) {
        connection = getConnection();

        try {
            String sql = """
                         SELECT [cus_name]
                         FROM [ClothingShopDB].[dbo].[Customer]
                         WHERE [cus_id] = ?""";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, customerID);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "Unknown Customer";
    }

    public boolean insertChat(Chat chat) {
        connection = getConnection();

        try {
            // Get or create dialogue ID first
            int dialogueID = getOrCreateDialogueID(chat.getSenderID(), chat.getReceiverID());
            if (dialogueID == -1) {
                System.out.println("Failed to get or create dialogueID");
                return false;
            }

            // Set the dialogue ID in the chat object
            chat.setDialogueID(dialogueID);

            // Insert the message
            String sql = """
                  INSERT INTO [dbo].[Message]
                             ([dialogueID]
                             ,[senderID]
                             ,[receiverID]
                             ,[messageContent]
                             ,[sentDate]
                             ,[isSeen])
                       VALUES
                             (?, ?, ?, ?, ?, ?)""";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, chat.getDialogueID());
            statement.setInt(2, chat.getSenderID());
            statement.setInt(3, chat.getReceiverID());
            statement.setString(4, chat.getMessageContent());
            statement.setTimestamp(5, chat.getSentDate());
            statement.setBoolean(6, chat.isIsSeen());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error in insertChat: " + e.getMessage());
            return false;
        } finally {
            closeResources();
        }
    }

// Add a method to close resources
    private void closeResources() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }

    public boolean updateStatusSeen(int user, Chat chat) {
        connection = getConnection();

        try {
            int dialogueID = getOrCreateDialogueID(chat.getSenderID(), chat.getReceiverID());
            String sql = "UPDATE [dbo].[Message] SET isSeen = 1 WHERE senderID = ? AND dialogueID = ? AND isSeen = 0";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, user);
            statement.setInt(2, dialogueID);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private int getOrCreateDialogueID(int user1ID, int user2ID) {
        connection = getConnection();
        try {

            String selectSql = "SELECT TOP 1 dialogueID FROM [dbo].[Dialogue] "
                    + "WHERE (user1ID = ? AND user2ID = ?) OR (user1ID = ? AND user2ID = ?) "
                    + "ORDER BY [createdDate] DESC";
            statement = connection.prepareStatement(selectSql);
            statement.setInt(1, user1ID);
            statement.setInt(2, user2ID);
            statement.setInt(3, user2ID);
            statement.setInt(4, user1ID);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("dialogueID");
            }

            String insertSql = "INSERT INTO [dbo].[Dialogue] (user1ID, user2ID, createdDate) "
                    + "VALUES (?, ?, GETDATE()); SELECT SCOPE_IDENTITY() AS dialogueID";
            statement = connection.prepareStatement(insertSql);
            statement.setInt(1, user1ID);
            statement.setInt(2, user2ID);

            ResultSet insertRs = statement.executeQuery();
            if (insertRs.next()) {
                return insertRs.getInt("dialogueID");
            }
            return -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public List<Chat> getChatsBetweenUsers(int userID1, int userID2) {
        connection = getConnection();
        List<Chat> chats = new ArrayList<>();

        try {
            String sql = "SELECT * FROM [dbo].[Message] WHERE "
                    + "((senderID = ? AND receiverID = ?) OR (senderID = ? AND receiverID = ?)) "
                    + "ORDER BY sentDate ASC";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userID1);
            statement.setInt(2, userID2);
            statement.setInt(3, userID2);
            statement.setInt(4, userID1);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int senderID = resultSet.getInt("senderID");
                int receiverID = resultSet.getInt("receiverID");
                String messageContent = resultSet.getString("messageContent");
                Timestamp sentDate = resultSet.getTimestamp("sentDate");
                int dialogueID = resultSet.getInt("dialogueID");
                boolean isSeen = resultSet.getBoolean("isSeen");

                Chat chat = new Chat(senderID, receiverID, messageContent, sentDate, dialogueID, isSeen);
                chats.add(chat);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return chats;
    }

    public Set<Integer> getAdminChatCustomers() {
        Set<Integer> customerIDs = new HashSet<>();
        connection = getConnection();

        try {
            String sql = "SELECT DISTINCT CASE WHEN senderID = ? THEN receiverID ELSE senderID END AS customerID "
                    + "FROM [dbo].[Message] WHERE (senderID = ? OR receiverID = ?) ";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, ADMIN_ID);
            statement.setInt(2, ADMIN_ID);
            statement.setInt(3, ADMIN_ID);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customerIDs.add(resultSet.getInt("customerID"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customerIDs;
    }

    public static void main(String[] args) {
        ChatDAO chatDao = new ChatDAO();
        Chat chat = new Chat(2, 1, "Hello Admin Test", new Timestamp(new Date().getTime()), 1, false);
        if (chatDao.insertChat(chat)) {
            System.out.println("thanh cong");
        } else {
            System.out.println("that bai");
        }

    }
}
