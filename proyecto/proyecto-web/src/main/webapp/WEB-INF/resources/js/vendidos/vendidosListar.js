
$(document).ready(function(data) {

    var isEditarInline = false;
    var isStatus = true;

    //var permisoActivar = parseBolean($(this).find('.tablactivate-permiso').text());
    //var permisoDesactivar = parseBolean($(this).find('.tabldelete-permiso').text());
    var permisoEditar = parseBolean($(this).find('.tabledit-permiso').text());
    var permisoVisualizar = parseBolean($(this).find('.tablvisualizar-permiso').text());
    var permisoAsignar = parseBolean($(this).find('.tablasignar-permiso').text());

    var grid_selector = "#grid";
    var pager_selector = "#grid-pager";


    $(window).on('resize.jqGrid', function() {
        setTimeout(function() {
            $(grid_selector).jqGrid('setGridWidth', $(".content").width());

        }, 0);
    }),
            $(grid_selector).jqGrid({
        url: CONTEXT_ROOT + '/vehiculos/vendidos/listar',
        datatype: 'json',
        mtype: 'GET',
        height: 310,
        hidegrid: false,
        rownumbers: true,
        //width: $(".content").width(),
        colNames: ['ID', 'ID_VEHICULO', 'CHASIS','', 'TIPO VEHICULO', 'MARCA', 'MODELO', 'ANHO', 'COLOR', 'TRANSMISION', 'PRECIO DE COSTO', 'PRECIO DE VENTA', ''],
        colModel: [
            {name: 'id', index: 'id', key: true, hidden: true, width: 60, sorttype: "int", editable: false},
            {name: 'codigo', index: 'codigo', hidden: true, width: 60, editable: false},
            {name: 'chasis', index: 'chasis', hidden: true, width: 60, editable: false},
            {name: 'cancelado', index: 'cancelado', hidden: true, width: 60, editable: false},
            {name: 'tipo.nombre', index: 'tipo.nombre', width: 100, editable: false},
            {name: 'marca.nombre', index: 'marca.nombre', width: 100, editable: false},
            {name: 'modelo.nombre', index: 'modelo.nombre', width: 100, editable: false},
            {name: 'anho', index: 'anho', width: 60, editable: false},
            {name: 'color', index: 'color', width: 70, sortable: false},
            {name: 'transmision', index: 'transmision', width: 90, sortable: false},
            {name: 'precioCosto', index: 'precioCosto', formatter: 'number', width: 90, sortable: false},
            {name: 'precioVenta', index: 'precioVenta', width: 90, sortable: false},
            {name: 'act', index: 'act', width: 180, fixed: true, sortable: false, resize: false,
                //               formatter: 'actions',
                formatoptions: {
                    onError: function(jqXHR, textStatus, errorThrwn) {
                        if (textStatus.status !== 200) {
                            $('#mensaje').append('<div class="alert alert-error">'
                                    + '<button class="close" data-dismiss="alert" type="button"'
                                    + '><i class="fa  fa-remove"></i></button>'
                                    + '<strong>Error ' + textStatus.status + ' ! </strong>'
                                    + 'Error al editar el registro'
                                    + '</div>');
                        }

                    },
                    onSuccess: function(data) {
                        if (data.responseJSON.error === true) {
                            $('#mensaje').append('<div class="alert alert-error">'
                                    + '<button class="close" data-dismiss="alert" type="button"'
                                    + '><i class="fa  fa-remove"></i></button>'
                                    + '<strong>Error! </strong>'
                                    + data.responseJSON.mensaje
                                    + '</div>');

                        } else {
                            $('#mensaje').append('<div class="alert alert-info alert-dismissible fade in">'
                                    + '<button type="button" class="close" data-dismiss="alert"'
                                    + 'aria-label="Close"><i class="fa  fa-remove"></i></button>'
                                    + '<strong>Exito! </strong>'
                                    + data.responseJSON.mensaje
                                    + '</div>');
                            $(grid_selector).trigger('reloadGrid');

                        }
                    }

                }
            }
        ],
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 20, 30],
        pager: pager_selector,
        altRows: true,
        loadtext: "Cargando...",
        emptyrecords: "No se encontaron datos.",
        pgtext: "Pagina {0} de {1}",
        postData: {
            atributos: "id,nombre",
            filters: null,
            todos: false
        },
        serializeRowData: function(data) {
            console.log(data);
            return data;
        },
        jsonReader: {
            root: 'retorno',
            page: 'page',
            total: 'total',
            records: function(obj) {
                if (obj.retorno !== null) {
                    return obj.retorno.length;
                } else {
                    return 0;
                }
            }
        },
        //toppager: true,
        loadComplete: function() {
            var table = this;
            setTimeout(function() {
                //styleCheckbox(table);

                //updateActionIcons(table);
                updatePagerIcons(table);
                //enableTooltips(table);
            }, 0);
        },
        gridComplete: function() {
            var ids = $(grid_selector).getDataIDs();
            var datos = $(grid_selector).getGridParam();
            //console.log($(grid_selector));
            for (var i = 0; i < ids.length; i++) {
                var cl = ids[i];
                var dato = $(grid_selector).jqGrid('getRowData', cl);
                var asignar = '';
                var editForm = '';
                var ce = '';
                var visuali = '';
                var desact = '';
                var activar = '';
                var ini = '<div style="float: none;" class="btn-group btn-group-sm">';
                var fin = '</div>';
                var compra = '';
                var venta = '';
                var devolucion = '';
                if (isStatus) {
                    var content = window.location.href;
                    var compraContent = window.location.hostname + 'proyecto';
                    var ventaContent = window.location.hostname + 'proyecto';
                    if (permisoEditar) {
                        editForm = '<a onmouseout="jQuery(this).removeClass(' + "'ui-state-hover'" + ')"'
                                + ' onmouseover="jQuery(this).addClass(' + "'i-state-hover'" + ');" href="' + content + '/visualizar/' + cl + '"'
                                + '  class=" btn btn-xs btn-info" style="float:left;cursor:pointer;" title="Ver datos">'
                                + ' <span class="fa fa-fw fa-wrench"></span></a>';
                    }
                    asignar = "";
                    compra = '<a onmouseout="jQuery(this).removeClass(' + "'ui-state-hover'" + ')"'
                                + ' onmouseover="jQuery(this).addClass(' + "'i-state-hover'" + ');" onclick=popCompra(' + cl + ');'
                                + '  class=" btn btn-xs btn-info" style="float:left;cursor:pointer;" title="Ver compra">'
                                + ' <span class="fa fa-fw fa-cart-plus"></span></a>';
                        
                    venta = '<a onmouseout="jQuery(this).removeClass(' + "'ui-state-hover'" + ')"'
                                + ' onmouseover="jQuery(this).addClass(' + "'i-state-hover'" + ');" onclick=popVenta(' + cl + ');'
                                + '  class=" btn btn-xs btn-info" style="float:left;cursor:pointer;" title="Ver venta">'
                                + ' <span class="fa fa-fw fa-shopping-bag"></span></a>';
                    
                    if(dato.cancelado === false || dato.cancelado === 'false'){  
                       
                        devolucion = '<a onmouseout="jQuery(this).removeClass(' + "'ui-state-hover'" + ')"'
                                + ' onmouseover="jQuery(this).addClass(' + "'i-state-hover'" + ');" onclick=popDevolucion(' + cl + ');'
                                + '  class=" btn btn-xs btn-info" style="float:left;cursor:pointer;" title="Registrar devolucion">'
                                + ' <span class="fa fa-fw fa-exchange"></span></a>';
                        
                    }  
                    
                    //visuali = visualizarButton(cl, permisoVisualizar,null);
                    //editForm = editFormButton(cl, permisoEditar);
                    //desact = desactivarButton(cl, permisoDesactivar);
                    //$(grid_selector).setRowData(ids[i], {act: ini + editForm + asignar + visuali + desact + fin});
                    $(grid_selector).setRowData(ids[i], {act: ini + editForm + compra + venta + devolucion + asignar + visuali + fin});



                } else {
                    if (isEditarInline) {

                        edit = editInlineButton(cl, permisoEditar);
                        $(grid_selector).setRowData(ids[i], {act: edit});
                    } else {

                        //asignar = asigButton(cl, true);
                        visuali = visualizarButton(cl, permisoVisualizar, null);
                        editForm = editFormButton(cl, permisoEditar);
                        $(grid_selector).setRowData(ids[i], {act: ini + editForm + compra + venta + devolucion + asignar + visuali + fin});
                    }
                }


            }
        },
        editurl: "/editar", //nothing is saved
        caption: "Vehiculos"

    });
    $(window).triggerHandler('resize.jqGrid');
    $(grid_selector).jqGrid('setGridWidth', $(".content").width());
    $(grid_selector).jqGrid('navGrid', pager_selector, {edit: false, add: false, del: false, search: false});
});

function updatePagerIcons(table) {
    var replacement =
            {
                'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
                'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
                'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
                'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
            };
    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function() {
        var icon = $(this);
        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

        if ($class in replacement)
            icon.attr('class', 'ui-icon ' + replacement[$class]);
    });
}



function parseBolean(val) {
    if (val.toLowerCase() === 'true') {
        return true;
    } else if (val.toLowerCase() === 'false' || val.toLowerCase() === '') {
        return false;
    }

}

function popCompra(id_vehiculo) {
    $('#compra-modal').modal('show'); 
    
    var jqXHR = $.get(CONTEXT_ROOT + "/compras/obtener/" + id_vehiculo, function(data, textStatus, jqXHR) {
                if (data.error === true) {
                    $('#mensaje').append('<div class="alert alert-error">'
                            + '<button class="close" data-dismiss="alert" type="button"'
                            + '><i class="fa  fa-remove"></i></button>'
                            + '<strong>Error! </strong>'
                            + data.mensaje
                            + '</div>');

                } else {
                    var compra = data.data;
                    
                    //console.log(compra);
                    //console.log(compra['compra.nroFactura']);
                    console.log(compra['compra.proveedor.nombre']);
                    //Agregar modal que trae los datos de la compra
                    
                    $('#idCompra').val(compra['compra.id']);
                    $('#nroFactura').val(compra['compra.nroFactura']);
                    //$('#ruc').val(compra['compra.proveedor.ruc']);
                    $('#nombre').val(compra['compra.proveedor.nombre']);
                    
                    $('#date-timeDesde').val(compra['compra.fechaCuota']);
                    $('#descuento').val(compra['compra.descuento']);
                    $('#interes').val(compra['compra.porcentajeInteresCredito']);
                    $('#montoInteres').val(compra['compra.montoInteres']);
                    $('#id-date-picker').val(compra['compra.fechaCuota']);
                    
                    if (compra['compra.formaPago'] === 'CONTADO') {
                        $('#contado').prop("checked", true);
                        $("#formCredito").hide();
                        $("#tipo-descuento").show();
                    } else if (compra['compra.formaPago'] === 'CREDITO') {
                        $('#credito').prop("checked", true);
                        $("#general").attr("disabled", true);
                        $("#detallado").attr("disabled", true);
                        $("#tipo-descuento").hide();
                        $("#formCredito").show();
                        $("#formDescuento").hide();
                    }

                    if (compra['compra.tipoDescuento'] === 'GENERAL') {
                        $('#general').prop("checked", true);
                        $("#formDescuento").show();
                    } else if (compra['compra.tipoDescuento'] === 'DETALLADO') {
                        $('#detallado').prop("checked", true);
                        $("#formDescuento").hide();
                    }
                    
                    if (compra['compra.descuento'] == 0 || compra['compra.descuento'] == "" || compra['compra.descuento'] == "0" ) {
                        $("#formDescuento").hide();
                    }
                    
                    $('#moraInteres').val(compra['compra.moraInteres']);
                    $('#cuotas').val(compra['compra.cantidadCuotas']);
                    $('#montoCuota').val(compra['compra.montoCuotas']);
                    $('#montoTotal').val(compra['compra.monto']);
                    $('#entrega').val(compra['compra.entrega']);
                    $('#montoDescuento').val(compra['compra.montoDescuento']);
                    $('#saldo').val(compra['compra.saldo']);
                    $('#neto').val(compra['compra.neto']);
                    
                    var a = document.getElementById('irCompraButton');
                    a.href = CONTEXT_ROOT + "/compras/registros/visualizar/" + compra['compra.id'];
                    
                    
                }
            });
    
}


function popVenta(id_vehiculo) {
    $('#venta-modal').modal('show'); 
    
    var jqXHR = $.get(CONTEXT_ROOT + "/ventas/obtener/" + id_vehiculo, function(data, textStatus, jqXHR) {
                if (data.error === true) {
                    $('#mensaje').append('<div class="alert alert-error">'
                            + '<button class="close" data-dismiss="alert" type="button"'
                            + '><i class="fa  fa-remove"></i></button>'
                            + '<strong>Error! </strong>'
                            + data.mensaje
                            + '</div>');

                } else {
                    var venta = data.data;
                    
                    //console.log(compra);
                    //console.log(compra['compra.nroFactura']);
                    console.log(venta['venta.cliente.nombre']);
                    //Agregar modal que trae los datos de la compra
                    
                    $('#idVenta').val(venta['venta.id']);
                    $('#nroFacturaVenta').val(venta['venta.nroFactura']);
                    //$('#ruc').val(venta['venta.proveedor.ruc']);
                    $('#nombreVenta').val(venta['venta.cliente.nombre']);
                    $('#deudaPendiente').val(venta.deudaPendiente);
                    $('#date-timeDesdeVenta').val(venta['venta.fechaCuota']);
                    $('#descuentoVenta').val(venta['venta.descuento']);
                    $('#interesVenta').val(venta['venta.porcentajeInteresCredito']);
                    $('#montoInteresVenta').val(venta['venta.montoInteres']);
                    $('#id-date-pickerVenta').val(venta['venta.fechaCuota']);
                    
                    if (venta['venta.formaPago'] === 'CONTADO') {
                        $('#contadoVenta').prop("checked", true);
                        $("#formCreditoVenta").hide();
                        $("#tipo-descuentoVenta").show();
                    } else if (venta['venta.formaPago'] === 'CREDITO') {
                        $('#creditoVenta').prop("checked", true);
                        $("#generalVenta").attr("disabled", true);
                        $("#detalladoVenta").attr("disabled", true);
                        $("#tipo-descuentoVenta").hide();
                        $("#formCreditoVenta").show();
                        $("#formDescuentoVenta").hide();
                    }

                    
                    
                    if (venta['venta.descuento'] == 0 || venta['venta.descuento'] == "" || venta['venta.descuento'] == "0" ) {
                        $("#formDescuentoVenta").hide();
                    }
                    
                    $('#moraInteresVenta').val(venta['venta.moraInteres']);
                    $('#cuotasVenta').val(venta['venta.cantidadCuotas']);
                    $('#montoCuotaVenta').val(venta['venta.montoCuotas']);
                    $('#montoTotalVenta').val(venta['venta.monto']);
                    $('#entregaVenta').val(venta['venta.entrega']);
                    $('#moraInteresVenta').val(venta['venta.moraInteres']);
                    $('#diasGraciaVenta').val(venta['venta.diasGracia']);
                    $('#montoDescuentoVenta').val(venta['venta.montoDescuento']);
                    $('#saldoVenta').val(venta['venta.saldo']);
                    $('#netoVenta').val(venta['venta.neto']);
                    
                    var a = document.getElementById('irVentaButton');
                    a.href = CONTEXT_ROOT + "/ventas/visualizar/" + venta['venta.id'];
                    
                    
                }
            });
    
}


function popDevolucion(id_vehiculo) {
    $('#devolucion-modal').modal('show'); 

    var dato = $("#grid").jqGrid('getRowData', id_vehiculo);
    
    var jqXHR = $.get(CONTEXT_ROOT + "/ventas/obtener/" + id_vehiculo, function(data, textStatus, jqXHR) {
                if (data.error === true) {
                    $('#mensaje').append('<div class="alert alert-error">'
                            + '<button class="close" data-dismiss="alert" type="button"'
                            + '><i class="fa  fa-remove"></i></button>'
                            + '<strong>Error! </strong>'
                            + data.mensaje
                            + '</div>');

                } else {
                    var venta = data.data;
                    
                    //console.log(compra);
                    //console.log(compra['compra.nroFactura']);
                    //console.log(venta['venta.cliente.nombre']);
                    //Agregar modal que trae los datos de la compra
                    
                    $('#idVentaDevolucion').val(venta['venta.id']);
                    $('#idVehiculoDevolucion').val(dato.id);
                    $('#idClienteDevolucion').val(venta['venta.cliente.id']);

                    $('#nroFacturaDevolucion').val(venta['venta.nroFactura']);
                    //$('#ruc').val(venta['venta.proveedor.ruc']);
                    $('#nombreDevolucion').val(venta['venta.cliente.nombre']);
                    $('#deudaPendienteDevolucion').val(venta.deudaPendiente);
                    $('#montoTotalDevolucion').val(venta['venta.monto']);

                    $('#chasisDevolucion').val(dato.chasis);
                    $('#marcaDevolucion').val(dato['marca.nombre']);
                    $('#modeloDevolucion').val(dato['modelo.nombre']);
                    $('#costoDevolucion').val(dato.precioCosto);

                    
                    
                    //var a = document.getElementById('guardarDevolucion');
                    //a.href = CONTEXT_ROOT + "/guardar/";
                    
                    
                }
            });
    
}

$('#guardarDevolucion').on('click', function() {
    if($("#validation-formDevolucion").valid()){
        $("#validation-formDevolucion").submit();
    }
    
    
});
