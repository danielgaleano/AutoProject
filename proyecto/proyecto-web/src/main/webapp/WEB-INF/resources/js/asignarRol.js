
$(document).ready(function(data) { 
    var registros = $('.checkbox-identifier');
    $('#example1').Tabledit({
        url: CONTEXT_ROOT+'/usuarios/asignar',
        urlDelete: CONTEXT_ROOT+'/roles/desactivar/',
        urlActivate: CONTEXT_ROOT+'/roles/activar/',
        urlAsignar: CONTEXT_ROOT+'/roles/asignar/',
        tableId:'example1',
        inputClass:'form-control input-large',
        titleAsignar:'Asignar Permisos',
        isStatus:false,
        editarFormButton: false,
        editButton: true,
        deleteButton: false,
        activarButton:false,
        asignarButton:false,
        visualizarButton:false,
        columns: {
            identifier: [0, 'id'],
            editable: [
                [2, 'rol.id',roles]
            ]
        },
         onDraw: function() {
            console.log('onDraw()');
        },
        onSuccess: function(data, textStatus, jqXHR) {
            window.location = window.location +"#success";

            console.log('onSuccess(data, textStatus, jqXHR)');
            console.log(data);
            console.log(textStatus);
            console.log(jqXHR);
        },
        onFail: function(jqXHR, textStatus, errorThrown) {
            console.log('onFail(jqXHR, textStatus, errorThrown)');
            console.log(jqXHR);
            console.log(textStatus);
            console.log(errorThrown);
        },
        onAlways: function() {
            console.log('onAlways()');
        },
        onAjax: function(action, serialize) {
            console.log('onAjax(action, serialize)');
            console.log(action);
            console.log(serialize);
        }

    });
  
});



function parseBolean(val){
    if(val.toLowerCase() === 'true'){
        return true;
    }else if (val.toLowerCase() === 'false' || val.toLowerCase() === ''){
        return false;
    }

}

function asignarPermisos(){
    var permisos = [];
    var per ='';
    var els = $('.checkbox-identifier');
    
    els.each(function(index, el) {
        if(this.checked){
            per += $(el).val()+",";
             permisos.push(parseInt($(el).val()));
         }
    });
    console.log(permisos);
    $.ajax({
        type:'PUT',
        url: CONTEXT_ROOT+'/roles/asignar/'+idRol+'/permisos', 
        data: JSON.stringify({
                permisos: permisos
        }),
        success: function(data){ 
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
         },
        async: false
    });
       
    

}


            