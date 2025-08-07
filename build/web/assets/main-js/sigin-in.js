async  function signIn() {

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const signIn = {
        email: email,
        password: password
    };

    const signInJson = JSON.stringify(signIn);

    const response = await fetch(
            "/MasterWear/SignIn",
            {
                method: "POST",
                body: signInJson,
                header: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const json = await response.json();

        if (json.status) {

            if (json.loginStatus === "0") {
                window.location.href = "auth-acc-verification.html";
            } else if (json.loginStatus === "1") {
                window.location.href = "/MasterWear/index.html";
            }

        } else {
            Swal.fire({
                title: "Failed to Log In!",
                text: json.message,
                icon: "error"
            });
        }
    } else {
        Swal.fire({
            title: "Login Failed! Please Try Again Later!",
            text: json.message,
            icon: "error"
        });
    }
}