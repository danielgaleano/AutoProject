
$(document).ready(function(data) {
    
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
                $("#saldo").val(neto);
            } else {
                $.messager.alert('Error!!', 'Debe ingresar un valor numerico!!!');
            }
        }, 0);

    });

    $("#porcentajeDescuento").keypress(function(e) {
        setTimeout(function() {
            if ($.isNumeric(e.key) || e.key === 'Backspace') {
                var precio = $("#precioCompra").val();
                var interes = precio * $("#porcentajeDescuento").val() / 100;
                $("#montoDescuentoCompra").val(interes);
                var neto = precio - interes;
                $("#netoCompra").val(neto);
            } else {
                $.messager.alert('Error!!', 'Debe ingresar un valor numerico!!!');
            }
        }, 0);

    });

    $('#validation-formDescuento').validate({
        errorElement: 'span',
        errorClass: 'help-inline',
        focusInvalid: false,
        rules: {
            porcentajeDescuento: {
                required: true
            }
        },
        messages: {
            porcentajeDescuento: "Debe ingresar el interes de descuento!"
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
            var $form = $('#validation-form');
            var serialize = $form.find('.tableusuario-input').serialize();

            var $formDescuento = $('#validation-formDescuento');
            var serializeDesc = $formDescuento.find('.tableusuario-input').serialize();

            var id = $('#idCompra').val();
            var idPedido = $('#idPedido').val();
            var idPedidoDet = $('#idDetPedido').val();

            if (id === null || id === '') {
                var jqXHR = $.post(CONTEXT_ROOT + '/compras/' + idPedido + '/guardar', serialize, function(data, textStatus, jqXHR) {
                    if (data.error) {
                        $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                                + '<button class="close" data-dismiss="alert" type="button"'
                                + '><i class="fa  fa-remove"></i></button>'
                                + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                                + data.mensaje
                                + '</div>');
                    } else {
                        
                        $('#idCompra').val(data.id);

                        var jqXHR = $.post(CONTEXT_ROOT + '/compras/detalle/' + idPedidoDet + '/guardar', serializeDesc, function(data, textStatus, jqXHR) {
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
                                
                                window.location.href = CONTEXT_ROOT +'/compras/'+$('#idCompra').val();
                                
                                


                            }

                        });

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
            } else {

            }

        }
    });


});


