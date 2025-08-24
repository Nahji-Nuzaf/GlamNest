/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

async function loadPurchaseHistory() {
//    console.log("Ok");

    const response = await fetch("LoadPurchaseHistory");

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            console.log(json);

            const order_card_container = document.getElementById("order-card-container");
            order_card_container.innerHTML = "";

            json.ordersList.forEach(orders => {

                let subtotal = 0;
                let totalQty = 0;
                let productrow = "";

                const orderItems = json.orderItemsList.filter(item => item.orders && item.orders.id === orders.id);

                const shipping = orderItems[0]?.deliverytype?.price;

                orderItems.forEach(orderItems => {

                    const itemTotal = orderItems.product.price * orderItems.qty;
                    subtotal += itemTotal;
                    totalQty += orderItems.qty;

                    productrow += `<tr>
                                                                        <td class="d-flex align-items-center gap-3">
                                                                            <img src="product-images\\${orderItems.product.id}\\image1.png" alt="Image"
                                                                                 class="product-img" />
                                                                            <div>
                                                                                <div>${orderItems.product.title}</div>
                                                                                <small class="text-muted">Category: ${orderItems.product.subcategory.category.name}</small>
                                                                            </div>
                                                                        </td>
                                                                        <td>$ ${orderItems.product.price}</td>
                                                                        <td>${orderItems.qty}</td>
                                                                        <td>$ ${itemTotal.toFixed(2)}</td>
                                                                    </tr>`;
                });

                const total = subtotal + shipping;

                let orderData = `<div class="p-4 mb-4" style="border: 1px solid; border-radius: 15px;">
                                    <div class="d-flex justify-content-between align-items-start flex-wrap gap-2">
                                        <div>
                                            <h5 class="mb-2">Order ID: <strong>${orders.id}</strong></h5>
                                            <p class="mb-0 text-muted " style="font-size: 15px;">
                                                Order Date: ${orders.created_at}
                                                
                                            </p>
                                        </div>
                                        <button class="btn btn-invoice">Invoice</button>
                                    </div>

                                        <!-- Accordion for Products -->
                                        <div class="accordion mt-4" id="productsAccordion-${orders.id}">
                                            <div class="accordion-item">
                                                <h2 class="accordion-header" id="heading-${orders.id}">
                                                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                                            data-bs-target="#collapse-${orders.id}" aria-expanded="true" aria-controls="collapse-${orders.id}">
                                                        View Products
                                                    </button>
                                                </h2>
                                                <div id="collapse-${orders.id}" class="accordion-collapse collapse show" aria-labelledby="heading-${orders.id}"
                                                     data-bs-parent="#productsAccordion-${orders.id}">
                                                    <div class="accordion-body p-2">
                                                        <div class="table-responsive">
                                                            <table class="table align-middle mb-0">
                                                                <thead>
                                                                    <tr>
                                                                        <th>Products</th>
                                                                        <th>Price</th>
                                                                        <th>Quantity</th>
                                                                        <th>Total Amount</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    ${productrow}
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                    <!-- Summary Section with Shipping Info -->
                                    <div class="summary-grid mt-4">
                                        <div class="shipping-details">
                                            <h6 style="color: #b20042; font-size: 20px;">Shipping Address</h6>
                                            <p class="mb-0 mt-1" style="font-size: 15px;">${orders.address.lineOne}<br />${orders.address.lineTwo}<br />${orders.address.city.name}<br />${orders.address.postalCode}</p>
                                        </div>

                                        <div class="summary-details">
                                            <div class="summary-line"><span class="text-muted">Subtotal:</span> <span>$ ${subtotal.toFixed(2)}</span></div>
                                            <div class="summary-line"><span class="text-muted">Shipping:</span> <span>$ ${shipping}</span></div>
                                            <hr />
                                            <div class="summary-line"><span class="text-total">Total:</span> <span
                                                    class="text-total">$ ${total.toFixed(2)}</span></div>
                                        </div>
                                    </div>
                                </div>`;

                order_card_container.innerHTML += orderData;

            });


        } else {
            console.log(json.message);
//            window.location = "sign-in.html";
        }
    } else {
        console.log(json.message);
    }

}
