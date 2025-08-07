async function resetPassword() {


    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirm-password").value;

    const data = {
        password: password,
        confirmPassword: confirmPassword
    };

    const dataJson = JSON.stringify(data);
    const response = await fetch("/MasterWear/ResetPassword", {
        method: "POST",
        body: dataJson,
        headers: {
            "Content-Type": "application/json"
        }
    });


    if (response.ok) {
        const json = await response.json();

        console.log(json.errorCode);

        if (json.status) {

            Swal.fire({
                title: json.message,
                text: "You will be redirected to account verification",
                icon: "success",
                showConfirmButton: false,
                timer: 3000
            }).then(() => {
                setTimeout(() => {
                    window.location.href = "auth-login-basic.html";
                }, 0);
            });

        } else if (json.errorCode === "ACC-UNVER") {
            Swal.fire({
                title: json.message,
                text: "You will be redirected to account verification",
                icon: "error",
                showConfirmButton: false,
                timer: 3000
            }).then(() => {
                setTimeout(() => {
                    window.location.href = "auth-acc-verification.html";
                }, 0);
            });
        } else {

            Swal.fire({
                title: "Password Reset Failed!",
                text: json.message,
                icon: "error"
            });

            document.getElementById("password").innerHTML = "";
            document.getElementById("confirm-password").innerHTML = "";
        }
    } else {
        Swal.fire({
            title: "Password Reset Failed!",
            text: "Please Try Again Later",
            icon: "error"
        });

        document.getElementById("password").innerHTML = "";
        document.getElementById("confirm-password").innerHTML = "";
    }
}

