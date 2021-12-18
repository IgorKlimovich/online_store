

let tit=document.getElementById('title')
let a1=document.getElementById('a1');
let a2=document.getElementById('a2');
let a3=document.getElementById('a3');
let a4=document.getElementById('a4');
let a5=document.getElementById('a5');
let a6=document.getElementById('a6');


if (tit.textContent==='Главная'){
    if (a1===null){
        console.log('null')
    }else
    {a1.style.color='white';}
    a2.style.color='black';
    a3.style.color='white';
    a4.style.color='white';
    a5.style.color='white';
    a6.style.color='white';

}
if (tit.textContent==='Админ'){
    if (a1===null){
        console.log('null')
    }else
    { a1.style.color='black';}
    a2.style.color='white';
    a3.style.color='white';
    a4.style.color='white';
    a5.style.color='white';
    a6.style.color='white';

}
if (tit.textContent==='Про нас'){
    if (a1===null){
        console.log('null')
    }else
    {a1.style.color='white';}
    a2.style.color='white';
    a3.style.color='white';
    a4.style.color='white';
    a5.style.color='white';
    a6.style.color='black';

}
if (tit.textContent==='Заказы'){
    if (a1===null){
        console.log('null')
    }else
    {a1.style.color='white';}
    a2.style.color='white';
    a3.style.color='white';
    a4.style.color='black';
    a5.style.color='white';
    a6.style.color='white';

}
if (tit.textContent==='Контакты'){
    if (a1===null){
        console.log('null')
    }else
    {a1.style.color='white';}
    a2.style.color='white';
    a3.style.color='white';
    a4.style.color='white';
    a5.style.color='black';
    a6.style.color='white';

}

if (tit.textContent==='Корзина'){
    if (a1===null){
        console.log('null')
    }else
    {a1.style.color='white';}
    a2.style.color='white';
    a3.style.color='white';
    a4.style.color='black';
    a5.style.color='white';
    a6.style.color='white';
}

if (tit.textContent==='Товары'){
    if (a1===null){
        console.log('null')
    }else
    {a1.style.color='white';}
    a2.style.color='white';
    a3.style.color='black';
    a4.style.color='white';
    a5.style.color='white';
    a6.style.color='white';
}

if (tit.textContent==='Пользователи'){
    if (a1===null){
        console.log('null')
    }else
    {a1.style.color='black';}
    a2.style.color='white';
    a3.style.color='white';
    a4.style.color='white';
    a5.style.color='white';
    a6.style.color='white';
}
if (tit.textContent==='Пользователь'){
    a1.style.color='black';
    a2.style.color='white';
    a3.style.color='white';
    a4.style.color='white';
    a5.style.color='white';
    a6.style.color='white';
}

if (tit.textContent==='Товар'){
    if (a1===null){
        console.log('null')
    }else
    {a1.style.color='white';}
    a2.style.color='white';
    a3.style.color='black';
    a4.style.color='white';
    a5.style.color='white';
    a6.style.color='white';
}
if (tit.textContent==='Мой профиль'){
    if (a1===null){
        console.log('null')
    }else
    {a1.style.color='white';}
    a2.style.color='white';
    a3.style.color='white';
    a4.style.color='white';
    a5.style.color='white';
    a6.style.color='white';
}

let list=document.getElementById('list');

function openListProfile(){
    document.getElementById("list").classList.toggle("show");
}

function closeListProfile(){
    list.style.display='none';
}
window.onclick = function(event) {
    if (!event.target.matches('.dropdown-toggle')) {
        var dropdowns = document.getElementsByClassName("dropdown-menu");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}

let openModalDeleteProfile= document.getElementById('openModalDeleteProfile')
let openModalSignOut= document.getElementById('openModalSignOut')

function openModelDeleteProfile(){
    openModalDeleteProfile.style.opacity='1';
    openModalDeleteProfile.style.pointerEvents='auto';
}

function closeModalDeleteProfile(){
    openModalDeleteProfile.style.opacity='0';
    openModalDeleteProfile.style.pointerEvents='none';
}

function closeModalDeleteProfileButton(){
    openModalDeleteProfile.style.opacity='0';
    openModalDeleteProfile.style.pointerEvents='none';
}

function openModalSignOuts(){
    openModalSignOut.style.opacity='1';
    openModalSignOut.style.pointerEvents='auto';
}

function closeModalSignOut(){
    openModalSignOut.style.opacity='0';
    openModalSignOut.style.pointerEvents='none';
}

function closeModalSignOutButton(){
    openModalSignOut.style.opacity='0';
    openModalSignOut.style.pointerEvents='none';
}







