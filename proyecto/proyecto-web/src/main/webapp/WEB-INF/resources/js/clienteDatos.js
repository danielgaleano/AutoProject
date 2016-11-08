
$(document).ready(function(data) {
    
    if (action === 'VISUALIZAR') {
        $('#buttonOption').hide();
        $('#buttonOptionContacto').hide();
        $('#buttonOptionEmpleo').hide();
        $('#idCliente').attr("disabled", true);
        $('#documento').attr("disabled", true);
        $('#nombre').attr("disabled", true);
        $('#id-date-picker').attr("disabled", true);
        $('#sexo').attr("disabled", true);
        $('#pais').attr("disabled", true);
        $('#email').attr("disabled", true);
        $('#telefono').attr("disabled", true);
        $('#direccion').attr("disabled", true);
        $('#comentario').attr("disabled", true);
        $('#nombreEmpresa').attr("disabled", true);
        $('#actividad').attr("disabled", true);
        $('#cargo').attr("disabled", true);
        $('#antiguedad').attr("disabled", true);
        $('#salario').attr("disabled", true);
        $('#telefonoEmpleo').attr("disabled", true);
        $('#direccionEmpleo').attr("disabled", true);
        $('#comentarioEmpleo').attr("disabled", true);
        $('#nombreContacto').attr("disabled", true);
        $('#cargoContacto').attr("disabled", true);
        $('#telefonoContacto').attr("disabled", true);
        $('#contactoEmail').attr("disabled", true);
        $('#comentarioContacto').attr("disabled", true);
    }else if (action === 'EDITAR'){
        $('#documento').attr("disabled", true);
    }
    
    if (id !== null && id !== "") {
        var jqXHR = $.get(CONTEXT_ROOT + "/clientes/" + id, function(response, textStatus, jqXHR) {

            if (response.error) {
                $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                        + '<button class="close" data-dismiss="alert" type="button"'
                        + '><i class="fa  fa-remove"></i></button>'
                        + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                        + response.mensaje
                        + '</div>');
            } else {
                var cliente = response.data;
                
                $('#idCliente').val(cliente.id);
                $('#idEmpleo').val(cliente['empleo.id']);
                $('#idContacto').val(cliente['contacto.id']);
                $('#documento').val(cliente.documento);
                $('#nombre').val(cliente.nombre);
                $('#id-date-picker').val(cliente.fechaNacimiento);
                $('#sexo').val(cliente.sexo);
                $('#pais').val(cliente.pais);
                $('#email').val(cliente.email);
                $('#telefono').val(cliente.telefono);
                $('#direccion').val(cliente.direccion);
                $('#comentario').val(cliente.comentario);
                $('#nombreEmpresa').val(cliente['empleo.nombreEmpresa']);
                $('#actividad').val(cliente['empleo.actividad']);
                $('#cargo').val(cliente['empleo.cargo']);
                $('#antiguedad').val(cliente['empleo.antiguedad']);
                $('#salario').val(cliente['empleo.salario']);
                $('#telefonoEmpleo').val(cliente['empleo.telefono']);
                $('#direccionEmpleo').val(cliente['empleo.direccion']);
                $('#comentarioEmpleo').val(cliente['empleo.comentario']);
                $('#nombreContacto').val(cliente['contacto.nombre']);
                $('#cargoContacto').val(cliente['contacto.cargo']);
                $('#telefonoContacto').val(cliente['contacto.telefono']);
                $('#contactoEmail').val(cliente['contacto.email']);
                $('#comentarioContacto').val(cliente['contacto.comentario']);

                $('#mensaje').append('<div class="alert alert-success alert-dismissible fade in">'
                        + '<button type="button" class="close" data-dismiss="alert"'
                        + 'aria-label="Close"><i class="fa  fa-remove"></i></button>'
                        + '<h4><strong><i class="icon fa fa-check"></i> Exito! </strong></h4>'
                        + response.mensaje
                        + '</div>');

            }

        });

        jqXHR.fail(function(jqXHR, textStatus, errorThrown) {

        });
    }
});
