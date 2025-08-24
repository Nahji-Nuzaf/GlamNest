
async function loadData() {

    const searchParams = new URLSearchParams(window.location.search);

//    console.log(searchParams);

    if (searchParams.has("id")) {
//        console.log("Ok");
        const productId = searchParams.get("id");
        const response = await fetch("LoadSingleProduct?id=" + productId);
//    
        if (response.ok) {
            const json = await response.json();
            if (json.status) {
                console.log(json);

                document.getElementById("mainImage").src = "product-images\\" + json.product.id + "\\image1.png";
                document.getElementById("image1").src = "product-images\\" + json.product.id + "\\image1.png";
                document.getElementById("image2").src = "product-images\\" + json.product.id + "\\image2.png";
                document.getElementById("image3").src = "product-images\\" + json.product.id + "\\image3.png";
//
                document.getElementById("procategory").innerHTML = json.product.subcategory.category.name;
                document.getElementById("product-title").innerHTML = json.product.title;
                document.getElementById("productprice").innerHTML = "$" + new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(json.product.price);
                document.getElementById("quantity").innerHTML = json.product.qty + " Units In Stock";
                document.getElementById("description1").innerHTML = json.product.description;
                document.getElementById("color1").innerHTML = json.product.color;
                document.getElementById("pro-brand").innerHTML = json.product.brand.name;
                document.getElementById("pro-subCat").innerHTML = json.product.subcategory.name;
                document.getElementById("description2").innerHTML = json.product.description;
                document.getElementById("published-on").innerHTML = json.product.created_at;
                document.getElementById("sub-category").innerHTML = json.product.subcategory.name;
                document.getElementById("brand").innerHTML = json.product.brand.name;
                document.getElementById("color2").innerHTML = json.product.color;
                document.getElementById("seating-capacity").innerHTML = json.product.seating_capacity;
                document.getElementById("material").innerHTML = json.product.material.value;
                document.getElementById("product-style").innerHTML = json.product.style.value;
                document.getElementById("overall-shape").innerHTML = json.product.overall_shape;
                document.getElementById("arm-style").innerHTML = json.product.arm_style.value;
                document.getElementById("condition").innerHTML = json.product.quality.value;
                document.getElementById("warranty").innerHTML = json.product.warranty.duration;
//
                const addToCartMain = document.getElementById("add-to-cart-main");
                addToCartMain.addEventListener("click", (e) => {
                    addToCart(json.product.id, document.getElementById("add-to-cart-qty").value);
                    e.preventDefault();
                });
//
                let similar_products_main = document.getElementById("similar-products-main");
                let productcard = document.getElementById("similar-product");
                similar_products_main.innerHTML = "";
                json.productList.forEach(item => {

                    let productCloneHtml = productcard.cloneNode(true);
                    
                    
                    const link = productCloneHtml.querySelector("#similar-product-link");
                    link.href = "single-product.html?id=" + item.id;
                    
                    console.log(productCloneHtml.querySelector("#similar-product-link"));
                    
                    productCloneHtml.querySelector("#similar-product-image").src = "product-images\\" + item.id + "\\image1.png";
                    productCloneHtml.querySelector("#similar-product-add-to-cart").addEventListener(
                            "click", (e) => {
                        addToCart(item.id, 1);
                        e.preventDefault();
                    });
                    productCloneHtml.querySelector("#similar-product-quick").href = "single-product.html?id=" + item.id;
                    productCloneHtml.querySelector("#similar-product-title").innerHTML = item.title;
                    productCloneHtml.querySelector("#similar-product-price").innerHTML = "$ " + new Intl.NumberFormat(
                            "en-US",
                            {minimumFractionDigits: 2})
                            .format(item.price);
                    ;

                    
                    similar_products_main.appendChild(productCloneHtml);
                });

            } else {
                window.location = "index.html";
            }
        } else {
            window.location = "sign-in.html";
        }
//    
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
            swal("", json.message, "error");
        }
    } else {
        console.log(json.message);
    }
}