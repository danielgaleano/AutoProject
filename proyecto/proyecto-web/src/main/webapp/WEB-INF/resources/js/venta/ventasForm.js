$(document).ready(function(data) {

    $(".date-picker").datepicker('setDate', new Date());
    $('.date-picker').datepicker('option', 'dateFormat', 'yyyy-mm-dd');

    $(":input").inputmask();
    /*$("#factura").inputmask("Regex", {
     regex: "^[0-9]{3}-[0-9]{3}-[0-9]{6}$"
     });*/

    $('#validation-formVenta').validate({
        errorElement: 'span',
        errorClass: 'help-inline',
        focusInvalid: false,
        rules: {
            nroFactura: {
                required: true,
                //expresion regular para validar el factura
                //regx: /^[0-9]{3}-[0-9]{3}-[0-9]{6}$/
            },
            formaPago: {
                required: true
            },
            tipoDescuento: {
                required: true
            },
            cuotaFecha: {
                required: function(element) {
                    if ($('#credito').is(':checked')) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            },
            cantidadCuotas: {
                required: function(element) {
                    if ($('#credito').is(':checked')) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            }

        },
        messages: {
            nroFactura: {
                required: "Debe ingresar un número de factura!"
            },
            formaPago: "Debe seleccionar un tipo de pago!",
            tipoDescuento: "Debe seleccionar un tipo de descuento!",
            cuotaFecha: "Debe ingresar fecha de la primera cuota!",
            cantidadCuotas: "Cuotas!"
        },
        invalidHandler: function(event, validator) { //display error alert on form submit   
            $('.alert-error', $('.login-form')).show();
        },
        highlight: function(element) {
            var id_attr = "#" + $(element).attr("id") + "1";
            $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
            $(id_attr).removeClass('glyphicon-ok').addClass('glyphicon-remove');
            //$(e).closest('.form-group').removeClass('has-info').addClass('has-error');
        },
        success: function(e) {
            $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
            $(e).remove();
        },
        errorPlacement: function(error, element) {
            if (element.is(':checkbox') || element.is(':radio')) {
                var controls = element.closest('.controls');
                if (controls.find(':checkbox,:radio').length > 1)
                    controls.append(error);
                else
                    error.insertAfter(element.nextAll('.lbl').eq(0));
            } else if (element.is('.chzn-select')) {
                error.insertAfter(element.nextAll('[class*="chzn-container"]').eq(0));
            } else
                error.insertAfter(element);
        },
        unhighlight: function(element) {
            var id_attr = "#" + $(element).attr("id") + "1";
            $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
            $(id_attr).removeClass('glyphicon-remove').addClass('glyphicon-ok');
        },
        submitHandler: function(form) {
            var $form = $('#validation-formVenta');
            var serialize = $form.find('.tableusuario-input').serialize();
            var idVenta = $('#idVenta').val();
            //var idPedido = $('#idPedido').val();

            if (idVenta === null || idVenta === '') {
                if ($("#idCliente").val() === null || $("#idCliente").val() === "") {
                    $.messager.confirm('Error', 'Debe cargar los Datos del Cliente!');
                } else {
                    var jqXHR = $.post(CONTEXT_ROOT + '/ventas/guardar', serialize, function(data, textStatus, jqXHR) {
                        if (data.error) {
                            $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                                    + '<button class="close" data-dismiss="alert" type="button"'
                                    + '><i class="fa  fa-remove"></i></button>'
                                    + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                                    + data.mensaje
                                    + '</div>');
                        } else {
                            cargarDatos(data.id);
                            $('#idVenta').val(data.id);

                            $('#mensaje').append('<div class="alert alert-success alert-dismissible fade in">'
                                    + '<button type="button" class="close" data-dismiss="alert"'
                                    + 'aria-label="Close"><i class="fa  fa-remove"></i></button>'
                                    + '<h4><strong><i class="icon fa fa-check"></i> Exito! </strong></h4>'
                                    + data.mensaje
                                    + '</div>');


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
            } else {
                if ($("#idCliente").val() === null || $("#idCliente").val() === "") {
                    $.messager.confirm('Error', 'Debe cargar los Datos del Cliente!');
                } else {
                    var jqXHR = $.post(CONTEXT_ROOT + '/ventas/' + 1 + '/editar', serialize, function(data, textStatus, jqXHR) {
                        if (data.error) {
                            $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                                    + '<button class="close" data-dismiss="alert" type="button"'
                                    + '><i class="fa  fa-remove"></i></button>'
                                    + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                                    + data.mensaje
                                    + '</div>');
                        } else {
                            cargarDatos(idVenta);
                            $('#mensaje').append('<div class="alert alert-success alert-dismissible fade in">'
                                    + '<button type="button" class="close" data-dismiss="alert"'
                                    + 'aria-label="Close"><i class="fa  fa-remove"></i></button>'
                                    + '<h4><strong><i class="icon fa fa-check"></i> Exito! </strong></h4>'
                                    + data.mensaje
                                    + '</div>');

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

        }
    });

    $("#botonAprobar").click(function() {
        if ($("#idCliente").val() === null || $("#idCliente").val() === "") {
            $.messager.confirm('Error', 'Debe cargar los Datos del Cliente!');
        } else {
            $.messager.confirm('Confirm', 'Esta Seguro que desea aprobar la venta?', function(r) {
                var jqXHR = $.get(CONTEXT_ROOT + "/ventas/" + id + "/aprobar", function(response, textStatus, jqXHR) {
                    if (response.error === true) {
                        $('#mensaje').append('<div class="alert alert-error">'
                                + '<button class="close" data-dismiss="alert" type="button"'
                                + '><i class="fa  fa-remove"></i></button>'
                                + '<strong>Error! </strong>'
                                + response.mensaje
                                + '</div>');

                    } else {

                        $('#validation-formVenta').find('.tableusuario-input').attr("disabled", true);
                        $("#aceptar").hide();

                        $('#mensaje').append('<div class="alert alert-success alert-dismissible fade in">'
                                + '<button type="button" class="close" data-dismiss="alert"'
                                + 'aria-label="Close"><i class="fa  fa-remove"></i></button>'
                                + '<h4><strong><i class="icon fa fa-check"></i> Exito! </strong></h4>'
                                + response.mensaje
                                + '</div>');

                    }
                });
            });
        }

    });
    $("#credito").click(function() {
        if (this.checked) { //chequear status del select
            $("#general").attr("disabled", true);
            $("#detallado").attr("disabled", true);
            $("#formCredito").show();
            $("#tipo-descuento").hide();
        }
    });
    $("#contado").click(function() {
        if (this.checked) { //chequear status del select
            $("#general").attr("disabled", false);
            $("#detallado").attr("disabled", false);
            $("#formCredito").hide();
            $("#tipo-descuento").show();
        }
    });
    $("#general").click(function() {
        if (this.checked) { //chequear status del select
            $('#grid').trigger('reloadGrid');
            $("#formDescuento").show();
        }
    });
    $("#detallado").click(function() {
        if (this.checked) { //chequear status del select
            $('#validation-formVenta').valid();
            $('#grid').trigger('reloadGrid');
            $("#formDescuento").hide();
        }
    });

    $("#entrega").keypress(function(e) {
        setTimeout(function() {
            if ($.isNumeric(e.key) || e.key === 'Backspace') {
                var interes = $("#interes").val();
                if (interes !== null && interes !== "") {
                    var entrega = $("#entrega").val();
                    var precio = $("#montoTotal").val();
                    var saldo = precio - entrega;
                    var interes = saldo * $("#interes").val() / 100;
                    $("#montoInteres").val(interes);
                    var neto = saldo + interes;
                    $("#neto").val(neto);
                    $("#saldo").val(saldo);

                } else {
                    var entrega = $("#entrega").val();
                    var precio = $("#montoTotal").val();
                    var saldo = precio - entrega;

                    $("#neto").val(saldo);
                    $("#saldo").val(saldo);
                }
            } else {
                $.messager.alert('Error!!', 'Debe ingresar un valor numerico!!!');
            }
        }, 0);

    });

    $("#interes").keypress(function(e) {
        setTimeout(function() {
            if ($.isNumeric(e.key) || e.key === 'Backspace') {
                var entrega = $("#entrega").val();
                if (entrega !== null && entrega !== "") {
                    var precio = $("#montoTotal").val();
                    var saldo = precio - entrega;
                    var interes = saldo * $("#interes").val() / 100;
                    $("#montoInteres").val(interes);
                    var neto = saldo + interes;
                    $("#neto").val(neto);
                    $("#saldo").val(saldo);

                } else {
                    var precio = $("#montoTotal").val();
                    var saldo = precio;
                    var interes = saldo * $("#interes").val() / 100;
                    $("#montoInteres").val(interes);
                    var neto = saldo + interes;
                    $("#neto").val(neto);
                    $("#saldo").val(saldo);
                }
            } else {
                $.messager.alert('Error!!', 'Debe ingresar un valor numerico!!!');
            }
        }, 0);

    });
    $("#cuotas").keypress(function(e) {
        setTimeout(function() {
            if ($.isNumeric(e.key) || e.key === 'Backspace') {
                if ($("#interes").val() !== null & $("#interes").val() !== "") {
                    var monto = $("#neto").val();
                    var montoCuota = monto / $("#cuotas").val();
                    $("#montoCuota").val(montoCuota);
                } else {
                    $.messager.alert('Error!!', 'Debe ingresar un interes a cobrar!!!');
                }
            } else {
                $.messager.alert('Error!!', 'Debe ingresar un valor numerico!!!');
            }
        }, 0);

    });

    $("#cuotas").click(function(e) {
        setTimeout(function() {

            if ($("#interes").val() !== null & $("#interes").val() !== "") {
                var monto = $("#neto").val();
                var montoCuota = monto / $("#cuotas").val();
                $("#montoCuota").val(montoCuota);
            } else {
                $.messager.alert('Error!!', 'Debe ingresar un interes a cobrar!!!');
            }

        }, 0);

    });

    $("#descuento").keypress(function(e) {
        setTimeout(function() {
            if ($.isNumeric(e.key) || e.key === 'Backspace') {
                var precio = $("#montoTotal").val();
                var interes = precio * $("#descuento").val() / 100;
                $("#montoDescuento").val(interes);
                var neto = precio - interes;
                $("#neto").val(neto);
                
            } else {
                $.messager.alert('Error!!', 'Debe ingresar un valor numerico!!!');
            }
        }, 0);

    });

    $("#porcentajeDescuento").keypress(function(e) {
        setTimeout(function() {
            if ($.isNumeric(e.key) || e.key === 'Backspace') {
                var precio = $("#precioVenta").val();
                var interes = precio * $("#porcentajeDescuento").val() / 100;
                $("#montoDescuentoCompra").val(interes);
                var neto = precio - interes;
                $("#netoCompra").val(neto);
            } else {
                $.messager.alert('Error!!', 'Debe ingresar un valor numerico!!!');
            }
        }, 0);

    });

});



function ventaForm(id, action) {

    if (action !== "CREAR") {
        $('#cliente').attr("disabled", true);
        $('#id-date-picker').attr("disabled", true);
        $('#observacion').attr("disabled", true);
    }

    if (id !== null && id !== "") {
        var jqXHR = $.get(CONTEXT_ROOT + "/ventas/" + id, function(response, textStatus, jqXHR) {
            if (response.error === true) {
                $('#mensaje').append('<div class="alert alert-error">'
                        + '<button class="close" data-dismiss="alert" type="button"'
                        + '><i class="fa  fa-remove"></i></button>'
                        + '<strong>Error! </strong>'
                        + response.mensaje
                        + '</div>');

            } else {
                var venta = response.data;

                var jqXHR = $.get(CONTEXT_ROOT + "/clientes/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc", function(response, textStatus, jqXHR) {
                    var sel = '<option value="">Seleccione opcion</option>';
                    $.each(response.retorno, function() {
                        if (this['id'] === venta['cliente.id']) {
                            sel += '<option value="' + this['id'] + '" selected>' + this['nombre'] + '</option>'; // label and value are returned from Java layer
                        } else {
                            sel += '<option value="' + this['id'] + '">' + this['nombre'] + '</option>'; // label and value are returned from Java layer
                        }

                    });
                    $('#cliente').append(sel);
                });

                $('#idVenta').val(venta.id);
                $('#factura').val(venta.factura);
                $('#ruc').val(venta.ruc);
                $('#nombre').val(venta.nombre);
                $('#direccion').val(venta.direccion);
                $('#montoTotal').val(venta.total);
                $('#telefono').val(venta.telefono);
                $('#descuento').val(venta.descuento);
                $('#montoDescuento').val(venta.montoDescuento);
                $('#cantCuotas').val(venta.cantCuotas);
                $('#montoCuota').val(venta.montoCuota);
                $('#interes').val(venta.interes);
                $('#montoInteres').val(venta.montoInteres);
                $('#interesMora').val(venta.interesMora);
                $('#entrega').val(venta.entrega);
                $('#saldo').val(venta.saldo);
                $('#neto').val(venta.neto);
                $('#id-date-picker-venta').val(venta.fechaCompra);
                $('#id-date-picker-cuota').val(venta.fechaCuota);

            }
        });
    } else {

        var jqXHR = $.get(CONTEXT_ROOT + "/clientes/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc", function(response, textStatus, jqXHR) {
            var sel = '<option value="">Seleccione opcion</option>';
            $.each(response.retorno, function() {
                sel += '<option value="' + this['id'] + '">' + this['nombre'] + '</option>'; // label and value are returned from Java layer
            });
            $('#cliente').append(sel);
        });

        var codigo = randomString(7, "COD");
        $('#codigo').val(codigo);
    }


}
            

