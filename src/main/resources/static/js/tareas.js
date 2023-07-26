
document.addEventListener("DOMContentLoaded", function () {
  obtenerTareas();
  //document.getElementById('tareas').classList.add('dataTable'); // Agrega la clase "dataTable" para dar estilo a la tabla (si es necesario).
  actualizarEmailUsuario();
});

//-------------BOTONES DE ORDENACION---------------------------
const botones = document.querySelectorAll('.sorting')
console.log(botones)
//btn para insertar una tarea
const addButton = document.querySelector("#btn-add")
//inputs del formulario de la tarea
const nombre = document.querySelector("#nombreTarea")
const fechaLimite = document.querySelector("#fechaLimite")
const prioridad = document.querySelector("#prioridad")

//colocar la fecha del input fechaLimite por defecto a la fecha de hoy
const fechaActual = new Date().toISOString().split('T')[0];
fechaLimite.value = fechaActual

/**
 * Esta funcion permite actualizar el email del usuario en la pagina
 */
function actualizarEmailUsuario() {
  document.getElementById('txt_email_user').outerHTML = localStorage.email
}

/**
 * Estaf funcion permite formatear la fecha a formato YYYY-MM-DD
 * @param {Iterable} tarea - La tarea a la que le queremos formatear la fecha
 * @example
 * formatearFecha(fecha)
 */
const formatearFecha = tarea => {
  const fechaLimite = new Date(tarea.fechaLimite);
  const fechaFormateada = fechaLimite.toISOString().split('T')[0];
  tarea.fechaLimite = fechaFormateada;
}
/**
 * Esta funcion permite eliminar una tarea
 * @param {number} id - es el id de la tarea que vamos a eliminar
 * @example
 * deleteTask(1)
 */
const deleteTask = async (id,button) => {
  if (!confirm("Estas seguro de que quieres eliminar la tarea?")) {
    return;
  }
  const response = await fetch(`api/tareas/${id}`, {
    method: 'DELETE',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': localStorage.token,
    },
  });
  statusCode = response.status;
  if (statusCode == 204) {
    alert("Tarea eliminada correctamente")
    const fila = button.parentNode.parentNode;
    fila.remove();
  } else {
    let text = await response.text()
    alert(text)
  }
}

/**
 * Esta funcion permite cargar las tareas en la tabla
 * @param {Iterable} data 
 * @example 
 * cargarTareas(data)
 */
const cargarTareas = (data) => {
  for (let tarea of data) {
    anadirTarea(tarea)
  }
};

/**
 * Esta funcion pemite anadir una tarea a la tabla
 * @param {JSON} tarea - Es la tarea que se va anadir a la tabla 
 */
const anadirTarea = tarea => {
  let text = '';
  let deleteButton = `<a href="#" onclick="deleteTask(${tarea.id},this)" class="btn btn-danger btn-circle"><i class="fas fa-trash"></i></a>`;
  let editButton = `<a href="#" onclick="editTask(${tarea.id})" class="btn btn-info btn-circle btnEdit"><i class="fas fa-pencil-alt"></i></a>`;
  let completeButton;
  //en funcion de si la tarea esta completada o no ponemos un boton o otro
  if (tarea.completada == 0) {
    completeButton = `<a href="#" onclick="updateTask(${tarea.id})" class="btn btn-success btn-circle"><i class="fas fa-check"></i></a>`
  } else {
    completeButton = `<a href="#" onclick="updateTask(${tarea.id})" class="btn btn-warning btn-circle"><i class="fas fa-bookmark"></i></a>`
  }
  const borde = obtenerColorBorde(tarea.prioridad);
  let tareaHtml = `
    <tr data-taskid="${tarea.id}" class="${borde}">
      <td>${tarea.id}</td>
      <td>${tarea.nombre}</td>
      <td>${tarea.fechaLimite}</td>
      <td>${tarea.prioridad}</td>
      <td>${deleteButton} ${editButton} ${completeButton}</td>
    </tr>`;
  text += tareaHtml;
  document.querySelector('#tareas tbody').innerHTML += text;

}
/**
 * Esta funcion permimte obtener el color del borde de la fila de 
 * la tabla que contiene la tarea en funcion de su prioridad
 * @param {text} prioridad 
 * @returns la clase que se le debe asignar a la fila de la tabla
 */
const obtenerColorBorde = prioridad =>{
  switch(prioridad){
    case 'Baja':
      return 'border-left-success'
    case 'Media':
      return 'border-left-warning'
    case 'Alta':
      return 'border-left-danger'
    case 'Extrema':
      return 'border-left-dark'
  }
}

/**
 * Esta funcion permite actualizar una tarea
 * @param {number} taskId - Es el id de la la tarea que queremos modificar ´
 * @example
 * updateTask(12)
 * 
 */
const updateTask = async taskId => {
  const response = await fetch(`api/tareas/modificar`, {
    method: 'PUT',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': localStorage.token,
    },
    body: JSON.stringify(taskId)
  });
  let statusCode = response.status;
  let text = await response.text();

  alert(text)
  if (statusCode != 200) {
    return;
  }
  const completeButton = document.querySelector(`[onclick="updateTask(${taskId})"]`);

  if (completeButton.classList.contains('btn-success')) {
    // Si el botón actual es el botón de completar tarea, cambia al botón de marcado
    completeButton.outerHTML = `<a href="#" onclick="updateTask(${taskId})" class="btn btn-warning btn-circle btnComplete"><i class="fas fa-bookmark"></i></a>`;
  } else {
    // Si el botón actual es el botón de marcado, cambia al botón de completar tarea
    completeButton.outerHTML = `<a href="#" onclick="updateTask(${taskId})" class="btn btn-success btn-circle btnComplete"><i class="fas fa-check"></i></a>`;
  }
}

/**
 * Esta funcion permite modificar una tarea
 * @param {number} taskId - es el id de la tarea que se va a modificar 
 * @example
 * editTask(1)
 */
// Función para editar una tarea
const editTask = async taskId => {
  const row = document.querySelector(`#tareas tr[data-taskid="${taskId}"]`);
  const tds = row.querySelectorAll('td:not(:first-child):not(:last-child)'); // Excluye la primera columna (ID) y la última celda de la fila (botones)

  // Convertir las celdas en editables
  tds.forEach(td => {
    const cellValue = td.textContent;
    const columnIndex = td.cellIndex;

    if (columnIndex === 2) {
      // Si es la tercera celda (fecha límite), crear un input tipo date con el valor por defecto
      // Formatear el valor de la fecha a 'YYYY-MM-DD'
      const formattedDate = cellValue.split('/').reverse().join('-');
      td.innerHTML = `<input type="date" value="${formattedDate}">`;
    } else if (columnIndex === 3) {
      // Si es la cuarta celda (prioridad), crear un campo de selección con las opciones
      td.innerHTML = `
        <select class="form-control" id="prioridad" name="prioridad" required>
          <option value="Baja" ${cellValue === 'Baja' ? 'selected' : ''}>Baja</option>
          <option value="Media" ${cellValue === 'Media' ? 'selected' : ''}>Media</option>
          <option value="Alta" ${cellValue === 'Alta' ? 'selected' : ''}>Alta</option>
          <option value="Extrema" ${cellValue === 'Extrema' ? 'selected' : ''}>Extrema</option>
        </select>`;
    } else {
      // Para las demás celdas, crear inputs tipo text con el valor por defecto
      td.innerHTML = `<input type="text" value="${cellValue}">`;
    }
  });

  // Cambiar el botón de edición a botón de guardar
  const editButton = row.querySelector('.btnEdit');
  editButton.innerHTML = '<i class="fas fa-save"></i>';
  editButton.onclick = () => saveChanges(taskId);
};

/**
 * Esta funcion permite persistir los cambios en una tarea
 * @param {number} taskId - es el id de la tarea modificada sin persistir,
 * la cual vamos a persistir 
 */
// Función para guardar los cambios editados
const saveChanges = async taskId => {
  const row = document.querySelector(`#tareas tr[data-taskid="${taskId}"]`);
  const tds = row.querySelectorAll('td:not(:first-child):not(:last-child)'); // Excluye la primera columna (ID) y la última celda de la fila (botones)

  // Obtener el id de la tarea
  const id = taskId;

  // Obtener el nombre de la tarea desde el DOM
  const name = tds[0].querySelector('input').value;

  // Obtener los nuevos valores de los campos de entrada y selección
  const fechaLimiteValue = tds[1].querySelector('input').value; // Obtiene el valor del campo de entrada tipo "date"
  const prioridadValue = tds[2].querySelector('select').value; // Obtiene el valor del campo de selección

  // Aquí puedes realizar la lógica para enviar la solicitud al backend y persistir los cambios.
  // Por ejemplo, puedes usar fetch() para hacer una petición POST o PUT al backend con los datos actualizados.
  data = {
    "id": taskId,
    "nombre": name,
    "fechaLimite": fechaLimiteValue,
    "prioridad": prioridadValue
  }
  const response = await fetch(`api/tareas`, {
    method: 'PUT',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': localStorage.token,
    },
    body: JSON.stringify(data)
  });

  const statusCode = response.status
  const text = await response.text();
  if (statusCode != 200) {
    alert(text)
    return
  }
  if (!confirm("Estas seguro de que quieres modificar la tarea?")) {
    return
  }

  // Actualizar las celdas con los nuevos valores
  tds[0].textContent = name;
  tds[1].textContent = fechaLimiteValue;
  tds[2].textContent = prioridadValue;

  console.log(tds)

  // Cambiar el botón de guardar a botón de edición
  const editButton = row.querySelector('.btnEdit');
  editButton.innerHTML = '<i class="fas fa-pencil-alt"></i>';
  editButton.onclick = () => editTask(taskId);

  row.classList.forEach(clase => {
    row.classList.remove(clase);
  });
  const borde = obtenerColorBorde(prioridadValue);
  row.classList.add(borde);
};

/**
 * Esta funcion permite obtener todas las tareas del usuario registrado
 * @example
 * obtenerTareas()
 */
async function obtenerTareas() {
  const emailUsuario = localStorage.email;
  try {
    const response = await fetch(`api/tareas/${emailUsuario}`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token,
      },
    });

    if (!response.ok) {
      throw new Error('Ha ocurrido un error en la solicitud.');
    }

    const data = await response.json();
    data.forEach((tarea) => {
      formatearFecha(tarea)
    });
    cargarTareas(data)
    console.log(data);

    // Formatear la fecha en cada tarea para mostrar solo el año, mes y día
  } catch (error) {
    console.error('Error:', error.message);
  }
}

/**
 * Esta funcion permite insertar una tarea
 * @example
 * insertarTarea()
 */
const insertarTarea = async () => {
  data = {
    "nombre": nombre.value,
    "fechaLimite": fechaLimite.value,
    "prioridad": prioridad.value
  }
  const response = await fetch(`api/tareas`, {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': localStorage.token,
    },
    body: JSON.stringify(data)
  });

  let statusCode = response.status
  let text = await response.text();
  alert(text)
  if (statusCode == 201) {
    colocarNuevaTarea(data.nombre)
  }
}
const colocarNuevaTarea = async nombre => {
  const response = await fetch(`api/tareas/obtener/${nombre}`, {
    method: 'GET',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': localStorage.token,
    },
  });
  const tarea = await response.json();
  formatearFecha(tarea)
  anadirTarea(tarea)

}

//anadir el eventListener al boton que permite inserta una tarea
addButton.addEventListener("click", insertarTarea)




