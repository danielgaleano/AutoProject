
$(document).ready(function(data) {
    $('#validation-form').validate({
        errorElement: 'span',
        errorClass: 'help-inline',
        focusInvalid: false,
        rules: {
            importePagar: {
                required: true
            }
        },
        messages: {
            importePagar: "Debe ingresar importe a pagar!"
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
            var idCompra = $('#idCompra').val();

            if (idCompra !== null || idCompra !== '') {
                var jqXHR = $.post(CONTEXT_ROOT + '/cobros/realizar', serialize, function(data, textStatus, jqXHR) {
                    if (data.error) {
                        $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                                + '<button class="close" data-dismiss="alert" type="button"'
                                + '><i class="fa  fa-remove"></i></button>'
                                + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                                + data.mensaje
                                + '</div>');
                    } else {
                        $('#interes').val("0");
			$('#vuelto').val("0");
                        $('#importePagar').val("");
//                        $.get(CONTEXT_ROOT + '/reportes/exportar/recibo/pdf/' + data.idMovimiento, function(response, textStatus, jqXHR) {
//                            
//                        }
                        window.location.href = CONTEXT_ROOT +'/reportes/exportar/recibo/pdf/' + data.idMovimiento;
                        
                        cargarDatos(data.id);
                        $("#grid").trigger("reloadGrid", [{page: 1, current: true}]);
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
    });

});

function cargarDatos(id) {
    var jqXHR = $.get(CONTEXT_ROOT + "/cobros/" + id, function(response, textStatus, jqXHR) {

        if (response.error) {
            $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                    + '<button class="close" data-dismiss="alert" type="button"'
                    + '><i class="fa  fa-remove"></i></button>'
                    + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                    + response.mensaje
                    + '</div>');
        } else {
            var pago = response.data;
            // $('.ui-jqdialog-titlebar-close').trigger('click');
            $('#idCompra').val(pago.idCompra);
            $('#docPagar').val(pago.idDocPagar);
            $('#nroFactura').val(pago.nroFactura);
            $('#montoTotal').val(pago.neto);

            $('#saldo').val(pago.saldo);



            if (pago.formaPago === "CONTADO") {
                if (parseInt(pago.saldo) > 0 && pago.saldo !== null) {
                    $('#neto').val(pago.saldo);
                    $('#netoOculto').val(pago.saldo);
                } else {
                    if (pago.cancelado) {
                        $('#neto').val(pago.saldo);
                        $('#netoOculto').val(pago.saldo);
                    } else {
                        $('#neto').val(pago.importePagar);
                        $('#netoOculto').val(pago.importePagar);
                    }

                }
                $('#monto').val(0);
                $('#id-date-picker').val(pago.fechaCuota);
                $('#nroCuota').hide();
                $('#monto').hide();
                $('#id-date-picker').hide();
            }
            else if (pago.formaPago === "CREDITO") {
                $('#monto').val(pago.monto);
                $('#neto').val(pago.importePagar);
                $('#netoOculto').val(pago.importePagar);
                $('#id-date-picker').val(pago.fechaCuota);
                $('#nroCuota').val(pago.cuota);
            }

        }

    });
}
