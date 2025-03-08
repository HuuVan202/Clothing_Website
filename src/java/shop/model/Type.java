/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

/**
 *
 * @author
 */
public class Type {

    private int type_id;
    private String type_name;
    private int cat_id;

    public Type() {
    }

    public Type(int type_id, String type_name, int cat_id) {
        this.type_id = type_id;
        this.type_name = type_name;
        this.cat_id = cat_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    @Override
    public String toString() {
        return "Type{" + "type_id=" + type_id + ", type_name=" + type_name + ", cat_id=" + cat_id + '}';
    }

}
