function RegisterPOST() {
    let myForm = document.getElementById('myForm');
    let formData = new FormData(myForm);
   
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            alert("Success!");
            $("#myForm").hide();
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    const data={};
    formData.forEach((value, name) => (data[name] = value));
    jsonData=JSON.stringify(data);
    alert(jsonData);
    
    xhr.open('POST', 'Register');
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(jsonData);
}

function loginPOST() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
           //setChoicesForLoggedUser();
           showStudentTab();
           $("#ajaxContent").html("In student");
           $("#ajaxContent").html(createTableFromJSON(JSON.parse(xhr.responseText)));
            //$("#ajaxContent").html("Successful Login");
            alert("Successful Login");
        } else if (xhr.status !== 200) {
             alert("Error Wrong credentials");
            //$("#error").html("Wrong Credentials");
            //('Request failed. Returned status of ' + xhr.status);
        }
    };
    var data = $('#loginForm').serialize();
    xhr.open('POST', 'Login');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send(data);
}

function adminGET(reqtype){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {  
           //const responseData = jQuery.parseJSON(xhr.responseText);
           if(reqtype==="users") $("#UserTable").html(createTableFromJSON(xhr.responseText));
           if(reqtype==="chart1") DataToChart(reqtype,xhr.responseText.toString());
           if(reqtype==="chart2") DataToChart(reqtype,xhr.responseText.toString());
           if(reqtype==="chart3") DataToChart(reqtype,xhr.responseText.toString());
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    xhr.open('GET', 'Admin');
    xhr.setRequestHeader('reqtype',reqtype);
    xhr.send();
}

function adminDELETE(){
    
}

function librarianGET(){
    
}

function librarianPOST(reqtype,id){
    var data="";
    if(reqtype==="addbookstock" || reqtype==="removebookstock"){
        let isbn = document.getElementById("stockisbn").value;
        data = isbn+id;
    }
    if(reqtype==="addbook"){
        let rawdata = document.getElementById("bookdetails");
        let formData = new FormData(rawdata);
        const jsonData={};
        formData.forEach((value, name) => (jsonData[name] = value));
        jsonData["library_id"] = id;
        jsonData["available"] = "false";
        data=JSON.stringify(jsonData);
        data+=id;
    }
    if(reqtype==="changedetails"){
        let rawdata = document.getElementById("accdetails");
        let formData = new FormData(rawdata);
        const jsonData={};
        formData.forEach((value, name) => (jsonData[name] = value));
        data=JSON.stringify(jsonData);
        data+=id;
    }
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {  
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    xhr.open('POST', 'Librarian');
    xhr.setRequestHeader('reqtype',reqtype);
    xhr.send(data);
}

function showLogin() {
    $('body').load("admin.html");
}


function showRegistrationForm() {
    $('body').load("librarian.html");
}

function DataToChart(id,rawdata){
    var temp = rawdata.split("+");
    let data = [];
    for(let x in temp){
        data[x]=temp[x].split(",");
    }
    LoadGoogle(id,data);
}

function createTableFromJSON(data) {
    
    var input = data.toString();
    var temp1= input.replace('[','');
    var temp2= temp1.replace(']','');
    var jsonArr = temp2.split("},");
    jsonArr[8]=jsonArr[8].substring(0,jsonArr[8].length-1);
    var html = "<table id='users'><tr><th>Username</th><th>Lastname</th>\n\
                <th>Firstname</th><th>Type</th><th></th></tr>";
    for(let j in jsonArr){
        var userid = "";
        let json = JSON.parse(jsonArr[j]+"}");
        html+="<tr>";
        for (const x in json) {
            var value = json[x].toString();
            if(x==="username") {
                html += "<td>" + value + "</td>";
                userid = value;
            }
            if(x==="student_id"){
                html += "<td> Student </td>";
            } else if(x==="libraryname"){
                html += "<td> Librarian </td>";
            }
            if(x==="firstname") html += "<td>" + value + "</td>";
            if(x==="lastname") html += "<td>" + value + "</td>";
        }
        html+="<td><button id='"+userid+" ' class='dltbtn'>Delete</button></td></tr>";
    }
        html += "</table>";
    return html;
}

function getBooks(){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            
            const responseData = jQuery.parseJSON(xhr.responseText);
             console.log(typeof responseData) ;
            //alert(xhr.responseText);
            $("#ajaxContent").html(createTableFromJSON(responseData));
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    const data = JSON.stringify({"genre": $('#genre option:selected').text()});
    xhr.open('POST', 'GetBooks');
    xhr.setRequestHeader('Content-type','application/json');
    xhr.send(data);
}