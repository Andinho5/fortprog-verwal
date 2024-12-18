package org.example.mail;

import org.example.service.ModelObject;
import org.example.user.User;

import java.sql.Timestamp;
import java.util.Objects;

public class PinnwandComment implements ModelObject {
    private String commentid;
    private User owner;
    private User author;
    private String content;
    private Timestamp date;

    public PinnwandComment() {

    }

    public PinnwandComment(String commentid, User owner, User author, String content, Timestamp date) {
        this.commentid = commentid;
        this.owner = owner;
        this.author = author;
        this.content = content;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PinnwandComment that)) return false;
        return Objects.equals(commentid, that.commentid) && Objects.equals(owner, that.owner) && Objects.equals(author, that.author) && Objects.equals(content, that.content) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentid, owner, author, content, date);
    }

    @Override
    public String toString() {
        return """
                %s / (%s): \n\t%s
                """.formatted(author.getUsermail(), date, content);
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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
