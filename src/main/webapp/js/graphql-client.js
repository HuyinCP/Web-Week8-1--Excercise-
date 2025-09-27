// GraphQL Client for AJAX requests
const GRAPHQL_ENDPOINT = '/graphql';

// Utility function to make GraphQL requests
async function graphqlRequest(query, variables = {}) {
    try {
        const response = await fetch(GRAPHQL_ENDPOINT, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                query: query,
                variables: variables
            })
        });

        const result = await response.json();
        
        if (result.errors) {
            throw new Error(result.errors[0].message);
        }
        
        return result.data;
    } catch (error) {
        console.error('GraphQL request failed:', error);
        throw error;
    }
}

// Show/hide loading spinner
function showLoading(elementId) {
    document.getElementById(elementId).style.display = 'block';
}

function hideLoading(elementId) {
    document.getElementById(elementId).style.display = 'none';
}

// Display response data
function displayResponse(elementId, data, title) {
    const responseElement = document.getElementById(elementId);
    responseElement.innerHTML = `
        <h6>${title}</h6>
        <pre style="background-color: #f8f9fa; padding: 10px; border-radius: 5px; font-size: 12px;">${JSON.stringify(data, null, 2)}</pre>
    `;
}

// Product Functions
async function getAllProducts() {
    showLoading('productsLoading');
    
    const query = `
        query {
            allProducts {
                id
                title
                quantity
                description
                price
                user {
                    id
                    fullname
                    email
                }
            }
        }
    `;
    
    try {
        const data = await graphqlRequest(query);
        displayResponse('productsResponse', data.allProducts, 'All Products (Sorted by Price)');
    } catch (error) {
        document.getElementById('productsResponse').innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
    } finally {
        hideLoading('productsLoading');
    }
}

async function getProductsByCategory() {
    const categoryId = document.getElementById('categoryId').value;
    if (!categoryId) {
        alert('Please enter a category ID');
        return;
    }
    
    showLoading('productsLoading');
    
    const query = `
        query GetProductsByCategory($categoryId: ID!) {
            productsByCategory(categoryId: $categoryId) {
                id
                title
                quantity
                description
                price
                user {
                    id
                    fullname
                    email
                }
            }
        }
    `;
    
    try {
        const data = await graphqlRequest(query, { categoryId: parseInt(categoryId) });
        displayResponse('productsResponse', data.productsByCategory, `Products for Category ${categoryId}`);
    } catch (error) {
        document.getElementById('productsResponse').innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
    } finally {
        hideLoading('productsLoading');
    }
}

// User Functions
async function getAllUsers() {
    showLoading('usersLoading');
    
    const query = `
        query {
            allUsers {
                id
                fullname
                email
                phone
                categories {
                    id
                    name
                }
            }
        }
    `;
    
    try {
        const data = await graphqlRequest(query);
        displayResponse('usersResponse', data.allUsers, 'All Users');
    } catch (error) {
        document.getElementById('usersResponse').innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
    } finally {
        hideLoading('usersLoading');
    }
}

// Category Functions
async function getAllCategories() {
    showLoading('categoriesLoading');
    
    const query = `
        query {
            allCategories {
                id
                name
                images
                users {
                    id
                    fullname
                    email
                }
            }
        }
    `;
    
    try {
        const data = await graphqlRequest(query);
        displayResponse('categoriesResponse', data.allCategories, 'All Categories');
    } catch (error) {
        document.getElementById('categoriesResponse').innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
    } finally {
        hideLoading('categoriesLoading');
    }
}

// Create Product
async function createProduct(productData) {
    const mutation = `
        mutation CreateProduct($input: ProductInput!) {
            createProduct(input: $input) {
                id
                title
                quantity
                description
                price
                user {
                    id
                    fullname
                }
            }
        }
    `;
    
    try {
        const data = await graphqlRequest(mutation, { input: productData });
        displayResponse('productsResponse', data.createProduct, 'Product Created Successfully');
        return data.createProduct;
    } catch (error) {
        document.getElementById('productsResponse').innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
        throw error;
    }
}

// Create User
async function createUser(userData) {
    const mutation = `
        mutation CreateUser($input: UserInput!) {
            createUser(input: $input) {
                id
                fullname
                email
                phone
            }
        }
    `;
    
    try {
        const data = await graphqlRequest(mutation, { input: userData });
        displayResponse('usersResponse', data.createUser, 'User Created Successfully');
        return data.createUser;
    } catch (error) {
        document.getElementById('usersResponse').innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
        throw error;
    }
}

// Create Category
async function createCategory(categoryData) {
    const mutation = `
        mutation CreateCategory($input: CategoryInput!) {
            createCategory(input: $input) {
                id
                name
                images
            }
        }
    `;
    
    try {
        const data = await graphqlRequest(mutation, { input: categoryData });
        displayResponse('categoriesResponse', data.createCategory, 'Category Created Successfully');
        return data.createCategory;
    } catch (error) {
        document.getElementById('categoriesResponse').innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
        throw error;
    }
}

// Form event listeners
document.addEventListener('DOMContentLoaded', function() {
    // Product form
    document.getElementById('createProductForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const productData = {
            title: document.getElementById('productTitle').value,
            quantity: parseInt(document.getElementById('productQuantity').value),
            description: document.getElementById('productDescription').value,
            price: parseFloat(document.getElementById('productPrice').value),
            userId: parseInt(document.getElementById('productUserId').value)
        };
        
        showLoading('productsLoading');
        try {
            await createProduct(productData);
            this.reset();
        } finally {
            hideLoading('productsLoading');
        }
    });
    
    // User form
    document.getElementById('createUserForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const userData = {
            fullname: document.getElementById('userFullname').value,
            email: document.getElementById('userEmail').value,
            password: document.getElementById('userPassword').value,
            phone: document.getElementById('userPhone').value
        };
        
        showLoading('usersLoading');
        try {
            await createUser(userData);
            this.reset();
        } finally {
            hideLoading('usersLoading');
        }
    });
    
    // Category form
    document.getElementById('createCategoryForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const categoryData = {
            name: document.getElementById('categoryName').value,
            images: document.getElementById('categoryImages').value
        };
        
        showLoading('categoriesLoading');
        try {
            await createCategory(categoryData);
            this.reset();
        } finally {
            hideLoading('categoriesLoading');
        }
    });
});
