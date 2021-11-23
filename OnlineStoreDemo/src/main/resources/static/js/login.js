function myfunc() {
    let label=document.getElementById('log');
    let input=document.getElementById('floatingInput');
    console.log(input.value.length);
    console.log('helo')
    label.style.display='inline';
}

function myfunc1() {
    let label=document.getElementById('log');

    let input=document.getElementById('floatingInput');
    if (input.value.length===0){
        label.style.display='none';
    }
}

function myfunc2() {
    let label1=document.getElementById('log1');
    let input1=document.getElementById('floatingPassword');
    console.log(input1.value.length);
    console.log('helo')
    label1.style.display='inline';
}

function myfunc3() {
    let label1=document.getElementById('log1');

    let input1=document.getElementById('floatingPassword');
    if (input1.value.length===0){
        label1.style.display='none';
    }
}
