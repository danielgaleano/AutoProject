
$(document).ready(function(data) {
    $("#interes").keypress(function(e) {
        setTimeout(function() {
            if ($.isNumeric(e.key) || e.key === 'Backspace') {
                $("#neto").val("0");
                var monto = parseInt($("#monto").val()) + parseInt($("#netoOculto").val());
                console.log(monto);
                var neto = parseInt($("#interes").val()) + parseInt($("#netoOculto").val());
                console.log(neto);
                $("#neto").val(neto);

            } else {
                $.messager.alert('Error!!', 'Debe ingresar un valor numerico!!!');
            }
        }, 0);

    });

    $("#importePagar").keypress(function(e) {
        setTimeout(function() {
            if ($.isNumeric(e.key) || e.key === 'Backspace') {

                var monto = $("#neto").val();
                var montoCuota = $("#importePagar").val() - monto;
                $("#vuelto").val(montoCuota);

            } else {
                $.messager.alert('Error!!', 'Debe ingresar un valor numerico!!!');
            }
        }, 0);

    });

    var isEditarInline = false;
    var isStatus = true;

    var permisoActivar = parseBolean($(this).find('.tablactivate-permiso').text());
    var permisoDesactivar = parseBolean($(this).find('.tabldelete-permiso').text());
    var permisoEditar = parseBolean($(this).find('.tabledit-permiso').text());
    var permisoVisualizar = parseBolean($(this).find('.tablvisualizar-permiso').text());
    var permisoDetalle = parseBolean($(this).find('.tabladd-permiso').text());

    var grid_selector = "#grid";
    var pager_selector = "#grid-pager";


    $(window).on('resize.jqGrid', function() {
        setTimeout(function() {
            $(grid_selector).jqGrid('setGridWidth', $(".content").width());

        }, 0);
    });
    $(grid_selector).jqGrid({
        url: CONTEXT_ROOT + '/cobros/listar',
        datatype: 'json',
        mtype: 'GET',
        height: 150,
        hidegrid: false,
        rownumbers: true,
        width: 1050,
        colNames: ['ID', 'NRO. FACTURA', 'FORMA PAGO', 'CUOTA PENDIENTE', 'MONTO CUOTA', 'TIPO DESCUENTO', 'NETO', 'CLIENTE', 'TELF. CLIENTE', ''],
        colModel: [
            {name: 'id', index: 'id', key: true, hidden: true, width: 60, sorttype: "int", editable: false},
            {name: 'nroFactura', index: 'nroFactura', width: 90, editable: false},
            {name: 'formaPago', index: 'formaPago', width: 90, editable: false},
            {name: 'cuota', index: 'cuota', formatter: 'integer', width: 90, editable: false},
            {name: 'montoCuota', index: 'montoCuota', formatter: 'number', width: 90, editable: false},
            {name: 'tipoDescuento', index: 'tipoDescuento', width: 150, editable: true},
            {name: 'neto', index: 'neto', width: 90, formatter: 'number', sortable: false},
            {name: 'cliente.nombre', index: 'cliente.nombre', width: 90, sortable: false},
            {name: 'cliente.telefono', index: 'cliente.telefono', width: 90, sortable: false},
            {name: 'act', index: 'act', width: 160, fixed: true, sortable: false, resize: false,
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
            estado: action,
            todos: false
        },
        onSelectRow: function(id) {
            var jqXHR = $.get(CONTEXT_ROOT + "/cobros/" + id, function(response, textStatus, jqXHR) {

                if (response.error) {
                    $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                            + '<button class="close" data-dismiss="alert" type="button"'
                            + '><i class="fa  fa-remove"></i></button>'
                            + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                            + response.mensaje
                            + '</div>');
                } else {
                    var pago = response.data;
                    // $('.ui-jqdialog-titlebar-close').trigger('click');
                    $('#idVenta').val(pago.idCompra);
                    $('#docPagar').val(pago.idDocPagar);
                    $('#nroFactura').val(pago.nroFactura);
                    $('#montoTotal').val(pago.neto);

                    $('#diasMoraSaldo').val(pago.diasMoraSaldo);
                    $('#diasMoraCuota').val(pago.diasMoraCuota);

                    $('#saldo').val(pago.saldo);
                    $('#interes').val(pago.montoInteres);

                    if (pago.idProveedor !== null && pago.idProveedor !== "") {

                        $('#idCliente').val(pago.idProveedor);
                        $('#validation-formProvee').find('.tableusuario-input').attr("disabled", true);
                        $('#buttonOption').hide();

                        var jqXHR = $.get(CONTEXT_ROOT + "/clientes/" + pago.idProveedor, function(response, textStatus, jqXHR) {

                            if (response.error) {
                                $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                                        + '<button class="close" data-dismiss="alert" type="button"'
                                        + '><i class="fa  fa-remove"></i></button>'
                                        + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                                        + response.mensaje
                                        + '</div>');
                            } else {

                                var cliente = response.data;

                                $('#idCliente').val(cliente.id);
                                $('#idEmpleo').val(cliente['empleo.id']);
                                $('#idContacto').val(cliente['contacto.id']);
                                $('#ruc').val(cliente.documento);
                                $('#nombre').val(cliente.nombre);
                                $('#id-date-picker-cliente').val(cliente.fechaNacimiento);
                                $('#sexo').val(cliente.sexo);
                                $('#pais').val(cliente.pais);
                                $('#email').val(cliente.email);
                                $('#telefono').val(cliente.telefono);
                                $('#telefonoMovil').val(cliente.telefonoMovil);
                                $('#direccion').val(cliente.direccion);
                                $('#comentario').val(cliente.comentario);
                                $('#nombreEmpresa').val(cliente['empleo.nombreEmpresa']);
                                $('#actividad').val(cliente['empleo.actividad']);
                                $('#cargo').val(cliente['empleo.cargo']);
                                $('#antiguedad').val(cliente['empleo.antiguedad']);
                                $('#salario').val(cliente['empleo.salario']);
                                $('#telefonoEmpleo').val(cliente['empleo.telefono']);
                                $('#direccionEmpleo').val(cliente['empleo.direccion']);
                                $('#comentarioEmpleo').val(cliente['empleo.comentario']);
                                $('#movilContacto').val(cliente['contacto.movil']);
                                $('#documentoContacto').val(cliente['contacto.documento']);
                                $('#nombreContacto').val(cliente['contacto.nombre']);
                                $('#cargoContacto').val(cliente['contacto.cargo']);
                                $('#telefonoContacto').val(cliente['contacto.telefono']);
                                $('#emailContacto').val(cliente['contacto.email']);
                                $('#comentarioContacto').val(cliente['contacto.comentario']);


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

                
                    
                    if (pago.idcompra !== null && pago.idCompra !== "") {

                        $('#idVenta').val(pago.idCompra);
                        $('#validation-formProvee').find('.tableusuario-input').attr("disabled", true);
                        $('#buttonOption').hide();

                        var jqXHR = $.get(CONTEXT_ROOT + "/ventas/" + pago.idCompra, function(response, textStatus, jqXHR) {

                            if (response.error) {
                                $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                                        + '<button class="close" data-dismiss="alert" type="button"'
                                        + '><i class="fa  fa-remove"></i></button>'
                                        + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                                        + response.mensaje
                                        + '</div>');
                            } else {

                                var venta = response.data;

                                $('#idVentaPago').val(venta['id']);
                                $('#nroFacturaPago').val(venta['nroFactura']);
                                $('#id-date-pickerPago').val(pago['fechaCuota']);
                                $('#descuentoPago').val(venta['descuento']);
                                $('#interesPago').val(venta['porcentajeInteresCredito']);
                                $('#montoInteresPago').val(venta['montoInteres']);
                                $('#moraInteresPago').val(venta['moraInteres']);
                                $('#cuotasPago').val(venta['cantidadCuotas']);
                                $('#montoCuotaPago').val(venta['montoCuotas']);
                                $('#montoTotalPago').val(venta['monto']);
                                $('#entregaPago').val(venta['entrega']);
                                $('#montoDescuentoPago').val(venta['montoDescuento']);
                                $('#saldoPago').val(venta['saldo']);
                                $('#netoPago').val(venta['neto']);
                                $('#diasGraciaPago').val(venta['diasGracia']);
                                
                                if (venta['formaPago'] === 'CONTADO') {
                                    $('#contadoPago').prop("checked", true);
                                    $("#formCredito").hide();
                                    $("#tipo-descuento").show();
                                } else if (venta['formaPago'] === 'CREDITO') {
                                    $('#creditoPago').prop("checked", true);
                                    $("#generalPago").attr("disabled", true);
                                    $("#detalladoPago").attr("disabled", true);
                                    $("#tipo-descuento").hide();
                                    $("#formCredito").show();
                                    $("#formDescuento").hide();
                                }



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
                    
                    
                    $('#formaPago').val(pago.formaPago);
                    $('#formaPagoCred').val(pago.formaPago);
                    if (pago.formaPago === "CONTADO") {
                        $('#contado_dato').show();
                        $('#credito_dato').hide();
                        if (parseInt(pago.saldo) > 0 && pago.saldo !== null) {
                            $('#neto').val(pago.saldo);
                            $('#netoOculto').val(pago.saldo);
                        } else {
                            if (pago.cancelado) {
                                $('#neto').val(pago.saldo);
                                $('#netoOculto').val(pago.saldo);
                            } else {
                                $('#neto').val(pago.importePagar);
                                $('#netoOculto').val(pago.importePagar);
                            }

                        }
                        $('#monto').val(0);
                        $('#id-date-picker').val(pago.fechaCuota);
                        $('#nroCuota').hide();
                        $('#monto').hide();
                        $('#id-date-picker').hide();
                    }
                    else if (pago.formaPago === "CREDITO") {
                        $('#contado_dato').hide();
                        $('#credito_dato').show();
                        $('#monto').val(pago.monto);
                        $('#neto').val(pago.importePagar);
                        $('#netoOculto').val(pago.importePagar);
                        $('#id-date-picker').val(pago.fechaCuota);
                        $('#nroCuota').val(pago.cuota);
                        $('#nroCuota').show();
                        $('#monto').show();
                        $('#id-date-picker').show();
                    }




                }

            });
        },
        ondblClickRow: function(id) {
            var jqXHR = $.get(CONTEXT_ROOT + "/pagos/" + id, function(response, textStatus, jqXHR) {

                if (response.error) {
                    $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                            + '<button class="close" data-dismiss="alert" type="button"'
                            + '><i class="fa  fa-remove"></i></button>'
                            + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                            + response.mensaje
                            + '</div>');
                } else {
                    var pago = response.data;
                    // $('.ui-jqdialog-titlebar-close').trigger('click');
                    $('#idCompra').val(pago.idCompra);
                    $('#docPagar').val(pago.idDocPagar);
                    $('#nroFactura').val(pago.nroFactura);
                    $('#montoTotal').val(pago.neto);

                    $('#saldo').val(pago.saldo);

                    if (pago.idProveedor !== null && pago.idProveedor !== "") {

                        $('#idProveedorConta').val(pago.idProveedor);
                        $('#validation-formProvee').find('.tableusuario-input').attr("disabled", true);
                        $('#buttonOption').hide();

                        var jqXHR = $.get(CONTEXT_ROOT + "/proveedores/" + pago.idProveedor, function(response, textStatus, jqXHR) {

                            if (response.error) {
                                $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                                        + '<button class="close" data-dismiss="alert" type="button"'
                                        + '><i class="fa  fa-remove"></i></button>'
                                        + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                                        + response.mensaje
                                        + '</div>');
                            } else {

                                var proveedor = response.data;

                                $('#idProveedor').val(proveedor.id);
                                $('#ruc').val(proveedor.ruc);
                                $('#nombre').val(proveedor.nombre);
                                $('#email').val(proveedor.email);
                                $('#telefono').val(proveedor.telefono);
                                $('#direccion').val(proveedor.direccion);
                                $('#comentario').val(proveedor.comentario);
                                $('#fax').val(proveedor.fax);
                                $('#pais').val(proveedor.pais);
                                $('#ciudad').val(proveedor.ciudad);
                                $('#codigoPostal').val(proveedor.codigoPostal);
                                $('#comentario').val(proveedor.comentario);


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

                    if (pago.formaPago === "CONTADO") {
                        if (parseInt(pago.saldo) > 0 && pago.saldo !== null) {
                            $('#neto').val(pago.saldo);
                            $('#netoOculto').val(pago.saldo);
                        } else {
                            if (pago.cancelado) {
                                $('#neto').val(pago.saldo);
                                $('#netoOculto').val(pago.saldo);
                            } else {
                                $('#neto').val(pago.importePagar);
                                $('#netoOculto').val(pago.importePagar);
                            }

                        }
                        $('#monto').val(0);
                        $('#id-date-picker').val(pago.fechaCuota);
                        $('#nroCuota').hide();
                        $('#monto').hide();
                        $('#id-date-picker').hide();
                    }
                    else if (pago.formaPago === "CREDITO") {
                        $('#monto').val(pago.monto);
                        $('#neto').val(pago.importePagar);
                        $('#netoOculto').val(pago.importePagar);
                        $('#id-date-picker').val(pago.fechaCuota);
                        $('#nroCuota').val(pago.cuota);
                        $('#nroCuota').show();
                        $('#monto').show();
                        $('#id-date-picker').show();
                    }




                }

            });
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
            console.log(ids);
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
                if (isStatus) {
                    var estado = dato.activo;
                    if (estado === 'S') {
                        var labelActivo = '<span class="table-estado label label-success" value="S">Activo</span>';
                        if (isEditarInline) {

                            edit = editInlineButton(cl, permisoEditar);
                            $(grid_selector).setRowData(ids[i], {act: edit});

                        } else {

                            asignar = "";
                            visuali = visualizarButton(cl, permisoVisualizar, null);
                            //edit = editInlineButton(cl, permisoEditar);
                            editForm = detalleButton(cl, permisoDetalle, 'Realizar Compra', 'orden/compras/realizar');
                            desact = desactivarButton(cl, permisoDesactivar);
                            $(grid_selector).setRowData(ids[i], {act: ini + visuali + editForm + desact + fin});
                        }
                        $(grid_selector).setRowData(ids[i], {activo: labelActivo});
                    } else {
                        var labelInactivo = '<span class="table-estado label label-danger"  value="N" >Inactivo</span>';
                        activar = activarButton(cl, permisoActivar);
                        $(grid_selector).setRowData(ids[i], {act: ini + activar + fin});
                        $(grid_selector).setRowData(ids[i], {activo: labelInactivo});
                    }
                } else {
                    if (isEditarInline) {

                        edit = editInlineButton(cl, permisoEditar);
                        $(grid_selector).setRowData(ids[i], {act: edit});
                    } else {

                        //asignar = asigButton(cl, true);
                        visuali = visualizarButton(cl, permisoVisualizar, null);
                        editForm = editFormButton(cl, permisoEditar);
                        $(grid_selector).setRowData(ids[i], {act: ini + editForm + asignar + visuali + fin});
                    }
                }


            }
        },
        editurl: "/editar", //nothing is saved
        caption: "Ventas Pendientes",
        subGrid: true,
        subGridOptions: {
            plusicon: 'fa fa-fw fa-sort-amount-asc',
            minusicon: 'fa fa-fw fa-arrow-up'
        },
        subGridRowExpanded: function(subgrid_id, row_id) {
            // we pass two parameters
            // subgrid_id is a id of the div tag created within a table
            // the row_id is the id of the row
            // If we want to pass additional parameters to the url we can use
            // the method getRowData(row_id) - which returns associative array in type name-value
            // here we can easy construct the following
            var subgrid_table_id;
            subgrid_table_id = subgrid_id + "_t";
            $("#" + subgrid_id).html("<table id='" + subgrid_table_id + "' class='scroll'></table>");
            $("#" + subgrid_table_id).jqGrid({
                url: CONTEXT_ROOT + '/ventas/docApagar/listar?_search=false&todos=true&rows=10&page=1&sidx=&sord=asc&idVenta=' + row_id,
                datatype: 'json',
                mtype: 'GET',
                width: 650,
                colNames: ['NRO. CUOTA', 'MONTO', 'SALDO', 'FECHA VENCIMIENTO', 'ESTADO'],
                colModel: [
                    {name: "nroCuota", index: "nroCuota", width: 120, key: true},
                    {name: "monto", index: "monto", formatter: 'number', width: 130},
                    {name: "saldo", index: "saldo", formatter: 'number', width: 80, align: "right"},
                    {name: "fecha", index: "fecha", width: 120, align: "right"},
                    {name: "estado", index: "estado", width: 130, align: "right", sortable: false}
                ],
                height: '100%',
                rowNum: 60,
                sortname: 'num',
                sortorder: "asc",
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
                }
            });
        }

    });
    $(window).triggerHandler('resize.jqGrid');
    $(grid_selector).jqGrid('setGridWidth', $(".content").width());
    //navButtons
    jQuery(grid_selector).jqGrid('navGrid', pager_selector,
            {//navbar options
                edit: false,
                editicon: 'ace-icon fa fa-pencil blue',
                add: false,
                addicon: 'ace-icon fa fa-plus-circle purple',
                del: false,
                delicon: 'ace-icon fa fa-trash-o red',
                search: false,
                searchicon: 'ace-icon fa fa-search orange',
                refresh: true,
                refreshicon: 'ace-icon fa fa-refresh green',
                view: false,
                viewicon: 'ace-icon fa fa-search-plus grey',
            },
            {
                //edit record form
                //closeAfterEdit: true,
                //width: 700,
                recreateForm: true,
                beforeShowForm: function(e) {

                }
            },
    {
        //new record form
        //width: 700,
        closeAfterAdd: true,
        recreateForm: true,
        viewPagerButtons: false,
        beforeShowForm: function(e) {

        }
    },
    {
        //delete record form
        recreateForm: true,
        beforeShowForm: function(e) {

        },
        onClick: function(e) {
            //alert(1);
        }
    },
    {
        //search form
        recreateForm: true,
        afterShowSearch: function(e) {


        },
        afterRedraw: function() {
            style_search_filters($(this));
        }
        ,
        multipleSearch: true,
        /**
         multipleGroup:true,
         showQuery: true
         */
    },
            {
                //view record form
                recreateForm: true,
                beforeShowForm: function(e, p) {

                    $('#viewhdgrid').hide();
                    $('#viewcntgrid').hide();
                    $('#viewmodgrid').hide();


                    var id = e[0].elements[0].value;
                    console.log(e[0].elements[0].value);
                    var jqXHR = $.get(CONTEXT_ROOT + "/pagos/" + id, function(response, textStatus, jqXHR) {

                        if (response.error) {
                            $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                                    + '<button class="close" data-dismiss="alert" type="button"'
                                    + '><i class="fa  fa-remove"></i></button>'
                                    + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                                    + response.mensaje
                                    + '</div>');
                        } else {
                            var pago = response.data;
                            // $('.ui-jqdialog-titlebar-close').trigger('click');
                            $('#idCompra').val(pago.idCompra);
                            $('#docPagar').val(pago.idDocPagar);
                            $('#nroFactura').val(pago.nroFactura);
                            $('#montoTotal').val(pago.neto);

                            $('#saldo').val(pago.saldo);

                            if (pago.idProveedor !== null && pago.idProveedor !== "") {

                                $('#idProveedorConta').val(pago.idProveedor);
                                $('#validation-formProvee').find('.tableusuario-input').attr("disabled", true);
                                $('#buttonOption').hide();

                                var jqXHR = $.get(CONTEXT_ROOT + "/proveedores/" + pago.idProveedor, function(response, textStatus, jqXHR) {

                                    if (response.error) {
                                        $('#mensaje').append('<div class="alert alert-danger alert-dismissible">'
                                                + '<button class="close" data-dismiss="alert" type="button"'
                                                + '><i class="fa  fa-remove"></i></button>'
                                                + '<h4><strong><i class="icon fa fa-ban"></i> Error! </strong></h4>'
                                                + response.mensaje
                                                + '</div>');
                                    } else {

                                        var proveedor = response.data;

                                        $('#idProveedor').val(proveedor.id);
                                        $('#ruc').val(proveedor.ruc);
                                        $('#nombre').val(proveedor.nombre);
                                        $('#email').val(proveedor.email);
                                        $('#telefono').val(proveedor.telefono);
                                        $('#direccion').val(proveedor.direccion);
                                        $('#comentario').val(proveedor.comentario);
                                        $('#fax').val(proveedor.fax);
                                        $('#pais').val(proveedor.pais);
                                        $('#ciudad').val(proveedor.ciudad);
                                        $('#codigoPostal').val(proveedor.codigoPostal);
                                        $('#comentario').val(proveedor.comentario);


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

                            if (pago.formaPago === "CONTADO") {
                                if (parseInt(pago.saldo) > 0 && pago.saldo !== null) {
                                    $('#neto').val(pago.saldo);
                                    $('#netoOculto').val(pago.saldo);
                                } else {
                                    if (pago.cancelado) {
                                        $('#neto').val(pago.saldo);
                                        $('#netoOculto').val(pago.saldo);
                                    } else {
                                        $('#neto').val(pago.importePagar);
                                        $('#netoOculto').val(pago.importePagar);
                                    }

                                }
                                $('#monto').val(0);
                                $('#id-date-picker').val(pago.fechaCuota);
                                $('#nroCuota').hide();
                                $('#monto').hide();
                                $('#id-date-picker').hide();
                            }
                            else if (pago.formaPago === "CREDITO") {
                                $('#monto').val(pago.monto);
                                $('#neto').val(pago.importePagar);
                                $('#netoOculto').val(pago.importePagar);
                                $('#id-date-picker').val(pago.fechaCuota);
                                $('#nroCuota').val(pago.cuota);
                                $('#nroCuota').show();
                                $('#monto').show();
                                $('#id-date-picker').show();
                            }




                        }

                    });
//                    var form = $(e[0]);
//                    form.closest('.ui-jqdialog').find('.ui-jqdialog-title').wrap('<div class="widget-header" />')
                }
            }
    )
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
            