
$(document).ready(function(data) { 
    var registros = $('.checkbox-identifier');
    $.ajax({
        type:'GET',
        url: CONTEXT_ROOT+'/roles/'+idRol+'/permisos',        
        success: function(data){ 
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
                for(i=0; i<data.permisos.length; i++){
                    $.each(registros, function(index, el) {
                        if(data.permisos[i] === parseInt($(el).val())){
                            $(this).attr('checked', true);
                        }
                   });
                }
            }
         },
        async: false
    });
    
    var permisos = []; 
    $("#selectall").click(function () {
        var els = $('.checkbox-identifier');
        console.log(els);
        
        if (!this.checked) {
            console.log("false");
            els.prop('checked', false);
            els.prop('defaultChecked', false);
            
            $('#arrayContent').empty();
        } else {
            els.attr('checked', true);
            console.log("true");
            $.each(els, function(index, el) {
                 permisos.push( $(el).val() ); 
            });
            console.log(permisos);
//            $('#arrayContent').empty();
//            $('#arrayContent').append(permisos.join( ', ' ) );
        }
        //contacts.push($('#tbody').children(tr > td > input).val();)
    });

    // if all checkbox are selected, check the selectall checkbox
    // and viceversa

//    $(".checkbox-identifier").click(function() {
//        if($(this).is(':checked')){
//            console.log("false");
//           $(this).prop('checked', false); 
//        }else{
//            console.log("true")
//           $(this).prop('checked', true);  
//        }
//    });



    $('#example1').DataTable( {
        "order": [[ 0, "asc" ]],
        "pageLength": 10,
        "autoWidth": false,

        "language": {
            "lengthMenu": "Mostrar _MENU_ resultados por pagina",
            "zeroRecords": "No se encontraron resultados",
            "info": "Pagina _PAGE_ de _PAGES_",
            "infoEmpty": "No hay resultados disponibles",
            "infoFiltered": "(filtrado de un total de _MAX_ )",
            "search": "Buscar:"
        }
    } );  
  
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


            