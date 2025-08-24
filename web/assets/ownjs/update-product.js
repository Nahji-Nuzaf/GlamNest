function loadData() {
//    loadProductData();
    loadUpdateProduct();
}

var subcategoryList;

async function loadProductData() {

    console.log("OK");

    const response = await fetch("LoadProductData");

    if (response.ok) {
        const json = await response.json();

        if (json.status) {//if true

            loadSelect("category", json.categoryList, "name");
            subcategoryList = json.subcategoryList;
            loadSelect("subcategory", json.subcategoryList, "name");
            loadSelect("brand", json.brandList, "name");
            loadSelect("promaterial", json.materialList, "value");
            loadSelect("prostyle", json.styleList, "value");
            loadSelect("armStyle", json.armstyleList, "value");
            loadSelect("proQuality", json.qualityList, "value");
            loadSelect("warranty", json.warrantyList, "duration");
            return true;  // Indicate success

        } else {//when false
            //custom message
            document.getElementById("message").innerHTML = "Unable to load Product. Please try again later";
            return false;
        }
    } else {
        document.getElementById("message").innerHTML = "Unable to load";
        return false;
    }

}



function loadSelect(selectId, list, property) {

    const select = document.getElementById(selectId);

    list.forEach(item => {

        const option = document.createElement("option");
        option.value = item.id;
        option.innerHTML = item[property];
        select.appendChild(option);

    });

}




async function loadUpdateProduct() {

    const dropdownsLoaded = await loadProductData();
    if (!dropdownsLoaded)
        return; // stop if failed

    const Params = new URLSearchParams(window.location.search);

    if (Params.has("id")) {
        const proId = Params.get("id");
        const response = await fetch("LoadUpdateProduct?id=" + proId);

        if (response.ok) {
            const json = await response.json();
            if (json.status) {
                console.log(json);

                document.getElementById("img1-preview").src = "product-images/" + json.product.id + "/image1.png";
                document.getElementById("img2-preview").src = "product-images/" + json.product.id + "/image2.png";
                document.getElementById("img3-preview").src = "product-images/" + json.product.id + "/image3.png";


                document.getElementById("title").value = json.product.title;
                document.getElementById("description").value = json.product.description;
                document.getElementById("price").value = json.product.price;
                document.getElementById("qty").value = json.product.qty;
                document.getElementById("qty").value = json.product.qty;
                document.getElementById("color").value = json.product.color;
                document.getElementById("warranty").value = json.product.warranty.id;
                document.getElementById("category").value = json.product.subcategory.category.id;
                document.getElementById("subcategory").value = json.product.subcategory.id;
                document.getElementById("brand").value = json.product.brand.id;
                document.getElementById("promaterial").value = json.product.material.id;
                document.getElementById("prostyle").value = json.product.style.id;
                document.getElementById("overallshape").value = json.product.overall_shape;
                document.getElementById("armStyle").value = json.product.arm_style.id;
                document.getElementById("seating").value = json.product.seating_capacity;
                document.getElementById("proQuality").value = json.product.quality.id;
                document.getElementById("assembly").value = json.product.assembly;

                const updateProductbtn = document.getElementById("update-product-buttton");
                updateProductbtn.addEventListener("click", (e) => {
                    updateProduct(json.product.id);
                    e.preventDefault();
                });

            } else {
                console.log(json);
            }
        } else {
            console.log(json);
        }
    }


}

async function updateProduct(pid) {

    const title = document.getElementById("title").value;
    const description = document.getElementById("description").value;
    const price = document.getElementById("price").value;
    const qty = document.getElementById("qty").value;
    const color = document.getElementById("color").value;

    const image1 = document.getElementById("img1").files[0];
    const image2 = document.getElementById("img2").files[0];
    const image3 = document.getElementById("img3").files[0];

    const warrantyId = document.getElementById("warranty").value;
    const catId = document.getElementById("category").value;
    const subCatId = document.getElementById("subcategory").value;
    const brandId = document.getElementById("brand").value;
    const promaterialId = document.getElementById("promaterial").value;
    const prostyleId = document.getElementById("prostyle").value;
    const overallshape = document.getElementById("overallshape").value;
    const armStyleId = document.getElementById("armStyle").value;
    const seating = document.getElementById("seating").value;
    const proQualityId = document.getElementById("proQuality").value;
    const assembly = document.getElementById("assembly").value;

    const form = new FormData();
    form.append("title", title);
    form.append("description", description);
    form.append("price", price);
    form.append("qty", qty);
    form.append("color", color);

    // Append only if selected
    if (image1)
        form.append("image1", image1);
    if (image2)
        form.append("image2", image2);
    if (image3)
        form.append("image3", image3);

    form.append("warrantyId", warrantyId);
    form.append("catId", catId);
    form.append("subCatId", subCatId);
    form.append("brandId", brandId);
    form.append("promaterialId", promaterialId);
    form.append("prostyleId", prostyleId);
    form.append("overallshape", overallshape);
    form.append("armStyleId", armStyleId);
    form.append("seating", seating);
    form.append("proQualityId", proQualityId);
    form.append("assembly", assembly);

    const response = await fetch(
            "UpdateProduct?pid=" + pid,
            {
                method: "POST",
                body: form
            }
    );

    if (response.ok) {

        const json = await response.json();

        if (json.status) {
            console.log(json);
        } else {
            console.log(json);
        }
    } else {
//        console.log(json);

    }

}
