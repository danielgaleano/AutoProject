$(document).ready(function() {

    var formulario = $('#validation-form');
    //Dropzone.autoDiscover = false;

    Dropzone.options.validationForm = {
    //dropzoneOptions = {
        url : "upload",//url del servidor con el metodo que guarda la imagen
        autoProcessQueue : false,
        uploadMultiple : false,
        maxFilesize : 5, // MB
        parallelUploads : 1,
        maxFiles : 1,
        addRemoveLinks : false,
        acceptedFiles: "image/*",
        clickable:".dropzone-previews",
        previewsContainer : ".dropzone-previews",
        dictDefaultMessage: "Agregue una imagen",
        dictInvalidFileType: "El archivo seleccionado no es una imagen",
        dictFileTooBig: "El archivo debe ser menor a 5 MB",
        previewTemplate: '<div class="dz-preview dz-clickable dz-file-preview"><div class="dz-details"><img data-dz-thumbnail /></div><div data-dz-remove><button class="btn btn-xs btn-danger"><i class="fa fa-close"></i></button></div><div class="dz-error-message"><span data-dz-errormessage></span></div></div>',
        // The setting up of the dropzone
        init : function() {

            var myDropzone = this;

            // first set autoProcessQueue = false
            $('#aceptar').on("click", function(e) {
                var is_valid = formulario.valid();
                //verificar si es valido el formulario con los inputs
                if (is_valid) {
                    myDropzone.processQueue();
                }
                else{
                    //alert("Formulario no valido");
                };
                
            });

            this.on("maxfilesexceeded", function(file) {
                this.removeAllFiles();
                this.addFile(file);
            });

            
        }
    }

    //var uploader = document.querySelector('#validation-form');
    //var newDropzone = new Dropzone(uploader, dropzoneOptions);

    /*$('div.dz-default.dz-message > span').show(); // Show message span
    $('div.dz-default.dz-message').css({'opacity':1, 'background-image': 'none'});*/

});