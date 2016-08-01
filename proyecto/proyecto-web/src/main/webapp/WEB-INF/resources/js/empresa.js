
$(document).ready(function (data) {
    $(":input").inputmask();
    $("#ruc").inputmask("Regex", {
        regex: "^[0-9]{5}[0-9]?[0-9]?[0-9]?-[0-9]$"
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
    }, "Debe ingresar un número de ruc válido!");

    $('#validation-form').validate({
        errorElement: 'span',
        errorClass: 'help-inline',
        focusInvalid: false,
        rules: {
            ruc: {
                required: true,
                //expresion regular para validar el ruc
                regx: /^[0-9]{5}[0-9]?[0-9]?[0-9]?-[0-9]$/,
                minlength: 7,
                maxlength: 10
            },
            nombre: {
                required: true
            },
            direccion: {
                required: true
            },
            /*telefono: {
             required: true
             },*/
            email: {
                required: true,
                email: true
            },
            nombreContacto: {
                required: true
            },
            contactoCargo: {
                required: true
            },
            telefonoContacto: {
                required: true
            }
        },
        messages: {
            ruc: "Debe ingresar un número de ruc!",
            nombre: "Debe ingresar un nombre!",
            direccion: "Debe ingresar una dirección!",
            /*telefono: "Debe ingresar un número de telefono!",*/
            email: "Debe ingresar un email válido!",
            nombreContacto: "Debe ingresar nombre del contacto!",
            contactoCargo: "Debe ingresar el cargo del contacto!",
            telefonoContacto: "Debe ingresar teléfono del contacto!"

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
        /*highlight: function(element) {
         var id_attr = "#" + $( element ).attr("id") + "1";
         $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
         $(id_attr).removeClass('glyphicon-ok').addClass('glyphicon-remove');         
         },*/
        unhighlight: function (element) {
            var id_attr = "#" + $(element).attr("id") + "1";
            $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
            $(id_attr).removeClass('glyphicon-remove').addClass('glyphicon-ok');
        },
        submitHandler: function (form) {
            console.log("exitoooo");
            console.log($("#id-disable-check").is(':checked'));
            if($("#id-disable-check").is(':checked')){
               $("#tieneContacto").val(true);
            }else{
               $("#tieneContacto").val(false); 
            }
            var $form = $('#validation-form');
            var serialize = $form.find('.table-empresa-input').serialize();
            if (empresa == null) {
                var jqXHR = $.post(CONTEXT_ROOT + '/empresas/guardar', serialize, function (data, textStatus, jqXHR) {
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
                            window.location = CONTEXT_ROOT + "/empresas";
                        }, 1500);

                    }

                });

                jqXHR.fail(function (jqXHR, textStatus, errorThrown) {

                });
            } else {
                var jqXHR = $.post(CONTEXT_ROOT + '/empresas/editar', serialize, function (data, textStatus, jqXHR) {
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
                            window.location = CONTEXT_ROOT + "/empresas";
                        }, 1500);

                    }

                });

                jqXHR.fail(function (jqXHR, textStatus, errorThrown) {

                });
            }

        }
    });

    $('#imgPortada').on('error', function () {
        if (this.naturalWidth == 0) {
            $(this).attr('alt', 'Imagen con errores, vuelva a subir.');
        }
    });
    
    $("#id-disable-check").click(function () {
        if(this.checked) { //chequear status del select
            $("#formContacto").show();
        }else{
            $("#formContacto").hide();        
        }
    });

//    $("#botonEditar").hide();
//    if(editar){
//        $('h1').append('Editar Empresa');
//        $('input[id="idEmpresa"]').val(empresa.id);
//        $('input[id="ruc"]').prop('disabled', true);
//        $('input[id="nombre"]').val(empresa.nombre);
//        $('input[id="direccion"]').val(empresa.direccion);
//        $('input[id="email"]').val(empresa.email);
//        $('input[id="telefono"]').val(empresa.telefono);
//        $('input[id="ruc"]').val(empresa.ruc);
//        $('input[id="descripcion"]').val(empresa.descripcion);
//        $('input[id="telefonoMovil"]').val(empresa.telefonoMovil);
//        $('input[id="nombreContacto"]').val(empresa.nombreContacto);
//        $('input[id="telefonoContacto"]').val(empresa.telefonoContacto);
//        $('input[id="telefonoMovilContacto"]').val(empresa.telefonoMovilContacto);
//
//        //para la visualizacion en editar de campo RUC
//        $("#ruc").inputmask("Regex");
//
//    }else if(visualizar){
//        $('h1').append('Visualizar Empresa');
//        $('input[type="text"]').prop('disabled', true);
//        $('input[type="email"]').prop('disabled', true);
//        $('input[id="nombre"]').val(empresa.nombre);
//        $('input[id="direccion"]').val(empresa.direccion);
//        $('input[id="email"]').val(empresa.email);
//        $('input[id="telefono"]').val(empresa.telefono);
//        $('input[id="ruc"]').val(empresa.ruc);
//        $('input[id="descripcion"]').val(empresa.descripcion);
//        $('input[id="telefonoMovil"]').val(empresa.telefonoMovil);
//        $('input[id="nombreContacto"]').val(empresa.nombreContacto);
//        $('input[id="telefonoContacto"]').val(empresa.telefonoContacto);
//        $('input[id="telefonoMovilContacto"]').val(empresa.telefonoMovilContacto);
//        if(empresa.activo == 'S'){
//            $("#botonEditar").show();
//        } 
//        $("#aceptar").hide();
//        $("#cancelar").hide();
//    }else{
//        $('h1').append('Crear Empresa');
//    }
//    //do 
//    //jQuery stuff when DOM is ready
})



//function sendData() {
//    var datos = $("#formUsuario").serialize();
//        $.ajax({
//        type:'POST',
//        url: CONTEXT_ROOT+'/empresas/guardar', 
//        data: datos ,
//        success: function(data){ 
//            if(data.error == true){
//                $('#mensaje').before('<div class="alert alert-danger alert-dismissible fade in">'
//                                    + '<button type="button" class="close" data-dismiss="alert"'
//                                    +'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
//                                    +'<strong>Error! </strong>'
//                                    + data.menasje
//                                    + '</div>');
//                $('html, body').animate({ scrollTop: 0 }, 0);
//
//            }else{
//                $('#mensaje').before('<div class="alert alert-info alert-dismissible fade in">'
//                                    + '<button type="button" class="close" data-dismiss="alert"'
//                                    +'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
//                                    +'<strong>Exito! </strong>'
//                                    + data.menasje
//                                    + '</div>');
//                $('html, body').animate({ scrollTop: 0 }, 0);
//            }
//         },
//        async: false
//    });


//}

