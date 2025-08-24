
async function loadData() {

    const response = await fetch("LoadData");
    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            console.log(json);
            document.getElementById("all-item-count").innerHTML = json.allProductCount;
            loadOptionst("category", json.categoryList, "name");
            loadOptionst("brand", json.brandList, "name");
            loadOptionst("material", json.materialList, "value");
            loadOptions("condition", json.qualityList, "value");
            loadOptions("style", json.styleList, "value");

            updateProductView(json);

        } else {
            window.location = "index.html";
        }
    } else {
        window.location = "index.html";
    }
}

function loadOptions(prefix, dataList, property) {

    let options = document.getElementById(prefix + "-options");
    let li = document.getElementById(prefix + "-li");
    options.innerHTML = "";

    dataList.forEach(item => {
        let li_clone = li.cloneNode(true);

        li_clone.querySelector("#" + prefix + "-a").innerHTML = item[property];
        options.appendChild(li_clone);
    });

    const all_links = document.querySelectorAll("#" + prefix + "-options span a");

    all_links.forEach(link => {
        link.addEventListener("click", function (e) {
            e.preventDefault(); // prevent actual link navigation

            // Remove 'chosen' from all
            all_links.forEach(l => l.parentElement.classList.remove("chosen"));

            // Add 'chosen' to the clicked one
            this.parentElement.classList.add("chosen");
        });
    });

}

function loadOptionst(prefix, dataList, property) {
    let options = document.getElementById(prefix + "-options");
    let templateLi = document.getElementById(prefix + "-li");
    options.innerHTML = "";

    dataList.forEach((item, index) => {
        let liClone = templateLi.cloneNode(true);
        liClone.style.display = "block"; // make it visible
        liClone.removeAttribute("id");   // remove duplicate ID

        const checkbox = liClone.querySelector('input[type="checkbox"]');
        checkbox.value = item[property];
        checkbox.id = `${prefix}-${index}`;

        const labelText = liClone.querySelector(".label-text");
        labelText.innerText = item[property];

        // Optional: make label's `for` match input ID if not wrapping
        // liClone.querySelector("label").setAttribute("for", checkbox.id);

        options.appendChild(liClone);
    });

    const all_inputs = document.querySelectorAll("#" + prefix + "-options li label input");

    all_inputs.forEach(input => {
        input.addEventListener("click", function () {
            // Uncheck all others and remove 'chosen' class
            all_inputs.forEach(i => {
                if (i !== this) {
                    i.checked = false;
                    i.closest("li").classList.remove("chosen");
                }
            });

            // Toggle 'chosen' class for the selected one
            if (this.checked) {
                this.closest("li").classList.add("chosen");
            } else {
                this.closest("li").classList.remove("chosen");
            }
        });
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

async function searchProduct(firstResult) {
    const category_name = document.getElementById("category-options")
            .querySelector(".chosen")?.querySelector("input")?.value;
    const brand_name = document.getElementById("brand-options")
            .querySelector(".chosen")?.querySelector("input")?.value;
    const material_name = document.getElementById("material-options")
            .querySelector(".chosen")?.querySelector("input")?.value;
    const condition_name = document.getElementById("condition-options")
            .querySelector(".chosen")?.querySelector("a").innerHTML;
    const style_name = document.getElementById("style-options")
            .querySelector(".chosen")?.querySelector("a").innerHTML;

    const minPrice = document.getElementById("minPrice").value;
    const maxPrice = document.getElementById("maxPrice").value;
    const basicSearch = document.getElementById("basicsearch").value;

    const data = {
        firstResult: firstResult,
        categoryName: category_name,
        brandName: brand_name,
        materialName: material_name,
        conditionName: condition_name,
        styleName: style_name,
        minPrice: minPrice,
        maxPrice: maxPrice,
        basicSearch: basicSearch
    };

    const dataJSON = JSON.stringify(data);

    const response = await fetch("SearchProducts",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: dataJSON
            });


    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            console.log(json);
            updateProductView(json);
//            swal("", "Product Loading Complete...", "success");

        } else {
            swal("", "Somthing went wrong. Please try again later", "error");
//            console.log(json.message);

        }
    } else {
        swal("", "Somthing went wrong. Please try again later", "error");
//        console.log(json.message);
    }

}

const search_product = document.getElementById("search-product"); // product card parent node
let search_pagination_button = document.getElementById("search-pagination-button");
let current_page = 0;

function updateProductView(json) {

    const product_container = document.getElementById("search-product-container");
    product_container.innerHTML = "";

    json.productList.forEach(product => {
        let search_product_clone = search_product.cloneNode(true);// enable child nodes cloning / allow child nodes
        search_product_clone.querySelector("#search-product-a1").href = "single-product.html?id=" + product.id;
        search_product_clone.querySelector("#search-product-img1").src = "product-images//" + product.id + "//image1.png";
        search_product_clone.querySelector("#search-product-add-to-cart").addEventListener(
                "click", (e) => {
            addToCart(product.id, 1);
            e.preventDefault();
        });
        search_product_clone.querySelector("#search-product-a2").href = "single-product.html?id=" + product.id;
        search_product_clone.querySelector("#search-product-title-1").innerHTML = product.title;
        search_product_clone.querySelector("#search-product-price-1").innerHTML = "$" + new Intl.NumberFormat(
                "en-US",
                {minimumFractionDigits: 2})
                .format(product.price);
        ;
        //append child
        product_container.appendChild(search_product_clone);
    });

    let search_pagination_container = document.getElementById("search-pagination-container");
    search_pagination_container.innerHTML = "";

    let all_product_count = json.allProductCount;
    document.getElementById("all-item-count").innerHTML = all_product_count;
    let product_per_page = 2;
    let pages = Math.ceil(all_product_count / product_per_page); // round upper integer 

    //previous-button
    if (current_page !== 0) {
        let search_pagination_button_prev_clone = search_pagination_button.cloneNode(true);
        search_pagination_button_prev_clone.innerHTML = "<<";
        search_pagination_button_prev_clone.addEventListener(
                "click", (e) => {
            current_page--;
            searchProduct(current_page * product_per_page);
            e.preventDefault();
        });
        search_pagination_container.appendChild(search_pagination_button_prev_clone);
    }

    // pagination-buttons
    for (let i = 0; i < pages; i++) {
        let search_pagination_button_clone = search_pagination_button.cloneNode(true);
        search_pagination_button_clone.innerHTML = i + 1;
        search_pagination_button_clone.addEventListener(
                "click", (e) => {
            current_page = i;
            searchProduct(i * product_per_page);
            e.preventDefault();
        });

        if (i === Number(current_page)) {
            search_pagination_button_clone.className = "active";
        } else {
            search_pagination_button_clone.className = "";
        }
        search_pagination_container.appendChild(search_pagination_button_clone);
    }
    
    // next-button
    if (current_page !== (pages - 1)) {
        let search_pagination_button_next_clone = search_pagination_button.cloneNode(true);
        search_pagination_button_next_clone.innerHTML = ">>";
        search_pagination_button_next_clone.addEventListener(
                "click", (e) => {
            current_page++;
            searchProduct(current_page * product_per_page);
            e.preventDefault();
        });
        search_pagination_container.appendChild(search_pagination_button_next_clone);
    }

}