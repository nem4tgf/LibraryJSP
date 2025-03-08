package org.example.demo1.model;

import java.time.LocalDateTime;

public class Book {
    private int id;
    private String bookCode; // Mã sách
    private String title; // Tiêu đề
    private String author; // Tác giả
    private int totalQuantity; // Tổng số lượng
    private int availableQuantity; // Số lượng sẵn có
    private LocalDateTime createdAt; // Thời gian tạo

    // Constructor mặc định
    public Book() {}

    // Constructor đầy đủ
    public Book(int id, String bookCode, String title, String author, int totalQuantity, int availableQuantity, LocalDateTime createdAt) {
        this.id = id;
        this.bookCode = bookCode;
        this.title = title;
        this.author = author;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
        this.createdAt = createdAt;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBookCode() { return bookCode; }
    public void setBookCode(String bookCode) { this.bookCode = bookCode; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}