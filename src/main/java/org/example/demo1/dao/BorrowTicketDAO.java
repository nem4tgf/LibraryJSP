package org.example.demo1.dao;

import org.example.demo1.model.BorrowTicket;
import org.example.demo1.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BorrowTicketDAO {
    private static final String SELECT_BY_ID = "SELECT id, student_id, book_id, borrow_date, due_date, return_date, status, created_at FROM borrow_tickets WHERE id = ?";
    private static final String SELECT_BY_STUDENT = "SELECT id, student_id, book_id, borrow_date, due_date, return_date, status, created_at FROM borrow_tickets WHERE student_id = ?";
    private static final String SELECT_ALL = "SELECT id, student_id, book_id, borrow_date, due_date, return_date, status, created_at FROM borrow_tickets";
    private static final String INSERT = "INSERT INTO borrow_tickets (student_id, book_id, borrow_date, due_date, status) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE borrow_tickets SET return_date = ?, status = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM borrow_tickets WHERE id = ?";

    public BorrowTicket getBorrowTicketById(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapResultSetToBorrowTicket(rs) : null;
            }
        }
    }

    public List<BorrowTicket> getBorrowTicketsByStudentId(int studentId) throws SQLException {
        List<BorrowTicket> tickets = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_STUDENT)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapResultSetToBorrowTicket(rs));
                }
            }
            return tickets;
        }
    }

    public List<BorrowTicket> getAllBorrowTickets() throws SQLException {
        List<BorrowTicket> tickets = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tickets.add(mapResultSetToBorrowTicket(rs));
            }
            return tickets;
        }
    }

    public void addBorrowTicket(BorrowTicket ticket) throws SQLException {
        validateBorrowTicket(ticket);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT)) {
            stmt.setInt(1, ticket.getStudentId());
            stmt.setInt(2, ticket.getBookId());
            stmt.setDate(3, Date.valueOf(ticket.getBorrowDate()));
            stmt.setDate(4, Date.valueOf(ticket.getDueDate()));
            stmt.setString(5, ticket.getStatus());
            stmt.executeUpdate();
        }
    }

    public void updateBorrowTicket(BorrowTicket ticket) throws SQLException {
        if (ticket == null || ticket.getId() <= 0) {
            throw new IllegalArgumentException("Invalid borrow ticket");
        }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            stmt.setDate(1, ticket.getReturnDate() != null ? Date.valueOf(ticket.getReturnDate()) : null);
            stmt.setString(2, ticket.getStatus());
            stmt.setInt(3, ticket.getId());
            int rows = stmt.executeUpdate();
            if (rows == 0) throw new SQLException("No borrow ticket found with id: " + ticket.getId());
        }
    }

    public void deleteBorrowTicket(int ticketId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setInt(1, ticketId);
            int rows = stmt.executeUpdate();
            if (rows == 0) throw new SQLException("No borrow ticket found with id: " + ticketId);
        }
    }

    private void validateBorrowTicket(BorrowTicket ticket) {
        if (ticket == null || ticket.getStudentId() <= 0 || ticket.getBookId() <= 0 ||
                ticket.getBorrowDate() == null || ticket.getDueDate() == null || ticket.getStatus() == null) {
            throw new IllegalArgumentException("Borrow ticket data is incomplete");
        }
    }

    private BorrowTicket mapResultSetToBorrowTicket(ResultSet rs) throws SQLException {
        LocalDate returnDate = rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null;
        return new BorrowTicket(
                rs.getInt("id"),
                rs.getInt("student_id"),
                rs.getInt("book_id"),
                rs.getDate("borrow_date").toLocalDate(),
                rs.getDate("due_date").toLocalDate(),
                returnDate,
                rs.getString("status"),
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null
        );
    }
}