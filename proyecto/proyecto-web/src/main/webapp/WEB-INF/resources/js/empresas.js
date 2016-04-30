
function desactivar(data) {
        $.ajax({
        type:'DELETE',
        url: CONTEXT_ROOT+'/empresas/'+data+'/desactivar', 
        success: function(data){ 
            location.reload();
            if(data.error == true){
                
                $('#mensaje').before('<div class="alert alert-danger alert-dismissible fade in">'
                                    + '<button type="button" class="close" data-dismiss="alert"'
                                    +'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                                    +'<strong>Error! </strong>'
                                    + data.menasje
                                    + '</div>');

            }else{
                $('#mensaje').before('<div class="alert alert-block alert-success">'
                                    + '<button type="button" class="close" data-dismiss="alert"'
                                    +'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                                    +'<strong class="green">Exito! </strong>'
                                    + data.menasje
                                    + '</div>');
            }
        },
        async: false
    });

}

function activar(data) {
        $.ajax({
        type:'GET',
        url: CONTEXT_ROOT+'/empresas/'+data+'/activar', 
        success: function(data){ 
            if(data.error == true){
                location.reload();
                $('#mensaje').before('<div class="alert alert-danger alert-dismissible fade in">'
                                    + '<button type="button" class="close" data-dismiss="alert"'
                                    +'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                                    +'<strong>Error! </strong>'
                                    + data.menasje
                                    + '</div>');

            }else{
                location.reload();
                $('#mensaje').before('<div class="alert alert-info alert-dismissible fade in">'
                                    + '<button type="button" class="close" data-dismiss="alert"'
                                    +'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                                    +'<strong>Exito! </strong>'
                                    + data.menasje
                                    + '</div>');
            }
         },
        async: false
    });


}