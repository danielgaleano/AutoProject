
$(document).ready(function(data) {
    $("#botonEditar").hide();
    if(editar){
        $('h1').append('Editar Empresa');
        $('input[id="idEmpresa"]').val(empresa.id);
        $('input[id="ruc"]').prop('disabled', true);
        $('input[id="nombre"]').val(empresa.nombre);
        $('input[id="direccion"]').val(empresa.direccion);
        $('input[id="email"]').val(empresa.email);
        $('input[id="telefono"]').val(empresa.telefono);
        $('input[id="ruc"]').val(empresa.ruc);
        $('input[id="descripcion"]').val(empresa.descripcion);
        $('input[id="telefonoMovil"]').val(empresa.telefonoMovil);
        $('input[id="nombreContacto"]').val(empresa.nombreContacto);
        $('input[id="telefonoContacto"]').val(empresa.telefonoContacto);
        $('input[id="telefonoMovilContacto"]').val(empresa.telefonoMovilContacto);

        //para la visualizacion en editar de campo RUC
        $("#ruc").inputmask("Regex");

    }else if(visualizar){
        $('h1').append('Visualizar Empresa');
        $('input[type="text"]').prop('disabled', true);
        $('input[type="email"]').prop('disabled', true);
        $('input[id="nombre"]').val(empresa.nombre);
        $('input[id="direccion"]').val(empresa.direccion);
        $('input[id="email"]').val(empresa.email);
        $('input[id="telefono"]').val(empresa.telefono);
        $('input[id="ruc"]').val(empresa.ruc);
        $('input[id="descripcion"]').val(empresa.descripcion);
        $('input[id="telefonoMovil"]').val(empresa.telefonoMovil);
        $('input[id="nombreContacto"]').val(empresa.nombreContacto);
        $('input[id="telefonoContacto"]').val(empresa.telefonoContacto);
        $('input[id="telefonoMovilContacto"]').val(empresa.telefonoMovilContacto);
        if(empresa.activo == 'S'){
            $("#botonEditar").show();
        } 
        $("#aceptar").hide();
        $("#cancelar").hide();
    }else{
        $('h1').append('Crear Empresa');
    }
    //do 
    //jQuery stuff when DOM is ready
})
function sendData() {
    console.log($('#formUsuario').serialize());
        var id = document.getElementById("idEmpresa");
        var nombre = document.getElementById("nombre");
        var direccion = document.getElementById("direccion");
        var ruc = document.getElementById("ruc");
        var descripcion = document.getElementById("descripcion");
        var telefono = document.getElementById("telefono");
        var telefonoMovil = document.getElementById("telefonoMovil");
        var nombreContacto = document.getElementById("nombreContacto");
        var telefonoContacto = document.getElementById("telefonoContacto");
        var telefonoMovilContacto = document.getElementById("telefonoMovilContacto");
        var email = document.getElementById("email");

        if(nombre.value===""||ruc.value===""||direccion.value===""||email.value===""||nombreContacto.value===""){
            $('#mensaje').before('<div class="alert alert-danger alert-dismissible fade in">'
                                            + '<button type="button" class="close" data-dismiss="alert"'
                                            +'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                                            +'<strong>Error! </strong>'
                                            + 'Fuck the world'
                                            + '</div>');
            $('html, body').animate({ scrollTop: 0 }, 0);

        }
        else{
                $.ajax({
                type:'POST',
                url: CONTEXT_ROOT+'/empresas/guardar', 
                data: {
                        id:id.value,
                        nombre: nombre.value,
                        descripcion: descripcion.value,
                        ruc: ruc.value,
                        direccion: direccion.value,
                        telefono: telefono.value,
                        telefonoMovil: telefonoMovil.value,
                        nombreContacto: nombreContacto.value,
                        telefonoContacto: telefonoContacto.value,
                        telefonoMovilContacto: telefonoMovilContacto.value,
                        email: email.value
                },
                success: function(data){ 
                    if(data.error == true){
                        $('#mensaje').before('<div class="alert alert-danger alert-dismissible fade in">'
                                            + '<button type="button" class="close" data-dismiss="alert"'
                                            +'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                                            +'<strong>Error! </strong>'
                                            + data.mensaje
                                            + '</div>');
                        $('html, body').animate({ scrollTop: 0 }, 0);

                    }else{
                        $('#mensaje').before('<div class="alert alert-info alert-dismissible fade in">'
                                            + '<button type="button" class="close" data-dismiss="alert"'
                                            +'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                                            +'<strong>Exito! </strong>'
                                            + data.mensaje
                                            + '</div>');
                        $('html, body').animate({ scrollTop: 0 }, 0);
                    }
                 },
                async: false
            });
        }

}