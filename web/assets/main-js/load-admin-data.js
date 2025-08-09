async function loadAdminData() {

    const response = await fetch("/MasterWear/LoadAdminData");

    if (response.ok) {
        const json = await response.json();

        if (json.status) {

            if (json.hasUserData) {
                //has addtional user data

                console.log("loading data")
                console.log(json);

                document.getElementById("username").innerHTML = json.username;
                document.getElementById("email").value = json.email;
                document.getElementById("phoneNumber").value = json.mobile;
                document.getElementById("firstName").value = json.firstName;
                document.getElementById("lastName").value = json.lastName;
                document.getElementById("line1").value = json.line1;
                document.getElementById("line2").value = json.line2;
                document.getElementById("zipCode").value = json.postalCode;
                document.getElementById("city").value = json.cityId;

            } else {
                console.log("error loading data")
            }

        } else {
            console.log("reques failed")
        }

    }
}
async function loadCity() {

    console.log("loading city data")

    const response = await fetch("/MasterWear/LoadCity");

    if (response.ok) {
        const json = await response.json();

        if (json.status) {

            cityList = json.cityList;

//            console.log(cityList);

            cityList.forEach(item => {
                const option = document.createElement("option");
                option.value = item.id;
                option.innerHTML = item.name;
                document.getElementById("city").appendChild(option);
            });

        } else {
            document.getElementById("message").innerHTML = json.message;
        }
    } else {
        document.getElementById("message").innerHTML = "Failed to Load Data! Please Try Again later!";
    }
}
