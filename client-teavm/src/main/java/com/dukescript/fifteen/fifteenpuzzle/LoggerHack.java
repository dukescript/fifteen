package com.dukescript.fifteen.fifteenpuzzle;

import java.util.List;
import java.util.logging.Logger;
import org.teavm.diagnostics.Diagnostics;
import org.teavm.model.BasicBlock;
import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ClassReaderSource;
import org.teavm.model.Instruction;
import org.teavm.model.MethodHolder;
import org.teavm.model.Program;
import org.teavm.model.instructions.EmptyInstruction;
import org.teavm.model.instructions.InvokeInstruction;

/**
 *
 * @author Alexey Andreev
 */
public class LoggerHack implements ClassHolderTransformer {
    @Override
    public void transformClass(ClassHolder cls, ClassReaderSource innerSource, Diagnostics diagnostics) {
        for (MethodHolder method : cls.getMethods()) {
            Program program = method.getProgram();
            if (program != null) {
                transformProgram(program);
            }
        }
    }

    private void transformProgram(Program program) {
        for (int i = 0; i < program.basicBlockCount(); ++i) {
            BasicBlock block = program.basicBlockAt(i);
            List<Instruction> instructions = block.getInstructions();
            for (int j = 0; j < instructions.size(); ++j) {
                Instruction insn = instructions.get(j);
                if (!(insn instanceof InvokeInstruction)) {
                    continue;
                }
                InvokeInstruction invocation = (InvokeInstruction)insn;
                if (invocation.getMethod().getClassName().equals(Logger.class.getName())) {
                    instructions.set(j, new EmptyInstruction());
                }
            }
        }
    }
}
