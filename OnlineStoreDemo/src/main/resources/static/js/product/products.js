// var input,search,pr,result,result_arr, locale_HTML, result_store;
//
// locale_HTML = document.body.innerHTML;   // сохраняем в переменную весь body (Исходный)
//
// function findOnPage(name, status) {
//
//     input = document.getElementById(name).value; //получаем значение из поля в html
//     input = numer.replace(/^\s+/g,'');
//     input = numer.replace(/[ ]{1,}/g,' ');
//
//     if(input.length<3&&status==true) {
//         alert('Для поиска вы должны ввести три или более символов');
//         function FindOnPageBack() { document.body.innerHTML = locale_HTML; }   //обнуляем стили
//     }
//
//     if(input.length>=3)
//     {
//         function FindOnPageGo() {
//             search = '/'+input+'/g';  //делаем из строки регуярное выражение
//             pr = document.body.innerHTML;   // сохраняем в переменную весь body
//             result = pr.match(/>(.*?)</g);  //отсекаем все теги и получаем только текст
//             result_arr = [];   //в этом массиве будем хранить результат работы (подсветку)
//
//             for(var i=0; i<result.length;i++) {
//                 result_arr[i] = result[i].replace(eval(search), '<span style="background-color:yellow;">'+input+'</span>'); //находим нужные элементы, задаем стиль и сохраняем в новый массив
//             }
//             for(var i=0; i<result.length;i++) {
//                 pr=pr.replace(result[i],result_arr[i])  //заменяем в переменной с html текст на новый из новогом ассива
//             }
//             document.body.innerHTML = pr;  //заменяем html код
//         }
//     }
//     function FindOnPageBack() { document.body.innerHTML = locale_HTML; }   //обнуляем стили
//     if(status) { FindOnPageBack(); FindOnPageGo(); } //чистим прошлое и Выделяем найденное
//     if(!status) { FindOnPageBack(); } //Снимаем выделение
// }
//
// function myFunction() {
//     // Объявить переменные
//     var input, filter, table, tr, td, i, txtValue;
//     input = document.getElementById("myInput");
//     filter = input.value.toUpperCase();
//     table = document.getElementById("myTable");
//     tr = table.getElementsByTagName("tr");
//
//     // Перебирайте все строки таблицы и скрывайте тех, кто не соответствует поисковому запросу
//     for (i = 0; i < tr.length; i++) {
//         td = tr[i].getElementsByTagName("td")[0];
//
//         if (td) {
//             txtValue = td.textContent || td.innerText;
//             if (txtValue.toUpperCase().indexOf(filter) > -1) {
//                 tr[i].style.display = "";
//             } else {
//                 tr[i].style.display = "none";
//             }
//         }
//     }
// }

function search() {
    // Объявить переменные
    var input, filter, table, tr, td, i, txtValue, h3;
    input = document.getElementById("myInput");
    filter = input.value.toUpperCase();
    table = document.getElementById("myTable");
    tr = table.getElementsByTagName("tr");

    // Перебирайте все строки таблицы и скрывайте тех, кто не соответствует поисковому запросу
    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[0];
        h3= td.getElementsByTagName("h3")[0];
        if (h3) {
            txtValue = h3.textContent || h3.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}