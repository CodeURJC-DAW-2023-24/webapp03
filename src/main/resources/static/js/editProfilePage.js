$(() => {
    $("#passwordsNotMatching").hide();
    $("#passwordChanged").hide();
    $("#passwordRequirements").hide();
    $("#confirmPasswordChange").hide();
    let passwordChanged = false;

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

                $.ajax({
                    url: window.location.href.substring(0, window.location.href.lastIndexOf("/")) + "/upload",
                    type: "POST",
                    contentType: "application/json",
                    data: JSON.stringify({
                        image: imageString
                    }),
                    success: (data) => {
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

})
;

$("#editProfileBut").on("click", () => {
    window.location.assign(window.location.href + "/edit");
});

$("#returnBut").on("click", () => {
    window.location.assign(window.location.href.slice(0, -5));
    $("#passwordChanged").hide();
});

$("#saveChangesBut").on("click", () => {
    $.ajax({
        url: window.location.href + "?alias=" + $("#inputAlias").val() + "&email=" + $("#inputEmailAddress").val() + "&description=" + $("#inputProfileDescription").val() + "&password=" + $("#inputPassword").val(),
        type: "POST",
        datatype: "json",
        success: (data) => {
            window.location.assign(window.location.href.slice(0, -5));
            if (passwordChanged) {
                $("#passwordChanged").hide();
                $("#confirmPasswordChange").hide();
            } else if ((!passwordChanged) && ($("#inputPassword").val() !== "")) {
                $("#confirmPasswordChange").show();
            }
        }
    });
});

function passwordsDoMatch() {
    setTimeout(() => {
        let newPassword = $("#inputPassword").val();
        let confirmPassword = $("#inputConfirmPassword").val();

        if ((newPassword !== confirmPassword) && (newPassword !== "") && (confirmPassword !== "")) {
            $("#passwordsNotMatching").show();
        } else {
            $("#passwordsNotMatching").hide();
        }
    }, 100)

}

function passwordMeetsRequirements(newPassword) {
    let passwordRequirements = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
    if (newPassword.match(passwordRequirements)) {
        $("#passwordRequirements").hide();
        return true;
    } else {
        $("#passwordRequirements").show();
        return false;
    }
}

$(".change-password-button").on("click", () => {
    let currentHypoPassword = $("#inputOldPassword").val();
    let newPassword = $("#inputPassword").val();
    let confirmPassword = $("#inputConfirmPassword").val();

    if ((newPassword !== "") && (newPassword === confirmPassword) && passwordMeetsRequirements(newPassword)) {
        $.ajax({
            url: window.location.href.replace("edit", "editPassword"),
            type: "POST",
            data: {
                currentPassword: currentHypoPassword,
                newPassword: newPassword
            },
            success: function (response) {
                $("#passwordChanged").show();
            },
            error: function (error) {
                console.log(error);
            }
        });
    }
});

$("#inputPassword, #inputConfirmPassword").on("keydown", (e) => {
    passwordsDoMatch();

    passwordMeetsRequirements($("#inputPassword").val());
});
