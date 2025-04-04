/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

import java.util.Date;

/**
 *
 * @author
 */
public class Feedback {

    private int feedback_id;
    private int order_id;
    private int pro_id;
    private int cus_id;
    private String cus_name;
    private int rating;
    private String comment;
    private Date feedback_date;
    private String purchasedSizes;

    public Feedback(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public Feedback(int feedback_id, int order_id, int pro_id, int cus_id, int rating, String comment, Date feedback_date) {
        this.feedback_id = feedback_id;
        this.order_id = order_id;
        this.pro_id = pro_id;
        this.cus_id = cus_id;
        this.rating = rating;
        this.comment = comment;
        this.feedback_date = feedback_date;
    }

    public Feedback(int feedback_id, int rating, String comment, Date feedback_date) {
        this.feedback_id = feedback_id;
        this.rating = rating;
        this.comment = comment;
        this.feedback_date = feedback_date;
    }

    public Feedback(int feedback_id, int pro_id, int cus_id, String cus_name, int rating, String comment, Date feedback_date, String purchasedSizes) {
        this.feedback_id = feedback_id;
        this.pro_id = pro_id;
        this.cus_id = cus_id;
        this.cus_name = cus_name;
        this.rating = rating;
        this.comment = comment;
        this.feedback_date = feedback_date;
        this.purchasedSizes = purchasedSizes;
    }

    public Feedback() {
    }

    public int getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(int feedback_id) {
        this.feedback_id = feedback_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getPro_id() {
        return pro_id;
    }

    public void setPro_id(int pro_id) {
        this.pro_id = pro_id;
    }

    public int getCus_id() {
        return cus_id;
    }

    public void setCus_id(int cus_id) {
        this.cus_id = cus_id;
    }

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getFeedback_date() {
        return feedback_date;
    }

    public void setFeedback_date(Date feedback_date) {
        this.feedback_date = feedback_date;
    }

    public String getPurchasedSizes() {
        return purchasedSizes;
    }

    public void setPurchasedSizes(String purchasedSizes) {
        this.purchasedSizes = purchasedSizes;
    }

    @Override
    public String toString() {
        return "Feedback{"
                + "feedback_id=" + feedback_id
                + ", order_id=" + order_id
                + ", pro_id=" + pro_id
                + ", cus_id=" + cus_id
                + ", rating=" + rating
                + ", comment='" + comment + '\''
                + ", feedback_date=" + feedback_date
                + ", purchasedSizes='" + purchasedSizes + '\''
                + '}';
    }
}
