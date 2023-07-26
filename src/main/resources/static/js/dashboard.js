
document.addEventListener("DOMContentLoaded", function () {
  actualizarEmailUsuario();
  obtenerPorcentajeTareasCompletadas();
  obtenerPorcentajeTareasPendientes();
  obtenerTareasCompletadas();
  obtenerTareasPendientes();
});


//------------------ELEMENTOS------------------------//
//tareas completadas
const txtPorcentajeTareasCompletadas = document.querySelector("#txtPorcentajeTareasCompletadas")
const barraTareasCompletadas = document.querySelector("#barraTareasCompletadas")
const txtTareasCompletadas = document.querySelector("#txtTareasCompletadas")
//tareas pendientes
const txtPorcentajeTareasPendientes = document.querySelector("#txtPorcentajeTareasPendientes")
const barraTareasPendientes = document.querySelector("#barraTareasPendientes")
const txtTareasPendientes = document.querySelector("#txtTareasPendientes")
//Porcentaje tareas extremas
const txtPrioridadExtrema = document.querySelector("#txt_prioridad_extrema")
const barraPrioridadExtrema = document.querySelector("#barra_prioridad_extrema")
//Porcentaje tareas altas
const txtPrioridadAlta = document.querySelector("#txt_prioridad_alta")
const barraPrioridadAlta = document.querySelector("#barra_prioridad_alta")
//porcentaje tareas medias
const txtPrioridadMedia = document.querySelector("#txt_prioridad_media")
const barraPrioridadMedia = document.querySelector("#barra_prioridad_media")
//Porcentaje Tareas Medias
const txtPrioridadBaja = document.querySelector("#txt_prioridad_baja")
const barraPrioridadBaja = document.querySelector("#barra_prioridad_baja")

//------------------FUNCIONES PARA LAS ESTADISTICAS-----------------------------------------//
/**
 * Esta funcion permite obtener el procentaje de tareas completadas
 * @example
 * obtenerPorcentajeTareasCompletadas()
 */
const obtenerPorcentajeTareasCompletadas = async () => {
  let email = localStorage.email;
  const response = await fetch(`api/tareas/estadisticas/porcentaje/completadas/${email}`, {
    method: 'GET',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': localStorage.token,
    },
  });
  let porcentaje = await response.text();
  porcentaje = Math.trunc(porcentaje)
  let texto = porcentaje + '%'
  txtPorcentajeTareasCompletadas.innerHTML = texto
  barraTareasCompletadas.style.width = texto

}
/**
 * Esta funcion permite obtener el numero de tareas completadas
 * @example
 * obtenerTareasCompletadas()
 */
const obtenerTareasCompletadas = async () => {
  let email = localStorage.email;
  const response = await fetch(`api/tareas/estadisticas/completadas/${email}`, {
    method: 'GET',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': localStorage.token,
    },
  });
  let tareasCompletadas = await response.text();
  txtTareasCompletadas.innerHTML = tareasCompletadas
}
/**
 * Esta funcion permite obtener el procentaje de tareas pendientes
 * @example
 * obtenerPorcentajeTareasPendientes()
 */
const obtenerPorcentajeTareasPendientes = async () => {
  let email = localStorage.email;
  const response = await fetch(`api/tareas/estadisticas/porcentaje/pendientes/${email}`, {
    method: 'GET',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': localStorage.token,
    },
  });
  let porcentaje = await response.text();
  porcentaje = Math.trunc(porcentaje)
  let texto = porcentaje + '%'
  txtPorcentajeTareasPendientes.innerHTML = texto
  barraTareasPendientes.style.width = texto
}
/**
 * Esta metodo permite obtener el numero de tareas pendientes
 * @example
 * obtenerTareasPendientes()
 */
const obtenerTareasPendientes = async () => {
  let email = localStorage.email;
  const response = await fetch(`api/tareas/estadisticas/pendientes/${email}`, {
    method: 'GET',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': localStorage.token,
    },
  });
  let tareasPendientes = await response.text();
  txtTareasPendientes.innerHTML = tareasPendientes
}
/**
 * Esta funcion permite obtejer el porcentaje de tareas completadas de una determinada prioridad
 * @param {text} prioridad - Es la prioridad de las tareas de la cual queremos obtener el porcentaje
 * @param {Element} txt - es el elemento html donde se coloca el porcentaje
 * @param {Element} barra - es el elemento html que representa en forma de barra el porcentaje
 * @example
 * obtenerPorcentajePrioridad("Alta",txt_prioridad_alta,barra_prioridad_alta)
 */
const obtenerPorcentajePrioridad = async (prioridad, txt, barra) => {
  let email = localStorage.email;
  const response = await fetch(`api/tareas/estadisticas/porcentaje/prioridad/${email}`, {
    method: 'GET',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': localStorage.token,
      'Prioridad': prioridad
    },
  });
  let texto = await response.text();
  texto = Math.trunc(texto)
  let porcentaje = texto + '%';
  txt.innerHTML = porcentaje
  barra.style.width = porcentaje
  barra.setAttribute("aria-valuenow", texto);
}

/**
 * Esta funcion permite actualizar el email del usuario en la pagina
 */
function actualizarEmailUsuario() {
  document.getElementById('txt_email_user').outerHTML = localStorage.email
}



//Obtener los porcentajes de las tareas completadas de cada prioridad
obtenerPorcentajePrioridad("Extrema", txtPrioridadExtrema, barraPrioridadExtrema)
obtenerPorcentajePrioridad("Alta", txtPrioridadAlta, barraPrioridadAlta)
obtenerPorcentajePrioridad("Media", txtPrioridadMedia, barraPrioridadMedia)
obtenerPorcentajePrioridad("Baja", txtPrioridadBaja, barraPrioridadBaja)