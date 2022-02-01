let flag=document.getElementById('flag');
let flagDel =document.getElementById('flag-del');
let modal=document.getElementById('openModalCat');
let modalDel= document.getElementById('openModalDel');
let modalDelError= document.getElementById('openModalDelError');
// console.log(modalDelError);
// console.log(flagDel.value);
if (flag===null){
    console.log('null');
}
// else
    if (flag.value==='1'){
    modal.style.opacity='1';
    modal.style.pointerEvents='auto';
}
if (flag.value==='2'){
    console.log(true);
    modalDelError.style.opacity='1';
    modalDelError.style.pointerEvents='auto';
}
function closeModal(){
    modal.style.opacity='0';
    modal.style.pointerEvents='none';
}
function closeModalButton() {
    modal.style.opacity='0';
    modal.style.pointerEvents='none';

}

function openMod() {
    modal.style.opacity='1';
    modal.style.pointerEvents='auto';

}

function openModalDel() {

    console.log(modalDel);

    modalDel.style.opacity='1';
    modalDel.style.pointerEvents='auto';
}


function closeModalDelButton() {
    modalDel.style.opacity='0';
    modalDel.style.pointerEvents='none';
}

function closeModalDel() {
    modalDel.style.opacity='0';
    modalDel.style.pointerEvents='none';
}

function closeModalDelErrorButton() {
    modalDelError.style.opacity='0';
    modalDelError.style.pointerEvents='none';
}

function closeModalDelError() {
    modalDelError.style.opacity='0';
    modalDelError.style.pointerEvents='none';
}


// function openModalDelError() {
//
//     console.log(modalDelError);
//
//     modalDel.style.opacity='1';
//     modalDel.style.pointerEvents='auto';
// }

