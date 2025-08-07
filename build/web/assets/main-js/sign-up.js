async function signUp() {


    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    const user = {
        username: username,
        email: email,
        password: password,
        confirmPassword: confirmPassword
    };

    const userJson = JSON.stringify(user);

    const response = await fetch(
            "/MasterWear/SignUp",
            {
                method: "POST",
                body: userJson,
                header: {
                    "Content-Type": "application/json"
                }
            }
    );

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
                    window.location.href = "auth-acc-verification.html";
                }, 0);
            });

        } else {

            Swal.fire({
                title: "Account Registration Failed!",
                text: json.message,
                icon: "error"
            });

            document.getElementById("password").innerHTML = "";
            document.getElementById("confirmPassword").innerHTML = "";
        }
    } else {
        Swal.fire({
            title: "Sign Up Failed!",
            text: "Please Try Again Later",
            icon: "error"
        });

        document.getElementById("password").innerHTML = "";
        document.getElementById("confirmPassword").innerHTML = "";
    }
}

