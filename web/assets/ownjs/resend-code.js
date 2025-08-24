/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

async function resendCode() {
//    console.log("resendCode() called");

    try {
        const response = await fetch("ResendCode", {
            method: "POST"
        });

        console.log("Response status:", response.status);

        if (response.ok) {
            const json = await response.json();
            console.log("Response JSON:", json);
            document.getElementById("message").innerHTML = json.status
                    ? "Code sent to your email"
                    : (json.message || "Code sending failed");
        } else {
            document.getElementById("message").innerHTML = "Server error";
        }
    } catch (error) {
        console.error("Fetch error:", error);
        document.getElementById("message").innerHTML = "Network error";
    }
}
