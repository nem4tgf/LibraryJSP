<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        body { padding: 20px; }
    </style>
</head>
<body>
<div class="container">
    <h1 class="mb-4">Book List</h1>
    <a href="add-book.jsp" class="btn btn-primary mb-3">Add New Book</a>
    <table class="table table-bordered table-striped">
        <thead class="table-light">
        <tr>
            <th>Book Code</th>
            <th>Title</th>
            <th>Author</th>
            <th>Total Quantity</th>
            <th>Available Quantity</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="book" items="${books}">
            <tr>
                <td>${book.bookCode}</td>
                <td>${book.title}</td>
                <td>${book.author}</td>
                <td>${book.totalQuantity}</td>
                <td>${book.availableQuantity}</td>
                <td>
                    <form action="books" method="post" class="d-inline">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="bookId" value="${book.id}">
                        <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure?');">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a href="librarian.jsp" class="btn btn-secondary">Back to Librarian Dashboard</a>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>