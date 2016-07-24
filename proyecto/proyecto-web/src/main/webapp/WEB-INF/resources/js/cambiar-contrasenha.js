
$(document).ready(function (data) {
    
    $('.boton-imagen').on('click', function (e) {
        e.preventDefault();
        e.stopPropagation();
        var boton = $(this);
        var campo = boton.data('campo');
        var imagen = boton.data('img');
        var alto = boton.data('alto');
        var ancho = boton.data('ancho');
        console.log(campo);
        console.log(imagen);
        $('#' + campo + 'Input').click().change(function () {
            var input = this
            if (input.files && input.files[0]) {
                var FR = new FileReader();
                FR.onload = function (e) {
                    $('#' + imagen).attr("src", e.target.result);
                    $('input[name="' + campo + '"]').val(e.target.result);
                };
                FR.readAsDataURL(input.files[0]);
            }
        });
    });

    $('#validation-form').validate({
        errorElement: 'span',
        errorClass: 'help-inline',
        focusInvalid: false,
        rules: {
            claveAcceso: {
                required: true,
                minlength: 5
            },
            claveAccesoNuevo: {
                required: true,
                minlength: 5
            },
            claveAccesoNuevoRepetir {
                required: true,
                minlength: 5,
                equalTo: "#claveAccesoNuevo"
            }
            
        },
        messages: {
            claveAcceso: {
                required: "Debe ingresar la contraseña actual del usuario!",
                minlength: "Longitud mínima de 5 caracteres!"
            },
            claveAccesoNuevo: {
                required: "Debe ingresar la nueva contraseña del usuario!",
                minlength: "Longitud mínima de 5 caracteres!"
            },
            claveAccesoNuevoRepetir: {
                required: "Debe repetir la nueva contraseña del usuario!",
                minlength: "Longitud mínima de 5 caracteres!",
                equalTo: "Las nuevas contraseñas ingresadas no coinciden!"
            }

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
            console.log("exitoooo");
            var $form = $('#validation-form');
            var serialize = $form.find('.tableusuario-input').serialize();
            var datos = serialize.replace("empresa", "empresa.id");
            if (usuario == null) {
                var jqXHR = $.post(CONTEXT_ROOT + '/usuarios/guardar', datos, function (data, textStatus, jqXHR) {
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
                        setTimeout(function () {
                            window.location = CONTEXT_ROOT + "/usuarios";
                        }, 1500);

                    }

                });

                jqXHR.fail(function (jqXHR, textStatus, errorThrown) {

                });
            } else {
                var jqXHR = $.post(CONTEXT_ROOT + '/usuarios/editar', serialize, function (data, textStatus, jqXHR) {
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
                        setTimeout(function () {
                            window.location = CONTEXT_ROOT + "/usuarios";
                        }, 1500);

                    }

                });

                jqXHR.fail(function (jqXHR, textStatus, errorThrown) {

                });
            }

        }
    });



});
