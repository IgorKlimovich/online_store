
let modalErrorPay=document.getElementById('openModalErrorPay');
let flag = document.getElementById('flag');
let modalAddCard = document.getElementById('openModalAddCard');
let modalChooseCard = document.getElementById('openModalChooseCard');
let modalErrorNoMoney = document.getElementById('openModalErrorNoMoney');
if (flag.value==='4'){
    console.log(flag.value);
    modalErrorPay.style.opacity='1';
    modalErrorPay.style.pointerEvents='auto';
}
function closeModalErrorPay() {
    modalErrorPay.style.opacity='0';
    modalErrorPay.style.pointerEvents='none';

}
function closeModalErrorPayButton(){
    modalErrorPay.style.opacity='0';
    modalErrorPay.style.pointerEvents='none';
}

if (flag.value==='5'){
    console.log(flag.value);
    modalAddCard.style.opacity='1';
    modalAddCard.style.pointerEvents='auto';
}
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

if (flag.value==='6'){
    console.log(flag.value);
    modalChooseCard.style.opacity='1';
    modalChooseCard.style.pointerEvents='auto';
}

function closeModalChooseCard() {
    modalChooseCard.style.opacity='0';
    modalChooseCard.style.pointerEvents='none';
}

function closeModalChooseCardButton() {
    modalChooseCard.style.opacity='0';
    modalChooseCard.style.pointerEvents='none';
}

if (flag.value==='7'){
    console.log(flag.value);
    modalErrorNoMoney.style.opacity='1';
    modalErrorNoMoney.style.pointerEvents='auto';
}

function closeModalErrorNoMoney() {
    modalErrorNoMoney.style.opacity='0';
    modalErrorNoMoney.style.pointerEvents='none';
}

function closeModalErrorNoMoneyButton() {
    modalErrorNoMoney.style.opacity='0';
    modalErrorNoMoney.style.pointerEvents='none';
}