let modalUpdateProfile =document.getElementById('openModalUpdateProfile')
let modalChooseCards=document.getElementById('openModalChooseCards')
let modalAddCard=document.getElementById('openModalAddCard')
let modalUpdateCard=document.getElementById('openModalUpdateCard')
let flag = document.getElementById('flag');
function openModalUpdateProfile(){
    modalUpdateProfile.style.opacity='1';
    modalUpdateProfile.style.pointerEvents='auto';
}

function closeModalUpdateProfile(){
    modalUpdateProfile.style.opacity='0';
    modalUpdateProfile.style.pointerEvents='none';
}


function closeModalUpdateProfileButton(){
    modalUpdateProfile.style.opacity='0';
    modalUpdateProfile.style.pointerEvents='none';
}
if (flag===null){
    console.log('null')
}


if (flag.value==='7'){
    console.log(flag.value);
    modalUpdateProfile.style.opacity='1';
    modalUpdateProfile.style.pointerEvents='auto';
}

// function openModalChooseCards(){
//     modalChooseCards.style.opacity='1';
//     modalChooseCards.style.pointerEvents='auto';
// }
// function closeModalChooseCards(){
//     modalChooseCards.style.opacity='0';
//     modalChooseCards.style.pointerEvents='none';
// }
// function closeModalChooseCardsButton(){
//     modalChooseCards.style.opacity='0';
//     modalChooseCards.style.pointerEvents='none';
// }
function openModalAddCard(){
    modalAddCard.style.opacity='1';
    modalAddCard.style.pointerEvents='auto';
}

function closeModalAddCard() {
    modalAddCard.style.opacity='0';
    modalAddCard.style.pointerEvents='none';
}

function closeModalAddCardButton() {
    modalAddCard.style.opacity='0';
    modalAddCard.style.pointerEvents='none';
}

if (flag.value==='8'){
    console.log(flag.value);
    modalAddCard.style.opacity='1';
    modalAddCard.style.pointerEvents='auto';
}

if (flag.value==='9'){
    console.log(flag.value);
    modalChooseCards.style.opacity='1';
    modalChooseCards.style.pointerEvents='auto';
}


if (flag.value==='10'){
    console.log(flag.value);
    modalUpdateCard.style.opacity='1';
    modalUpdateCard.style.pointerEvents='auto';
}

function closeModalUpdateCard() {
    modalUpdateCard.style.opacity='0';
    modalUpdateCard.style.pointerEvents='none';
}

function closeModalUpdateCardButton() {
    modalUpdateCard.style.opacity='0';
    modalUpdateCard.style.pointerEvents='none';
}

if (flag.value==='11'){
    console.log(flag.value);
    modalUpdateCard.style.opacity='1';
    modalUpdateCard.style.pointerEvents='auto';
}
function show_hide_password(target){
    var input = document.getElementById('password');
    if (input.getAttribute('type') == 'password') {
        target.classList.add('view');
        input.setAttribute('type', 'text');
    } else {
        target.classList.remove('view');
        input.setAttribute('type', 'password');
    }
    return false;
}
