async function loadSearchData() {

    console.log("loading product title data")

    const response = await fetch("/MasterWear/LoadProductTitles");

    if (response.ok) {
        const json = await response.json();

        if (json.status) {

            productList = json.productList;

            productList.forEach(item => {
                const option = document.createElement("option");
                option.innerHTML = item.title;
                document.getElementById("datalistOptions").appendChild(option);
            });

        } else {
            document.getElementById("message").innerHTML = json.message;
        }
    } else {
        document.getElementById("message").innerHTML = "Failed to Load Data! Please Try Again later!";
    }
}