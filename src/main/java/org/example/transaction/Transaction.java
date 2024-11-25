package org.example.transaction;

import org.example.service.ModelObject;
import org.example.user.User;

import java.util.Objects;
import java.util.Optional;

public class Transaction implements ModelObject {
    private String transactionid;
    private User sender;
    private User receiver;
    private double menge;
    private String purposemessage;

    /**
     * BOILERPLATE (Konstruktoren, Equals/HashCode, Getter/Setter
     */

    public Transaction() {

    }

    public Transaction(String transactionid, User sender, User receiver, double menge, String purposemessage) {
        this.transactionid = transactionid;
        this.sender = sender;
        this.receiver = receiver;
        this.menge = menge;
        this.purposemessage = purposemessage;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Transaction that)) return false;
        return Objects.equals(transactionid, that.transactionid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(transactionid);
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public double getMenge() {
        return menge;
    }

    public void setMenge(double menge) {
        this.menge = menge;
    }

    public String getPurposemessage() {
        return purposemessage;
    }

    public void setPurposemessage(String purposemessage) {
        this.purposemessage = purposemessage;
    }
}

