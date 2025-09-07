const form = document.getElementById('form');
const password = document.getElementById('password');
const confirm_password = document.getElementById('confirm_password');
const submit = document.getElementById("submit");
const p_error = document.getElementById("p_error");
const p_info = document.getElementById("p_info");
const showPass = document.getElementById("showPass");
const showconPass = document.getElementById("showConPass");
const libtxtdiv=document.getElementById("textar_div");
const libnamediv=document.getElementById("libname_div");
const general_error=document.getElementById("general_error");
var pass_weak=0;
var user="";

showPass.addEventListener("click", function(e){
    if(password.getAttribute('type')==='password'){
        password.setAttribute('type', 'text');
        showPass.classList.toggle("fa-eye");
    }else{
        password.setAttribute('type', 'password');
    }
})

showconPass.addEventListener("click", function(e){
    if(confirm_password.getAttribute('type')==='password'){
        confirm_password.setAttribute('type', 'text');
        showconPass.classList.toggle("fa-eye");
    }else{
        confirm_password.setAttribute('type', 'password');
    }
})

function insert_sym(str, char){
    for(let i=0; i<str.length; i++){
        if(char==str.charAt(i)) return str;
    }
    str+=char;
    return str;
}

function  check_symbols(pass){
    let tmp=pass.replace(/[a-zA-Z0-9]/g, '');
    let ind=0;
    let found="";

    for(var si=0; si<tmp.length; si++){
        for(var i=0; i<pass.length;i++){
            if(pass.charAt(i)==tmp.charAt(si) )
                found=insert_sym(found, tmp.charAt(si))
        }
    }
    if(found.length>1) return true;
    else return false ;
}

password.addEventListener("change", function(e){
    if(p_info.style.visibility=="visible") p_info.style.visibility="hidden"
    let pass=password.value;
    if(pass=="helmepa" || pass=="uoc" || pass=="tuc"){
        p_info.style.visibility="visible"
        p_info.innerHTML="Cannot use that password. Try again.";
        return;
    }
    if(pass.match(/[a-z]/g)!=null && pass.match(/[A-Z]/g)!=null && check_symbols(pass)){
        p_info.style.visibility="visible";
        p_info.style.color="lime";
        p_info.innerHTML="Strong password";
        pass_weak=0;
    }else if(pass.match(/[0-9]/g) && pass.match(/[0-9]/g).length>(pass.length/2)){ 
        p_info.style.visibility="visible";
        p_info.style.color="red";
        p_info.innerHTML="Weak Password";
        pass_weak=1;
    }else{
        p_info.style.visibility="visible";
        p_info.style.color="orange";
        p_info.innerHTML="Medium Password";
        pass_weak=0;
    }
})

confirm_password.addEventListener("change", function(e){
    if(p_error.style.visibility=="visible") p_error.style.visibility="hidden"
    if(password.value!=confirm_password.value){
        p_error.style.visibility="visible";
        p_error.innerHTML="Passwords does not match. Try again.";
    }
})

submit.addEventListener("click", function(e){
    let flag=1;
    if(pass_weak) e.preventDefault();
    else if(e.defaultPrevented) e.submit();
    if(user=="student"){
        var unicol = document.forms["myForm"]["univercity"].value;
        var email = document.getElementById("email").value;
        if(email.substr(email.length - (unicol.length+3))!=unicol+".gr"){
            e.preventDefault();
            flag=0;
            setError("Email must end with '"+unicol+".gr'");
        }
        if(dates_check()){
            e.preventDefault();
            flag=0;
            setError("Starting date must not be greater than the ending date");
        }
        if(id_duration()){
            e.preventDefault();
            flag=0;
            setError("Id duration does not match the type of student");
        }
        if(flag && e.defaultPrevented){
            general_error.style.visibility="hidden";
            e.submit();
        }
    }
})

function setError(str){
    general_error.style.visibility="visible"
    general_error.innerHTML=str;
}

function id_duration(){
    let st = document.getElementById("st_date").value;
    let end= document.getElementById("end_date").value;
    let st_date= new Date(st);
    let end_date = new Date(end);
    let duration = end_date.getFullYear()-st_date.getFullYear();

    if(document.getElementById("undergraduate").checked===true && duration<=6)
        return false;
    else if(document.getElementById("postgraduate").checked===true && duration<=2)
        return false;
    else if(document.getElementById("doctoral").checked===true && duration<=5)
        return false;
    return true;
}

function dates_check(){
    let st = document.getElementById("st_date").value;
    let end= document.getElementById("end_date").value;
    let st_date= new Date(st);
    let end_date = new Date(end);
    if(end_date>st_date)
        return false;
    return true;
}

function handleStud(){
    user="student";
    
    document.getElementById("AID_div").setAttribute('required', '');
    document.getElementById("stdate_div").setAttribute('required', '');
    document.getElementById("enddate_div").setAttribute('required', '');
    document.getElementById("address").innerHTML="Home address";
    
    document.getElementById("STtype_div").hidden = false;
    document.getElementById("AID_div").hidden = false;
    document.getElementById("stdate_div").hidden = false;
    document.getElementById("enddate_div").hidden = false;
    document.getElementById("unicol_div").hidden = false;
    document.getElementById("unicoldep_div").hidden = false;

    document.getElementById("libname_div").hidden = true;
    document.getElementById("libname").removeAttribute('required');

    document.getElementById("textar_div").hidden = true;
    document.getElementById("libtxt").removeAttribute('required');
}

function handleLib(){
    user="librarian";
    
    document.getElementById("aid").removeAttribute('required');
    document.getElementById("st_date").removeAttribute('required');
    document.getElementById("end_date").removeAttribute('required');
    document.getElementById("address").innerHTML="Library address";
    
    document.getElementById("STtype_div").hidden = true;
    document.getElementById("AID_div").hidden = true;
    document.getElementById("stdate_div").hidden = true;
    document.getElementById("enddate_div").hidden = true;
    document.getElementById("unicol_div").hidden = true;
    document.getElementById("unicoldep_div").hidden = true;

    document.getElementById("libname_div").hidden = false;
    document.getElementById("libname").setAttribute('required', '');

    document.getElementById("textar_div").hidden = false;
    document.getElementById("libtxt").setAttribute('required', '');
}

function check_address(){
    const data = null;
    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            const obj = JSON.parse(xhr.responseText);
            var text=obj[0].display_name;
            console.log(text);
        }
    });
    var addressName=document.getElementById("addr").value;
    var city=document.getElementById("city").value;
    var country=document.getElementById("country").value;
    var address=addressName+" " +city+" "+country;
    
    xhr.open("GET", "https://forward-reverse-geocoding.p.rapidapi.com/v1/search?q="+address+"&accept-language=en&polygon_threshold=0.0");
    xhr.setRequestHeader("x-rapidapi-host", "forward-reverse-geocoding.p.rapidapi.com");
    xhr.setRequestHeader("x-rapidapi-key", "8208de91e2msh27de0db0932416ep111990jsn11aa2b8a5d2b");
    xhr.send(data);
}