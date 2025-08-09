//loard cart items

async function loadAllProducts() {



    const response = await fetch("/MasterWear/LoadAllProducts");

    if (response.ok) {

        const json = await response.json();
        if (json.status) {

            console.log(json);

            const table_body = document.getElementById("tblody");
            table_body.innerHTML = "";

            json.product.forEach(product => {
                let tableRow =
                        `<tr>
      <td class="px-6 py-4">${product.id}</td>
      <td class="px-6 py-4">${product.title}</td>
      <td class="px-6 py-4">Rs : ${product.price}</td>
      <td class="px-6 py-4">${product.qty}</td>
      <td class="px-6 py-4">${product.category.name}</td>
      <td class="px-6 py-4">${product.size.name}</td>
      <td class="px-6 py-4">${product.color.name}</td>
      <td class="px-6 py-4 text-center">
        <button class="px-3 py-1 bg-blue-500 text-white rounded hover:bg-red-400">
          ${product.status.name}
        </button>
      </td>
    </tr>`


                        ;

//                cart_item_container.innerHTML += tableData;
                table_body.innerHTML += tableRow;


            });

        } else {
            alert(json.message);
        }
        console.log(json);
    } else {
        console.log("error loading data");
    }
}



