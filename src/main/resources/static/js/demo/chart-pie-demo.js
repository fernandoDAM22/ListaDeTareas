
// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

/**
 * Esta funcion permite obtener los datos que se van a colocar en el grafico
 * @returns la informacion que se coloca en el grafico
 * @example
 * let data = obtenerDatosGrafico()
 */
const obtenerDatosGrafico = async () =>{
  let email = localStorage.email;
  const response = await fetch(`api/tareas/estadisticas/grafico/${email}`, {
    method: 'GET',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': localStorage.token,
    },
  });
  let data = await response.text();
  return JSON.parse(data); // Convertir la cadena de texto en un array
}
/**
 * Esta funcion permite cargar los datos en el grafico
 * @example cargarDatos()
 */
const cargarDatos = async () =>{
  let response = await obtenerDatosGrafico();
  console.log(response);
  if(response != undefined || response != null){
    data = response;
    // Actualizar el gráfico con los nuevos datos
    myPieChart.data.datasets[0].data = data;
    myPieChart.update();
  }
}
cargarDatos();

// Pie Chart Example
var ctx = document.getElementById("myPieChart");
var myPieChart = new Chart(ctx, {
  type: 'doughnut',
  data: {
    labels: ["Extremas", "Altas", "Medias","Bajas"],
    datasets: [{
      data:[50,50,50,50],
      backgroundColor: ['#5a5c69', '#e74a3b', '#f6c23e','#1cc88a'],
      hoverBackgroundColor: ['#484A53', '#FA3523', '#F7B713','#0FA972'],
      hoverBorderColor: "rgba(234, 236, 244, 1)",
    }],
  },
  options: {
    maintainAspectRatio: false,
    tooltips: {
      backgroundColor: "rgb(255,255,255)",
      bodyFontColor: "#858796",
      borderColor: '#dddfeb',
      borderWidth: 1,
      xPadding: 15,
      yPadding: 15,
      displayColors: false,
      caretPadding: 10,
    },
    legend: {
      display: false
    },
    cutoutPercentage: 80,
  },
});


