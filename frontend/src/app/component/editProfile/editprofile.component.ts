import {Component, OnInit} from "@angular/core";
import {UserService} from "../../services/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {LoginService} from "../../services/session.service";

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

  constructor(private router: Router, private userService: UserService, private loginService: LoginService, private activatedRoute: ActivatedRoute) {

  }

  profileImage() {
    return this.userService.downloadProfilePicture(this.username);
  }


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

  initialize() {
    let saveChangesBut = document.getElementById("saveChangesBut") as HTMLButtonElement;
    if (saveChangesBut) {

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

    }
  }

  ngOnInit() {
    this.loginService.checkLogged().subscribe({
      next: r => {
        if (r) {
          this.loginService.getLoggedUser().subscribe({
            next: user => {
              if (user.roles.includes("ADMIN")) {
                this.initialize();
              } else if (user.username == this.activatedRoute.snapshot.params['username']) {
                this.initialize();
              } else {
                this.router.navigate(['/error'], {
                  queryParams: {
                    title: "Error de acceso",
                    description: "No tienes permisos para acceder a esta página."
                  }
                });
              }
            }
          });
        } else {
          this.router.navigate(['/error'], {
            queryParams: {
              title: "Error de acceso",
              description: "No tienes permisos para acceder a esta página."
            }
          });
        }
      },
      error: r => {
        this.router.navigate(['/error'], {
          queryParams: {
            title: "Se ha producido un error",
            description: r.error
          }
        });
      }
    });
  }

  checkPasswords() {
    let password = this.passwordMeetsRequirements((document.getElementById("inputPassword") as HTMLInputElement).value);
    let confirmPassword = this.passwordMeetsRequirements((document.getElementById("inputConfirmPassword") as HTMLInputElement).value);
    if (this.passwordsDoMatch() && password && confirmPassword) {
      (document.getElementById("saveChangesBut") as HTMLButtonElement).disabled = false;
    } else {
      (document.getElementById("saveChangesBut") as HTMLButtonElement).disabled = true;
    }
  }

  saveChanges() {
    let newPassword;
    let confirmPassword;
    let oldPassword;
    let correctPassword = false;
    let noPasswordChange = true;

    //If password is to be changed...
    if ((document.getElementById("inputOldPassword") as HTMLInputElement).value !== "" && (document.getElementById("inputPassword") as HTMLInputElement).value !== "") {
      noPasswordChange = false;
      newPassword = (document.getElementById("inputPassword") as HTMLInputElement).value;
      confirmPassword = (document.getElementById("inputConfirmPassword") as HTMLInputElement).value;
      oldPassword = (document.getElementById("inputOldPassword") as HTMLInputElement).value;
      this.userService.changePassword(this.username, {
        newPassword: newPassword,
        confirmPassword: confirmPassword,
        oldPassword: oldPassword
      }).subscribe({
        next: () => {
          let correctPasswordDiv = document.getElementById("correctPassword");
          if (correctPasswordDiv) {
            correctPassword = true;
            this.checkInfoChange(correctPassword, noPasswordChange);
            correctPasswordDiv.style.display = "block";
            setTimeout(() => {
              this.router.navigate(['/profile/' + this.username]);
            }, 3000);
          }
        },
        error: () => {
          correctPassword = false;

          let wrongPasswordDiv = document.getElementById("wrongPassword")
          if (wrongPasswordDiv) {
            wrongPasswordDiv.style.display = "block";
            setTimeout(() => {
              wrongPasswordDiv.style.display = "none";
              window.location.reload();
            }, 3000);
          }
        }
      });
    }

    this.checkInfoChange(correctPassword, noPasswordChange);

  }

  checkInfoChange(correctPassword: boolean, noPasswordChange: boolean) {
    if (correctPassword || noPasswordChange) {
      this.alias = (document.getElementById("inputAlias") as HTMLInputElement).value;
      this.email = (document.getElementById("inputEmailAddress") as HTMLInputElement).value;
      this.description = (document.getElementById("inputProfileDescription") as HTMLInputElement).value;
      this.userService.editProfile(this.username, {
        alias: this.alias,
        email: this.email,
        description: this.description
      }).subscribe({
        next: () => {
          this.router.navigate(['/profile/' + this.username]);
        }
      });
    }
  }

}
