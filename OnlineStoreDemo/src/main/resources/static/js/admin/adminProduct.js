let modalUpdateProduct=document.getElementById('openModalUpdateProduct');
let flag = document.getElementById('flag');
let modalDeleteProduct = document.getElementById('openModalDelProduct');
if (flag.value==='2'){
    console.log(flag.value);
    modalUpdateProduct.style.opacity='1';
    modalUpdateProduct.style.pointerEvents='auto';
}



function openModalUpdateProduct() {
    modalUpdateProduct.style.opacity='1';
    modalUpdateProduct.style.pointerEvents='auto';

}



function closeModalUpdateProduct(){
    modalUpdateProduct.style.opacity='0';
    modalUpdateProduct.style.pointerEvents='none';
}
function closeModalUpdateProductButton() {
    modalUpdateProduct.style.opacity='0';
    modalUpdateProduct.style.pointerEvents='none';

}

function openModalDeleteProduct() {
    modalDeleteProduct.style.opacity='1';
    modalDeleteProduct.style.pointerEvents='auto';
}

function closeModalDelProduct() {
    modalDeleteProduct.style.opacity='0';
    modalDeleteProduct.style.pointerEvents='none';

}
function closeModalDelProductButton(){
    modalDeleteProduct.style.opacity='0';
    modalDeleteProduct.style.pointerEvents='none';
}
