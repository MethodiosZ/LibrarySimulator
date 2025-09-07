var script = document.createElement("script");
script.type = "text/javascript";
script.src = "https://www.gstatic.com/charts/loader.js";
document.head.appendChild(script);

function LoadGoogle(id,rawdata){
    google.charts.load('current', {
    callback: drawChart,
    packages:['corechart']
  });
  
  function drawChart() { 
    for(let x in rawdata){
        if(x>0){
            for(let y in rawdata[x]){
                if(y>0){
                    rawdata[x][y]= parseInt(rawdata[x][y]);
                }
            }
        }
    }
    var data = new google.visualization.arrayToDataTable(rawdata);
    

    var options = {
        'title':'',
        'width':600,
        'height':400,
        'backgroundColor':'#e8e8e8'
    };

    var chart = new google.visualization.PieChart(document.getElementById(id));
    chart.draw(data, options);
  }
}
   
