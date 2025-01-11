/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author
 */
public class Support {

    private int sup_id;
    private int cus_id;
    private String description;

    public Support(int sup_id, int cus_id, String description) {
        this.sup_id = sup_id;
        this.cus_id = cus_id;
        this.description = description;
    }

    public int getSup_id() {
        return sup_id;
    }

    public void setSup_id(int sup_id) {
        this.sup_id = sup_id;
    }

    public int getCus_id() {
        return cus_id;
    }

    public void setCus_id(int cus_id) {
        this.cus_id = cus_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Support{" + "sup_id=" + sup_id + ", cus_id=" + cus_id + ", description=" + description + '}';
    }

}
