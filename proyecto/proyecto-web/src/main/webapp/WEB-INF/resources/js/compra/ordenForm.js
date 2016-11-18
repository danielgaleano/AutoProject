$(document).ready(function(data) {

    $(".date-picker").datepicker('setDate', new Date());
    $('.date-picker').datepicker('option', 'dateFormat', 'yyyy-mm-dd');

    /*$("#factura").inputmask("Regex", {
        regex: "^[0-9]{3}-[0-9]{3}-[0-9]{6}$"
    });*/

    $('#validation-form').validate({
        errorElement: 'span',
        errorClass: 'help-inline',
        focusInvalid: false,
        rules: {
            factura: {
                required: true,
                //expresion regular para validar el factura
                //regx: /^[0-9]{3}-[0-9]{3}-[0-9]{6}$/
            },           
            optionsPago: {
                required: true
            },
            optionsDescuento: {
                required: true
            }
        },
        messages: {
            factura: {
                required: "Debe ingresar un número de factura!"
            },
            optionsPago: "Debe seleccionar un tipo de pago!",
            optionsDescuento: "Debe seleccionar un tipo de descuento!"
        },
        invalidHandler: function (event, validator) { //display error alert on form submit   
            $('.alert-error', $('.login-form')).show();
        },
        highlight: function (element) {
            var id_attr = "#" + $(element).attr("id") + "1";
            $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
            $(id_attr).removeClass('glyphicon-ok').addClass('glyphicon-remove');
            //$(e).closest('.form-group').removeClass('has-info').addClass('has-error');
        },
        success: function (e) {
            $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
            $(e).remove();
        },
        errorPlacement: function (error, element) {
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
        unhighlight: function (element) {
            var id_attr = "#" + $(element).attr("id") + "1";
            $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
            $(id_attr).removeClass('glyphicon-remove').addClass('glyphicon-ok');
        },
        submitHandler: function (form) {      
            var $form = $('#validation-form');
            var serialize = $form.find('.tableusuario-input').serialize();
            var idCompra = $('#idCompra').val();
            var idPedido = $('#idPedido').val();
            
            if (idCompra === null || idCompra === '') {
                var jqXHR = $.post(CONTEXT_ROOT + '/compras/'+idPedido+'/guardar', serialize, function (data, textStatus, jqXHR) {
                    if (data.error) {
                        $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                                + '<button class="close" data-dismiss="alert" type="button"'
                                + '><i class="fa  fa-remove"></i></button>'
                                + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                                + data.mensaje
                                + '</div>');
                    } else {
                        $('#idCompra').val(data.id);
                        
                        window.location.href = CONTEXT_ROOT +'/compras/'+data.id;
                        
                        $('#mensaje').append('<div class="alert alert-success alert-dismissible fade in">'
                                + '<button type="button" class="close" data-dismiss="alert"'
                                + 'aria-label="Close"><i class="fa  fa-remove"></i></button>'
                                + '<h4><strong><i class="icon fa fa-check"></i> Exito! </strong></h4>'
                                + data.mensaje
                                + '</div>');


                    }

                });

                jqXHR.fail(function (jqXHR, textStatus, errorThrown) {
                    $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                        + '<button class="close" data-dismiss="alert" type="button"'
                        + '><i class="fa  fa-remove"></i></button>'
                        + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                        + 'Error! Favor comunicarse con el Administrador'
                        + '</div>');
                });
            } else{
                var jqXHR = $.post(CONTEXT_ROOT + '/compras/editar', serialize, function (data, textStatus, jqXHR) {
                    if (data.error) {
                        $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                                + '<button class="close" data-dismiss="alert" type="button"'
                                + '><i class="fa  fa-remove"></i></button>'
                                + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                                + data.mensaje
                                + '</div>');
                    } else {
                        $('#mensaje').append('<div class="alert alert-success alert-dismissible fade in">'
                                + '<button type="button" class="close" data-dismiss="alert"'
                                + 'aria-label="Close"><i class="fa  fa-remove"></i></button>'
                                + '<h4><strong><i class="icon fa fa-check"></i> Exito! </strong></h4>'
                                + data.mensaje
                                + '</div>');

                    }

                });

                jqXHR.fail(function (jqXHR, textStatus, errorThrown) {
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



    
    var jqXHR = $.get(CONTEXT_ROOT + "/pedidos/" + id, function(response, textStatus, jqXHR) {
        if (response.error === true) {
            $('#mensaje').append('<div class="alert alert-error">'
                    + '<button class="close" data-dismiss="alert" type="button"'
                    + '><i class="fa  fa-remove"></i></button>'
                    + '<strong>Error! </strong>'
                    + response.mensaje
                    + '</div>');

        } else {
            var pedido = response.data;

            $('#idPedido').val(pedido.id);
            $('#ruc').val(pedido['proveedor.ruc']);
            $('#nombre').val(pedido['proveedor.nombre']);
            $('#direccion').val(pedido['proveedor.direccion']);
            $('#telefono').val(pedido['proveedor.telefono']);
            $('#montoTotal').val(pedido.total);
            $('#id-date-picker').val(pedido.fechaEntrega);

        }
    });

    $("#credito").click(function() {
        if (this.checked) { //chequear status del select
            $("#general").attr("disabled", true);
            $("#detallado").attr("disabled", true);
            $("#formCredito").show();
        }
    });
    $("#contado").click(function() {
        if (this.checked) { //chequear status del select
            $("#general").attr("disabled", false);
            $("#detallado").attr("disabled", false);
            $("#formCredito").hide();
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
            $('#validation-form').valid();
            $('#grid').trigger('reloadGrid');
            $("#formDescuento").hide();
        }
    });

});



function compraForm(id, action) {

    if (action !== "CREAR") {
        $('#cliente').attr("disabled", true);
        $('#id-date-picker').attr("disabled", true);
        $('#observacion').attr("disabled", true);
    }

    if (id !== null && id !== "") {
        var jqXHR = $.get(CONTEXT_ROOT + "/compras/" + id, function(response, textStatus, jqXHR) {
            if (response.error === true) {
                $('#mensaje').append('<div class="alert alert-error">'
                        + '<button class="close" data-dismiss="alert" type="button"'
                        + '><i class="fa  fa-remove"></i></button>'
                        + '<strong>Error! </strong>'
                        + response.mensaje
                        + '</div>');

            } else {
                var compra = response.data;

                var jqXHR = $.get(CONTEXT_ROOT + "/clientes/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc", function(response, textStatus, jqXHR) {
                    var sel = '<option value="">Seleccione opcion</option>';
                    $.each(response.retorno, function() {
                        if (this['id'] === compra['cliente.id']) {
                            sel += '<option value="' + this['id'] + '" selected>' + this['nombre'] + '</option>'; // label and value are returned from Java layer
                        } else {
                            sel += '<option value="' + this['id'] + '">' + this['nombre'] + '</option>'; // label and value are returned from Java layer
                        }

                    });
                    $('#cliente').append(sel);
                });

                $('#idCompra').val(compra.id);
                $('#factura').val(compra.factura);
                $('#ruc').val(compra.ruc);
                $('#nombre').val(compra.nombre);
                $('#direccion').val(compra.direccion);
                $('#montoTotal').val(compra.total);
                $('#telefono').val(compra.telefono);
                $('#descuento').val(compra.descuento);
                $('#montoDescuento').val(compra.montoDescuento);
                $('#cantCuotas').val(compra.cantCuotas);
                $('#montoCuota').val(compra.montoCuota);
                $('#interes').val(compra.interes);
                $('#montoInteres').val(compra.montoInteres);
                $('#interesMora').val(compra.interesMora);
                $('#entrega').val(compra.entrega);
                $('#saldo').val(compra.saldo);
                $('#neto').val(compra.neto);
                $('#id-date-picker-compra').val(compra.fechaCompra);
                $('#id-date-picker-cuota').val(compra.fechaCuota);

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
            