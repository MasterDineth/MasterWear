async function verifyAccount() {

    const verificationCode = document.getElementById("verificationCode").value;

    const vcode = {
        verification: verificationCode
    };

    const vcodeJson = JSON.stringify(vcode);

    const response = await fetch(
            "/MasterWear/VerifyAccount",
            {
                method: "POST",
                body: vcodeJson,
                header: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const json = await response.json();

        console.log(json.errorCode);

        if (json.status && json.VerificationType === "ACC") {

            Swal.fire({
                title: json.message,
                text: "You Will Be Redirected Shortly!",
                icon: "success",
                showConfirmButton: false,
                timer: 2500
            }).then(() => {
                setTimeout(() => {
                    window.location.href = "/MasterWear/html/auth-login-basic.html";
                }, 0);
            });

        } else if (json.status && json.VerificationType === "PSW") {

            Swal.fire({
                title: json.message,
                text: "You Will Be Redirected Shortly!",
                icon: "success",
                showConfirmButton: false,
                timer: 2500
            }).then(() => {
                setTimeout(() => {
                    window.location.href = "/MasterWear/html/auth-reset-password.html";
                }, 0);
            });

        } else if (json.errorCode === "ACC-UNVER") {

            Swal.fire({
                title: json.message,
                text: "You Will Be Redirected Shortly!",
                icon: "error",
                showConfirmButton: false,
                timer: 2500
            }).then(() => {
                setTimeout(() => {
                    window.location.href = "/MasterWear/html/auth-acc-verification.html";
                }, 0);
            });

        } else {
            if (json.message === "SES-NODATA") {

                Swal.fire({
                    title: json.message,
                    text: "Please Request Password Reset Again!",
                    icon: "success",
                    showConfirmButton: false,
                    timer: 3000
                }).then(() => {
                    setTimeout(() => {
                        window.location.href = "/MasterWear/html/auth-forgot-password-basic.html";
                    }, 0);
                });


            } else {
                Swal.fire({
                    title: "Password Reset Failed!",
                    text: json.message,
                    icon: "error"
                });
            }
        }
    } else {
        Swal.fire({
            title: "Password Reset Failed!",
            text: "Please Try Again Later",
            icon: "error"
        });
    }
}


