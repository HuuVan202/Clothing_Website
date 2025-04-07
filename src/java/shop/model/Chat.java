/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

import java.sql.Timestamp;

/**
 *
 * @author Admin
 */
public class Chat {

    private int senderID;
    private int receiverID;
    private String messageContent;
    private Timestamp sentDate;
    private int dialogueID;
    private boolean isSeen;

    public Chat() {
    }

    public Chat(int senderID, int receiverID, String messageContent, Timestamp sentDate, int dialogueID, boolean isSeen) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.messageContent = messageContent;
        this.sentDate = sentDate;
        this.dialogueID = dialogueID;
        this.isSeen = isSeen;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Timestamp getSentDate() {
        return sentDate;
    }

    public void setSentDate(Timestamp sentAt) {
        this.sentDate = sentAt;
    }

    public int getDialogueID() {
        return dialogueID;
    }

    public void setDialogueID(int dialogueID) {
        this.dialogueID = dialogueID;
    }

    public boolean isIsSeen() {
        return isSeen;
    }

    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    @Override
    public String toString() {
        return "Chat{" + "senderID=" + senderID + ", receiverID=" + receiverID + ", messageContent=" + messageContent + ", sentAt=" + sentDate + ", dialogueID=" + dialogueID + ", isSeen=" + isSeen + '}';
    }

}
