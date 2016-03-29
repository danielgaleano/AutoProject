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