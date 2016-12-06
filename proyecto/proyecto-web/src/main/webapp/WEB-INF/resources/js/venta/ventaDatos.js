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
        $('#contado').attr("disabled", false);
        $('#credito').attr("disabled", false);
        $('#aceptar').hide();
        $('#botonAprobar').hide();
    } else if (action === 'CREAR_ORDEN') {
        $('#validation-form').find('.tableusuario-input').attr("disabled", true);
        $('#botonAprobar').hide();
        $('#buttonOption').hide();
    }

    if (id !== null) {
        cargarDatos(id);
    } else {
        var jqXHR = $.get(CONTEXT_ROOT + "/facturas/optener", function(response, textStatus, jqXHR) {
            if (response.error) {
                $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                        + '<button class="close" data-dismiss="alert" type="button"'
                        + '><i class="fa  fa-remove"></i></button>'
                        + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                        + response.mensaje
                        + '</div>');
            } else {
                $('#nroFactura').val(response.data);
            }

        });
    }

    $('#cliente').searchableOptionList({
        data: CONTEXT_ROOT + "/clientes/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc",
        converter: function(sol, rawDataFromUrl) {
            var solData = [];
            $.each(rawDataFromUrl.retorno, function() {

//                var select = false;
//                if (this['id'] === vehiculo['tipo.id']) {
//                    select = true; // label and value are returned from Java layer
//                } else {
//                    select = false; // label and value are returned from Java layer
//                }

                var aSingleOptionItem = {
                    // required attributes
                    "type": "option",
                    "label": this['nombre'],
                    "value": this['id'],
                    // optional attributes
                    "selected": false
                };
                solData.push(aSingleOptionItem);
            });
            return solData;
        },
        maxHeight: '220px',
        events: {
            onChange: function(a) {
                $('#idClienteConta').val(a.getSelection()[0].value);
            }
        }
    });

});

function cargarDatos(id) {
    var jqXHR = $.get(CONTEXT_ROOT + "/ventas/" + id, function(response, textStatus, jqXHR) {
        if (response.error) {
            $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                    + '<button class="close" data-dismiss="alert" type="button"'
                    + '><i class="fa  fa-remove"></i></button>'
                    + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                    + response.mensaje
                    + '</div>');
        } else {
            var venta = response.data;

            if (venta.estadoVenta === 'VENTA_PENDIENTE') {
                $('#botonAprobar').hide();
                $('#aceptar').show();
            } else if (venta.estadoVenta === 'VENTA_REALIZADA') {
                $('#botonAprobar').hide();
                $('#validation-formCompra').find('.tableusuario-input').attr("disabled", true);
                $("#aceptar").hide();
            }
            $('#nroFactura').val(venta.nroFactura);

            if (venta.nroFactura !== null && venta.nroFactura !== " "
                    && action !== 'VISUALIZAR') {
                $('#botonAprobar').show();
            } else {
                $('#botonAprobar').hide();
            }
            $('#date-timeDesde').val(venta.fechaCuota);
            $('#descuento').val(venta.descuento);
            $('#interes').val(venta.porcentajeInteresCredito);
            $('#montoInteres').val(venta.montoInteres);
            $('#id-date-picker').val(venta.fechaCuota);

            if (venta.formaPago === 'CONTADO') {
                $('#contado').prop("checked", true);
                $("#formCredito").hide();
                $("#tipo-descuento").show();
            } else if (venta.formaPago === 'CREDITO') {
                $('#credito').prop("checked", true);
                $("#general").attr("disabled", true);
                $("#detallado").attr("disabled", true);
                $("#tipo-descuento").hide();
                $("#formCredito").show();
            }

            if (venta.tipoDescuento === 'GENERAL') {
                $('#general').prop("checked", true);
                $("#formDescuento").show();
            } else if (venta.tipoDescuento === 'DETALLADO') {
                $('#detallado').prop("checked", true);
                $("#formDescuento").hide();
            }

//            $('#ruc').val(venta['cliente.ruc']);
//            $('#telefono').val(venta['cliente.telefono']);
//            $('#direccion').val(venta['cliente.direccion']);
//            $('#nombre').val(venta['cliente.nombre']);

            $('#moraInteres').val(venta.moraInteres);
            $('#cuotas').val(venta.cantidadCuotas);
            $('#montoCuota').val(venta.montoCuotas);
            $('#montoTotal').val(venta.monto);
            $('#entrega').val(venta.entrega);
            $('#montoDescuento').val(venta.montoDescuento);
            $('#saldo').val(venta.saldo);
            $('#neto').val(venta.neto);

            if (venta['cliente.id'] !== null && venta['cliente.id'] !== "") {
                $('#idProveedorConta').val(venta['cliente.id']);
                $('#validation-form').find('.tableusuario-input').attr("disabled", true);
                $('#buttonOption').hide();

                var jqXHR = $.get(CONTEXT_ROOT + "/clientes/" + venta['cliente.id'], function(response, textStatus, jqXHR) {

                    if (response.error) {
                        $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                                + '<button class="close" data-dismiss="alert" type="button"'
                                + '><i class="fa  fa-remove"></i></button>'
                                + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                                + response.mensaje
                                + '</div>');
                    } else {

                        var cliente = response.data;

                        $('#idProveedor').val(cliente.id);
                        $('#ruc').val(cliente.ruc);
                        $('#nombre').val(cliente.nombre);
                        $('#email').val(cliente.email);
                        $('#telefono').val(cliente.telefono);
                        $('#direccion').val(cliente.direccion);
                        $('#comentario').val(cliente.comentario);
                        $('#fax').val(cliente.fax);
                        $('#pais').val(cliente.pais);
                        $('#ciudad').val(cliente.ciudad);
                        $('#codigoPostal').val(cliente.codigoPostal);
                        $('#comentario').val(cliente.comentario);


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


