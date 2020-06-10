package com.vnakhoa.hotel.model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ContactBooking implements Serializable {
    private String fullName;
    private String gender;
    private String idCard;
    private String email;
    private String country;
    private String datein;
    private String dateout;
    private String room;
    private String id_category;

    public ContactBooking() {

    }

    public ContactBooking(String fullName, String gender, String idCard, String email, String country, String datein, String dateout, String room, String id_category) {
        this.fullName = fullName;
        this.gender = gender;
        this.idCard = idCard;
        this.email = email;
        this.country = country;
        this.datein = datein;
        this.dateout = dateout;
        this.room = room;
        this.id_category = id_category;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getId_category() {
        return id_category;
    }

    public void setId_category(String id_category) {
        this.id_category = id_category;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDatein() {
        return datein;
    }

    public void setDatein(String datein) {
        this.datein = datein;
    }

    public String getDateout() {
        return dateout;
    }

    public void setDateout(String dateout) {
        this.dateout = dateout;
    }

    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("fullname",this.fullName);
            object.put("gender",this.gender);
            object.put("email",this.email);
            object.put("idcard",this.idCard);
            object.put("country",this.country);
            object.put("datein",this.datein);
            object.put("dateout",this.dateout);
            object.put("room",this.room);
            object.put("id_category",this.id_category);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
