let flag=document.getElementById('flag');
let modal=document.getElementById('openModal');
if (flag.value==='1'){
    modal.style.opacity='1';
    modal.style.pointerEvents='auto';
}

function closeModal(){
    modal.style.opacity='0';
    modal.style.pointerEvents='none';
}
function closeModalButton() {
    modal.style.opacity='0';
    modal.style.pointerEvents='none';

}

function openModal() {
    modal.style.opacity='1';
    modal.style.pointerEvents='auto';

}