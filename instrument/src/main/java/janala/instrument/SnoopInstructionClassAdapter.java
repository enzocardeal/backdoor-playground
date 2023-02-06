
package janala.instrument;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static janala.instrument.Utils.getAllLines;


public class SnoopInstructionClassAdapter extends ClassVisitor {
  private final String className;
  private String superName;

  public SnoopInstructionClassAdapter(ClassVisitor cv, String className) {
    super(Opcodes.ASM8, cv);
    this.className = className;
  }

  @Override
  public void visit(int version,
                    int access,
                    String name,
                    String signature,
                    String superName,
                    String[] interfaces) {
    assert name.equals(this.className);
    this.superName = superName;
    cv.visit(version, access, name, signature, superName, interfaces);
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, 
      String signature, String[] exceptions) {
    MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
    List<String> scope = new ArrayList<String>();
    List<String> ignoreList = new ArrayList<String>();

    boolean inScope = false;
    boolean ignore = false;

    try {
      scope.addAll(getAllLines("scope.out"));
      ignoreList.addAll(getAllLines("ignore.out"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    for(String scopeItem : scope){
      inScope = className.contains(scopeItem);
      if(inScope){
        break;
      }
    }

    for(String ignoreItem : ignoreList){
      ignore = className.contains(ignoreItem);
      if(ignore){
        break;
      }
    }

//    if (mv != null && !methodAllowed && inScope && !ignore && !className.contains("edu/berkeley/cs/jqf/instrument/")) {
    if (mv != null  && inScope && !ignore && !className.contains("edu/berkeley/cs/jqf/instrument/")) {
      return new SnoopInstructionMethodAdapter(mv, className, name, desc, superName,
          GlobalStateForInstrumentation.instance);
    }
    else if(mv!=null){
      return mv;
    }
    return null;
  }
}
