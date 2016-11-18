$(document).ready(function(data) {
    
    if (action === 'VISUALIZAR') {
        $('#botonAprobar').hide();
        $('#aceptar').hide();      
        $('#validation-form').find('.tableusuario-input').attr("disabled", true);       
    }
    
    cargarDatos(id);    
});

function cargarDatos(id) {
    var jqXHR = $.get(CONTEXT_ROOT + "/compras/" + id, function(response, textStatus, jqXHR) {
        if (response.error) {
            $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                    + '<button class="close" data-dismiss="alert" type="button"'
                    + '><i class="fa  fa-remove"></i></button>'
                    + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                    + response.mensaje
                    + '</div>');
        } else {
            var compra = response.data;
            
            if(compra.estadoCompra === 'COMPRA_PENDIENTE'){
                $('#botonAprobar').hide();
            }else if(compra.estadoCompra === 'COMPRA_REALIZADA'){
                $('#botonAprobar').hide();
                $('#validation-form').find('.tableusuario-input').attr("disabled", true);
                $("#aceptar").hide();
            }
            $('#nroFactura').val(compra.nroFactura);
            
            if(compra.nroFactura !== null && compra.nroFactura !== " "){
                $('#botonAprobar').show();
            }else{
                $('#botonAprobar').hide();
            }
            $('#date-timeDesde').val(compra.fechaCuota);
            $('#descuento').val(compra.descuento);
            $('#interes').val(compra.porcentajeInteresCredito);
            $('#montoInteres').val(compra.montoInteres);
            
            if(compra.formaPago === 'CONTADO'){
                 $('#contado').prop("checked",true);
                 $("#formCredito").hide();
            }else if(compra.formaPago === 'CREDITO'){
                  $('#credito').prop("checked",true);
                  $("#general").attr("disabled", true);
                  $("#detallado").attr("disabled", true);
                  $("#formCredito").show();
            }
            
            if(compra.tipoDescuento === 'GENERAL'){
                 $('#general').prop("checked",true);
                 $("#formDescuento").show();
            }else if(compra.tipoDescuento === 'DETALLADO'){
                  $('#detallado').prop("checked",true);
                  $("#formDescuento").hide();
            }
            
            $('#ruc').val(compra['proveedor.ruc']);
            $('#telefono').val(compra['proveedor.telefono']);
            $('#direccion').val(compra['proveedor.direccion']);
            $('#nombre').val(compra['proveedor.nombre']);

            $('#moraInteres').val(compra.moraInteres);
            $('#cuotas').val(compra.cantidadCuotas);
            $('#montoCuota').val(compra.montoCuotas);
            $('#montoTotal').val(compra.monto);
            $('#entrega').val(compra.entrega);
            $('#montoDescuento').val(compra.montoDescuento);
            $('#saldo').val(compra.saldo);
            $('#neto').val(compra.neto);

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
