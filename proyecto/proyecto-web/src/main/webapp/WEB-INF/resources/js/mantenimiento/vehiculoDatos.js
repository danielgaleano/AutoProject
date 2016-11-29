
$(document).ready(function(data) {
    
     
    $("#editButton").click(function () {
        $('#buttonOption').show();
        $('#buttonOptionDetalle').show();
        
        $('#validation-form').find('.tableusuario-input').attr("disabled", false);
        $('#validation-formDetalles').find('.tableusuario-input').attr("disabled", false);
    });

    
    if (action === 'VISUALIZAR') {
        $('#buttonOption').hide();
        $('#buttonOptionDetalle').hide();
        
        $('#validation-form').find('.tableusuario-input').attr("disabled", true);
        $('#validation-formDetalles').find('.tableusuario-input').attr("disabled", true);

    }else if (action === 'EDITAR'){
        $('#codigo').attr("disabled", true);
    }
    
    if (id !== null && id !== "") {
        var jqXHR = $.get(CONTEXT_ROOT + "/vehiculos/" + id, function(response, textStatus, jqXHR) {

            if (response.error) {
                $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                        + '<button class="close" data-dismiss="alert" type="button"'
                        + '><i class="fa  fa-remove"></i></button>'
                        + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                        + response.mensaje
                        + '</div>');
            } else {
                var vehiculo = response.data;
                
                $('#idVehiculo').val(vehiculo.id);
                $('#idDetalle').val(vehiculo['detalle.id']);
                
                $('#codigo').val(vehiculo.codigo);
                $('#tipo').val(vehiculo.tipo);
                $('#id-date-picker').val(vehiculo.fechaMantenimiento);
                $('#marca').val(vehiculo.marca);
                $('#modelo').val(vehiculo.modelo);
                $('#color').val(vehiculo.color);
                $('#anho').val(vehiculo.anho);
                $('#chapa').val(vehiculo.chapa);
                $('#chasis').val(vehiculo.chasis);
                $('#motor').val(vehiculo.motor);
                $('#kilometraje').val(vehiculo.kilometraje);
                $('#precioVenta').val(vehiculo.precioVenta);
                

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
    $('#id-date-picker').datepicker({
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


