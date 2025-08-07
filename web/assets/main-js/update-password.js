async function updatePassword() {

    const oldPassword = document.getElementById("oldPassword").value;
    const newPassword = document.getElementById("newPassword").value;
    const confirmNewPassword = document.getElementById("confirmNewPassword").value;


    const pass = {
        oldPassword: oldPassword,
        newPassword: newPassword,     
        confirmNewPassword: confirmNewPassword
    };

    const passJson = JSON.stringify(pass);

    const response = await fetch(
            "/MasterWear/UpdatePassword",
            {
                method: "POST",
                body: passJson,
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
                title: "Password Update Failed!",
                text: json.message,
                icon: "warning"
            });
            console.log(json.errorCode)
        }
    } else {
//        window.location.href = "/MasterWear/page-500.html";
        console.log(json.errorCode)
    }
}
