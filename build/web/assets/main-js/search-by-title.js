async function search() {

    console.log("searching for product");

    let searchText = document.getElementById("exampleDataList").value;

    console.log(searchText);
    const search = {
        searchText: searchText
    };

    const searchJson = JSON.stringify(search);

    const response = await fetch(
            "/MasterWear/SearchProcess",
            {
                method: "POST",
                body: searchJson,
                header: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const json = await response.json();

        if (json.status) {
            console.log(json);
            
            
            
            updateProductView(json);
        } else {
//            Swal.fire({
//                title: "Search Failed!",
//                text: json.message,
//                icon: "error"
//            });
            console.log("something went wrong");
        }
    } else {
//         window.location.href = "/MasterWear/page-500.html";
        console.log("something went wrong in request");
    }
}

//load products

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

let pages = 1;
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