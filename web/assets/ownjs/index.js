function indexOnloadFunctions() {
    checkSessionCart();
    loadProductData();
}

async function checkSessionCart() {

    const response = await fetch("CheckSessionCart");
    if (!response.ok) {
        console.log(message);
    }

}

async function loadProductData() {
    const response = await fetch("LoadHomeData");

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            console.log(json);
            loadHomeProducts(json);

//            const userName = document.getElementById("userName");

//            if (json.user && json.user.name) {
//                userName.innerHTML = "Sign in | Register";
//            } else {
//                userName.innerHTML = "Welcome " + json.user.name;

//            }

        } else {
            console.log(json.message);
        }
    } else {
        console.log(json.message);
    }
}

function loadHomeProducts(json) {
    const new_arrival_product_container = document.getElementById("new-arrival-product-container");
    new_arrival_product_container.innerHTML = "";

    json.productList.forEach(item => {
        let product_card = `<div class="col-lg-3 col-md-6 col-sm-6 col-md-6 col-sm-6 mix new-arrivals">
                        <div class="product__item">
                            <div class="product__item__pic set-bg" data-setbg=""
                                 style="border-radius: 7px;">
                                 <a href="single-product.html?id=${item.id}"><img src="product-images\\${item.id}\\image1.png" /></a>
                                <span class="label">New</span>
                                <ul class="product__hover">
                                    <li><a  onclick="addToWishlist(${item.id});" style="cursor: pointer;"><img src="vendor/img/icon/heart.png" alt=""> <span>Wishlist</span></a></li>
                                    <li><a href="#" onclick="addToCart(${item.id},1);"><img src="vendor/img/icon/cart.png" alt=""> <span>Add to Cart</span></a></li>
                                    <li><a href="single-product.html?id=${item.id}"><img src="vendor/img/icon/eye.png" alt=""> <span>View Product</span></a></li>
                                </ul>
                            </div>
                            <div class="product__item__text">
                                <h6>${item.title}</h6>
                                <a onclick="addToCart(${item.id},1);" style="cursor: pointer;" class="add-cart">+ Add To Cart</a>

                                <h5 class="mt-3">$ ${new Intl.NumberFormat(
                "en-US",
                {minimumFractionDigits: 2})
                .format(item.price)}</h5>
                                
                            </div>
                        </div>
                    </div>`;
        new_arrival_product_container.innerHTML += product_card;
    });
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

async function addToWishlist(pid) {

    const response = await fetch("AddToWishlist?pid=" + pid);

    if (response.ok) {

        const json = await response.json();

        if (json.status) {
            console.log(json.message);
            swal("", json.message, "success");
        } else {
            console.log(json.message);
            swal("", json.message, "error");
        }

    } else {
        console.log(json.message);
    }

}