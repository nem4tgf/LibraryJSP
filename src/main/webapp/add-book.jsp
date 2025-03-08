<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add New Book</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

    <style>
        body {
            padding: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1 class="mb-4">Add New Book</h1>

    <% if (request.getAttribute("error") != null) { %>
    <p class="alert alert-danger"><%= request.getAttribute("error") %></p>
    <% } %>

    <form action="books" method="post" class="mb-4">
        <input type="hidden" name="action" value="add">
        <div class="mb-3">
            <label for="bookCode" class="form-label">Book Code:</label>
            <input type="text" id="bookCode" name="bookCode" class="form-control" required>
        </div>
        <div class="mb-3">
            <label for="title" class="form-label">Title:</label>
            <input type="text" id="title" name="title" class="form-control" required>
        </div>
        <div class="mb-3">
            <label for="author" class="form-label">Author:</label>
            <input type="text" id="author" name="author" class="form-control">
        </div>
        <div class="mb-3">
            <label for="totalQuantity" class="form-label">Total Quantity:</label>
            <input type="number" id="totalQuantity" name="totalQuantity" class="form-control" min="0" required>
        </div>
        <div>
            <button type="submit" class="btn btn-primary">Add Book</button>
        </div>
    </form>

    <a href="books" class="btn btn-secondary">Back to Book List</a>
</div>

<!-- Bootstrap JS (Optional, if you need Bootstrap's JavaScript features) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>