<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Dashboard</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <!-- Optional: Custom CSS -->
    <style>
        body {
            padding: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1 class="mb-4">Welcome, Student!</h1>
    <p class="alert alert-info">Hello, ${sessionScope.user.fullName}</p>

    <% if (request.getAttribute("error") != null) { %>
    <p class="alert alert-danger"><%= request.getAttribute("error") %></p>
    <% } %>

    <h2 class="mt-4">Borrow a Book</h2>
    <form action="borrow" method="post" class="mb-4">
        <input type="hidden" name="action" value="borrow">
        <div class="mb-3">
            <label for="bookId" class="form-label">Select a Book:</label>
            <select id="bookId" name="bookId" class="form-select" required>
                <option value="">-- Choose a book --</option>
                <c:forEach var="book" items="${books}">
                    <c:if test="${book.availableQuantity > 0}">
                        <option value="${book.id}">${book.title} by ${book.author} (Available: ${book.availableQuantity})</option>
                    </c:if>
                </c:forEach>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Borrow</button>
    </form>

    <h2 class="mt-4">Available Books</h2>
    <table class="table table-bordered table-striped">
        <thead class="table-light">
        <tr>
            <th>Title</th>
            <th>Author</th>
            <th>Total Quantity</th>
            <th>Available Quantity</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="book" items="${books}">
            <tr>
                <td>${book.title}</td>
                <td>${book.author}</td>
                <td>${book.totalQuantity}</td>
                <td>${book.availableQuantity}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <ul class="list-group list-group-flush">
        <li class="list-group-item"><a href="borrow?action=list" class="text-decoration-none">View Borrow History</a></li>
        <li class="list-group-item"><a href="auth?logout=true" class="text-decoration-none">Logout</a></li>
    </ul>
</div>

<!-- Bootstrap JS (Optional, if you need Bootstrap's JavaScript features) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>