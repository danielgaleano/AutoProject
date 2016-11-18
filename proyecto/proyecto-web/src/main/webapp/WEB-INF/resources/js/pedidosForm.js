function pedidoForm(id, action) {

    $(".date-picker").datepicker('setDate', new Date());
    $('.date-picker').datepicker('option', 'dateFormat', 'yyyy-mm-dd');

    if (action !== "CREAR") {
        $('#proveedor').attr("disabled", true);
        $('#id-date-picker').attr("disabled", true);
        $('#observacion').attr("disabled", true);
    }

    if (id !== null && id !== "") {
        var jqXHR = $.get(CONTEXT_ROOT + "/pedidos/" + id, function(response, textStatus, jqXHR) {
            if (response.error === true) {
                $('#mensaje').append('<div class="alert alert-error">'
                        + '<button class="close" data-dismiss="alert" type="button"'
                        + '><i class="fa  fa-remove"></i></button>'
                        + '<strong>Error! </strong>'
                        + response.mensaje
                        + '</div>');

            } else {
                var pedido = response.data;

                $('#proveedor').searchableOptionList({
                    data: CONTEXT_ROOT + "/proveedores/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc",
                    converter: function(sol, rawDataFromUrl) {
                        var solData = [];
                        $.each(rawDataFromUrl.retorno, function() {

                            var select = false;
                            if (this['id'] === pedido['proveedor.id']) {
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
                             $('#idProveedor').val(a.getSelection()[0].value);
                        }
                    }
                });


                $('#idPedido').val(pedido.id);
                $('#codigo').val(pedido.codigo);
                $('#montoTotal').val(pedido.total);
                $('#cantidadAprobados').val(pedido.cantidadAprobados);
                $('#cantidadTotal').val(pedido.cantidadTotal);
                $('#observacion').val(pedido.observacion);
                $('#id-date-picker').val(pedido.fechaEntrega);


            }
        });
    } else {

        $('#proveedor').searchableOptionList({
            data: CONTEXT_ROOT + "/proveedores/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc",
            converter: function(sol, rawDataFromUrl) {
                var solData = [];
                $.each(rawDataFromUrl.retorno, function() {
                    var aSingleOptionItem = {
                        // required attributes
                        "type": "option",
                        "label": this['nombre'],
                        "value": this['id'],
                        // optional attributes
                        //"selected": false
                    };
                    solData.push(aSingleOptionItem);
                });
                return solData;
            },
            maxHeight: '220px',
            events: {
                onChange: function(a) {
                    $('#idProveedor').val(a.getSelection()[0].value);
                }
            }
        });
        var codigo = randomString(7, "COD");
        $('#codigo').val(codigo);
    }


}
            