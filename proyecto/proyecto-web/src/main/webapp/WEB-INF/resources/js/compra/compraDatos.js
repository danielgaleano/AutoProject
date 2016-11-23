$(document).ready(function(data) {

    if (action === 'VISUALIZAR') {
        $('#botonAprobar').hide();
        $('#aceptar').hide();
        $('#buttonOption').hide();
        $('#validation-formCompra').find('.tableusuario-input').attr("disabled", true);
        $('#validation-form').find('.tableusuario-input').attr("disabled", true);
    } else if (action === 'CREAR') {
        $("#general").attr("disabled", true);
        $("#detallado").attr("disabled", true);
        $('#contado').attr("disabled", true);
        $('#credito').attr("disabled", true);
        $('#aceptar').hide();
        $('#botonAprobar').hide();
    } else if (action === 'CREAR_ORDEN') {
        $('#validation-form').find('.tableusuario-input').attr("disabled", true);
        $('#botonAprobar').hide();
        $('#buttonOption').hide();
    }

    if (id !== null) {
        cargarDatos(id);
    }

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

            if (compra.estadoCompra === 'COMPRA_PENDIENTE') {
                $('#botonAprobar').hide();
                $('#aceptar').show();
            } else if (compra.estadoCompra === 'COMPRA_REALIZADA') {
                $('#botonAprobar').hide();
                $('#validation-formCompra').find('.tableusuario-input').attr("disabled", true);
                $("#aceptar").hide();
            }
            $('#nroFactura').val(compra.nroFactura);

            if (compra.nroFactura !== null && compra.nroFactura !== " "
                    && action !== 'VISUALIZAR') {
                $('#botonAprobar').show();
            } else {
                $('#botonAprobar').hide();
            }
            $('#date-timeDesde').val(compra.fechaCuota);
            $('#descuento').val(compra.descuento);
            $('#interes').val(compra.porcentajeInteresCredito);
            $('#montoInteres').val(compra.montoInteres);
            $('#id-date-picker').val(compra.fechaCuota);

            if (compra.formaPago === 'CONTADO') {
                $('#contado').prop("checked", true);
                $("#formCredito").hide();
            } else if (compra.formaPago === 'CREDITO') {
                $('#credito').prop("checked", true);
                $("#general").attr("disabled", true);
                $("#detallado").attr("disabled", true);
                $("#formCredito").show();
            }

            if (compra.tipoDescuento === 'GENERAL') {
                $('#general').prop("checked", true);
                $("#formDescuento").show();
            } else if (compra.tipoDescuento === 'DETALLADO') {
                $('#detallado').prop("checked", true);
                $("#formDescuento").hide();
            }

//            $('#ruc').val(compra['proveedor.ruc']);
//            $('#telefono').val(compra['proveedor.telefono']);
//            $('#direccion').val(compra['proveedor.direccion']);
//            $('#nombre').val(compra['proveedor.nombre']);

            $('#moraInteres').val(compra.moraInteres);
            $('#cuotas').val(compra.cantidadCuotas);
            $('#montoCuota').val(compra.montoCuotas);
            $('#montoTotal').val(compra.monto);
            $('#entrega').val(compra.entrega);
            $('#montoDescuento').val(compra.montoDescuento);
            $('#saldo').val(compra.saldo);
            $('#neto').val(compra.neto);

            if (compra['proveedor.id'] !== null && compra['proveedor.id'] !== "") {
                $('#idProveedorConta').val(compra['proveedor.id']);
                $('#validation-form').find('.tableusuario-input').attr("disabled", true);
                $('#buttonOption').hide();
                
                var jqXHR = $.get(CONTEXT_ROOT + "/proveedores/" + compra['proveedor.id'], function(response, textStatus, jqXHR) {

                    if (response.error) {
                        $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                                + '<button class="close" data-dismiss="alert" type="button"'
                                + '><i class="fa  fa-remove"></i></button>'
                                + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                                + response.mensaje
                                + '</div>');
                    } else {

                        var proveedor = response.data;

                        $('#idProveedor').val(proveedor.id);                        
                        $('#ruc').val(proveedor.ruc);
                        $('#nombre').val(proveedor.nombre);
                        $('#email').val(proveedor.email);
                        $('#telefono').val(proveedor.telefono);
                        $('#direccion').val(proveedor.direccion);
                        $('#comentario').val(proveedor.comentario);
                        $('#fax').val(proveedor.fax);
                        $('#pais').val(proveedor.pais);
                        $('#ciudad').val(proveedor.ciudad);
                        $('#codigoPostal').val(proveedor.codigoPostal);
                        $('#comentario').val(proveedor.comentario);


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
