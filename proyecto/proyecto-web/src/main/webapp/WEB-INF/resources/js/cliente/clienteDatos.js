
$(document).ready(function(data) {
    
     
    $("#editButton").click(function () {
        $('#buttonOption').show();
        $('#buttonOptionContacto').show();
        $('#buttonOptionEmpleo').show();
        
        $('#validation-form').find('.tableusuario-input').attr("disabled", false);
        $('#validation-formEmpleo').find('.tableusuario-input').attr("disabled", false);
        $('#validation-formContacto').find('.tableusuario-input').attr("disabled", false);
    });

    
    if (action === 'VISUALIZAR') {
        $('#buttonOption').hide();
        $('#buttonOptionContacto').hide();
        $('#buttonOptionEmpleo').hide();
        
        $('#validation-form').find('.tableusuario-input').attr("disabled", true);
        $('#validation-formEmpleo').find('.tableusuario-input').attr("disabled", true);
        $('#validation-formContacto').find('.tableusuario-input').attr("disabled", true);

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
                $('#id-date-picker-cliente').val(cliente.fechaNacimiento);
                $('#sexo').val(cliente.sexo);
                $('#pais').val(cliente.pais);
                $('#email').val(cliente.email);
                $('#telefono').val(cliente.telefono);
                $('#telefonoMovil').val(cliente.telefonoMovil);
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
                $('#movilContacto').val(cliente['contacto.movil']);
                $('#documentoContacto').val(cliente['contacto.documento']);
                $('#nombreContacto').val(cliente['contacto.nombre']);
                $('#cargoContacto').val(cliente['contacto.cargo']);
                $('#telefonoContacto').val(cliente['contacto.telefono']);
                $('#emailContacto').val(cliente['contacto.email']);
                $('#comentarioContacto').val(cliente['contacto.comentario']);

//                $('#mensaje').append('<div class="alert alert-success alert-dismissible fade in">'
//                        + '<button type="button" class="close" data-dismiss="alert"'
//                        + 'aria-label="Close"><i class="fa  fa-remove"></i></button>'
//                        + '<h4><strong><i class="icon fa fa-check"></i> Exito! </strong></h4>'
//                        + response.mensaje
//                        + '</div>');

            }

        });

        jqXHR.fail(function(jqXHR, textStatus, errorThrown) {
            $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                        + '<button class="close" data-dismiss="alert" type="button"'
                        + '><i class="fa  fa-remove"></i></button>'
                        + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                        + 'Error! Favor comunicarse con el Administrador'
                        + '</div>');
        });
    }


    //Inicializar Fechas
    /*var hasta = new Date();
    var dia = hasta.getDate();
    var mes = hasta.getMonth() + 1;
    var anho = hasta.getFullYear();

    var fechaDesde = dia + "/" + mes + "/" + anho + " " + "00:00";*/
    $('#id-date-picker-cliente').datepicker({
        //format: 'DD/MM/YYYY HH:mm',
        format: "dd/mm/yyyy",
        language: "es-ES",
        startView: 2,
        minViewMode: 2
        

        //startDate: fechaDesde,
        //useCurrent: false,
        //minDate: new Date()
    });
});
