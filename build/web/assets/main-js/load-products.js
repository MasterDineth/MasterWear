
async function loadData() {

    const response = await fetch("/MasterWear/LoadProducts");
    if (response.ok) {
        const json = await response.json();
        if (json.status) {

//            console.log(json);
            loadOptions("category", json.categorydList, "name");
            loadOptions("size", json.sizeList, "name");
            loadOptions("color", json.colorList, "name");

            updateProductView(json);
        } else {
            console.log("something went wrong");
        }
    } else {
        console.log("something went wrong in the response!");
    }
}

function loadOptions(prefix, dataList, property) {

    let options = document.getElementById(prefix + "-options");
    let li = document.getElementById(prefix + "-li");
    options.innerHTML = "";

    dataList.forEach(item => {
        let li_clone = li.cloneNode(true);

        if (prefix === "color") {

            li_clone.querySelector("#" + prefix + "-c").style.backgroundColor = item[property];

            li_clone.querySelector("#" + prefix + "-a").value = item[property];
            li_clone.querySelector("#" + prefix + "-b").innerHTML = item[property];
        } else {

            li_clone.querySelector("#" + prefix + "-a").value = item[property];
            li_clone.querySelector("#" + prefix + "-b").innerHTML = item[property];
        }
        options.appendChild(li_clone);

    });

    const all_li = document.querySelectorAll("#" + prefix + "-options li");
    all_li.forEach(list => {
        list.addEventListener("click", function () {
            all_li.forEach(y => {
                y.classList.remove("chosen");
            });
            this.classList.add("chosen");
        });
    });
}





async function searchProduct(firstResult) {
//
//    let category = "";
//    let size = "";
//    let color = "";

//    const sizeCheckBox = document.querySelector("#size-options input[type='checkbox']:checked");
//    let size = sizeCheckBox.value;
//
//    const categoryCheckBox = document.querySelector("#category-options input[type='checkbox']:checked");
//    let category = categoryCheckBox.value;
//
//    const colorCheckBox = document.querySelector("#color-options input[type='checkbox']:checked");
//    let color = colorCheckBox.value;

    const sizeCheckBox = document.querySelector("#size-options input[type='checkbox']:checked");
    let size = sizeCheckBox ? sizeCheckBox.value : "";

    const categoryCheckBox = document.querySelector("#category-options input[type='checkbox']:checked");
    let category = categoryCheckBox ? categoryCheckBox.value : "";

    const colorCheckBox = document.querySelector("#color-options input[type='checkbox']:checked");
    let color = colorCheckBox ? colorCheckBox.value : "";





    const price_range_start = $("#slider-range").slider("values", 0);
    const price_range_end = $("#slider-range").slider("values", 1);
    const sort_value = document.getElementById("st-sort").value;

//        console.log(category);
//    console.log(size);
//    console.log(color);
//    console.log(price_range_end);
//    console.log(price_range_start);
//    console.log(sort_value);

    const data = {
        firstResult: firstResult,
        categoryName: category,
        sizeName: size,
        colorName: color,

        priceStart: price_range_start,
        priceEnd: price_range_end,
        sortValue: sort_value
    };

    const dataJSON = JSON.stringify(data);

//    console.log(dataJSON);
    const response = await fetch("/MasterWear/SearchProductsByFilter", {
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
            console.log("Product loading Complete...");
        } else {
            console.log("Something went wrong.Please try again later");
        }
    } else {
        console.log("Something went wrong.Please try again later");
    }
}

const st_product = document.getElementById("st-product"); //product card parent node
let st_pagination_button = document.getElementById("st-pagination-button");
let current_page = 0;

function updateProductView(json) {
    const product_container = document.getElementById("st-product-container");
    product_container.innerHTML = "";
    json.productList.forEach(product => {
        let st_product_clone = st_product.cloneNode(true);
        st_product_clone.querySelector("#st-product-a-1").href = "/MasterWear/shop-single.html?id=" + product.id;
        st_product_clone.querySelector("#st-product-img-1").src = "images\\product\\" + product.id + "\\image1.png";

//        st_product_clone.querySelector("#st-product-add-to-cart").addEventListener(
//                "click", (e) => {
//            addToCart(product.id, 1);
//            e.preventDefault();
//        });

//        st_product_clone.querySelector("#st-product-a-2").href = "single-product.html?id=" + product.id;

        st_product_clone.querySelector("#st-product-title-1").innerHTML = product.title;
        st_product_clone.querySelector("#st-product-price-1").innerHTML = "Rs : " + new Intl.NumberFormat(
                "en-US",
                {minimumFractionDigits: 2}).format(product.price);
        //append child
        product_container.appendChild(st_product_clone);
    });

    let st_pagination_container = document.getElementById("st-pagination-container");
    st_pagination_container.innerHTML = "";
    let all_product_count = json.allProductCount;

    document.getElementById("all-item-count").innerHTML = all_product_count;

    let product_per_page = 6;
    let pages = Math.ceil(all_product_count / product_per_page);

    //previos-button
    if (current_page !== 0) {
        let st_pagination_button_per_colone = st_pagination_button_colone(true);
        st_pagination_button_per_colone.innerHTML = "Prev";

        st_pagination_button_per_colone.addEventListener(
                "click", (e) => {
            current_page--;
            searchProduct(current_page * product_per_page);
        });
        st_pagination_container.appendChild(st_pagination_button_per_colone);
    }

//pagination-button
    for (let i = 0; i < pages; i++) {
        let st_pagination_button_colone = st_pagination_button.cloneNode(true);
        st_pagination_button_colone.innerHTML = i + 1;
        st_pagination_button_colone.addEventListener(
                "click", (e) => {
            current_page = i;
            searchProduct(i * product_per_page);
            e.prevenDefault();
        });

        if (i === Number(current_page)) {
            st_pagination_button_colone.className = "axil-btn btn-bg-primary ml--10";
        } else {
            st_pagination_button_colone.className = "axil-btn btn-bg-secondary ml--10";

        }
        st_pagination_container.appendChild(st_pagination_button_colone);
    }

    //Next-button
//    if (current_page !== (pages - 1)) {
//        let st_pagination_button_next_colone = st_pagination_button_colone(true);
//        st_pagination_button_next_colone.innerHTML = "Next";
//        st_pagination_button_next_colone.addEventListener(
//                "click", (e) => {
//            current_page++;
//            searchProduct(current_page * product_per_page);
//        });
//        st_pagination_container.appendChild(st_pagination_button_next_colone);
//    }
}

function addToCart(productId, qty) {
    console.log(productId + " " + qty);
}