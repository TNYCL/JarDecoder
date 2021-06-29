package com.decompiler.bytecode.opcode;

import java.util.List;

public interface DecodedSwitch {

    List<DecodedSwitchEntry> getJumpTargets();
}
