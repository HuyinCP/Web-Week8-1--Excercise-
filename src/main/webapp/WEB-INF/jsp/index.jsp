<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GraphQL Product Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .graphql-section {
            margin: 20px 0;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
        }
        .response-area {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            margin-top: 10px;
            max-height: 400px;
            overflow-y: auto;
        }
        .loading {
            display: none;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <h1 class="text-center my-4">
                    <i class="fas fa-shopping-cart"></i> GraphQL Product Management System
                </h1>
            </div>
        </div>

        <div class="row">
            <!-- Products Section -->
            <div class="col-md-4">
                <div class="graphql-section">
                    <h3><i class="fas fa-box"></i> Products</h3>
                    
                    <div class="mb-3">
                        <h5>Get All Products (Sorted by Price)</h5>
                        <button class="btn btn-primary" onclick="getAllProducts()">
                            <i class="fas fa-list"></i> Get All Products
                        </button>
                    </div>

                    <div class="mb-3">
                        <h5>Get Products by Category</h5>
                        <div class="input-group">
                            <input type="number" id="categoryId" class="form-control" placeholder="Category ID">
                            <button class="btn btn-info" onclick="getProductsByCategory()">
                                <i class="fas fa-search"></i> Search
                            </button>
                        </div>
                    </div>

                    <div class="mb-3">
                        <h5>Create Product</h5>
                        <form id="createProductForm">
                            <div class="mb-2">
                                <input type="text" id="productTitle" class="form-control" placeholder="Title" required>
                            </div>
                            <div class="mb-2">
                                <input type="number" id="productQuantity" class="form-control" placeholder="Quantity" required>
                            </div>
                            <div class="mb-2">
                                <input type="text" id="productDescription" class="form-control" placeholder="Description">
                            </div>
                            <div class="mb-2">
                                <input type="number" step="0.01" id="productPrice" class="form-control" placeholder="Price" required>
                            </div>
                            <div class="mb-2">
                                <input type="number" id="productUserId" class="form-control" placeholder="User ID" required>
                            </div>
                            <button type="submit" class="btn btn-success">
                                <i class="fas fa-plus"></i> Create Product
                            </button>
                        </form>
                    </div>

                    <div class="loading" id="productsLoading">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Loading...</span>
                        </div>
                    </div>

                    <div class="response-area" id="productsResponse"></div>
                </div>
            </div>

            <!-- Users Section -->
            <div class="col-md-4">
                <div class="graphql-section">
                    <h3><i class="fas fa-users"></i> Users</h3>
                    
                    <div class="mb-3">
                        <h5>Get All Users</h5>
                        <button class="btn btn-primary" onclick="getAllUsers()">
                            <i class="fas fa-list"></i> Get All Users
                        </button>
                    </div>

                    <div class="mb-3">
                        <h5>Create User</h5>
                        <form id="createUserForm">
                            <div class="mb-2">
                                <input type="text" id="userFullname" class="form-control" placeholder="Full Name" required>
                            </div>
                            <div class="mb-2">
                                <input type="email" id="userEmail" class="form-control" placeholder="Email" required>
                            </div>
                            <div class="mb-2">
                                <input type="password" id="userPassword" class="form-control" placeholder="Password" required>
                            </div>
                            <div class="mb-2">
                                <input type="text" id="userPhone" class="form-control" placeholder="Phone">
                            </div>
                            <button type="submit" class="btn btn-success">
                                <i class="fas fa-plus"></i> Create User
                            </button>
                        </form>
                    </div>

                    <div class="loading" id="usersLoading">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Loading...</span>
                        </div>
                    </div>

                    <div class="response-area" id="usersResponse"></div>
                </div>
            </div>

            <!-- Categories Section -->
            <div class="col-md-4">
                <div class="graphql-section">
                    <h3><i class="fas fa-tags"></i> Categories</h3>
                    
                    <div class="mb-3">
                        <h5>Get All Categories</h5>
                        <button class="btn btn-primary" onclick="getAllCategories()">
                            <i class="fas fa-list"></i> Get All Categories
                        </button>
                    </div>

                    <div class="mb-3">
                        <h5>Create Category</h5>
                        <form id="createCategoryForm">
                            <div class="mb-2">
                                <input type="text" id="categoryName" class="form-control" placeholder="Category Name" required>
                            </div>
                            <div class="mb-2">
                                <input type="text" id="categoryImages" class="form-control" placeholder="Images URL">
                            </div>
                            <button type="submit" class="btn btn-success">
                                <i class="fas fa-plus"></i> Create Category
                            </button>
                        </form>
                    </div>

                    <div class="loading" id="categoriesLoading">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Loading...</span>
                        </div>
                    </div>

                    <div class="response-area" id="categoriesResponse"></div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="alert alert-info text-center">
                    <h5><i class="fas fa-info-circle"></i> GraphQL Endpoints</h5>
                    <p class="mb-0">
                        <strong>GraphQL Playground:</strong> <a href="http://localhost:8080/graphiql" target="_blank">http://localhost:8080/graphiql</a> |
                        <strong>H2 Console:</strong> <a href="http://localhost:8080/h2-console" target="_blank">http://localhost:8080/h2-console</a>
                    </p>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="js/graphql-client.js"></script>
</body>
</html>
