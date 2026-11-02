function ingresar(){

    let usuario = document.getElementById("usuario").value;

    let contrasena = document.getElementById("contrasena").value;

//Paciente 

    if(usuario=="Paciente" && contrasena=="1234"){

        alert("Bienvenido al sistema PyP paciente✨​");
        window.location.href="panelPaciente.html";

    }
    //Personal de salud

    else if(usuario=="Salud" && contrasena=="1234"){

        alert("Bienvenido al sistema PyP-Personal de salud✨​");
        window.location.href="panelSalud.html";

    }

    else{

        alert("Usuario o contraseña incorrectos.");
    }
}

function registrar(){

    let nombre = document.getElementById("nombre").value;
    let apellido = document.getElementById("apellido").value;
    let documento = document.getElementById("documento").value;
    let correo = document.getElementById("correo").value;
    let fecha = document.getElementById("fecha").value;
    let sexo = document.getElementById("sexo").value;
    let telefono = document.getElementById("telefono").value;
    let direccion = document.getElementById("direccion").value;
    let usuario = document.getElementById("usuarioNuevo").value;
    let password = document.getElementById("passwordNueva").value;
    let confirmar = document.getElementById("confirmarPassword").value;

    if(
        nombre=="" ||
        apellido=="" ||
        documento=="" ||
        correo=="" ||
        fecha=="" ||
        sexo=="" ||
        telefono=="" ||
        direccion=="" ||
        usuario=="" ||
        password=="" ||
        confirmar==""
    ){

        alert("Debe completar todos los campos.");
        return;

    }

    if(password != confirmar){

        alert("Las contraseñas no coinciden.");
        return;

    }

    alert("Usuario registrado correctamente.");

    window.location.href="index.html";

}
