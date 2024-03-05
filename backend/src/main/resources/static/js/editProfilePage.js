$(() => {

    let token = $("#_csrf").first().attr("value");

    if (localStorage.getItem("wrongPassword")) {
        $("#wrongPassword").show();
        localStorage.removeItem("wrongPassword");
    }

    $("*").on("focus", () => {
        if ($("#wrongPassword").is(":visible")) {
            $("#wrongPassword").hide();
        }
    });

    $("#uploadImage").on("click", () => {
        $("#inputProfileImage").click();
    });

    $("#inputProfileImage").on("change", () => {

        let file = $("#inputProfileImage")[0].files[0];
        let fileByteArray = []
        let reader = new FileReader();
        reader.readAsArrayBuffer(file);
        $(reader).on("loadend", (e) => {
            if (e.target.readyState == FileReader.DONE) {
                let arrayBuffer = e.target.result,
                    array = new Uint8Array(arrayBuffer);
                for (let i = 0; i < array.length; i++) {
                    fileByteArray.push(array[i]);
                }
                let imageString = btoa(fileByteArray.map((v) => {
                    return String.fromCharCode(v)
                }).join(""));

                let uploadUrl = "https://" + window.location.host + "/profile/" + $("#username").outerText + "/upload";

                $.ajax({
                    url: uploadUrl,
                    type: "POST",
                    contentType: "application/json",
                    beforeSend: (xhr) => {
                        xhr.setRequestHeader("X-CSRF-TOKEN", token);
                    },
                    data: JSON.stringify({
                        image: imageString
                    }),
                    success: () => {
                        setTimeout(() => {
                            location.reload();
                        }, 1000);

                        $("#uploadSuccessful").show();
                        setTimeout(() => {
                            $("#uploadSuccessful").fadeOut(3000);
                        }, 5000);
                    }
                });
            }
        });

    });


    $("#editProfileBut").on("click", () => {
        let newUrl = "https://" + window.location.host + "/profile/" + $("#userName-header").text() + "/edit";
        setTimeout(() => {

        }, 100);
        window.location.assign(newUrl);
    });

    $("#returnBut").on("click", () => {
        let newUrl = "https://" + window.location.host + "/profile/" + $("#username").text();
        window.location.assign(newUrl);
        $("#passwordChanged").hide();
    });

    $("#saveChangesBut").on("click", () => {
        let newPassword;
        let url = "https://" + window.location.host + "/profile/" + $("#username").text() + "/editPassword";
        let correctPassword = false;
        let noPasswordChange = true;

        //If password is to be changed...
        if (($("#inputOldPassword").val() !== "") && ($("#inputPassword").val() !== "")) {
            noPasswordChange = false;
            newPassword = $("#inputPassword").val();
            url += "?currentPassword=" + $("#inputOldPassword").val();
            $.ajax({
                url: url,
                type: "POST",
                data: newPassword,
                contentType: "application/json",
                beforeSend: (xhr) => {
                    xhr.setRequestHeader("X-CSRF-TOKEN", token);
                },
                success: () => {
                    let newUrl = "https://" + window.location.host + "/profile/" + $("#username").text();
                    $("#correctPassword").show();
                    $("#correctPassword").fadeOut(3000);
                    setTimeout(() => {
                        window.location.assign(newUrl);
                    }, 3000);
                },
                error: () => {
                    correctPassword = false;
                    localStorage.setItem("wrongPassword", true);
                    location.reload();
                }
            });
        }

        //Fields will only be updated if either the password is to be changed and is also correct or either if it doesn't want to be changed at all
        if (correctPassword || noPasswordChange) {
            $.ajax({
                url: "https://" + window.location.host + "/profile/" + $("#username").text() + "/edit" + "?alias=" + $("#inputAlias").val() + "&email=" + $("#inputEmailAddress").val() + "&description=" + $("#inputProfileDescription").val(),
                type: "POST",
                datatype: "json",
                beforeSend: (xhr) => {
                    xhr.setRequestHeader("X-CSRF-TOKEN", token);
                },
                success: (data) => {
                    let newUrl = "https://" + window.location.host + "/profile/" + $("#username").text();
                    window.location.assign(newUrl);
                }
            });
        }
    });

    function passwordsDoMatch() {
        let newPassword = $("#inputPassword").val();
        let confirmPassword = $("#inputConfirmPassword").val();

        if ((newPassword !== confirmPassword) && (newPassword !== "") && (confirmPassword !== "")) {
            $("#passwordsNotMatching").show();
            return false;
        } else if ((newPassword !== "") && (confirmPassword !== "")) {
            $("#passwordsNotMatching").hide();
            return true;
        }

    }

    function passwordMeetsRequirements(newPassword) {
        let passwordRequirements = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
        if (newPassword.match(passwordRequirements)) {
            $("#passwordRequirements").hide();
            $("#saveChangesBut").prop("disabled", false);
            return true;
        } else {
            $("#passwordRequirements").show();
            $("#saveChangesBut").prop("disabled", true);
            return false;
        }
    }

    $("#inputPassword, #inputConfirmPassword").on("keyup", () => {
        let password = passwordMeetsRequirements($("#inputPassword").val());
        let confirmPassword = passwordMeetsRequirements($("#inputConfirmPassword").val());
        if (passwordsDoMatch() && password && confirmPassword) {
            $("#saveChangesBut").prop("disabled", false);
        } else {
            $("#saveChangesBut").prop("disabled", true);
        }
    });

    $()

});
