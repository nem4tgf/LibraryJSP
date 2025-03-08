package org.example.demo1.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BorrowTicket {
    private int id;
    private int studentId; // ID của học sinh mượn sách
    private int bookId; // ID của sách được mượn
    private LocalDate borrowDate; // Ngày mượn
    private LocalDate dueDate; // Ngày phải trả
    private LocalDate returnDate; // Ngày trả thực tế (có thể null)
    private String status; // "borrowed", "returned", "overdue"
    private LocalDateTime createdAt; // Thời gian tạo

    // Constructor mặc định
    public BorrowTicket() {}

    // Constructor đầy đủ
    public BorrowTicket(int id, int studentId, int bookId, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate, String status, LocalDateTime createdAt) {
        this.id = id;
        this.studentId = studentId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}