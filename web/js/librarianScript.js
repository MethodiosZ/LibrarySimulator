document.getElementById("addBookDetails").addEventListener("click",function(){
    if(document.getElementById("bookdetails").hidden){
        document.getElementById("bookdetails").hidden = false;
    } else {
        document.getElementById("bookdetails").hidden = true;
    }
});

document.getElementById("accbutton").addEventListener("click",function(){
    if(document.getElementById("accdetails").hidden){
        document.getElementById("accdetails").hidden = false;
    } else {
        document.getElementById("accdetails").hidden = true;
    }
});