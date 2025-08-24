
async function verifyAccount() {

    const verificationCode =
            document.getElementById("vc1").value +
            document.getElementById("vc2").value +
            document.getElementById("vc3").value +
            document.getElementById("vc4").value +
            document.getElementById("vc5").value +
            document.getElementById("vc6").value;


    const verification = {
        verificationCode: verificationCode
    };

    const verificationJson = JSON.stringify(verification);

    const response = await fetch(
            "VerifyAccount",
            {
                method: "POST",

                header: {
                    "Content-Type": "application/json"
                },
                body: verificationJson

            });

    if (response.ok) {//success
        const json = await response.json();
        if (json.status) {//if true
            //redirect another page
            window.location = "index.html";
        } else {//when false
            //custom message
            if (json.message === "1") {
                window.location = "sign-in.html";
            } else {
                document.getElementById("message").innerHTML = json.message;
            }

        }
    } else {
        document.getElementById("message").innerHTML = "Verification failed. please try again";
    }

}

async function resendCode(){
    
    
    
}
