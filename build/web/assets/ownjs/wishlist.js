
//async function loadWishlistItems() {
//    try {
//        const response = await fetch("LoadWishlistItems");
//
//
//
//        if (response.ok) {
//            const json = await response.json(); // parse JSON no matter what
//            if (json.status) {
//
//
//
//            } else {
////                const wishlist_container = document.getElementById("wishlist-container");
////                wishlist_container.innerHTML = json.message;
//
//                console.log(json.message);
//            }
//        } else {
//            console.log(json.message || "Failed to load wishlist");
//        }
//    } catch (error) {
//        console.error("Error loading wishlist:", error);
//    }
//}


async function loadWishlistItems() {
    try {
        const response = await fetch("LoadWishlistItems");

        if (response.ok) {
            const json = await response.json();
            if (json.status) {


                console.log(json);

                const wishlist_container = document.getElementById("wishlist-container");
                wishlist_container.innerHTML = "";

                json.wishlistItems.forEach(wishlist => {

                    let wishlistData = `<tr class="product-row">
                                            <td>
                                                <button class="remove-btn" onclick="removeFromCart(${wishlist.product.id});">&times;</button>
                                            </td>
                                            <td class="d-flex align-items-center ps-4">
                                                <a href="single-product.html?id=${wishlist.product.id}"><img src="product-images\\${wishlist.product.id}\\image1.png" alt="Light Brown Sweter" class="product-img me-3"></a>
                                                <div class="product-info">
                                                    <div class="fw-semibold mb-0 fs-5">${wishlist.product.title}</div>
                                                    <small><span>Category: </span> <span>${wishlist.product.subcategory.category.name}</span></small><br>
                                                    <small><span>Sub Category: </span> <span>${wishlist.product.subcategory.name}</span></small>
                                                </div>
                                            </td>
                                            <td>IKEA</td>
                                            <td>$64.00</td>
                                            <td><span class="instock">${wishlist.product.qty} Units Instock</span></td>
                                            <td>
                                                <button class="add-cart-btn" onclick="addToCart(${wishlist.product.id},1);">Add to Cart</button>
                                            </td>
                                        </tr>`;
                    wishlist_container.innerHTML += wishlistData;

                });



            } else {
                console.log(json.message);
            }
        } else {
            console.log("Failed to load wishlist");
        }
    } catch (error) {
        console.error("Error loading wishlist:", error);
    }
}


async function addToCart(productId, qty) {

    const response = await fetch("AddToCart?prId=" + productId + "&qty=" + qty);
    if (response.ok) {
        const json = await response.json(); // await response.text();
        if (json.status) {
            console.log(json.message);
            swal("", json.message, "success");
        } else {
            console.log(json.message);

        }
    } else {
        console.log(json.message);
    }
}

//async function removeFromCart(productId) {
//
//    const response = await fetch("RemoveCartItems?pid=" + productId + "");
//
//    if (response.ok) {
//
//        const json = await response.json();
//
//        if (json.status) {
//
//            console.log(json.message);
//            swal("", json.message, "success");
//            window.location.reload();
//        } else {
//            console.log(json.message);
//        }
//    } else {
//        console.log(json.message);
//    }
//
//}

