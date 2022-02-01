let flag=document.getElementById('flag');
let modal=document.getElementById('openModalCategory');
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
function search() {
    // Объявить переменные
    var input, filter, table, tr, td, i, txtValue, a;
    input = document.getElementById("myInput");
    filter = input.value.toUpperCase();
    table = document.getElementById("myTable");
    tr = table.getElementsByTagName("tr");

    // Перебирайте все строки таблицы и скрывайте тех, кто не соответствует поисковому запросу
    for (i = 1; i < tr.length; i++) {
        console.log(tr.length);
        td = tr[i].getElementsByTagName("td")[0];
        console.log(td);
        a= td.getElementsByTagName("a")[0];
        console.log(a);

        if (a) {
            txtValue = a.textContent || a.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}