
$('#imgPortada').on('error', function(){
  if(this.naturalWidth == 0){
    $(this).attr('alt', 'Imagen con errores, vuelva a subir.');
  }
});

$(document).ready(function(data) {
    
    
    $('.boton-imagen').on('click', function(e) {
        e.preventDefault();
        e.stopPropagation();
        var boton = $(this);
        var campo = boton.data('campo');
        var imagen = boton.data('img');
        var alto = boton.data('alto');
        var ancho = boton.data('ancho');
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
			documento: {
				required: true
			},
			alias: {
				required: true
			},
			contrasena: {
				required: true,
                                minlength:5
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
                                email:true
			}
		},
		messages: {			
			documento: "Favor ingresar numero de documento!",
			alias: "Debe ingresar un alias para el usuario!",
                        contrasena: {
				required: "Debe ingresar una contraseña para el usuario!",
                                minlength: "Longitud minima de 5 caracteres!"
			},
			nombre: "Debe ingresar nombre del usuario!",
			apellido: "Debe ingresar apellido del usuario!",
			telefono: "Debe ingresar numero de telefono del usuario!",
			email: "Favor ingresar un email valido!"
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
                    var serialize = $form.find('.tabledit-input').serialize();
                    var jqXHR = $.post(CONTEXT_ROOT+'/servicios/guardar', serialize, function(data, textStatus, jqXHR) {
                        if(data.error){
                            $('#mensaje').addClass('alert alert-danger alert-dismissible fade in');   
                            $('#mensaje').append(data.descripcion);
                            $('#mensaje').show();
                        }else{
                            $('#buttonServicio').attr('disabled','disabled');                           
                            var $input = $form.find('.tabledit-input');
                            $input.attr('disabled','disabled');
                            $('#subServicio').show();
                            $('#mensaje').addClass('alert alert-block alert-success');   
                            $('#mensaje').append(data.descripcion);
                            $('#mensaje').show();
                            setTimeout(function() {
                                window.location = CONTEXT_ROOT+"/servicios/"+data.id+"/subServicios";
                            }, 2500);
                            
                        }

                    });

                    jqXHR.fail(function(jqXHR, textStatus, errorThrown) {

                    });
		}
	});
    
    
    $("#botonEditar").hide();
    if(editar){
        $('h1').append('Editar Usuario');
        $('input[id="idUsuario"]').val(usuario.id);
        $('input[id="documento"]').prop('disabled', true);
        $('input[id="alias"]').val(usuario.alias);
        $('input[id="claveAcceso"]').val(usuario.claveAcceso);
        $('input[id="nombre"]').val(usuario.nombre);
        $('input[id="apellido"]').val(usuario.apellido);
        $('input[id="direccion"]').val(usuario.direccion);
        $('input[id="email"]').val(usuario.email);
        $('input[id="telefono"]').val(usuario.telefono);
        $('input[id="documento"]').val(usuario.documento);
        
    }else if(visualizar){
        $('h1').append('Visualizar Usuario');
        $('input[type="text"]').prop('disabled', true);
        $('input[id="alias"]').val(usuario.alias);
        $('input[id="claveAcceso"]').val(usuario.claveAcceso);
        $('input[id="nombre"]').val(usuario.nombre);
        $('input[id="apellido"]').val(usuario.apellido);
        $('input[id="direccion"]').val(usuario.direccion);
        $('input[id="email"]').val(usuario.email);
        $('input[id="telefono"]').val(usuario.telefono);
        $('input[id="documento"]').val(usuario.documento);
        
        if(usuario.activo == 'S'){
            $("#botonEditar").show();
        } 
        $("#aceptar").hide();
        $("#cancelar").hide();
    }else{
        $('h1').append('Crear Usuario');
    }
           
    
});
