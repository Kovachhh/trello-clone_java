package com.example.trello;

public class Card {
    private String title;
    private String status;
    private String description;
    private String expirationDate;
    private String assigneeUser;
    private String authorUser;
    private String tags;

    // Конструктор класу Card
    public Card(String title, String status, String description, String expirationDate, String assigneeUser, String authorUser, String tags) {
        this.title = title;
        this.status = status;
        this.description = description;
        this.expirationDate = expirationDate;
        this.assigneeUser = assigneeUser;
        this.authorUser = authorUser;
        this.tags = tags;
    }

    public Card(String title, String description, String expirationDate, String assigneeUser, String authorUser, String tags) {
        this.title = title;
        this.description = description;
        this.expirationDate = expirationDate;
        this.assigneeUser = assigneeUser;
        this.authorUser = authorUser;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getAssigneeUser() {
        return assigneeUser;
    }
    public void setAssigneeUser(String expirationDate) {
        this.assigneeUser = assigneeUser;
    }

    public String getAuthorUser() {
        return authorUser;
    }
    public void setAuthorUser(String authorUser) {
        this.authorUser = authorUser;
    }

    public String getTags() {
        return tags;
    }
    public void setTags(String authorUser) {
        this.tags = tags;
    }
}
