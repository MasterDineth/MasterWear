async function loadUserData() {

    const response = await fetch("/MasterWear/LoadUserData");

    if (response.ok) {
        const json = await response.json();




        if (json.status) {

            if (json.hasUserData) {
                //has addtional user data

                console.log("loading data")
//                console.log(json);

                document.getElementById("topUsername").innerHTML = json.username;
                document.getElementById("topEmail").innerHTML = json.email;
                document.getElementById("topMobile").innerHTML = json.mobile;

                document.getElementById("topBio").innerHTML = json.bio;
                document.getElementById("topCity").innerHTML = json.city;
                document.getElementById("topArd1").innerHTML = json.line1;

                //document.getElementById("since").innerHTML = json.joinedAt;

                document.getElementById("firstname").value = json.firstName;
                document.getElementById("lastname").value = json.lastName;
                document.getElementById("mobile").value = json.mobile;

                document.getElementById("line1").value = json.line1;
                document.getElementById("line2").value = json.line2;
                document.getElementById("postalCode").value = json.postalCode;

                document.getElementById("citySelect").value = json.cityId;


            } else {
                //does not have additional user data

                console.log("loading Basic Data")

                document.getElementById("topUsername").innerHTML = json.username;
                document.getElementById("topEmail").innerHTML = json.email;
            }

        } else {
            console.log("error loading data")
        }
        
    } else {
        console.log("reques failed")
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
                document.getElementById("citySelect").appendChild(option);
            });

        } else {
            document.getElementById("message").innerHTML = json.message;
        }
    } else {
        document.getElementById("message").innerHTML = "Failed to Load Data! Please Try Again later!";
    }
}
