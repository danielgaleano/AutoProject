
$('#imgPortada').on('error', function () {
    if (this.naturalWidth == 0) {
        $(this).attr('alt', 'Imagen con errores, vuelva a subir.');
    }
});

$(document).ready(function (data) {
    $(":input").inputmask();
    $("#documento").inputmask("Regex", {
        regex: "^[0-9]{5}([0-9])?([0-9])?([0-9])?([0-9])?$"
    });
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
            alias: {
                required: true
            },
            claveAcceso: {
                required: true,
                minlength: 5
            },
            nombre: {
                required: true
            },
            apellido: {
                required: true
            },
            telefono: {
                required: true
            },
            email: {
                required: true,
                email: true
            },
            empresa: {
                required: true
            }
        },
        messages: {
            documento: {
                required: "Debe ingresar un número de documento!",
                minlength: "Longitud mínima de 5 números!",
                maxlength: "Longitud máxima de 10 números!",   
            },
            alias: "Debe ingresar un alias para el usuario!",
            claveAcceso: {
                required: "Debe ingresar una contraseña para el usuario!",
                minlength: "Longitud mínima de 5 caracteres!"
            },
            nombre: "Debe ingresar nombre del usuario!",
            apellido: "Debe ingresar apellido del usuario!",
            telefono: "Debe ingresar numero de telefono del usuario!",
            email: "Debe ingresar un email valido!",
            empresa: "Debe seleccionar una empresa!"
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


    //   $("#botonEditar").hide();
//    if(editar){
//        $('h1').append('Editar Usuario');
//        $('input[id="idUsuario"]').val(usuario.id);
//        $('input[id="documento"]').prop('disabled', true);
//        $('input[id="alias"]').val(usuario.alias);
//        $('input[id="claveAcceso"]').val(usuario.claveAcceso);
//        $('input[id="nombre"]').val(usuario.nombre);
//        $('input[id="apellido"]').val(usuario.apellido);
//        $('input[id="direccion"]').val(usuario.direccion);
//        $('input[id="email"]').val(usuario.email);
//        $('input[id="telefono"]').val(usuario.telefono);
//        $('input[id="documento"]').val(usuario.documento);
//        
//    }else if(visualizar){
//        $('h1').append('Visualizar Usuario');
//        $('input[type="text"]').prop('disabled', true);
//        $('input[id="alias"]').val(usuario.alias);
//        $('input[id="claveAcceso"]').val(usuario.claveAcceso);
//        $('input[id="nombre"]').val(usuario.nombre);
//        $('input[id="apellido"]').val(usuario.apellido);
//        $('input[id="direccion"]').val(usuario.direccion);
//        $('input[id="email"]').val(usuario.email);
//        $('input[id="telefono"]').val(usuario.telefono);
//        $('input[id="documento"]').val(usuario.documento);

//        if(usuario.activo == 'S'){
//            $("#botonEditar").show();
//        } 
//        $("#aceptar").hide();
//        $("#cancelar").hide();
//    }else{
////        $('h1').append('Crear Usuario');
//    }


});
