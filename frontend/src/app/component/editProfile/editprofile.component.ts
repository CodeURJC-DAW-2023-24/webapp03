import {Component, OnInit} from "@angular/core";
import {UserService} from "../../services/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {LoginService} from "../../services/session.service";
import {NavbarService} from "../../services/navbar.service";

@Component({
  selector: 'editprofile',
  templateUrl: './editprofile.component.html',
  styleUrls: ['./editprofile.component.css']
})
export class EditProfileComponent implements OnInit {
  username = "";
  alias = "";
  email = "";
  description = "";

  userLoaded = false;

  constructor(private router: Router, private userService: UserService, private loginService: LoginService, private navbarService: NavbarService, private activatedRoute: ActivatedRoute) {

  }

  profileImage() {
    return this.userService.downloadProfilePicture(this.username);
  }


//   if (localStorage.getItem("wrongPassword")) {
//   $("#wrongPassword").show();
//   localStorage.removeItem("wrongPassword");
// }
//
// $("#saveChangesBut").on("click", () => {
//   let newPassword;
//   let url = "https://" + window.location.host + "/profile/" + $("#username").text() + "/editPassword";
//   let correctPassword = false;
//   let noPasswordChange = true;
//
//   //If password is to be changed...
//   if (($("#inputOldPassword").val() !== "") && ($("#inputPassword").val() !== "")) {
//     noPasswordChange = false;
//     newPassword = $("#inputPassword").val();
//     url += "?currentPassword=" + $("#inputOldPassword").val();
//     $.ajax({
//       url: url,
//       type: "POST",
//       data: newPassword,
//       contentType: "application/json",
//       beforeSend: (xhr) => {
//         xhr.setRequestHeader("X-CSRF-TOKEN", token);
//       },
//       success: () => {
//         let newUrl = "https://" + window.location.host + "/profile/" + $("#username").text();
//         $("#correctPassword").show();
//         $("#correctPassword").fadeOut(3000);
//         setTimeout(() => {
//           window.location.assign(newUrl);
//         }, 3000);
//       },
//       error: () => {
//         correctPassword = false;
//         localStorage.setItem("wrongPassword", true);
//         location.reload();
//       }
//     });
//   }
//
//   //Fields will only be updated if either the password is to be changed and is also correct or either if it doesn't want to be changed at all
//   if (correctPassword || noPasswordChange) {
//     $.ajax({
//       url: "https://" + window.location.host + "/profile/" + $("#username").text() + "/edit" + "?alias=" + $("#inputAlias").val() + "&email=" + $("#inputEmailAddress").val() + "&description=" + $("#inputProfileDescription").val(),
//       type: "POST",
//       datatype: "json",
//       beforeSend: (xhr) => {
//         xhr.setRequestHeader("X-CSRF-TOKEN", token);
//       },
//       success: (data) => {
//         let newUrl = "https://" + window.location.host + "/profile/" + $("#username").text();
//         window.location.assign(newUrl);
//       }
//     });
//   }
// });

  uploadImage() {
    let inputBut = document.getElementById("inputProfileImage") as HTMLInputElement;
    if (inputBut) {
      inputBut.click();

      inputBut.onchange = () => {
        if (inputBut.files && inputBut.files.length > 0) {
          let file = inputBut.files[0];
          let fileSizeInMB = file.size / 1024 / 1024;
          if (fileSizeInMB < 5) {
            let fileByteArray = [];
            let reader = new FileReader();
            reader.readAsArrayBuffer(file);
            reader.onloadend = (e) => {
              if (reader.readyState === FileReader.DONE) {
                let arrayBuffer = reader.result as ArrayBuffer;
                let array = new Uint8Array(arrayBuffer);
                for (let i = 0; i < array.length; i++) {
                  fileByteArray.push(array[i]);
                }
                let imageString = btoa(fileByteArray.map((v) => {
                  return String.fromCharCode(v);
                }).join(""));

                this.userService.uploadProfilePicture(this.username, file).subscribe({
                  next: n => {
                    setTimeout(() => {
                      location.reload();
                    }, 1000);

                    let uploadSuccessful = document.getElementById("uploadSuccessful");
                    if (uploadSuccessful) {
                      uploadSuccessful.style.display = "block";
                      setTimeout(() => {
                        uploadSuccessful.style.display = "none";
                      }, 5000);
                    }
                  },
                  error: e => {
                    console.log(e);
                    this.showUploadError();
                  }
                });
              }
            }
          } else {
            this.showUploadError();
          }
        }
      }
    }
  }

  showUploadError() {
    let uploadNotSuccessful = document.getElementById("uploadNotSuccessful");
    if (uploadNotSuccessful) {
      uploadNotSuccessful.style.display = "block";
      setTimeout(() => {
        uploadNotSuccessful.style.display = "none";
      }, 5000);
    }
  }

  passwordMeetsRequirements(newPassword: string) {
    let passwordRequirements = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
    let passwordRequirementsDiv = document.getElementById("passwordRequirements");

    if (newPassword.match(passwordRequirements)) {
      if (passwordRequirementsDiv) {
        passwordRequirementsDiv.style.display = "none";
      }
      (document.getElementById("saveChangesBut") as HTMLButtonElement).disabled = false;
      return true;
    } else {
      if (passwordRequirementsDiv) {
        passwordRequirementsDiv.style.display = "block";
      }
      (document.getElementById("saveChangesBut") as HTMLButtonElement).disabled = true;
      return false;
    }
  }

  // @ts-ignore
  passwordsDoMatch() {
    let newPassword = (document.getElementById("inputPassword") as HTMLInputElement).value;
    let confirmPassword = (document.getElementById("inputConfirmPassword") as HTMLInputElement).value;
    let passwordsNotMatching = document.getElementById("passwordsNotMatching");

    if ((newPassword !== confirmPassword) && (newPassword !== "") && (confirmPassword !== "")) {
      if (passwordsNotMatching) {
        passwordsNotMatching.style.display = "block";
      }
      return false;
    } else if ((newPassword !== "") && (confirmPassword !== "")) {
      if (passwordsNotMatching) {
        passwordsNotMatching.style.display = "none";
      }
      return true;
    }
  }

  ngOnInit() {

    let saveChangesBut = document.getElementById("saveChangesBut") as HTMLButtonElement;
    if (saveChangesBut) {
      saveChangesBut.onclick = () => {
        let newPassword;
        let url = "https://" + window.location.host + "/profile/" + this.username + "/editPassword";
        let correctPassword = false;
        let noPasswordChange = true;

        //If password is to be changed...
        if ((document.getElementById("inputOldPassword") as HTMLInputElement).value !== "" && (document.getElementById("inputPassword") as HTMLInputElement).value !== "") {
          noPasswordChange = false;
          newPassword = (document.getElementById("inputPassword") as HTMLInputElement).value;
          url += "?currentPassword=" + (document.getElementById("inputOldPassword") as HTMLInputElement).value;
          // this.userService.changePassword(this.username, newPassword).subscribe({
          //   next: () => {
          //     let newUrl = "https://" + window.location.host + "/profile/" + this.username;
          //     let correctPasswordDiv = document.getElementById("correctPassword");
          //     if (correctPasswordDiv) {
          //       correctPasswordDiv.style.display = "block";
          //       setTimeout(() => {
          //         window.location.assign(newUrl);
          //       }, 3000);
          //     }
          //   },
          //   error: () => {
          //     correctPassword = false;
          //     localStorage.setItem("wrongPassword", "true");
          //     location.reload();
          //   }
          // });
        }

        if (correctPassword || noPasswordChange) {
          // this.userService.editProfile(this.username, this.alias, this.email, this.description).subscribe({
          //   next: () => {
          //     let newUrl = "https://" + window.location.host + "/profile/" + this.username;
          //     window.location.assign(newUrl);
          //   }
          // });
        }
      }

      // get username from url
      this.username = this.activatedRoute.snapshot.params['username'];
      this.userService.getUser(this.username).subscribe({
        next: n => {
          this.username = n.username;
          this.alias = n.alias;
          this.email = n.email;
          this.description = n.description;

          this.userLoaded = true;
        },
        error: e => {
          // route to error page
          this.router.navigate(['/error']);
        }
      });

      Array.from(document.body.getElementsByTagName("*")).forEach((element) => {
        element.addEventListener("focus", () => {
          let wrongPassword = document.getElementById("wrongPassword");
          if (wrongPassword) {
            if (wrongPassword.style.display === "block") {
              wrongPassword.style.display = "none";
            }
          }
        });
      });

      document.querySelectorAll("#inputPassword, #inputConfirmPassword").forEach((element) => {
        element.addEventListener("keyup", () => {
          let password = this.passwordMeetsRequirements((document.getElementById("inputPassword") as HTMLInputElement).value);
          let confirmPassword = this.passwordMeetsRequirements((document.getElementById("inputConfirmPassword") as HTMLInputElement).value);
          if (this.passwordsDoMatch() && password && confirmPassword) {
            (document.getElementById("saveChangesBut") as HTMLButtonElement).disabled = false;
          } else {
            (document.getElementById("saveChangesBut") as HTMLButtonElement).disabled = true;
          }
        });
      });

    }
  }
}
