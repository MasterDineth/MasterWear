async function updateProfile() {

    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const mobile = document.getElementById("phoneNumber").value;
    const line1 = document.getElementById("line1").value;
    const line2 = document.getElementById("line2").value;
    const city = document.getElementById("city").value;
    const zipCode = document.getElementById("zipCode").value;

    const profile = {
        firstName: firstName,
        lastName: lastName,
        mobile: mobile,
        line1: line1,
        line2: line2,
        city: city,
        postalCode: zipCode
    };

    const profileJson = JSON.stringify(profile);

    const response = await fetch(
            "/MasterWear/UpdateProfile",
            {
                method: "POST",
                body: profileJson,
                header: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const json = await response.json();

        if (json.status) {

            Swal.fire({
                title: "Success",
                text: json.message,
                icon: "success"
            });
            setTimeout(() => {
                window.location.reload();
            }, 1500);

        } else {
            Swal.fire({
                title: "Profile Update Failed!",
                text: json.message,
                icon: "warning"
            });
            console.log(json.errorCode)
        }
    } else {
        window.location.href = "/MasterWear/page-500.html?code=" + json.errorCode;
        console.log(json.errorCode)
    }
}
