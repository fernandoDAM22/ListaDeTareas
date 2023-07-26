$(function() {
    // Handler for .ready() called.
  });


//Boton que permite al usuario inicarSesion
const registerButton = document.getElementById('loginButton')
registerButton.addEventListener("click",iniciarSesion)

/**
 * Esta funcion retorna un objeto con los headers necesarios en la
 * peticion que permite inicar sesion
 * @returns Un objeto con los headers de la peticion
 * @example
 * getHeaders()
 */
const getHeaders = () =>{
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization' : localStorage.token
    }
}
/**
 * Este metodo permite inicar sesion 
 * @example
 * inicarSesion()
 */
async function iniciarSesion() {
    let data = {}
    data.email = document.getElementById('txtEmail').value
    data.password = document.getElementById('txtPassword').value

    const request = await fetch('api/login', {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify(data)

    });
    const response = await request.text()
    console.log(response)
    if(response != 'FAIL'){
        localStorage.token = response;
        localStorage.email = data.email;
        window.location.href = 'tareas.html'
    }else{
        alert("Las credenciales no coinciden")
    }
}

