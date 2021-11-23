function signNameFocus() {
    let labelName=document.getElementById('labelName');
    let inputName=document.getElementById('firstName');
    console.log(inputName.value.length);
    console.log('helo')
    labelName.style.display='inline';
}

function signNameBlur() {
    let labelName=document.getElementById('labelName');

    let inputName=document.getElementById('firstName');
    if (inputName.value.length===0){
        labelName.style.display='none';
    }
}





function signLastNameFocus() {
    let labelLastName=document.getElementById('labelLastName');
    let inputLastName=document.getElementById('lastName');
    console.log(inputLastName.value.length);
    console.log('helo')
    labelLastName.style.display='inline';
}

function signLastNameBlur() {
    let labelLastName=document.getElementById('labelLastName');

    let inputLastName=document.getElementById('lastName');
    if (inputLastName.value.length===0){
        labelLastName.style.display='none';
    }
}




function signEmailFocus() {
    let labelEmail=document.getElementById('labelEmail');
    let inputEmail=document.getElementById('email');
    console.log(inputEmail.value.length);
    console.log('helo')
    labelEmail.style.display='inline';
}

function signEmailBlur() {
    let labelEmail=document.getElementById('labelEmail');

    let inputEmail=document.getElementById('email');
    if (inputEmail.value.length===0){
        labelEmail.style.display='none';
    }
}






function signPhoneNumberFocus() {
    let labelPhoneNunber=document.getElementById('labelPhoneNumber');
    let inputPhoneNumber=document.getElementById('phoneNumber');
    console.log(inputPhoneNumber.value.length);
    console.log('helo')
    labelPhoneNumber.style.display='inline';
}

function signPhoneNumberBlur() {
    let labelPhoneNumber=document.getElementById('labelPhoneNumber');

    let inputPhoneNumber=document.getElementById('phoneNumber');
    if (inputPhoneNumber.value.length===0){
        labelPhoneNumber.style.display='none';
    }
}




function signLoginFocus() {
    let labelLogin=document.getElementById('labelLogin');
    let inputLogin=document.getElementById('login');
    console.log(inputLogin.value.length);
    console.log('helo')
    labelLogin.style.display='inline';
}

function signLoginBlur() {
    let labelLogin=document.getElementById('labelLogin');

    let inputLogin=document.getElementById('login');
    if (inputLogin.value.length===0){
        labelLogin.style.display='none';
    }
}




function signPasswordFocus() {
    let labelPassword=document.getElementById('labelPassword');
    let inputPassword=document.getElementById('password');
    console.log(inputPassword.value.length);
    console.log('helo')
    labelPassword.style.display='inline';
}

function signPasswordBlur() {
    let labelPassword=document.getElementById('labelPassword');

    let inputPassword=document.getElementById('password');
    if (inputPassword.value.length===0){
        labelPassword.style.display='none';
    }
}
