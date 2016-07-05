
$(document).ready(function(data) {
    
    $(":input").inputmask();
    
    $('.boton-imagen').on('click', function(e) {
        e.preventDefault();
        e.stopPropagation();
        var boton = $(this);
        var campo = boton.data('campo');
        var imagen = boton.data('img');
        var alto = boton.data('alto');
        var ancho = boton.data('ancho');
        console.log(campo);
        console.log(imagen);
        $('#' + campo + 'Input').click().change(function() {
          var input = this
          if (input.files && input.files[0]) {
            var FR = new FileReader();
            FR.onload = function(e) {
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
			ruc: {
				required: true
			},			
			nombre: {
				required: true
			},
			direccion: {
				required: true
			},
			telefono: {
				required: true
			},
			email: {
				required: true,
                                email:true
			},
			nombreContacto: {
				required: true
			},
			telefonoContacto: {
				required: true
			}
		},
		messages: {			
			ruc: "Favor ingresar numero de ruc!",
                        nombre: "Debe ingresar nombre de la empresa!",
			alias: "Debe ingresar un alias para el usuario!",                        			
			direccion: "Debe ingresar direccion de la empresa!",
			telefono: "Debe ingresar numero de telefono del usuario!",
			email: "Favor ingresar un email valido!",
                        nombreContacto: "Debe ingresar nombre del contacto!",
                        telefonoContacto: "Debe ingresar telefono del contacto!"
		},
		invalidHandler: function (event, validator) { //display error alert on form submit   
			$('.alert-error', $('.login-form')).show();
		},
		highlight: function (e) {
			$(e).closest('.form-group').removeClass('has-info').addClass('has-error');
		},
		success: function (e) {
			$(e).closest('.form-group').removeClass('has-error').addClass('has-info');
			$(e).remove();
		},
		errorPlacement: function (error, element) {
			if(element.is(':checkbox') || element.is(':radio')) {
				var controls = element.closest('.controls');
				if(controls.find(':checkbox,:radio').length > 1) controls.append(error);
				else error.insertAfter(element.nextAll('.lbl').eq(0));
			} 
			else if(element.is('.chzn-select')) {
				error.insertAfter(element.nextAll('[class*="chzn-container"]').eq(0));
			}
			else error.insertAfter(element);
		},
		submitHandler: function (form) {
                    console.log("exitoooo");
                    var $form = $('#validation-form');
                    var serialize = $form.find('.table-empresa-input').serialize();
                    if(empresa == null ){
                        var jqXHR = $.post(CONTEXT_ROOT+'/empresas/guardar', serialize, function(data, textStatus, jqXHR) {
                            if(data.error){
                                $('#mensaje').append('<div class="alert alert-error">'
                                    + '<button class="close" data-dismiss="alert" type="button"'
                                    +'><i class="fa  fa-remove"></i></button>'
                                    +'<strong>Error! </strong>'
                                    + data.mensaje
                                    + '</div>');
                            }else{
                                $('#mensaje').append('<div class="alert alert-info alert-dismissible fade in">'
                                    + '<button type="button" class="close" data-dismiss="alert"'
                                    +'aria-label="Close"><i class="fa  fa-remove"></i></button>'
                                    +'<strong>Exito! </strong>'
                                    + data.mensaje
                                    + '</div>');
                                setTimeout(function() {
                                    window.location = CONTEXT_ROOT+"/empresas";
                                }, 1500);

                            }

                        });

                        jqXHR.fail(function(jqXHR, textStatus, errorThrown) {

                        });
                    }else{
                        var jqXHR = $.post(CONTEXT_ROOT+'/empresas/editar', serialize, function(data, textStatus, jqXHR) {
                            if(data.error){
                                $('#mensaje').append('<div class="alert alert-error">'
                                    + '<button class="close" data-dismiss="alert" type="button"'
                                    +'><i class="fa  fa-remove"></i></button>'
                                    +'<strong>Error! </strong>'
                                    + data.mensaje
                                    + '</div>');
                            }else{
                                $('#mensaje').append('<div class="alert alert-info alert-dismissible fade in">'
                                    + '<button type="button" class="close" data-dismiss="alert"'
                                    +'aria-label="Close"><i class="fa  fa-remove"></i></button>'
                                    +'<strong>Exito! </strong>'
                                    + data.mensaje
                                    + '</div>');
                                setTimeout(function() {
                                    window.location = CONTEXT_ROOT+"/empresas";
                                }, 1500);

                            }

                        });

                        jqXHR.fail(function(jqXHR, textStatus, errorThrown) {

                        });
                    }
                    
		}
	});
        
    $('#imgPortada').on('error', function(){
        if(this.naturalWidth == 0){
          $(this).attr('alt', 'Imagen con errores, vuelva a subir.');
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

