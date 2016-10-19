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
                var pedido = response.data;
                $('#idPedido').val(pedido.id);
                $('#codigo').val(pedido.codigo);
                $('#montoTotal').val(pedido.total);
                $('#cantidadAprobados').val(pedido.cantidadAprobados);
                $('#cantidadTotal').val(pedido.cantidadTotal);
                $('#observacion').val(pedido.observacion);
                $('#proveedor').val(pedido.proveedor.nombre);
                $('#id-date-picker').val(pedido.fechaEntrega);
                
                if(action !== "CREAR"){
                    $('#proveedor').attr("disabled",true);
                    $('#id-date-picker').attr("disabled",true);
                    $('#observacion').attr("disabled",true);
                }
                
            }
        });
    }else{
        var codigo = randomString(5, "COD");
        $('#codigo').val(codigo);
    }


}
            