
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

    const sizeCheckBox = document.querySelector("#size-options input[type='checkbox']:checked");
    let size = sizeCheckBox ? sizeCheckBox.value : "";

    const categoryCheckBox = document.querySelector("#category-options input[type='checkbox']:checked");
    let category = categoryCheckBox ? categoryCheckBox.value : "";

    const colorCheckBox = document.querySelector("#color-options input[type='checkbox']:checked");
    let color = colorCheckBox ? colorCheckBox.value : "";

    const price_range_start = $("#slider-range").slider("values", 0);
    const price_range_end = $("#slider-range").slider("values", 1);
    const sort_value = document.getElementById("st-sort").value;

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

    renderPagination();
}

let pages = 2; 
let currentPage = 1;

// Create pagination UI dynamically
function renderPagination() {

    const container = document.getElementById("st-pagination-container");
    const ul = document.createElement("ul");
    ul.innerHTML = "";

    // Previous button
    const prevLi = document.createElement("li");
    prevLi.innerHTML = `<a href="#">&lt;</a>`;
    if (currentPage === 1)
        prevLi.classList.add("disabled");
    ul.appendChild(prevLi);

    // Page number buttons
    for (let i = 1; i <= pages; i++) {
        const li = document.createElement("li");
        if (i === currentPage) {
            li.classList.add("active");
            li.innerHTML = `<span>${i}</span>`;
        } else {
            li.innerHTML = `<a href="#">${i}</a>`;
        }
        ul.appendChild(li);
    }

    // Next button
    const nextLi = document.createElement("li");
    nextLi.innerHTML = `<a href="#">&gt;</a>`;
    if (currentPage === pages)
        nextLi.classList.add("disabled");
    ul.appendChild(nextLi);

    // Replace old pagination
    container.innerHTML = "";
    container.appendChild(ul);

    // Attach click listeners
    ul.addEventListener("click", function (e) {
        const target = e.target;
        if (target.tagName !== "A" && target.tagName !== "SPAN")
            return;

        const text = target.textContent.trim();

        if (text === "<" && currentPage > 1) {
            currentPage--;
        } else if (text === ">" && currentPage < pages) {
            currentPage++;
        } else {
            const pageNum = parseInt(text);
            if (!isNaN(pageNum))
                currentPage = pageNum;
        }

        renderPagination();
        searchProduct((currentPage - 1) * 6);
    });
}



