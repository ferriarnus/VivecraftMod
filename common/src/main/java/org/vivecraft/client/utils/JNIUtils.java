package org.vivecraft.client.utils;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.libffi.FFICIF;
import org.lwjgl.system.libffi.FFIType;
import org.lwjgl.system.libffi.LibFFI;
import org.vivecraft.client_vr.settings.VRSettings;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JNIUtils {

    private static final Map<String, FFIInfo> FFIs = new HashMap<>();

    private static FFIInfo get(String format) {
        FFIInfo info = FFIs.get(format);
        //VRSettings.LOGGER.warn("calling: {}", Thread.currentThread().getStackTrace()[3]);
        if (info == null) {
            String[] parameters = format.split("_");
            FFICIF cif = FFICIF.malloc();
            PointerBuffer argTypes = null;
            if (!parameters[0].isEmpty()) {
                argTypes = MemoryUtil.memAllocPointer(parameters[0].length());
                for (int i = 0; i < parameters[0].length(); i++) {
                    argTypes.put(getType(parameters[0].charAt(i)));
                }
                argTypes.flip();
            }
            int ret = LibFFI.ffi_prep_cif(cif, LibFFI.FFI_DEFAULT_ABI, getType(parameters[1].charAt(0)),
                argTypes);
            if (ret != LibFFI.FFI_OK) {
                throw new RuntimeException("FFI error: " + ret);
            }
            info = new FFIInfo(cif, parameters[0].toCharArray(), argTypes);
            FFIs.put(format, info);
        }
        return info;
    }

    public static float callF(String signature, long __functionAddress, Object... args) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FFIInfo info = get(signature);

            PointerBuffer pointers = getPointers(stack, info.args, args);

            ByteBuffer returnValue = stack.malloc(Float.BYTES);

            LibFFI.ffi_call(info.cif, __functionAddress, returnValue, pointers);

            return returnValue.getFloat(0);
        }
    }

    public static int callI(String signature, long __functionAddress, Object... args) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FFIInfo info = get(signature);

            PointerBuffer pointers = getPointers(stack, info.args, args);

            ByteBuffer returnValue = stack.malloc(Integer.BYTES);

            LibFFI.ffi_call(info.cif, __functionAddress, returnValue, pointers);

            return returnValue.getInt(0);
        }
    }

    public static long callJ(String signature, long __functionAddress, Object... args) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FFIInfo info = get(signature);

            PointerBuffer pointers = getPointers(stack, info.args, args);

            ByteBuffer returnValue = stack.malloc(Long.BYTES);

            LibFFI.ffi_call(info.cif, __functionAddress, returnValue, pointers);

            return returnValue.getLong(0);
        }
    }

    public static void callV(String signature, long __functionAddress, Object... args) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FFIInfo info = get(signature);

            PointerBuffer pointers = getPointers(stack, info.args, args);

            LibFFI.ffi_call(info.cif, __functionAddress, null, pointers);
        }
    }

    public static boolean callZ(String signature, long __functionAddress, Object... args) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FFIInfo info = get(signature);

            PointerBuffer pointers = getPointers(stack, info.args, args);

            ByteBuffer returnValue = stack.malloc(Long.BYTES);

            LibFFI.ffi_call(info.cif, __functionAddress, returnValue, pointers);

            return returnValue.get(0) == 1;
        }
    }

    private static PointerBuffer getPointers(MemoryStack stack, char[] types, Object[] args) {
        if (types.length > 0 && args == null || (args != null && types.length != args.length)) {
            throw new IllegalArgumentException("arguments needed but not enough supplied! types:" + Arrays.toString(types) + ", args:" + (args == null ? "null" : Arrays.toString(Arrays.stream(args).map(o -> o.getClass().getSimpleName()).toArray())));
        }
        if (args == null) {
            return null;
        }
        PointerBuffer pointers = stack.mallocPointer(args.length);
        for (int i = 0; i < args.length; i++) {
            switch (types[i]) {
                case 'I', 'U' -> pointers.put(stack.ints((Integer) args[i]));
                case 'J' -> pointers.put(stack.longs((Long) args[i]));
                case 'F' -> pointers.put(stack.floats((Float) args[i]));
                case 'S' -> pointers.put(stack.shorts((Short) args[i]));
                case 'P' -> pointers.put(stack.pointers((Long) args[i]).address());
                case 'Z' ->
                    pointers.put(stack.bytes((Boolean) args[i] ? (byte) 1 : (byte) 0));
            }
        }
        pointers.flip();
        return pointers;
    }

    private static FFIType getType(char c) {
        return switch (c) {
            case 'I' -> LibFFI.ffi_type_sint32;
            case 'U' -> LibFFI.ffi_type_uint32;
            case 'J' -> LibFFI.ffi_type_uint64;
            case 'P' -> LibFFI.ffi_type_pointer;
            case 'F' -> LibFFI.ffi_type_float;
            case 'Z' -> LibFFI.ffi_type_uint8;
            case 'S' -> LibFFI.ffi_type_uint16;
            default -> throw new IllegalArgumentException("unknown parameter type: " + c);
        };
    }

    public static void free() {
        for (FFIInfo info : FFIs.values()) {
            info.cif.free();
            info.buffer.free();
        }
    }

    private record FFIInfo(FFICIF cif, char[] args, PointerBuffer buffer) {}
}
