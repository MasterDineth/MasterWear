async function logOut() {
    Swal.fire({
        title: "Log Out Confirmation",
        text: "Do You Want To Log Out?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, Log Out!"
    }).then(async (result) => {
        if (result.isConfirmed) {
            try {
                const response = await fetch("/MasterWear/SignOut");

                if (response.ok) {
                    const json = await response.json();

                    if (json.status) {
                        console.log("User Logged Out Successfully!");

                        Swal.fire({
                            title: "Logged Out!",
                            text: "You have been logged out successfully.",
                            icon: "success",
                            showConfirmButton: false,
                            timer: 500
                        }).then(() => {
                            setTimeout(() => {
                                window.location.href = "/MasterWear/html/auth-login-basic.html";
                            }, 0);
                        });
                    } else {
                        window.location.reload();
                    }
                } else {
                    Swal.fire({
                        title: "Error!",
                        text: "Failed to log out. Please try again later!",
                        icon: "error"
                    });
                }
            } catch (error) {
                Swal.fire({
                    title: "Network Error!",
                    text: "Could not contact server. Please check your connection.",
                    icon: "error"
                });
            }
        }
    });
}