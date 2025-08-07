async  function resetPasswordSendEmail() {

    const email = document.getElementById("email").value;


    const resetEmail = {
        email: email
    };

    const emailJson = JSON.stringify(resetEmail);

    const response = await fetch(
            "/MasterWear/ResetPasswordSendEmail",
            {
                method: "POST",
                body: emailJson,
                header: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const json = await response.json();

        if (json.status) {
            
            window.location.href = "/MasterWear/auth-confirm.html";
            
        } else {
            Swal.fire({
                title: "Failed To Request A Password Reset",
                text: json.message,
                icon: "error"
            });
        }
    } else {
        Swal.fire({
            title: "Password Reset Request Failed! Please Try Again Later!",
            text: json.message,
            icon: "error"
        });
    }
}