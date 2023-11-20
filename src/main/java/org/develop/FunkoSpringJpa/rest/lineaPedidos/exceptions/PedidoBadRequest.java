package org.develop.FunkoSpringJpa.rest.lineaPedidos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PedidoBadRequest extends PedidoException{
    public PedidoBadRequest(String id) {
        super("Pedido con ID: " + id + " tiene mala solicitud");
    }
}
