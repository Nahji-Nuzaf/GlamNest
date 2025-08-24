
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

        } else {//when false
            //custom message
            document.getElementById("message").innerHTML = "Unable to load Product. Please try again later";

        }
    } else {
        document.getElementById("message").innerHTML = "Unable to load";
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

function loadsubCategory() {

    const catId = document.getElementById("category").value;
    const subCategorySelect = document.getElementById("subcategory");
    subCategorySelect.length = 1;

    subcategoryList.forEach(item => {

        if (item.category.id == catId) {
            const option = document.createElement("option");
            option.value = item.id;
            option.innerHTML = item.name;
            subCategorySelect.appendChild(option);
        }

    });

}

async function saveProduct() {

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

    form.append("image1", image1);
    form.append("image2", image2);
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
            "SaveProduct",
            {
                method: "POST",
                body: form
            }
    );

    if (response.ok) {

        const json = await response.json();

        if (json.status) {

            document.getElementById("message").innerHTML = "New Product Added successfully";

            document.getElementById("title").value = "";
            document.getElementById("description").value = "";
            document.getElementById("price").value = "";
            document.getElementById("qty").value = "1";
            document.getElementById("color").value = "";

            document.getElementById("img1").value = "";
            document.getElementById("img2").value = "";
            document.getElementById("img3").value = "";

            document.getElementById("warranty").value = 0;
            document.getElementById("category").value = 0;
            document.getElementById("subcategory").value = 0;
            document.getElementById("brand").value = 0;
            document.getElementById("promaterial").value = 0;
            document.getElementById("prostyle").value = 0;
            document.getElementById("overallshape").value = "";
            document.getElementById("armStyle").value = 0;
            document.getElementById("seating").value = "";
            document.getElementById("proQuality").value = 0;
            document.getElementById("assembly").value = "";

        } else {

            if (json.message === "Please sign in!") {
                window.location = "sign-in.html";
            }else{
                document.getElementById("message").innerHTML = json.message;
            }

        }

    } else {

        document.getElementById("message").innerHTML = "Profile details update failed!";
    }

}
