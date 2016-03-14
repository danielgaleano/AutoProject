
$(document).ready(function(data) {
    $("#botonEditar").hide();
    if(editar){
        $('h1').append('Editar Empresa');
        $('input[id="idEmpresa"]').val(empresa.id);
        $('input[id="ruc"]').prop('disabled', true);
        $('input[id="nombre"]').val(empresa.nombre);
        $('input[id="direccion"]').val(empresa.direccion);
        $('input[id="email"]').val(empresa.email);
        $('input[id="telefono"]').val(empresa.telefono);
        $('input[id="ruc"]').val(empresa.ruc);
        $('input[id="descripcion"]').val(empresa.descripcion);
    }else if(visualizar){
        $('h1').append('Visualizar Empresa');
        $('input[type="text"]').prop('disabled', true);
        $('input[id="nombre"]').val(empresa.nombre);
        $('input[id="direccion"]').val(empresa.direccion);
        $('input[id="email"]').val(empresa.email);
        $('input[id="telefono"]').val(empresa.telefono);
        $('input[id="ruc"]').val(empresa.ruc);
        $('input[id="descripcion"]').val(empresa.descripcion);
        if(empresa.activo == 'S'){
            $("#botonEditar").show();
        } 
        $("#aceptar").hide();
        $("#cancelar").hide();
    }else{
        $('h1').append('Crear Empresa');
    }
    //do 
    //jQuery stuff when DOM is ready
    // *** editable avatar *** //
    try {//ie8 throws some harmless exceptions, so let's catch'em

            //first let's add a fake appendChild method for Image element for browsers that have a problem with this
            //because editable plugin calls appendChild, and it causes errors on IE at unpredicted points
            try {
                    document.createElement('IMG').appendChild(document.createElement('B'));
            } catch(e) {
                    Image.prototype.appendChild = function(el){}
            }

            var last_gritter
            $('#avatar').editable({
                    type: 'image',
                    name: 'avatar',
                    value: null,
                    image: {
                            //specify ace file input plugin's options here
                            btn_choose: 'Change Avatar',
                            droppable: true,
                            maxSize: 110000,//~100Kb

                            //and a few extra ones here
                            name: 'avatar',//put the field name here as well, will be used inside the custom plugin
                            on_error : function(error_type) {//on_error function will be called when the selected file has a problem
                                    if(last_gritter) $.gritter.remove(last_gritter);
                                    if(error_type == 1) {//file format error
                                            last_gritter = $.gritter.add({
                                                    title: 'File is not an image!',
                                                    text: 'Please choose a jpg|gif|png image!',
                                                    class_name: 'gritter-error gritter-center'
                                            });
                                    } else if(error_type == 2) {//file size rror
                                            last_gritter = $.gritter.add({
                                                    title: 'File too big!',
                                                    text: 'Image size should not exceed 100Kb!',
                                                    class_name: 'gritter-error gritter-center'
                                            });
                                    }
                                    else {//other error
                                    }
                            },
                            on_success : function() {
                                    $.gritter.removeAll();
                            }
                    },
                url: function(params) {
                            // ***UPDATE AVATAR HERE*** //
                            //for a working upload example you can replace the contents of this function with 
                            //examples/profile-avatar-update.js

                            var deferred = new $.Deferred

                            var value = $('#avatar').next().find('input[type=hidden]:eq(0)').val();
                            if(!value || value.length == 0) {
                                    deferred.resolve();
                                    return deferred.promise();
                            }


                            //dummy upload
                            setTimeout(function(){
                                    if("FileReader" in window) {
                                            //for browsers that have a thumbnail of selected image
                                            var thumb = $('#avatar').next().find('img').data('thumb');
                                            if(thumb) $('#avatar').get(0).src = thumb;
                                    }

                                    deferred.resolve({'status':'OK'});

                                    if(last_gritter) $.gritter.remove(last_gritter);
                                    last_gritter = $.gritter.add({
                                            title: 'Avatar Updated!',
                                            text: 'Uploading to server can be easily implemented. A working example is included with the template.',
                                            class_name: 'gritter-info gritter-center'
                                    });

                             } , parseInt(Math.random() * 800 + 800))

                            return deferred.promise();

                            // ***END OF UPDATE AVATAR HERE*** //
                    },

                    success: function(response, newValue) {
                    }
            })
    }catch(e) {}
})
function sendData() {
        var id = document.getElementById("idEmpresa");
        var nombre = document.getElementById("nombre");
        var direccion = document.getElementById("direccion");
        var ruc = document.getElementById("ruc");
        var descripcion = document.getElementById("descripcion");
        var telefono = document.getElementById("telefono");
        var email = document.getElementById("email");
        $.ajax({
        type:'POST',
        url: CONTEXT_ROOT+'/empresas/guardar', 
        data: {
                id:id.value,
                nombre: nombre.value,
                descripcion: descripcion.value,
                ruc: ruc.value,
                direccion: direccion.value,
                telefono: telefono.value,
                email: email.value
        },
        success: function(data){ 
            if(data.error == true){
                $('#mensaje').before('<div class="alert alert-danger alert-dismissible fade in">'
                                    + '<button type="button" class="close" data-dismiss="alert"'
                                    +'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                                    +'<strong>Error! </strong>'
                                    + data.menasje
                                    + '</div>');
                $('html, body').animate({ scrollTop: 0 }, 0);

            }else{
                $('#mensaje').before('<div class="alert alert-info alert-dismissible fade in">'
                                    + '<button type="button" class="close" data-dismiss="alert"'
                                    +'aria-label="Close"><span aria-hidden="true">&times;</span></button>'
                                    +'<strong>Exito! </strong>'
                                    + data.menasje
                                    + '</div>');
                $('html, body').animate({ scrollTop: 0 }, 0);
            }
         },
        async: false
    });


}