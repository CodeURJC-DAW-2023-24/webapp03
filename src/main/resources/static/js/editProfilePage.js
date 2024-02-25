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


        let imgPath = $("input[type=file]").get(0).files[0];

        let reader = new FileReader(),
            binary, base64;
        $(reader).on("loadend", function () {
            localStorage.setItem("inputProfileImage", reader.result);
        }, false);

        if (imgPath) {
            reader.readAsDataURL(imgPath);
        }

        let profileImg = $("#inputProfileImage")
        profileImg.src = localStorage.getItem("inputProfileImage");

        //IMAGE STORING
        fetch(profileImg)
            .then(res => res.blob())
            .then(blob => {
                // Crear un nuevo objeto File a partir del Blob
                let file = new File([blob], "profileImage.png", {type: "image/png"});

                // Crear un nuevo objeto FormData y agregar el archivo
                let formData = new FormData();
                formData.append("file", file);

                // Enviar el archivo al servidor
                fetch('/upload', {
                    method: 'POST',
                    body: formData
                })
                    .then(response => {
                        if (response.ok) {
                            console.log('La imagen del perfil se ha subido correctamente');
                        } else {
                            console.error('Error al subir la imagen del perfil');
                        }
                    })
                    .catch(error => console.error('Error:', error));
            });
        //IMAGE STORING

    });

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
})