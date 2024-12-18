package org.example.transaction;

import org.example.service.ModelObject;
import org.example.user.User;

import java.sql.Timestamp;
import java.util.Objects;

public class Transaction implements ModelObject, BaseTransaction {
    private String transactionid;
    private User sender;
    private User receiver;
    private double menge;
    private Timestamp date;
    private String purposemessage;

    /**
     * BOILERPLATE (Konstruktoren, Equals/HashCode, Getter/Setter
     */

    public Transaction() {

    }

    public Transaction(String transactionid, User sender, User receiver, double menge, Timestamp date, String purposemessage) {
        this.transactionid = transactionid;
        this.sender = sender;
        this.receiver = receiver;
        this.menge = menge;
        this.date = date;
        this.purposemessage = purposemessage;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Transaction that)) return false;
        return Objects.equals(transactionid, that.transactionid);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "receiver=" + receiver +
                ", date=" + date +
                ", menge=" + menge +
                ", purposemessage='" + purposemessage + '\'' +
                ", transactionid='" + transactionid + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(transactionid);
    }

    public String getTransactionId() {
        return transactionid;
    }

    @Override
    public String getReceiverMail() {
        return receiver.getUsermail();
    }

    public void setTransactionId(String transactionid) {
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

    public double getAmount() {
        return menge;
    }

    public void setAmount(double menge) {
        this.menge = menge;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getPurposeMessage() {
        return purposemessage;
    }

    public void setPurposeMessage(String purposemessage) {
        this.purposemessage = purposemessage;
    }
}

