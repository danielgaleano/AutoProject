
$(document).ready(function(data) {
    SearchableOptionList.defaults.texts.noItemsAvailable = "No se encontraron datos";
    SearchableOptionList.defaults.texts.selectAll = 'Select all';
    SearchableOptionList.defaults.texts.selectNone = 'Select none';
    SearchableOptionList.defaults.texts.quickDelete = '&times;';
    SearchableOptionList.defaults.texts.searchplaceholder = 'Seleccione Opcion';



    $("#editButton").click(function() {
        $('#buttonOption').show();
        $('#buttonOptionDetalle').show();

        $('#validation-form').find('.tableusuario-input').attr("disabled", false);
        $('#validation-formDetalles').find('.tableusuario-input').attr("disabled", false);
    });


    if (action === 'VISUALIZAR') {
        $('#buttonOption').hide();
        $('#buttonOptionDetalle').hide();

        $('#validation-form').find('.tableusuario-input').attr("disabled", true);
        $('#validation-formDetalles').find('.tableusuario-input').attr("disabled", true);

    } else if (action === 'EDITAR') {
        $('#codigo').attr("disabled", true);
    }

    if (id !== null && id !== "") {
        var jqXHR = $.get(CONTEXT_ROOT + "/mantenimiento/vehiculos/" + id, function(response, textStatus, jqXHR) {

            if (response.error) {
                $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                        + '<button class="close" data-dismiss="alert" type="button"'
                        + '><i class="fa  fa-remove"></i></button>'
                        + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                        + response.mensaje
                        + '</div>');
            } else {
                var vehiculo = response.data;
                $('#idMarca').val(vehiculo['marca.id']);
                $('#tipo').searchableOptionList({
                    data: CONTEXT_ROOT + "/tipos/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc",
                    converter: function(sol, rawDataFromUrl) {
                        var solData = [];
                        $.each(rawDataFromUrl.retorno, function() {

                            var select = false;
                            if (this['id'] === vehiculo['tipo.id']) {
                                select = true; // label and value are returned from Java layer
                            } else {
                                select = false; // label and value are returned from Java layer
                            }

                            var aSingleOptionItem = {
                                // required attributes
                                "type": "option",
                                "label": this['nombre'],
                                "value": this['id'],
                                // optional attributes
                                "selected": select
                            };
                            solData.push(aSingleOptionItem);
                        });
                        return solData;
                    },
                    maxHeight: '220px',
                    events: {
                        onChange: function(a) {
                            $('#idTipo').val(a.getSelection()[0].value);
                        }
                    }
                });

                $('#marca').searchableOptionList({
                    data: CONTEXT_ROOT + "/marcas/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc",
                    converter: function(sol, rawDataFromUrl) {
                        var solData = [];
                        $.each(rawDataFromUrl.retorno, function() {

                            var select = false;
                            if (this['id'] === vehiculo['marca.id']) {
                                select = true; // label and value are returned from Java layer
                            } else {
                                select = false; // label and value are returned from Java layer
                            }

                            var aSingleOptionItem = {
                                // required attributes
                                "type": "option",
                                "label": this['nombre'],
                                "value": this['id'],
                                // optional attributes
                                "selected": select
                            };
                            solData.push(aSingleOptionItem);
                        });
                        return solData;
                    },
                    maxHeight: '220px',
                    events: {
                        onChange: function(a) {
                            
                            setTimeout(function() {
                                $('#idMarca').val(a.getSelection()[0].value);
                                $('#sol').find('.sol-container').remove();
                                $('#sol').find('.sol-option').remove();
                                console.log(newSol);
                                newSol.options.data = CONTEXT_ROOT + "/modelos/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc&idMarca=" + a.getSelection()[0].value ;
                                newSol.items = "";
                                newSol.init();
                                
                                $('#modelo').searchableOptionList({
                                    data: CONTEXT_ROOT + "/modelos/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc&idMarca=" + a.getSelection()[0].value,
                                    converter: function(sol, rawDataFromUrl) {
                                        var solData = [];
                                        $.each(rawDataFromUrl.retorno, function() {

                                            var aSingleOptionItem = {
                                                // required attributes
                                                "type": "option",
                                                "label": this['nombre'],
                                                "value": this['id'],
                                                // optional attributes
                                                "selected": false
                                            };
                                            solData.push(aSingleOptionItem);
                                        });
                                        return solData;
                                    },
                                    maxHeight: '220px',
                                    events: {
                                        onChange: function(a) {
                                            $('#idModelo').val(a.getSelection()[0].value);
                                        }
                                    }
                                });
                            }, 0);
                        }
                    }
                });

                var newSol = $('#modelo').searchableOptionList({
                    data: CONTEXT_ROOT + "/modelos/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc&idMarca="+$('#idMarca').val(),
                    converter: function(sol, rawDataFromUrl) {
                        var solData = [];
                        $.each(rawDataFromUrl.retorno, function() {

                            var select = false;
                            if (this['id'] === vehiculo['modelo.id']) {
                                select = true; // label and value are returned from Java layer
                            } else {
                                select = false; // label and value are returned from Java layer
                            }

                            var aSingleOptionItem = {
                                // required attributes
                                "type": "option",
                                "label": this['nombre'],
                                "value": this['id'],
                                // optional attributes
                                "selected": select
                            };
                            solData.push(aSingleOptionItem);
                        });
                        return solData;
                    },
                    maxHeight: '220px',
                    events: {
                        onChange: function(a) {
                            $('#idModelo').val(a.getSelection()[0].value);
                        }
                    }
                });

                $('#idVehiculo').val(vehiculo.id);
                $('#idDetalle').val(vehiculo['detalle.id']);

                $('#codigo').val(vehiculo.codigo);
                $('#idTipo').val(vehiculo['tipo.id']);
                $('#id-date-picker').val(vehiculo.fechaMantenimiento);
                $('#idMarca').val(vehiculo['marca.id']);
                $('#idModelo').val(vehiculo['modelo.id']);
                $('#color').val(vehiculo.color);
                $('#anho').val(vehiculo.anho);
                $('#chapa').val(vehiculo.chapa);
                $('#chasis').val(vehiculo.chasis);
                $('#motor').val(vehiculo.motor);
                $('#kilometraje').val(vehiculo.kilometraje);
                $('#precioVenta').val(vehiculo.precioVenta);
                $('#precioCosto').val(vehiculo.precioCosto);
                $('#precioMantenimiento').val(vehiculo.precioMantenimiento);
                //$('#cedulaVerde').val(vehiculo.cedulaVerde);
                //$('#titulo').val(vehiculo.titulo);
                
                console.log("ced= " + vehiculo.cedulaVerde);
                if(vehiculo.cedulaVerde == "t" || vehiculo.cedulaVerde == 1 || vehiculo.cedulaVerde == "True"){
                    $('#cedulaVerde').prop("checked", true);
                }
                if(vehiculo.titulo == "t" || vehiculo.titulo == 1 || vehiculo.titulo == "True"){
                    $('#titulo').prop("checked", true);
                }


//                $('#mensaje').append('<div class="alert alert-success alert-dismissible fade in">'
//                        + '<button type="button" class="close" data-dismiss="alert"'
//                        + 'aria-label="Close"><i class="fa  fa-remove"></i></button>'
//                        + '<h4><strong><i class="icon fa fa-check"></i> Exito! </strong></h4>'
//                        + response.mensaje
//                        + '</div>');

            }

        });

        jqXHR.fail(function(jqXHR, textStatus, errorThrown) {
            $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                    + '<button class="close" data-dismiss="alert" type="button"'
                    + '><i class="fa  fa-remove"></i></button>'
                    + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                    + 'Error! Favor comunicarse con el Administrador'
                    + '</div>');
        });
    }


    //Inicializar Fechas
    /*var hasta = new Date();
     var dia = hasta.getDate();
     var mes = hasta.getMonth() + 1;
     var anho = hasta.getFullYear();
     
     var fechaDesde = dia + "/" + mes + "/" + anho + " " + "00:00";*/
    $('#id-date-picker').datepicker({
        //format: 'DD/MM/YYYY HH:mm',
        format: "dd/mm/yyyy",
        language: "es-ES",
        startView: 2,
        minViewMode: 2


                //startDate: fechaDesde,
                //useCurrent: false,
                //minDate: new Date()
    });
});


