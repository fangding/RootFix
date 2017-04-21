package com.pudding.fixcore.runtime.utility;

import com.pudding.fixcore.runtime.io.SizeOf;

import java.lang.reflect.Field;

/**
 *
 * Mapping a C/C++ Struct in memory.
 *
 * @see StructMember
 * @author Pudding
 * @version 1.0
 */
public class Struct {

    public static final int UINT_16 = 2;
    public static final int UINT_32 = 4;

    protected long structAddress;

    public Struct(long structAddress) {
        this.structAddress = structAddress;
        applyElement();
    }

    private void applyElement() {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            StructMapping structMapping = field.getAnnotation(StructMapping.class);
            if (structMapping != null) {
                int length = structMapping.length();
                int offset = structMapping.offset();
                if (offset == -1) {
                    throw new IllegalStateException("StructMapping in " + getClass() + " named " + field.getName() + " must declare the offset.");
                }
                if (length == -1) {
                    length = SizeOf.INT;
                }
                StructMember structVar = new StructMember(structAddress, offset, length);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                try {
                    field.set(this, structVar);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage());
                }

            }
        }
    }

    public long getAddress() {
        return structAddress;
    }

    public void write(byte[] data) {
        write(data, 0);
    }
    public void write(byte[] data,int offset) {
        Memory.write(structAddress + offset,data);
    }


}
