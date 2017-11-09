package com.mercadopago.mocks;

import com.mercadopago.model.Instruction;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.utils.ResourcesUtil;

/**
 * Created by vaserber on 11/2/17.
 */

public class Instructions {

    private Instructions() {

    }

    public static Instruction getRapipagoInstruction() {
        String json = ResourcesUtil.getStringResource("instructions_rapipago.json");
        return JsonUtil.getInstance().fromJson(json, Instruction.class);
    }

    public static Instruction getBoletoInstruction() {
        String json = ResourcesUtil.getStringResource("instructions_boleto.json");
        return JsonUtil.getInstance().fromJson(json, Instruction.class);
    }
}
