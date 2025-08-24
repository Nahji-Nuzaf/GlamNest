
function loadData() {
    getUserData();
    getCityData();
}

async function getUserData() {

    const response = await fetch("MyAccount");

    if (response.ok) {
        const json = await response.json();

        document.getElementById("username").innerHTML = `${json.firstName} ${json.lastName}`;
        document.getElementById("since").innerHTML = `Member Since ${json.since}`;
        document.getElementById("firstName").value = json.firstName;
        document.getElementById("lastName").value = json.lastName;
        document.getElementById("email").value = json.email;
        document.getElementById("mobile").value = json.mobile;
        document.getElementById("currentPassword").value = json.password;

        if (json.hasOwnProperty("addressList") && json.addressList !== undefined) {
            let email;
            let lineOne;
            let lineTwo;
            let city;
            let postalCode;
            let cityId;
            const addressUL = document.getElementById("addressUL");
            json.addressList.forEach(address => {
                email = address.user.email;
                lineOne = address.lineOne;
                lineTwo = address.lineTwo;
                city = address.city.name;
                postalCode = address.postalCode;
                cityId = address.city.id;
                const line = document.createElement("li");
                line.innerHTML = lineOne + ", " +
                        lineTwo + ",<br/>" +
                        city + "<br/>" +
                        postalCode;
                addressUL.appendChild(line);
            });

            document.getElementById("lineOne").value = lineOne;
            document.getElementById("lineTwo").value = lineTwo;
            document.getElementById("postalCode").value = postalCode;
            document.getElementById("citySelect").value = parseInt(cityId);

        }
    }

}

async function getCityData() {

    const response = await fetch("CityData");

    if (response.ok) {
        const json = await response.json();

        const citySelect = document.getElementById("citySelect");
        json.forEach(city => {
            let option = document.createElement("option");
            option.innerHTML = city.name;
            option.value = city.id;
            citySelect.appendChild(option);
        });
    }
}

async function savePersonalChanges() {

//    console.log("OK");

    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const mobile = document.getElementById("mobile").value;
    const lineOne = document.getElementById("lineOne").value;
    const lineTwo = document.getElementById("lineTwo").value;
    const postalCode = document.getElementById("postalCode").value;
    const cityId = document.getElementById("citySelect").value;

    const userDataObject = {
        firstName: firstName,
        lastName: lastName,
        mobile: mobile,
        lineOne: lineOne,
        lineTwo: lineTwo,
        postalCode: postalCode,
        cityId: cityId
    };

    const userDataJSON = JSON.stringify(userDataObject);

    const response = await fetch("MyAccount",
            {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: userDataJSON
            }
    );

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            getUserData();
            swal("", json.message, "success");
        } else {
            document.getElementById("message").innerHTML = json.message;
        }
    } else {
        document.getElementById("message").innerHTML = "Profile details update failed!";
    }

}

async function savePasswordChanges() {

    const currentPassword = document.getElementById("currentPassword").value;
    const newPassword = document.getElementById("newPassword").value;
    const confirmNewPassword = document.getElementById("confirmNewPassword").value;

    const userPasswordObject = {
        currentPassword: currentPassword,
        newPassword: newPassword,
        confirmNewPassword: confirmNewPassword
    };

    const userPasswordJson = JSON.stringify(userPasswordObject);

    const response = await fetch("MyAccount",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: userPasswordJson
            }
    );

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
//            console.log(json);
            swal("", json.message, "success");
            
            document.getElementById("newPassword").value = "";
            document.getElementById("confirmNewPassword").value = "";
            
            document.getElementById("messagep").innerHTML = "";
            
        } else {
            document.getElementById("messagep").innerHTML = json.message;
        }
    } else {
        document.getElementById("messagep").innerHTML = "Password update failed!";
    }

}