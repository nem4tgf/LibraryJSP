<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Librarian Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        body { padding: 20px; }
    </style>
</head>
<body>
<div class="container">
    <h1 class="mb-4">Welcome, Librarian!</h1>
    <p class="alert alert-info">Hello, ${sessionScope.user.fullName}</p>
    <ul class="list-group list-group-flush">
        <li class="list-group-item"><a href="books" class="text-decoration-none">Manage Books</a></li>
        <li class="list-group-item"><a href="users" class="text-decoration-none">Manage Users</a></li>
        <li class="list-group-item"><a href="borrow?action=list" class="text-decoration-none">Manage Borrow Tickets</a></li>
        <li class="list-group-item"><a href="auth?logout=true" class="text-decoration-none">Logout</a></li>
    </ul>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>