let modalErrorAdd=document.getElementById('openModalErrorAdd');
let flag = document.getElementById('flag');
let modalChoosePath = document.getElementById('openModalChoosePath');
if (flag.value==='3'){
    console.log(flag.value);
    modalErrorAdd.style.opacity='1';
    modalErrorAdd.style.pointerEvents='auto';
}
function closeModalErrorAdd() {
    modalErrorAdd.style.opacity='0';
    modalErrorAdd.style.pointerEvents='none';

}
function closeModalErrorAddButton(){
    modalErrorAdd.style.opacity='0';
    modalErrorAdd.style.pointerEvents='none';
}
