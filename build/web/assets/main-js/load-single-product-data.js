async function loadSingleProductData() {
    const  searchParams = new URLSearchParams(window.location.search);

    if (searchParams.has("id")) {
        const productId = searchParams.get("id");
        console.log(productId);
        const response = await fetch("/MasterWear/LoadSingleProduct?id=" + productId);

        if (response.ok) {
            const json = await response.json();
            if (json.status) {
                console.log(json);
                //single-product-images
                document.getElementById("productImage").src = "images\\product\\" + json.product.id + "\\image1.png";

                //single-product-images

                document.getElementById("productTitle").innerHTML = json.product.title;
                document.getElementById("productDescription").innerHTML = json.product.description;

                document.getElementById("productPrice").innerHTML = "Rs " + new Intl.NumberFormat(
                        "en-US",
                        {minimumFractionDigits: 2}).format(json.product.price);

                document.getElementById("productColor").innerHTML = json.product.color.name;
                document.getElementById("productCategory").innerHTML = json.product.category.name;
                document.getElementById("productSize").innerHTML = json.product.size.name;
                document.getElementById("productQty").innerHTML = json.product.qty;

//                document.getElementById("color-border").style.borderColor = "black";
//                document.getElementById("color-background").style.backgroundColor = json.product.color.value;
//                document.getElementById("product-storage").innerHTML = json.product.storage.value;
//                document.getElementById("product-description").innerHTML = json.product.description;

                //add-to-cart-main-button
                const addToCartMain = document.getElementById("addToCartBtn");
                addToCartMain.addEventListener(
                        "click", (e) => {
                    addToCart(json.product.id, document.getElementById("addToCartValue").value);
                    e.preventDefault();
                });
                //add-to-cart-main-button

                //similar-product
//                let similer_product_main = document.getElementById("smiler-product-main");
//                let productHTML = document.getElementById("similer-product");
//                similer_product_main.innerHTML = "";
//                json.productList.forEach(item => {
//                    
//                    let productCloneHTML = productHTML.cloneNode(true);
//                    productCloneHTML.querySelector("#similer-product-a1").href = "single-product.html?id=" + item.id;
//                    productCloneHTML.querySelector("#similer-product-image").src = "product-images\\" + item.id + "\\image1.png";
//                    productCloneHTML.querySelector("#similer-product-add-to-cart").addEventListener(
//                            "click", (e) => {
//                        addToCart(item.id, 1);
//                        e.preventDefualt();
//                    });
//                    productCloneHTML.querySelector("#similer-product-a2").href = "single-product.html?id=" + item.id;
//                    productCloneHTML.querySelector("#similer-product-title").innerHTML = item.title;
//                    productCloneHTML.querySelector("#similer-product-storege").innerHTML = item.storage.value;
//                    productCloneHTML.querySelector("#similer-prorduct-price").innerHTML = "Rs. " + new Intl.NumberFormat(
//                            "en-US",
//                            {minimumFractionDigits: 2}).format(item.price);
//                    ;
//                    productCloneHTML.querySelector("#simirel-product-color-border").style.borderColor = "Black";
//                    productCloneHTML.querySelector("#similer-product-color-background").style.backgroundColor = item.color.value;
//
//                    //append the clone code
//                    similer_product_main.appendChild(productCloneHTML);
//                    //append the clone code
//                });
//                //similar-product
//
//                $('.recent-product-activation').slick({
//                    infinite: true,
//                    slidesToShow: 4,
//                    slidesToScroll: 4,
//                    arrows: true,
//                    dots: false,
//                    prevArrow: '<button class="slide-arrow prev-arrow"><i class="fal fa-long-arrow-left"></i></button>',
//                    nextArrow: '<button class="slide-arrow next-arrow"><i class="fal fa-long-arrow-right"></i></button>',
//                    responsive: [{
//                            breakpoint: 1199,
//                            settings: {
//                                slidesToShow: 3,
//                                slidesToScroll: 3
//                            }
//                        },
//                        {
//                            breakpoint: 991,
//                            settings: {
//                                slidesToShow: 2,
//                                slidesToScroll: 2
//                            }
//                        },
//                        {
//                            breakpoint: 479,
//                            settings: {
//                                slidesToShow: 1,
//                                slidesToScroll: 1
//                            }
//                        }
//                    ]
//                });

            } else {
//                window.location = "index.html";
                console.log("error response");
            }
        } else {

        }
    }
}

async function addToCart(productId, qty) {
    const response = await fetch("/MasterWear/AddToCart?prId=" + productId + "&qty=" + qty);
    if (response.ok) {
        const json = await response.json();

        if (json.status) {
           alert("added to cart");
        } else {
           console.log("error")
        }
    } else {

    }
}



