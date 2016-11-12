
$(document).ready(function(data) {
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
            var $form = $('#validation-formContacto');
            var serialize = $form.find('.tableusuario-input').serialize();
            var id = $('#idCliente').val();
            var idContacto = $('#idContacto').val();
            if (id === null || id === '') {
                $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                        + '<button class="close" data-dismiss="alert" type="button"'
                        + '><i class="fa  fa-remove"></i></button>'
                        + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                        + 'Se deben guardar los datos personales del cliente'
                        + '</div>');
            } else {
                if (idContacto === null || idContacto === '') {
                    var jqXHR = $.post(CONTEXT_ROOT + '/clientes/' + id + '/contacto/guardar', serialize, function(data, textStatus, jqXHR) {
                        if (data.error) {
                            $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                                    + '<button class="close" data-dismiss="alert" type="button"'
                                    + '><i class="fa  fa-remove"></i></button>'
                                    + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                                    + data.mensaje
                                    + '</div>');
                        } else {
                            $('#idContacto').val(data.id);
                            $('#mensaje').append('<div class="alert alert-success alert-dismissible fade in">'
                                    + '<button type="button" class="close" data-dismiss="alert"'
                                    + 'aria-label="Close"><i class="fa  fa-remove"></i></button>'
                                    + '<h4><strong><i class="icon fa fa-check"></i> Exito! </strong></h4>'
                                    + data.mensaje
                                    + '</div>');
//                        setTimeout(function () {
//                            window.location = CONTEXT_ROOT + "/clientes";
//                        }, 1500);

                        }

                    });

                    jqXHR.fail(function(jqXHR, textStatus, errorThrown) {

                    });
                } else {
                    var jqXHR = $.post(CONTEXT_ROOT + '/clientes/contacto/editar', serialize, function(data, textStatus, jqXHR) {
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
//                        setTimeout(function () {
//                            window.location = CONTEXT_ROOT + "/clientes";
//                        }, 1500);

                        }

                    });

                    jqXHR.fail(function(jqXHR, textStatus, errorThrown) {

                    });
                }
            }

        }
    });


});

function agregarDescuentoOrdenCompra(table) {

}

