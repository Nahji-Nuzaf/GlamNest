

async function loadCartItems() {

    const response = await fetch("LoadCartItems");

    if (response.ok) {

        const json = await response.json();

        if (json.status) {

            const cart_item_container = document.getElementById("cart-item-container");
            cart_item_container.innerHTML = "";

            let total = 0;
            let totalQty = 0;

            json.cartItems.forEach(cart => {

                let productSubtotal = cart.product.price * cart.qty;
                total += productSubtotal;
                totalQty += cart.qty;

                let tableData = `<tr id="cart-item-row">
                                        <td class="product__cart__item">
                                            <div class="product__cart__item__pic">
                                                <a href="single-product.html?id=${cart.product.id}"><img src="product-images\\${cart.product.id}\\image1.png" alt=""></a>
                                            </div>
                                            <div class="product__cart__item__text">
                                                <h6>${cart.product.title}</h6>
                                                <h5>$ ${new Intl.NumberFormat("en-US",
                        {minimumFractionDigits: 2})
                        .format(cart.product.price)}</h5>
                                            </div>
                                        </td>
                                        <td class="quantity__item">
                                            <div class="input-group" style="width: 120px;">
                                                
                                                <input type="text" class="form-control text-center" disabled id="qty" value="${cart.qty}"
                                                       style="height: 42px;">
                                                
                                            </div>
                                        </td>
                                        <td class="cart__price">$ ${new Intl.NumberFormat("en-US",
                        {minimumFractionDigits: 2})
                        .format(productSubtotal)}</td>
                                        <td class="cart__close" onclick="removeFromCart(${cart.product.id});"><img style="cursor: pointer;" src="vendor/img/icon/close.png"/></td>
                                    </tr>`;
                cart_item_container.innerHTML += tableData;

            });

            document.getElementById("order-total-amount").innerHTML = `$ ${new Intl.NumberFormat("en-US",
                    {minimumFractionDigits: 2})
                    .format(total)}`;


        } else {

            const cart_container = document.getElementById("shopping-cart-container");
            cart_container.innerHTML = json.message;

//            console.log(json.message);
        }

    } else {
        console.log("Cart Items Loading failed");
    }

}

async function removeFromCart(productId) {

    const response = await fetch("RemoveCartItems?pid="+productId+"");

    if (response.ok) {

        const json = await response.json();

        if (json.status) {

            console.log(json.message);
            swal("", json.message, "success");
            window.location.reload();
        }else{
            console.log(json.message);
        }
    }else{
        console.log(json.message);
    }

}