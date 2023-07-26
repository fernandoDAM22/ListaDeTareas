// Call the dataTables jQuery plugin
$(document).ready(function() {
        
}); 

//Boton que permite al usuario registrarse
const registerButton = document.getElementById('registerButton')
registerButton.addEventListener("click",registrarUsuario)

/**
 * Esta funcion permite al usuario registrarse
 * @example
 * registrarUsuario()
 */
async function registrarUsuario() {
    //creamos el objeto con los datos del usuario
    let data = {}
    data.nombre = document.getElementById('txtNombre').value
    data.apellido = document.getElementById('txtApellido').value
    data.email = document.getElementById('txtEmail').value
    data.password = document.getElementById('txtPassword').value

    let repetirPassword = document.getElementById('txtRepeatPassword').value
    if(repetirPassword != data.password){
        alert("Las contraseñas no coinciden");
        return;
    }
    //realizamos la peticion
    const request = await fetch('api/usuarios', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    //obtenemos la respuesta
    let text = await request.text()
    console.log(text)
    const statusCode  = request.status
    if(statusCode == 409){
        alert(text)
    }else{
        alert(text)
        window.location.href = 'login.html'
    }
}
