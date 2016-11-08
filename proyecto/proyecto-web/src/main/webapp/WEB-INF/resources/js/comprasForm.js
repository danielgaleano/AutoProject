$(document).ready(function(data) {

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

//            var jqXHR = $.get(CONTEXT_ROOT + "/proveedores/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc", function(response, textStatus, jqXHR) {
//                var sel = '<option value="">Seleccione opcion</option>';
//                $.each(response.retorno, function() {
//                    if (this['id'] === pedido['proveedor.id']) {
//                        sel += '<option value="' + this['id'] + '" selected>' + this['nombre'] + '</option>'; // label and value are returned from Java layer
//                    } else {
//                        sel += '<option value="' + this['id'] + '">' + this['nombre'] + '</option>'; // label and value are returned from Java layer
//                    }
//
//                });
//                $('#proveedor').append(sel);
//            });

            $('#idPedido').val(pedido.id);
            $('#ruc').val(pedido['proveedor.ruc']);
            $('#nombre').val(pedido['proveedor.nombre']);
            $('#direccion').val(pedido['proveedor.direccion']);
            $('#telefono').val(pedido['proveedor.telefono']);
            $('#montoTotal').val(pedido.total);
            $('#id-date-picker').val(pedido.fechaEntrega);

        }
    });

    $("#credito").click(function() {
        if (this.checked) { //chequear status del select
            $("#formCredito").show();
        }
    });
    $("#contado").click(function() {
        if (this.checked) { //chequear status del select
            $("#formCredito").hide();
        }
    });
    $("#general").click(function() {
        if (this.checked) { //chequear status del select
            $("#formDescuento").show();
        }
    });
    $("#detallado").click(function() {
        if (this.checked) { //chequear status del select
            $("#formDescuento").hide();
        }
    });

});



function compraForm(id, action) {

    if (action !== "CREAR") {
        $('#cliente').attr("disabled", true);
        $('#id-date-picker').attr("disabled", true);
        $('#observacion').attr("disabled", true);
    }

    if (id !== null && id !== "") {
        var jqXHR = $.get(CONTEXT_ROOT + "/compras/" + id, function(response, textStatus, jqXHR) {
            if (response.error === true) {
                $('#mensaje').append('<div class="alert alert-error">'
                        + '<button class="close" data-dismiss="alert" type="button"'
                        + '><i class="fa  fa-remove"></i></button>'
                        + '<strong>Error! </strong>'
                        + response.mensaje
                        + '</div>');

            } else {
                var compra = response.data;

                var jqXHR = $.get(CONTEXT_ROOT + "/clientes/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc", function(response, textStatus, jqXHR) {
                    var sel = '<option value="">Seleccione opcion</option>';
                    $.each(response.retorno, function() {
                        if (this['id'] === compra['cliente.id']) {
                            sel += '<option value="' + this['id'] + '" selected>' + this['nombre'] + '</option>'; // label and value are returned from Java layer
                        } else {
                            sel += '<option value="' + this['id'] + '">' + this['nombre'] + '</option>'; // label and value are returned from Java layer
                        }

                    });
                    $('#cliente').append(sel);
                });

                $('#idCompra').val(compra.id);
                $('#factura').val(compra.factura);
                $('#ruc').val(compra.ruc);
                $('#nombre').val(compra.nombre);
                $('#direccion').val(compra.direccion);
                $('#montoTotal').val(compra.total);
                $('#telefono').val(compra.telefono);
                $('#descuento').val(compra.descuento);
                $('#montoDescuento').val(compra.montoDescuento);
                $('#cantCuotas').val(compra.cantCuotas);
                $('#montoCuota').val(compra.montoCuota);
                $('#interes').val(compra.interes);
                $('#montoInteres').val(compra.montoInteres);
                $('#interesMora').val(compra.interesMora);
                $('#entrega').val(compra.entrega);
                $('#saldo').val(compra.saldo);
                $('#neto').val(compra.neto);
                $('#id-date-picker-compra').val(compra.fechaCompra);
                $('#id-date-picker-cuota').val(compra.fechaCuota);

            }
        });
    } else {

        var jqXHR = $.get(CONTEXT_ROOT + "/clientes/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc", function(response, textStatus, jqXHR) {
            var sel = '<option value="">Seleccione opcion</option>';
            $.each(response.retorno, function() {
                sel += '<option value="' + this['id'] + '">' + this['nombre'] + '</option>'; // label and value are returned from Java layer
            });
            $('#cliente').append(sel);
        });

        var codigo = randomString(7, "COD");
        $('#codigo').val(codigo);
    }


}
            