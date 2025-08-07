//loard cart items

async function loadCartItems() {



    const response = await fetch("/MasterWear/LoadCartProducts");

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            const cart_item_container = document.getElementById("cartTableBody");
            cart_item_container.innerHTML = "";

            let total = 0;
            let totalQty = 0;

            json.cartItems.forEach(cart => {
                let productSubTotal = cart.product.price * cart.qty;
                total += productSubTotal;
                totalQty += cart.qty;

                let tableRow =
                        `<tr>
                    <td class="product-thumbnail">
                      <img src="images\\product\\${cart.product.id}\\image1.png" alt="Image" class="img-fluid">
                    </td>
                    <td class="product-name">
                      <h2 class="h5 text-black">${cart.product.title}</h2>
                    </td>
                    <td>Rs. ${new Intl.NumberFormat("en-US",
                                {minimumFractionDigits: 2}).format(cart.product.price)}</td>
                    <td>
                      <div class="input-group mb-3" style="max-width: 120px;">
                        <div class="input-group-prepend">
                          <button class="btn btn-outline-primary js-btn-minus" type="button">&minus;</button>
                        </div>
                        <input type="text" class="form-control text-center" value="${cart.qty}" placeholder="" aria-label="Example text with button addon" aria-describedby="button-addon1">
                        <div class="input-group-append">
                          <button class="btn btn-outline-primary js-btn-plus" type="button">&plus;</button>
                        </div>
                      </div>

                    </td>
                    <td>Rs. ${new Intl.NumberFormat("en-US", {minimumFractionDigits: 2}).format(productSubTotal)}</td>
                    <td><a href="#" class="btn btn-primary btn-sm">X</a></td>
                  </tr>`;

//                cart_item_container.innerHTML += tableData;
                cart_item_container.innerHTML += tableRow;


            });

            document.getElementById("noOfCartItems").innerHTML = totalQty;
            document.getElementById("cartTotal").innerHTML ="Rs : "+new Intl.NumberFormat("en-US",
                    {minimumFractionDigits: 2}).format(total);
        } else {
            alert(json.message);
        }
        console.log(json);
    } else {
        console.log("error loading data");
    }
}



