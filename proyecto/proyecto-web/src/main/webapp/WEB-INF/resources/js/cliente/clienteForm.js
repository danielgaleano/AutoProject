
$(document).ready(function (data) {
    $(":input").inputmask();
    $("#documento").inputmask("Regex", {
        regex: "^[0-9]{5}([0-9])?([0-9])?([0-9])?([0-9])?$"
    });


    $.validator.addMethod("regx", function (value, element, regexpr) {
        return regexpr.test(value);
    }, "Debe ingresar un número de documento válido!");

    $('#validation-form').validate({
        errorElement: 'span',
        errorClass: 'help-inline',
        focusInvalid: false,
        rules: {
            documento: {
                required: true,
                //expresion regular para validar el documento
                regx: /^[0-9]{5}([0-9])?([0-9])?([0-9])?([0-9])?$/,
                minlength: 5,
                maxlength: 10
            },           
            nombre: {
                required: true
            },
            telefono: {
                required: true
            },
            email: {
                required: true,
                email: true
            },
            fecha: {
                required: true
            },
            sexo: {
                required: true
            },
            pais: {
                required: true
            }
        },
        messages: {
            documento: {
                required: "Debe ingresar un número de documento!",
                minlength: "Longitud mínima de 5 números!",
                maxlength: "Longitud máxima de 10 números!",   
            },
            nombre: "Debe ingresar el nombre del cliente!",
            telefono: "Debe ingresar el numero de telefono del cliente!",
            email: "Debe ingresar un email valido!",
            fecha: "Debe ingresar fecha de nacimiento del cliente!",
            sexo: "Debe ingresar el sexo del cliente!",
            pais: "Debe ingresar el pais del cliente!"
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
            if($("#id-disable-check").is(':checked')){
               $("#tieneContacto").val(true);
            }else{
               $("#tieneContacto").val(false); 
            }
            var $form = $('#validation-form');
            var serialize = $form.find('.tableusuario-input').serialize();
            var idCliente = $('#idCliente').val();
            
            if (idCliente === null || idCliente === '') {
                var jqXHR = $.post(CONTEXT_ROOT + '/clientes/guardar', serialize, function (data, textStatus, jqXHR) {
                    if (data.error) {
                        $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                                + '<button class="close" data-dismiss="alert" type="button"'
                                + '><i class="fa  fa-remove"></i></button>'
                                + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                                + data.mensaje
                                + '</div>');
                    } else {
                        $('#idCliente').val(data.id);
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
                var jqXHR = $.post(CONTEXT_ROOT + '/clientes/editar', serialize, function (data, textStatus, jqXHR) {
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
  
});
