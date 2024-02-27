$(() => {

    let token = $("#_csrf").attr("value");

    $("#passwordsNotMatching").hide();
    $("#passwordChanged").hide();
    $("#passwordRequirements").hide();
    $("#confirmPasswordChange").hide();
    $("#userError").hide();
    let passwordChanged = false;


    function passwordsDoMatch() {
        setTimeout(() => {
            let newPassword = $("#inputPassword").val();
            let confirmPassword = $("#inputConfirmPassword").val();

            if ((newPassword !== confirmPassword) && (newPassword !== "") && (confirmPassword !== "")) {
                $("#passwordsNotMatching").show();
                $('#submit').attr('disabled', true);
            } else {
                $("#passwordsNotMatching").hide();
                $('#submit').attr('disabled', false);
            }
        }, 100)

    }

    function passwordMeetsRequirements(newPassword) {
        let passwordRequirements = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{4,}$/;
        if (newPassword.match(passwordRequirements)) {
            $("#passwordRequirements").hide();
            $('#submit').attr('disabled', false);
            return true;
        } else {
            $("#passwordRequirements").show();
            $('#submit').attr('disabled', true);
            return false;
        }
    }

    function checkUserAvailability(username){ 
        $.get("/signup/checkAvailability", { username: username }, function(response) {
            if(response) {
                $("#userError").hide();
                $('#submit').attr('disabled', false);
            } else {
                $("#userError").show();
                $('#submit').attr('disabled', true);
            }
        });
    }

    $("#inputPassword, #inputConfirmPassword").keyup(function() {
        passwordsDoMatch();
        passwordMeetsRequirements($("#inputPassword").val());
    });

    $('#inputUsername').keyup(function() {
        var username = $(this).val();
        checkUserAvailability(username);
    });

});
