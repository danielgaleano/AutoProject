var baseConstantes = {
	botonCerrar: '<button data-dismiss="alert" class="close" type="button"><i class="fa fa-times"></i></button>'
};
function sendData(url) {
    
    var serialize = $("#formUsuario").serialize();
    
    var jqXHR = $.post(CONTEXT_ROOT+url, serialize, function(data, textStatus, jqXHR) {
                    console.log(data);
                    if(data.error == true){
                        $('#mensaje').before('<div class="alert alert-danger alert-dismissible fade in">'
                                            + '<button type="button" class="close" data-dismiss="alert"'
                                            +'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                                            +'<strong>Error! </strong>'
                                            + data.mensaje
                                            + '</div>');
                        $('html, body').animate({ scrollTop: 0 }, 0);

                    }else{
                        $('#mensaje').before('<div class="alert alert-info alert-dismissible fade in">'
                                            + '<button type="button" class="close" data-dismiss="alert"'
                                            +'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                                            +'<strong>Exito! </strong>'
                                            + data.mensaje
                                            + '</div>');
                        $('html, body').animate({ scrollTop: 0 }, 0);
                    }
                }, 'json');
                
    jqXHR.fail(function(jqXHR, textStatus, errorThrown) {
        
                $('#mensaje').before('<div class="alert alert-danger alert-dismissible fade in">'
                                            + '<button type="button" class="close" data-dismiss="alert"'
                                            +'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                                            +'<strong>Error! </strong>'
                                            + 'Error al crear el registro. Vuelva a intentarlo.'
                                            + '</div>');
                        $('html, body').animate({ scrollTop: 0 }, 0);
            });

            jqXHR.always(function() {
                
            });

            return jqXHR;
    
}

// Botón para subir imágenes
$('.btn-subir-imagen').each(function(b,btn){

	if(!$(btn).data('activar')){
		$(btn).removeClass('btn-subir-imagen');
		return;
	}

	var div = $('<div>').addClass('img-boton');
	$(btn).after(div);
	$(btn).appendTo(div);

	$(btn).click(function(){
		var form = $(btn).data('form');
		var campo = $(btn).data('campo');
		var id = 'imagen-'+form+'-'+campo;

		if(!$('#'+id).length){
			$('<input>').attr('id',id)
				.attr('type','file')
				.addClass('hidden')
				.appendTo('body')
				.change(function(){
					var input = this;
					if ( input.files && input.files[0] ) {
				        var FR = new FileReader();
				        FR.onload = function(e) {
				             $(btn).attr( "src", e.target.result );
				             $('#'+form).find('input[name="'+campo+'"]')
				             	.val( e.target.result );
				        };
				        FR.readAsDataURL( input.files[0] );

				        $(btn).parent().mensaje({
			        		tipoMensaje: 'alert-warning',
			        		mensaje: 'Para confirmar el cambio de imagen haz click en <strong>Guardar</strong>.',
			        		after: true
			        	});
				    }
				});
		}

		$('#'+id).click();
	});

});

// Botón de cancelar
$('.boton-cancelar').click(function(){
	window.history.back();
});

//Aplicar el spin de 'cargando' solo para los botones que van a ir a otra vista
//$('#page-content-wrapper i.icon').click(function(e){
//	$(this).addClass('fa fa-spinner fa fa-spin');
//});

// Funcion para atrapar el evento de la tecla ENTER
//onEnter Plugin
$.fn.onEnter = function(callback){
    this.keyup(function(e){
            if(e.keyCode == baseKeys.ENTER){
                e.preventDefault();
                if (typeof callback == 'function'){
                    callback.apply(this);
                }
            }
        });
    return this;
};

// Función para mensajes
$.fn.mensaje = function(data){
	if( data.tipoMensaje==undefined || data.mensaje==undefined ||
		data.tipoMensaje=='' || data.mensaje==''){
		return;
	}

	var div = data.after? this.next() : this.prev();
	var contenido = '<div class="alert alert-dismissable '+data.tipoMensaje+ '">'
		+ baseConstantes.botonCerrar + data.mensaje+'</div>';

	if (div.hasClass('alert')){
		div.remove();
	}

	if(data.after){
    	this.after(contenido);
	}else{
    	this.before(contenido);
	}

    return this;
};