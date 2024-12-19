package org.example.mail;

import org.example.service.ModelObject;
import org.example.user.User;

import java.sql.Timestamp;
import java.util.Objects;

public class BBMail implements ModelObject {
    private String chatid;
    private User sender;
    private User recipient;
    private String content;
    private Timestamp date;

    /**
     * BOILERPLATE (Konstruktoren, Equals/HashCode, Getter/Setter
     */

    public BBMail() {
    }

    public BBMail(String chatid, User sender, User recipient, String content, Timestamp date) {
        this.chatid = chatid;
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BBMail bbMail)) return false;
        return Objects.equals(chatid, bbMail.chatid);
    }

    @Override
    public String toString() {
        return "BBMail: " +
                "Sender=" + sender +
                ", Empfaenger=" + recipient +
                ", Inhalt='" + content + '\'' +
                ", Datum=" + date;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(chatid);
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
