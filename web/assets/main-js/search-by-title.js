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