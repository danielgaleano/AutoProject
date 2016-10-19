function pedidoForm(id, action) {

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

                var jqXHR = $.get(CONTEXT_ROOT + "/proveedores/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc", function(response, textStatus, jqXHR) {
                    var sel = '<option value="">Seleccione opcion</option>';
                    $.each(response.retorno, function() {
                        if (this['id'] === pedido.proveedor.id) {
                            sel += '<option value="' + this['id'] + '" selected>' + this['nombre'] + '</option>'; // label and value are returned from Java layer
                        } else {
                            sel += '<option value="' + this['id'] + '">' + this['nombre'] + '</option>'; // label and value are returned from Java layer
                        }

                    });
                    $('#proveedor').append(sel);
                });

                var pedido = response.data;
                $('#idPedido').val(pedido.id);
                $('#codigo').val(pedido.codigo);
                $('#montoTotal').val(pedido.total);
                $('#cantidadAprobados').val(pedido.cantidadAprobados);
                $('#cantidadTotal').val(pedido.cantidadTotal);
                $('#observacion').val(pedido.observacion);
                $('#proveedor').val(pedido.proveedor.id);
                $('#id-date-picker').val(pedido.fechaEntrega);

                if (action !== "CREAR") {
                    $('#proveedor').attr("disabled", true);
                    $('#id-date-picker').attr("disabled", true);
                    $('#observacion').attr("disabled", true);
                }

            }
        });
    } else {
        
        var jqXHR = $.get(CONTEXT_ROOT + "/proveedores/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc", function(response, textStatus, jqXHR) {
            var sel = '<option value="">Seleccione opcion</option>';
            $.each(response.retorno, function() {
                sel += '<option value="' + this['id'] + '">' + this['nombre'] + '</option>'; // label and value are returned from Java layer
            });
            $('#proveedor').append(sel);
        });
        
        var codigo = randomString(7, "COD");
        $('#codigo').val(codigo);
    }


}
            