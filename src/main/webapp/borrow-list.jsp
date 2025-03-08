<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Borrow Tickets</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        body { padding: 20px; }
    </style>
</head>
<body>
<div class="container">
    <h1 class="mb-4">Borrow Tickets</h1>
    <table class="table table-bordered table-striped">
        <thead class="table-light">
        <tr>
            <th>Ticket ID</th>
            <th>Student Name</th>
            <th>Book Title</th>
            <th>Borrow Date</th>
            <th>Due Date</th>
            <th>Return Date</th>
            <th>Status</th>
            <c:if test="${role == 'librarian'}"><th>Action</th></c:if>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="ticket" items="${tickets}">
            <tr>
                <td>${ticket.id}</td>
                <td>${studentNames[ticket.studentId]}</td>
                <td>${bookTitles[ticket.bookId]}</td>
                <td>${ticket.borrowDate}</td>
                <td>${ticket.dueDate}</td>
                <td>${ticket.returnDate != null ? ticket.returnDate : '-'}</td>
                <td>${ticket.status}</td>
                <c:if test="${role == 'librarian' && ticket.status == 'borrowed'}">
                    <td>
                        <form action="borrow" method="post" class="d-inline">
                            <input type="hidden" name="action" value="return">
                            <input type="hidden" name="ticketId" value="${ticket.id}">
                            <button type="submit" class="btn btn-success btn-sm" onclick="return confirm('Return this book?');">Return</button>
                        </form>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a href="${role == 'librarian' ? 'librarian.jsp' : 'student.jsp'}" class="btn btn-secondary">Back to Dashboard</a>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>