
async function loadSellerDashboard() {

    const response = await fetch("LoadSellerDashboard");

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            console.log(json);

            let productCount = json.productList.length;
            let orderCount = json.ordersList.length;

            const order_history_report = document.getElementById("order-history-report");
            order_history_report.innerHTML = "";


            const order_history = document.getElementById("order-history");
            order_history.innerHTML = "";

            // Calculate total sales
            let totalSales = 0;
            json.orderItemsList.forEach(item => {
                // Get price from product
                let price = item.product.price || 0;
                // Get quantity from order item
                let qty = item.qty || 0;
                // Add to total
                totalSales += price * qty;
            });

            json.orderItemsList.forEach(orderItems => {

                let orderItemsData = `<tr>
                                                        <th scope="row">
                                                            
                                                            Order No : ${orderItems.orders.id}
                                                        </th>
                                                        <td class="text-start">${orderItems.orders.created_at}</td>
                                                        <td class="text-start">${orderItems.product.title}</td>
                                                        <td class="text-start">$ ${orderItems.product.price}</td>
                                                        <td class="text-start">${orderItems.qty}</td>
                                                        <td class="text-start">
                                                            <span class="badge badge-info">${orderItems.orderstatus.value}</span>
                                                        </td>
                                                    </tr>`;

                order_history_report.innerHTML += orderItemsData;

            });

            json.ordersList.forEach(orders => {

                let orderData = `<tr>
                                                        <th scope="row">
                                                            ${orders.id}
                                                        </th>
                                                        <td class="text-start">${orders.created_at}</td>
                                                        <td class="text-start">$ ${totalSales.toFixed(2)}</td>
                                                        <td class="text-start">${orders.address.user.email}</td>
                                                        
                                                    </tr>`;

                order_history.innerHTML += orderData;

            });



            document.getElementById("myproductcount").textContent = productCount;
            document.getElementById("myorderscount").textContent = orderCount;
            document.getElementById("totalSales").textContent = "$" + totalSales.toFixed(2);

        } else {

        }
    } else {

    }

}

async function loadMyProducts() {

    const response = await fetch("LoadSellerDashboard");

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            console.log(json);

            const my_products_report = document.getElementById("my-products-report");
            my_products_report.innerHTML = "";

            json.productList.forEach(products => {

                let statusClass = "";
                let statusText = products.status.value;
                let actionButton = "";

                if (statusText.toLowerCase() === 'active') {
                    statusClass = "badge-success"; // green badge
                    actionButton = `<button class="btn btn-sm btn-danger deactivate-btn" onclick="changeStatus(${products.id});">
                            Deactivate
                        </button>`;
                } else {
                    statusClass = "badge-danger"; // red badge
                    actionButton = `<button class="btn btn-sm btn-success activate-btn" onclick="changeStatus(${products.id});">
                            Activate
                        </button>`;
                }

                let productsData = `<tr>
                                                        <th scope="row">
                                                            ${products.id}
                                                        </th>
                                                        <td class="text-start">${products.title}</td>
                                                        <td class="text-start">$ ${products.price}</td>
                                                        <td class="text-start">${products.qty}</td>
                                                        <td class="text-start">${products.subcategory.category.name}</td>
                                                        <td class="text-start">${products.subcategory.name}</td>
                                                        <td class="text-start">${products.brand.name}</td>
                                                        <td class="text-start">${products.quality.value}</td>
                                                        <td class="text-start">
                                                            <span class="badge ${statusClass}">${statusText}</span>
                                                        </td>
                                                        <td class="text-start">
                                                            <button class="btn btn-sm mb-1 btn-primary update-btn" onclick="window.location.href='update-product.html?id=${products.id}'"  data-id="${products.id}">
                                                                Update
                                                            </button>
                                                            ${actionButton}
                                                        </td>
                                                    </tr>`;

                my_products_report.innerHTML += productsData;

            });

        }
    }

}


async function changeStatus(pid) {

    const response = await fetch("ChangeProductStatus?pid=" + pid);

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            console.log(json.message);
            window.location.reload();
            swal("", json.message, "success");
            
        } else {
            console.log(json.message);
            swal("", json.message, "error");
        }
    } else {
        console.log(json.message);
    }

}

async function loadUsers() {

    const response = await fetch("LoadSellerDashboard");

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            console.log(json);

            const users_report = document.getElementById("users-report");
            users_report.innerHTML = "";

            json.addressList.forEach(users => {

                let userData = `<tr>
                                                        <th scope="row">
                                                            ${users.user.id}
                                                        </th>
                                                        <td class="text-start">${users.user.first_name}</td>
                                                        <td class="text-start">${users.user.last_name}</td>
                                                        <td class="text-start">${users.user.email}</td>
                                                        <td class="text-start">${users.user.mobile}</td>
                                                        <td class="text-start">${users.lineOne} ${users.lineTwo}</td>
                                                        <td class="text-start">${users.city.name}</td>
                                                        <td class="text-start">${users.user.created_at}</td>
                                                        <td class="text-start">
                                                            <span class="badge badge-success">${users.user.verification}</span>
                                                        </td>
                                                    </tr>`;

                users_report.innerHTML += userData;

            });

        } else {

        }
    } else {

    }

}

async function loadIncomeReport() {
    const response = await fetch("LoadSellerDashboard");

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            console.log(json);

            const income_report = document.getElementById("incomereport");
            income_report.innerHTML = "";

            let totalAmt = 0;

            json.orderItemsList.forEach(orderEarnings => {

                let price = orderEarnings.product.price || 0;
                let qty = orderEarnings.qty || 0;
                let orderTotal = price * qty;

                totalAmt += orderTotal;

                let incomeData = `
                    <tr>
                        <th scope="row">Order No : ${orderEarnings.orders.id}</th>
                        <td class="text-start">${orderEarnings.orders.user.email}</td>
                        <td class="text-start">${orderEarnings.orders.created_at}</td>
                        <td class="text-start">${qty}</td>
                        <td class="text-start">$ ${orderTotal.toFixed(2)}</td>
                    </tr>
                `;

                income_report.innerHTML += incomeData;
            });

            income_report.innerHTML += `
                <tr style="font-weight: bold;">
                    <td colspan="4" class="text-end">Grand Total:</td>
                    <td class="text-start">$ ${totalAmt.toFixed(2)}</td>
                </tr>
            `;
        }
    }
}

function printTable() {
    // Get the table's HTML
    var tableContent = document.getElementById("Table").innerHTML;

    // Create a new window
    var printWindow = window.open('', '', 'width=900,height=650');

    // Write the table into the new window
    printWindow.document.write(`
        <html>
        <head>
            <title>GlamNest Statistics Report</title>
            <style>
                table { width: 100%; border-collapse: collapse; }
                th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
                th { background-color: #f4f4f4; }
            </style>
        </head>
        <body>
            ${tableContent}
        </body>
        </html>
    `);

    // Close the document to apply styles
    printWindow.document.close();

    // Trigger print
    printWindow.print();
}
