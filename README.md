# API for web application [Shop](https://github.com/rbrahinets/shop-frontend)

## Explore Rest APIs

The app defines following CRUD APIs.

### Auth

| Method | Url             | Description | Sample Valid Request Body | 
|--------|-----------------|-------------|---------------------------|
| POST   | /api/v1/sign-up | Sign up     | [JSON](#signup)           |
| POST   | /api/v1/sign-in | Sign in     |                           |

### Admin Number

| Method | Url                             | Description              | Sample Valid Request Body   |
|--------|---------------------------------|--------------------------|-----------------------------|
| GET    | /api/v1/admins-numbers          | Get all admins numbers   |                             |
| GET    | /api/v1/admins-numbers/{id}     | Get admins number by id  |                             |
| POST   | /api/v1/admins-numbers          | Create new admins number | [JSON](#adminsnumbercreate) |
| PUT    | /api/v1/admins-numbers/{id}     | Update admins number     | [JSON](#adminsnumberupdate) |
| DELETE | /api/v1/admins-numbers/{number} | Delete admins number     |                             |

### Cart

| Method | Url                | Description     | Sample Valid Request Body |
|--------|--------------------|-----------------|---------------------------|
| GET    | /api/v1/carts      | Get all carts   |                           |
| GET    | /api/v1/carts/{id} | Get cart by id  |                           |
| POST   | /api/v1/carts      | Create new cart | [JSON](#cartcreate)       |
| PUT    | /api/v1/carts/{id} | Update cart     | [JSON](#cartupdate)       |
| DELETE | /api/v1/carts/{id} | Delete cart     |                           |

### Category

| Method | Url                       | Description         | Sample Valid Request Body |
|--------|---------------------------|---------------------|---------------------------|
| GET    | /api/v1/categories        | Get all categories  |                           |
| GET    | /api/v1/categories/{id}   | Get category by id  |                           |
| POST   | /api/v1/categories        | Create new category | [JSON](#categorycreate)   |
| PUT    | /api/v1/categories/{id}   | Update category     | [JSON](#categoryupdate)   |
| DELETE | /api/v1/categories/{name} | Delete category     |                           |

### Main

| Method | Url     | Description              | Sample Valid Request Body |
|--------|---------|--------------------------|---------------------------|
| GET    | /api/v1 | Get some random products |                           |

### Payment

| Method | Url             | Description     | Sample Valid Request Body |
|--------|-----------------|-----------------|---------------------------|
| POST   | /api/v1/payment | Process payment | [JSON](#payment)          |

### Product

| Method | Url                         | Description          | Sample Valid Request Body |
|--------|-----------------------------|----------------------|---------------------------|
| GET    | /api/v1/products            | Get all products     |                           |
| GET    | /api/v1/products/{id}       | Get product by id    |                           |
| POST   | /api/v1/products            | Create new product   | [JSON](#productcreate)    |
| PUT    | /api/v1/products/{id}       | Update product       | [JSON](#productupdate)    |
| DELETE | /api/v1/products/{barcode}  | Delete product       |                           |
| GET    | /api/v1/products/image/{id} | Get image of product |                           |

### Product-Category

| Method | Url                                         | Description                  | Sample Valid Request Body         |
|--------|---------------------------------------------|------------------------------|-----------------------------------|
| GET    | /api/v1/product-category/{id}               | Get all products in category |                                   |
| GET    | /api/v1/product-category/category/{barcode} | Get category for product     |                                   |
| POST   | /api/v1/product-category                    | Add products to category     | [JSON](#addproductstocategory)    |
| PUT    | /api/v1/product-category                    | Update category for product  | [JSON](#updatecategoryforproduct) |
| DELETE | /api/v1/product-category/{barcode}          | Delete product from category |                                   |

### Report

| Method | Url            | Description     | Sample Valid Request Body |
|--------|----------------|-----------------|---------------------------|
| POST   | /api/v1/report | Download report | [JSON](#report)           |

### User

| Method | Url                  | Description     | Sample Valid Request Body |
|--------|----------------------|-----------------|---------------------------|
| GET    | /api/v1/users        | Get all users   |                           |
| GET    | /api/v1/users/{id}   | Get user by id  |                           |
| POST   | /api/v1/users        | Create new user | [JSON](#usercreate)       |
| PUT    | /api/v1/users/{id}   | Update user     | [JSON](#userupdate)       |
| DELETE | /api/v1/users/{name} | Delete user     |                           |

### User-Role

| Method | Url                    | Description          | Sample Valid Request Body  |
|--------|------------------------|----------------------|----------------------------|
| GET    | /api/v1/user-role/{id} | Get role for user by |                            |
| POST   | /api/v1/user-role      | Add role for user    | [JSON](#addroleforuser)    |
| PUT    | /api/v1/user-role/{id} | Update role for user | [JSON](#updateroleforuser) |

## Sample Valid JSON Request Bodies

##### <a id="signup">Sign Up -> /api/v1/sign-up</a>

```json
{
    "firstName": "John",
    "lastName": "Smith",
    "email": "test@email.com",
    "password": "password",
    "admin_number": ""
}
```

##### <a id="adminsnumbercreate">Create New Admins Number -> /api/v1/admins-numbers</a>

```json
{
    "admin_number": "12345678"
}
```

##### <a id="adminsnumberupdate">Update Admins Number -> /api/v1/admins-numbers/{id}</a>

```json
{
    "admin_number": "12345678"
}
```

##### <a id="cartcreate">Create New Cart -> /api/v1/carts</a>

```json
{
    "price_amount": 0,
    "user_id": 1
}
```

##### <a id="cartupdate">Update Cart -> /api/v1/carts/{id}</a>

```json
{
    "price_amount": 0,
    "user_id": 1
}
```

##### <a id="categorycreate">Create New Category -> /api/v1/categories</a>

```json
{
    "name": "name"
}
```

##### <a id="categoryupdate">Update Category -> /api/v1/categories/{id}</a>

```json
{
    "name": "name"
}
```

##### <a id="payment">Process Payment -> /api/v1/payment</a>

```json
{
    "price_amount": 0,
    "cart_number": "4242424242424242",
    "cart_expiry": "01/29",
    "cart_cvc": "123"
}
```

##### <a id="productcreate">Create New Product -> /api/v1/products</a>

```json
{
    "name": "name",
    "describe": "describe",
    "price": 0,
    "barcode": "barcode",
    "in_stock": true,
    "image": [
        1,
        1,
        1
    ]
}
```

##### <a id="productupdate">Update Product -> /api/v1/products/{id}</a>

```json
{
    "name": "name",
    "describe": "describe",
    "price": 0,
    "barcode": "barcode",
    "in_stock": true,
    "image": [
        1,
        1,
        1
    ]
}
```

##### <a id="addproductstocategory">Add Products To Category -> /api/v1/product-category</a>

```json
{
    "product_id": 1,
    "category_id": 1
}
```

##### <a id="updatecategoryforproduct">Update Category For Product -> /api/v1/product-category</a>

```json
{
    "product_id": 1,
    "category_id": 1
}
```

##### <a id="report">Download Report -> /api/v1/report</a>

```json
{
    "products": [],
    "price_amount": 0
}
```

##### <a id="usercreate">Create New User -> /api/v1/users</a>

```json
{
    "firstName": "John",
    "lastName": "Smith",
    "email": "test@email.com",
    "password": "password",
    "admin_number": ""
}
```

##### <a id="userupdate">Update User -> /api/v1/users/{id}</a>

```json
{
    "firstName": "John",
    "lastName": "Smith",
    "email": "test@email.com",
    "password": "password",
    "admin_number": ""
}
```

##### <a id="addroleforuser">Add Role For User -> /api/v1/user-role</a>

```json
{
    "user_id": 1,
    "role_id": 1
}
```

##### <a id="updateroleforuser">Update Role For User -> /api/v1/user-role/{id}</a>

```json
{
    "user_id": 1,
    "role_id": 1
}
```
