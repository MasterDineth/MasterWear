async function loadProductData() {
    const response = await fetch("/MasterWear/LoadProductData");

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            loadSelect("category", json.categoryList, "name");
            loadSelect("size", json.sizeList, "name");
            loadSelect("color", json.colorList, "name");
        } else {
            console.log("Unable to get product data! Please try again later");
        }
    } else {
        console.log("request error");
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

async function saveProduct() {
    
    console.log("saving product");
    
    const categoryId = document.getElementById("category").value;
    const sizeId = document.getElementById("size").value;
    const colorId = document.getElementById("color").value;
    const title = document.getElementById("title").value;

    const description = document.getElementById("description").value;
    const price = document.getElementById("price").value;
    const qty = document.getElementById("quantity").value;

    const image = document.getElementById("image").files[0];
    
    console.log(price);


    const form = new FormData();
    form.append("categoryId", categoryId);
    form.append("sizeId", sizeId);
    form.append("colorId", colorId);
    form.append("title", title);
    form.append("description", description);
    form.append("price", price);
    form.append("qty", qty);
    form.append("image", image);


    const response = await fetch("/MasterWear/SaveProductData", {
        method: "POST",
        body: form
    });

    if (response.ok) {
        const json = await response.json();
        if (json.status) {           

            document.getElementById("category").value = 0;
            document.getElementById("size").value = 0;
            document.getElementById("color").value = 0;
            document.getElementById("title").value = "";
            document.getElementById("description").value = "";
            document.getElementById("price").value = 0.00;
            document.getElementById("quantity").value = 1;
            document.getElementById("image").value = "";

        } else {

            if (json.message === "please sign in!") {
                window.location = "auth-login-basic.html";
            } else {
                console.log(json.message);
            }

        }
    } else {
        console.log("request error");
    }
}
