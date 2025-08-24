
async function signUp(event) {
    event.preventDefault();

    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const email = document.getElementById("email").value;
    const mobile = document.getElementById("mobile").value;
    const password = document.getElementById("password").value;

    const user = {
        firstName: firstName,
        lastName: lastName,
        email: email,
        mobile: mobile,
        password: password
    };

    const userJson = JSON.stringify(user);

    const response = await fetch(
            "SignUp",
            {
                method: "POST",

                header: {
                    "Content-Type": "application/json"
                },
                body: userJson

            });


    if (response.ok) {
        const json = await response.json();
        if (json.status) {//if true
            //redirect another page
            window.location = "verify-account.html";
        } else {//when false
            //custom message


            document.getElementById("message").innerHTML = json.message;

        }
    } else {
        document.getElementById("message").innerHTML = "Registration failed. please try again";
    }

}
